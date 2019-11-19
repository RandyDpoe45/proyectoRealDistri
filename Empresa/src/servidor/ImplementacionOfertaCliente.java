/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.Candidato;
import negocio.Oferta;

/**
 *
 * @author randy
 */
public class ImplementacionOfertaCliente implements OfertaCliente{
    
    private List<Oferta> ofertas;
    private Locker locker;

    public ImplementacionOfertaCliente(List<Oferta> ofertas) {
        this.ofertas = ofertas;
        this.locker=new Locker();
    }
    
    
    
    @Override
    public void notificarOferta(Long identificador, Candidato c, String documento) throws RemoteException {
        try {
            System.out.println("in");
            this.locker.lockRead();
            for(Oferta o:this.ofertas){
                if(o.getIdentificador()==identificador){
                    o.addCandidatoAsignados(c);
                }
            }
            System.out.println("actualizar:"+c);
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionOfertaCliente.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
