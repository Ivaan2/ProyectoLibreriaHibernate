package UD04.biblioteca.Biblioteca;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.NaturalId;

@Entity
public class Socio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idSocio;
	
	@NaturalId
	private String dni;
	@Column
	private Date fNacimiento;
	@Column
	private String nombre;
	
	@ManyToMany(mappedBy = "listaSocios")
	private List<Libro> listaLibros = new ArrayList<>();

	public List<Libro> getListaLibros() {
		return listaLibros;
	}

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
	
	public Socio() {
		
	}

	public Socio(String dni, Date fNacimiento, String nombre) {
		super();
		this.dni = dni;
		this.fNacimiento = fNacimiento;
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Socio [idSocio=" + idSocio + ", dni=" + dni + ", fNacimiento=" + fNacimiento + ", nombre=" + nombre
				+ ", listaLibros=" + listaLibros + "]";
	}
	
	

}
