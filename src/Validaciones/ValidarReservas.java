/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validaciones;
import Entidades.Reserva;
import java.time.LocalDate;
import java.util.Map;
import Entidades.Cliente;
import Entidades.Vehiculos;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author Eduard Salas Murillo
 */
public abstract class ValidarReservas {
    public static boolean DuracionValida(LocalDate fechaInicio, LocalDate fechaFin) {
        if(fechaFin.isBefore(fechaInicio)){
            return false;
        }
        long dias = ChronoUnit.DAYS.between(fechaInicio, fechaFin)+1;
        return dias <= 30;
    }

    public static boolean VehiculoDisponible(
            Vehiculos placa,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            Map<Integer,Reserva> reservas
    ) {
        for (Reserva r : reservas.values()) {
            if (r.getVehiculo().equals(placa)) {
                boolean solapado = !(fechaFin.isBefore(r.getFechaInicio()) || fechaInicio.isAfter(r.getFechaFin()));
                if (solapado) {
                    return false;
                }
            }
        }
        return true;
    }
}