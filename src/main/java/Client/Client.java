/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author flodavid
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import Encryption.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

public class Client {
    private final static Logger logger = Logger.getLogger(Client.class);
    
    public PublicKey myPublicKey;
    private PrivateKey myPrivateKey;
    
    private PublicKey serverPublicKey;
    
    private ObjectInputStream inS;
    private ObjectOutputStream outS;

    /**
     * Initialize connection with a distant or local server
     * @param address : IP address of the server
     */
    public void initConnection(String message, String address, String s_port) {
        
        generateKeys(address);
        
        //Client

        int port = Integer.parseInt(s_port);
        RSAEncryptor encryptor = new RSAEncryptor();
        try (Socket socket = new Socket(InetAddress.getByName(address), port)) {

            // Envoi clé
            outS = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            outS.writeObject(myPublicKey);
            outS.flush();

            // Reception clé
            inS = new ObjectInputStream(socket.getInputStream());
            serverPublicKey = (PublicKey) inS.readObject();
            
            sendReceiveEncryptedMessage(message);
            
//            socket.close();
        } catch (SocketException e) {
            logger.debug("SocketException", e);
        } catch (UnknownHostException e) {
            logger.debug("UnknownHostException", e);
        } catch (IOException e) {
            logger.debug("IOException", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void generateKeys(String address) {
        KeyGenerator generator= new KeyGenerator();
        
        myPublicKey = generator.generatePublicKey();
        logger.info("Clé publique : "+myPublicKey);

        myPrivateKey = generator.generatePrivateKey();
        logger.debug("Clé privée : "+ myPrivateKey);        
    }

    public String encryptMessage(String message) {
        Encryptor encryptor = new RSAEncryptor();
                
        return encryptor.encryptToString(message, myPublicKey);
    }
    
    public String decryptMessage(String encrypted_message) {
        Encryptor encryptor = new RSAEncryptor();
                
        return encryptor.decrypt(encrypted_message, myPrivateKey);
    }
    
    public void sendReceiveEncryptedMessage(String message) throws IOException {
        Encryptor encryptor = new RSAEncryptor();
        
        // Envoi d'un message
        String msg = "message";

        String messageCrypted = encryptor.encryptToString(msg, serverPublicKey);
        try {
            outS.writeObject(messageCrypted);
            outS.flush();

            // Réception de la réponse
            String reponse = (String) inS.readObject();
            String decryptedReponse = encryptor.decrypt(reponse, myPrivateKey);
            logger.info("Réponse : " + decryptedReponse);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendEncryptedMessage(String message) {
        Encryptor encryptor = new RSAEncryptor();
        
        BigInteger[] encryptedHello = encryptor.encrypt(message, myPublicKey);   
    }
    
    public String receiveEncryptedMessage(String address) {
        
        if (serverPublicKey == null) {
            logger.error("Je n'ai pas recu la clé public du serveur");
            return "";
        }
        
//        logger.info("Message décrypté  : "+ decryptedMSG);

        return "";
    }
    
    /**
     * @param args : Parameters of the connection :
     *  "args[0]" is the address and "args[1]" the port
     */
    public static void main(String[] args) {
                        
        String message;
        if (args.length < 1) {
            message = "hello";
        } else {
            message = args[0];
        }
        
        String address;
        if (args.length < 2) {
            address = "localhost";
        } else {
            address = args[1];
        }
//        address = "192.168.99.107";
        
        String s_port;
        if (args.length < 3) {
            s_port = "2000";
        } else {
            s_port = args[2];
        }

        Client client = new Client();
        client.initConnection(message, address, s_port);
    }
}
