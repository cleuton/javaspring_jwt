package guru.desenvolvedor.curso.persistence;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.common.io.Resources;


public class ParamSingleton {
	
	private static ParamSingleton mySelf;
	private String privateKey;
	private String passphrase;
	private String jwtPrivateKeyPath;
	private String jwtPrivateKeyPassphrase;
	private String jwtPublicKeyPath;
	private long jwtValidadeSegundos;
	private int tentativasLogin;
	private long tempoLogin;
	
	private ParamSingleton() throws Exception {
		try {
			URL url = Resources.getResource("propriedades.json");
			privateKey = Resources.toString(url, StandardCharsets.UTF_8);			
		}
		catch (IllegalArgumentException iae) {
			// O resource não existe: Pegar de variável de ambiente
			String pathPrkey = System.getenv("AVISOS_PRK");
			if (pathPrkey == null) {
				throw new IllegalArgumentException("Private Key não está nos recursos e não há variável de ambiente");
			}
			privateKey = new String(Files.readAllBytes(Paths.get(pathPrkey)), StandardCharsets.UTF_8);
		}
		passphrase = System.getenv("AVISOS_KEYPHRASE");
		if (passphrase == null) {
			throw new IllegalArgumentException("Falta a variável de ambiente para o passphrase");
		}
		jwtPrivateKeyPath = System.getenv("AVISOS_JWT_PRIVATE_KEY_PATH");
		if (jwtPrivateKeyPath == null) {
			throw new IllegalArgumentException("Falta a variável de ambiente do path da private key do token JWT");
		}
		jwtPublicKeyPath = System.getenv("AVISOS_JWT_PUBLIC_KEY_PATH");
		if (jwtPublicKeyPath == null) {
			throw new IllegalArgumentException("Falta a variável de ambiente do path da public key do token JWT");
		}
		jwtPrivateKeyPassphrase = System.getenv("AVISOS_JWT_PRIVATE_KEY_PASSPHRASE");
		if (jwtPrivateKeyPassphrase == null) {
			throw new IllegalArgumentException("Falta a variável de ambiente da passphrase da private key do token JWT");
		}
		
		String validade = System.getenv("AVISOS_JWT_VALIDADE_SEGUNDOS");
		if (validade == null) {
			throw new IllegalArgumentException("Falta a variável de ambiente da validade em segundos do token JWT");
		}
		try {
			jwtValidadeSegundos = Long.parseLong(validade);
			if (jwtValidadeSegundos <=0 || jwtValidadeSegundos > 86400) {
				throw new IllegalArgumentException("Variiável de ambiente da validade em segundos do token JWT inválida. Tem que ser maior que zero e até 24 horas (86400)");
			}
		}
		catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Variiável de ambiente da validade em segundos do token JWT inválida");
		}
		
		String tlogin = System.getenv("AVISOS_TENTATIVAS_LOGIN");
		try {
			tentativasLogin = Integer.parseInt(tlogin);
			if (tentativasLogin <=0 || tentativasLogin > 5) {
				throw new IllegalArgumentException("Variável de ambiente das tentativas de login inválida. Tem que ser maior que zero e até 5");
			}
		}
		catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Variável de ambiente das tentativas de login inválida");
		}
		
		String tmpLogin = System.getenv("AVISOS_TEMPO_LOGIN_SEGUNDOS");
		try {
			tempoLogin = Long.parseLong(tmpLogin);
			if (tempoLogin <= 0) {
				throw new IllegalArgumentException("Variável de ambiente do tempo de suspensão de login inválida. Tem que ser maior que zero");
			}
		}
		catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Variável de ambiente do tempo de suspensão de login inválida");
		}
	}
	
	private static ParamSingleton getInstance() throws Exception {
		if (mySelf == null) {
			mySelf = new ParamSingleton();
		}
		return mySelf;
	}

	public static String getPrivateKey() throws Exception {
		return getInstance().privateKey;
	}

	public static String getPassphrase() throws Exception {
		return getInstance().passphrase;
	}

	public static String getJwtPrivateKeyPath() throws Exception {
		return getInstance().jwtPrivateKeyPath;
	}

	public static String getJwtPrivateKeyPassphrase() throws Exception {
		return getInstance().jwtPrivateKeyPassphrase;
	}

	public static String getJwtPublicKeyPath() throws Exception {
		return getInstance().jwtPublicKeyPath;
	}

	public static long getJwtValidadeSegundos() throws Exception {
		return getInstance().jwtValidadeSegundos;
	}

	public static int getTentativasLogin() throws Exception {
		return getInstance().tentativasLogin;
	}

	public static Long getTempoLogin() throws Exception {
		return getInstance().tempoLogin;
	}
	
}
