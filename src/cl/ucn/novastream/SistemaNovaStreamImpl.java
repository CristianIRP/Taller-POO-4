package cl.ucn.novastream;

import cl.ucn.novastream.Contenido;
import cl.ucn.novastream.Pelicula;
import cl.ucn.novastream.Serie;
import cl.ucn.novastream.Documental;
import cl.ucn.novastream.Usuario;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Administra operaciones principales del programa y aplicamos patron singleton
 */
public class SistemaNovaStreamImpl implements SistemaNovaStream {

    // Seguimos el patron singleton
    private static SistemaNovaStreamImpl instancia;

    // Colecciones obligatorias segun la especificación del taller
    private Set<Usuario> usuarios;
    private Map<String, Contenido> catalogoContenidos;
    private int contadorIdContenido;

    /**
     * Constructor privado para restringir la instanciación directa
     */
    private SistemaNovaStreamImpl() {
        this.usuarios = new HashSet<>();
        this.catalogoContenidos = new HashMap<>();
        this.contadorIdContenido = 1;
    }

    /**
     * Patron de diseño Singleton, usado para poseer solo una instancia de la clase SistemaNovaStreamimpl.
     *
     * @return Instancia del sistema
     */
    public static SistemaNovaStreamImpl getInstance() {
        if (instancia == null) {
            instancia = new SistemaNovaStreamImpl();
        }
        return instancia;
    }

    /**
     * Metodo que lee y comprueba los usuarios del archivo, comprobando cantidad de datos y formato
     *
     * @param rutaArchivo Ruta del archivo a leer ("archivos/usuarios.csv")
     * @return Comprobacion true/false de que se leyo el texto
     */
    @Override
    public boolean cargarUsuarios(String rutaArchivo) {

        // Se abre y empieza a leer el archivo, separandolo por lineas
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                // Se separa la linea de texto, usando como referencia ","
                String[] partes = linea.split(",");

                // Se verifica que la linea no posea datos adicionales y se atribuye cada dato.
                if (partes.length >= 5) {
                String id = partes[0].trim();
                String nombre = partes[1].trim();
                String rut = partes[2].trim();
                String correo = partes[3].trim();
                String clave = partes[4].trim();


                // Se crea el usuario con los datos atribuidos de la linea de texto
                usuarios.add(new Usuario(id, nombre, rut, correo, clave));
                }
            }

            //Se retorna true al tener una lectura exitosa
            return true;

