package cl.ucn.novastream;

import cl.ucn.novastream.Contenido;
import cl.ucn.novastream.Usuario;

/**
 * Interfaz usada para llamar los metodos de la clase SistemaNovaStreamimpl
 */


public interface SistemaNovaStream {

    /**
     * Metodo que lee y comprueba los usuarios del archivo, comprobando cantidad de datos y formato
     *
     * @param rutaArchivo Ruta del archivo a leer ("archivos/usuarios.csv")
     * @return Comprobacion true/false de que se leyo el texto
     */
    boolean cargarUsuarios(String rutaArchivo);

    /**
     * Metodo que lee y comprueba los contenidos del archivo, comprobando cantidad de datos y formato, ademas de
     * atribuirlos como Pelicula, Serie y Documental.
     *
     * @param rutaArchivo Ruta del archivo a leer ("archivos/contenidos.csv")
     * @return Comprobacion true/false de que se leyo el texto
     */
    boolean cargarContenidos(String rutaArchivo);

    /**
     * Proceso de inicio de sesion, comprobando que la clave y el usuario esten atribuidos correctamente.
     *
     * @param rut Rut del usuario
     * @param clave Clave del usuario
     * @return Usuario asociado
     */
    Usuario iniciarSesion(String rut, String clave);

    /**
     * Metodo encargado de buscar el contenido asociado al ID
     *
     * @param id Identificdor del contenido
     * @return Contenido asociado
     */
    Contenido buscarContenidoPorId(String id);

    /**
     * Metodo encargado de registrar una Pelicula nueva, tanto en el catalogo del programa, como en el archivo de texto
     *
     * @param titulo
     * @param anio
     * @param genero
     * @param duracion
     */
    void registrarPelicula(String titulo, int anio, String genero, int duracion);

    /**
     * Metodo encargado de registrar una Serie nueva, tanto en el catalogo del programa, como en el archivo de texto
     *
     * @param titulo
     * @param anio
     * @param genero
     * @param temporadas
     * @param episodios
     * @param duracionEp
     */
    void registrarSerie(String titulo, int anio, String genero, int temporadas, int episodios, int duracionEp);

    /**
     * Metodo encargado de registrar un Documental nuevo, tanto en el catalogo del programa, como en el archivo de texto
     * @param titulo
     * @param anio
     * @param genero
     * @param duracionMin
     * @param tema
     */
    void registrarDocumental(String titulo, int anio, String genero, int duracionMin, String tema);

    /**
     * Metodo encargado de ordenar los contenidos PPV (con fecha de expiracion) en orden de mas proximo a menos proximo.
     *
     */
    void mostrarProximosAVencer();

    /**
     * Muestra los contenidos de cierto tipo o, en caso de no especificarse, muestra todos.
     * @param tipo Tipo de contenido
     */
    void mostrarContenidos(String tipo);
}