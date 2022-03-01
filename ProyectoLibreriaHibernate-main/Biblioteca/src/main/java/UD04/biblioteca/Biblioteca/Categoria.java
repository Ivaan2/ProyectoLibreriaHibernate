package UD04.biblioteca.Biblioteca;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Categoria {
	@Id
	private String codigo;
	
	@Column
	private String descripcion;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Socio> listaSocios = new ArrayList<>();
	
	
	
	public List<Socio> getListaCategorias() {
		return listaSocios;
	}

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

	public Categoria(String codigo, String descripcion) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
	}
}
