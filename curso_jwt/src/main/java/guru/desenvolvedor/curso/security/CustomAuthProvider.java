package guru.desenvolvedor.curso.security;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthProvider implements AuthenticationProvider {
	private static Logger logger = LoggerFactory.getLogger(CustomAuthProvider.class);
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SuspendedSingleton suspendedSingleton = SuspendedSingleton.getInstance();
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		if (!name.equalsIgnoreCase("fulano") || !password.equals("senhafulano")) {
			try {
				suspendedSingleton.suspend(name, password);
			} catch (Exception e) {
				logger.error("Exception ao pegar instância de SuspendedSingleton " + e.getMessage());
				throw new BadCredentialsException(e.getMessage());
			}
			throw new BadCredentialsException("Invalid Credentials");
		}
		else {
			try {
				if (suspendedSingleton.checkSuspended(name, password)) {
					throw new BadCredentialsException("Login suspenso");
				}
			} catch (Exception e) {
				logger.error("Exception ao verificar suspensao de login " + e.getMessage());
				throw new BadCredentialsException(e.getMessage());
			}
		}
		return new UsernamePasswordAuthenticationToken(
	              name, password, new ArrayList<>());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
