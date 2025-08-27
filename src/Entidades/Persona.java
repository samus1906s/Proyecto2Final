/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import Validaciones.ValidarPersona;
import java.time.LocalDate;

/**
 *
 * @author Eduard Salas Murillo
 */
public abstract class Persona {
    protected String cedula;
    protected String nombre;
    protected LocalDate fechaNacimiento;
    protected String telefono;
    protected String correo;

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public boolean calcularEdad(){
        return ValidarPersona.calcularEdad(fechaNacimiento);
    }
    
    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Persona(String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo) {
        this.cedula = cedula;
        this.nombre = nombre;
        if(ValidarPersona.FechaNoFutura(fechaNacimiento))
        this.fechaNacimiento = fechaNacimiento;
        if(ValidarPersona.ValidarTelefono(telefono))
        this.telefono = telefono;
        if(ValidarPersona.ValidarCorreo(correo))
        this.correo = correo;
    }
    
    
}