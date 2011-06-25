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
    private NetAddress address;
    private String text;
    private ObjectListener listener;
    private KeyStrokeListener keylistener;
   /* private ServerSocket serversocket;
    private Socket socket,ack=new Socket(),sendsocket;
    private Thread runner;
    private AbstractMessage mesg;
    */
    /*@Override
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
    */
    public Node(){
        try {
            this.address=new NetAddress(InetAddress.getLocalHost().getHostAddress(),6070);
            this.nodelist.add(address);
            System.out.println("My address"+this.address);
            this.listener = new ObjectListener(6070,this);
            listener.start();
            this.keylistener = new KeyStrokeListener();
            keylistener.run();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Node(int port){
        this.address=new NetAddress(6070);
    }
    
    public Node(String a_nodes_addr, int a_nodes_port){
        try {
            Join join = new Join();
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
            this.address=new NetAddress(InetAddress.getLocalHost().getHostAddress(),6071);
            this.nodelist.add(address);
            System.out.println("My address"+address);
            this.nodelist.add(new NetAddress(a_nodes_addr,a_nodes_port));
            System.out.println("My next's address"+new NetAddress(a_nodes_addr,a_nodes_port));
            inp.close();
            waitforack.close();
            this.listener = new ObjectListener(6071,this);
            listener.start();
            this.keylistener = new KeyStrokeListener();
            keylistener.run();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Node(String a_nodes_addr){
        Join join = new Join();
        Socket socket;
        ObjectOutputStream ooutputstream;
        for (int i = 6070; i < 6080; i++) {
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
    }
    
    public Node(NetAddress addr){
        this.address=addr;
    }
    
    public void addNodeToList(NetAddress n){
        this.nodelist.add(n);
    }
    
    public NetAddress getNextNode(){
        if(this.nodelist.indexOf(this.address)+1<this.nodelist.size()){
            return this.nodelist.get(this.nodelist.indexOf(this.address)+1);
        }
        else if(this.nodelist.indexOf(this.address)+1==this.nodelist.size()){
            return this.nodelist.get(0);
        }
        else{
            throw new IndexOutOfBoundsException();
        }
    }
    
    public void set_node_list(List<NetAddress> new_node_list){
        this.nodelist.clear();
        this.nodelist.addAll(new_node_list);
    }
    
    public ArrayList<NetAddress> getNodeList(){
        return this.nodelist;
    }
    
    public ArrayList<String> getMsg(){
        return this.keylistener.getMsglist();
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
