import Client.Client;
import Client.ClientGUI;

import Encryption.*;
import MessageExchange.Whisper;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

/**
 * Only used as a test sandbox
 */
public class Sandbox {
    private final static Logger LOGGER = Logger.getLogger(Sandbox.class);

    public static void testASCII() {
        RSAEncryptor encryptor = new RSAEncryptor();
        byte[] ascii_message = encryptor.toASCII("Bonjour !");
        LOGGER.info("Traduction de l'ASCII : " + encryptor.fromASCII(ascii_message));
    }

    public static void testEncryptDecrypt(String message) {
        //testASCII();

        KeyGenerator generator = new KeyGenerator();
        generator.initParameters();

        BigInteger n = new BigInteger("5141");
        BigInteger e = new BigInteger("7");
        BigInteger u = new BigInteger("4279");

        generator.defineParameters(n, new BigInteger("0"), e);
        generator.defineU(u);

        PublicKey publicKey = generator.generatePublicKey();
        LOGGER.info("Clé publique : " + publicKey);

        PrivateKey privateKey = generator.generatePrivateKey();
        LOGGER.info("Clé privée : " + privateKey);

        Encryptor encryptor = new RSAEncryptor();

        BigInteger[] encryptedHello = encryptor.encrypt(message, publicKey);

        String decryptedMSG = encryptor.decrypt(encryptedHello, privateKey);

        LOGGER.info("Message décrypté  : " + decryptedMSG);
    }

    public static void startClientAsServer(String args[]) {
                
        String s_port;
        if (args.length < 1) {
            s_port = "2000";
        } 
        else {
            s_port = args[0];
        }

        boolean hasGUI= true;
        Client client= new Client(hasGUI);
        try {
            client.initConnection(s_port);
            client.startChat();
            client.stopChat();
            client.closeConnection();
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
    }
    
    public static void OLDstartGUIClientAsServer(String[] args) {

        String s_port;
        if (args.length < 1) {
            s_port = "2000";
        } else {
            s_port = args[0];
        }
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>


        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientGUI(/*s_port*/).setVisible(true);
                
//                ClientGUI.client.stopChat();
//                client.closeConnection();
            }
        });
    }
    
        
    public static void startGUIClientAsServer(String[] args) {

        String s_port;
        if (args.length < 1) {
            s_port = "2000";
        } else {
            s_port = args[0];
        }

        boolean has_GUI= true;
        Client client = new Client(has_GUI);
        
        Whisper.setMyName("Bob");

        try {
            client.initConnection(s_port);
            client.startChat();
//            client.stopChat();
//            client.closeConnection();
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
    }
    
    public static void showIPAddress() {        
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            System.out.println("Your current IP address : " + ip.getHostAddress());
        } catch (UnknownHostException e) {
 
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        testASCII();
//        startClientAsServer(args);
//        showIPAddress();

        startGUIClientAsServer(args);
    }
}
