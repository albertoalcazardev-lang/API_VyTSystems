package org.example.modelo;

public abstract class Usuario {
    private int idUsuario;
    private String nombre;
    private String correo;
    private RolUsuario rol;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombre, String correo, RolUsuario rol) {
        setIdUsuario(idUsuario);
        setNombre(nombre);
        setCorreo(correo);
        setRol(rol);
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        if (idUsuario < 0) {
            throw new IllegalArgumentException("El id del usuario no puede ser negativo.");
        }
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return capitalizar(nombre);
    }

    public void setNombre(String nombre) {
        String valor = limpiarTexto(nombre);
        if (valor.length() < 3 || valor.length() > 100) {
            throw new IllegalArgumentException("El nombre del usuario debe tener entre 3 y 100 caracteres.");
        }
        this.nombre = valor;
    }

    public String getCorreo() {
        return limpiarTexto(correo).toLowerCase();
    }

    public void setCorreo(String correo) {
        String valor = limpiarTexto(correo);
        if (!valor.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("El correo del usuario no tiene un formato valido.");
        }
        this.correo = valor;
    }

    public String getRol() {
        return rol == null ? "Sin rol" : rol.getEtiqueta();
    }

    public RolUsuario getRolValor() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        if (rol == null) {
            throw new IllegalArgumentException("El rol del usuario es obligatorio.");
        }
        this.rol = rol;
    }

    private static String limpiarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim().replaceAll("\\s+", " ");
    }

    private static String capitalizar(String texto) {
        String valor = limpiarTexto(texto).toLowerCase();
        if (valor.isEmpty()) {
            return "";
        }

        StringBuilder resultado = new StringBuilder();
        boolean nuevaPalabra = true;
        for (char caracter : valor.toCharArray()) {
            if (Character.isWhitespace(caracter)) {
                nuevaPalabra = true;
                resultado.append(caracter);
            } else if (nuevaPalabra) {
                resultado.append(Character.toUpperCase(caracter));
                nuevaPalabra = false;
            } else {
                resultado.append(caracter);
            }
        }
        return resultado.toString();
    }

    public abstract String mostrarTipoUsuario();

    public abstract String obtenerPermisos();

    @Override
    public String toString() {
        return "Tipo de usuario: " + mostrarTipoUsuario() + "\n"
                + "Id usuario: " + getIdUsuario() + "\n"
                + "Nombre: " + getNombre() + "\n"
                + "Correo: " + getCorreo() + "\n"
                + "Rol: " + getRol() + "\n"
                + "Permisos: " + obtenerPermisos();
    }
}
