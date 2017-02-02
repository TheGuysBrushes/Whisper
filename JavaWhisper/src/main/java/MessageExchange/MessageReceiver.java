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
    public String receiveMessage() throws IOException {
    
        try {
            // Réception de la réponse
            String response = (String) inS.readObject();

            return response;
        } catch (ClassNotFoundException e) {
            LOGGER.error("Exception : "+ e);
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
                System.out.flush();
                for (int i= 0; i < "Chuchoter :".length(); ++i) System.out.print("\b");
                
                System.out.println("SENDER_NAME : " + message);
                System.out.print("Chuchoter : ");
            } catch (IOException e) {
                LOGGER.error("Exception : "+ e);
            }
        } while (! message.equals("quit"));
    }
}
