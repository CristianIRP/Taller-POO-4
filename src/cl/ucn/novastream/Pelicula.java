package cl.ucn.novastream;

import java.time.LocalDate;

/**
 * Clase Pelicula, clase hija de la clase Contenido, con fecha de expiracion y duracion
 *
 */
public class Pelicula extends Contenido {

    /* Fecha de expiracion de la pelicula */
    private LocalDate fechaExpiracion;

    /* Duracion de la pelicula */
    private int duracionMinutos;

    /**
     * Constructor de la clase Pelicula, haciendo uso de herencia
     * @param id
     * @param titulo
     * @param anioLanzamiento
     * @param genero
     * @param fechaExpiracion
     * @param duracionMinutos
     */
    public Pelicula(String id, String titulo, int anioLanzamiento, String genero, LocalDate fechaExpiracion, int duracionMinutos) {
        super(id, titulo, "Pelicula", anioLanzamiento, genero);
        this.fechaExpiracion = fechaExpiracion;
        this.duracionMinutos = duracionMinutos;
    }

    /**
     * Obtiene la fecha de expiracion de la pelicula
     *
     * @return fecha expiracion
     */
    public LocalDate getFechaExpiracion() { return fechaExpiracion; }

    /**
     * Obtiene la duracion de la pelicula
     *
     * @return duracion
     */
    @Override
    public int obtenerDuracion() { return this.duracionMinutos; }
}