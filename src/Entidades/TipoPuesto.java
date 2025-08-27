/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Entidades;

/**
 *
 * @author samue
 */
public enum TipoPuesto {
    GERENTE("Gerente"),
    MECANICO("Mecanico"),
    SECRETARIO("Secretario");
    
    private final String trabajo;

    private TipoPuesto(String trabajo) {
        this.trabajo = trabajo;
    }

    public String getTrabajo() {
        return trabajo;
    }
    
    
}
