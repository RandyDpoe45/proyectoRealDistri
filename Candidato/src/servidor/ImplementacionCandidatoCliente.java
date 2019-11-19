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
import presentacion.ClienteGUI;

/**
 *
 * @author randy
 */
public class ImplementacionCandidatoCliente implements CandidatoCliente{
    private static ImplementacionCandidatoCliente self;
    private List<Candidato> candidatos;
    private Locker locker;
    public static ClienteGUI gui;
    
    public ImplementacionCandidatoCliente(List<Candidato> candidatos) {
        this.candidatos = candidatos;
        this.locker = new Locker();
        self=this;
    }
    
    
    @Override
    public void actualizarCandidato(String cedula, Long identificador, Oferta oferta) throws RemoteException {
        try {
            System.out.println("in");
            this.locker.lockRead();
            for(Candidato c:this.candidatos){
                if(c.getDocumento().trim().equals(cedula.trim())){
                    c.setOfertaAsignadas(oferta);
                    c.setIdOferta(identificador);
                }
            }
            System.out.println("actualizar:"+oferta);
            if(gui!=null){
                gui.actualizarEntradas();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionCandidatoCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
