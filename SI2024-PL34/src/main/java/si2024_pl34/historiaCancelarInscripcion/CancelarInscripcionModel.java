package si2024_pl34.historiaCancelarInscripcion;

import java.util.*;
import si2024_pl34.util.Database;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CancelarInscripcionModel {

    private final Database db = new Database();

    // DTO para mostrar los cursos en la tabla
    public static class CursoInscritoDTO {
        private int idInscripcion;
        private int idCurso;
        private String titulo;
        private String fechaInicio;
        private String fechaFin;
        private String estadoInscripcion;
        private String fechaInscripcion;
        private String metodoPago;
        private String fechaPago;
        private double cantidadPagada;
        private String fechaCancelacion;
        private double cantidadDevuelta;
        private boolean permiteCancelacion;
        private String fechaLimiteCancelacion;
        private int nListaEspera;
        private int porcentajeDevolucion; // Nuevo campo para el porcentaje de devolución

        // Getters y setters
        public int getIdInscripcion() { return idInscripcion; }
        public void setIdInscripcion(int idInscripcion) { this.idInscripcion = idInscripcion; }
        public int getIdCurso() { return idCurso; }
        public void setIdCurso(int idCurso) { this.idCurso = idCurso; }
        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }
        public String getFechaInicio() { return fechaInicio; }
        public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
        public String getFechaFin() { return fechaFin; }
        public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
        public String getEstadoInscripcion() { return estadoInscripcion; }
        public void setEstadoInscripcion(String estadoInscripcion) { this.estadoInscripcion = estadoInscripcion; }
        public String getFechaInscripcion() { return fechaInscripcion; }
        public void setFechaInscripcion(String fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
        public String getMetodoPago() { return metodoPago; }
        public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
        public String getFechaPago() { return fechaPago; }
        public void setFechaPago(String fechaPago) { this.fechaPago = fechaPago; }
        public double getCantidadPagada() { return cantidadPagada; }
        public void setCantidadPagada(double cantidadPagada) { this.cantidadPagada = cantidadPagada; }
        public String getFechaCancelacion() { return fechaCancelacion; }
        public void setFechaCancelacion(String fechaCancelacion) { this.fechaCancelacion = fechaCancelacion; }
        public double getCantidadDevuelta() { return cantidadDevuelta; }
        public void setCantidadDevuelta(double cantidadDevuelta) { this.cantidadDevuelta = cantidadDevuelta; }
        public boolean isPermiteCancelacion() { return permiteCancelacion; }
        public void setPermiteCancelacion(boolean permiteCancelacion) { this.permiteCancelacion = permiteCancelacion; }
        public String getFechaLimiteCancelacion() { return fechaLimiteCancelacion; }
        public void setFechaLimiteCancelacion(String fechaLimiteCancelacion) { this.fechaLimiteCancelacion = fechaLimiteCancelacion; }
        public int getNListaEspera() { return nListaEspera; }
        public void setNListaEspera(int nListaEspera) { this.nListaEspera = nListaEspera; }
        public int getPorcentajeDevolucion() { return porcentajeDevolucion; }
        public void setPorcentajeDevolucion(int porcentajeDevolucion) { this.porcentajeDevolucion = porcentajeDevolucion; }
    }

    // Buscar colegiado por número, devuelve id o -1 si no existe
    public int buscarColegiadoPorNumero(String numeroColegiado) {
        String sql = "SELECT id FROM colegiado WHERE numero_colegiado = ?";
        try {
            List<Map<String, Object>> res = db.executeQueryMap(sql, numeroColegiado);
            if (!res.isEmpty()) {
                return Integer.parseInt(res.get(0).get("id").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Listar cursos a los que está inscrito el colegiado (incluyendo todos los estados)
    public List<CursoInscritoDTO> listarCursosInscritos(int idColegiado) {
        String sql = "SELECT ins.id as idInscripcion, c.id as idCurso, c.titulo, c.fecha_inicio as fechaInicio, c.fecha_fin as fechaFin, " +
                "ins.estado as estadoInscripcion, ins.fecha_inscripcion as fechaInscripcion, ins.metodo_pago as metodoPago, " +
                "ins.fecha_pago as fechaPago, ins.cantidad_pagada as cantidadPagada, ins.fecha_cancelacion as fechaCancelacion, " +
                "ins.cantidad_devolucion as cantidadDevuelta, c.permite_cancelacion as permiteCancelacion, c.fecha_limite_cancelacion as fechaLimiteCancelacion, " +
                "ins.n_lista_espera as nListaEspera, c.porcentaje_devolucion as porcentajeDevolucion " +
                "FROM inscripciones_curso ins " +
                "JOIN cuota cu ON ins.id_cuota = cu.id " +
                "JOIN cursos c ON cu.id_curso = c.id " +
                "WHERE ins.id_colegiado = ?";
        try {
            return db.executeQueryPojo(CursoInscritoDTO.class, sql, idColegiado);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private int getNumeroListaEspera(int idInscripcion){
        int listaEspera = 0;
        String sql = "SELECT n_lista_espera FROM inscripciones_curso WHERE id = ?";
        try {
            List<Map<String, Object>> res = db.executeQueryMap(sql, idInscripcion);
            if (!res.isEmpty() && res.get(0).get("n_lista_espera") != null) {
                listaEspera = Integer.parseInt(res.get(0).get("n_lista_espera").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEspera;
    }

    private int getIdCurso(int idInscripcion){
        int idCurso = 0;
        // Modificada la consulta para obtener el id_curso a través de la tabla cuota
        String sql = "SELECT c.id_curso FROM inscripciones_curso ins JOIN cuota c ON ins.id_cuota = c.id WHERE ins.id = ?";
        try {
            List<Map<String, Object>> res = db.executeQueryMap(sql, idInscripcion);
            if (!res.isEmpty()) {
                idCurso = Integer.parseInt(res.get(0).get("id_curso").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idCurso;
    }

    // Cancelar inscripción si está permitido y en plazo
    public String cancelarInscripcion(int idInscripcion, boolean permiteCancelacion, String fechaLimiteCancelacion) {
        if (!permiteCancelacion) return null;
        if (fechaLimiteCancelacion == null) return null;
        String hoy = java.time.LocalDate.now().toString();
        if (hoy.compareTo(fechaLimiteCancelacion) > 0) return null; // fuera de plazo

        try {
            // Obtener el número de lista de espera antes de cualquier actualización
            int numeroListaEspera = getNumeroListaEspera(idInscripcion);
            
            // Obtener el id del curso para actualizar la lista de espera
            int idCurso = getIdCurso(idInscripcion);

            // Obtener toda la información relevante para el justificante
            String sqlInfo = "SELECT ins.cantidad_pagada, c.porcentaje_devolucion, c.titulo, ins.id_colegiado, ins.fecha_cancelacion, " +
                             "c.fecha_inicio, c.fecha_fin, ins.fecha_inscripcion, ins.metodo_pago, ins.fecha_pago, ins.estado, " +
                             "co.nombre, co.apellidos, co.dni " +
                             "FROM inscripciones_curso ins " +
                             "JOIN cuota cu ON ins.id_cuota = cu.id " +
                             "JOIN cursos c ON cu.id_curso = c.id " +
                             "JOIN colegiado co ON ins.id_colegiado = co.id " +
                             "WHERE ins.id = ?";
            List<Map<String, Object>> infoResult = db.executeQueryMap(sqlInfo, idInscripcion);

            if (infoResult.isEmpty()) return null;

            Map<String, Object> info = infoResult.get(0);
            double cantidadPagada = Double.parseDouble(info.get("cantidad_pagada").toString());
            int porcentajeDevolucion = 0;
            if (info.containsKey("porcentaje_devolucion") && info.get("porcentaje_devolucion") != null) {
                porcentajeDevolucion = Integer.parseInt(info.get("porcentaje_devolucion").toString());
            }
            double cantidadDevolucion = cantidadPagada * porcentajeDevolucion / 100.0;

            // Si el usuario estaba en lista de espera, actualizar la lista
            if (numeroListaEspera > 0) {
                // Actualizar la lista de espera decrementando solo los números mayores
                actualizarListaEspera(idCurso, numeroListaEspera);
            }

            // Actualizar la inscripción con la cancelación y la cantidad a devolver
            String sqlUpdate = "UPDATE inscripciones_curso SET estado = 'Cancelada', fecha_cancelacion = ?, cantidad_devolucion = ?, n_lista_espera = NULL WHERE id = ?";
            db.executeUpdate(sqlUpdate, hoy, cantidadDevolucion, idInscripcion);

            // Datos para el justificante
            String nombre = info.get("nombre") != null ? info.get("nombre").toString() : "";
            String apellidos = info.get("apellidos") != null ? info.get("apellidos").toString() : "";
            String dni = info.get("dni") != null ? info.get("dni").toString() : "";
            String usuario = nombre + " " + apellidos;
            String curso = info.get("titulo") != null ? info.get("titulo").toString() : "";
            String fechaInicio = info.get("fecha_inicio") != null ? info.get("fecha_inicio").toString() : "";
            String fechaFin = info.get("fecha_fin") != null ? info.get("fecha_fin").toString() : "";
            String fechaInscripcion = info.get("fecha_inscripcion") != null ? info.get("fecha_inscripcion").toString() : "";
            String metodoPago = info.get("metodo_pago") != null ? info.get("metodo_pago").toString() : "";
            String fechaPago = info.get("fecha_pago") != null ? info.get("fecha_pago").toString() : "";
            String estado = info.get("estado") != null ? info.get("estado").toString() : "";

            // Generar justificante y devolver la ruta
            return generarJustificanteCancelacion(
                usuario, dni, curso, fechaInicio, fechaFin, fechaInscripcion, metodoPago, fechaPago,
                cantidadPagada, porcentajeDevolucion, cantidadDevolucion, hoy, estado
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Actualiza la lista de espera de un curso decrementando en 1 el número de lista de espera
     * para las inscripciones que tengan un número de lista de espera mayor que el número especificado.
     * 
     * @param idCurso Identificador del curso cuya lista de espera se va a actualizar
     * @param numeroEliminado Número de lista de espera que se ha eliminado
     */
    private void actualizarListaEspera(int idCurso, int numeroEliminado) {
        try {
            // Actualizar solo los números mayores que el eliminado
            String sql = "UPDATE inscripciones_curso SET n_lista_espera = n_lista_espera - 1 " +
                         "WHERE id_cuota IN (SELECT id FROM cuota WHERE id_curso = ?) " +
                         "AND n_lista_espera > ?";
            db.executeUpdate(sql, idCurso, numeroEliminado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Obtener nombre completo del colegiado por id
    private String obtenerNombreCompletoColegiado(int idColegiado) {
        String sql = "SELECT nombre, apellidos FROM colegiado WHERE id = ?";
        try {
            List<Map<String, Object>> res = db.executeQueryMap(sql, idColegiado);
            if (!res.isEmpty()) {
                Map<String, Object> row = res.get(0);
                return row.get("nombre") + " " + row.get("apellidos");
            }
        } catch (Exception e) {
            // Ignorar
        }
        return "Desconocido";
    }

    // Generar justificante de cancelación en archivo txt y devolver la ruta
    private String generarJustificanteCancelacion(
            String usuario, String dni, String curso, String fechaInicio, String fechaFin, String fechaInscripcion,
            String metodoPago, String fechaPago, double cantidadPagada, int porcentajeDevolucion, double cantidadDevuelta,
            String fechaCancelacion, String estadoInscripcion) {
        try {
            String carpeta = "justificantes_cancelacion";
            if (!Files.exists(Paths.get(carpeta))) {
                Files.createDirectories(Paths.get(carpeta));
            }
            String nombreArchivo = carpeta + "/justificante_" + usuario.replaceAll("\\s+", "_") + "_" + curso.replaceAll("\\s+", "_") + "_" + fechaCancelacion + ".txt";
            try (FileWriter writer = new FileWriter(nombreArchivo)) {
                writer.write("JUSTIFICANTE DE CANCELACIÓN DE INSCRIPCIÓN\n");
                writer.write("==========================================\n");
                writer.write("Usuario: " + usuario + "\n");
                writer.write("DNI: " + dni + "\n");
                writer.write("Curso: " + curso + "\n");
                writer.write("Fecha de inicio del curso: " + (fechaInicio != null ? fechaInicio : "-") + "\n");
                writer.write("Fecha de fin del curso: " + (fechaFin != null ? fechaFin : "-") + "\n");
                writer.write("Fecha de inscripción: " + (fechaInscripcion != null ? fechaInscripcion : "-") + "\n");
                writer.write("Método de pago: " + (metodoPago != null ? metodoPago : "-") + "\n");
                writer.write("Fecha de pago: " + (fechaPago != null ? fechaPago : "-") + "\n");
                writer.write("Cantidad pagada: " + String.format("%.2f", cantidadPagada) + " €\n");
                writer.write("Porcentaje de devolución: " + porcentajeDevolucion + "%\n");
                writer.write("Cantidad devuelta: " + String.format("%.2f", cantidadDevuelta) + " €\n");
                writer.write("Fecha de cancelación: " + fechaCancelacion + "\n");
                writer.write("==========================================\n");
            }
            return Paths.get(nombreArchivo).toAbsolutePath().toString();
        } catch (IOException e) {
            // Si falla la escritura, no interrumpe el proceso de cancelación
            return null;
        }
    }

    // Ordenar lista de cursos inscritos según criterio
    public List<CursoInscritoDTO> ordenarCursos(List<CursoInscritoDTO> lista, String criterio) {
        if (lista == null || criterio == null) return lista;
        switch (criterio) {
            case "Fecha de Inicio":
                lista.sort(Comparator.comparing(CursoInscritoDTO::getFechaInicio, Comparator.nullsLast(String::compareTo)));
                break;
            case "Fecha de Fin":
                lista.sort(Comparator.comparing(CursoInscritoDTO::getFechaFin, Comparator.nullsLast(String::compareTo)));
                break;
            case "Título":
                lista.sort(Comparator.comparing(CursoInscritoDTO::getTitulo, Comparator.nullsLast(String::compareToIgnoreCase)));
                break;
            case "Cantidad Pagada":
                lista.sort(Comparator.comparingDouble(CursoInscritoDTO::getCantidadPagada).reversed());
                break;
            case "Fecha Cancelación":
                lista.sort(Comparator.comparing(CursoInscritoDTO::getFechaCancelacion, Comparator.nullsLast(String::compareTo)));
                break;
        }
        return lista;
    }
}
