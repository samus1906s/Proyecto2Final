/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validaciones;

import Entidades.EstadoVehiculos;
import Entidades.TipoVehiculo;
import Excepciones.VehiculoExcepciones.AñoIncorrectoExcepcion;
import Excepciones.VehiculoExcepciones.CampoVacioExcepcion;
import Excepciones.VehiculoExcepciones.EstadoInvalidoExcepcion;
import Excepciones.VehiculoExcepciones.PlacaInvalidaExcepcion;
import java.time.Year;
import java.util.regex.Pattern;

/**
 *
 * @author Valdelomaar
 */
public class ValidarVehiculos {
    private static final Pattern PLACA_REGEX = Pattern.compile("^[A-Z0-9-]{5,10}$");
    private static final int ANTIGUEDAD_MAX_ANIOS = 20;
/**
 *
 * @author Valdelomaar
 */ 
    
    public static String obligatorio(String valor) throws CampoVacioExcepcion {
        if (valor == null || valor.trim().isEmpty()) throw new CampoVacioExcepcion();
        return valor.trim();
    }

    public static String placa(String placa) throws CampoVacioExcepcion, PlacaInvalidaExcepcion {
        String p = obligatorio(placa).toUpperCase();
        if (!PLACA_REGEX.matcher(p).matches()) throw new PlacaInvalidaExcepcion();
        return p;
    }

    public static int anio(int anio) throws AñoIncorrectoExcepcion {
        int actual = Year.now().getValue();
        if (anio > actual) throw new AñoIncorrectoExcepcion();
        if (actual - anio > ANTIGUEDAD_MAX_ANIOS) throw new AñoIncorrectoExcepcion();
        return anio;
    }

    public static TipoVehiculo tipo(TipoVehiculo tipo) throws CampoVacioExcepcion {
        if (tipo == null) throw new CampoVacioExcepcion();
        return tipo;
    }

    public static EstadoVehiculos estado(EstadoVehiculos estado) throws EstadoInvalidoExcepcion {
        if (estado == null) throw new EstadoInvalidoExcepcion();
        return estado;
    }
    
}

