package guru.desenvolvedor.curso.security;

public class Usuario {
	public String username;
	public String password;
	@Override
	public boolean equals(Object obj) {
		return this.username.equalsIgnoreCase(((Usuario)obj).username);
	}
	@Override
	public int hashCode() {
		return this.username.hashCode();
	}
	
}
