/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageExchange;

/**
 *
 * @author flodavid
 */
public interface MessageWriter {
    
    /**
     * Set a message sender to send the messages that are written
     * @param msgSender 
     */
    public void setMessageSender(MessageExchange.MessageSender msgSender);
    
    public void startSending();
    
    public void stopSending() throws InterruptedException;

}
