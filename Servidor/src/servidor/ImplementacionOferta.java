/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.Candidato;
import negocio.Oferta;

/**
 *
 * @author randy
 */
public class ImplementacionOferta implements OperacionesOferta{

    @Override
    public Oferta imprimirOferta(Oferta o,List<String> ips)throws RemoteException {
        System.out.println(o.toString());
        try {
            return reenviar(o,ips);
        } catch (NotBoundException ex) {
            System.out.println("fallo");
            Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return o;
    }
    
    public Oferta reenviar(Oferta o,List<String> ips) throws RemoteException, NotBoundException{
        ips.add(Servidor.Ip);
        Oferta O = o;
        for(String ip:Servidor.vecinos.keySet()){
            if(!ips.contains(ip)){
                    Registry registry=Servidor.vecinos.get(ip);
                    OperacionesOferta oo = (OperacionesOferta) registry.lookup("Oferta");
                    O=oo.imprimirOferta(o, ips);
            }
        }
        return O;
    }
    
}
