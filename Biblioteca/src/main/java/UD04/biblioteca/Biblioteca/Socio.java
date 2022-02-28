package UD04.biblioteca.Biblioteca;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.NaturalId;

@Entity
public class Socio {
	@Id
	@GeneratedValue
	private long idSocio;
	
	@NaturalId
	private String dni;
	
	private Date fNacimiento;
	
	private String nombre;
	
	@ManyToMany(mappedBy = "socios")
	private List<Libro> listaLibros = new ArrayList<>();

	public long getIdSocio() {
		return idSocio;
	}

	public void setIdSocio(long idSocio) {
		this.idSocio = idSocio;
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

	public Socio(long idSocio, String dni, Date fNacimiento, String nombre) {
		super();
		this.idSocio = idSocio;
		this.dni = dni;
		this.fNacimiento = fNacimiento;
		this.nombre = nombre;
	}
}
