package org.example.vista;

import org.example.dao.SolicitudDAO;
import org.example.modelo.Administrador;
import org.example.modelo.DocumentoBase;
import org.example.modelo.EstadoSolicitud;
import org.example.modelo.MedioEntrega;
import org.example.modelo.PrioridadSolicitud;
import org.example.modelo.Solicitud;
import org.example.modelo.Usuario;
import org.example.modelo.UsuarioConsulta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class Menu {
    private static final BufferedReader LEER = new BufferedReader(new InputStreamReader(System.in));
    private static final SolicitudDAO SOLICITUD_DAO = new SolicitudDAO();
    private static final DateTimeFormatter FORMATO_FECHA_ENTRADA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FORMATO_HORA_ENTRADA = DateTimeFormatter.ofPattern("HH:mm");

    private Menu() {
    }

    public static void menu() throws IOException {
        Administrador administrador = new Administrador(1, "Director del medio", "admin@vyt.mx");
        UsuarioConsulta usuarioConsulta = new UsuarioConsulta(2, "Licenciado Fabela", "consulta@vyt.mx");

        int opcion;
        do {
            System.out.println("\n========== VyT Systems ==========");
            System.out.println("1. Entrar como administrador");
            System.out.println("2. Entrar como usuario de consulta");
            System.out.println("3. Salir");
            opcion = leerEntero("Elige una opcion: ");

            switch (opcion) {
                case 1:
                    mostrarUsuario(administrador);
                    menuAdministrador(administrador);
                    break;
                case 2:
                    mostrarUsuario(usuarioConsulta);
                    System.out.println("Modulo de consulta creado, pero pendiente de implementar.");
                    break;
                case 3:
                    System.out.println("Saliendo del sistema.");
                    break;
                default:
                    System.out.println("Opcion invalida.");
                    break;
            }
        } while (opcion != 3);
    }

    private static void menuAdministrador(Administrador administrador) throws IOException {
        if (!administrador.puedeModificarSolicitudes()) {
            System.out.println("Este usuario no tiene permisos para modificar solicitudes.");
            return;
        }

        int opcion;
        do {
            System.out.println("\n========== Menu Administrador ==========");
            System.out.println("1. Agregar nueva solicitud");
            System.out.println("2. Editar solicitud");
            System.out.println("3. Eliminar solicitud");
            System.out.println("4. Visualizar todas las solicitudes");
            System.out.println("5. Filtrar solicitudes");
            System.out.println("6. Volver al menu principal");
            opcion = leerEntero("Elige una opcion: ");

            switch (opcion) {
                case 1:
                    agregarSolicitud();
                    break;
                case 2:
                    editarSolicitud();
                    break;
                case 3:
                    eliminarSolicitud();
                    break;
                case 4:
                    visualizarSolicitudes();
                    break;
                case 5:
                    filtrarSolicitudes();
                    break;
                case 6:
                    System.out.println("Regresando al menu principal.");
                    break;
                default:
                    System.out.println("Opcion invalida.");
                    break;
            }
        } while (opcion != 6);
    }

    private static void agregarSolicitud() throws IOException {
        Solicitud solicitud = capturarSolicitud(0);
        if (SOLICITUD_DAO.agregarSolicitud(solicitud)) {
            System.out.println("Solicitud registrada correctamente.");
        } else {
            System.out.println("No se pudo registrar la solicitud.");
        }
    }

    private static void editarSolicitud() throws IOException {
        int id = leerEntero("Id de la solicitud a editar: ");
        Solicitud solicitudActual = SOLICITUD_DAO.buscarPorId(id);

        if (solicitudActual == null) {
            System.out.println("No existe una solicitud con ese id.");
            return;
        }

        System.out.println("\nSolicitud actual:");
        imprimirDocumento(solicitudActual);
        System.out.println("\nCaptura los nuevos datos de la solicitud.");

        Solicitud solicitudEditada = capturarSolicitud(id);
        if (SOLICITUD_DAO.editarSolicitud(solicitudEditada)) {
            System.out.println("Solicitud actualizada correctamente.");
        } else {
            System.out.println("No se pudo actualizar la solicitud.");
        }
    }

    private static void eliminarSolicitud() throws IOException {
        int id = leerEntero("Id de la solicitud a eliminar: ");
        Solicitud solicitud = SOLICITUD_DAO.buscarPorId(id);

        if (solicitud == null) {
            System.out.println("No existe una solicitud con ese id.");
            return;
        }

        imprimirDocumento(solicitud);
        if (!leerSiNo("Seguro que deseas eliminarla? (s/n): ")) {
            System.out.println("Eliminacion cancelada.");
            return;
        }

        if (SOLICITUD_DAO.eliminarSolicitud(id)) {
            System.out.println("Solicitud eliminada correctamente.");
        } else {
            System.out.println("No se pudo eliminar la solicitud.");
        }
    }

    private static void visualizarSolicitudes() {
        ArrayList<Solicitud> solicitudes = SOLICITUD_DAO.obtenerTodas();
        imprimirSolicitudes(solicitudes);
    }

    private static void filtrarSolicitudes() throws IOException {
        String palabraClave = leerTextoOpcional("Palabra clave (enter para omitir): ");
        String estado = leerFiltroEstado();
        String prioridad = leerFiltroPrioridad();

        ArrayList<Solicitud> solicitudes = SOLICITUD_DAO.filtrarSolicitudes(palabraClave, estado, prioridad);
        imprimirSolicitudes(solicitudes);
    }

    private static Solicitud capturarSolicitud(int id) throws IOException {
        while (true) {
            try {
                LocalDate fechaEntrega = leerFecha("Fecha de entrega (yyyy-MM-dd): ");
                LocalTime horaEntrega = leerHora("Hora de entrega (HH:mm): ");
                String autoridad = leerTextoObligatorio("Autoridad o area receptora: ");
                String asunto = leerTextoObligatorio("Asunto corto: ");
                MedioEntrega medioEntrega = leerMedioEntrega();
                EstadoSolicitud estadoSolicitud = leerEstadoSolicitud();
                String tipoSolicitud = leerTextoObligatorio("Tipo de solicitud: ");
                String evidencia = leerTextoObligatorio("Evidencia (carpeta, folio o link): ");
                boolean tieneAcuse = leerSiNo("Tiene acuse? (s/n): ");
                String detalleAcuse = tieneAcuse
                        ? leerTextoObligatorio("Detalle del acuse (sello, folio, etc.): ")
                        : "Sin acuse";
                LocalDate plazoVencimiento = leerFecha("Plazo de vencimiento (yyyy-MM-dd): ");
                PrioridadSolicitud prioridad = leerPrioridad();

                return new Solicitud(
                        id,
                        fechaEntrega,
                        horaEntrega,
                        autoridad,
                        asunto,
                        medioEntrega,
                        estadoSolicitud,
                        tipoSolicitud,
                        evidencia,
                        tieneAcuse,
                        detalleAcuse,
                        plazoVencimiento,
                        prioridad
                );
            } catch (IllegalArgumentException err) {
                System.out.println("Datos invalidos: " + err.getMessage());
                System.out.println("Captura nuevamente la solicitud.\n");
            }
        }
    }

    private static void imprimirSolicitudes(ArrayList<Solicitud> solicitudes) {
        if (solicitudes.isEmpty()) {
            System.out.println("No hay solicitudes para mostrar.");
            return;
        }

        System.out.println("\n========== Solicitudes ==========");
        for (Solicitud solicitud : solicitudes) {
            imprimirDocumento(solicitud);
            System.out.println("---------------------------------");
        }
    }

    private static void imprimirDocumento(DocumentoBase documento) {
        System.out.println(documento);
    }

    private static void mostrarUsuario(Usuario usuario) {
        System.out.println("\n" + usuario);
    }

    private static MedioEntrega leerMedioEntrega() throws IOException {
        while (true) {
            System.out.println("Medio de entrega:");
            System.out.println("1. Digital");
            System.out.println("2. Impreso");
            int opcion = leerEntero("Elige una opcion: ");
            switch (opcion) {
                case 1:
                    return MedioEntrega.DIGITAL;
                case 2:
                    return MedioEntrega.IMPRESO;
                default:
                    System.out.println("Opcion invalida.");
                    break;
            }
        }
    }

    private static EstadoSolicitud leerEstadoSolicitud() throws IOException {
        while (true) {
            System.out.println("Estado de la solicitud:");
            System.out.println("1. Entregada");
            System.out.println("2. En proceso");
            int opcion = leerEntero("Elige una opcion: ");
            switch (opcion) {
                case 1:
                    return EstadoSolicitud.ENTREGADA;
                case 2:
                    return EstadoSolicitud.EN_PROCESO;
                default:
                    System.out.println("Opcion invalida.");
                    break;
            }
        }
    }

    private static PrioridadSolicitud leerPrioridad() throws IOException {
        while (true) {
            System.out.println("Prioridad:");
            System.out.println("1. Alta");
            System.out.println("2. Media");
            System.out.println("3. Baja");
            int opcion = leerEntero("Elige una opcion: ");
            switch (opcion) {
                case 1:
                    return PrioridadSolicitud.ALTA;
                case 2:
                    return PrioridadSolicitud.MEDIA;
                case 3:
                    return PrioridadSolicitud.BAJA;
                default:
                    System.out.println("Opcion invalida.");
                    break;
            }
        }
    }

    private static String leerFiltroEstado() throws IOException {
        while (true) {
            System.out.println("Filtro por estado:");
            System.out.println("0. Todos");
            System.out.println("1. Entregada");
            System.out.println("2. En proceso");
            int opcion = leerEntero("Elige una opcion: ");
            switch (opcion) {
                case 0:
                    return "";
                case 1:
                    return EstadoSolicitud.ENTREGADA.name();
                case 2:
                    return EstadoSolicitud.EN_PROCESO.name();
                default:
                    System.out.println("Opcion invalida.");
                    break;
            }
        }
    }

    private static String leerFiltroPrioridad() throws IOException {
        while (true) {
            System.out.println("Filtro por prioridad:");
            System.out.println("0. Todas");
            System.out.println("1. Alta");
            System.out.println("2. Media");
            System.out.println("3. Baja");
            int opcion = leerEntero("Elige una opcion: ");
            switch (opcion) {
                case 0:
                    return "";
                case 1:
                    return PrioridadSolicitud.ALTA.name();
                case 2:
                    return PrioridadSolicitud.MEDIA.name();
                case 3:
                    return PrioridadSolicitud.BAJA.name();
                default:
                    System.out.println("Opcion invalida.");
                    break;
            }
        }
    }

    private static String leerTextoObligatorio(String mensaje) throws IOException {
        while (true) {
            String valor = leerTextoOpcional(mensaje);
            if (!valor.isBlank()) {
                return valor;
            }
            System.out.println("Este dato es obligatorio.");
        }
    }

    private static String leerTextoOpcional(String mensaje) throws IOException {
        System.out.print(mensaje);
        String valor = LEER.readLine();
        return valor == null ? "" : valor.trim();
    }

    private static int leerEntero(String mensaje) throws IOException {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(LEER.readLine().trim());
            } catch (NumberFormatException err) {
                System.out.println("Debes ingresar un numero entero.");
            }
        }
    }

    private static LocalDate leerFecha(String mensaje) throws IOException {
        while (true) {
            try {
                System.out.print(mensaje);
                return LocalDate.parse(LEER.readLine().trim(), FORMATO_FECHA_ENTRADA);
            } catch (DateTimeParseException err) {
                System.out.println("Formato invalido. Usa yyyy-MM-dd, ejemplo: 2026-08-03.");
            }
        }
    }

    private static LocalTime leerHora(String mensaje) throws IOException {
        while (true) {
            try {
                System.out.print(mensaje);
                return LocalTime.parse(LEER.readLine().trim(), FORMATO_HORA_ENTRADA);
            } catch (DateTimeParseException err) {
                System.out.println("Formato invalido. Usa HH:mm, ejemplo: 14:30.");
            }
        }
    }

    private static boolean leerSiNo(String mensaje) throws IOException {
        while (true) {
            System.out.print(mensaje);
            String valor = LEER.readLine().trim().toLowerCase();
            if (valor.equals("s") || valor.equals("si")) {
                return true;
            }
            if (valor.equals("n") || valor.equals("no")) {
                return false;
            }
            System.out.println("Responde con s o n.");
        }
    }
}
