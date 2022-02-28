package UD04.biblioteca.Biblioteca;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.NaturalId;

@Entity
public class Libro {
	@Id
	@GeneratedValue
	private long idLibro;
	
	@Column
	@NaturalId
	private String isbn;
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Socio> listaSocios = new ArrayList<>();
	
	@Column
	private String nombre;

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
	
	public void addSocio(Socio s) {
		this.listaSocios.add(s);
		//GetOwners
	}

	public Libro(long idLibro, String isbn, String nombre) {
		super();
		this.idLibro = idLibro;
		this.isbn = isbn;
		this.nombre = nombre;
	}
}
