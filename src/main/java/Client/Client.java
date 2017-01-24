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
import java.math.BigInteger;
import org.apache.log4j.Logger;

public class Client {
    private final static Logger logger = Logger.getLogger(Client.class);
    
    public PublicKey myPublicKey;
    public PrivateKey myPrivateKey;
    
    public PrivateKey serverPrivateKey;

    /**
     * Initialize connection with a distant or local server
     * @param args : Parameters of the connection :
     *  "args[0]" is the port and "args[1]" the address
     */
    public void initConnection(String args[]) {
                
        String message;
        if (args.length < 1) {
            message = "hello";
        } else {
            message = args[0];
        }
        
        String address;
        if (args.length < 2) {
            address = "192.168.99.107";
        } else {
            address = args[1];
        }
        
        String s_port;
        if (args.length < 2) {
            s_port = "2008";
        } else {
            s_port = args[1];
        }
        
        generateKeys(address);
        
        //Client
        try {
            //Envoi
            DatagramSocket socket = new DatagramSocket();
            String key = myPublicKey.get_n() + "%" + myPublicKey.get_e();
            byte[] buf = key.getBytes();
            InetAddress adresse = InetAddress.getByName(address);
            
            int port = Integer.parseInt(s_port);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, adresse, port);
            
            socket.send(packet);

            //Reception
            byte[] inBuf = new byte[1024];
            DatagramPacket packetReceive = new DatagramPacket(inBuf, inBuf.length);
            socket.receive(packetReceive);
            String s2 = new String(packetReceive.getData(), 0, packetReceive.getLength());
            System.out.println(s2);

            socket.close();
        } catch (Exception e) {
            System.err.println("impossible de creer");
        }
    }
    
    public void generateKeys(String address) {
        KeyGenerator generator= new KeyGenerator();
        
        PublicKey publicKey = generator.generatePublicKey();
        logger.info("Clé publique : "+publicKey);

        PrivateKey privateKey = generator.generatePrivateKey();
        logger.debug("Clé privée : "+ privateKey);        
    }

    public String encryptMessage(String message) {
        Encryptor encryptor = new RSAEncryptor();
                
        return encryptor.encryptToString(message, myPublicKey);
    }
    
    public String decryptMessage(String encrypted_message) {
        Encryptor encryptor = new RSAEncryptor();
                
        return encryptor.decrypt(encrypted_message, serverPrivateKey);
    }

    public void sendEncryptedMessage(String message) {
        Encryptor encryptor = new RSAEncryptor();
        
        BigInteger[] encryptedHello = encryptor.encrypt(message, myPublicKey);   
    }
    
    public String receiveEncryptedMessage(String address) {
        
        if (serverPrivateKey == null) {
            logger.error("Je n'ai pas recu la clé public du serveur");
            return "";
        }
        
//        logger.info("Message décrypté  : "+ decryptedMSG);

        return "";
    }
    
    public static void main(String[] args) {
        
        
    }
}
