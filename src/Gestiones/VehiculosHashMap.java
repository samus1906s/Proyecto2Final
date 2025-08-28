/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gestiones;

import Entidades.EstadoVehiculos;
import Interfaces.Listas;
import Entidades.Vehiculos;
import Excepciones.VehiculoExcepciones.CampoVacioExcepcion;
import Excepciones.VehiculoExcepciones.PlacaInvalidaExcepcion;
import Excepciones.VehiculoExcepciones.AñoIncorrectoExcepcion;
import Excepciones.VehiculoExcepciones.EstadoInvalidoExcepcion;
import Excepciones.VehiculoExcepciones.TransicionEstadoNoPermitidoExcepcion;
import Validaciones.ValidarVehiculos;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
/**
 *
 * @author Valdelomaar
 */
public class VehiculosHashMap implements Listas<Vehiculos> {
    
    private final Map<String,Vehiculos> data=new HashMap<>();
    public Map<String, Vehiculos> getVehiculos() {
    return Collections.unmodifiableMap(data);
    }
    
    private static String key(String placa) throws CampoVacioExcepcion, PlacaInvalidaExcepcion{return ValidarVehiculos.placa(placa);}

    @Override
    public boolean agregar(Vehiculos v){
        try{
            if(v==null)throw new CampoVacioExcepcion();
            String k=key(v.getPlaca());
            if(data.containsKey(k))throw new PlacaInvalidaExcepcion();
            ValidarVehiculos.obligatorio(v.getMarca());
            ValidarVehiculos.obligatorio(v.getModelo());
            ValidarVehiculos.anio(v.getAnio());
            ValidarVehiculos.tipo(v.getTipo());
            ValidarVehiculos.estado(v.getEstado());
            data.put(k,v);
            return true;
        }catch(CampoVacioExcepcion|PlacaInvalidaExcepcion|AñoIncorrectoExcepcion|EstadoInvalidoExcepcion e){
            return false;
        }
    }

    @Override
    public boolean eliminar(Vehiculos v) {
    try{
        if(v==null)throw new CampoVacioExcepcion();
        String k=key(v.getPlaca());
        Vehiculos a=data.get(k);
        if(a==null)return false;
        if(a.getEstado()==EstadoVehiculos.EN_ALQUILER)throw new TransicionEstadoNoPermitidoExcepcion();
        data.remove(k);
        return true;
                }catch(CampoVacioExcepcion|PlacaInvalidaExcepcion|TransicionEstadoNoPermitidoExcepcion e){
            return false;
        }
    }

    public boolean actualizar(Vehiculos v){
    try{
        if(v==null)throw new CampoVacioExcepcion();
        String k=key(v.getPlaca()); Vehiculos a=data.get(k);
        if(a==null)return false;
        String m=v.getModelo(); if(m!=null)a.setModelo(ValidarVehiculos.obligatorio(m));
        if(v.getTipo()!=null)a.setTipo(ValidarVehiculos.tipo(v.getTipo()));
        if(v.getEstado()!=null)a.setEstado(ValidarVehiculos.estado(v.getEstado()));
        return true;
    }catch(CampoVacioExcepcion|PlacaInvalidaExcepcion|EstadoInvalidoExcepcion|TransicionEstadoNoPermitidoExcepcion e){return false;}
}
    
    @Override
    public Vehiculos buscar(Object id){
        try{return data.get(key(String.valueOf(id)));}
            catch(CampoVacioExcepcion|PlacaInvalidaExcepcion e){return null;}
    }
    
}
