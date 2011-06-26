/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structs;

/**
 *
 * @author 3070130-3070175
 */
public class FirstNode extends Node{
    private static final long serialVersionUID = 1L;
    private Token lastTokenSeen=null;
    private Message lastMessageSeen=null;

    public void setLastMessageSeen(Message lastMessageSeen) {
        this.lastMessageSeen = lastMessageSeen;
    }

    public Message getLastMessageSeen() {
        return lastMessageSeen;
    }

    public void setLastTokenSeen(Token lastTokenSeen) {
        this.lastTokenSeen = lastTokenSeen;
    }

    public Token getLastTokenSeen() {
        return lastTokenSeen;
    }
    
    public FirstNode(){
        super();
    }
    
    
    
    @Override
    public boolean isFirst(){
        return true;
    }
    
}
