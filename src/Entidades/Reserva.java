/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;
import Validaciones.ValidacionGeneral;
import Validaciones.ValidarReservas;
import java.time.LocalDate;
import Entidades.Cliente;
import Entidades.Vehiculos;
import Excepciones.ClientesExcepciones.ClienteNoEncontrado;
import Excepciones.ReservaExcepciones.DuracionReservaExcedida;
import Excepciones.ReservaExcepciones.FechaInicioInvalida;
import Excepciones.ReservaExcepciones.FechasDeReservasIncompletas;
import Excepciones.VehiculoExcepciones.VehiculoNoDisponible;
import Excepciones.VehiculoExcepciones.VehiculoNoEncontrado;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Eduard Salas Murillo
 */
public class Reserva {
    private int idReserva;
    private Cliente cliente;
    private Vehiculos vehiculo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public int getIdReserva() {
        return idReserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Vehiculos getVehiculo() {
        return vehiculo;
    }
    
    public void setVehiculo(Vehiculos nuevoVehiculo) {
    this.vehiculo = nuevoVehiculo;
   }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }


    public Reserva(int idReserva,Cliente cliente, Vehiculos vehiculo, LocalDate fechaInicio, LocalDate fechaFin, Map<Integer, Reserva> reservas, Map<String, Vehiculos> vehiculos, List<Cliente> clientes) throws ClienteNoEncontrado, VehiculoNoEncontrado, FechaInicioInvalida, FechasDeReservasIncompletas, DuracionReservaExcedida,VehiculoNoDisponible {
    
        if (cliente == null || !ValidacionGeneral.ClienteRegistrado(cliente.getCedula(), clientes)) {
         throw new ClienteNoEncontrado();
        }
    
        if (vehiculo == null || !ValidacionGeneral.VehiculoRegistrado(vehiculo.getPlaca(), vehiculos)) {
         throw new VehiculoNoEncontrado();
        }
    
      if (fechaInicio == null || !ValidacionGeneral.FechaInicioValida(fechaInicio)) {
           throw new FechaInicioInvalida();
        }
    
     if (fechaFin == null || !ValidacionGeneral.FechaFinPosterior(fechaInicio, fechaFin)) {
         throw new FechasDeReservasIncompletas();
        }
     
      if (!ValidarReservas.DuracionValida(fechaInicio, fechaFin)) {
         throw new DuracionReservaExcedida();
        }
    
     if (!ValidarReservas.VehiculoDisponible(vehiculo, fechaInicio, fechaFin, reservas)){
         throw new VehiculoNoDisponible();
        }
    
     this.idReserva = idReserva;
     this.cliente = cliente;
     this.vehiculo = vehiculo;
     this.fechaInicio = fechaInicio;
     this.fechaFin = fechaFin;
    }
}
