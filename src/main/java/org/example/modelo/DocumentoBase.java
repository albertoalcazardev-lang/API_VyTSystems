package org.example.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class DocumentoBase {
    protected static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    protected static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private int id;
    private LocalDate fechaEntrega;
    private LocalTime horaEntrega;
    private String autoridadReceptora;
    private String asunto;

    public DocumentoBase() {
    }

    public DocumentoBase(int id, LocalDate fechaEntrega, LocalTime horaEntrega, String autoridadReceptora, String asunto) {
        setId(id);
        setFechaEntrega(fechaEntrega);
        setHoraEntrega(horaEntrega);
        setAutoridadReceptora(autoridadReceptora);
        setAsunto(asunto);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("El id no puede ser negativo.");
        }
        this.id = id;
    }

    public String getFechaEntrega() {
        return fechaEntrega == null ? "Sin fecha" : fechaEntrega.format(FORMATO_FECHA);
    }

    public LocalDate getFechaEntregaValor() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        if (fechaEntrega == null) {
            throw new IllegalArgumentException("La fecha de entrega es obligatoria.");
        }
        if (fechaEntrega.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de entrega no puede ser futura.");
        }
        if (fechaEntrega.getYear() < 2000) {
            throw new IllegalArgumentException("La fecha de entrega debe ser del anio 2000 en adelante.");
        }
        this.fechaEntrega = fechaEntrega;
    }

    public String getHoraEntrega() {
        return horaEntrega == null ? "Sin hora" : horaEntrega.format(FORMATO_HORA);
    }

    public LocalTime getHoraEntregaValor() {
        return horaEntrega;
    }

    public void setHoraEntrega(LocalTime horaEntrega) {
        if (horaEntrega == null) {
            throw new IllegalArgumentException("La hora de entrega es obligatoria.");
        }
        this.horaEntrega = horaEntrega;
    }

    public String getAutoridadReceptora() {
        return capitalizar(autoridadReceptora);
    }

    public void setAutoridadReceptora(String autoridadReceptora) {
        String valor = limpiarTexto(autoridadReceptora);
        if (valor.length() < 3 || valor.length() > 120) {
            throw new IllegalArgumentException("La autoridad o area receptora debe tener entre 3 y 120 caracteres.");
        }
        this.autoridadReceptora = valor;
    }

    public String getAsunto() {
        return limpiarTexto(asunto);
    }

    public void setAsunto(String asunto) {
        String valor = limpiarTexto(asunto);
        if (valor.length() < 5 || valor.length() > 180) {
            throw new IllegalArgumentException("El asunto debe tener entre 5 y 180 caracteres.");
        }
        this.asunto = valor;
    }

    protected static String limpiarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim().replaceAll("\\s+", " ");
    }

    protected static String capitalizar(String texto) {
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

    public abstract String mostrarTipoDocumento();

    public abstract long calcularDiasRestantes();

    @Override
    public String toString() {
        return "Tipo de documento: " + mostrarTipoDocumento() + "\n"
                + "Id: " + getId() + "\n"
                + "Fecha de entrega: " + getFechaEntrega() + "\n"
                + "Hora de entrega: " + getHoraEntrega() + "\n"
                + "Autoridad o area receptora: " + getAutoridadReceptora() + "\n"
                + "Asunto: " + getAsunto();
    }
}
