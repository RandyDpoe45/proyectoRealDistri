/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
import javax.swing.table.TableModel;
import negocio.Candidato;
import negocio.ExperienciaLaboral;
import negocio.Oferta;
import negocio.SectorEmpresa;
import negocio.Sobre;
import persistencia.Reader;
import servidor.CandidatoCliente;
import servidor.ImplementacionCandidatoCliente;
import servidor.OperacionesCandidato;

/**
 *
 * @author gustavo
 */
public class ClienteGUI extends javax.swing.JFrame {

    /**
     * Creates new form Cliente
     */
    private static String ServerIP="127.0.0.1";
    private static String IP="127.0.0.1";
    private static int port;
    private static OperacionesCandidato stub;
    private static List<Candidato> candidatos;
    public ClienteGUI() {
        initComponents();
    }
    
    private void conectar(){
        ImplementacionCandidatoCliente.gui=this;
        try {
            IP = java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) { 
            Logger.getLogger(ClienteGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(IP);
        try {
            candidatos=new ArrayList<>();
            //System.setProperty("java.rmi.server.hostname","192.168.43.171");
            ImplementacionCandidatoCliente icc=new ImplementacionCandidatoCliente(candidatos);
            CandidatoCliente cc= (CandidatoCliente) UnicastRemoteObject.exportObject(icc, port);
            Registry reg = LocateRegistry.createRegistry(port);
            reg.rebind("CandidatoCliente", cc);
            Registry registry = LocateRegistry.getRegistry(ServerIP, 9635);
            stub = (OperacionesCandidato) registry.lookup("Candidato");
            stub.registrar(IP, port);
            
        } catch (RemoteException ex) {
            Logger.getLogger(ClienteGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(ClienteGUI.class.getName()).log(Level.SEVERE, null, ex);
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
        for(Candidato c:candidatos){
            Object[] row=new Object[]{
                c.getNombre(), 
                c.getDocumento(), 
                c.getNivelEstudios(),
                c.getAspiracionLaboral(),
                (c.getIdOferta()!=null)?c.getIdOferta():"none"
            };
            dm.addRow(row);
        }

        Entradas.setModel(dm);
        dm.fireTableDataChanged();
        Entradas.repaint();
    }
    
    private static Map<String,SectorEmpresa> stringToSector=new HashMap<String,SectorEmpresa>();
    public void read(String file,OperacionesCandidato stub,String IP,int port,List<Candidato> listaCandidatos){ 
        stringToSector.put("comercio", SectorEmpresa.comercio);
        stringToSector.put("financiero", SectorEmpresa.financiero);
        stringToSector.put("manufactura", SectorEmpresa.manufactura);

        File archivoCandidatos = new File(file);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(archivoCandidatos));
        String st;

        while ((st = br.readLine()) != null) {
            String[] info_candidato = st.split(" ");

            String nombre = info_candidato[0];
            String documento = info_candidato[1];
            int nivel_estudios = Integer.parseInt(info_candidato[2]);
            float aspiracion_salarial = Float.parseFloat(info_candidato[3].trim());

            Candidato candidato = new Candidato(nombre, documento, nivel_estudios, aspiracion_salarial);
            List<ExperienciaLaboral> listaExperienciasLaborales = new ArrayList<>();

            while (!"0".equals(st = br.readLine())) {
                String[] experiencia_laboral = st.split(" ");

                String cargo = experiencia_laboral[0];
                int duracion = Integer.parseInt(experiencia_laboral[1].trim());
                SectorEmpresa sector = stringToSector.get(experiencia_laboral[2]);

                ExperienciaLaboral info_experiencia_laboral = new ExperienciaLaboral(cargo, duracion, sector);
                listaExperienciasLaborales.add(info_experiencia_laboral);
            }
            candidato.setExperiencia(listaExperienciasLaborales);

            try {
                Candidato output=stub.registrarCandidato(new Sobre<Candidato>(IP+":"+port,candidato),new ArrayList());
                System.out.println(output);
                candidato=output;
                
            } catch (RemoteException e) {
                System.out.println("Excepcion: " + e);
            }

            listaCandidatos.add(candidato);
            actualizarEntradas();
            int tiempo_espera = Integer.parseInt(br.readLine().trim());
            Thread.sleep(tiempo_espera * 1000);
        }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(servidor.Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(servidor.Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(servidor.Cliente.class.getName()).log(Level.SEVERE, null, ex);
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

        IPLabel = new javax.swing.JLabel();
        IPTextField = new javax.swing.JTextField();
        portTextField = new javax.swing.JTextField();
        startButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        Scroll = new javax.swing.JScrollPane();
        Entradas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        IPLabel.setText("ingresar IP del server y port local");
        getContentPane().add(IPLabel);

        IPTextField.setText("127.0.0.1");
        IPTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IPTextFieldActionPerformed(evt);
            }
        });
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
                "Nombre", "Documento", "Estudios", "Aspiracion", "Oferta"
            }
        ));
        Scroll.setViewportView(Entradas);

        jPanel1.add(Scroll);

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonMouseClicked
        // TODO add your handling code here:
        System.out.println("started...");
        ServerIP=IPTextField.getText().trim();
        port=Integer.decode(portTextField.getText().trim());
        conectar();
        Thread t=new Thread(){
            @Override
            public void run(){
                read("./src/persistencia/candidatos.txt", stub, IP,port,candidatos);
            }
        };
        t.start();
    }//GEN-LAST:event_startButtonMouseClicked

    private void IPTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IPTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IPTextFieldActionPerformed

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
            java.util.logging.Logger.getLogger(ClienteGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClienteGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClienteGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClienteGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClienteGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Entradas;
    private javax.swing.JLabel IPLabel;
    private javax.swing.JTextField IPTextField;
    private javax.swing.JScrollPane Scroll;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField portTextField;
    private javax.swing.JButton startButton;
    // End of variables declaration//GEN-END:variables
}
