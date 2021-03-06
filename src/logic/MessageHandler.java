/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import structs.FirstNode;
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
    private Socket sendsocket=new Socket();
    private Node node;
    private KeyStrokeListener keylisten;
    
    public MessageHandler(Object msg, Node node, KeyStrokeListener keylisten) throws IOException{
        this.mesg=msg;
        this.node=node;
        this.keylisten=keylisten;
    }
    
    @Override
    synchronized public void run(){
        try {
            if(mesg instanceof Join){
                Join msg=(Join) mesg;
                int senderport=msg.getSender_addr().get_port();
                node.NodeToBeAddedToList(new NetAddress(msg.getSender_addr().get_IP(),senderport));
                System.out.println("* Join received from "+msg.getSender_addr()+":"+senderport);
                try{
                    Socket socket = new Socket("localhost",7060);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    byte buffer = (byte)100;
                    out.writeByte(buffer);
                    out.flush();
                    System.out.println("* My next's address"+new NetAddress(msg.getSender_addr().get_IP(),senderport));
                    if(node.getNodeList().size()==1){
                        System.out.println("* New token!");
                        node.addWaitingNodesToList();
                        System.out.println("* Replies with an ACK");
                        Socket send=new Socket(msg.getSender_addr().get_IP(),senderport);
                        NetAddress next=new NetAddress(msg.getSender_addr().get_IP(),senderport);
                        sendsocket=null;
                        int i;
                        while(true){
                            i=0;
                            for(;i<3;i++){
                                try {
                                    sendsocket=new Socket(msg.getSender_addr().get_IP(),senderport);
                                    break;
                                } catch (IOException asd) {
                                    if(i==3){
                                        node.removeNode(next);
                                        next=node.getNextNode();
                                    }
                                }
                            }
                            if(sendsocket!=null) {
                                break;
                            }
                        }
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
            }
            else if(mesg instanceof Message){
                Message msg = (Message)mesg;
                System.out.println("* Message received with sequence number: "+msg.get_seq_number()+" from "+msg.get_senders_IP());
                //System.out.println("Message");
                //System.out.println(this.node.get_port()+" sent this message to "+msg.get_senders_port());
                if(this.node.get_port()==msg.get_senders_port()){
                    if(node.isFirst()){
                        FirstNode n=(FirstNode)node;
                        Message last = null;
                        try {
                            last = n.getLastMessageSeen();
                            if(last.get_seq_number()!=msg.get_seq_number()){
                                NetAddress next=node.getNextNode();
                                sendsocket=null;
                                int i;
                                while(true){
                                    i=0;
                                    for(;i<3;i++){
                                        try {
                                            sendsocket=new Socket(next.get_IP(),next.get_port());
                                            break;
                                        } catch (IOException asd) {
                                            if(i==3){
                                                node.removeNode(next);
                                                next=node.getNextNode();
                                            }
                                        }
                                    }
                                    if(sendsocket!=null) {
                                        break;
                                    }
                                }
                                OutputStream temp=sendsocket.getOutputStream();
                                ObjectOutputStream out=new ObjectOutputStream(temp);
                                System.out.println("* Create and send token with sequence number: "
                                        +(msg.get_seq_number()+1)+" to "+next.get_IP()+":"+next.get_port());
                                out.writeObject(new Token(node.getNodeList(),(short)(msg.get_seq_number()+1)));
                                out.close();
                                temp.close();
                                sendsocket.close();
                                n.setLastMessageSeen(msg);
                            }
                        } catch (NullPointerException e) {
                            NetAddress next=node.getNextNode();
                            sendsocket=null;
                            int i;
                            while(true){
                                i=0;
                                for(;i<3;i++){
                                    try {
                                        sendsocket=new Socket(next.get_IP(),next.get_port());
                                        break;
                                    } catch (IOException asd) {
                                        if(i==3){
                                            node.removeNode(next);
                                            next=node.getNextNode();
                                        }
                                    }
                                }
                                if(sendsocket!=null) {
                                    break;
                                }
                            }
                            OutputStream temp=sendsocket.getOutputStream();
                            ObjectOutputStream out=new ObjectOutputStream(temp);
                            System.out.println("* Create and send token with sequence number: "+
                                    (msg.get_seq_number()+1)+" to "+next.get_IP()+":"+next.get_port());
                            out.writeObject(new Token(node.getNodeList(),(short)(msg.get_seq_number()+1)));
                            out.close();
                            temp.close();
                            sendsocket.close();
                            n.setLastMessageSeen(msg);
                        }
                    }
                    else{
                        NetAddress next=node.getNextNode();
                        sendsocket=null;
                        int i;
                        while(true){
                            i=0;
                            for(;i<3;i++){
                                try {
                                    sendsocket=new Socket(next.get_IP(),next.get_port());
                                    break;
                                } catch (IOException asd) {
                                    if(i==3){
                                        node.removeNode(next);
                                        next=node.getNextNode();
                                    }
                                }
                            }
                            if(sendsocket!=null) {
                                break;
                            }
                        }
                        OutputStream temp=sendsocket.getOutputStream();
                        ObjectOutputStream out=new ObjectOutputStream(temp);
                        out.writeObject(new Token(node.getNodeList(),(short)(msg.get_seq_number()+1)));
                        out.close();
                        temp.close();
                        sendsocket.close();
                    }
                }
                else{
                    System.out.println(">>"+msg.get_message());
                    System.out.println("* Message forwarding - sequence number: "+(msg.get_seq_number()));
                    NetAddress next=node.getNextNode();
                    sendsocket=null;
                    int i;
                    while(true){
                        i=0;
                        for(;i<3;i++){
                            try {
                                sendsocket=new Socket(next.get_IP(),next.get_port());
                                break;
                            } catch (IOException asd) {
                                if(i==3){
                                    node.removeNode(next);
                                    next=node.getNextNode();
                                }
                            }
                        }
                        if(sendsocket!=null) {
                            break;
                        }
                    }
                    OutputStream temp=sendsocket.getOutputStream();
                    ObjectOutputStream out=new ObjectOutputStream(temp);
                    out.writeObject(msg);
                    out.close();
                    temp.close();
                    sendsocket.close();
                }
            }
            else if(mesg instanceof Token){
                Token msg = (Token) mesg;
                //System.out.println("* Token received - last sequense number: "+msg.get_seq_number());
                node.set_node_list(msg.get_node_list());
                node.addWaitingNodesToList();
                boolean bool=true;
                /*if(node.isFirst()){
                    FirstNode n=(FirstNode)node;
                    try{
                        bool=n.getLastTokenSeen().hashCode()==(msg.hashCode());
                    } catch(NullPointerException e){
                        bool=true;
                    }*/
                    if(bool){
                        String text=this.keylisten.getMsglist();
                        if(!text.isEmpty()){
                            //System.out.println("* I'm about to send this message: "+text+" with length: "
                            //        + ""+(byte)text.length()+" and sequense number: "+(msg.get_seq_number()+1));
                            Message newmesg = new Message(node.getAddress(),(short)(msg.get_seq_number()+1),text);
                            //node.set_node_list(msg.get_node_list());
                            NetAddress next=node.getNextNode();
                            sendsocket=null;
                            int i;
                            while(true){
                                i=0;
                                for(;i<3;i++){
                                    try {
                                        sendsocket=new Socket(next.get_IP(),next.get_port());
                                        break;
                                    } catch (IOException asd) {
                                        System.out.println("Someone failed 3!");
                                        if(i==3){
                                            node.removeNode(next);
                                            next=node.getNextNode();
                                        }
                                    }
                                }
                                if(sendsocket!=null) {
                                    break;
                                }
                            }
                            OutputStream temp=sendsocket.getOutputStream();
                            ObjectOutputStream out=new ObjectOutputStream(temp);
                            out.writeObject(newmesg);
                            out.close();
                            temp.close();
                            sendsocket.close();
                        }
                        else{
                            NetAddress next=node.getNextNode();
                            sendsocket=null;
                            int i;
                            while(true){
                                i=0;
                                for(;i<3;i++){
                                    try {
                                        sendsocket=new Socket(next.get_IP(),next.get_port());
                                        break;
                                    } catch (IOException asd) {
                                        System.err.println("Someone failed 3 times!");
                                        if(i==3){
                                            node.removeNode(next);
                                            next=node.getNextNode();
                                        }
                                    }
                                }
                                if(sendsocket!=null) {
                                    break;
                                }
                            }
                            OutputStream temp=sendsocket.getOutputStream();
                            ObjectOutputStream out=new ObjectOutputStream(temp);
                            //System.out.println("* I'm about to send a token"+
                            //        " - sequense number: "+(msg.get_seq_number()));
                            out.writeObject(new Token(node.getNodeList(), msg.get_seq_number()));
                            out.close();
                            temp.close();
                            sendsocket.close();
                        }
                        //n.setLastTokenSeen(msg);
                    //}
                }
                else{
                    String text=this.keylisten.getMsglist();
                    if(!text.isEmpty()){
                        System.out.println("I'm about to send this message: "+text+" with length: "+(byte)text.length());
                        Message newmesg = new Message(node.getAddress(),(short)(msg.get_seq_number()+1),text);
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
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
    
    }
    
}
