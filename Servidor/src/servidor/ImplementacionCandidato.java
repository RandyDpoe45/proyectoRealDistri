/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import negocio.Candidato;
import negocio.ExperienciaLaboral;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.Oferta;
import negocio.Sobre;
import presentacion.VistaServidor;

/**
 *
 * @author randy
 */
public class ImplementacionCandidato implements OperacionesCandidato{
    private static ImplementacionCandidato self;
    private Map<Long,DataEntry<Oferta>> ofertas;
    private Map<String,DataEntry<Candidato>> candidatos;
    private Map<String,CandidatoCliente> candidatoClientes;
    private Map<String,OfertaCliente> ofertasCliente;
    private Locker locker;
    public static VistaServidor gui;

    public ImplementacionCandidato(Map<Long, DataEntry<Oferta>> ofertas, Map<String, DataEntry<Candidato>> candidatos, Map<String, CandidatoCliente> candidatoClientes, Map<String, OfertaCliente> ofertasCliente) {
        this.ofertas = ofertas;
        this.candidatos = candidatos;
        this.candidatoClientes = candidatoClientes;
        this.ofertasCliente = ofertasCliente;
        this.locker = new Locker();
        self=this;
    }
    
    


    @Override
    public Candidato registrarCandidato(Sobre<Candidato> s,List<String> ips) throws RemoteException {
        Candidato c = (Candidato) s.getData();
        try {
            locker.lockWrite();
            if(candidatoClientes.keySet().contains(s.getHostName())){
                this.candidatos.put(c.getDocumento(), new DataEntry<Candidato>(s.getHostName(),c));
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                locker.unlockWrite();
            } catch (InterruptedException ex) {
                Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        PriorityQueue<Entry<Oferta>> que = new PriorityQueue<>();
        try {
            
            locker.lockRead();
            for(DataEntry<Oferta> o : this.ofertas.values()){
                
                que.add(new Entry(Oferta.evaluarCandidato(o.getData(), c),o.getData()));
            }
            
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            locker.unlockRead();
        }
        try {
            locker.lockWrite();
            while(!que.isEmpty()){
            Entry<Oferta> o = que.poll();
            if(o.getPuntaje()>70){
                DataEntry<Oferta> off = this.ofertas.get(o.getValue().getIdentificador());
                c.setOfertaAsignadas(o.getValue());
                c.setIdOferta(o.getValue().getIdentificador());
                //DataEntry<Candidato> candi = this.candidatos.get(c.getDocumento());
                //CandidatoCliente cc = this.candidatoClientes.get(candi.getHostName());
                //cc.actualizarCandidato(c.getDocumento(),off.getData().getIdentificador() , off.getData());
                OfertaCliente oc = this.ofertasCliente.get(off.getHostName());
                o.getValue().addCandidato(c);
                oc.notificarOferta(off.getData().getIdentificador(), c, c.getDocumento());
                System.out.println("notificando:"+c);
            }
        }
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                locker.unlockWrite();
            } catch (InterruptedException ex) {
                Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //forward server
        ips.add(VistaServidor.IP);
        for(String ip:ServerImplementation.vecinos.keySet()){
            if(!ips.contains(ip)){
                Registry R=ServerImplementation.vecinos.get(ip);
                try {
                    OperacionesCandidato stub=(OperacionesCandidato) R.lookup("Candidato");
                    c=stub.registrarCandidato(s, ips);
                } catch (NotBoundException ex) {
                    Logger.getLogger(ImplementacionCandidato.class.getName()).log(Level.SEVERE, null, ex);
                } catch (AccessException ex) {
                    Logger.getLogger(ImplementacionCandidato.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //end forwad
        System.out.println(c);
        try {
            gui.actualizarEntradas();
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionCandidato.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    @Override
    public void registrar(String hostName, int port) throws RemoteException {
        
        try {
            Registry candidatoClient = LocateRegistry.getRegistry(hostName, port);
            CandidatoCliente candidatoStub = (CandidatoCliente) candidatoClient.lookup("CandidatoCliente");
            this.candidatoClientes.put(hostName+":"+port, candidatoStub);
        } catch (NotBoundException ex) {
            Logger.getLogger(ImplementacionCandidato.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccessException ex) {
            Logger.getLogger(ImplementacionCandidato.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
}
