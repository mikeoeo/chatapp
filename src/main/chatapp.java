package main;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import logic.KeyStrokeListener;
import logic.ObjectListener;
import structs.AbstractMessage;
import structs.Join;
import structs.Message;
import structs.NetAddress;
import structs.Token;

/**
 * Application core
 * 
 * @author 3070130-3070175
 */
public class chatapp {

    public static List<NetAddress> nodeslist;

    private static void usage(int exitstatus) {
        System.out.println("chatapp - "
                + "Copyright (c) 2011 Ntanasis Periklis and Chatzipetors Mike\n\n"
                + "Usage:"
                + "\tchatapp -i\n"
                + "\tchatapp -c address port\n"
                + "\tchatapp -s address\n"
                + "\tchatapp -h");
        System.exit(exitstatus);
    }

    public static void main(String[] ars) throws InterruptedException, UnknownHostException, IOException {

        if (ars.length == 0) {
            usage(1);
        }

        if (ars[0].equalsIgnoreCase("-i")) {
            //initial node
            System.out.println("Creating initial node...");
            ObjectListener listener = new ObjectListener(6070);
            listener.start();
        } else if (ars[0].equalsIgnoreCase("-c")) {
            //new node with specific port
            Join join = new Join();
            Socket socket = new Socket(ars[1], Integer.parseInt(ars[2]));
            ObjectOutputStream ooutputstream = new ObjectOutputStream(socket.getOutputStream());
            ooutputstream.writeObject(join);
            ooutputstream.close();
        } else if (ars[0].equalsIgnoreCase("-s")) {
            //new node - search port
            Join join = new Join();
            Socket socket;
            ObjectOutputStream ooutputstream;
            for (int i = 6070; i < 6080; i++) {
                socket = new Socket(ars[1], i);
                ooutputstream = new ObjectOutputStream(socket.getOutputStream());
                ooutputstream.writeObject(join);
                ooutputstream.close();
            }
        } else if (ars[0].equalsIgnoreCase("-h")) {
            usage(0);
        } else {
            usage(1);
        }

        KeyStrokeListener keylistener = new KeyStrokeListener();
        keylistener.run();
        
    }
}