package si2024.pl34.historiaProcesarSolicitudes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.toedter.calendar.JDateChooser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import si2024_pl34.util.Database;

/**
 * Modelo responsable de procesar archivos JSON con solicitudes de colegiación
 * y actualizar su estado en la base de datos según sus titulaciones.
 */
public class ProcesarSolicitudesModel {

    private final Database db;
    private final Set<String> dnisProcesados = new HashSet<>();
    private final Map<String, String> errores = new HashMap<>();

    /**
     * Clave para identificar errores relacionados con el archivo en el mapa de
     * errores.
     */
    private static final String CLAVE_ERROR_ARCHIVO = "archivo";

    /**
     * Estados posibles para una solicitud de colegiación.
     */
    public enum EstadoSolicitud {
        RECHAZADO("Rechazado"),
        COLEGIADO("Colegiado");

        private final String estado;

        EstadoSolicitud(String estado) {
            this.estado = estado;
        }

        public String getEstado() {
            return estado;
        }

        @Override
        public String toString() {
            return estado;
        }
    }

    public enum CamposObligatorios {
        CAMPO_DNI("DNI"),
        CAMPO_FECHA_SOLICITUD("fechaSolicitud"),
        CAMPO_LISTA_TITULACIONES("listaTitulaciones");

        private final String campo;

        CamposObligatorios(String campo) {
            this.campo = campo;
        }

        public String getCampo() {
            return campo;
        }

        @Override
        public String toString() {
            return campo;
        }
    }

    /**
     * Lista de titulaciones que permiten la colegiación.
     */
private static final List<String> TITULACIONES_ACEPTADAS = 
    Collections.unmodifiableList(Arrays.asList(
        "Ingeniero en informática",
        "Máster en Ingeniería Informática",
        "Licenciatura en Informática"
    ));


    /**
     * Constructor principal.
     */
    public ProcesarSolicitudesModel() {
        this.db = new Database();
    }

    /**
     * Obtiene los mensajes de error generados durante el procesamiento.
     * 
     * @return Map con los DNIs o identificadores como claves y los mensajes de
     *         error como valores
     */
    public Map<String, String> getErrores() {
        return new HashMap<>(errores);
    }

    /**
     * Limpia los errores y DNIs procesados para iniciar un nuevo procesamiento.
     */
    public void limpiarEstado() {
        errores.clear();
        dnisProcesados.clear();
    }

    /**
     * Procesa un archivo de respuesta de solicitudes.
     * 
     * @param archivoRespuesta El archivo JSON con las solicitudes
     * @return Lista de solicitudes procesadas correctamente
     */
    public List<SolicitudDTO> procesarArchivoRespuesta(File archivoRespuesta) {
        List<SolicitudDTO> solicitudes = new ArrayList<>();
        limpiarEstado();

        try (InputStream is = new FileInputStream(archivoRespuesta)) {
            JSONArray jsonSolicitudes = extraerJsonSolicitudes(is);
            if (jsonSolicitudes == null) {
                return solicitudes;
            }

            // Pre-procesar para detectar DNIs duplicados
            detectarDnisDuplicados(jsonSolicitudes);

            // Pre-procesar para verificar existencia de DNIs en la base de datos
            preProcesarDNIS(jsonSolicitudes);

            // Procesamiento normal de solicitudes
            procesarListaSolicitudes(jsonSolicitudes, solicitudes);

        } catch (FileNotFoundException e) {
            registrarError(CLAVE_ERROR_ARCHIVO, "No se pudo encontrar el archivo: " + e.getMessage());
        } catch (Exception e) {
            registrarError(CLAVE_ERROR_ARCHIVO, "Error inesperado: " + e.getMessage());
        }

        return solicitudes;
    }

