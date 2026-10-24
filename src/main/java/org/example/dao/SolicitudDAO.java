package org.example.dao;

import org.example.config.Conexion;
import org.example.modelo.EstadoSolicitud;
import org.example.modelo.PrioridadSolicitud;
import org.example.modelo.Solicitud;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Time;
import java.util.ArrayList;

public class SolicitudDAO {
    public boolean agregarSolicitud(Solicitud solicitud) {
        String sql = "INSERT INTO Solicitud "
                + "(fecha_entrega, hora_entrega, autoridad_receptora, asunto, medio_entrega, "
                + "estado_solicitud, tipo_solicitud, evidencia, tiene_acuse, detalle_acuse, "
                + "plazo_vencimiento, prioridad) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement stm = conexion.prepareStatement(sql)) {

            cargarParametrosSolicitud(stm, solicitud);
            return stm.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException err) {
            System.out.println("No se guardo la solicitud porque parece duplicada: " + err.getMessage());
        } catch (SQLException err) {
            System.out.println("Error al agregar solicitud: " + err.getMessage());
        } catch (IllegalArgumentException err) {
            System.out.println("Datos invalidos: " + err.getMessage());
        }
        return false;
    }

    public boolean editarSolicitud(Solicitud solicitud) {
        String sql = "UPDATE solicitudes SET "
                + "fecha_entrega = ?, hora_entrega = ?, autoridad_receptora = ?, asunto = ?, "
                + "medio_entrega = ?, estado_solicitud = ?, tipo_solicitud = ?, evidencia = ?, "
                + "tiene_acuse = ?, detalle_acuse = ?, plazo_vencimiento = ?, prioridad = ? "
                + "WHERE id_solicitud = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement stm = conexion.prepareStatement(sql)) {

            cargarParametrosSolicitud(stm, solicitud);
            stm.setInt(13, solicitud.getId());
            return stm.executeUpdate() > 0;
        } catch (SQLException err) {
            System.out.println("Error al editar solicitud: " + err.getMessage());
        } catch (IllegalArgumentException err) {
            System.out.println("Datos invalidos: " + err.getMessage());
        }
        return false;
    }

    public boolean eliminarSolicitud(int idSolicitud) {
        String sql = "DELETE FROM solicitudes WHERE id_solicitud = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement stm = conexion.prepareStatement(sql)) {

            stm.setInt(1, idSolicitud);
            return stm.executeUpdate() > 0;
        } catch (SQLException err) {
            System.out.println("Error al eliminar solicitud: " + err.getMessage());
        }
        return false;
    }

    public ArrayList<Solicitud> obtenerTodas() {
        ArrayList<Solicitud> solicitudes = new ArrayList<>();
        String sql = "SELECT * FROM Solicitud "
                + "ORDER BY CASE prioridad "
                + "WHEN 'ALTA' THEN 1 WHEN 'MEDIA' THEN 2 ELSE 3 END, plazo_vencimiento ASC";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement stm = conexion.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                solicitudes.add(mapearSolicitud(rs));
            }
        } catch (SQLException err) {
            System.out.println("Error al consultar solicitudes: " + err.getMessage());
        }
        return solicitudes;
    }

    public Solicitud buscarPorId(int idSolicitud) {
        String sql = "SELECT * FROM Solicitud WHERE id_solicitud = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement stm = conexion.prepareStatement(sql)) {

            stm.setInt(1, idSolicitud);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapearSolicitud(rs);
                }
            }
        } catch (SQLException err) {
            System.out.println("Error al buscar solicitud: " + err.getMessage());
        }
        return null;
    }

    public ArrayList<Solicitud> filtrarSolicitudes(String palabraClave, String estado, String prioridad) {
        ArrayList<Solicitud> solicitudes = new ArrayList<>();
        ArrayList<String> parametros = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM solicitudes WHERE 1 = 1");

        if (palabraClave != null && !palabraClave.trim().isEmpty()) {
            sql.append(" AND (autoridad_receptora LIKE ? OR asunto LIKE ? OR tipo_solicitud LIKE ? ")
                    .append("OR evidencia LIKE ? OR detalle_acuse LIKE ?)");
            String busqueda = "%" + palabraClave.trim() + "%";
            parametros.add(busqueda);
            parametros.add(busqueda);
            parametros.add(busqueda);
            parametros.add(busqueda);
            parametros.add(busqueda);
        }

        if (estado != null && !estado.trim().isEmpty()) {
            sql.append(" AND estado_solicitud = ?");
            parametros.add(EstadoSolicitud.desdeTexto(estado).name());
        }

        if (prioridad != null && !prioridad.trim().isEmpty()) {
            sql.append(" AND prioridad = ?");
            parametros.add(PrioridadSolicitud.desdeTexto(prioridad).name());
        }

        sql.append(" ORDER BY plazo_vencimiento ASC");

        try (Connection conexion = Conexion.conectar();
             PreparedStatement stm = conexion.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                stm.setString(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    solicitudes.add(mapearSolicitud(rs));
                }
            }
        } catch (SQLException err) {
            System.out.println("Error al filtrar solicitudes: " + err.getMessage());
        } catch (IllegalArgumentException err) {
            System.out.println("Filtro invalido: " + err.getMessage());
        }

        return solicitudes;
    }

    private void cargarParametrosSolicitud(PreparedStatement stm, Solicitud solicitud) throws SQLException {
        stm.setDate(1, Date.valueOf(solicitud.getFechaEntregaValor()));
        stm.setTime(2, Time.valueOf(solicitud.getHoraEntregaValor()));
        stm.setString(3, solicitud.getAutoridadReceptora());
        stm.setString(4, solicitud.getAsunto());
        stm.setString(5, solicitud.getMedioEntregaValor().name());
        stm.setString(6, solicitud.getEstadoSolicitudValor().name());
        stm.setString(7, solicitud.getTipoSolicitud());
        stm.setString(8, solicitud.getEvidencia());
        stm.setBoolean(9, solicitud.getTieneAcuseValor());
        stm.setString(10, solicitud.getDetalleAcuse());
        stm.setDate(11, Date.valueOf(solicitud.getPlazoVencimientoValor()));
        stm.setString(12, solicitud.getPrioridadValor().name());
    }

    private Solicitud mapearSolicitud(ResultSet rs) throws SQLException {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(rs.getInt("id_solicitud"));
        solicitud.setFechaEntrega(rs.getDate("fecha_entrega").toLocalDate());
        solicitud.setHoraEntrega(rs.getTime("hora_entrega").toLocalTime());
        solicitud.setAutoridadReceptora(rs.getString("autoridad_receptora"));
        solicitud.setAsunto(rs.getString("asunto"));
        solicitud.setMedioEntrega(rs.getString("medio_entrega"));
        solicitud.setEstadoSolicitud(rs.getString("estado_solicitud"));
        solicitud.setTipoSolicitud(rs.getString("tipo_solicitud"));
        solicitud.setEvidencia(rs.getString("evidencia"));
        solicitud.setTieneAcuse(rs.getBoolean("tiene_acuse"));
        solicitud.setDetalleAcuse(rs.getString("detalle_acuse"));
        solicitud.setPlazoVencimiento(rs.getDate("plazo_vencimiento").toLocalDate());
        solicitud.setPrioridad(rs.getString("prioridad"));
        return solicitud;
    }
}
