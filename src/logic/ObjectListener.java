package logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import structs.Node;

/**
 * Waits for new nodes to join
 * 
 */
public class ObjectListener extends ThreadInterface {

    private ServerSocket socket;
    private Node node;
    private int port;
    private KeyStrokeListener keylisten;
    
    public ObjectListener(int port, Node node, KeyStrokeListener keylist) {
        this.node=node;
        this.port=port;
        this.keylisten=keylist;
    }

    @Override
    synchronized public void run() {
        Socket clientListener;
        ObjectInputStream oinputstream;
        Object object;

        while (true) {
            try {
                this.socket = new ServerSocket(port);
                clientListener = socket.accept();
                oinputstream = new ObjectInputStream(clientListener.getInputStream());
                object = oinputstream.readObject();
                MessageHandler msghandler = new MessageHandler(object,clientListener.getInetAddress().getHostAddress(),6071,this.node,this.keylisten);
                oinputstream.close();
                clientListener.close();
                socket.close();
                msghandler.start();
                
            } catch (IOException ex) {
                Logger.getLogger(ObjectListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ObjectListener.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }

    }
}
