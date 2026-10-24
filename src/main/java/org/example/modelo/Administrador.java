package org.example.modelo;

public class Administrador extends Usuario implements GestionadorSolicitudes {
    public Administrador() {
        setRol(RolUsuario.ADMINISTRADOR);
    }

    public Administrador(int idUsuario, String nombre, String correo) {
        super(idUsuario, nombre, correo, RolUsuario.ADMINISTRADOR);
    }

    @Override
    public String mostrarTipoUsuario() {
        return "Administrador";
    }

    @Override
    public String obtenerPermisos() {
        return "Agregar, editar, eliminar, visualizar y filtrar solicitudes.";
    }

    @Override
    public String obtenerModuloAsignado() {
        return "Modulo CRUD de solicitudes";
    }

    @Override
    public boolean puedeModificarSolicitudes() {
        return true;
    }
}
