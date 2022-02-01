package guru.desenvolvedor.curso.security;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import guru.desenvolvedor.curso.persistence.ParamSingleton;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class TokenProvider {

	@Autowired
	private AvisoUserDetailsService userDetailsService;

	public String getToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public boolean validateToken(String token) throws Exception {
		try {
			Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new Exception("Expired or invalid JWT token");
		}
	}

	public Authentication getAuthentication(String token) throws ExpiredJwtException, UsernameNotFoundException,
			UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, Exception {
		UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUsername(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException,
			SignatureException, IllegalArgumentException, Exception {
		return Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token).getBody().getSubject();
	}

	public String createToken(String username, String role) throws Exception {

		Claims claims = Jwts.claims().setSubject(username);
		claims.put("auth", role);

		Date agora = new Date();

		Date validade = Date
				.from(ZonedDateTime.now().plusSeconds(ParamSingleton.getJwtValidadeSegundos()).toInstant());

		return Jwts.builder().setClaims(claims).setIssuedAt(agora).setExpiration(validade)
				.signWith(SignatureAlgorithm.RS256, this.getPrivateKey())//
				.compact();
	}

	public PrivateKey getPrivateKey() throws Exception {

		// Converta sua chave SSH para RSA com o comando: 
		// ssh-keygen -p -N "" -m pem -f /path/to/key
		// Depois converta para PKCS8: 
		// openssl pkcs8 -topk8 -inform PEM -outform DER -in private_key_file  -nocrypt > pkcs8_key
		
		String privateKeyPath = ParamSingleton.getJwtPrivateKeyPath();
		byte[] keyBytes = Files.readAllBytes(Paths.get(privateKeyPath));
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

	public PublicKey getPublicKey() throws Exception {
		
		// Converta a public key do SSH: 
		/// ssh-keygen -f apendo4_rsa.pub -e -m pkcs8 > test-pkcs8.pub
		// E depois converta para x509:
		// openssl rsa -pubin -in test-pkcs8.pub -outform pem > test-x509.pem

		String publicKeyPath = ParamSingleton.getJwtPublicKeyPath();
		String sBytes = Files.readString(Path.of(publicKeyPath, "")); 
		sBytes = sBytes.replace("-----BEGIN PUBLIC KEY-----", ""); // Retira o cabeçalho
		sBytes = sBytes.replace("-----END PUBLIC KEY-----", "");   // Retira o rodapé
		sBytes = sBytes.replaceAll("\\s+","");                     // Retira os Linefeeds
		byte[] keyBytes = Base64.getDecoder().decode(sBytes);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}

	public String getPrivateKeyPassphrase() throws Exception {
		return ParamSingleton.getJwtPrivateKeyPassphrase();
	}

}
