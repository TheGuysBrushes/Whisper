import Encryption.KeyGenerator;
import Encryption.PrivateKey;
import Encryption.PublicKey;
import org.apache.log4j.Logger;

import java.math.BigInteger;

/**
 * Created by etudiant on 18/01/17.
 */
public class Sandbox {
    private final static Logger logger = Logger.getLogger(KeyGenerator.class);

    public static void main(String[] args) {
        KeyGenerator generator= new KeyGenerator();
        //BigInteger a = new BigInteger("7");
        //BigInteger b = new BigInteger("4992");

        //generator.setE(a);
        //generator.setM(b);

        PublicKey publicKey = generator.generatePublicKey();
        logger.info("Clé publique : "+publicKey);

        PrivateKey privateKey = generator.generatePrivateKey();
        logger.info("Clé privée : "+ privateKey);
    }
}