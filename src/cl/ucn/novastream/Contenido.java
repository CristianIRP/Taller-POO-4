package cl.ucn.novastream;

import java.time.LocalDate;

/**
 * Clase padre de los materiales audiovisuales en novastream
 */
public class Contenido implements Comparable<Contenido> {


    /* Identificador del contenido */
    protected String id;

    /* Titulo o nombre del contenido */
    protected String titulo;

    /* Tipo de contenido (Pelicula, Serie o Documental)*/
    protected String tipo;

    /* Año de publicacion del contenido */
    protected int anioLanzamiento;

    /* Genero audiovisual del contenido */
    protected String genero;


    /**
     *
     * Constructor de la clase Contenido
     *
     * @param id Identificador
     * @param titulo Titulo o nombre
     * @param tipo Tipo de contenido
     * @param anioLanzamiento Año de publicacion
     * @param genero Genero audiovisual
     */
    public Contenido(String id, String titulo, String tipo, int anioLanzamiento, String genero) {

        // Se atribuye cada dato de manera directa
        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.anioLanzamiento = anioLanzamiento;
        this.genero = genero;
    }


    /**
     * Metodo que compara las fechas de expiracion (dando como resultado si es mayor, menor o igual) usada en las
     * clases Pelicula y Serie
     *
     * @param otro the object to be compared.
     * @return numero int que muestra si es mayor, igual o menor (1,0,-1)
      */
    @Override
    public int compareTo(Contenido otro) {

        // Se verifica que el contenido sea Pelicula o Serie, es decir, un contenido con fecha de expiracion,
        // consiguiendo su fecha de expiracion de ser asi
        LocalDate estaFecha = null;
        if (this instanceof Pelicula) {
            estaFecha = ((Pelicula) this).getFechaExpiracion();
        } else if (this instanceof Serie) {
            estaFecha = ((Serie) this).getFechaExpiracion();
        }

        // Se verifica que el contenido A COMPARAR sea pelicula o serie, atribuyendo su fecha de expiracion de ser asi
        LocalDate otraFecha = null;
        if (otro instanceof Pelicula) {
            otraFecha = ((Pelicula) otro).getFechaExpiracion();
        } else if (otro instanceof Serie) {
            otraFecha = ((Serie) otro).getFechaExpiracion();
        }

        // Se verifica que se hayan obtenido las dos fechas (evitando comparar un contenido PPV sin fecha atribuida)
        // y se comparan, regresando un valor dependiendo de si la fecha del contenido A COMPARAR es mayor, menor o igual.
        if (estaFecha != null && otraFecha != null) {
            return estaFecha.compareTo(otraFecha);
        } else if (estaFecha != null) {
            return -1;
        } else if (otraFecha != null) {
            return 1;
        }
        return 0;


    }
    /**
     * Metodo base que será sobrescrito por las subclases.
     *
     * @return Duracion en minutos (0 base)
     */
    public int obtenerDuracion() {
        return 0;
    }

    /**
     * Obtiene el identificador del contenido
     *
     * @return Identificador unico
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el titulo del contenido
     *
     * @return Titulo o nombre
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Obtiene el tipo de contenido
     *
     * @return Tipo de contenido
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Obtiene el año de lanzamiento del contenido
     *
     * @return Año de lanzamiento
     */
    public int getAnioLanzamiento() { return anioLanzamiento; }

    /**
     * Obtiene el genero del contenido
     *
     * @return Genero del contenido
     */
    public String getGenero() { return genero; }
}