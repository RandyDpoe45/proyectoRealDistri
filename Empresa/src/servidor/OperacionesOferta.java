/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;
import negocio.Sobre;
import java.rmi.Remote;
import java.rmi.RemoteException;
import negocio.Oferta;

/**
 *
 * @author randy
 */
public interface OperacionesOferta extends Remote {
    void registrar(String hostName,int port) throws RemoteException;
    Oferta registrarOferta(Sobre<Oferta> s) throws RemoteException;
}
