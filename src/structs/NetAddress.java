/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structs;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author 3070130-3070175
 */
public class NetAddress{
    private InetAddress IP;
    private int port;
    
    //constructors
    public NetAddress(InetAddress i,int p){
        this.IP=i;
        this.port=p;
    }
    public NetAddress(String hostname,int p){
        try {
            this.IP=InetAddress.getByName(hostname);
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetAddress.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.port=p;
    }
    public NetAddress(int p){
        try {
            this.IP=InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetAddress.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.port=p;
    }
    //get methods
    public InetAddress get_IP(){
        return this.IP;
    }
    public int get_port(){
        return this.port;
    }
    //set methods
    public void set_IP(InetAddress addr){
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
