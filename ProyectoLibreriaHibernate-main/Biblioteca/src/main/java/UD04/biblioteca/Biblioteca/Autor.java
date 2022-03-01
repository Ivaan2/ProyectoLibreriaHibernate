package UD04.biblioteca.Biblioteca;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Autor {
	@Id
	@GeneratedValue
	private long idAutor;
	
	@Column
	private String dni;
	
	@Column
	private Date fNacimiento;
	
	@Column
	private String nombre;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Libro> libros = new ArrayList<>();
	
	public long getIdAutor() {
		return idAutor;
	}
	public void setIdAutor(long idAutor) {
		this.idAutor = idAutor;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public Date getfNacimiento() {
		return fNacimiento;
	}
	public void setfNacimiento(Date fNacimiento) {
		this.fNacimiento = fNacimiento;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Autor(long idAutor, String dni, Date fNacimiento, String nombre) {
		super();
		this.idAutor = idAutor;
		this.dni = dni;
		this.fNacimiento = fNacimiento;
		this.nombre = nombre;
	}
}
