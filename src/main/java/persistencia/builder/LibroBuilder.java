package persistencia.builder;

import dominio.Libro;
import persistencia.entidad.LibroEntity;

public class LibroBuilder {
	
	private LibroBuilder() {}
	
	public static Libro convertirADominio(LibroEntity libroEntity) {
		Libro libro = null;
		
		if(libroEntity != null) {
			libro = new Libro(libroEntity.getIsbn(), libroEntity.getTitulo(), libroEntity.getAnio());
		}
		
		return libro;
	}
	
	public static LibroEntity convertirAEntity(Libro libro) {
		LibroEntity libroEntity = new LibroEntity();
		libroEntity.setTitulo(libro.getTitulo());
		libroEntity.setIsbn(libro.getIsbn());
		libroEntity.setAnio(libro.getAnio());
		
		return libroEntity;
	}
}
