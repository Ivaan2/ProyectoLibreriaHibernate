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
		cargarDatos(session);
        do{
        	opc = solicitarOpcion();
        	tratarOpcion(opc, session);
        }while(opc != 10);
        session.close();
        sf.close();
        sr.close();
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
			//mostrarTemasDisponibles(session);
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
			//Consulta de datos de un libro por isbn, así como sus préstamos.
			consultarLibro(session);
			break;
		case 7:
			/*
			 * private static void consultarPrestamosDeUnTemaPorCodigo() {
        // TODO Auto-generated method stub

        List<Libro> librosPrestados = new ArrayList<>();

        SessionFactory sf = inicializarSessionFactory();
        Session session = realizarTransaccion(sf);

        System.out.println("Introduce el codigo del tema");
        String codigo =teclado.nextLine().toUpperCase();

        Query sql = session.createNativeQuery("select * from Libro l where l.tema_codigo =?1", Libro.class);

        sql.setParameter(1, codigo);
        List<Libro> results =(List<Libro>) sql.getResultList();

        for (Libro libro : results) {
            Query nativeQuery = session.createNativeQuery("select * from socio_libro sl where sl.listaLibros_idLibro = ?1");
            nativeQuery.setParameter(1, libro.getIdLibro());

            librosPrestados =  (List<Libro>)nativeQuery.list();
        }

        System.out.println("Numero de libros: "+librosPrestados.size());

        cerrarTransaccion(sf, session);

    }
			 */
			break;
		case  8:
			break;
		case 9:
			break;
		}
		
	}

	private static void consultarLibro(Session session) {
		String isbn = solicitarCadena("Introduce el isbn del libro: ");
		if(isbnExiste(isbn, session)) {
			String sql = "SELECT l from Libro l where isbn = ?1";
			Query query = session.createQuery(sql);
			query.setString(1, isbn);
			List<Libro> libros = query.list();
			for(Libro l: libros) {
				System.out.println(l);
			}
			
		}else {
			System.out.println("No existe un libro por ese ISBN.");
		}
	}

	private static void cargarDatos(Session session) {
		Tema t = new Tema("HISTORIA", "Historia");
		session.save(t);
		Tema t2 = new Tema("AVENTURAS", "Aventuras");
		session.save(t2);
		Libro l1 = new Libro("JIUWDF7", "Libro1", t);
		session.save(l1);
		Libro l2 = new Libro("HBIS233", "Libro2", t2);
		session.save(l2);
		session.getTransaction().commit();
		session.beginTransaction();
	}

	private static void consultarDatosSocio(Session session) {
		String dni = solicitarCadena("Introduzca el dni del socio: ");
		String sql = "select s from Socio s where s.dni = ?1";
		Query q = session.createQuery(sql);
		q.setString(1, dni);
		List<Socio> datosSocio = q.list();
		for (Socio o: datosSocio) {
			System.out.println(o);
		}
		
	}

	private static boolean realizarPrestamo(Session session) {
		String isbn, dni;
		Socio s;
		Libro l;
		boolean realizado = false;
		
		isbn = solicitarCadena("Introduzca el isbn del libro que desea sacar.");
		isbnExiste(isbn, session);
		dni = solicitarCadena("Introduzca el dni del socio que alquila el libro.");
		dniExiste(dni, session);
		
		if(isbnExiste(isbn, session) && dniExiste(dni, session)) {
			l = obtenerLibroPorISBN(session, isbn);
			s = obtenerSocioPorDNI(session, dni);
			
			l.addSocio(s);
			
			
			if(session.save(l) != null) {
				realizado = true;
				session.getTransaction().commit();
			}
		}
		
		return realizado;
	}

	private static Socio obtenerSocioPorDNI(Session session, String dni) {
		Socio s;
		String sql2 = "select s from Socio s where dni = ?1";
		Query qSocio = session.createQuery(sql2);
		qSocio.setString(1, dni);
		s = (Socio) qSocio.uniqueResult();
		return s;
	}

	private static Libro obtenerLibroPorISBN(Session session, String isbn) {
		Libro l;
		String sql = "select l from Libro l where isbn = ?1";
		Query qLibro= session.createQuery(sql);
		qLibro.setString(1, isbn);
		l = (Libro) qLibro.uniqueResult();
		return l;
	}

	private static boolean dniExiste(String dni, Session session) {
		boolean exists = false;
		String sql = "select count(*) from Socio where dni = ?1";
		Query query = session.createQuery(sql);
		query.setString(1, dni);
		Long n = (Long) query.uniqueResult();
		if(n != 0) {
			exists = true;
		}
		
		return exists;
	}

	private static boolean isbnExiste(String isbn, Session session) {
		boolean exists = false;
		String sql = "select count(*) from Libro where isbn = ?1";
		Query query = session.createQuery(sql);
		query.setString(1, isbn);
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
		session.getTransaction().commit();
		
		if(tema != null) {
			creado = true;
		}
		return creado;
	}

	private static Tema crearTema(String codigo, String descripcion) {
		return new Tema(codigo, descripcion);
	}

	private static void mostrarTemasDisponibles(Session session) {
		String sql = "select t from Tema t group by t.codigo";
		Query q = session.createQuery(sql);
		if(q != null) {
			List<Tema> result = q.list();
		for(Tema t: result) {
			System.out.println(t);
		}
		}else {
			System.out.println("Aún no hay temas.");
		}
	}

	private static boolean altaLibro(Session session) {
		boolean creado = false;
		Libro libro;
		String isbn = solicitarCadena("Introduce el ISBN del libro a dar de alta: ");
		String nombre = solicitarCadena("Introduce el nombre del libro a dar de alta: ");
		String codigo = solicitarCadena("Introduce el codigo del tema del libro: ");
		String descripcion = solicitarCadena("Introduce la descripcion del tema del libro: ");
		
		Tema tema = new Tema(codigo, descripcion);
		session.save(tema);
		
		libro = crearLibro(isbn, nombre, tema);
		session.save(libro);
		session.getTransaction().commit();
		
		if(libro != null) {
			creado = true;
		}
		return creado;
	}

	private static Libro crearLibro(String isbn, String nombre, Tema tema) {
		return new Libro(isbn, nombre, tema);
	}

	private static boolean altaSocio(Session session) throws ParseException {
		boolean creado = false;
		Socio socio;
		String dni = solicitarCadena("Introduzca el dni para el socio a dar de alta: ");
		String nombre = solicitarCadena("Introduzca el nombre para el socio a dar de alta: ");
		Date fNacimiento = solicitarFecha();
		
		socio = crearSocio(dni, nombre, fNacimiento);
		session.save(socio);
		session.getTransaction().commit();
		session.beginTransaction();

		if(socio != null) {
			creado = true;
		}
		return creado;
	}

	private static Socio crearSocio(String dni, String nombre, Date fNacimiento) {
		return new Socio(dni, fNacimiento, nombre);
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
		System.out.println(string);
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
