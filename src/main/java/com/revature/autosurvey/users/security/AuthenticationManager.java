package com.revature.autosurvey.users.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager, InitializingBean {

	private Logger log = LoggerFactory.getLogger(AuthenticationManager.class);

	@Value("${google.firebase.credentialsjson}")
	private String credentials;
	@Value("${google.firebase.serviceaccountid}")
	private String serviceAccountId;
	@Autowired
	private FirebaseUtil firebaseUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseOptions options = FirebaseOptions.builder()
						.setCredentials(GoogleCredentials
								.fromStream(new FileInputStream(new ClassPathResource(credentials).getFile())))
						.setServiceAccountId(serviceAccountId).build();
				FirebaseApp.initializeApp(options);
			}
		} catch (IOException e) {
			throw new BeanInitializationException("Unable to initialize Firebase", e);
		}
	}

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		String authToken = authentication.getCredentials().toString();

		return firebaseUtil.getDetailsFromCustomToken(authToken).flatMap(decodedToken -> {
			log.debug("token found: {}", decodedToken);
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
