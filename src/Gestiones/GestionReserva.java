/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gestiones;

import Entidades.Alquiler;
import Entidades.Cliente;
import Entidades.Reserva;
import Entidades.Vehiculos;
import Entidades.EstadoReserva; 
import Interfaces.Listas;
import Validaciones.ValidarReservas;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;

public class GestionReserva implements Listas<Reserva> {

    private final Map<Integer, Reserva> reservasActivas;
    private final Queue<Reserva> reservasEnEspera;

    private final Map<String, Vehiculos> vehiculos;
    private final List<Cliente> clientes;

    public GestionReserva() {
        this.reservasActivas = new HashMap<>();
        this.reservasEnEspera = new LinkedList<>();
        this.vehiculos = new HashMap<>();
        this.clientes = new ArrayList<>();
    }

    
    public Map<Integer, Reserva> getReservas() {
        return this.reservasActivas;
    }

    public Map<String, Vehiculos> getVehiculos() {
        return this.vehiculos;
    }
    public List<Cliente> getClientes() {
        return this.clientes;
    }

    @Override
    public boolean agregar(Reserva reserva) {
        if (ValidarReservas.VehiculoDisponible(
                reserva.getVehiculo(),
                reserva.getFechaInicio(),
                reserva.getFechaFin(),
                reservasActivas)) {

            reserva.setEstado(EstadoReserva.CONFIRMADA); 
            reservasActivas.put(reserva.getIdReserva(), reserva);
            return true;
        } else {
            reserva.setEstado(EstadoReserva.EN_ESPERA); 
            if (!reservasEnEspera.contains(reserva)) {  
                reservasEnEspera.add(reserva);
            }
            return false;
        }
    }

    @Override
    public boolean eliminar(Reserva reserva) {
        if (reservasEnEspera.remove(reserva)) {
            reserva.setEstado(EstadoReserva.CANCELADA); 
            return true;
        }
        if (reservasActivas.containsKey(reserva.getIdReserva())) {
            LocalDate hoy = LocalDate.now();
            if (reserva.getFechaInicio().isAfter(hoy)) {
                reservasActivas.remove(reserva.getIdReserva());
                reserva.setEstado(EstadoReserva.CANCELADA); 
                return true;
            }
        }
        return false;
    }

    @Override
    public Reserva buscar(Object id) {
        if (id instanceof Integer) {
            return reservasActivas.get((Integer) id);
        } else if (id instanceof String) {
            try {
                int intId = Integer.parseInt((String) id);
                return reservasActivas.get(intId);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    // CHANGED: excluir la propia reserva al validar, para no chocar consigo misma
    public boolean modificar(int idReserva, Vehiculos nuevoVehiculo) {
        Reserva r = reservasActivas.get(idReserva);
        if (r != null) {
            Map<Integer, Reserva> sinMi = new HashMap<>(reservasActivas);
            sinMi.remove(idReserva);

            if (ValidarReservas.VehiculoDisponible(
                    nuevoVehiculo,
                    r.getFechaInicio(),
                    r.getFechaFin(),
                    sinMi)) {
                r.setVehiculo(nuevoVehiculo);
                return true;
            }
        }
        return false;
    }

    public Alquiler confirmarReserva(int idReserva,double tarifaDiaria,Map<String, Cliente> repoClientes,Map<String, Vehiculos> repoVehiculos) {
        Reserva r = null;
        for (Reserva x : reservasEnEspera) {
            if (x.getIdReserva() == idReserva) {
                r = x;
                break;
            }
        }
        if (r == null) return null;

        if (!ValidarReservas.VehiculoDisponible(
                r.getVehiculo(),
                r.getFechaInicio(),
                r.getFechaFin(),
                reservasActivas)) {
            return null;
        }

        reservasEnEspera.remove(r);
        r.setEstado(EstadoReserva.CONFIRMADA); 
        reservasActivas.put(r.getIdReserva(), r);

        try {
            return new Alquiler(
                r,
                tarifaDiaria,
                new ArrayList<>(repoClientes.values()),
                repoVehiculos
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}