    /**
     * Detecta DNIs duplicados en el archivo JSON y los marca como errores.
     * 
     * @param jsonSolicitudes Array JSON con las solicitudes
     */
    private void detectarDnisDuplicados(JSONArray jsonSolicitudes) {
        Map<String, Integer> contadorDnis = new HashMap<>();
        Set<String> dnisDuplicados = new HashSet<>();

        // Primera pasada: contar ocurrencias de cada DNI
        for (int i = 0; i < jsonSolicitudes.length(); i++) {
            try {
                JSONObject solicitud = jsonSolicitudes.getJSONObject(i);
                if (solicitud.has("DNI")) {
                    String dni = solicitud.getString("DNI");
                    if (dni != null && !dni.trim().isEmpty()) {
                        contadorDnis.put(dni, contadorDnis.getOrDefault(dni, 0) + 1);
                        if (contadorDnis.get(dni) > 1) {
                            dnisDuplicados.add(dni);
                        }
                    }
                }
            } catch (JSONException e) {
                // Ignoramos errores aquí, se manejarán durante el procesamiento real
            }
        }

        // Registrar errores para todos los DNIs duplicados
        for (String dni : dnisDuplicados) {
            registrarError(dni, "DNI duplicado en el archivo. Se omitirán todas las solicitudes con este DNI.");
            // Añadir al conjunto de DNIs procesados para que ninguna solicitud con este DNI
            // se procese
            dnisProcesados.add(dni);
        }

    }

    /**
     * Extrae el JSONArray de solicitudes del archivo JSON.
     * 
     * @param is InputStream del archivo JSON
     * @return JSONArray con las solicitudes o null si hay error
     */
    private JSONArray extraerJsonSolicitudes(InputStream is) {
        try {
            JSONTokener tokener = new JSONTokener(is);
            JSONObject jsonObject = new JSONObject(tokener);

            if (!jsonObject.has("solicitudes")) {
                registrarError(CLAVE_ERROR_ARCHIVO, "El archivo no contiene el campo 'solicitudes'");
                return null;
            }

            return jsonObject.getJSONArray("solicitudes");
        } catch (JSONException e) {
            registrarError(CLAVE_ERROR_ARCHIVO, "Formato JSON inválido: " + e.getMessage());
            return null;
        }
    }

    /**
     * Procesa todas las solicitudes del array JSON.
     * 
     * @param jsonSolicitudes Array JSON con las solicitudes
     * @param solicitudes     Lista donde se añadirán las solicitudes procesadas
     */
    private void procesarListaSolicitudes(JSONArray jsonSolicitudes, List<SolicitudDTO> solicitudes) {
        for (int i = 0; i < jsonSolicitudes.length(); i++) {
            String indice = "solicitud_" + i;

            try {
                JSONObject jsonSolicitud = jsonSolicitudes.getJSONObject(i);
                SolicitudDTO solicitud = procesarSolicitudIndividual(jsonSolicitud, indice);

                if (solicitud != null) {
                    solicitudes.add(solicitud);
                }
            } catch (JSONException e) {
                registrarError(indice, "Formato de solicitud inválido: " + e.getMessage());
            }
        }
    }

    /**
     * Procesa una solicitud individual del JSON.
     * 
     * @param jsonSolicitud Objeto JSON con la solicitud
     * @param indice        Identificador de la solicitud para mensajes de error
     * @return SolicitudDTO procesada o null si hay error
     */
    private SolicitudDTO procesarSolicitudIndividual(JSONObject jsonSolicitud, String indice) {
        // Verificar campos obligatorios
        if (!tieneCamposObligatorios(jsonSolicitud, indice)) {
            return null;
        }

        // Extraer y validar DNI
        String dni = extraerDni(jsonSolicitud, indice);
        if (dni == null) {
            return null;
        }

        // Verificar DNI duplicado
        if (esDniDuplicado(dni)) {
            return null;
        }

        // Extraer y validar fecha
        String fechaSolicitud = extraerFechaSolicitud(jsonSolicitud, dni);
        if (fechaSolicitud == null) {
            return null;
        }

        // Extraer y validar titulaciones
        List<String> titulaciones = extraerTitulaciones(jsonSolicitud, dni);
        if (titulaciones == null || titulaciones.isEmpty()) {
            return null;
        }

        // Crear y configurar la solicitud
        SolicitudDTO solicitud = crearSolicitud(dni, fechaSolicitud, titulaciones);

        // Actualizar en base de datos
        if (!actualizarSolicitud(solicitud)) {
            return null;
        }

        return solicitud;
    }

