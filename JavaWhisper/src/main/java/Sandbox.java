import Client.Client;
import Encryption.*;
import java.io.IOException;
import org.apache.log4j.Logger;

import java.math.BigInteger;

/**
 * Created by etudiant on 18/01/17.
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


    public static void main(String[] args) {
        //testASCII();
        
        String s_port;
        if (args.length < 1) {
            s_port = "2000";
        } 
        else {
            s_port = args[0];
        }

        
        Client client= new Client();
        try {
            client.initConnection(s_port);
            client.startChat();
            client.closeConnection();
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
    }
}
