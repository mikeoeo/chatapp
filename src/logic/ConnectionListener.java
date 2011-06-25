package logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import structs.Join;
import structs.Message;
import structs.Token;

/**
 * Waits for new nodes to join
 * 
 */
public class ConnectionListener extends ThreadInterface {

    private ServerSocket socket;

    public ConnectionListener(int port) {
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    synchronized public void run() {
        Socket clientListener;
        ObjectInputStream oinputstream;
        Object object;

        while (true) {
            try {
                clientListener = socket.accept();
                
                oinputstream = new ObjectInputStream(clientListener.getInputStream());
                object = oinputstream.readObject();
                
                if(object instanceof Join) {
                    System.out.println("Join received");
                } else if(object instanceof Message) {
                    System.out.println("Message received");
                } else if(object instanceof Token) {
                    System.out.println("Token received");
                }
                
            } catch (IOException ex) {
                Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
