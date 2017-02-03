/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import MessageExchange.MessageSender;

import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.Logger;

/**
 *
 * @author flodavid
 */
public class TermWriter implements MessageExchange.MessageWriter, Runnable  {
    private final static Logger LOGGER = Logger.getLogger(TermWriter.class);
    
    private Thread senderThread;
    private MessageSender sender;

    @Override
    public void setMessageSender(MessageSender msgSender) {
        sender= msgSender;
    }

    @Override
    public void startSending() {
        // Lancement du Thread d'envoi de messages
        senderThread= new Thread(this);
        senderThread.start();
    }

    @Override
    public void stopSending() throws InterruptedException {
        senderThread.join();
    }
    

    /**
     * Override of the run method
     */
    @Override
    public void run() {
        Scanner textscan=new Scanner(System.in);
        
        String message;
        do {
            
            System.out.print("ME : ");
            message= textscan.nextLine();
            
            try {
                System.out.println("Me : " + message);
                sender.sendMessage(message);
            } catch (IOException e) {
                LOGGER.error("Exception : "+ e);
            }
        } while (! message.equals("quit"));
    }
}
