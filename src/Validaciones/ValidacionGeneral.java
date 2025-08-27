/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validaciones;

import Entidades.Cliente;
import Entidades.Vehiculos;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ValidacionGeneral {

    public static boolean ClienteRegistrado(String cedula, List<Cliente> clientes) {
        if (cedula == null || clientes == null) return false;
        for (Cliente c : clientes) {
            if (c != null && c.getCedula() != null && cedula.equals(c.getCedula())) {
                return true;
            }
        }
        return false;
    }

    public static boolean VehiculoRegistrado(String placa, Map<String, Vehiculos> vehiculos) {
        return placa != null && vehiculos != null && vehiculos.containsKey(placa);
    }

    public static boolean FechaInicioValida(LocalDate fechaInicio) {
        return fechaInicio != null && !fechaInicio.isBefore(LocalDate.now());
    }

    public static boolean FechaFinPosterior(LocalDate fechaInicio, LocalDate fechaFin) {
        return fechaInicio != null && fechaFin != null && fechaFin.isAfter(fechaInicio);
    }

    public static boolean FechasDeRangoValidas(LocalDate fechaInicio1, LocalDate fechaFin1,LocalDate fechaInicio2, LocalDate fechaFin2) {
        if (fechaInicio1 == null || fechaFin1 == null || fechaInicio2 == null || fechaFin2 == null) return false;
        if (fechaInicio1.isAfter(fechaFin1) || fechaInicio2.isAfter(fechaFin2)) return false;
        return !(fechaFin1.isBefore(fechaInicio2) || fechaFin2.isBefore(fechaInicio1));
    }
    
}
