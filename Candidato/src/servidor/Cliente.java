/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import negocio.Candidato;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.ExperienciaLaboral;
import negocio.SectorEmpresa;
import persistencia.Reader;

/**
 *
 * @author randy
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        try {
            //System.setProperty("java.rmi.server.hostname","192.168.43.171");
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 9635);
            OperacionesCandidato stub = (OperacionesCandidato) registry.lookup("Candidato");
            String file="./src/persistencia/candidatos.txt";
            Reader.read(file,stub);
        } catch (RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    



    
}
