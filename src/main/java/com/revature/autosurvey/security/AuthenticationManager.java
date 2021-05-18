package com.revature.autosurvey.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.revature.autosurvey.beans.TokenVerifier;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager, InitializingBean {

	private Logger log = LoggerFactory.getLogger(AuthenticationManager.class);
	@Value("${google.firebase.apikey}")
	private String firebaseKey;
	@Value("${google.firebase.credentialsjson")
	private String credentials;
	@Value("${google.firebase.serviceaccountid}")
	private String serviceAccountId;
	@Value("${google.firebase.databaseurl}")
	private String databaseUrl;

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseOptions options = FirebaseOptions.builder()
						.setCredentials(GoogleCredentials.fromStream(new FileInputStream(
								new ClassPathResource(credentials)
										.getFile())))
						.setServiceAccountId(serviceAccountId)
						.setDatabaseUrl(databaseUrl).build();
				FirebaseApp.initializeApp(options);
			}
		} catch (IOException e) {
			throw new BeanInitializationException("Unable to initialize Firebase", e);
		}
	}

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		String authToken = authentication.getCredentials().toString();
		TokenVerifier request = new TokenVerifier();
		request.setToken(authToken);
		request.setReturnSecureToken(true);
		HttpClient httpClient = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.responseTimeout(Duration.ofMillis(5000))
				.doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
						.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
		WebClient client = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
		@SuppressWarnings("rawtypes")
		Mono<Map> res = client.post()
				.uri("https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken?key=" + firebaseKey)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.retrieve()
				.bodyToMono(Map.class)
				.onErrorReturn((Map) Mono.empty());

		return res.flatMap(result -> {
			FirebaseToken decodedToken;
			try {
				decodedToken = FirebaseAuth.getInstance().verifyIdToken((String) result.get("idToken"));
			} catch (FirebaseAuthException e) {
				log.error("error thrown: ", e);
				return Mono.empty();
			}
			String userId = decodedToken.getUid();
			Map<String, Object> claims = decodedToken.getClaims();
			@SuppressWarnings("unchecked")
			List<String> rolesMap = (List<String>) claims.get("roles");
			List<GrantedAuthority> authorities = new ArrayList<>();
			for (String rolemap : rolesMap) {
				authorities.add(new SimpleGrantedAuthority(rolemap));
			}
			return Mono.just(new UsernamePasswordAuthenticationToken(userId, null, authorities));
		});

	}
}
