/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.rmi.NotBoundException;
import negocio.Candidato;
import negocio.ExperienciaLaboral;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author randy
 */
public class ImplementacionCandidato implements OperacionesCandidato{

    @Override
    public Candidato imprimirCandidato(Candidato c,List<String> ips) throws RemoteException {
        Servidor.candidatos.add(c);
        List<ExperienciaLaboral> x = new ArrayList<>();
        ExperienciaLaboral y = new ExperienciaLaboral();
        y.setCargo("vendedor de perico");
        x.add(y);
        c.setExperiencia(x);
        c.setNivelEstudios(5);
        System.out.println(c.toString());
        try {
            return reenviar(c,ips);
        } catch (NotBoundException ex) {
            Logger.getLogger(ImplementacionCandidato.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }
    
    public Candidato reenviar(Candidato c,List<String> ips) throws RemoteException, NotBoundException{
        ips.add(Servidor.Ip);
        Candidato C = c;
        for(String ip:Servidor.vecinos.keySet()){
            if(!ips.contains(ip)){
                    Registry registry=Servidor.vecinos.get(ip);
                    OperacionesCandidato oc = (OperacionesCandidato) registry.lookup("Candidato");
                    C=oc.imprimirCandidato(c, ips);
            }
        }
        return C;
    }

}
