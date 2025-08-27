/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validaciones;

import java.time.LocalDate;
import java.time.Period;

/**
 *
 * @author Eduard Salas Murillo
 */
public abstract class ValidarPersona {
    
    public static boolean calcularEdad(LocalDate fechaNacimiento){
       int edad = Period.between( fechaNacimiento, LocalDate.now()).getYears();
       return edad>=18;
    }
    
    public static boolean FechaNoFutura(LocalDate date){
        return !date.isAfter(LocalDate.now());
    }
    
    public static boolean ValidarTelefono(String telefono){
        return telefono.matches("^[0-9]{2}-[0-9]{2}-[0-9]{2}-[0-9]{2}$");
    }
    
    public static boolean ValidarCorreo(String correo){
        return correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }
}