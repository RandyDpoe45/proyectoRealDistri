/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import negocio.Candidato;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author randy
 */
public interface OperacionesCandidato extends Remote {
    Candidato imprimirCandidato(Candidato c) throws RemoteException;
    Candidato registrarCandidato(Candidato c) throws RemoteException;
    
    
}
