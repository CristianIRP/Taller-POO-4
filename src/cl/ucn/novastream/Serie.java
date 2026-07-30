package cl.ucn.novastream;

import java.time.LocalDate;

/**
 * Clase Serie, clase hija de la clase Contenido, con fecha de expiracion, numero de temporadas, capitulos y duracion
 * por episodio
 */
public class Serie extends Contenido {
    /* Fecha de expiracion de la serie */
    private LocalDate fechaExpiracion;

    /* Temporadas de la serie */

    private int temporadas;

    /* Numero de episodios por temporada de la serie */
    private int episodiosPorTemporada;

    /* Duracion de cada episodio de la serie*/
    private int duracionEpisodio;

    /**
     * Constructor de la clase Serie, haciendo uso de herencia
     * @param id
     * @param titulo
     * @param anioLanzamiento
     * @param genero
     * @param fechaExpiracion
     * @param temporadas
     * @param episodiosPorTemporada
     * @param duracionEpisodio
     */
    public Serie(String id, String titulo, int anioLanzamiento, String genero, LocalDate fechaExpiracion, int temporadas, int episodiosPorTemporada, int duracionEpisodio) {
        super(id, titulo, "Serie", anioLanzamiento, genero);
        this.fechaExpiracion = fechaExpiracion;
        this.temporadas = temporadas;
        this.episodiosPorTemporada = episodiosPorTemporada;
        this.duracionEpisodio = duracionEpisodio;
    }

    /**
     * Obtiene la fecha de expiracion
     *
     * @return fechaExp
     */
    public LocalDate getFechaExpiracion() { return fechaExpiracion; }

    /**
     * Obtiene la duracion TOTAL de la serie
     *
     * @return duracion
      */
    @Override
    public int obtenerDuracion() { return this.temporadas * this.episodiosPorTemporada * this.duracionEpisodio; }

    /**
     * Obtiene la duracion por capitulo
     *
     * @return duracion media de capitulo
     */
    public int getDuracionEpisodio() { return duracionEpisodio; }

    /**
     * Obtiene la cantidad de temporadas
     *
     * @return temporadas
     */
    public int getTemporadas() { return temporadas; }

    /**
     * Obtiene la cantidad de episodios por temporada
     *
     * @return cantEpisodios
     */
    public int getEpisodiosPorTemporada() { return episodiosPorTemporada; }
}