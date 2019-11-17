/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.rmi.RemoteException;
import java.util.List;
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
        System.out.println("actualizar:"+c);
    }
    
}
