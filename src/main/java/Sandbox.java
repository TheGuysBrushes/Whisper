import Encryption.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigInteger;
import java.net.*;

import Client.Client;

/**
 * Created by etudiant on 18/01/17.
 */
public class Sandbox {
    private final static Logger logger = Logger.getLogger(Sandbox.class);

    public static void testASCII() {
        RSAEncryptor encryptor = new RSAEncryptor();
        byte[] ascii_message = encryptor.toASCII("Bonjour !");
        logger.info("Traduction de l'ASCII : " + encryptor.fromASCII(ascii_message));
    }

    public static void testEncryptDecrypt(String message) {

        KeyGenerator generator = new KeyGenerator();
        generator.initParameters();

        BigInteger n = new BigInteger("5141");
        BigInteger e = new BigInteger("7");
        BigInteger u = new BigInteger("4279");

        generator.defineParameters(n, new BigInteger("0"), e);
        generator.defineU(u);

        PublicKey publicKey = generator.generatePublicKey();
        logger.info("Clé publique : " + publicKey);

        PrivateKey privateKey = generator.generatePrivateKey();
        logger.info("Clé privée : " + privateKey);

        Encryptor encryptor = new RSAEncryptor();

        BigInteger[] encryptedHello = encryptor.encrypt(message, publicKey);

        String decryptedMSG = encryptor.decrypt(encryptedHello, privateKey);

        logger.info("Message décrypté  : " + decryptedMSG);
    }


    public static void main(String[] args) {
        //testASCII();        

    }
}
