package cl.ucn.novastream;

import cl.ucn.novastream.Contenido;
import cl.ucn.novastream.ReporteReproduccion;
import cl.ucn.novastream.Usuario;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class Main {

    // Instancias necesarias para el uso de Singleton y el funcionamiento del programa
    private static SistemaNovaStream sistema = SistemaNovaStreamImpl.getInstance(); // Ejemplo con Singleton
    private static Scanner scanner = new Scanner(System.in);
    private static Usuario usuarioLogueado = null;

    /**
     * Metodo main encargado del funcionamiento en consola del programa
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("=== Sistema NovaStream ===");

        // Carga inicial de datos desde archivos CSV
        boolean cargaU = sistema.cargarUsuarios("archivos/usuarios.csv");
        boolean cargaC = sistema.cargarContenidos("archivos/contenidos.csv");

        // Texto de confirmacion de la correcta lectura de datos
        if (cargaU && cargaC) {
            System.out.println("Datos cargados correctamente en memoria.\n");
        } else {
            System.out.println("Advertencia: Hubo problemas al cargar algunos archivos locales.\n");
        }

        // Menu de inicio de sesion
        int opcion = -1;
        do {
            System.out.println("=====================================");
            System.out.println("          Sistema NovaStream         ");
            System.out.println("=====================================");
            System.out.println("1. Iniciar sesion");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            // Derivacion a cada metodo dependiendo de la opcion ingresada
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
                switch (opcion) {
                    case 1:
                        menuLogin();
                        break;
                    case 0:
                        System.out.println("Saliendo de la aplicacion...");
                        break;
                    default:
                        System.out.println("Opcion invalida. Intente de nuevo.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un numero valido.\n");
            }

            // Si la opcion es igual a 0, se termina el programa
        } while (opcion != 0);
    }

    /**
     * Autentica al usuario en el sistema con su RUT y Contraseña.
     */
    private static void menuLogin() {

        // Se registran los datos ingresados por el usuario
        System.out.println("\n--- INICIO DE SESION ---");
        System.out.print("Ingrese su RUT: ");
        String rut = scanner.nextLine().trim();
        System.out.print("Ingrese su contraseña: ");
        String clave = scanner.nextLine().trim();

        // Se valida que exista el usuario y la clave sea correcta
        usuarioLogueado = sistema.iniciarSesion(rut, clave);

        // Si existe un usuario, se inicia el menu principal, de lo contrario se termina el metodo
        if (usuarioLogueado != null) {
            System.out.println("\n¡Bienvenido/a " + usuarioLogueado.getNombre() + "!");
            desplegarMenuContenido(); // Pasa al menú de la plataforma
        } else {
            System.out.println("Credenciales incorrectas o usuario inexistente.\n");
        }
    }

    /**
     * Menú que se despliega únicamente cuando un usuario ha iniciado sesión.
     */
    private static void desplegarMenuContenido() {

        // Se crea una opcion previa para el correcto ingreso de la opcion del usuario, y se imprime el formato del menu
        int opcion = -1;
        do {
            System.out.println("\n=====================================");
            System.out.println("           MENU CONTENIDO            ");
            System.out.println("=====================================");
            System.out.println("1. Registrar contenido");
            System.out.println("2. Proximas a vencer");
            System.out.println("3. Reproduccion");
            System.out.println("0. Cerrar sesion");
            System.out.print("Seleccione una opcion: ");

            // Derivacion de cada metodo dependiendo de la opcion ingresada en el menu principal
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
                switch (opcion) {
                    case 1:
                        menuRegistrarContenido();
                        break;
                    case 2:
                        sistema.mostrarProximosAVencer();
                        break;
                    case 3:
                        ejecutarReproduccion();
                        break;
                    case 4:
                        sistema.mostrarContenidos(null);
                        break;
                    case 0:
                        usuarioLogueado = null;
                        System.out.println("Sesion cerrada exitosamente.");
                        break;
                    default:
                        System.out.println("Opcion invalida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un numero valido.");
            }

            //
        } while (opcion != 0);
    }

    /**
     * registrar contenido nuevo
     */
    private static void menuRegistrarContenido() {

        // Se imprimen las opciones de tipo de contenido
        System.out.println("\n--- Registrar contenido ---");
        System.out.println("Seleccione el Tipo:");
        System.out.println("1. Pelicula");
        System.out.println("2. Serie");
        System.out.println("3. Documental");
        System.out.print("Tipo: ");

        // Se registra la opcion ingresada del usuario
        int tipo = 0;
        try {
            tipo = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Tipo invalido.");
            return;
        }

        // Se piden los datos comunes de todo contenido
        System.out.print("Titulo: ");
        String titulo = scanner.nextLine().trim();
        System.out.print("Año: ");
        int anio = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Genero: ");
        String genero = scanner.nextLine().trim();

        //Se ingresan distintos datos dependiendo del tipo de contenido

        if (tipo == 1) { // Película
            System.out.print("Duracion (minutos): ");
            int duracion = Integer.parseInt(scanner.nextLine().trim());
            sistema.registrarPelicula(titulo, anio, genero, duracion);
        } else if (tipo == 2) { // Serie
            System.out.print("Temporadas: ");
            int temporadas = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Episodios por temporada: ");
            int eps = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Duracion por episodio (minutos): ");
            int duracionEp = Integer.parseInt(scanner.nextLine().trim());
            sistema.registrarSerie(titulo, anio, genero, temporadas, eps, duracionEp);
        } else if (tipo == 3) { // Documental
            System.out.print("Duracion minima (minutos): ");
            int duracionMin = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Tema principal: ");
            String tema = scanner.nextLine().trim();
            sistema.registrarDocumental(titulo, anio, genero, duracionMin, tema);
        } else {
            System.out.println("opcion de tipo invalida.");
            return;
        }
        System.out.println("contenido registrado.");
    }

    /**
     * seleccionar contenidos por medio del ID y dar el reporte solicitado
     */
    private static void ejecutarReproduccion() {

        // Se crea una lista vacia para los contenidos visualizados y una opcion provisiona
        List<Contenido> contenidosSeleccionados = new ArrayList<>();
        int opcionTipo = -1;

        // Se imprime el encabezado de el menu
        System.out.println("\n=== REGISTRAR REPRODUCCION ===");
        System.out.println("Ingrese los contenidos que desea reproducir. (Ingrese 0 en la categoría para terminar).");

        while (true) {

            // Se escoge un tipo de contenido para filtrar
            System.out.println("\n¿Qué tipo de contenido deseas ver?");
            System.out.println("1. Peliculas");
            System.out.println("2. Series");
            System.out.println("3. Documentales");
            System.out.println("4. Ver todo el catalogo");
            System.out.println("0. Terminar proceso");
            System.out.print("Seleccione una opcion: ");

            // Se registra la opcion ingresada
            try {
                opcionTipo = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Intente de nuevo.");
                continue;
            }

            // Si el usuario ingresa 0 en el menú de categorías, salimos para generar el reporte
            if (opcionTipo == 0) {
                break;
            }

            // Mostrar el catálogo según la categoría seleccionada
            switch (opcionTipo) {
                case 1:
                    sistema.mostrarContenidos("Pelicula");
                    break;
                case 2:
                    sistema.mostrarContenidos("Serie");
                    break;
                case 3:
                    sistema.mostrarContenidos("Documental");
                    break;
                case 4:
                    sistema.mostrarContenidos(null);
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    continue;
            }

            // Pedir el ID del contenido a añadir
            System.out.print("Ingrese el ID del contenido que desea añadir (o 0 para volver a elegir categoría): ");
            String idIngresado = scanner.nextLine().trim();

            // Si no se ingresa 0, se repite el proceso
            if (idIngresado.equals("0")) {
                continue;
            }

            // Buscamos el contenido en el sistema
            Contenido contenidoSeleccionado = sistema.buscarContenidoPorId(idIngresado.toUpperCase());

            // Se agrega el contenido a la lista de los contenidos visualizados, junto a un mensaje de confirmacion
            if (contenidoSeleccionado != null) {
                contenidosSeleccionados.add(contenidoSeleccionado);
                System.out.println(">> Añadido exitosamente: " + contenidoSeleccionado.getTitulo());
            } else {
                System.out.println("No se encontró ningún contenido con el ID: " + idIngresado);
            }
        }

        // Si se selecciono al menos un contenido, generamos, mostramos y guardamos el reporte
        if (!contenidosSeleccionados.isEmpty()) {
            ReporteReproduccion reporte = new ReporteReproduccion(usuarioLogueado.getNombre(), contenidosSeleccionados);
            reporte.mostrarReporte();
            reporte.guardarEnArchivo(); // Guarda el archivo <uuid>.txt en la carpeta archivos/
        } else {
            System.out.println("No se seleccionó ningún contenido para la reproducción.");
        }
    }
}