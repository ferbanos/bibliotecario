package dominio;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;

import utilidades.Constants;

public class Bibliotecario {

	private RepositorioLibro repositorioLibro;
	private RepositorioPrestamo repositorioPrestamo;

	public Bibliotecario(RepositorioLibro repositorioLibro, RepositorioPrestamo repositorioPrestamo) {
		this.repositorioLibro = repositorioLibro;
		this.repositorioPrestamo = repositorioPrestamo;
	}

	public void prestar(String isbn, String nombreUsuario) {
		Libro libro = repositorioLibro.obtenerPorIsbn(isbn);
		
		if (libro == null)
			throw new PrestamoException(Constants.MSJ_LIBRO_NO_EXISTE);
	
		if (esPrestado(isbn))
			throw new PrestamoException(Constants.MSJ_LIBRO_NO_DISPONIBLE);
		
		if (esPalindromo(isbn)) 
			throw new PrestamoException(Constants.MSJ_ISBN_PALINDROMO);
		
		// Se definen las fechas 
		Calendar fechaHoy    = new GregorianCalendar(new Locale("es", "CO"));
		Calendar fechaMaxima = null;
		
		if (cantidadSumada(isbn) > 30) 
			 fechaMaxima = fechaMaximaEntrega(fechaHoy);
				
		repositorioPrestamo.agregar(new Prestamo(fechaHoy.getTime(), libro, fechaMaxima != null ? fechaMaxima.getTime() : null, nombreUsuario));
	}

	public boolean esPrestado(String isbn) {
		Libro libro = repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn);
		
		if (libro != null)
			return true;
		
		return false;
	}	
	
	// Método que evalua el ISBN para saber si es palindromo
	private boolean esPalindromo (String isbn) {
		int longitud = isbn.length();
		
		char[] caracteres = isbn.toCharArray();
		
		for(int i=0; i < (int) longitud/2; i++) {
			if (caracteres[i] != caracteres[longitud - 1 - i])
				return false;
		}

		return true;
	}
	
	// Método que suma los datos númericos del ISBN
	private int cantidadSumada (String isbn) {
		int cantidad = 0;
		
		for (int i=0; i<isbn.length(); i++) {
			cantidad += esString(isbn.substring(i, i+1));
		}
		
		return cantidad;
	}
	
	// Método que identifica si el caracter es númerico o no
	private int esString (String letra) {
		try {
			return Integer.valueOf(letra);
		} catch (NumberFormatException e) {
			return 0;
		} 		
	}
	
	// Método que calcula la fecha de entrega
	private Calendar fechaMaximaEntrega (Calendar fechaHoy) {
		int dias = Constants.DIAS_PRESTAMO;
		
		if (fechaHoy.get(Calendar.DAY_OF_WEEK) >= 6)
			dias++;
			
		fechaHoy.add(Calendar.DAY_OF_YEAR, dias);	
		
		return fechaHoy;
	}	
}