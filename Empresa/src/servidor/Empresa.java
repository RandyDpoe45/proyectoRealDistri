/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.net.UnknownHostException;
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
import negocio.Candidato;
import negocio.Oferta;
import persistencia.Reader;

/**
 *
 * @author randy
 */
public class Empresa {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scn=new Scanner(System.in);
        String IP = null;
        String ServerIP=null;
        try {
            IP = java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Empresa.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(IP);
        System.out.println("serverIP:");
        ServerIP=scn.next();
        System.out.println("port:");
        int port=scn.nextInt();
        try {
            List<Oferta> ofertas=new ArrayList<>();
            //System.setProperty("java.rmi.server.hostname","192.168.43.171");
            ImplementacionOfertaCliente ioc=new ImplementacionOfertaCliente(ofertas);
            OfertaCliente oc=(OfertaCliente) UnicastRemoteObject.exportObject(ioc, port);
            Registry reg = LocateRegistry.createRegistry(port);
            reg.rebind("OfertaCliente", oc);
            Registry registry = LocateRegistry.getRegistry(ServerIP, 9635);
            OperacionesOferta stub = (OperacionesOferta) registry.lookup("Oferta");
            stub.registrar(IP, port);
            Reader.read("./src/persistencia/ofertas.txt", stub, ofertas,IP,port);
            //System.out.println(response.getCargo());
        } catch (RemoteException ex) {
            Logger.getLogger(Oferta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Oferta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
