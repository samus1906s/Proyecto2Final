/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package GUI;

import Entidades.Reserva;
import Gestiones.GestionReserva;
import Validaciones.ValidarPersona;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Eduard Salas Murillo
 */
public class IntFrmBuscarReserva extends javax.swing.JInternalFrame {
     private final GestionReserva gestion = FrmMenú.RESERVAS;
    private DefaultTableModel modelo;
    /**
     * Creates new form IntFrmBuscarReserva
     */
    public IntFrmBuscarReserva() {
        initComponents();
        
    }

    private void inicializarTabla() {
       String[] columnas = {"ID","Cliente","Cédula","Placa","Inicio","Fin","Estado"};
      modelo = new javax.swing.table.DefaultTableModel(columnas, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
       };
      JTableReservas.setModel(modelo);
      JTableReservas.setRowHeight(28);
     JTableReservas.setAutoCreateRowSorter(true);
    }
    private void cargarTodo() {
    modelo.setRowCount(0);
    for (Entidades.Reserva r : gestion.getReservas().values()) {
        modelo.addRow(new Object[]{
            r.getIdReserva(),                                                
            (r.getCliente()!=null? r.getCliente().getNombre() : ""),        
            (r.getCliente()!=null? r.getCliente().getCedula() : ""),        
            (r.getVehiculo()!=null? r.getVehiculo().getPlaca() : ""),       
            (r.getFechaInicio()!=null? r.getFechaInicio().toString() : ""), 
            (r.getFechaFin()!=null? r.getFechaFin().toString() : ""),       
            (r.getEstado()!=null? r.getEstado().toString() : "")            
        });
    }
}
    
    private void buscarConFiltros() {
    String qId  = safe(txtBuscarId.getText()).trim();
    String qCed = safe(txtBuscarCedula.getText()).trim();
    Date f      = getFechaDeJFecha();  

    modelo.setRowCount(0);
    java.util.Collection<Entidades.Reserva> base = gestion.getReservas().values();


    if (!qId.isEmpty()) {
        if (!qId.matches("\\d+")) {
            javax.swing.JOptionPane.showMessageDialog(this, "El ID debe ser numérico.");
            return;
        }
        Entidades.Reserva r = gestion.buscar(Integer.parseInt(qId));
        base = (r == null) ? java.util.List.of() : java.util.List.of(r);
    }

   
    if (!qCed.isEmpty() && !base.isEmpty()) {
        String cedDigits = onlyDigits(qCed);
        java.util.List<Entidades.Reserva> tmp = new java.util.ArrayList<>();
        for (Entidades.Reserva r : base) {
            Entidades.Cliente c = r.getCliente();
            String cDigits = onlyDigits(c != null ? c.getCedula() : "");
            if (!cedDigits.isEmpty() && cDigits.contains(cedDigits)) tmp.add(r);
        }
        base = tmp;
    }


    if (f != null && !base.isEmpty()) {
        java.util.List<Entidades.Reserva> tmp = new java.util.ArrayList<>();
        for (Entidades.Reserva r : base) {
          
            Date ini = (r.getFechaInicio() != null) ? java.sql.Date.valueOf(r.getFechaInicio()) : null;
            Date fin = (r.getFechaFin()    != null) ? java.sql.Date.valueOf(r.getFechaFin())    : null;
            if (ini != null && fin != null && !f.before(ini) && !f.after(fin)) {
                tmp.add(r);
            }
        }
        base = tmp;
    }

    for (Entidades.Reserva r : base) addRow(r);
    if (modelo.getRowCount() == 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "Sin resultados para los filtros ingresados.");
    }
}
    private Date getFechaDeJFecha() {
    String s = (JFecha == null) ? "" : JFecha.getText().trim();
    if (s.isEmpty()) return null;
    try {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        f.setLenient(false);
        return f.parse(s);
    } catch (ParseException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Fecha inválida. Usa dd/MM/yyyy.");
        return null;
    }
}
    private void addRow(Entidades.Reserva r) {
    modelo.addRow(new Object[]{
        r.getIdReserva(),
        (r.getCliente()!=null? safeNombre(r.getCliente()) : ""),
        (r.getCliente()!=null? safe(r.getCliente().getCedula()) : ""),
        (r.getVehiculo()!=null? safe(r.getVehiculo().getPlaca()) : ""),
        (r.getFechaInicio()!=null? r.getFechaInicio().toString() : ""),
        (r.getFechaFin()!=null? r.getFechaFin().toString() : ""),
        (r.getEstado()!=null? r.getEstado().toString() : "")
    });
}
    private String onlyDigits(String s) { return s == null ? "" : s.replaceAll("\\D+", ""); }
    private String safe(Object o) { return o == null ? "" : String.valueOf(o); }
    private String safeNombre(Entidades.Cliente c) {
    return (c != null && c.getNombre() != null) ? c.getNombre() : "";
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
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTableReservas = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtBuscarId = new javax.swing.JTextField();
        txtBuscarCedula = new javax.swing.JTextField();
        JFecha = new javax.swing.JFormattedTextField();
        btnBuscar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Buscar Reserva");
        setToolTipText("");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/IconsProyecto2/Search (9).png"))); // NOI18N
        jLabel1.setText("Ingrese el Dato a Buscar");

        JTableReservas.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        JTableReservas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Cedula Cliente", "Nombre", "Fecha de Nacimiento", "Telefono", "Correo", "Placa", "Marca", "Modelo", "Año", "Tipo Vehiculo", "Id Reserva", "Fecha Inicio", "Fecha Finalizacion"
            }
        ));
        jScrollPane1.setViewportView(JTableReservas);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/IconsProyecto2/contacts.png"))); // NOI18N
        jLabel2.setText("ID RESERVA");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/IconsProyecto2/contact (4).png"))); // NOI18N
        jLabel3.setText("CEDULA");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/IconsProyecto2/okteta.png"))); // NOI18N
        jLabel4.setText("FECHA");

        txtBuscarId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtBuscarCedula.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        JFecha.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        JFecha.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/IconsProyecto2/old-edit-find-replace.png"))); // NOI18N
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 918, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(txtBuscarId, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(99, 99, 99)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(txtBuscarCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(143, 143, 143)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(JFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscarId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBuscarCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarConFiltros();
    }//GEN-LAST:event_btnBuscarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField JFecha;
    private javax.swing.JTable JTableReservas;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtBuscarCedula;
    private javax.swing.JTextField txtBuscarId;
    // End of variables declaration//GEN-END:variables
}