    /**
     * Verifica si el JSON de solicitud tiene todos los campos obligatorios.
     * 
     * @param jsonSolicitud JSON de la solicitud
     * @param indice        Identificador para mensajes de error
     * @return true si tiene todos los campos, false en caso contrario
     */
    private boolean tieneCamposObligatorios(JSONObject jsonSolicitud, String indice) {
        if (!jsonSolicitud.has(CamposObligatorios.CAMPO_DNI.getCampo()) ||
                !jsonSolicitud.has(CamposObligatorios.CAMPO_FECHA_SOLICITUD.getCampo()) ||
                !jsonSolicitud.has(CamposObligatorios.CAMPO_LISTA_TITULACIONES.getCampo())) {

            registrarError(indice, "Faltan campos obligatorios (" +
                    CamposObligatorios.CAMPO_DNI.getCampo() + ", " +
                    CamposObligatorios.CAMPO_FECHA_SOLICITUD.getCampo() + " o " +
                    CamposObligatorios.CAMPO_LISTA_TITULACIONES.getCampo() + ")");
            return false;
        }
        return true;
    }

    /**
     * Extrae y valida el DNI de la solicitud.
     * 
     * @param jsonSolicitud JSON de la solicitud
     * @param indice        Identificador para mensajes de error
     * @return DNI validado o null si es inválido
     */
    private String extraerDni(JSONObject jsonSolicitud, String indice) {
        try {
            String dni = jsonSolicitud.getString("DNI");
            if (dni == null || dni.trim().isEmpty()) {
                registrarError(indice, "El campo DNI está vacío");
                return null;
            }
            return dni;
        } catch (JSONException e) {
            registrarError(indice, "Error al obtener el DNI: " + e.getMessage());
            return null;
        }
    }

    /**
     * Verifica si un DNI ya ha sido marcado como duplicado.
     * 
     * @param dni DNI a verificar
     * @return true si es duplicado, false en caso contrario
     */
    private boolean esDniDuplicado(String dni) {
        return dnisProcesados.contains(dni);
    }

