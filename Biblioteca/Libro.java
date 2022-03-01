package UD04.biblioteca.Biblioteca;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NaturalId;

@Entity
public class Libro {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idLibro;
	
	@Column
	@NaturalId
	private String isbn;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Socio> listaSocios = new ArrayList<>();
	
	
	@ManyToOne
	@JoinColumn(name = "tema_codigo", nullable = false)
	private Tema tema;
	
	@Column
	private String nombre;
	
	

	public List<Socio> getListaSocios() {
		return listaSocios;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public long getIdLibro() {
		return idLibro;
	}

	public void setIdLibro(long idLibro) {
		this.idLibro = idLibro;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(isbn);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Libro other = (Libro) obj;
		return Objects.equals(isbn, other.isbn);
	}

	public void addSocio(Socio s) {
		this.listaSocios.add(s);
		s.getListaLibros().add(this);
	}
	
	public void removeSocio(Socio s) {
		this.listaSocios.remove(s);
		s.getListaLibros().remove(this);
	}

	public Libro() {
		
	}
	
	public Libro(String isbn, String nombre, Tema tema) {
		super();
		this.isbn = isbn;
		this.nombre = nombre;
		this.tema = tema;
	}

	@Override
	public String toString() {
		return "Libro [idLibro=" + idLibro + ", isbn=" + isbn + ", listaSocios=" + listaSocios + ", tema=" + tema
				+ ", nombre=" + nombre + "]";
	}
	
	
}
