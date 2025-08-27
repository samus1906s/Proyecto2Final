/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gestiones;
import Entidades.Cliente;
import Validaciones.ValidarPersona;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.Period;
import Interfaces.Listas;

/**
 *
 * @author samue
 */
public class GestionClientesArrayList implements Listas<Cliente> {
    private ArrayList<Cliente> clientes;

    public GestionClientesArrayList() {
        this.clientes = new ArrayList<>();
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    @Override
    public boolean agregar(Cliente cliente) {
        if (cliente == null || cliente.getCedula() == null || existeClientePorCedula(cliente.getCedula()) ||
        cliente.getLicenciaconductor() == null || cliente.getLicenciaconductor().trim().isEmpty() ||
        !ValidarPersona.calcularEdad(cliente.getFechaNacimiento()) ||
        !ValidarPersona.ValidarCorreo(cliente.getCorreo()) ||
        !ValidarPersona.ValidarTelefono(cliente.getTelefono())) {
        return false;
    }
    clientes.add(cliente);
    return true;
    }

    @Override
    public boolean eliminar(Cliente cliente) {
        if (cliente != null && cliente.getCedula() != null) {
            Cliente eliminado = buscar(cliente.getCedula());
            if (eliminado != null) {
                return clientes.remove(eliminado);
            }
        }
        return false;
    }

    @Override
    public Cliente buscar(Object id) {
        if (id != null) {
            String cedula = String.valueOf(id);
            for (Cliente cliente : clientes) {
                if (cliente.getCedula().equals(cedula)) {
                    return cliente;
                }
            }
        }
        return null;
    }
    
    public boolean actualizar(Cliente clienteActualizado) {
    Cliente c = (clienteActualizado == null || clienteActualizado.getCedula() == null) ? null : buscar(clienteActualizado.getCedula());
    if (c == null || !ValidarPersona.ValidarCorreo(clienteActualizado.getCorreo()) ||
        !ValidarPersona.ValidarTelefono(clienteActualizado.getTelefono()) ||
        clienteActualizado.getLicenciaconductor() == null || clienteActualizado.getLicenciaconductor().trim().isEmpty()) {
        return false;
    }
    c.setTelefono(clienteActualizado.getTelefono());
    c.setCorreo(clienteActualizado.getCorreo());
    c.setLicenciaconductor(clienteActualizado.getLicenciaconductor());
    return true;
}
    
     private boolean existeClientePorCedula(String cedula) {
        return buscar(cedula) != null;
    }
    
    
}
