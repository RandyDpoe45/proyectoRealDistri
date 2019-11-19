/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.Candidato;
import negocio.Oferta;
import negocio.Sobre;
import presentacion.VistaServidor;
import static servidor.ImplementacionCandidato.gui;

/**
 *
 * @author randy
 */
public class ImplementacionOferta implements OperacionesOferta{
    private static ImplementacionOferta self;
    private static Long indiceOferta =Long.valueOf(0);
    private Map<Long,DataEntry<Oferta>> ofertas;
    private Map<String,DataEntry<Candidato>> candidatos;
    private Map<String,CandidatoCliente> candidatoClientes;
    private Map<String,OfertaCliente> ofertasCliente;
    private Locker locker;
    public static VistaServidor gui;

    public ImplementacionOferta(Map<Long, DataEntry<Oferta>> ofertas, Map<String, DataEntry<Candidato>> candidatos, Map<String, CandidatoCliente> candidatoClientes, Map<String, OfertaCliente> ofertasCliente) {
        this.ofertas = ofertas;
        this.candidatos = candidatos;
        this.candidatoClientes = candidatoClientes;
        this.ofertasCliente = ofertasCliente;
        this.locker = new Locker();
        self=this;
    }
    
    

    @Override
    public Oferta registrarOferta(Sobre<Oferta> s,List<String> ips) throws RemoteException {
        Oferta o = s.getData();
        if(o.getCandidatosAsignados().size()>=3){
            return o;
        }
        if(o.getCandidatosAsignados()==null){
            o.setCandidatosAsignados(new ArrayList<>());
        }
        o.setIdentificador(indiceOferta);
        indiceOferta ++;
        try {
            locker.lockWrite();
            System.out.println(ofertasCliente.keySet()+":"+s.getHostName());
            if(ofertasCliente.containsKey(s.getHostName())){
                this.ofertas.put(s.getData().getIdentificador(), new DataEntry<Oferta>(s.getHostName(),s.getData()));
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
        PriorityQueue<Entry<Candidato>> que = new PriorityQueue<>();
        try {
            
            locker.lockRead();
            for(DataEntry<Candidato> c : this.candidatos.values()){
                que.add(new Entry(Oferta.evaluarCandidato(o, c.getData()),c.getData()));
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            locker.unlockRead();
        }
        if(!que.isEmpty()){
        try {
            locker.lockWrite();
            //DataEntry<Oferta> of = this.ofertas.get(o.getIdentificador());
            //OfertaCliente ofc = this.ofertasCliente.get(of.getHostName());
            for(int i=0;i<3 && !que.isEmpty();){
                System.out.println("i:"+i);
                Entry<Candidato> aux = que.poll();
                Candidato can=(Candidato)aux.getValue();
                if( aux.getPuntaje() >= 70){
                    can.setOfertaAsignadas(o);
                    o.addCandidato(can);
                    System.out.println(this.candidatoClientes.keySet()+"::"+this.candidatos.get(can.getDocumento()).getHostName());
                    CandidatoCliente candi = this.candidatoClientes.get(this.candidatos.get(can.getDocumento()).getHostName());
                    if(candi==null){
                        DataEntry<Candidato> entry = this.candidatos.get(can.getDocumento());
                        String[] split;
                        split = entry.getHostName().split(":");
                        CandidatoCliente cc=(CandidatoCliente) LocateRegistry.getRegistry(split[0],Integer.decode(split[1])).lookup("CandidatoCliente");
                        this.candidatoClientes.put(entry.getHostName(), cc);
                        candi=cc;
                    }
                    candi.actualizarCandidato(can.getDocumento(), o.getIdentificador(), o);
                    i++;
                }
                
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (NotBoundException ex) {
                Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AccessException ex) {
                Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
            try {
                locker.unlockWrite();
            } catch (InterruptedException ex) {
                Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }
        //forward server
        ips.add(VistaServidor.IP);
        for(String ip:ServerImplementation.vecinos.keySet()){
            if(!ips.contains(ip)){
                Registry R=ServerImplementation.vecinos.get(ip);
                try {
                    OperacionesOferta stub=(OperacionesOferta) R.lookup("Oferta");
                    o=stub.registrarOferta(s, ips);
                } catch (NotBoundException ex) {
                    Logger.getLogger(ImplementacionCandidato.class.getName()).log(Level.SEVERE, null, ex);
                } catch (AccessException ex) {
                    Logger.getLogger(ImplementacionCandidato.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //end forwad
        System.out.println(o.getCandidatosAsignados());
        System.out.println(o);
        try {
            gui.actualizarEntradas();
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionCandidato.class.getName()).log(Level.SEVERE, null, ex);
        }
        return o;
        
    }

    @Override
    public void registrar(String hostName,int port) throws RemoteException {
        try {
            Registry ofertaClient = LocateRegistry.getRegistry(hostName, port);
            OfertaCliente ofertaStub = (OfertaCliente) ofertaClient.lookup("OfertaCliente");
            this.ofertasCliente.put(hostName+":"+port, ofertaStub);
        } catch (NotBoundException ex) {
            Logger.getLogger(ImplementacionCandidato.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccessException ex) {
            Logger.getLogger(ImplementacionCandidato.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
