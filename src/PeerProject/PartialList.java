/**
 * P2World @ Sistemas Distribuidos, ESTGF 2013/14
 *
 * PEER TO PEER FILE TRANSMISSION APPLICATION
 *
 * Developed by
 *
 * 8090228, Luís Sousa and 8090242, Ricardo Barbosa
 */

package PeerProject;

import java.util.ArrayList;
import java.util.List;

public class PartialList {
    
    private String address;
    private int port;
    private List<String> files;

    public PartialList(String address, int port) {
        this.setAddress(address);
        this.setPort(port);
        this.files = new ArrayList<String>();
    }

    public List<String> getFiles() {
        return files;
    }

    
    
    
    public void AddFile(String fileName){
        files.add(fileName);        
    }
    
    
    // Get & Set Methods
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public boolean exist(String fileName){
        boolean found = false;
        
        if(files.contains(fileName)){
            found = true;
        }
        
        return found;
    }
    
   
    
    
}
