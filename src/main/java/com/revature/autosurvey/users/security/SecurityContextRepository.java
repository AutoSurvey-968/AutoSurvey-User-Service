package com.revature.autosurvey.users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

	public static final String COOKIE_KEY = "token";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange swe) {
		if (swe.getRequest().getCookies().containsKey(COOKIE_KEY)
				&& swe.getRequest().getCookies().getFirst(COOKIE_KEY) != null) {
			String authCookie = swe.getRequest().getCookies().getFirst(COOKIE_KEY).getValue();
			Authentication auth = new UsernamePasswordAuthenticationToken(authCookie, authCookie);
			return this.authenticationManager.authenticate(auth)
					.map(SecurityContextImpl::new);
		} else {
			return Mono.empty();
		}
	}

}
