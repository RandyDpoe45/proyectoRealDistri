/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.Candidato;
import negocio.Oferta;

/**
 *
 * @author gustavo
 */
public class ServerImplementation implements Serializable, ServerInterface{
    public static Map<String,Registry> vecinos=new HashMap<>();
    @Override
    public void conectWithServer(String ip) throws RemoteException {
        Registry registry=LocateRegistry.getRegistry(ip, 9635);
         vecinos.put(ip,registry);
        try {
            ServerInterface stub=(ServerInterface) registry.lookup("Server");
            stub.conectWithServer(Servidor.IP);
        } catch (NotBoundException ex) {
            Logger.getLogger(ServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccessException ex) {
            Logger.getLogger(ServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         System.out.println("conect:"+ip);
    }

    
    
}
