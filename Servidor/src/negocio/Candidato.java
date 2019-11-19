/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import negocio.ExperienciaLaboral;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author randy
 */
public class Candidato implements Serializable{
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String documento;
    private int nivelEstudios;
    private float aspiracionLaboral;
    private List<ExperienciaLaboral> experiencia;
    private Long idOferta;
    private List<Oferta> ofertaAsignadas ;

    public Candidato(String nombre, String documento, int nivelEstudios, float aspiracionLaboral) {
        this.nombre = nombre;
        this.documento = documento;
        this.nivelEstudios = nivelEstudios;
        this.aspiracionLaboral = aspiracionLaboral;
        this.experiencia = new ArrayList<>();
        this.ofertaAsignadas=new ArrayList<>();
    }

    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public int getNivelEstudios() {
        return nivelEstudios;
    }

    public void setNivelEstudios(int nivelEstudios) {
        this.nivelEstudios = nivelEstudios;
    }

    public float getAspiracionLaboral() {
        return aspiracionLaboral;
    }

    public void setAspiracionLaboral(float aspiracionLaboral) {
        this.aspiracionLaboral = aspiracionLaboral;
    }

    public List<ExperienciaLaboral> getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(List<ExperienciaLaboral> experiencia) {
        this.experiencia = experiencia;
    }

    public List<Oferta> getOfertaAsignadas() {
        return ofertaAsignadas;
    }

    public void setOfertaAsignadas(Oferta ofertaAsignada) {
        this.idOferta=ofertaAsignada.getIdentificador();
        if(this.ofertaAsignadas==null){
            this.ofertaAsignadas=new ArrayList<>();
        }
        this.ofertaAsignadas.add(ofertaAsignada);
    }

    public Long getIdOferta() {
        return idOferta;
    }

    public void setIdOferta(Long idOferta) {
        this.idOferta = idOferta;
    }

    
    @Override
    public String toString() {
        return "Candidato{" + "nombre=" + nombre + ", documento=" + documento + ", nivelEstudios=" + nivelEstudios + ", aspiracionLaboral=" + aspiracionLaboral + ", experiencia=" + experiencia + '}';
    }
    
    
    
}
