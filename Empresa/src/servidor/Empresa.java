/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.Candidato;
import negocio.Oferta;

/**
 *
 * @author randy
 */
public class Empresa {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //System.setProperty("java.rmi.server.hostname","192.168.43.171");
            Registry registry = LocateRegistry.getRegistry("192.168.43.171", 9635);
            OperacionesOferta stub = (OperacionesOferta) registry.lookup("Oferta");
            
            //System.out.println(response.getCargo());
        } catch (RemoteException ex) {
            Logger.getLogger(Oferta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Oferta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
