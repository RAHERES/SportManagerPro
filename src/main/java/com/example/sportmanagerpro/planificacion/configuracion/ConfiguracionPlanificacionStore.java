package com.example.sportmanagerpro.planificacion.configuracion;

/**
 * Guarda temporalmente la configuración activa.
 * Después se puede reemplazar por DAO o base de datos.
 */
public final class ConfiguracionPlanificacionStore {

    private static ConfiguracionPlanificacion configuracionActiva;

    private ConfiguracionPlanificacionStore() {
    }

    public static ConfiguracionPlanificacion getConfiguracionActiva() {
        if (configuracionActiva == null) {
            configuracionActiva = new ConfiguracionPlanificacion();
        }

        return configuracionActiva;
    }

    public static void setConfiguracionActiva(ConfiguracionPlanificacion configuracion) {
        configuracionActiva = configuracion;
    }
}