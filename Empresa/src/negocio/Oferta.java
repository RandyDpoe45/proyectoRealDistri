/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author randy
 */
public class Oferta implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cargo;
    private int nivelEstudios;
    private int experienciaRequerida;
    private float salarioOfrecido; 
    private SectorEmpresa sectorEmpresa ;
    private List<Candidato> candidatos;

    public Oferta(String cargo, int nivel_estudios, int experiencia, float salario, SectorEmpresa sector) {
        this.cargo=cargo;
        this.experienciaRequerida=experiencia;
        this.nivelEstudios=nivel_estudios;
        this.salarioOfrecido=salario;
        this.sectorEmpresa=sector;
    }
    
    public List<Candidato> getCandidatos(){
        return candidatos;
    }

    public void setCandidatos(List<Candidato> candidatos) {
        this.candidatos = candidatos;
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
    
    
}
