/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author Eduard Salas Murillo
 */
public enum EstadoReserva {
    EN_ESPERA("En espera"),
    CONFIRMADA("Confirmada"),
    CANCELADA("Cancelada"),
    FINALIZADA("Finalizada");

    private final String reservaEstado;

    public String getReservaEstado() {
        return reservaEstado;
    }

    private EstadoReserva(String reservaEstado) {
        this.reservaEstado = reservaEstado;
    }

}