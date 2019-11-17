/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import negocio.Candidato;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.Oferta;

/**
 *
 * @author randy
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Map<String,Candidato> candidatos = new HashMap<>();  
        Map<Long,Oferta> ofertas = new HashMap<>();
        try {
            //System.setProperty("java.rmi.server.hostname","192.168.43.171");            
            ImplementacionCandidato obj = new ImplementacionCandidato(ofertas,candidatos);
            ImplementacionOferta obj1 = new ImplementacionOferta(ofertas,candidatos);
            OperacionesCandidato stub = (OperacionesCandidato) UnicastRemoteObject.exportObject(obj, 9635);
            OperacionesOferta stub1 = (OperacionesOferta) UnicastRemoteObject.exportObject(obj1, 9635);
            Registry registry = LocateRegistry.createRegistry(9635);
            registry.rebind("Candidato", stub);
            registry.rebind("Oferta", stub1);
            Registry ofertaClient = LocateRegistry.getRegistry("192.168.43.171", 9635);
            OperacionesOferta ofertaStub = (OperacionesOferta) ofertaClient.lookup("OfertaCliente");
            Registry candidatoClient = LocateRegistry.getRegistry("192.168.43.171", 9635);
            OperacionesOferta candidatoStub = (OperacionesOferta) candidatoClient.lookup("CandidatoCliente");
            
            
        } catch (RemoteException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
