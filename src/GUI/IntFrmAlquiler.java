/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package GUI;

import Excepciones.VehiculoExcepciones.EstadoInvalidoExcepcion;
import Excepciones.VehiculoExcepciones.TransicionEstadoNoPermitidoExcepcion;
import Excepciones.AlquileresExcepciones.AlquilerNoValidoExcepcion;
import Excepciones.AlquileresExcepciones.TarifaNoValidaExcepcion;
import Excepciones.AlquileresExcepciones.FechaInvalidaExcepcion;
import Validaciones.ValidacionGeneral;
import Utilidad.UtilidadesGUI;
import Entidades.*;
import Gestiones.GestorAlquileresHashMap;
import Excepciones.ClientesExcepciones.ClienteNoEncontrado;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author je110
 */
public class IntFrmAlquiler extends javax.swing.JInternalFrame {

    private GestorAlquileresHashMap gestorAlquileres;
    private List<Cliente> listaClientes;
    private Map<String, Vehiculos> mapaVehiculos;
    private DefaultTableModel modeloTabla;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Alquiler currentAlquiler;
    /**
     * Creates new form IntFrmAlquiler
     */
    public IntFrmAlquiler(GestorAlquileresHashMap gestor, List<Cliente> clientes, Map<String, Vehiculos> vehiculos) {
        this.gestorAlquileres = gestor;
        this.listaClientes = clientes;
        this.mapaVehiculos = vehiculos;
        this.currentAlquiler = null;
        
        initComponents();
        ImageIcon icon = new ImageIcon(getClass().getResource("/Icons/IconsProyecto2/CrearAlquiler.png"));
        ImageIcon icon2 = new ImageIcon(getClass().getResource("/Icons/IconsProyecto2/FinalizarAlquiler.png"));
        ImageIcon icon3 = new ImageIcon(getClass().getResource("/Icons/IconsProyecto2/CancelarAlquiler.png"));
        ImageIcon icon4 = new ImageIcon(getClass().getResource("/Icons/IconsProyecto2/BuscarAlquiler.png"));
        componentesConfiguracion();
        configuraciónTiposVehículos();
        configuraciónEventos();
        cargarTabla();

    }
    
