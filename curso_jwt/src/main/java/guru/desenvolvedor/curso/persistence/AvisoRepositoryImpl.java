package guru.desenvolvedor.curso.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Repository;

import guru.desenvolvedor.curso.security.TokenProvider;

@Repository
public class AvisoRepositoryImpl implements AvisoRepository {

	@Autowired
	private EntityManager entityManager;


	private static String PRIVATE_KEY;
	private static String PASSPHRASE;

	public AvisoRepositoryImpl() throws Exception {
		PRIVATE_KEY = ParamSingleton.getPrivateKey();
		PASSPHRASE = ParamSingleton.getPassphrase();
	}

	@Override
	public List<Aviso> findAll() {
		Query q = entityManager.createNativeQuery("SELECT id, " + "        pgp_pub_decrypt(titulo, keys.privkey,'"
				+ PASSPHRASE + "') As titulo," + "        pgp_pub_decrypt(resumo, keys.privkey,'" + PASSPHRASE
				+ "') As resumo," + "        pgp_pub_decrypt(thumb, keys.privkey,'" + PASSPHRASE + "') As thumb,"
				+ "        pgp_pub_decrypt(imagem, keys.privkey,'" + PASSPHRASE + "') As imagem,"
				+ "        pgp_pub_decrypt(texto, keys.privkey,'" + PASSPHRASE + "') As texto,"
				+ "        pgp_pub_decrypt(data, keys.privkey,'" + PASSPHRASE + "') As data " + "         "
				+ "    FROM aviso " + "        CROSS JOIN" + "            (SELECT dearmor('" + PRIVATE_KEY
				+ "') As privkey) As keys;", Aviso.class);
		List<Aviso> avisos = q.getResultList();

		return avisos;
	}

}
