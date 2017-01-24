import Encryption.KeyGenerator;
import Encryption.PrivateKey;
import Encryption.PublicKey;
import Encryption.RSAEncryptor;
import org.apache.log4j.Logger;

import java.math.BigInteger;

/**
 * Created by etudiant on 18/01/17.
 */
public class Sandbox {
    private final static Logger logger = Logger.getLogger(KeyGenerator.class);

    public static void main(String[] args) {
        KeyGenerator generator = new KeyGenerator();
        RSAEncryptor encryptor = new RSAEncryptor();

        BigInteger a = new BigInteger("5141");
        BigInteger b = new BigInteger("4992");

        //generator.setE(a);
        //generator.setM(b);
/*
        PublicKey publicKey = generator.generatePublicKey();
        logger.info("Clé publique : "+publicKey);

        PrivateKey privateKey = generator.generatePrivateKey();
        logger.info("Clé privée : "+ privateKey);

        int[] encrypted = encryptor.encrypt("Ceci est test",publicKey);
        String decrypted = encryptor.decrypt(encrypted,privateKey);

        logger.info("Message recu : "+decrypted);
        */

        String res = "";
        byte[] ASCIIMessage = encryptor.toASCII("Test");
        for (byte bit : ASCIIMessage) {
            res += bit + " ";
        }
        logger.debug(res);

        int encrypted_message[] = new int[ASCIIMessage.length];

        for (int i = 0; i < ASCIIMessage.length; ++i) {
            BigInteger ASCIIChar = BigInteger.valueOf((long) (ASCIIMessage[i]));
            encrypted_message[i] = (ASCIIChar.modPow(a, b)).intValue();
        }
        res = "";
        for (int bit : encrypted_message) {
            res += bit + " ";
        }
        logger.debug(res);


    }
}