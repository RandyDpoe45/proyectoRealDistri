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

/**
 *
 * @author randy
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    private static Map<String,SectorEmpresa> stringToSector=new HashMap<String,SectorEmpresa>();
    public static void main(String[] args) {
        stringToSector.put("comercio", SectorEmpresa.comercio);
        stringToSector.put("financiero", SectorEmpresa.financiero);
        stringToSector.put("manufactura", SectorEmpresa.manufactura);
        try {
            //System.setProperty("java.rmi.server.hostname","192.168.43.171");
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 9635);
            OperacionesCandidato stub = (OperacionesCandidato) registry.lookup("Candidato");
            String file="./src/persistencia/candidatos.txt";
            read(file,stub);
        } catch (RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private static void read(String file,OperacionesCandidato stub){ 

        List<Candidato> listaCandidatos = new ArrayList<>();

        File archivoCandidatos = new File(file);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(archivoCandidatos));
        String st;

        while ((st = br.readLine()) != null) {
            String[] info_candidato = st.split(" ");

            String nombre = info_candidato[0];
            String documento = info_candidato[1];
            int nivel_estudios = Integer.parseInt(info_candidato[2]);
            float aspiracion_salarial = Float.parseFloat(info_candidato[3].trim());

            Candidato candidato = new Candidato(nombre, documento, nivel_estudios, aspiracion_salarial);
            List<ExperienciaLaboral> listaExperienciasLaborales = new ArrayList<>();

            while (!"0".equals(st = br.readLine())) {
                String[] experiencia_laboral = st.split(" ");

                String cargo = experiencia_laboral[0];
                int duracion = Integer.parseInt(experiencia_laboral[1].trim());
                SectorEmpresa sector = stringToSector.get(experiencia_laboral[2]);

                ExperienciaLaboral info_experiencia_laboral = new ExperienciaLaboral(cargo, duracion, sector);
                listaExperienciasLaborales.add(info_experiencia_laboral);
            }
            candidato.setExperiencia(listaExperienciasLaborales);

            try {

                stub.imprimirCandidato(candidato);
            } catch (RemoteException e) {
                System.out.println("Excepcion: " + e);
            }

            listaCandidatos.add(candidato);
            int tiempo_espera = Integer.parseInt(br.readLine().trim());
            Thread.sleep(tiempo_espera * 1000);
        }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    
}
