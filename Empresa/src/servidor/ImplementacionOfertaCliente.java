/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.rmi.RemoteException;
import negocio.Candidato;

/**
 *
 * @author randy
 */
public class ImplementacionOfertaCliente implements OfertaCliente{

    @Override
    public void notificarOferta(Long identificador, Candidato c, String documento) throws RemoteException {
        
    }
    
}
