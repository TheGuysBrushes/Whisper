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

public class Client {

    /**
     * Initialize connection with a distant or local server
     * @param args : Parameters of the connection :
     *  "args[0]" is the port and "args[1]" the address
     */
    public void initConnection(String args[]) {
                
        String s_port;
        if (args.length < 1) {
            s_port = "2008";
        } else {
            s_port = args[0];
        }
        
        String message;
        if (args.length < 1) {
            message = "hello";
        } else {
            message = args[1];
        }
        
        //Client
        try {
            //Envoi
            DatagramSocket socket = new DatagramSocket();
            String s = message;
            byte[] buf = s.getBytes();
            InetAddress adresse = InetAddress.getByName("192.168.99.107");
//            InetAddress adresse = InetAddress.getByName("192.168.43.78");
            
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
    
    public static void main(String[] args) {
        KeyGenerator generator= new KeyGenerator();
        PublicKey key= generator.generatePublicKey();
        
        key.show();
        
        RSAEncryptor encryptor= new RSAEncryptor();
        encryptor.toASCII("Bonjour !");
        
        
//        generator.generatePrivateKey();
    }
}
