/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.net.UnknownHostException;
import negocio.Candidato;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.Reader;

/**
 *
 * @author randy
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scn=new Scanner(System.in);
        String IP="127.0.0.1";
        String ServerIP="127.0.0.1";
        try {
            IP = java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) { 
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(IP);
        System.out.println("serverIP:");
        ServerIP=scn.next();
        System.out.println("port:");
        int port=scn.nextInt();
        try {
            List<Candidato> candidatos=new ArrayList<>();
            //System.setProperty("java.rmi.server.hostname","192.168.43.171");
            ImplementacionCandidatoCliente icc=new ImplementacionCandidatoCliente(candidatos);
            CandidatoCliente cc= (CandidatoCliente) UnicastRemoteObject.exportObject(icc, port);
            Registry reg = LocateRegistry.createRegistry(port);
            reg.rebind("CandidatoCliente", cc);
            Registry registry = LocateRegistry.getRegistry(ServerIP, 9635);
            OperacionesCandidato stub = (OperacionesCandidato) registry.lookup("Candidato");
            stub.registrar(IP, port);
            Reader.read("./src/persistencia/candidatos.txt", stub, IP,port,candidatos);
        } catch (RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    
}
    
}
