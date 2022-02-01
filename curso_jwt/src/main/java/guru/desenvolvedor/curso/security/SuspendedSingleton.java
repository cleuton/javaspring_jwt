package guru.desenvolvedor.curso.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import guru.desenvolvedor.curso.persistence.ParamSingleton;

public class SuspendedSingleton {
	private static Logger logger = LoggerFactory.getLogger(SuspendedSingleton.class);
	private static SuspendedSingleton mySelf;
	private List<Suspended> suspendedLogins;
	
	private SuspendedSingleton() {
		this.suspendedLogins = new ArrayList<Suspended>();
	}
	
	public static SuspendedSingleton getInstance() {
		if (mySelf == null) {
			mySelf = new SuspendedSingleton();
		}
		return mySelf;
	}

	public List<Suspended> getSuspendedLogins() {
		return suspendedLogins;
	}

	public void setSuspendedLogins(List<Suspended> suspendedLogins) {
		this.suspendedLogins = suspendedLogins;
	}
	
	public void suspend(String username, String password) throws Exception {
		Usuario usu = new Usuario();
		usu.username = username;
		usu.password = password;
		Suspended suspended = new Suspended(usu);
		if (!this.suspendedLogins.contains(suspended)) {
			suspended.setTentativas(0);
			suspended.setSuspenso(false);
			this.suspendedLogins.add(suspended);
			logger.debug(String.format("Marcando login %s", username));
		}
		else {
			suspended = this.suspendedLogins.get(this.suspendedLogins.indexOf(suspended));
			if (!suspended.isSuspenso()) {
				logger.info(String.format("Nova tentativa de login de %s", username));
				suspended.setTentativas(suspended.getTentativas() + 1);
				if (suspended.getTentativas() >= ParamSingleton.getTentativasLogin()) {
					logger.error(String.format("Suspendendo login %s", username));
					suspended.setSuspenso(true);
					suspended.setTime(new Date());
				}
			}
			else {
				logger.error(String.format("Atualizando bloqueio de login %s", username));
				suspended.setTime(new Date());
			}
		}
	}
	
	public boolean checkSuspended(String username, String password) throws Exception {
		boolean suspendedLogin = false;
		Usuario usu = new Usuario();
		usu.username = username;
		usu.password = password;
		Suspended suspended = new Suspended(usu);
		if (this.suspendedLogins.contains(suspended)) {
			suspended = this.suspendedLogins.get(this.suspendedLogins.indexOf(suspended));
			Date agora = new Date();
			Date inicio = suspended.getTime();
		    long diffInMillies = Math.abs(agora.getTime() - inicio.getTime());
		    long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		    if (diff >= ParamSingleton.getTempoLogin()) {
		    	this.suspendedLogins.remove(suspended);
		    	logger.info(String.format("Login %s agora liberado", username));
		    }
		    else {
		    	suspended.setTime(new Date());
		    	logger.error(String.format("Login %s ainda suspenso, e por mais tempo", username));
		    	suspendedLogin = true;
		    }
		}
		return suspendedLogin;
	}

}
