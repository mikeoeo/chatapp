/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structs;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author 3070130-3070175
 */
public class NetAddress implements Serializable{
    private static final long serialVersionUID = 1L;
    private String IP;
    private int port;
    
    //constructors
    public NetAddress(String i,int p){
        this.IP=i;
        this.port=p;
    }
    
    public NetAddress(int p){
        try {
            this.IP=InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetAddress.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.port=p;
    }
    //get methods
    public String get_IP(){
        return this.IP;
    }
    public int get_port(){
        return this.port;
    }
    //set methods
    public void set_IP(String addr){
        this.IP=addr;
    }
    public void set_port(int p){
        this.port=p;
    }
    @Override
    public String toString(){
        return this.IP.toString()+":"+this.port;
    }
}
