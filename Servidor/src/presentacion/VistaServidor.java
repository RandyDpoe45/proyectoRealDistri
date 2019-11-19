/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentacion;

import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import negocio.Candidato;
import negocio.Oferta;
import servidor.CandidatoCliente;
import servidor.DataEntry;
import servidor.ImplementacionCandidato;
import servidor.ImplementacionOferta;
import servidor.OfertaCliente;
import servidor.OperacionesCandidato;
import servidor.OperacionesOferta;
import servidor.ServerImplementation;
import servidor.ServerInterface;
import servidor.Servidor;
import static servidor.Servidor.IP;

/**
 *
 * @author gustavo
 */
public class VistaServidor extends javax.swing.JFrame {

    /**
     * Creates new form VistaServidor
     */
    public static String IP="127.0.0.1";
    private static String serverIP="127.0.0.1";
    private static Map<String,DataEntry<Candidato>> candidatos = new HashMap<>();  
    private static Map<Long,DataEntry<Oferta>> ofertas = new HashMap<>();
    public VistaServidor() {
        ImplementacionCandidato.gui=this;
        ImplementacionOferta.gui=this;
        initComponents();
        conectar();
    }
    
    public void actualizarEntradas() throws InterruptedException{
        DefaultTableModel dmc,dmo;
        dmc=(DefaultTableModel) EntradasCandidatos.getModel();
        dmo=(DefaultTableModel) EntradasOfertas.getModel();
        //_____________________________
        int rowCount = dmc.getRowCount();
        for (int i =0; i <rowCount; i++) {
            dmc.removeRow(0);
        }
        rowCount = dmo.getRowCount();
        for (int i =0; i <rowCount; i++) {
            dmo.removeRow(0);
        }
        //__________________________
        for(Long key:ofertas.keySet()){
            Oferta o=ofertas.get(key).getData();
            Object[] row=new Object[]{
                o.getIdentificador(),
                o.getCargo(),
                o.getNivelEstudios(),
                o.getExperienciaRequerida(),
                o.getSalarioOfrecido(),
                o.getSectorEmpresa()
            };
            dmo.addRow(row);
        }
        for(String key:candidatos.keySet()){
            Candidato c=candidatos.get(key).getData();
            Object[] row=new Object[]{
                c.getNombre(), 
                c.getDocumento(), 
                c.getNivelEstudios(),
                c.getAspiracionLaboral()
            };
            dmc.addRow(row);
        }
        //________________________________
        EntradasOfertas.setModel(dmo);
        dmo.fireTableDataChanged();
        EntradasOfertas.repaint();
        
        EntradasCandidatos.setModel(dmc);
        dmc.fireTableDataChanged();
        EntradasCandidatos.repaint();
        //_______________________________
    }
    
    private void conectar(){
        try {
            IP = java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(IP);

        Map<String,CandidatoCliente> candidatoClientes = new HashMap<>();
        Map<String,OfertaCliente> ofertasCliente = new HashMap<>();
        
        try {
            //System.setProperty("java.rmi.server.hostname","192.168.43.171");            
            ImplementacionCandidato obj = new ImplementacionCandidato(ofertas,candidatos, candidatoClientes,ofertasCliente);
            ImplementacionOferta obj1 = new ImplementacionOferta(ofertas,candidatos ,candidatoClientes , ofertasCliente);
            ServerImplementation obj2=new ServerImplementation();
            
            OperacionesCandidato stub = (OperacionesCandidato) UnicastRemoteObject.exportObject(obj, 9635);
            OperacionesOferta stub1 = (OperacionesOferta) UnicastRemoteObject.exportObject(obj1, 9635);
            ServerInterface stub2 =(ServerInterface) UnicastRemoteObject.exportObject(obj2, 9635);
            
            
            
            Registry registry = LocateRegistry.createRegistry(9635);
            registry.rebind("Candidato", stub);
            registry.rebind("Oferta", stub1);
            registry.rebind("Servidor", stub2);
            

            
        } catch (RemoteException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void connectServer(String serverIP) throws RemoteException, NotBoundException{
        
        Registry ServerReg = LocateRegistry.getRegistry(serverIP, 9635);
        ServerInterface ServerStub = (ServerInterface) ServerReg.lookup("Servidor");
        ServerStub.conectWithServer(IP);
        ServerImplementation.addVecinoLocal(serverIP);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        IPTextField = new javax.swing.JTextField();
        ServidorButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        EntradasOfertas = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        EntradasCandidatos = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setText("IPs:");
        getContentPane().add(jLabel1);

        IPTextField.setText("127.0.0.1");
        IPTextField.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        IPTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IPTextFieldActionPerformed(evt);
            }
        });
        getContentPane().add(IPTextField);

        ServidorButton.setText("nuevo servidor");
        ServidorButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ServidorButtonMouseClicked(evt);
            }
        });
        getContentPane().add(ServidorButton);

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jLabel2.setText("Ofertas");
        jPanel2.add(jLabel2);

        EntradasOfertas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "oferta", "cargo", "estudios", "experiencia", "salario", "sector"
            }
        ));
        jScrollPane1.setViewportView(EntradasOfertas);

        jPanel2.add(jScrollPane1);

        jButton3.setText("jButton3");
        jPanel2.add(jButton3);

        jPanel1.add(jPanel2);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        jLabel3.setText("Candidatos");
        jPanel3.add(jLabel3);

        EntradasCandidatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "nombre", "documento", "estudios", "aspiracion"
            }
        ));
        jScrollPane2.setViewportView(EntradasCandidatos);

        jPanel3.add(jScrollPane2);

        jButton2.setText("jButton2");
        jPanel3.add(jButton2);

        jPanel1.add(jPanel3);

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void IPTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IPTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IPTextFieldActionPerformed

    private void ServidorButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ServidorButtonMouseClicked
        System.out.println("add server");
        try {
            connectServer(IPTextField.getText().trim());
        } catch (RemoteException ex) {
            Logger.getLogger(VistaServidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(VistaServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ServidorButtonMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VistaServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VistaServidor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable EntradasCandidatos;
    private javax.swing.JTable EntradasOfertas;
    private javax.swing.JTextField IPTextField;
    private javax.swing.JButton ServidorButton;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
