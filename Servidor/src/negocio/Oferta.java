/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author randy
 */
public class Oferta implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static Long id;
    private Long identificador;
    private String cargo;
    private int nivelEstudios;
    private int experienciaRequerida;
    private float salarioOfrecido; 
    private SectorEmpresa sectorEmpresa ;
    private List<Candidato> candidatosAsignados;

    public Oferta(String cargo, int nivelEstudios, int experienciaRequerida, float salarioOfrecido, SectorEmpresa sectorEmpresa) {
        this.candidatosAsignados=new ArrayList<>();
        this.cargo = cargo;
        this.nivelEstudios = nivelEstudios;
        this.experienciaRequerida = experienciaRequerida;
        this.salarioOfrecido = salarioOfrecido;
        this.sectorEmpresa = sectorEmpresa;
        this.identificador = id;
        id++;
    }

    public Long getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Long identificador) {
        this.identificador = identificador;
    }

    
    public List<Candidato> getCandidatosAsignados() {
        return candidatosAsignados;
    }

    public void setCandidatosAsignados(List<Candidato> candidatosAsignados) {
        this.candidatosAsignados = candidatosAsignados;
    }
    
    
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getNivelEstudios() {
        return nivelEstudios;
    }

    public void setNivelEstudios(int nivelEstudios) {
        this.nivelEstudios = nivelEstudios;
    }

    public int getExperienciaRequerida() {
        return experienciaRequerida;
    }

    public void setExperienciaRequerida(int experienciaRequerida) {
        this.experienciaRequerida = experienciaRequerida;
    }

    public float getSalarioOfrecido() {
        return salarioOfrecido;
    }

    public void setSalarioOfrecido(float salarioOfrecido) {
        this.salarioOfrecido = salarioOfrecido;
    }

    public SectorEmpresa getSectorEmpresa() {
        return sectorEmpresa;
    }

    public void setSectorEmpresa(SectorEmpresa sectorEmpresa) {
        this.sectorEmpresa = sectorEmpresa;
    }
    
    public static int evaluarCandidato(Oferta o, Candidato c){
        int puntaje=0;
        if(c.getNivelEstudios()>=o.getNivelEstudios()&&
        c.getAspiracionLaboral()<=o.getSalarioOfrecido()){
            for(ExperienciaLaboral e:c.getExperiencia()){
                if(e.getCargo().equals(o.getCargo())&&
                e.getDuracion()>=o.getExperienciaRequerida()){
                    int base=60;
                    int bono=0;
                    bono+=(c.getAspiracionLaboral()<o.getSalarioOfrecido())?1:0;
                    bono+=(e.getDuracion()>o.getExperienciaRequerida())?1:0;
                    bono+=(e.getSectorEmpresa().equals(o.getSectorEmpresa()))?2:0;
                    puntaje=(puntaje>base+bono*10)?puntaje:base+bono*10;
                }
            }
        }
        return puntaje;
    }



    public boolean addCandidato(Candidato can) {
        if(this.candidatosAsignados.size()<3){
            this.candidatosAsignados.add(can);
            return true;
        }
        return false;
    }
    
    
    
}
