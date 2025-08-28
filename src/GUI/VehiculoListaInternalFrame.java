/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package GUI;
import Entidades.Vehiculos;
import Entidades.TipoVehiculo;
import Entidades.EstadoVehiculos;
import Gestiones.VehiculosHashMap;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Valdelomaar
 */
public class VehiculoListaInternalFrame extends javax.swing.JInternalFrame {
     private final VehiculosHashMap repo = FrmMenú.VEHICULOS;
      private DefaultTableModel modelo;
    /**
     * Creates new form VehiculoListaInternalFrame
     */
    public VehiculoListaInternalFrame() {
        initComponents();
        postInit();
    }

    private void postInit() {
    modelo = new DefaultTableModel(new Object[]{"Placa","Marca","Modelo","Año","Tipo","Estado"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    tblVehiculos.setModel(modelo);
    tblVehiculos.setRowHeight(34);
    tblVehiculos.setAutoCreateRowSorter(true);
    var center = new DefaultTableCellRenderer();
    center.setHorizontalAlignment(SwingConstants.CENTER);
    tblVehiculos.getColumnModel().getColumn(3).setCellRenderer(center);

    
    cmbFiltroTipo.removeAllItems();
    cmbFiltroTipo.addItem("Todos");
    for (TipoVehiculo t : TipoVehiculo.values()) cmbFiltroTipo.addItem(t.getEtiqueta());

    cmbFiltroEstado.removeAllItems();
    cmbFiltroEstado.addItem("Todos");
    for (EstadoVehiculos e : EstadoVehiculos.values()) cmbFiltroEstado.addItem(e.getEtiqueta());
    cargarTodo();
}
    
    
    
    private void Buscar(){
         String placaLike = txtPlaca.getText().trim().toLowerCase();
    String tipoSel   = String.valueOf(cmbFiltroTipo.getSelectedItem());
    String estSel    = String.valueOf(cmbFiltroEstado.getSelectedItem());

    modelo.setRowCount(0);
    for (Vehiculos v : repo.getVehiculos().values()) {
        if (!placaLike.isEmpty()) {
            if (v.getPlaca()==null || !v.getPlaca().toLowerCase().contains(placaLike)) continue;
        }
        if (!"Todos".equalsIgnoreCase(tipoSel)) {
            String tipoV = (v.getTipo()!=null) ? v.getTipo().getEtiqueta() : "";
            if (!tipoSel.equalsIgnoreCase(tipoV)) continue;
        }
        if (!"Todos".equalsIgnoreCase(estSel)) {
            String estV = (v.getEstado()!=null) ? v.getEstado().getEtiqueta() : "";
            if (!estSel.equalsIgnoreCase(estV)) continue;
        }
        String tipo = (v.getTipo()!=null) ? v.getTipo().getEtiqueta() : "";
        String est  = (v.getEstado()!=null)? v.getEstado().getEtiqueta(): "";
        modelo.addRow(new Object[]{ v.getPlaca(), v.getMarca(), v.getModelo(), v.getAnio(), tipo, est });
    }
    actualizarTotal();
    }
    
    private void Refrescar(){
        txtPlaca.setText("");
    if (cmbFiltroTipo.getItemCount() > 0)   cmbFiltroTipo.setSelectedIndex(0);
    if (cmbFiltroEstado.getItemCount() > 0) cmbFiltroEstado.setSelectedIndex(0);
    cargarTodo();
    tblVehiculos.clearSelection(); 
    }
    
    private void editar(){
      int vr = tblVehiculos.getSelectedRow();
    if (vr < 0) { JOptionPane.showMessageDialog(this, "Seleccione una fila."); return; }
    int mr = tblVehiculos.convertRowIndexToModel(vr);
    String placa = String.valueOf(((javax.swing.table.DefaultTableModel)tblVehiculos.getModel()).getValueAt(mr, 0));

    Entidades.Vehiculos v = FrmMenú.VEHICULOS.buscar(placa);
    if (v == null) { JOptionPane.showMessageDialog(this, "No se encontró el vehículo."); return; }

    VehiculoAgregarEliminarInternal abm = null;
    javax.swing.JDesktopPane dp = getDesktopPane();
    if (dp != null) for (javax.swing.JInternalFrame f : dp.getAllFrames())
        if (f instanceof VehiculoAgregarEliminarInternal) { abm = (VehiculoAgregarEliminarInternal) f; break; }

    if (abm == null) { JOptionPane.showMessageDialog(this, "Abra primero 'Agregar/Eliminar Vehículo'."); return; }

    abm.cargarParaEdicion(v);
    abm.addPropertyChangeListener("vehiculosChanged", e -> btnRefrescarActionPerformed(null));
    abm.toFront();
    try { abm.setSelected(true); } catch (Exception ignore) {}
    }
    

    private void actualizarTotal() {
    labelVehiculos.setText(((DefaultTableModel)tblVehiculos.getModel()).getRowCount() + " vehículos");
}
    
    private void cargarTodo() {
    modelo.setRowCount(0);
    for (Vehiculos v : repo.getVehiculos().values()) {
        String tipo = (v.getTipo()!=null) ? v.getTipo().getEtiqueta() : "";
        String est  = (v.getEstado()!=null)? v.getEstado().getEtiqueta(): "";
        modelo.addRow(new Object[]{ v.getPlaca(), v.getMarca(), v.getModelo(), v.getAnio(), tipo, est });
    }
    actualizarTotal();
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        cmbFiltroTipo = new javax.swing.JComboBox<>();
        cmbFiltroEstado = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVehiculos = new javax.swing.JTable();
        labelVehiculos = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        btnRefrescar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnCargarEliminar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Vehiculos - Lista");
        setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtros de búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(0, 102, 255)), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 204)), null))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel1.setText("Placa");

        jLabel2.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel2.setText("Tipo");

        jLabel3.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel3.setText("Estado");

        cmbFiltroTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        cmbFiltroEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(cmbFiltroTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(102, 102, 102)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(cmbFiltroEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(86, 86, 86))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFiltroTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFiltroEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), javax.swing.BorderFactory.createEmptyBorder(8, 12, 12, 12)));

        tblVehiculos.setFont(new java.awt.Font("Segoe UI Symbol", 1, 13)); // NOI18N
        tblVehiculos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Placa", "Marca", "Modelo", "Año", "Tipo", "Estado"
            }
        ));
        tblVehiculos.setRowHeight(34);
        tblVehiculos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblVehiculos.setShowHorizontalLines(true);
        tblVehiculos.setShowVerticalLines(true);
        jScrollPane1.setViewportView(tblVehiculos);

        labelVehiculos.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 791, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(labelVehiculos)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelVehiculos)
                .addContainerGap(111, Short.MAX_VALUE))
        );

        btnBuscar.setBackground(new java.awt.Color(0, 102, 204));
        btnBuscar.setFont(new java.awt.Font("Segoe UI Emoji", 1, 13)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnRefrescar.setFont(new java.awt.Font("Segoe UI Emoji", 1, 13)); // NOI18N
        btnRefrescar.setText("Refrescar");
        btnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescarActionPerformed(evt);
            }
        });

        btnEditar.setFont(new java.awt.Font("Segoe UI Emoji", 1, 13)); // NOI18N
        btnEditar.setText("Editar selección");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnCargarEliminar.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        btnCargarEliminar.setText("Cargar");
        btnCargarEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(btnRefrescar)
                        .addGap(35, 35, 35)
                        .addComponent(btnEditar)
                        .addGap(18, 18, 18)
                        .addComponent(btnCargarEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnRefrescar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCargarEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        Buscar();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarActionPerformed
        Refrescar();
    }//GEN-LAST:event_btnRefrescarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
    editar();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnCargarEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarEliminarActionPerformed
      // 
    }//GEN-LAST:event_btnCargarEliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCargarEliminar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JComboBox<String> cmbFiltroEstado;
    private javax.swing.JComboBox<String> cmbFiltroTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelVehiculos;
    private javax.swing.JTable tblVehiculos;
    private javax.swing.JTextField txtPlaca;
    // End of variables declaration//GEN-END:variables
}
