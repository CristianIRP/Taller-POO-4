package cl.ucn.novastream;

import java.util.Objects;

public class Usuario {


    /* Identificador del usuario */
    private String idUsuario;

    /* Nombre del usuario */
    private String nombre;

    /* RUT del usuario */
    private String rut;

    /* Correo del usuario */
    private String correo;

    /* Clave del usuario */
    private String clave;

    public Usuario(String idUsuario, String nombre, String rut, String correo, String clave) {

        // Se atribuyen los datos de manera directa
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.rut = rut;
        this.correo = correo;
        this.clave = clave;
    }


    /**
     * Obtiene el ID del usuario
     *
     * @return identificador
     */
    public String getIdUsuario() { return idUsuario; }

    /**
     * Obtiene el nombre del usuario
     *
     * @return nombre
     */
    public String getNombre() { return nombre; }

    /**
     * Obtiene el rut del usuario
     *
     * @return rut
     */
    public String getRut() { return rut; }

    /**
     * Obtiene el correo del usuario
     *
     * @return correo
     */
    public String getCorreo() { return correo; }

    /**
     * Obtiene la clave del usuario
     *
     * @return clave
     */
    public String getClave() { return clave; }


    /**
     * Metodo encargado de comparar el usuario con un objeto
     *
     * @param o   the reference object with which to compare.
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(idUsuario, usuario.idUsuario);
    }

    /**
     * Retorna el id asociado a un usuario. Usado en HashList
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(idUsuario);
    }
}