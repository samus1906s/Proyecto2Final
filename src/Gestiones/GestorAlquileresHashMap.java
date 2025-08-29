/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gestiones;

import Entidades.Alquiler;
import Entidades.EstadoAlquiler;
import Excepciones.AlquileresExcepciones.FechaInvalidaExcepcion;
import Interfaces.Listas;
import Validaciones.ValidacionGeneral;

import java.time.LocalDate;
import java.util.HashMap;

public class GestorAlquileresHashMap implements Listas<Alquiler> {

    HashMap<Integer, Alquiler> alquileres;

    public GestorAlquileresHashMap() {
        this.alquileres = new HashMap<>();
    }

    public HashMap<Integer, Alquiler> getAlquileres() {
        return alquileres;
    }

    @Override
    public boolean agregar(Alquiler alquiler) {
        if (alquiler != null) {
            int id = alquiler.getAlquilerID();
            if (!alquileres.containsKey(id)) {
                if (existeAlquilerActivoEnRango(alquiler.getVehiculo().getPlaca(),
                    alquiler.getFechaInicial(), alquiler.getFechaFinal())) {
                    return false;
                }
                alquileres.put(id, alquiler);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(Alquiler alquiler) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Alquiler buscar(Object id) {
        if (id instanceof Integer) {
            return alquileres.get((Integer) id);
        } else if (id instanceof String) { 
            try {
                int intId = Integer.parseInt((String) id);
                return alquileres.get(intId);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    public boolean actualizarFechasAlquiler(int alquilerID, LocalDate nuevaInicio, LocalDate nuevaFin) throws FechaInvalidaExcepcion {
        Alquiler alquiler = alquileres.get(alquilerID);
        
        if (alquiler == null) return false; 

        if (nuevaInicio == null || nuevaFin == null) throw new FechaInvalidaExcepcion();
        
        if (!ValidacionGeneral.FechaFinPosterior(nuevaInicio, nuevaFin)) throw new FechaInvalidaExcepcion();

        for (Alquiler a : alquileres.values()) {
            
            if (a.getAlquilerID() != alquilerID && a.getVehiculo().getPlaca().equals(alquiler.getVehiculo().getPlaca()) &&a.getEstadoAlquiler() == EstadoAlquiler.ACTIVO) {
            
                if (!(nuevaFin.isBefore(a.getFechaInicial()) || nuevaInicio.isAfter(a.getFechaFinal()))) {
                    return false; 
                }
            }
        }

        alquiler.setFechaInicial(nuevaInicio);
        alquiler.setFechaFinal(nuevaFin);

        return true; 
    }

    public boolean existeAlquilerActivoEnRango(String placaVehiculo, LocalDate inicio, LocalDate fin) {
        for (Alquiler a : alquileres.values()) {
            if (a.getVehiculo() != null && a.getVehiculo().getPlaca().equals(placaVehiculo) &&
                a.getEstadoAlquiler() == EstadoAlquiler.ACTIVO) {
                if (!(fin.isBefore(a.getFechaInicial()) || inicio.isAfter(a.getFechaFinal()))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public int generarNuevoID() {
        if (alquileres.isEmpty()) {
            return 1;
        } else {
            return alquileres.keySet().stream().max(Integer::compareTo).get() + 1;
        }
    }
}
