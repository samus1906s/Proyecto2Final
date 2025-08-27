/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.time.LocalDate;

/**
 *
 * @author samue
 */
public class Empleado extends Persona {
    private TipoPuesto trabajo;
    private double salario;

    public TipoPuesto getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(TipoPuesto trabajo) {
        if(trabajo != null)
        this.trabajo = trabajo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        if(ValidarSalario(salario))
            this.salario = salario;
    }

    public Empleado(String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo, double salario) {
        super(cedula, nombre, fechaNacimiento, telefono, correo);
        if(ValidarSalario(salario)) 
        this.salario = 0;
       if(trabajo != null){
         this.trabajo = trabajo;
        }else
        this.trabajo = TipoPuesto.GERENTE;
    }
    
    
    
    public static boolean ValidarSalario(double salario){
        return salario >= 5000;
    }
}
