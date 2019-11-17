/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.io.Serializable;

/**
 *
 * @author randy
 */
public class ExperienciaLaboral implements Serializable{
    private static final long serialVersionUID = 1L;
    private String cargo;
    private int duracion;
    private SectorEmpresa sectorEmpresa;

    public ExperienciaLaboral(String cargo, int duracion, SectorEmpresa sector) {
        this.cargo=cargo;
        this.duracion=duracion;
        this.sectorEmpresa=sector;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public SectorEmpresa getSectorEmpresa() {
        return sectorEmpresa;
    }

    public void setSectorEmpresa(SectorEmpresa sectorEmpresa) {
        this.sectorEmpresa = sectorEmpresa;
    }
    
    
    
}
