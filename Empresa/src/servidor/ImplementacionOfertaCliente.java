/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.Candidato;
import negocio.Oferta;

/**
 *
 * @author randy
 */
public class ImplementacionOfertaCliente implements OfertaCliente{
    public static ImplementacionOfertaCliente self;
    public List<Oferta> ofertas;
    private Locker locker;

    public ImplementacionOfertaCliente(List<Oferta> ofertas) {
        this.ofertas = ofertas;
        this.locker=new Locker();
        self=this;
    }
    
    
    
    @Override
    public void notificarOferta(Long identificador, Candidato c, String documento) throws RemoteException {
        try {
            this.locker.lockRead();
            for(Oferta o:this.ofertas){
                if(Objects.equals(o.getIdentificador(), identificador) && o.getCandidatosAsignados().size()<3){
                    o.getCandidatosAsignados().add(c);
                }
            }
            System.out.println("actualizar:"+c);
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionOfertaCliente.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
