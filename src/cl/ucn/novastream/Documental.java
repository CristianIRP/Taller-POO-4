package cl.ucn.novastream;

/**
 * Clase Documental, clase hija de la clase Contenido, con duracion minima y tema principal
 *
 */
public class Documental extends Contenido {

    /* Duracion del documental */
    private int duracionMinima;

    /* Tema principal del documental */
    private String temaPrincipal;

    /**
     * Constructor del documental, haciendo uso de herencia desde la clase Contenido
     * @param id
     * @param titulo
     * @param anioLanzamiento
     * @param genero
     * @param duracionMinima
     * @param temaPrincipal
     */
    public Documental(String id, String titulo, int anioLanzamiento, String genero, int duracionMinima, String temaPrincipal) {
        super(id, titulo, "Documental", anioLanzamiento, genero);
        this.duracionMinima = duracionMinima;
        this.temaPrincipal = temaPrincipal;
    }

    /**
     * Obtiene la duracion del documental
     *
     * @return duracion minima
     */
    @Override
    public int obtenerDuracion() { return this.duracionMinima; }

    /**
     * Obtiene el tema principal del documental
     *
     * @return tema principal
     */
    public String getTemaPrincipal() { return temaPrincipal; }
}