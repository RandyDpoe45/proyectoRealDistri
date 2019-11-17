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
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.Candidato;
import negocio.Oferta;
import negocio.Sobre;

/**
 *
 * @author randy
 */
public class ImplementacionOferta implements OperacionesOferta{
    private static Long indiceOferta;
    private Map<Long,DataEntry<Oferta>> ofertas;
    private Map<String,DataEntry<Candidato>> candidatos;
    private Map<String,CandidatoCliente> candidatoClientes;
    private Map<String,OfertaCliente> ofertasCliente;
    private Locker locker;

    public ImplementacionOferta(Map<Long, DataEntry<Oferta>> ofertas, Map<String, DataEntry<Candidato>> candidatos, Map<String, CandidatoCliente> candidatoClientes, Map<String, OfertaCliente> ofertasCliente) {
        this.ofertas = ofertas;
        this.candidatos = candidatos;
        this.candidatoClientes = candidatoClientes;
        this.ofertasCliente = ofertasCliente;
        this.locker = new Locker();
    }
    
    

    @Override
    public Oferta registrarOferta(Sobre<Oferta> s) throws RemoteException {
        Oferta o = s.getData();
        o.setIdentificador(indiceOferta);
        indiceOferta ++;
        try {
            locker.lockWrite();
            this.ofertas.put(s.getData().getIdentificador(), new DataEntry<Oferta>(s.getHostName(),s.getData()));
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
                que.add(new Entry(Oferta.evaluarCandidato(o, c.getData()),c));
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ImplementacionOferta.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            locker.unlockRead();
        }
        try {
            locker.lockWrite();
            DataEntry<Oferta> of = this.ofertas.get(o.getIdentificador());
            OfertaCliente ofc = this.ofertasCliente.get(of.getHostName());
            for(int i=0;i<3 && !que.isEmpty();){
                
                Entry<Candidato> aux = que.poll();
                if(aux.getValue().getOfertaAsignadas() == null && aux.getPuntaje() >= 70){
                    aux.getValue().setOfertaAsignadas(o);
                    o.getCandidatosAsignados().add(aux.getValue());
                    CandidatoCliente candi = this.candidatoClientes.get(aux.getValue().getDocumento());
                    candi.actualizarCandidato(aux.getValue().getDocumento(), o.getIdentificador(), o);
                    ofc.notificarOferta(o.getIdentificador(), aux.getValue(), aux.getValue().getDocumento());
                    i++;
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
