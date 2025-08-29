/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gestiones;

import Entidades.Cliente;
import Entidades.Vehiculos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author je110
 */
public class CompartirDatos {
    public static List<Cliente> listaClientes = new ArrayList<>();
    public static Map<String, Vehiculos> mapaVehiculos = new HashMap<>();
    public static GestorAlquileresHashMap gestorAlquileres = new GestorAlquileresHashMap();
}
