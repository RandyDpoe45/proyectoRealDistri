/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.BufferedReader;
import negocio.Sobre;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.Oferta;
import negocio.SectorEmpresa;
import servidor.Empresa;
import servidor.OperacionesOferta;

/**
 *
 * @author gustavo
 */
public class Reader {
    
    
    
     private static Map<String,SectorEmpresa> stringToSector=new HashMap<String,SectorEmpresa>();
    public static void read(String file,OperacionesOferta stub,List<Oferta> ofertas,String IP,int port){ 
        
        stringToSector.put("comercio", SectorEmpresa.comercio);
        stringToSector.put("financiero", SectorEmpresa.financiero);
        stringToSector.put("manufactura", SectorEmpresa.manufactura);
        List<Oferta> listaOfertas = new ArrayList<>();

        File archivoCandidatos = new File(file);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(archivoCandidatos));
        String st;

        while ((st = br.readLine()) != null) {
            String[] info_candidato = st.split(" ");

            String cargo = info_candidato[0];
            int nivel_estudios = Integer.parseInt(info_candidato[1]);
            int experiencia = Integer.parseInt(info_candidato[2]);
            float salario = Float.parseFloat(info_candidato[3].trim());
            SectorEmpresa sector=stringToSector.get(info_candidato[4].trim());
            
            Oferta oferta = new Oferta(cargo, nivel_estudios,experiencia, salario,sector);

            try {
                Oferta resultado=stub.registrarOferta(new Sobre<Oferta>(IP+":"+port,oferta),new ArrayList<>());
                System.out.println(resultado);
            } catch (RemoteException e) {
                System.out.println("Excepcion: " + e);
            }

            ofertas.add(oferta);
            int tiempo_espera = Integer.parseInt(br.readLine().trim());
            Thread.sleep(tiempo_espera * 1000);
        }
        } catch (IOException ex) {
            Logger.getLogger(Empresa.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Empresa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
