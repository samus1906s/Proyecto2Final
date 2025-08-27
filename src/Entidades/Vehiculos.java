/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import static Entidades.EstadoVehiculos.DISPONIBLE;
import static Entidades.EstadoVehiculos.EN_ALQUILER;
import static Entidades.EstadoVehiculos.EN_MANTENIMIENTO;
import Excepciones.VehiculoExcepciones.AñoIncorrectoExcepcion;
import Excepciones.VehiculoExcepciones.TransicionEstadoNoPermitidoExcepcion;
import Excepciones.VehiculoExcepciones.EstadoInvalidoExcepcion;
import Excepciones.VehiculoExcepciones.CampoVacioExcepcion;
import Excepciones.VehiculoExcepciones.PlacaInvalidaExcepcion;
import Validaciones.ValidarVehiculo;
import java.util.Objects;

/**
 *
 * @author Valdelomaar
 */
public class Vehiculos {
    private final String placa;
    private String marca;
    private String modelo;
    private int anio;
    private TipoVehiculo tipo;
    private EstadoVehiculos estado;

    public Vehiculos(String placa, String marca, String modelo, int anio, TipoVehiculo tipo, EstadoVehiculos estadoInicial) 
        throws PlacaInvalidaExcepcion, CampoVacioExcepcion, AñoIncorrectoExcepcion, EstadoInvalidoExcepcion {
    this.placa  = ValidarVehiculo.placa(placa);
    this.marca  = ValidarVehiculo.obligatorio(marca);
    this.modelo = ValidarVehiculo.obligatorio(modelo);
    this.anio   = ValidarVehiculo.anio(anio);
    this.tipo   = ValidarVehiculo.tipo(tipo);
    this.estado = (estadoInicial == null)
        ? EstadoVehiculos.DISPONIBLE
        : ValidarVehiculo.estado(estadoInicial);
    }
    
    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnio() {
        return anio;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public EstadoVehiculos getEstado() {
        return estado;
    }

    
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }


    public void setEstado(EstadoVehiculos nuevoEstado) throws TransicionEstadoNoPermitidoExcepcion, EstadoInvalidoExcepcion {
        EstadoVehiculos destino = ValidarVehiculo.estado(nuevoEstado);
        if (!puedeCambiarAEstado(this.estado, destino)) {
            throw new TransicionEstadoNoPermitidoExcepcion();
        }
        this.estado = destino;
    }

    
    public boolean isDisponible() {
        return estado == EstadoVehiculos.DISPONIBLE;
    }

    
    public void enviarAMantenimiento() throws TransicionEstadoNoPermitidoExcepcion {
        if (estado == EstadoVehiculos.EN_ALQUILER) {
            throw new TransicionEstadoNoPermitidoExcepcion();
        }
        estado = EstadoVehiculos.EN_MANTENIMIENTO;
    }

    public void salirDeMantenimiento() throws TransicionEstadoNoPermitidoExcepcion {
        if (estado != EstadoVehiculos.EN_MANTENIMIENTO) {
            throw new TransicionEstadoNoPermitidoExcepcion();
        }
        estado = EstadoVehiculos.DISPONIBLE;
    }

    private static boolean puedeCambiarAEstado(EstadoVehiculos actual, EstadoVehiculos destino) {
        if (actual == null || destino == null) return false;
        switch (actual) {
            case DISPONIBLE:
                return destino == EstadoVehiculos.EN_ALQUILER || destino == EstadoVehiculos.EN_MANTENIMIENTO;
            case EN_ALQUILER:
                return destino == EstadoVehiculos.DISPONIBLE;
            case EN_MANTENIMIENTO:
                return destino == EstadoVehiculos.DISPONIBLE;
            default:
                return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehiculos)) return false;
        Vehiculos vehiculo = (Vehiculos) o;
        return Objects.equals(placa, vehiculo.placa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placa);
    }
}
