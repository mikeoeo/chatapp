package logic;

import java.util.ArrayList;
import java.util.Scanner;

/**
* Listens for new chat messages or other keystrokes.
*
*/
public class KeyStrokeListener implements Runnable {

    private ArrayList<String> msglist=new ArrayList<String>();

    @Override
    public void run() {
        Scanner in;
        String message;
        while (true) {
            in = new Scanner(System.in);
            message = in.nextLine();
            System.out.println("Message: " + message + " length:" + message.length());
            synchronized (this) {
                this.msglist.add(message);
            }
        }
    }

    synchronized public String getMsglist() {
        String list="";
        for(int i=0;i<this.msglist.size();i++){
            list+=this.msglist.get(i);
        }
        msglist.clear();
        return list;
    }
}

