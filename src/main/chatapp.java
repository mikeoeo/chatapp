/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author 3070130-3070175
 */
public class chatapp {
    public static void main(String[] ars){
        String[][] node_list=new String[10][2];
        InetAddress[] nodes=new InetAddress[4];
        try{
        nodes[3]=InetAddress.getByName("localhost");
        nodes[2]=InetAddress.getByName("www.google.com");
        nodes[0]=InetAddress.getByName("www.google.com");
        nodes[1]=InetAddress.getByName("www.aueb.com");
        }
        catch(UnknownHostException e){
            
        }
        for(int i=0;i<4;i++){
            System.out.println(nodes[i].toString());
        }
      /* String[][] temp=new String[2][3];
        for(int i=0;i<3;i++){
            temp[0][i]=nodes[i][0];
            temp[1][i]=nodes[i][1];
        }*/
        Arrays.sort(nodes, new Comparator<InetAddress>() {
            @Override
            public int compare(InetAddress entry1, InetAddress entry2) {
                return entry1.toString().compareTo(entry2.toString());
            }
        });
      /*  for(int i=0;i<3;i++){
            nodes[i][0]=temp[0][i];
            nodes[i][1]=temp[1][i];
        }*/
        System.out.println(nodes[2].toString().compareTo(nodes[3].toString()));
        for(int i=0;i<4;i++){
            System.out.println(nodes[i].toString());
        }
    }
}
