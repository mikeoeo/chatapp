package logic;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Listens for new chat messages or other keystrokes.
 * 
 */
public class KeyStrokeListener implements Runnable {

    private ArrayList<String> msglist;

    @Override
    public void run() {
        Scanner in;
        String message;
        while (true) {
            in = new Scanner(System.in);
            message = in.nextLine();
            System.out.println("Message: " + message + " length:" + message.length());
            synchronized (this) {
                getMsglist().add(message);
            }
        }
    }

    synchronized public ArrayList<String> getMsglist() {
        ArrayList<String> list = (ArrayList<String>) msglist.clone();
        msglist.clear();
        return list;
    }
}
