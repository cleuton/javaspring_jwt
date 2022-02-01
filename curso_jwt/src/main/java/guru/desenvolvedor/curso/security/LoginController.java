package guru.desenvolvedor.curso.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class LoginController {
	
	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/login")
	public ResponseEntity<AccessToken> login(@RequestBody Usuario usuario) throws Exception {
		// Na vida real você vai utilizar um banco de dados ou um serviço OAuth
		try {
			Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuario.username, usuario.password));
			AccessToken token = new AccessToken();
			token.access_token = tokenProvider.createToken(usuario.username, "AUTENTICADO");
			return ResponseEntity.ok(token);
		} catch (AuthenticationException e) {
			AccessToken token = new AccessToken();
			token.access_token = "Credenciais invalidas";
			return ResponseEntity.status(401).body(token);
		}
	}
}
