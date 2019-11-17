/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import negocio.Candidato;
import negocio.ExperienciaLaboral;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author randy
 */
public class ImplementacionCandidato implements OperacionesCandidato{

    @Override
    public Candidato imprimirCandidato(Candidato c) throws RemoteException {
        Servidor.candidatos.add(c);
        List<ExperienciaLaboral> x = new ArrayList<>();
        ExperienciaLaboral y = new ExperienciaLaboral();
        y.setCargo("vendedor de perico");
        x.add(y);
        c.setExperiencia(x);
        c.setNivelEstudios(5);
        System.out.println(c.toString());
        return c;
    }
    
}
