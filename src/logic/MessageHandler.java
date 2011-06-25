/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import structs.Join;
import structs.Message;
import structs.NetAddress;
import structs.Node;
import structs.Token;

/**
 *
 * @author 3070130-3070175
 */
public class MessageHandler extends ThreadInterface{
    private Object mesg;
    private String addr;
    private int port;
    private Socket sendsocket=new Socket();
    private Node node;
    
    public MessageHandler(Object msg,String addr, int port, Node node) throws IOException{
        this.mesg=msg;
        this.addr=addr;
        this.port=port;
        this.node=node;
    }
    
    @Override
    synchronized public void run(){
        try {
            if(mesg instanceof Join){
                System.out.println("Join received");
                try{
                    Socket socket = new Socket("localhost",7060);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    byte buffer = (byte)100;
                    out.writeByte(buffer);
                    out.flush();
                    node.addNodeToList(new NetAddress(this.addr,this.port));
                    System.out.println("My next's address"+new NetAddress(this.addr,this.port));
                    if(node.getNodeList().size()==2){
                        System.out.println("new token!");
                        Socket send=new Socket(this.addr,6071);
                        ObjectOutputStream obj = new ObjectOutputStream(send.getOutputStream());
                        obj.writeObject(new Token(node.getNodeList()));
                        obj.close();
                        send.close();
                    }
                    out.close();
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                }
                node.addNodeToList(new NetAddress(this.addr,this.port));
            }
            else if(mesg instanceof Message){
                System.out.println("Message received");
                Message msg = (Message)mesg;
                if(InetAddress.getLocalHost().getHostAddress().equals(msg.get_sender())){
                    NetAddress next=node.getNextNode();
                    sendsocket=new Socket(next.get_IP(),next.get_port());
                    OutputStream temp=sendsocket.getOutputStream();
                    ObjectOutputStream out=new ObjectOutputStream(temp);
                    out.writeObject(new Token(node.getNodeList()));
                    out.close();
                    temp.close();
                    sendsocket.close();
                }
                else{
                    System.out.println(msg.get_message());
                    NetAddress next=node.getNextNode();
                    sendsocket=new Socket(next.get_IP(),next.get_port());
                    OutputStream temp=sendsocket.getOutputStream();
                    ObjectOutputStream out=new ObjectOutputStream(temp);
                    out.writeObject(mesg);
                    out.close();
                    temp.close();
                    sendsocket.close();
                }
            }
            else if(mesg instanceof Token){
                System.out.println("Token received");
                Token msg = (Token) mesg;
                ArrayList<String> textlist = new ArrayList<String>(node.getMsg());
                String text="";
                for(int i=0;i<textlist.size();i++){
                    text+=textlist.get(i);
                }
                if(!text.isEmpty()){
                    Message newmesg = new Message((short)(msg.get_seq_number()+1),(byte)text.length(),text);
                    node.set_node_list(msg.get_node_list());
                    NetAddress next=node.getNextNode();
                    sendsocket=new Socket(next.get_IP(),next.get_port());
                    OutputStream temp=sendsocket.getOutputStream();
                    ObjectOutputStream out=new ObjectOutputStream(temp);
                    out.writeObject(newmesg);
                    out.close();
                    temp.close();
                    sendsocket.close();
                }
                else{
                    NetAddress next=node.getNextNode();
                    sendsocket=new Socket(next.get_IP(),next.get_port());
                    OutputStream temp=sendsocket.getOutputStream();
                    ObjectOutputStream out=new ObjectOutputStream(temp);
                    out.writeObject(msg);
                    out.close();
                    temp.close();
                    sendsocket.close();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
    
    }
    
}
