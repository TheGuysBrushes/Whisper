/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageExchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

/**
 *
 * @author Work
 */
public class MessageReceptor implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(MessageReceptor.class);
    
    private ObjectInputStream inS;
    
    public void initConnection(Socket socket) {
        
        // Création du stream d'entrée
        try {
            inS = new ObjectInputStream(socket.getInputStream());
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
    protected String receiveMessage() throws IOException {
    
        try {
            // Réception de la réponse
            String response = (String) inS.readObject();

            return response;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * 
     */
    @Override
    public void run() {
        String message= "quit";
        do {
            try {
                message= receiveMessage();
                System.out.println("SENDER_NAME : " + message);
            } catch (IOException e) {
                LOGGER.error("Exception : "+ e.getMessage());
                e.printStackTrace();
            }
        } while (! message.equals("quit"));
    }
}
