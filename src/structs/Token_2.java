package structs;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author 3070130-3070175
 */
public class Token_2 {
    //variables
    private short seq_number; //message sequence number
    private String[][] node_list=new String [10][2]; //IP and port list of the nodes
    
    //empty constructor
    public Token_2(){
        this.seq_number=0;
        for(int i=0;i<10;i++){
            for(int j=0;j<2;j++){
                this.node_list[i][j]="";
            }
        }
    }
    //constructor with previous data
    public Token_2(String[][] nodes){
        this.seq_number=0;
        for(int i=0;i<nodes.length;i++){
            System.arraycopy(nodes[i], 0, this.node_list[i], 0, nodes[0].length);
        }
        for(int i=nodes.length;i<10;i++){
            for(int j=0;j<2;j++){
                this.node_list[i][j]="";
            }
        }
    }
    
    //methods
    //returns the message sequence number
    public short get_seq_number(){
        return this.seq_number;
    }
    //returns the node list array
    public String[][] get_node_list(){
        return this.node_list;
    }
    //returns a line from the node list array
    public String[] get_node(int line){
        String[] s=new String[2];
        s[0]=this.node_list[line][0];
        s[1]=this.node_list[line][1];
        return s;
    }
    //sets the message sequence number
    public void set_seq_number(short number){
        this.seq_number=number;
    }
    //sets the node list
    public void set_node_list(String[][] new_node_list){
        this.node_list=new_node_list;
    }
    //increases the value of seq_number by 1
    public void add1(){
        this.seq_number++;
    }
    //adds one node in the node_list
    public void add(String ip,String port){
        for(int i=0;i<10;i++){
            if (this.node_list[i][0].equals(""))
            {
                this.node_list[i][0]=ip;
                this.node_list[i][1]=port;
                break;
            }
        }
    }
    //sorts the node list based in IP
    public void node_list_sort_by_IP(){
        Arrays.sort(this.node_list, new Comparator<String[]>() {
            @Override
            public int compare(String[] entry1, String[] entry2) {
                String time1 = entry1[0];
                String time2 = entry2[0];
                return time1.compareTo(time2);
            }
        });
    }
}
