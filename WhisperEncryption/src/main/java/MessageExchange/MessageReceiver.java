/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageExchange;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author Florian DAVID
 */
public class MessageReceiver implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(MessageReceiver.class);
    
    private ObjectInputStream inS;
    private MessageDisplayer displayer;

    public MessageReceiver(MessageDisplayer msgDisplayer) {
        displayer= msgDisplayer;
    }

    
    
    public ObjectInputStream getInS() {
        return inS;
    }
    
    public void initConnection(Socket socket) {
        // Création du stream d'entrée
        try {
            inS = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (SocketException e) {
            LOGGER.debug("SocketException", e);
        } catch (UnknownHostException e) {
            LOGGER.debug("UnknownHostException", e);
        } catch (IOException e) {
            LOGGER.debug("IOException", e);
        }
    }
    
    /**
     * Receive and decrypt a message sent by the server
     * @return the received message sent by the server
     * @throws IOException
     */
    public Whisper receiveMessage() throws IOException {
    
        try {
            // Réception de la réponse
//            String response = (String) inS.readObject();
            Whisper response = (Whisper) inS.readObject();

            return response;
        } catch (ClassNotFoundException e) {
            LOGGER.error("Exception : "+ e);
            return new Whisper("ERROR");
        }
    }
    
    /**
     * 
     */
    @Override
    public void run() {
        Whisper message= new Whisper("quit");
        do {
            try {
                message= receiveMessage();
                displayer.showMessage(message);
                
//                ActionEvent messageReceivedEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, message);
                
            } catch (IOException e) {
                LOGGER.error("Exception : "+ e);
            }
        } while (! message.equals("quit"));
    }
}
