/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

/**
 *
 * @author randy
 */
public class Entry<M> implements Comparable<Entry> {
    private Integer puntaje;
    private M value;

    public Entry(int puntaje, M value) {
        this.puntaje = puntaje;
        this.value = value;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public M getValue() {
        return value;
    }

    // getters
    public void setValue(M value) {
        this.value = value;
    }

    @Override
    public int compareTo(Entry other) {
        return this.getPuntaje().compareTo(other.getPuntaje());
    }
}
