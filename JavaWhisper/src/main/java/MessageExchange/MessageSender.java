/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageExchange;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import org.apache.log4j.Logger;

/**
 *
 * @author Work
 */
public class MessageSender implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(MessageSender.class);
    private ObjectOutputStream outS;
    
    public void initConnection(Socket socket) {

        // Création du stream de sortie
        try {
            outS = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        } catch (SocketException e) {
            LOGGER.debug("SocketException", e);
        } catch (UnknownHostException e) {
            LOGGER.debug("UnknownHostException", e);
        } catch (IOException e) {
            LOGGER.debug("IOException", e);
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
     * @param args
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
                LOGGER.error("Exception : "+ e.getMessage());
                e.printStackTrace();
            }
        } while (! message.equals("quit"));
    }
}
