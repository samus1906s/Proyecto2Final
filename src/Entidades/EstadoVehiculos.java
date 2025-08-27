/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Entidades;

/**
 *
 * @author Valdelomaar
 */
public enum EstadoVehiculos {
    DISPONIBLE("Disponible"),
    EN_ALQUILER("En alquiler"),
    EN_MANTENIMIENTO("En mantenimiento");

    private final String etiqueta;

    EstadoVehiculos(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    } 
}
