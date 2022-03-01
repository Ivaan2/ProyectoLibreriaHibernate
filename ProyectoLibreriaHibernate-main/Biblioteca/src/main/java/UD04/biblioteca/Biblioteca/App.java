package UD04.biblioteca.Biblioteca;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final Scanner sc = new Scanner(System.in);
    public static void main( String[] args )
    {
        System.out.println("---- Bienvenido al menú de la biblioteca ----");
        int opc;
        StandardServiceRegistry sr = new StandardServiceRegistryBuilder()
				.configure()
				.build();
		SessionFactory sf = new MetadataSources(sr).buildMetadata().buildSessionFactory();
		
		
		//Apertura de una sesión (e inicio de una transacción)
		Session session = sf.openSession();
		session.beginTransaction();
        do{
        	opc = solicitarOpcion();
        	tratarOpcion(opc, session);
        }while(opc != 10);
        System.out.println("Gracias por confiar en nosotros.");
    }

	private static void tratarOpcion(int opc, Session session) {
		switch(opc) {
		case 1:
			//Alta socio
			try {
				if(altaSocio(session)) {
					System.out.println("Se ha dado de alta el socio correctamente.");
				}else {
					System.out.println("No se ha podido dar de alta el socio correctamente.");
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			mostrarTemasDisponibles(session);
			if(altaLibro(session)) {
				System.out.println("Se ha dado de alta el libro correctamente.");
			}else {
				System.out.println("No se ha podido dar de alta el libro satisfactoriamnte.");
			}
			
			break;
		case 3:
			if(altaTema(session)) {
				System.out.println("Se ha dado de alta el nuevo tema correctamente.");
			}else {
				System.out.println("No se ha podido dar de alta el tema satisfactoriamente.");
			}
			
			break;
		case 4:
			if(realizarPrestamo(session)) {
				System.out.println("El préstamos se ha realizado correctamente.");
			}else {
				System.out.println("No se ha podido llevar a cabo el préstamo.");
			}
			break;
		case 5:
			//Consulta de datos de un socio por dni, así como sus préstamos.
			consultarDatosSocio(session);
			break;
		case 6:
			break;
		case 7:
			break;
		case  8:
			break;
		case 9:
			break;
		}
		
	}

	private static void consultarDatosSocio(Session session) {
		String dni = solicitarCadena("Introduzca el dni del socio: ");
		String sql = "select s from Socio s where s.dni = ?1";
		Query q = session.createQuery(sql);
		q.setString(1, dni);
		List<Object> datosSocio = q.list();
		for (Object o: datosSocio) {
			System.out.println(o);
		}
		
	}

	private static boolean realizarPrestamo(Session session) {
		String isbn, dni;
		Socio s;
		Libro l;
		boolean realizado = false;
		
		do{
			isbn = solicitarCadena("Introduzca el isbn del libro que desea sacar.");
		}while(isbnExiste(isbn, session));
		do {
			dni = solicitarCadena("Introduzca el dni del socio que alquila el libro.");
		}while(dniExiste(dni, session));
		
		l = obtenerLibroPorISBN(session, isbn);
		
		s = obtenerSocioPorDNI(session, dni);
		
		l.addSocio(s);
		if(session.save(l) != null) {
			realizado = true;
		}
		return realizado;
	}

	private static Socio obtenerSocioPorDNI(Session session, String dni) {
		Socio s;
		String sql2 = "select s from socio s where dni = ?";
		Query qSocio = session.createQuery(sql2);
		qSocio.setString(0, dni);
		s = (Socio) qSocio.uniqueResult();
		return s;
	}

	private static Libro obtenerLibroPorISBN(Session session, String isbn) {
		Libro l;
		String sql = "select l from libro l where isbn = ?";
		Query qLibro= session.createQuery(sql);
		qLibro.setString(0, isbn);
		l = (Libro) qLibro.uniqueResult();
		return l;
	}

	private static boolean dniExiste(String dni, Session session) {
		boolean exists = false;
		String sql = "select count(*) from socio where dni = ?";
		Query query = session.createQuery(sql);
		query.setString(0, dni);
		Long n = (Long) query.uniqueResult();
		if(n != 0) {
			exists = true;
		}
		
		return exists;
	}

	private static boolean isbnExiste(String isbn, Session session) {
		boolean exists = false;
		String sql = "select count(*) from libro where isbn = ?";
		Query query = session.createQuery(sql);
		query.setString(0, isbn);
		Long n = (Long) query.uniqueResult();
		if(n != 0) {
			exists = true;
		}
		return exists;
	}

	private static boolean altaTema(Session session) {
		boolean creado = false;
		Tema tema = null;
		String codigo = solicitarCadena("Introduzca el codigo del tema a introducir.").toUpperCase();
		String descripcion = solicitarCadena("Introduzca la descripción del tema a introducir.");
		
		tema = crearTema(codigo, descripcion);
		session.save(tema);
		
		if(tema != null) {
			creado = true;
		}
		return creado;
	}

	private static Tema crearTema(String codigo, String descripcion) {
		return new Tema(codigo, descripcion);
	}

	private static void mostrarTemasDisponibles(Session session) {
		String sql = "select * from temas group by codigo;";
		Query q = session.createQuery(sql);
		List<Object[]> result = q.list();
		for(Object[] o: result) {
			System.out.println("Tema: " + o[0] + " - Descripcion: " + o[1]);
		}
		
	}

	private static boolean altaLibro(Session session) {
		boolean creado = false;
		Libro libro;
		String isbn = solicitarCadena("Introduce el ISBN del libro a dar de alta: ");
		String nombre = solicitarCadena("Introduce el nombre del libro a dar de alta: ");
		
		libro = crearLibro(isbn, nombre);
		session.save(libro);
		
		if(libro != null) {
			creado = true;
		}
		return creado;
	}

	private static Libro crearLibro(String isbn, String nombre) {
		return new Libro(isbn, nombre);
	}

	private static boolean altaSocio(Session session) throws ParseException {
		boolean creado = false;
		Socio socio;
		String dni = solicitarCadena("Introduzca el dni para el socio a dar de alta: ");
		String nombre = solicitarCadena("Introduzca el nombre para el socio a dar de alta: ");
		Date fNacimiento = solicitarFecha();
		
		socio = crearSocio(dni, nombre, fNacimiento);
		session.save(socio);
		
		if(socio != null) {
			creado = true;
		}
		return creado;
	}

	private static Socio crearSocio(String dni, String nombre, Date fNacimiento) {
		return new Socio(dni, fNacimiento, nombre, new ArrayList<Libro>());
	}

	private static Date solicitarFecha() throws ParseException {
		int y, m, d;
		int diaLimite = 0;
		do{
			y = solicitarEntero("Introduzca el año en formato de 4 digitos");
		}while(y < 1900 || y > 2022);
		do {
			m = solicitarEntero("Introduzca el mes: ");
		}while(m < 1 || m > 12);
		if(m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) {
			diaLimite = 31;
		}else {
			if(m == 2) {
				diaLimite = 28;
			}else {
				diaLimite = 30;
			}
		}
		do {
			d = solicitarEntero("Introduzca el dia: ");
		}while(d < 1 || d > diaLimite);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
		String formatoFecha = d + "-" + m + "-" + y;
		return sdf.parse(formatoFecha);
	}

	private static int solicitarEntero(String string) {
		return Integer.parseInt(sc.nextLine());
	}

	private static String solicitarCadena(String string) {
		System.out.println(string);
		return sc.nextLine();
	}

	private static int solicitarOpcion() {
		int o = 0;
		do {
			System.out.println("1 - Dar de alta Socio");
			System.out.println("2 - Dar de alta Libro");
			System.out.println("3 - Dar de alta Tema");
			System.out.println("4 - Dar de alta un préstamo por dni e isbn");
			System.out.println("5 - Consultar datos de un socio por dni");
			System.out.println("6 - Consultar los datos de un libro por su isbn");
			System.out.println("7 - Consultar la cantidad de préstamos de un tema según su código");
			System.out.println("8 - Consultar el número de préstamos de una categoría por su código");
			System.out.println("9 - Realizar actualización de las categorías de los soicos");
			System.out.println("10 - Salir");
			o = Integer.parseInt(sc.nextLine());
		}while(o < 1 || o > 10);
				
		return o;
	}
}
