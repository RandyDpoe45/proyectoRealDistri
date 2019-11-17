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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.ExperienciaLaboral;
import negocio.Oferta;

/**
 *
 * @author randy
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static List<Candidato> candidatos = new ArrayList<>();  
    public static List<Oferta> ofertas = new ArrayList<>();
    public static Map<String,Registry> vecinos=new HashMap<>();
    public static String Ip="127.0.0.1";
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String ip="";
        try {
            //System.setProperty("java.rmi.server.hostname","192.168.43.171");
            ImplementacionCandidato obj = new ImplementacionCandidato();
            ImplementacionOferta obj1 = new ImplementacionOferta();
            ServerImplementation obj2=new ServerImplementation();
            OperacionesCandidato stub = (OperacionesCandidato) UnicastRemoteObject.exportObject(obj, 9635);
            OperacionesOferta stub1 = (OperacionesOferta) UnicastRemoteObject.exportObject(obj1, 9635);
            ServerInterface stub2=(ServerInterface) UnicastRemoteObject.exportObject(obj2, 9635);
            Registry registry = LocateRegistry.createRegistry(9635);
            registry.rebind("Candidato", stub);
            registry.rebind("Oferta", stub1);
            registry.rebind("Server", stub2);
            while(!ip.equals("0.0.0.0")){
                ip=scanner.next();
                addServer(ip);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static int evaluarCandidato(Oferta o, Candidato c){
        int puntaje=0;
        if(c.getNivelEstudios()>=o.getNivelEstudios()&&
        c.getAspiracionLaboral()<=o.getSalarioOfrecido()){
            for(ExperienciaLaboral e:c.getExperiencia()){
                if(e.getCargo().equals(o.getCargo())&&
                e.getDuracion()>=o.getExperienciaRequerida()){
                    int base=60;
                    int bono=0;
                    bono+=(c.getAspiracionLaboral()<o.getSalarioOfrecido())?1:0;
                    bono+=(e.getDuracion()>o.getExperienciaRequerida())?1:0;
                    bono+=(e.getSectorEmpresa().equals(o.getSectorEmpresa()))?2:0;
                    puntaje=(puntaje>base+bono*10)?puntaje:base+bono*10;
                }
            }
        }
        return puntaje;
    }
    
    public static void addServer(String ip){
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(ip, 9635);
            ServerInterface si = (ServerInterface) registry.lookup("Server");
            si.conectWithServer(Ip);
            vecinos.put(ip, registry);
        } catch (RemoteException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
