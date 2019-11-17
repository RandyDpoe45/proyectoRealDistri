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
import java.util.Scanner;
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
            //System.setProperty("java.rmi.server.hostname","192.168.43.171");
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 9635);
            OperacionesCandidato stub = (OperacionesCandidato) registry.lookup("Candidato");
            Candidato c = new Candidato();
            c.setNombre("nada");
            c.setDocumento("123456");
            Candidato response = stub.imprimirCandidato(c);
            
            System.out.println(response.getExperiencia().get(0).getCargo());
        } catch (RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
