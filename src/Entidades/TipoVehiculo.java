/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Entidades;

/**
 *
 * @author Valdelomaar
 */
public enum TipoVehiculo {
    SEDAN("Sedán"),
    SUV("SUV"),
    PICK_UP("Pick-up");

    private final String etiqueta;

    TipoVehiculo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
