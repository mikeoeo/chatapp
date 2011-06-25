/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structs;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author 3070130-3070175
 */
public class Message extends AbstractMessage{
    private static final long serialVersionUID = 1L;
    //variables
    private NetAddress sender;
    private short seq_number;
    private byte message_length;
    String message;
    
    //constructors
    public Message(NetAddress addr,short numb,byte length,String mess){
        this.sender=addr;
        this.seq_number=numb;
        this.message_length=length;
        this.message=mess;
    }
    //sender=localhost
    public Message(short numb,byte length,String mess) throws UnknownHostException{
        this.sender=new NetAddress(6070);
        this.seq_number=numb;
        this.message_length=length;
        this.message=mess;
    }
    public Message(NetAddress addr,short numb,String mess){
        this.sender=addr;
        this.seq_number=numb;
        this.message_length=(byte) mess.length();
        this.message=mess;
    }
    public Message(String IP,int port,short numb,byte length,String mess){
        this.sender=new NetAddress(IP,port);
        this.seq_number=numb;
        this.message_length=length;
        this.message=mess;
    }
    public Message(String IP,int port,short numb,String mess){
        this.sender=new NetAddress(IP,port);
        this.seq_number=numb;
        this.message_length=(byte) mess.length();
        this.message=mess;
    }
    
    //get methods
    public String get_sender(){
        return this.sender.get_IP();
    }
    public short get_seq_number(){
        return this.seq_number;
    }
    public byte get_message_length(){
        return this.message_length;
    }
    public String get_message(){
        return this.message;
    }
    //set methods
    public void set_sender(NetAddress addr){
        this.sender=addr;
    }
    public void set_sender(String addr,int port){
        this.sender=new NetAddress(addr,port);
    }
    
    
    //increases the value of seq_number by 1
    public void seq_plus_1(){
        this.seq_number++;
    }
}
