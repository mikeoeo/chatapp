package structs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author 3070130-3070175
 */
public class Token extends AbstractMessage{
    private static final long serialVersionUID = 1L;
    //variables
    private short seq_number; //message sequence number
    private List<NetAddress> node_list=new ArrayList<NetAddress>(); //IP and port list of the nodes
    
    //empty constructor
    public Token(){
        this.seq_number=0;
        this.node_list.clear();
    }
    //constructor with previous data
    public Token(List<NetAddress> nodes){
        this.seq_number=0;
        this.node_list.clear();
        this.node_list.addAll(nodes);
    }
    
    //methods
    //returns the message sequence number
    public short get_seq_number(){
        return this.seq_number;
    }
    //returns the node list array
    public List<NetAddress> get_node_list(){
        return this.node_list;
    }
    //returns a line from the node list array
    public NetAddress get_node(int index){
        return this.node_list.get(index);
    }
    //sets the message sequence number
    public void set_seq_number(short number){
        this.seq_number=number;
    }
    //sets the node list
    public void set_node_list(List<NetAddress> new_node_list){
        this.node_list.clear();
        this.node_list.addAll(new_node_list);
    }
    //adds one node in the node_list
    public void add_node(NetAddress addr){
        this.node_list.add(addr);
    }
    //sorts the node list based in IP
    public void node_list_sort_by_IP(){
        List<String> tempList = new ArrayList<String>();
        if(!this.node_list.isEmpty()){
            int k=this.node_list.size();
            for(int i=0;i<k;i++){
                tempList.add(this.node_list.get(i).get_IP());
            }
            Collections.sort(tempList);
            for(int i=0;i<k;i++){
                this.node_list.add(this.get_node(i));
            }
            for(int i=0;i<k;i++){
                for(int j=0;j<k;j++){
                    if(this.node_list.get(k+i-1).get_IP().compareTo(tempList.get(j))==0){
                        this.node_list.set(j,this.node_list.get(k+i-1));
                    }
                }
            }
            int l=this.node_list.size();
            for(int i=0;i<l-k;i++){
                this.node_list.remove(k);
            }
        }        
    }
}