     private void componentesConfiguracion() {
        txtFechaInicial.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
            new javax.swing.text.DateFormatter(dateFormat)
        ));
        txtFechaFinal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
            new javax.swing.text.DateFormatter(dateFormat)
        ));
        
        String[] columns = {"ID", "Cliente", "Vehiculo", "Fecha Inicial", "Fecha Final", "Monto", "Estado"};
        modeloTabla = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tblAlquileres.setModel(modeloTabla);
        tblAlquileres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tblAlquileres.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
    }
    
    private void configuraciónTiposVehículos() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Sedán");
        model.addElement("SUV");
        model.addElement("Pick-up");
        comboxVehiculos.setModel(model);
    }
    
    private void configuraciónEventos() {
        btnAgregar.addActionListener(e -> crearAlquiler());
        btnFinalizar.addActionListener(e -> finalizar());
        btnCancelar.addActionListener(e -> cancelar());
        btnBuscar.addActionListener(e -> buscar());
        
        txtCedula.addActionListener(e -> buscarClientePorCedula());
        txtPlaca.addActionListener(e -> buscarVehiculoPorPlaca());
        
        ActionListener calculateAmount = e -> calcularMontoTotal();
        txtFechaInicial.addActionListener(calculateAmount);
        txtFechaFinal.addActionListener(calculateAmount);
        txtFechaInicial.addPropertyChangeListener("value", evt -> calcularMontoTotal());
        txtFechaFinal.addPropertyChangeListener("value", evt -> calcularMontoTotal());
        
        txtTarifa.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { calcularMontoTotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { calcularMontoTotal(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { calcularMontoTotal(); }
        });
    }
    
    private void limpiar() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtPlaca.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtFechaInicial.setValue(null);
        txtFechaFinal.setValue(null);
        txtTarifa.setText("");
        txtMonto.setText("");
        comboxVehiculos.setSelectedIndex(0);
        btnFinalizar.setEnabled(true);
        btnCancelar.setEnabled(true);
        currentAlquiler = null;
    }
    
    private boolean validarCampos() {

       if (!UtilidadesGUI.validarRequiere(txtCedula, txtPlaca, txtFechaInicial, txtFechaFinal, txtTarifa)) {
            UtilidadesGUI.mostrarMensajeDeError(this, "Complete los campos obligatorios.", "Validación");
            return false;
        }

       if (!txtCedula.getText().trim().matches("\\d+")) {
            UtilidadesGUI.mostrarMensajeDeError(this, "La cédula debe contener solo números.", "Validación");
            txtCedula.requestFocus();
            return false;
        }

        try {
            double t = Double.parseDouble(txtTarifa.getText().trim());
            if (t <= 0) {
                UtilidadesGUI.mostrarMensajeDeError(this, "La tarifa debe ser un número positivo.", "Validación");
                txtTarifa.requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            UtilidadesGUI.mostrarMensajeDeError(this, "La tarifa no tiene un formato numérico válido.", "Validación");
            txtTarifa.requestFocus();
            return false;
        }

        try {
            String cedula = txtCedula.getText().trim();
            String placa = txtPlaca.getText().trim();
            java.time.LocalDate inicio = obtenerFecha(txtFechaInicial);
            java.time.LocalDate fin = obtenerFecha(txtFechaFinal);

            if (!ValidacionGeneral.ClienteRegistrado(cedula, listaClientes)) {
                UtilidadesGUI.mostrarMensajeDeError(this, "La cédula no corresponde a un cliente registrado.", "Validación");
                return false;
            }
            if (!ValidacionGeneral.VehiculoRegistrado(placa, mapaVehiculos)) {
                UtilidadesGUI.mostrarMensajeDeError(this, "La placa no corresponde a un vehículo registrado.", "Validación");
                return false;
            }
            if (!ValidacionGeneral.FechaInicioValida(inicio)) {
                UtilidadesGUI.mostrarMensajeDeError(this, "La fecha de inicio no puede ser menor a la fecha actual.", "Validación");
                return false;
            }
            if (!ValidacionGeneral.FechaFinPosterior(inicio, fin)) {
                UtilidadesGUI.mostrarMensajeDeError(this, "La fecha de finalización debe ser posterior a la de inicio.", "Validación");
                return false;
            }
            if (!ValidacionGeneral.FechasDeRangoValidas(inicio, fin, inicio, fin)) {
                UtilidadesGUI.mostrarMensajeDeError(this, "La duración del alquiler no puede exceder 30 días.", "Validación");
                return false;
            }
            if (gestorAlquileres.existeAlquilerActivoEnRango(placa, inicio, fin)) {
                UtilidadesGUI.mostrarMensajeDeError(this, "Ya existe un alquiler activo para ese vehículo en el mismo rango de fechas.", "Validación");
                return false;
            }
        } catch (Exception ex) {
            UtilidadesGUI.mostrarMensajeDeError(this, "Error en validaciones: " + ex.getMessage(), "Validación");
            return false;
        }
        return true;
    }  
    
    private void crearAlquiler() {
        if (!validarCampos()) {
            return;
        }
        
        try {
            String cedula = txtCedula.getText().trim();
            String placa = txtPlaca.getText().trim();
            
            LocalDate fechaInicial = obtenerFecha(txtFechaInicial);
            LocalDate fechaFinal = obtenerFecha(txtFechaFinal);
            double tarifa = Double.parseDouble(txtTarifa.getText().trim());
            
            Cliente cliente = buscarCliente(cedula);
            Vehiculos vehiculo = mapaVehiculos.get(placa);
            
            if (cliente == null) {
                mostrarError("Cliente no encontrado");
                return;
            }
            
            if (vehiculo == null) {
                mostrarError("Vehículo no encontrado");
                return;
            }
            
            int nuevoID = gestorAlquileres.generarNuevoID();

            Alquiler nuevoAlquiler = new Alquiler(nuevoID, cliente, vehiculo, fechaInicial, fechaFinal, tarifa, listaClientes, mapaVehiculos);

            if (!gestorAlquileres.agregar(nuevoAlquiler)) {
                mostrarError("No se pudo crear el alquiler");
                return;
            }
            
            mostrarMensaje("Alquiler creado exitosamente para " + cliente.getNombre());
            currentAlquiler = nuevoAlquiler;
            cargarTabla();
            seleccionarFilaPorId(nuevoID);  
            mostrarDatos();    
        } catch (FechaInvalidaExcepcion ex) {
            mostrarError("Fechas inválidas: " + ex.getMessage());
        } catch (TarifaNoValidaExcepcion ex) {
            mostrarError("Tarifa no válida: " + ex.getMessage());
        } catch (AlquilerNoValidoExcepcion ex) {
            mostrarError("Alquiler no válido: " + ex.getMessage());
        } catch (EstadoInvalidoExcepcion | TransicionEstadoNoPermitidoExcepcion ex) {
            mostrarError("Estado no permitido: " + ex.getMessage());
        } catch (ClienteNoEncontrado ex) {
            mostrarError("Cliente no encontrado.");
        } catch (Exception ex) {
            mostrarError("Error al crear alquiler: " + ex.getMessage());
        }
    }
    
    private void seleccionarFilaPorId(int id) {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            Object val = modeloTabla.getValueAt(i, 0);
            if (val != null && Integer.parseInt(val.toString()) == id) {
                tblAlquileres.setRowSelectionInterval(i, i);
                tblAlquileres.scrollRectToVisible(tblAlquileres.getCellRect(i, 0, true));
                break;
           }
        }
    }
    
    private void actualizar() {
        if (currentAlquiler == null) {
            mostrarError("No se ha seleccionado un registro");
            return;
        }
        
        if (!validarCampos()) {
            return;
        }
        
        try {
            String cedula = txtCedula.getText().trim();
            String placa = txtPlaca.getText().trim();
            LocalDate fechaInicial = obtenerFecha(txtFechaInicial);
            LocalDate fechaFinal = obtenerFecha(txtFechaFinal);
            double tarifa = Double.parseDouble(txtTarifa.getText().trim());
            
            currentAlquiler.setFechaFinal(fechaFinal);
            currentAlquiler.setTarifaDiaria(tarifa);
            
            mostrarMensaje("Alquiler actualizado exitosamente");
            cargarTabla();
            
        } catch (Exception ex) {
            mostrarError("Error al actualizar: " + ex.getMessage());
        }
    }
    
    private void finalizar() {
        if (currentAlquiler == null) {
            mostrarError("Seleccione un alquiler para finalizar");
            return;
        }

        try {
            if (confirmar("¿Está seguro de finalizar este alquiler?")) {
                currentAlquiler.finalizarAlquiler();
                mostrarMensaje("Alquiler finalizado exitosamente");
                cargarTabla();
                limpiar();
            }
        } catch (TransicionEstadoNoPermitidoExcepcion | EstadoInvalidoExcepcion ex) {
            mostrarError("No se puede finalizar un alquiler en estado " + String.valueOf(currentAlquiler.getEstadoAlquiler()) + ".");
        } catch (Exception ex) {
            mostrarError("Error al procesar: " + ex.getMessage());
        }
    } 

    private void cancelar() {
        if (currentAlquiler == null) {
            mostrarError("Seleccione un alquiler para cancelar");
            return;
        }

        try {
            if (confirmar("¿Está seguro de cancelar este alquiler?")) {
                currentAlquiler.cancelarAlquiler();
                mostrarMensaje("Alquiler cancelado exitosamente");
                cargarTabla();
                limpiar();
            }
        } catch (TransicionEstadoNoPermitidoExcepcion | EstadoInvalidoExcepcion ex) {
            mostrarError("No se puede cancelar un alquiler en estado " + String.valueOf(currentAlquiler.getEstadoAlquiler()) + ".");
        } catch (Exception ex) {
            mostrarError("Error al procesar: " + ex.getMessage());
        }
    }
    
    private void buscar() {
        String criterio = txtBuscar.getText().trim();
        if (criterio.isEmpty()) {
            cargarTabla();
            return;
        }
        
        modeloTabla.setRowCount(0);
        boolean found = false;
        
        for (Alquiler alquiler : gestorAlquileres.getAlquileres().values()) {
            if (coincideCriterioBusqueda(alquiler, criterio)) {
                agregarFilaTabla(alquiler);
                found = true;
            }
        }
        
        if (!found) {
            mostrarMensaje("No se encontraron alquileres con el criterio especificado");
        }
    }
    
    private boolean coincideCriterioBusqueda(Alquiler alquiler, String criterio) {
        criterio = criterio.toLowerCase();
        return String.valueOf(alquiler.getAlquilerID()).contains(criterio) || alquiler.getCliente().getNombre().toLowerCase().contains(criterio) || alquiler.getCliente().getCedula().contains(criterio) || alquiler.getVehiculo().getPlaca().toLowerCase().contains(criterio);
    }
    
    private void cargarSeleccion() {
        int selectedRow = tblAlquileres.getSelectedRow();
        if (selectedRow == -1) {
            currentAlquiler = null;
            return;
        }
        
        int alquilerID = (int) modeloTabla.getValueAt(selectedRow, 0);
        currentAlquiler = gestorAlquileres.buscar(alquilerID);
        
        if (currentAlquiler != null) {
            mostrarDatos();
        } else {
            limpiar();
        }
    }
    
    private void mostrarDatos() {
        txtCedula.setText(currentAlquiler.getCliente().getCedula());
        txtNombre.setText(currentAlquiler.getCliente().getNombre());
        txtTelefono.setText(currentAlquiler.getCliente().getTelefono());
        
        txtPlaca.setText(currentAlquiler.getVehiculo().getPlaca());
        txtMarca.setText(currentAlquiler.getVehiculo().getMarca());
        txtModelo.setText(currentAlquiler.getVehiculo().getModelo());
        comboxVehiculos.setSelectedItem(currentAlquiler.getVehiculo().getTipo());
        
        try {
            java.util.Date fechaInicialDate = java.sql.Date.valueOf(currentAlquiler.getFechaInicial());
            java.util.Date fechaFinalDate = java.sql.Date.valueOf(currentAlquiler.getFechaFinal());
            txtFechaInicial.setValue(fechaInicialDate);
            txtFechaFinal.setValue(fechaFinalDate);
        } catch (Exception ex) {
            txtFechaInicial.setText(currentAlquiler.getFechaInicial().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            txtFechaFinal.setText(currentAlquiler.getFechaFinal().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
        
        txtTarifa.setText(String.format("%.2f", currentAlquiler.getTarifaDiaria()));
        txtMonto.setText(String.format("%.2f", currentAlquiler.getMontoTotal()));
        
       boolean esActivo    = currentAlquiler.getEstadoAlquiler() == EstadoAlquiler.ACTIVO;
       boolean esFinalizado= currentAlquiler.getEstadoAlquiler() == EstadoAlquiler.FINALIZADO;

       btnFinalizar.setEnabled(esActivo);
       btnCancelar.setEnabled(esActivo && !esFinalizado);

    }
    
    private void cargarTabla() {
    Collection<Alquiler> data = gestorAlquileres.getAlquileres() != null ? gestorAlquileres.getAlquileres().values() : Collections.emptyList();
    cargarTablaCon(data);
    }

    private void cargarTablaCon(Collection<Alquiler> data) {
        modeloTabla.setRowCount(0);
        if (data == null) return;
        for (Alquiler a : data) {
            agregarFilaTabla(a);
        }
    }

    private void agregarFilaTabla(Alquiler alquiler) {
        if (alquiler == null) return;

        String cliente = (alquiler.getCliente() != null) ? alquiler.getCliente().getNombre() : "";
        String placa   = (alquiler.getVehiculo() != null) ? alquiler.getVehiculo().getPlaca() : "";

        String fIni = (alquiler.getFechaInicial() != null) ? alquiler.getFechaInicial().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")) : "";
        String fFin = (alquiler.getFechaFinal() != null) ? alquiler.getFechaFinal().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")) : "";

        double montoVal = alquiler.getMontoTotal();  
        String monto = String.format("%.2f", montoVal);

        EstadoAlquiler est = alquiler.getEstadoAlquiler(); 
        String estado = (est != null) ? est.toString() : "";

        Object[] fila = { alquiler.getAlquilerID(), cliente, placa, fIni, fFin, monto, estado };
        modeloTabla.addRow(fila);
    }

    private void filtrarTabla(String q) {
        String query = (q == null) ? "" : q.trim().toLowerCase();
        if (query.isEmpty()) {
            cargarTabla();
            return;
        }

        Collection<Alquiler> source = (gestorAlquileres.getAlquileres() != null) ? gestorAlquileres.getAlquileres().values() : Collections.emptyList();

        List<Alquiler> filtrados = new ArrayList<>();
        for (Alquiler a : source) {
            if (a == null) continue;

            String id      = String.valueOf(a.getAlquilerID()); // 👈 mismo getter que en la tabla
            String cliente = (a.getCliente() != null) ? a.getCliente().getNombre() : "";
            String placa   = (a.getVehiculo() != null) ? a.getVehiculo().getPlaca() : "";
            String estado  = (a.getEstadoAlquiler() != null) ? a.getEstadoAlquiler().toString() : "";

            String blob = (id + " " + cliente + " " + placa + " " + estado).toLowerCase();
            if (blob.contains(query)) {
                filtrados.add(a);
            }
        }

        cargarTablaCon(filtrados);
    }
    
    private void buscarClientePorCedula() {
        String cedula = txtCedula.getText().trim();
        if (!cedula.isEmpty()) {
            Cliente cliente = buscarCliente(cedula);
            if (cliente != null) {
                txtNombre.setText(cliente.getNombre());
                txtTelefono.setText(cliente.getTelefono());
            } else {
                txtNombre.setText("");
                txtTelefono.setText("");
                mostrarMensaje("Cliente no encontrado");
            }
        }
    }
    
    private void buscarVehiculoPorPlaca() {
        String placa = txtPlaca.getText().trim();
        if (!placa.isEmpty()) {
            Vehiculos vehiculo = mapaVehiculos.get(placa);
            if (vehiculo != null) {
                txtMarca.setText(vehiculo.getMarca());
                txtModelo.setText(vehiculo.getModelo());
                comboxVehiculos.setSelectedItem(vehiculo.getTipo());
            } else {
                txtMarca.setText("");
                txtModelo.setText("");
                mostrarMensaje("Vehículo no encontrado");
            }
        }
    }

    private void calcularMontoTotal() {
        try {
            if (txtFechaInicial.getValue() != null && 
                txtFechaFinal.getValue() != null && 
                !txtTarifa.getText().isEmpty()) {
                
                LocalDate inicio = obtenerFecha(txtFechaInicial);
                LocalDate fin = obtenerFecha(txtFechaFinal);
                double tarifa = Double.parseDouble(txtTarifa.getText());
                
                int dias = (int) inicio.until(fin, ChronoUnit.DAYS);
                if (dias <= 0) dias = 1;
                
                double monto = tarifa * dias;
                txtMonto.setText(String.format("%.2f", monto));
            }
        } catch (Exception ex) {
        }
    }
    
    private LocalDate obtenerFecha(JFormattedTextField campo) throws IllegalArgumentException {
        try {
            Object value = campo.getValue();
            if (value instanceof java.util.Date) {
                java.util.Date date = (java.util.Date) value;
                return new java.sql.Date(date.getTime()).toLocalDate();
            } else if (value != null) {
                String fechaStr = value.toString();
                return LocalDate.parse(fechaStr, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }
            throw new IllegalArgumentException("Fecha no válida");
        } catch (Exception ex) {
            throw new IllegalArgumentException("Error al procesar fecha: " + ex.getMessage());
        }
    }
    
    private Cliente buscarCliente(String cedula) {
        for (Cliente cliente : listaClientes) {
            if (cliente.getCedula().equals(cedula)) {
                return cliente;
            }
        }
        return null;
    }

    private void mostrarError(String message) {
        UtilidadesGUI.mostrarMensajeDeError(this, message, "Error");
    }
    
    private void mostrarMensaje(String message) {
        UtilidadesGUI.mostrarMensaje(this, message, "Información");
    }
    
    private boolean confirmar(String message) {
        return javax.swing.JOptionPane.showConfirmDialog(this, message, "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlquileres = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnAgregar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnFinalizar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JFormattedTextField();
        txtCedula = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        txtMarca = new javax.swing.JTextField();
        txtModelo = new javax.swing.JTextField();
        comboxVehiculos = new javax.swing.JComboBox<>();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtFechaInicial = new javax.swing.JFormattedTextField();
        txtFechaFinal = new javax.swing.JFormattedTextField();
        txtTarifa = new javax.swing.JTextField();
        txtMonto = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Gestor de Alquileres");
        setToolTipText("");
        setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Bell MT", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Buscar Alquiler:");

        txtBuscar.setBackground(new java.awt.Color(255, 255, 255));
        txtBuscar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBuscar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtBuscar.setOpaque(true);
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(153, 153, 153));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/IconsProyecto2/BuscarAlquiler.png"))); // NOI18N
        btnBuscar.setOpaque(true);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBuscar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblAlquileres.setBackground(new java.awt.Color(255, 255, 255));
        tblAlquileres.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153)));
        tblAlquileres.setFont(new java.awt.Font("Bell MT", 1, 14)); // NOI18N
        tblAlquileres.setForeground(new java.awt.Color(102, 102, 102));
        tblAlquileres.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Cliente", "Vehiculo", "Fecha Inicial", "Fecha Final", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAlquileres.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAlquileresMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAlquileres);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        btnAgregar.setBackground(new java.awt.Color(153, 153, 153));
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/IconsProyecto2/CrearAlquiler.png"))); // NOI18N
        btnAgregar.setOpaque(true);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(153, 153, 153));
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/IconsProyecto2/CancelarAlquiler.png"))); // NOI18N
        btnCancelar.setOpaque(true);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnFinalizar.setBackground(new java.awt.Color(153, 153, 153));
        btnFinalizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/IconsProyecto2/FinalizarAlquiler.png"))); // NOI18N
        btnFinalizar.setOpaque(true);
        btnFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalizarActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(153, 153, 153));
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/IconsProyecto2/gtk-clear.png"))); // NOI18N
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnActualizar.setBackground(new java.awt.Color(153, 153, 153));
        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/IconsProyecto2/update-manager.png"))); // NOI18N
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAgregar)
                .addGap(27, 27, 27)
                .addComponent(btnFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAgregar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFinalizar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(4, 4, 4))
        );

        jLabel2.setFont(new java.awt.Font("Bell MT", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Crear/Agregar Alquiler");

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));

        jLabel3.setFont(new java.awt.Font("Bell MT", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Datos Cliente");

        jLabel6.setText("Cedula:");

        jLabel7.setText("Nombre:");

        jLabel8.setText("Telefono:");

        txtNombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        try {
            txtTelefono.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtTelefono.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtCedula.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCedula))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTelefono)))
                .addGap(98, 98, 98))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(174, 174, 174))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 29, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));

        jLabel4.setFont(new java.awt.Font("Bell MT", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Datos Vehiculos");

        jLabel9.setText("Placa:");

        jLabel10.setText("Marca:");

        jLabel11.setText("Modelo:");

        jLabel12.setText("Tipo:");

        txtPlaca.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtMarca.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtModelo.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPlaca))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMarca))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboxVehiculos, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtModelo, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)))
                .addGap(108, 108, 108))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(163, 163, 163)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(comboxVehiculos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(153, 153, 153));

        jLabel5.setFont(new java.awt.Font("Bell MT", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("Fechas y Monto");

        jLabel13.setText("Fecha Inicio:");

        jLabel14.setText("Fecha Final:");

        jLabel15.setText("Tarifa:");

        jLabel16.setText("Monto:");

        txtFechaInicial.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        txtFechaInicial.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtFechaFinal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        txtFechaFinal.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtTarifa.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtMonto.setEditable(false);
        txtMonto.setBackground(new java.awt.Color(204, 204, 204));
        txtMonto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMonto.setEnabled(false);
        txtMonto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMontoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMonto))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTarifa))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFechaFinal))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(97, 97, 97)))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtTarifa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtMonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(94, 94, 94))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        filtrarTabla(txtBuscar.getText());
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void txtMontoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMontoActionPerformed
        
    }//GEN-LAST:event_txtMontoActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        crearAlquiler();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalizarActionPerformed
        finalizar();
    }//GEN-LAST:event_btnFinalizarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void tblAlquileresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAlquileresMouseClicked
        cargarSeleccion();
    }//GEN-LAST:event_tblAlquileresMouseClicked

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        actualizar();
    }//GEN-LAST:event_btnActualizarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnFinalizar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox<String> comboxVehiculos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAlquileres;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JFormattedTextField txtFechaFinal;
    private javax.swing.JFormattedTextField txtFechaInicial;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtModelo;
    private javax.swing.JTextField txtMonto;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPlaca;
    private javax.swing.JTextField txtTarifa;
    private javax.swing.JFormattedTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
