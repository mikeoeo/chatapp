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
        Scanner in = new Scanner(System.in);
        String message;
        while (in.hasNextLine()) {
            message = in.nextLine();
            System.out.println("Message: " + message + " length:" + message.length());
            synchronized (this) {
                this.msglist.add(message);
            }
        }
        //if EOF (ctrl-D in unix based systems, ctrl-Z in MS-DOS based systems)
        //exits loop
        //print statistics and other messages
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