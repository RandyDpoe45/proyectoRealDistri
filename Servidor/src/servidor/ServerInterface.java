/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import negocio.Candidato;
import negocio.Oferta;

/**
 *
 * @author gustavo
 */
public interface ServerInterface extends Remote {
    void conectWithServer(String ip) throws RemoteException;
}
