package guru.desenvolvedor.curso.persistence;

import java.nio.charset.StandardCharsets;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;

@Entity
public class Aviso {

  
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  
  @Lob
  @Type(type = "org.hibernate.type.BinaryType")
  private byte[] titulo;
  @Lob
  @Type(type = "org.hibernate.type.BinaryType")
  private byte[] resumo;
  @Lob
  @Type(type = "org.hibernate.type.BinaryType")
  private byte[] thumb;
  @Lob
  @Type(type = "org.hibernate.type.BinaryType")
  private byte[] imagem;
  @Lob
  @Type(type = "org.hibernate.type.BinaryType")
  private byte[] texto;
  @Lob
  @Type(type = "org.hibernate.type.BinaryType")
  private byte[] data;

  protected Aviso() {}



  public Aviso(byte[] titulo, byte[] resumo, byte[] thumb, byte[] imagem, byte[] texto, byte[] data) {
	super();
	this.titulo = titulo;
	this.resumo = resumo;
	this.thumb = thumb;
	this.imagem = imagem;
	this.texto = texto;
	this.data = data;
  }


  	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Aviso [id=");
		builder.append(id);
		builder.append(", titulo=");
		builder.append(this.getTitulo());
		builder.append(", resumo=");
		builder.append(this.getResumo());
		builder.append(", thumb=");
		builder.append(this.getThumb());
		builder.append(", imagem=");
		builder.append(this.getImagem());
		builder.append(", texto=");
		builder.append(this.getTexto());
		builder.append(", data=");
		builder.append(this.getData());
		builder.append("]");
		return builder.toString();
	}


  	private String transform(byte [] entrada) {
  		//byte[] decoded = Base64.getDecoder().decode(entrada);
  		return new String(entrada, StandardCharsets.UTF_8);
  	}

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getTitulo() {
		return this.transform(this.titulo);
	}



	public String getResumo() {
		return this.transform(this.resumo);
	}

	public String getThumb() {
		return this.transform(this.thumb);
	}

	public String getImagem() {
		return this.transform(this.imagem);
	}

	public String getTexto() {
		return this.transform(this.texto);
	}


	public String getData() {
		return this.transform(this.data);
	}


}