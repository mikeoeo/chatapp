/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structs;

/**
 *
 * @author 3070130-3070175
 */
public class Join extends AbstractMessage{
    private static final long serialVersionUID = 1L;
    private NetAddress sender_addr;
    
    public Join(NetAddress sender_addr){
        this.sender_addr=sender_addr;
    }
    
    public NetAddress getSender_addr() {
        return sender_addr;
    }
}
