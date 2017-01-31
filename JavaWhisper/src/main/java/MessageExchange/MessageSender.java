/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageExchange;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.apache.log4j.Logger;

/**
 *
 * @author Florian DAVID
 */
public class MessageSender implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(MessageSender.class);
    private ObjectOutputStream outS;

    public ObjectOutputStream getOutS() {
        return outS;
    }
    
    public void initConnection(Socket socket) {
        // Création du stream de sortie
        try {
            outS = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (SocketException | UnknownHostException e) {
            LOGGER.error("SocketException", e);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
    }
    
    /**
     * Encrypt and send a message to the server
     * @param message : message to send
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        outS.writeObject(message);
        outS.flush();
    }
        
    /**
     * Override of the run method
     */
    @Override
    public void run() {
        Scanner textscan=new Scanner(System.in);
        
        String message;
        do {
            
            System.out.print("Chuchoter : ");
            message= textscan.nextLine();
            
            try {
//                System.out.println("Me : " + message);
                sendMessage(message);
            } catch (IOException e) {
                LOGGER.error("Exception : "+ e);
            }
        } while (! message.equals("quit"));
    }
}
