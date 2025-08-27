/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gestiones;

import Entidades.Empleado;
import Entidades.TipoPuesto;
import Interfaces.Listas;
import Validaciones.ValidarPersona;
import java.util.ArrayList;

/**
 *
 * @author samue
 */
public class GestionEmpleadosArrayList implements Listas <Empleado> {
    private ArrayList<Empleado> empleados;

    public GestionEmpleadosArrayList(ArrayList<Empleado> empleados) {
        this.empleados = new ArrayList<>();
    }

    public ArrayList<Empleado> getEmpleados() {
        return empleados;
    }
    
    @Override
    public boolean agregar(Empleado empleado) {
    if (empleado == null || empleado.getCedula() == null ||
        existeEmpleadoPorCedula(empleado.getCedula()) ||
        !Empleado.ValidarSalario(empleado.getSalario()) ||
        !ValidarPersona.calcularEdad(empleado.getFechaNacimiento()) ||
        !ValidarPersona.ValidarCorreo(empleado.getCorreo()) ||
        !ValidarPersona.ValidarTelefono(empleado.getTelefono())) {
        return false;
    }
    if (empleado.getTrabajo() == null) {
        empleado.setTrabajo(TipoPuesto.GERENTE);
    }

    empleados.add(empleado);
    return true;
}

    @Override
    public boolean eliminar(Empleado empleado) {
        if (empleado != null && empleado.getCedula() != null) {
            Empleado encontrado = buscar(empleado.getCedula());
            if (encontrado != null) {
                return empleados.remove(encontrado);
            }
        }
        return false; 
    }

    @Override
    public Empleado buscar(Object id) {
        if (id != null) {
            String cedula = String.valueOf(id);
            for (Empleado empleado : empleados) {
                if (empleado.getCedula().equals(cedula)) {
                    return empleado;
                }
            }
        }
        return null; 
    }
    
    public boolean actualizar(Empleado empleadoActualizado) {
    Empleado e = (empleadoActualizado == null || empleadoActualizado.getCedula() == null) ? null : buscar(empleadoActualizado.getCedula());
    if (e == null || !ValidarPersona.ValidarCorreo(empleadoActualizado.getCorreo()) ||
        !ValidarPersona.ValidarTelefono(empleadoActualizado.getTelefono())) {
        return false;
    }
    e.setTelefono(empleadoActualizado.getTelefono());
    e.setCorreo(empleadoActualizado.getCorreo());
    if (empleadoActualizado.getTrabajo() != null) e.setTrabajo(empleadoActualizado.getTrabajo());
    return true;
}
    
     private boolean existeEmpleadoPorCedula(String cedula) {
        return buscar(cedula) != null;
    }
    
}
