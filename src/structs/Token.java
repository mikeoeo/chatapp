package structs;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author 3070130-3070175
 */
public class Token extends AbstractMessage{
    //variables
    private short seq_number; //message sequence number
    private NetAddress[] node_list= new NetAddress[10]; //IP and port list of the nodes
    
    //empty constructor
    public Token(){
        this.seq_number=0;
        for(int i=0;i<10;i++){
                this.node_list[i]=null;
        }
    }
    //constructor with previous data
    public Token(NetAddress[] nodes){
        this.seq_number=0;
        System.arraycopy(nodes, 0, this.node_list, 0, nodes.length);
        for(int i=nodes.length;i<10;i++){
            this.node_list[i]=null;
        }
    }
    
    //methods
    //returns the message sequence number
    public short get_seq_number(){
        return this.seq_number;
    }
    //returns the node list array
    public NetAddress[] get_node_list(){
        return this.node_list;
    }
    //returns a line from the node list array
    public NetAddress get_node(int line){
        return this.node_list[line];
    }
    //sets the message sequence number
    public void set_seq_number(short number){
        this.seq_number=number;
    }
    //sets the node list
    public void set_node_list(NetAddress[] new_node_list){
        this.node_list=new_node_list;
    }
    //adds one node in the node_list
    public void add_node(NetAddress addr){
        int i = 0;
        try{
            for(i=0;i<10;i++){
                this.get_node(i);
            }
        }
        catch(NullPointerException e){
            this.node_list[i]=addr;
        }
    }
    //sorts the node list based in IP
    public void node_list_sort_by_IP(){
        Arrays.sort(this.node_list, new Comparator<NetAddress>() {
            @Override
            public int compare(NetAddress entry1, NetAddress entry2) {
                return entry1.toString().compareTo(entry2.toString());
            }
        });
    }
}
