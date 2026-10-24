package org.example.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Solicitud extends DocumentoBase implements Vencible, Filtrable {
    private MedioEntrega medioEntrega;
    private EstadoSolicitud estadoSolicitud;
    private String tipoSolicitud;
    private String evidencia;
    private boolean tieneAcuse;
    private String detalleAcuse;
    private LocalDate plazoVencimiento;
    private PrioridadSolicitud prioridad;

    public Solicitud() {
    }

    public Solicitud(
            int id,
            LocalDate fechaEntrega,
            LocalTime horaEntrega,
            String autoridadReceptora,
            String asunto,
            MedioEntrega medioEntrega,
            EstadoSolicitud estadoSolicitud,
            String tipoSolicitud,
            String evidencia,
            boolean tieneAcuse,
            String detalleAcuse,
            LocalDate plazoVencimiento,
            PrioridadSolicitud prioridad
    ) {
        super(id, fechaEntrega, horaEntrega, autoridadReceptora, asunto);
        setMedioEntrega(medioEntrega);
        setEstadoSolicitud(estadoSolicitud);
        setTipoSolicitud(tipoSolicitud);
        setEvidencia(evidencia);
        setTieneAcuse(tieneAcuse);
        setDetalleAcuse(detalleAcuse);
        setPlazoVencimiento(plazoVencimiento);
        setPrioridad(prioridad);
    }

    public String getMedioEntrega() {
        return medioEntrega == null ? "Sin medio" : medioEntrega.getEtiqueta();
    }

    public MedioEntrega getMedioEntregaValor() {
        return medioEntrega;
    }

    public void setMedioEntrega(MedioEntrega medioEntrega) {
        if (medioEntrega == null) {
            throw new IllegalArgumentException("El medio de entrega es obligatorio.");
        }
        this.medioEntrega = medioEntrega;
    }

    public void setMedioEntrega(String medioEntrega) {
        setMedioEntrega(MedioEntrega.desdeTexto(medioEntrega));
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud == null ? "Sin estado" : estadoSolicitud.getEtiqueta();
    }

    public EstadoSolicitud getEstadoSolicitudValor() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(EstadoSolicitud estadoSolicitud) {
        if (estadoSolicitud == null) {
            throw new IllegalArgumentException("El estado de la solicitud es obligatorio.");
        }
        this.estadoSolicitud = estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        setEstadoSolicitud(EstadoSolicitud.desdeTexto(estadoSolicitud));
    }

    public String getTipoSolicitud() {
        return capitalizar(tipoSolicitud);
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        String valor = limpiarTexto(tipoSolicitud);
        if (valor.length() < 3 || valor.length() > 80) {
            throw new IllegalArgumentException("El tipo de solicitud debe tener entre 3 y 80 caracteres.");
        }
        this.tipoSolicitud = valor;
    }

    public String getEvidencia() {
        return limpiarTexto(evidencia);
    }

    public void setEvidencia(String evidencia) {
        String valor = limpiarTexto(evidencia);
        if (valor.length() < 3 || valor.length() > 255) {
            throw new IllegalArgumentException("La evidencia debe ser una carpeta, folio o link valido.");
        }
        this.evidencia = valor;
    }

    public String getTieneAcuse() {
        return tieneAcuse ? "Si" : "No";
    }

    public boolean getTieneAcuseValor() {
        return tieneAcuse;
    }

    public void setTieneAcuse(boolean tieneAcuse) {
        this.tieneAcuse = tieneAcuse;
        if (!tieneAcuse) {
            this.detalleAcuse = "Sin acuse";
        }
    }

    public String getDetalleAcuse() {
        return limpiarTexto(detalleAcuse);
    }

    public void setDetalleAcuse(String detalleAcuse) {
        if (!tieneAcuse) {
            this.detalleAcuse = "Sin acuse";
            return;
        }

        String valor = limpiarTexto(detalleAcuse);
        if (valor.length() < 2 || valor.length() > 100) {
            throw new IllegalArgumentException("El detalle del acuse debe indicar sello, folio u otra referencia.");
        }
        this.detalleAcuse = valor;
    }

    public String getPlazoVencimiento() {
        return plazoVencimiento == null ? "Sin plazo" : plazoVencimiento.format(FORMATO_FECHA);
    }

    public LocalDate getPlazoVencimientoValor() {
        return plazoVencimiento;
    }

    public void setPlazoVencimiento(LocalDate plazoVencimiento) {
        if (plazoVencimiento == null) {
            throw new IllegalArgumentException("El plazo de vencimiento es obligatorio.");
        }
        if (getFechaEntregaValor() != null && plazoVencimiento.isBefore(getFechaEntregaValor())) {
            throw new IllegalArgumentException("El plazo de vencimiento no puede ser anterior a la fecha de entrega.");
        }
        this.plazoVencimiento = plazoVencimiento;
    }

    public String getPrioridad() {
        return prioridad == null ? "Sin prioridad" : prioridad.getEtiqueta();
    }

    public PrioridadSolicitud getPrioridadValor() {
        return prioridad;
    }

    public void setPrioridad(PrioridadSolicitud prioridad) {
        if (prioridad == null) {
            throw new IllegalArgumentException("La prioridad es obligatoria.");
        }
        this.prioridad = prioridad;
    }

    public void setPrioridad(String prioridad) {
        setPrioridad(PrioridadSolicitud.desdeTexto(prioridad));
    }

    public String getDiasRestantes() {
        long dias = calcularDiasRestantes();
        if (dias < 0) {
            return "Vencida hace " + Math.abs(dias) + " dia(s)";
        }
        if (dias == 0) {
            return "Vence hoy";
        }
        return "Faltan " + dias + " dia(s)";
    }

    @Override
    public long calcularDiasRestantes() {
        if (plazoVencimiento == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), plazoVencimiento);
    }

    @Override
    public String obtenerSemaforoVencimiento() {
        long dias = calcularDiasRestantes();
        if (dias < 0) {
            return "Vencida";
        }
        if (dias <= 3) {
            return "Urgente";
        }
        if (dias <= 10) {
            return "Proxima";
        }
        return "En tiempo";
    }

    @Override
    public boolean coincideConFiltro(String filtro) {
        String valor = limpiarTexto(filtro).toLowerCase();
        if (valor.isEmpty()) {
            return true;
        }

        String datos = (getAutoridadReceptora() + " "
                + getAsunto() + " "
                + getTipoSolicitud() + " "
                + getEvidencia() + " "
                + getDetalleAcuse() + " "
                + getEstadoSolicitud() + " "
                + getPrioridad()).toLowerCase();

        return datos.contains(valor);
    }

    @Override
    public String mostrarTipoDocumento() {
        return "Solicitud";
    }

    @Override
    public String toString() {
        return super.toString() + "\n"
                + "Medio de entrega: " + getMedioEntrega() + "\n"
                + "Estado: " + getEstadoSolicitud() + "\n"
                + "Tipo de solicitud: " + getTipoSolicitud() + "\n"
                + "Evidencia: " + getEvidencia() + "\n"
                + "Tiene acuse: " + getTieneAcuse() + "\n"
                + "Detalle de acuse: " + getDetalleAcuse() + "\n"
                + "Plazo de vencimiento: " + getPlazoVencimiento() + "\n"
                + "Dias restantes: " + getDiasRestantes() + "\n"
                + "Semaforo: " + obtenerSemaforoVencimiento() + "\n"
                + "Prioridad: " + getPrioridad();
    }
}