    /**
     * Extrae y valida la fecha de solicitud.
     * 
     * @param jsonSolicitud JSON de la solicitud
     * @param dni           DNI para mensajes de error
     * @return Fecha validada o null si es inválida
     */
    private String extraerFechaSolicitud(JSONObject jsonSolicitud, String dni) {
        try {
            String fechaSolicitud = jsonSolicitud.getString("fechaSolicitud");
            if (fechaSolicitud == null || fechaSolicitud.trim().isEmpty()) {
                registrarError(dni, "El campo fechaSolicitud está vacío");
                return null;
            }
            return fechaSolicitud;
        } catch (JSONException e) {
            registrarError(dni, "Error al obtener la fecha de solicitud: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extrae y valida las titulaciones de la solicitud.
     * 
     * @param jsonSolicitud JSON de la solicitud
     * @param dni           DNI para mensajes de error
     * @return Lista de titulaciones o null si hay error
     */
    private List<String> extraerTitulaciones(JSONObject jsonSolicitud, String dni) {
        try {
            JSONArray jsonTitulaciones = jsonSolicitud.getJSONArray("listaTitulaciones");

            // Validación temprana de array vacío
            if (jsonTitulaciones.length() == 0) {
                registrarError(dni, "No se han especificado titulaciones");
                return Collections.emptyList();
            }

            List<String> titulaciones = new ArrayList<>();

            // Procesar cada titulación
            for (int j = 0; j < jsonTitulaciones.length(); j++) {
                String titulacion = extraerTitulacionIndividual(jsonTitulaciones.getJSONObject(j), dni);
                if (titulacion != null) {
                    titulaciones.add(titulacion);
                }
            }

            // Verificar si quedaron titulaciones válidas
            if (titulaciones.isEmpty()) {
                registrarError(dni, "No se encontraron titulaciones válidas");
                return Collections.emptyList();
            }

            return titulaciones;

        } catch (JSONException e) {
            registrarError(dni, "Error al procesar titulaciones: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Extrae una titulación individual del JSON.
     * 
     * @param jsonTitulacion Objeto JSON con la titulación
     * @param dni            DNI para mensajes de error
     * @return La titulación extraída o null si no es válida
     */
    private String extraerTitulacionIndividual(JSONObject jsonTitulacion, String dni) {
        try {
            if (!jsonTitulacion.has("titulacion")) {
                registrarError(dni, "Una titulación no tiene el campo 'titulacion'");
                return null;
            }

            String titulacion = jsonTitulacion.getString("titulacion");
            if (titulacion == null || titulacion.trim().isEmpty()) {
                registrarError(dni, "Una titulación está vacía");
                return null;
            }

            return titulacion;
        } catch (JSONException e) {
            registrarError(dni, "Error al procesar una titulación individual: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crea y configura un objeto SolicitudDTO con la información validada.
     * 
     * @param dni            DNI validado
     * @param fechaSolicitud Fecha validada
     * @param titulaciones   Lista de titulaciones validadas
     * @return Objeto SolicitudDTO configurado
     */
    private SolicitudDTO crearSolicitud(String dni, String fechaSolicitud, List<String> titulaciones) {
        SolicitudDTO solicitud = new SolicitudDTO();
        solicitud.setDni(dni);
        solicitud.setFechaSolicitudColegiacion(fechaSolicitud);
        solicitud.setTitulaciones(titulaciones);

        boolean esAceptada = titulaciones.stream()
                .anyMatch(t -> TITULACIONES_ACEPTADAS.stream()
                        .anyMatch(ta -> ta.equalsIgnoreCase(t)));

        EstadoSolicitud estado = esAceptada ? EstadoSolicitud.COLEGIADO : EstadoSolicitud.RECHAZADO;

        if (estado == EstadoSolicitud.RECHAZADO) {
            String motivoRechazo = "No cumple con titulaciones aceptadas";
            solicitud.setMotivoRechazoColegiazion(motivoRechazo);
        }

        solicitud.setEstado(estado.getEstado());

        return solicitud;
    }

    /**
     * Registra un error en el mapa de errores.
     * 
     * @param clave   Identificador del error
     * @param mensaje Descripción del error
     */
    private void registrarError(String clave, String mensaje) {
        errores.put(clave, mensaje);
    }

    /**
     * Actualiza el estado de una solicitud en la base de datos.
     * 
     * @param solicitud La solicitud con los datos a actualizar
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    private boolean actualizarSolicitud(SolicitudDTO solicitud) {
        try {
            String sql = "UPDATE colegiado SET estado = ?, motivo_rechazo_colegiazion = ? WHERE dni = ? AND fecha_solicitud_colegiacion = ?";
            db.executeUpdate(sql, solicitud.getEstado(), solicitud.getMotivoRechazoColegiazion(), solicitud.getDni(),
                    solicitud.getFechaSolicitudColegiacion());
            return true;
        } catch (Exception e) {
            registrarError(solicitud.getDni(), "Error en base de datos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si el DNI de cada solicitud existe en la base de datos.
     * 
     * Para cada solicitud en el array JSON, comprueba si contiene un campo DNI.
     * Si el DNI existe y no ha sido procesado anteriormente, verifica su existencia
     * en la tabla de colegiados. Si no existe, registra un error.
     * 
     * @param jsonSolicitudes Array JSON que contiene las solicitudes a procesar
     */
    private void preProcesarDNIS(JSONArray jsonSolicitudes) {
        for (int i = 0; i < jsonSolicitudes.length(); i++) {
            try {
                JSONObject solicitud = jsonSolicitudes.getJSONObject(i);
                if (solicitud.has("DNI")) {
                    String dni = solicitud.getString("DNI");
                    if (dni != null && !dni.trim().isEmpty() && !dnisProcesados.contains(dni)) {
                        existeDNi(dni);
                    }
                }
            } catch (JSONException e) {
                // Se maneja en el procesamiento posterior
            }
        }
    }

    /**
     * Verifica si un DNI existe en la base de datos.
     * 
     * @param dni El DNI a verificar
     */
    private void existeDNi(String dni) {
        String sql = "SELECT dni FROM colegiado WHERE dni = ?";
        try {
            List<Object[]> dnis = db.executeQueryArray(sql, dni);
            if (dnis.isEmpty()) {
                registrarError(dni, "El DNI no existe en la base de datos");
                dnisProcesados.add(dni);
            }
        } catch (Exception e) {
            registrarError(dni, "Error en base de datos: " + e.getMessage());
            dnisProcesados.add(dni);
        }
    }

}
