/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
         vecinos.put(ip,LocateRegistry.getRegistry(ip, 9635));
         System.out.println("conect:"+ip);
    }

    
    
}
