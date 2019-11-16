/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import negocio.Oferta;

/**
 *
 * @author randy
 */
public class ImplementacionOferta implements OperacionesOferta{

    @Override
    public Oferta imprimirOferta(Oferta o) {
        System.out.println(o.toString());
        return o;
    }
    
}
