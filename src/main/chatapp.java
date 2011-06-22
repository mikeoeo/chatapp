/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.List;
import structs.AbstractMessage;
import structs.Join;
import structs.Message;
import structs.NetAddress;
import structs.Token;

/**
 *
 * @author 3070130-3070175
 */
public class chatapp {
    public static void main(String[] ars){
        /*AbstractMessage abs = new Join();
        if(abs instanceof Join){
            System.out.println("abs Join");
        }
        else if(abs instanceof Message){
            System.out.println("abs Message");
        }
        else if(abs instanceof AbstractMessage){
            System.out.println("abs AbstractMessage");
        }
        Join join = new Join();
        Object o=join;
        Join j=(Join)o;
        if(o instanceof Join){
            System.out.println("o Join");
        }
        else if(o instanceof Message){
            System.out.println("o Message");
        }
        else if(o instanceof AbstractMessage){
            System.out.println("o AbstractMessage");
        }*/
        
        List<NetAddress> list= new ArrayList<NetAddress>();
        list.add(new NetAddress("192.168.1.3",6070));
        list.add(new NetAddress("192.168.1.2",6070));
        list.add(new NetAddress("192.168.0.25",6070));
        list.add(new NetAddress("192.168.3.3",6070));
        list.add(new NetAddress("192.168.0.1",6070));
        
        Token tok = new Token(list);
        for(int i=0;i<tok.get_node_list().size();i++){
            System.out.println(i+" "+tok.get_node_list().get(i).get_IP());
        }
        tok.node_list_sort_by_IP();
        for(int i=0;i<tok.get_node_list().size();i++){
            System.out.println(i+" "+tok.get_node_list().get(i).get_IP());
        }
    }
}