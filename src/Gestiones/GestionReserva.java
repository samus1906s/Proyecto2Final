/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gestiones;

import Entidades.Alquiler;
import Entidades.Cliente;
import Entidades.Reserva;
import Entidades.Vehiculos;
import Interfaces.Listas;
import Validaciones.ValidarReservas;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;

/**
 *
 * @author Eduard Salas Murillo
 */
public class GestionReserva implements Listas <Reserva> {

    private Map<Integer, Reserva> reservasActivas;
    private Queue<Reserva> reservasEnEspera;

    public GestionReserva() {
        this.reservasActivas = new HashMap<>();
        this.reservasEnEspera = new LinkedList<>();
    }

    @Override
    public boolean agregar(Reserva reserva) {
        if (ValidarReservas.VehiculoDisponible(
            reserva.getVehiculo(), 
            reserva.getFechaInicio(), 
            reserva.getFechaFin(), 
            reservasActivas)) {
            reservasActivas.put(reserva.getIdReserva(), reserva);
            return true;
        } else {
            reservasEnEspera.add(reserva);
            return false;
        }
    }

    @Override
    public boolean eliminar(Reserva reserva) {
     if (reservasActivas.containsKey(reserva.getIdReserva())) {
         LocalDate hoy = LocalDate.now();
         if (reserva.getFechaInicio().isAfter(hoy)) {
             reservasActivas.remove(reserva.getIdReserva());
             return true;
            } 
        }
     return false; 
    }

    @Override
    public Reserva buscar(Object id) {
      if (id instanceof Integer) {
          return reservasActivas.get((Integer) id);
        }  else if (id instanceof String) {
              try {
                 int intId = Integer.parseInt((String) id);
                 return reservasActivas.get(intId);
                } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    public boolean modificar(int idReserva, Vehiculos nuevoVehiculo) {
        Reserva reservaAModificar = reservasActivas.get(idReserva);
        if (reservaAModificar != null) {
            if (ValidarReservas.VehiculoDisponible(
                nuevoVehiculo, 
                reservaAModificar.getFechaInicio(), 
                reservaAModificar.getFechaFin(), 
                reservasActivas)) {
                 reservaAModificar.setVehiculo(nuevoVehiculo);
                return true;
            }
        }
        return false;
    }
    
    public Alquiler confirmarReserva(int idReserva,double tarifaDiaria,Map<String, Cliente> clientes, Map<String, Vehiculos> vehiculos) {
     Reserva reservaAConfirmar = null;
     for (Reserva r : reservasEnEspera) {
         if (r.getIdReserva() == idReserva) {
            reservaAConfirmar = r;
             break;
            }
        }
     if (reservaAConfirmar != null) {
         if (ValidarReservas.VehiculoDisponible(
                 reservaAConfirmar.getVehiculo(),
                 reservaAConfirmar.getFechaInicio(),
                 reservaAConfirmar.getFechaFin(),
                 reservasActivas)) {
                 reservasEnEspera.remove(reservaAConfirmar);
                 reservasActivas.put(reservaAConfirmar.getIdReserva(), reservaAConfirmar);
 
                 try {
                     Alquiler nuevoAlquiler = new Alquiler(
                     reservaAConfirmar,
                     tarifaDiaria,
                     new ArrayList<>(clientes.values()),
                     vehiculos
                    );
                  return nuevoAlquiler;
                } catch (Exception e) {
                 e.printStackTrace();
                 return null;
                }
            }  
        }
      return null; 
    } 
} 