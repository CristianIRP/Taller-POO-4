package cl.ucn.novastream;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Clase que representa el reporte de reproducción generado cuando un usuario
 * visualiza contenidos en NovaStream.
 */


public class ReporteReproduccion {

    /* Identificador del reporte */
    private String idReporte;

    /* Nombre del usuario asociado al reporte */
    private String nombreUsuario;

    /* Fecha del reporte */
    private LocalDate fecha;

    /* Contenidos del reporte */
    private List<Contenido> contenidos;

    /* Suma de la duracion de los contenidos del reporte */
    private int duracionTotal;

    /**
     * Constructor para generar un nuevo reporte de reproducción.
     *
     * @param nombreUsuario nombre del usuario registrado que realiza la sesion
     * @param contenidos    lista de contenidos seleccionados para visualizar
     */
    public ReporteReproduccion(String nombreUsuario, List<Contenido> contenidos) {
        // Genera ID al azar de 12 dígitos
        this.idReporte = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        this.nombreUsuario = nombreUsuario;
        this.fecha = LocalDate.now();
        this.contenidos = contenidos;
        this.duracionTotal = calcularDuracionTotal();
    }

    /**
     * Calcula la suma acumulada de la duracion de los contenidos elegidos
     *
     * @return total de minutos acumulados
     */
    private int calcularDuracionTotal() {
        int suma = 0;
        for (Contenido c : contenidos) {
            suma += c.obtenerDuracion(); // Método polimórfico
        }
        return suma;
    }

    /**
     * Muestra el reporte en consola, siguiendo el mismo formato usado en el archivo de texto
     */
    public void mostrarReporte() {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.println("+--------------------------------------------------+");
        System.out.println("|                     REPORTE                      |");
        System.out.println("+--------------------------------------------------+");
        System.out.println("Id: " + this.idReporte);
        System.out.println("Usuario: " + this.nombreUsuario);
        System.out.println("Fecha: " + this.fecha.format(formatoFecha));
        System.out.println("----------------------------------------------------");

        for (Contenido c : contenidos) {
            String etiqueta = c.getTitulo() + " (" + c.getTipo() + ")";
            System.out.printf("%-40s %d min%n", etiqueta, c.obtenerDuracion());
        }

        System.out.println("----------------------------------------------------");
        System.out.printf("%-40s %d min%n", "Duración total:", this.duracionTotal);
        System.out.println("+--------------------------------------------------+");
    }

    public void guardarEnArchivo() {
        String nombreArchivo = "archivos/" + this.idReporte + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            writer.println("+--------------------------------------------------+");
            writer.println("|                     REPORTE                      |");
            writer.println("+--------------------------------------------------+");
            writer.println("Id: " + this.idReporte);
            writer.println("Usuario: " + this.nombreUsuario);
            writer.println("Fecha: " + this.fecha.format(formatoFecha));
            writer.println("----------------------------------------------------");

            for (Contenido c : contenidos) {
                String etiqueta = c.getTitulo() + " (" + c.getTipo() + ")";
                writer.printf("%-40s %d min%n", etiqueta, c.obtenerDuracion());
            }

            writer.println("----------------------------------------------------");
            writer.printf("%-40s %d min%n", "Duración total:", this.duracionTotal);
            writer.println("+--------------------------------------------------+");

            System.out.println(">> Archivo de reporte generado: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo de reporte: " + e.getMessage());
        }
    }

    /**
     * Obtiene el Id del reporte
     *
     * @return identificador
     */
    public String getIdReporte() {
        return idReporte;
    }

    /**
     * Obtiene el nombre del usuario
     *
     * @return nombre
     */
    public String getNombreUsuario() { return nombreUsuario; }

    /**
     * Obtiene la fecha del reporte
     *
     * @return fecha
     */
    public LocalDate getFecha() { return fecha; }

    /**
     * Obtiene los contenidos del reporte
     *
     * @return lista de contenidos
     */
    public List<Contenido> getContenidos() { return contenidos; }

    /**
     * Obtiene la duracion total del reporte
     *
     * @return suma de las duraciones de los contenidos
     */
    public int getDuracionTotal() { return duracionTotal; }
}