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
public class DataEntry<M> {
    
    private String hostName;
    private M data;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public M getData() {
        return data;
    }

    public void setData(M data) {
        this.data = data;
    }

    public DataEntry(String hostName, M data) {
        this.hostName = hostName;
        this.data = data;
    }
    
    
}
