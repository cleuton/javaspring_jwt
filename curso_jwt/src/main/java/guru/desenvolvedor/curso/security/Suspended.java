package guru.desenvolvedor.curso.security;

import java.util.Date;

public class Suspended {
	private Usuario usuario;
	private long tentativas;
	private Date time;
	private boolean suspenso;
	
	@Override
	public boolean equals(Object obj) {
		return this.getUsuario().equals(((Suspended) obj).getUsuario());
	}

	@Override
	public int hashCode() {
		return this.usuario.hashCode();
	}

	public Suspended() {
		super();
	}
	
	public Suspended(Usuario usuario) {
		this();
		this.usuario = usuario;
		this.tentativas = 0;
		this.time = new Date();
		this.suspenso = false;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public boolean isSuspenso() {
		return suspenso;
	}
	public void setSuspenso(boolean suspenso) {
		this.suspenso = suspenso;
	}

	public long getTentativas() {
		return tentativas;
	}

	public void setTentativas(long tentativas) {
		this.tentativas = tentativas;
	}
	
	
}
