/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Entidades;

/**
 *
 * @author je110
 */
public enum EstadoAlquiler {
    ACTIVO("Activo"),
    FINALIZADO("Finalizado"),
    CANCELADO("Cancelado");
    
    private String alquilerEstado;

    public String getAlquilerEstado() {
        return alquilerEstado;
    }

    private EstadoAlquiler(String alquilerEstado) {
        this.alquilerEstado = alquilerEstado;
    }
    
}
