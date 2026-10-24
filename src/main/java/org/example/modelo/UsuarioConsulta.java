package org.example.modelo;

public class UsuarioConsulta extends Usuario {
    public UsuarioConsulta() {
        setRol(RolUsuario.CONSULTA);
    }

    public UsuarioConsulta(int idUsuario, String nombre, String correo) {
        super(idUsuario, nombre, correo, RolUsuario.CONSULTA);
    }

    @Override
    public String mostrarTipoUsuario() {
        return "Usuario de consulta";
    }

    @Override
    public String obtenerPermisos() {
        return "Solo lectura. Este modulo queda creado, pero pendiente de implementar.";
    }
}
