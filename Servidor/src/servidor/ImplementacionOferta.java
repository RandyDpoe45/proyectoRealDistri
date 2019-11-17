/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.rmi.RemoteException;
import java.util.List;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.Candidato;
import negocio.Oferta;

/**
 *
 * @author randy
 */
public class ImplementacionOferta implements OperacionesOferta{

    private List<Oferta> ofertas;
    private List<Candidato> candidatos;
    private Locker locker;

    public ImplementacionOferta(List<Oferta> ofertas, List<Candidato> candidatos) {
        this.ofertas = ofertas;
        this.candidatos = candidatos;
        this.locker = new Locker();
    }
    
    
    @Override
    public Oferta imprimirOferta(Oferta o) {
        System.out.println(o.toString());
        return o;
    }

    @Override
    public Oferta registrarOferta(Oferta o) throws RemoteException {
        try {
            locker.lockWrite();
            this.ofertas.add(o);
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                locker.unlockWrite();
            } catch (InterruptedException ex) {
                Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        PriorityQueue<Entry<Candidato>> que = new PriorityQueue<>();
        try {
            
            locker.lockRead();
            for(Candidato c : this.candidatos){
                que.add(new Entry(Oferta.evaluarCandidato(o, c),c));
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            locker.unlockRead();
        }
        try {
            locker.lockWrite();
            for(int i=0;i<3 && !que.isEmpty();i++){
                Entry<Candidato> aux =que.poll();
                aux.getValue().setOfertaAsignadas(o);
                o.getCandidatosAsignados().add(aux.getValue());
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
        return o;
        
    }


}
