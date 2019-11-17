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
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.Oferta;

/**
 *
 * @author randy
 */
public class ImplementacionCandidato implements OperacionesCandidato{

    private Map<Long,Oferta> ofertas;
    private Map<String,Candidato> candidatos;
    private Locker locker;
    public ImplementacionCandidato(Map<Long,Oferta> ofertas, Map<String,Candidato> candidatos) {
        this.ofertas = ofertas;
        this.candidatos = candidatos;
        this.locker = new Locker();
    }
    
    @Override
    public Candidato imprimirCandidato(Candidato c) throws RemoteException {
        List<ExperienciaLaboral> x = new ArrayList<>();
        ExperienciaLaboral y = new ExperienciaLaboral();
        y.setCargo("vendedor de perico");
        x.add(y);
        c.setExperiencia(x);
        c.setNivelEstudios(5);
        System.out.println(c.toString());
        return c;
    }

    @Override
    public Candidato registrarCandidato(Candidato c) throws RemoteException {
        try {
            locker.lockWrite();
            this.candidatos.add(c);
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                locker.unlockWrite();
            } catch (InterruptedException ex) {
                Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        PriorityQueue<Entry<Oferta>> que = new PriorityQueue<>();
        try {
            
            locker.lockRead();
            for(Oferta o : this.ofertas){
                que.add(new Entry(Oferta.evaluarCandidato(o, c),o));
            }
            
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            locker.unlockRead();
        }
        try {
            locker.lockWrite();
            for(int i=0;i<3 && !que.isEmpty();i++){
                Entry<Oferta> aux =que.poll();
                aux.getValue().getCandidatosAsignados().add(c);
                c.setOfertaAsignadas(aux.getValue());
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                locker.unlockWrite();
            } catch (InterruptedException ex) {
                Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return c;
    }

    
}
