/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

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
        String IP="192.168.43.17";
        int port=scn.nextInt();
        try {
            List<Candidato> candidatos=new ArrayList<>();
            //System.setProperty("java.rmi.server.hostname","192.168.43.171");
            ImplementacionCandidatoCliente icc=new ImplementacionCandidatoCliente(candidatos);
            CandidatoCliente cc= (CandidatoCliente) UnicastRemoteObject.exportObject(icc, port);
            Registry reg = LocateRegistry.createRegistry(port);
            reg.rebind("CandidatoCliente", cc);
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 9635);
            OperacionesCandidato stub = (OperacionesCandidato) registry.lookup("Candidato");
            stub.registrar(IP, 15000);
            Reader.read("./src/persistencia/candidatos.txt", stub, IP,port);
        } catch (RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    
}
    
}