        // Validacion en caso de no poseer un formato correcto y/o no hallar el archivo
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios.csv: " + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo que lee y comprueba los contenidos del archivo, comprobando cantidad de datos y formato, ademas de
     * atribuirlos como Pelicula, Serie y Documental.
     *
     * @param rutaArchivo Ruta del archivo a leer ("archivos/contenidos.csv")
     * @return Comprobacion true/false de que se leyo el texto
     */
    @Override
    public boolean cargarContenidos(String rutaArchivo) {

        // Se abre y empieza a leer el archivo, separandolo por lineas
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                // Se separa la linea de texto, usando como referencia ","
                String[] partes = linea.split(";");

                // Se verifica que la linea no posea datos adicionales y se atribuye cada dato.
                if (partes.length < 5) continue;

                // Se atribuyen los datos de la parte comun entre todos los contenidos
                String id = partes[0].trim();
                String titulo = partes[1].trim();
                String tipo = partes[2].trim();
                int anio = Integer.parseInt(partes[3].trim());
                String genero = partes[4].trim();

                // Se separa la parte de los datos propios de cada tipo
                String[] datosContenido = partes[5].split(",");

                // Se crean un contenido provicional, ademas de establecer el formato de fecha para los contenidos PPV
                // y verificar que no exista el contenido previamente
                Contenido nuevoContenido = null;
                DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                boolean noExiste = comprobarContenido(id,titulo);

                if(noExiste) {

                    // En caso de coincidir el tipo de contenido y la cantidad de datos adicionales, se crea una Pelicula
                    if (tipo.equalsIgnoreCase("Pelicula") && datosContenido.length >= 2) {
                        LocalDate fechaExp = LocalDate.parse(datosContenido[0].trim(), formatoFecha);
                        int duracion = Integer.parseInt(datosContenido[1].trim());
                        nuevoContenido = new Pelicula(id, titulo, anio, genero, fechaExp, duracion);

                        // En caso de coincidir el tipo de contenido y la cantidad de datos adicionales, se crea una Serie
                    } else if (tipo.equalsIgnoreCase("Serie") && datosContenido.length >= 3) {
                        int temporadas = Integer.parseInt(datosContenido[0].trim());
                        int eps = Integer.parseInt(datosContenido[1].trim());
                        int duracionEp = Integer.parseInt(datosContenido[2].trim());
                        LocalDate fechaExp = LocalDate.parse(datosContenido[3].trim(), formatoFecha);
                        nuevoContenido = new Serie(id, titulo, anio, genero, fechaExp, temporadas, eps, duracionEp);

                        // // En caso de coincidir el tipo de contenido y la cantidad de datos adicionales, se crea un Documental
                    } else if (tipo.equalsIgnoreCase("Documental") && datosContenido.length >= 2) {
                        int duracionMin = Integer.parseInt(datosContenido[0].trim());
                        String tema = datosContenido[1].trim();
                        nuevoContenido = new Documental(id, titulo, anio, genero, duracionMin, tema);
                    }
                }

                // Se verifica que se haya creado una instancia, y de ser asi, se agrega a la lista de Contenido
                if (nuevoContenido != null) {
                    catalogoContenidos.put(id, nuevoContenido);

                    // Mantenemos el orden de los números mientras añadimos más contenidos
                    try {
                        int idNum = Integer.parseInt(id.replaceAll("[^0-9]", ""));
                        if (idNum >= contadorIdContenido) {
                            contadorIdContenido = idNum + 1;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al cargar contenidos.csv: " + e.getMessage());
            return false;
        }
    }

    /**
     * Proceso de inicio de sesion, comprobando que la clave y el usuario esten atribuidos correctamente.
     *
     * @param rut Rut del usuario
     * @param clave Clave del usuario
     * @return Usuario asociado
     */
    @Override
    public Usuario iniciarSesion(String rut, String clave) {

        // Se verifica que la clave este asociada al rut ingresado
        for (Usuario u : usuarios) {
            if (u.getRut().equalsIgnoreCase(rut) && u.getClave().equals(clave)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Metodo encargado de buscar el contenido asociado al ID
     *
     * @param id Identificdor del contenido
     * @return Contenido asociado
     */
    @Override
    public Contenido buscarContenidoPorId(String id) {

        // Se busca el contenido asociado al ID
        return catalogoContenidos.get(id);
    }

    /**
     * Genera el valor de id disponible, manteniendo el orden en el catalogo
     *
     * @return Id disponible en orden
     */
    private String generarSiguienteId() {

        // Se genera el siguiente ID disponible, para seguir un orden
        return String.valueOf(contadorIdContenido++);
    }

    /**
     * Metodo encargado de registrar una Pelicula nueva, tanto en el catalogo del programa, como en el archivo de texto
     *
     * @param titulo
     * @param anio
     * @param genero
     * @param duracion
     */
    @Override
    public void registrarPelicula(String titulo, int anio, String genero, int duracion) {

        // Se reciben los datos, ademas de generar una fecha de expiracion 30 dias despues de su creacion, y se
        // crea la instancia de Pelicula
        String id = generarSiguienteId();
        LocalDate fechaExp = LocalDate.now().plusDays(30);
        Pelicula p = new Pelicula(id, titulo, anio, genero, fechaExp, duracion);

        // Se agrega la pelicula a la lista de Contenido y al archivo de texto
        catalogoContenidos.put(id, p);
        guardarContenidoEnCSV(p);
    }

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
    @Override
    public void registrarSerie(String titulo, int anio, String genero, int temporadas, int episodios, int duracionEp) {

        // Se reciben los datos, ademas de generar una fecha de expiracion 30 dias despues de su creacion, y se
        // crea la instancia de Serie
        String id = generarSiguienteId();
        LocalDate fechaExp = LocalDate.now().plusDays(30);
        Serie s = new Serie(id, titulo, anio, genero, fechaExp, temporadas, episodios, duracionEp);

        // Se agrega la serie a la lista de Contenido y al archivo de texto
        catalogoContenidos.put(id, s);
        guardarContenidoEnCSV(s);
    }

    /**
     * Metodo encargado de registrar un Documental nuevo, tanto en el catalogo del programa, como en el archivo de texto
     * @param titulo
     * @param anio
     * @param genero
     * @param duracionMin
     * @param tema
     */
    @Override
    public void registrarDocumental(String titulo, int anio, String genero, int duracionMin, String tema) {

        // Se reciben los datos y se crea la instancia de Serie
        String id = generarSiguienteId();
        Documental d = new Documental(id, titulo, anio, genero, duracionMin, tema);

        // Se agrega el documental a la lista de Contenido y al archivo de texto
        catalogoContenidos.put(id, d);
        guardarContenidoEnCSV(d);
    }

    /**
     * Metodo encargado de ordenar los contenidos PPV (con fecha de expiracion) en orden de mas proximo a menos proximo.
     *
     */
    @Override
    public void mostrarProximosAVencer() {

        // Se genera el formato de texto a mostrar en la consola
        System.out.println("\n--- CONTENIDOS PROXIMOS A VENCER ---");

        // Se genera una lista vacia para reunir los contenidos PPV
        List<Contenido> listaLicenciados = new ArrayList<>();

        // Filtrar contenidos PPV (Pelicula y Serie)
        for (Contenido c : catalogoContenidos.values()) {
            if (c instanceof Pelicula || c instanceof Serie) {
                listaLicenciados.add(c);
            }
        }

        // Se verifica que la lista posea contenidos
        if (listaLicenciados.isEmpty()) {
            System.out.println("No hay contenidos licenciados próximos a vencer.");
            return;
        }

        // Ordenar la lista usando la funcion collections.sort, la cual se apoya del metodo compareTo, dentro de
        // la clase contenido
        Collections.sort(listaLicenciados);

        // Se imprime la lista de contenidos PPV en orden, de mas proximo a vencer hasta menos proximo a vencer, con un
        // formato preestablecido
        System.out.printf("%-6s | %-12s | %-25s | %-12s%n", "ID", "TIPO", "TÍTULO", "EXPIRACIÓN");
        System.out.println("--------------------------------------------------------------");
        for (Contenido c : listaLicenciados) {
            LocalDate exp = null;
            if (c instanceof Pelicula) exp = ((Pelicula) c).getFechaExpiracion();
            if (c instanceof Serie) exp = ((Serie) c).getFechaExpiracion();
            System.out.printf("%-6s | %-12s | %-25s | %-12s%n",
                    c.getId(), c.getTipo(), c.getTitulo(), exp != null ? exp.toString() : "N/A");
        }
    }

    /**
     * Metodo encargado de comprobar si un contenido nuevo no existe en el catalogo
     *
     * @param id
     * @param titulo
     * @return Comprobacion si existe o no
     */
    public boolean comprobarContenido(String id, String titulo) {

        // Se verifica que la lista posea el ID, en caso de no ser asi, retorna false
        if (catalogoContenidos.containsKey(id)) {
            return false;
        }

        // Se verifica que la lista posea un contenido del mismo nombre, en caso de no ser asi, retorna false
        for (Contenido c : catalogoContenidos.values()) {
            if (c.getTitulo().equalsIgnoreCase(titulo.trim())) {
                return false; // Ya existe con ese título
            }
        }

        // No se hallo el id o titulo en la lista
        return true;
    }

    /**
     * Metodo encargado de actualizar el archivo de texto con un contenido nuevo
     * @param c Contenido nuevo
     */
    private void guardarContenidoEnCSV(Contenido c) {

        // Se abre el archivo donde se guardara el contenido
        String rutaArchivo = "archivos/contenidos.csv";

        // Se empieza a escribir los contenidos en el archivo de texto
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo, true))) {
            StringBuilder sb = new StringBuilder();
            sb.append(c.getId()).append(";")
                    .append(c.getTitulo()).append(";")
                    .append(c.getTipo()).append(";")
                    .append(c.getAnioLanzamiento()).append(";")
                    .append(c.getGenero()).append(";");

            // Dependiendo del tipo de contenido a agregar, agregamos con unas caracteristicas u otras

            // Pelicula
            if (c instanceof Pelicula) {
                Pelicula p = (Pelicula) c;
                DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                sb.append(p.getFechaExpiracion().format(formatoFecha)).append(",")
                        .append(p.obtenerDuracion());

                // Serie
            } else if (c instanceof Serie) {
                Serie s = (Serie) c;
                DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                sb.append(s.getTemporadas()).append(",")
                        .append(s.getEpisodiosPorTemporada()).append(",")
                        .append(s.getDuracionEpisodio()).append(",")
                        .append(s.getFechaExpiracion().format(formatoFecha));

                // Documental
            } else if (c instanceof Documental) {
                Documental d = (Documental) c;
                sb.append(d.obtenerDuracion()).append(",")
                        .append(d.getTemaPrincipal());
            }
            writer.println(sb.toString());
        } catch (IOException e) {
            System.err.println("Error al guardar el contenido en el archivo CSV: " + e.getMessage());
        }
    }

    /**
     * Muestra los contenidos de cierto tipo o, en caso de no especificarse, muestra todos.
     * @param tipo Tipo de contenido
     */
    public void mostrarContenidos(String tipo) {

        // Si el tipo es vacio, se imprimen todos los contenidos
        if (tipo == null || tipo.trim().isEmpty()) {
            System.out.println("\n=== CATALOGO COMPLETO DE CONTENIDOS ===");
        } else {
            System.out.println("\n=== CATALOGO DE " + tipo.toUpperCase() + "S ===");
        }

        // Se verifica que hayan contenidos en el sistema
        if (catalogoContenidos.isEmpty()) {
            System.out.println("No hay contenidos registrados en el sistema.");
            return;
        }

        // Encabezado del formato de entrega
        System.out.printf("%-6s | %-12s | %-25s | %-30s%n", "ID", "TIPO", "TÍTULO", "DETALLES ESPECÍFICOS");
        System.out.println("-----------------------------------------------------------------------------------------");

        // booleano para ver que se encontro un tipo de contenido valido, de ser asi, se imprimen los contenidos
        boolean encontro = false;
        for (Contenido c : catalogoContenidos.values()) {
            if (tipo == null || tipo.trim().isEmpty() || c.getTipo().equalsIgnoreCase(tipo)) {
                encontro = true;

                //Formato para cada tipo de contenido
                String detalleEspecifico = "";
                if (c instanceof Pelicula) {
                    Pelicula p = (Pelicula) c;
                    detalleEspecifico = "Duración: " + p.obtenerDuracion() + " min";
                } else if (c instanceof Serie) {
                    Serie s = (Serie) c;
                    detalleEspecifico = s.getTemporadas() + " temp, " + s.getEpisodiosPorTemporada() + " eps/temp";
                } else if (c instanceof Documental) {
                    Documental d = (Documental) c;
                    detalleEspecifico = "Mín: " + d.obtenerDuracion() + " min";
                }
                System.out.printf("%-6s | %-12s | %-25s | %-30s%n",
                        c.getId(), c.getTipo(), c.getTitulo(), detalleEspecifico);
            }
        }

        if (!encontro) {
            System.out.println("No hay contenidos registrados del tipo: " + tipo);
        }
        System.out.println("-----------------------------------------------------------------------------------------\n");
    }
}