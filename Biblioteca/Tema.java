package UD04.biblioteca.Biblioteca;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Tema {
	@Id
	private String codigo;
	
	@Column
	private String descripcion;
	
	@OneToMany(mappedBy = "tema", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Libro> libros = new ArrayList<>();

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void addLibro(Libro l) {
		this.libros.add(l);
		l.setTema(this);
	}
	
	public void removeLibro(Libro l) {
		this.libros.remove(l);
		l.setTema(null);
	}
	
	
	public Tema() {
		
	}

	public Tema(String codigo, String descripcion) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "Tema [codigo=" + codigo + ", descripcion=" + descripcion + ", libros=" + libros + "]";
	}
	
	
}
