/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structs;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.KeyStrokeListener;
import logic.ObjectListener;

/**
 *
 * @author 3070130-3070175
 */
public class Node implements Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<NetAddress> nodelist=new ArrayList<NetAddress>();
    private ArrayList<NetAddress> nodelisttobe=new ArrayList<NetAddress>();
    private NetAddress address;
    private String text;
    private ObjectListener listener;
    private KeyStrokeListener keylistener;
    public Node(){
        try {
            this.address=new NetAddress(InetAddress.getLocalHost().getHostAddress(),6070);
            this.nodelist.add(this.address);
            //System.out.println("My address: "+this.address);
            this.keylistener = new KeyStrokeListener();
            this.listener = new ObjectListener(6070,this,this.keylistener);
            listener.start();
            keylistener.run();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Node(String a_nodes_addr, int a_nodes_port){
        try {
            ServerSocket ss = null;
            int j;
            for (j = 6070; j < 6080; j++) {
                try {
                    ss = new ServerSocket(j);
                    System.out.println("Found free port: "+j);
                    break;
                } catch (IOException ex) {
                }
            }
            ss.close();
            Join join = new Join(new NetAddress(InetAddress.getLocalHost().getHostAddress(),j));
            
            Socket socket = new Socket(a_nodes_addr, a_nodes_port);
            ObjectOutputStream ooutputstream = new ObjectOutputStream(socket.getOutputStream());
            ooutputstream.writeObject(join);
            ooutputstream.close();
            ServerSocket waitforack = new ServerSocket(7060);
            socket = waitforack.accept();
            DataInputStream inp=new DataInputStream(socket.getInputStream());
            byte ack=inp.readByte();
            if(ack!=100){//predecided integer for ack
                throw new IOException();
            }
            this.address=new NetAddress(InetAddress.getLocalHost().getHostAddress(),j);
            /*this.nodelist.add(address);
            System.out.println("My address: "+address);
            this.nodelist.add(new NetAddress(a_nodes_addr,a_nodes_port));
            System.out.println("My next's address: "+new NetAddress(a_nodes_addr,a_nodes_port));
            */inp.close();
            waitforack.close();
            this.keylistener = new KeyStrokeListener();
            this.listener = new ObjectListener(j,this,this.keylistener);
            listener.start();
            keylistener.run();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Node(String a_nodes_addr){
        try {
            ServerSocket ss = null;
            int j;
            for (j = 6070; j < 6080; j++) {
                try {
                    ss = new ServerSocket(j);
                    System.out.println("Found free port: "+j);
                    break;
                } catch (IOException ex) {
                }
            }
            if(j==6080){
                throw new IOException("Max number of nodes connected");
            }
            ss.close();
            Join join = new Join(new NetAddress(InetAddress.getLocalHost().getHostAddress(),j));
            Socket socket;
            ObjectOutputStream ooutputstream;
            int i;
            for (i = 6070; i < 6080; i++) {
                try {
                    socket = new Socket(a_nodes_addr, i);
                    ooutputstream = new ObjectOutputStream(socket.getOutputStream());
                    ooutputstream.writeObject(join);
                    ooutputstream.close();
                    break;
                } catch (UnknownHostException ex) {
                    Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ServerSocket waitforack = new ServerSocket(7060);
            socket = waitforack.accept();
            DataInputStream inp=new DataInputStream(socket.getInputStream());
            byte ack=inp.readByte();
            if(ack!=100){//predecided integer for ack
                throw new IOException();
            }
            this.address=new NetAddress(InetAddress.getLocalHost().getHostAddress(),j);
            /*this.nodelist.add(address);*/
            //System.out.println("My address: "+address);
            //this.nodelist.add(new NetAddress(a_nodes_addr,i));
            //System.out.println("My next's address: "+new NetAddress(a_nodes_addr,i));
            inp.close();
            waitforack.close();
            this.keylistener = new KeyStrokeListener();
            this.listener = new ObjectListener(j,this,this.keylistener);
            listener.start();
            keylistener.run();
        } catch (IOException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void addWaitingNodesToList(){
        if(!this.nodelisttobe.isEmpty()){
        this.nodelist.addAll(this.nodelisttobe);
        this.nodelisttobe.clear();
        }
    }
    
    public NetAddress getNextNode(){
        int i=0;
        String temp;
        for(;i<this.nodelist.size();i++){
            temp=""+this.nodelist.get(i).get_IP()+":"+this.nodelist.get(i).get_port();
            if(temp.equals(this.address.get_IP()+":"+this.address.get_port())){
                break;
            }
        }
        
        if(i+1==this.nodelist.size()){
            return this.nodelist.get(0);
        }
        else if(i!=this.nodelist.size()){
            return this.nodelist.get(i+1);
        }
        else{
            throw new NullPointerException(this.address+" isn't in the list: "+this.getNodeList());
        }
    }
    
    public void set_node_list(List<NetAddress> new_node_list){
        this.nodelist.clear();
        this.nodelist.addAll(new_node_list);
    }
    
    public ArrayList<NetAddress> getNodeList(){
        return this.nodelist;
    }
    
    public String getMsg(){
        return this.keylistener.getMsglist();
    }

    public int get_port() {
        return this.address.get_port();
    }

    public NetAddress getAddress() {
        return this.address;
    }

    public void NodeToBeAddedToList(NetAddress netAddress) {
        this.nodelisttobe.add(netAddress);
    }
    
    public boolean isFirst(){
        return false;
    }

    public void removeNode(NetAddress next) {
        this.nodelist.remove(next);
    }
    
    /**
     * Starts the execution of the thread.
     */
   /* public void start()
    {
        if (runner == null)
        {
            runner = new Thread(this);
            runner.setDaemon(true);
            runner.start();
        }
    }*/

    /**
     * Stops the execution of the thread.
     */
   /* public void stop()
    {
        try 
        {
            socket.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        } 
        runner.interrupt();
        runner = null;
    }
    */
   /* public Thread getThread()
    {
        return this.runner;
    }
    */
}
