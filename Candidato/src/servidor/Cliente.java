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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author randy
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            List<Candidato> candidatos=new ArrayList<>();
            //System.setProperty("java.rmi.server.hostname","192.168.43.171");
            ImplementacionCandidatoCliente icc=new ImplementacionCandidatoCliente(candidatos);
            CandidatoCliente cc= (CandidatoCliente) UnicastRemoteObject.exportObject(icc, 15000);
            Registry reg = LocateRegistry.createRegistry(15000);
            reg.rebind("CandidatoCliente", cc);
            Registry registry = LocateRegistry.getRegistry("192.168.43.172", 9635);
            OperacionesCandidato stub = (OperacionesCandidato) registry.lookup("Candidato");
            
            stub.registrar("192.168.43.17", 15000);
        } catch (RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    
}
    
}
