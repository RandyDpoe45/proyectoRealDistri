/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import negocio.Candidato;
import negocio.Oferta;

/**
 *
 * @author gustavo
 */
public class ServerImplementation implements Serializable, ServerInterface{

    @Override
    public void conectWithServer(String ip) throws RemoteException {
         Servidor.vecinos.put(ip,LocateRegistry.getRegistry(ip, 9635));
         System.out.println("conect:"+ip);
    }

    
    
}
