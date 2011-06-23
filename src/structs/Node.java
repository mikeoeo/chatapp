/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structs;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 3070130-3070175
 */
public class Node implements Runnable,Serializable{
    private static final long serialVersionUID = 1L;
    private List<NetAddress> nodelist;
    private ServerSocket serversocket;
    private Socket socket,ack=new Socket(),sendsocket;
    private Thread runner;
    private AbstractMessage mesg;
    private NetAddress address;
    private String text;
    
    @Override
    public void run() {
        try {
            serversocket = new ServerSocket(address.get_port());
            socket = serversocket.accept();
            InputStream inp = socket.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inp);
            mesg = (AbstractMessage) in.readObject();
            if(mesg instanceof Join){
                try{
                ack.connect(socket.getRemoteSocketAddress());
                ack.sendUrgentData(256);//ack
                } catch (IOException ex) {
                    Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.addNodeToList(new NetAddress(socket.getInetAddress(),socket.getPort()));
            }
            else if(mesg instanceof Message){
                Message msg = (Message)mesg;
                if(socket.getLocalSocketAddress().equals(socket.getRemoteSocketAddress())){
                    NetAddress next=this.getNextNode();
                    sendsocket=new Socket(next.get_IP(),next.get_port());
                    OutputStream temp=sendsocket.getOutputStream();
                    ObjectOutputStream out=new ObjectOutputStream(temp);
                    out.writeObject(new Token(this.nodelist));
                }
                else{
                    System.out.println(msg.get_message());
                    NetAddress next=this.getNextNode();
                    sendsocket=new Socket(next.get_IP(),next.get_port());
                    OutputStream temp=sendsocket.getOutputStream();
                    ObjectOutputStream out=new ObjectOutputStream(temp);
                    out.writeObject(mesg);
                }
            }
            else if(mesg instanceof Token){
                Token msg = (Token) mesg;
                if(!this.text.isEmpty()){
                    Message newmesg = new Message((short)(msg.get_seq_number()+1),(byte)this.text.length(),this.text);
                    this.set_node_list(msg.get_node_list());
                    NetAddress next=this.getNextNode();
                    sendsocket=new Socket(next.get_IP(),next.get_port());
                    OutputStream temp=sendsocket.getOutputStream();
                    ObjectOutputStream out=new ObjectOutputStream(temp);
                    out.writeObject(newmesg);
                }
                else{
                    NetAddress next=this.getNextNode();
                    sendsocket=new Socket(next.get_IP(),next.get_port());
                    OutputStream temp=sendsocket.getOutputStream();
                    ObjectOutputStream out=new ObjectOutputStream(temp);
                    out.writeObject(msg);
                }
            }
            
            in.close();
            socket.close();
            serversocket.close();
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        
        } catch (IOException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
    }
    
    public Node(NetAddress addr){
        this.address=addr;
    }
    
    public void addNodeToList(NetAddress n){
        this.nodelist.add(n);
    }
    
    public NetAddress getNextNode(){
        return this.nodelist.get(this.nodelist.indexOf(this.address)+1);
    }
    
    public void set_node_list(List<NetAddress> new_node_list){
        this.nodelist.clear();
        this.nodelist.addAll(new_node_list);
    }
    
    /**
     * Starts the execution of the thread.
     */
    public void start()
    {
        if (runner == null)
        {
            runner = new Thread(this);
            runner.setDaemon(true);
            runner.start();
        }
    }

    /**
     * Stops the execution of the thread.
     */
    public void stop()
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
    
    public Thread getThread()
    {
        return this.runner;
    }
    
}
