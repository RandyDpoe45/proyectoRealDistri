/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import negocio.Candidato;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import negocio.Sobre;

/**
 *
 * @author randy
 */
public interface OperacionesCandidato extends Remote {
    void registrar(String hostName,int port) throws RemoteException;
    Candidato registrarCandidato(Sobre<Candidato> s,List<String> ips) throws RemoteException;
    
}