/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import negocio.Candidato;
import negocio.Oferta;
import negocio.SectorEmpresa;
import negocio.Sobre;
import persistencia.Reader;
import servidor.Empresa;
import servidor.ImplementacionOfertaCliente;
import servidor.OfertaCliente;
import servidor.OperacionesOferta;

/**
 *
 * @author gustavo
 */
public class EmpresaGUI extends javax.swing.JFrame {

    /**
     * Creates new form Empresa
     */
    private static String ServerIP="127.0.0.1";
    private static String IP="127.0.0.1";
    private static int port;
    private static OperacionesOferta stub;
    private static List<Oferta> ofertas;
    public EmpresaGUI() {
        initComponents();
    }
    
    private void conectar(){
        try {
            IP = java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Empresa.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(IP);
        try {
            ofertas=new ArrayList<>();
            //System.setProperty("java.rmi.server.hostname","192.168.43.171");
            ImplementacionOfertaCliente ioc=new ImplementacionOfertaCliente(ofertas);
            OfertaCliente oc=(OfertaCliente) UnicastRemoteObject.exportObject(ioc, port);
            Registry reg = LocateRegistry.createRegistry(port);
            reg.rebind("OfertaCliente", oc);
            Registry registry = LocateRegistry.getRegistry(ServerIP, 9635);
            stub = (OperacionesOferta) registry.lookup("Oferta");
            stub.registrar(IP, port);
            //System.out.println(response.getCargo());
        } catch (RemoteException ex) {
            Logger.getLogger(Oferta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Oferta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        public void actualizarEntradas() throws InterruptedException{
        DefaultTableModel dm;
        dm=(DefaultTableModel) Entradas.getModel();
        int rowCount = dm.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i =0; i <rowCount; i++) {
            dm.removeRow(0);
        }
        for(Oferta o:ofertas){
            Object[] row=new Object[]{
                o.getIdentificador(),
                o.getCargo(),
                o.getNivelEstudios(),
                o.getExperienciaRequerida(),
                o.getSalarioOfrecido(),
                o.getSectorEmpresa()
            };
            dm.addRow(row);
        }

        Entradas.setModel(dm);
        dm.fireTableDataChanged();
        Entradas.repaint();
    }
    
    private static Map<String,SectorEmpresa> stringToSector=new HashMap<String,SectorEmpresa>();
    private void read(String file,OperacionesOferta stub,String IP,int port,List<Oferta> listaOfertas){
        stringToSector.put("comercio", SectorEmpresa.comercio);
        stringToSector.put("financiero", SectorEmpresa.financiero);
        stringToSector.put("manufactura", SectorEmpresa.manufactura);
        listaOfertas = new ArrayList<>();

        File archivoCandidatos = new File(file);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(archivoCandidatos));
        String st;

        while ((st = br.readLine()) != null) {
            String[] info_candidato = st.split(" ");

            String cargo = info_candidato[0];
            int nivel_estudios = Integer.parseInt(info_candidato[1]);
            int experiencia = Integer.parseInt(info_candidato[2]);
            float salario = Float.parseFloat(info_candidato[3].trim());
            SectorEmpresa sector=stringToSector.get(info_candidato[4].trim());
            
            Oferta oferta = new Oferta(cargo, nivel_estudios,experiencia, salario,sector);

            try {
                Oferta resultado=stub.registrarOferta(new Sobre<Oferta>(IP+":"+port,oferta),new ArrayList<>());
                System.out.println(resultado);
                oferta=resultado;
            } catch (RemoteException e) {
                System.out.println("Excepcion: " + e);
            }

            ofertas.add(oferta);
            actualizarEntradas();
            int tiempo_espera = Integer.parseInt(br.readLine().trim());
            Thread.sleep(tiempo_espera * 1000);
        }
        } catch (IOException ex) {
            Logger.getLogger(Empresa.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Empresa.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        portTextField = new javax.swing.JTextField();
        startButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Entradas = new javax.swing.JTable();
        CandidatosButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setText("ingresar IP del server");
        getContentPane().add(jLabel1);

        IPTextField.setText("127.0.0.1");
        getContentPane().add(IPTextField);

        portTextField.setText("15000");
        getContentPane().add(portTextField);

        startButton.setText("start");
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startButtonMouseClicked(evt);
            }
        });
        getContentPane().add(startButton);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        Entradas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "oferta", "cargo", "estudios", "experiencia", "salario", "sector"
            }
        ));
        jScrollPane1.setViewportView(Entradas);

        jPanel1.add(jScrollPane1);

        getContentPane().add(jPanel1);

        CandidatosButton.setText("ver candidatos");
        CandidatosButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CandidatosButtonMouseClicked(evt);
            }
        });
        CandidatosButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CandidatosButtonActionPerformed(evt);
            }
        });
        getContentPane().add(CandidatosButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonMouseClicked
        System.out.println("started...");
        ServerIP=IPTextField.getText().trim();
        port=Integer.decode(portTextField.getText().trim());
        conectar();
        Thread t=new Thread(){
            @Override
            public void run(){
                read("./src/persistencia/ofertas.txt", stub, IP,port,ofertas);
            }
        };
        t.start();
    }//GEN-LAST:event_startButtonMouseClicked

    private void CandidatosButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CandidatosButtonMouseClicked
        System.out.println("candidatos");
        int selected=Entradas.getSelectedRow();
        Oferta o=ImplementacionOfertaCliente.self.ofertas.get(selected);
        VistaCandidatos vc=new VistaCandidatos(o.getCandidatosAsignados());
    }//GEN-LAST:event_CandidatosButtonMouseClicked

    private void CandidatosButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CandidatosButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CandidatosButtonActionPerformed

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
            java.util.logging.Logger.getLogger(EmpresaGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmpresaGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmpresaGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmpresaGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmpresaGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CandidatosButton;
    private javax.swing.JTable Entradas;
    private javax.swing.JTextField IPTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField portTextField;
    private javax.swing.JButton startButton;
    // End of variables declaration//GEN-END:variables
}
