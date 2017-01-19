/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encryption;

import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.Random;

/**
 * Used to generate private and public keys in order to able two speakers
 * to communicate privately
 *
 * @author flodavid
 */
public class KeyGenerator {
    private final static Logger logger = Logger.getLogger(KeyGenerator.class);


    private BigInteger e;
    private BigInteger m;
    private BigInteger n;

    /**
     * Generate a public key
     * @return a generated public key
     */
    public PublicKey generatePublicKey() {
        Random rand = new Random();

        // p and q definition
        BigInteger p = BigInteger.probablePrime(100, rand);
        BigInteger q = BigInteger.probablePrime(100, rand);

        // n definition
        n = p.multiply(q);

        // m definition
        BigInteger p_less = p.subtract(new BigInteger("1") );
        m = p_less.multiply(q.subtract(new BigInteger("1") ));

        // e (public exponent) definition
        e= new BigInteger("2");

        while ( m.gcd(e) != new BigInteger("1") ) {
            e.nextProbablePrime();
        }

        // Create the key from calculated values
        PublicKey key= new PublicKey(n, e);

        return key;
    }

    public BigInteger generatePrivateKey() {
        logger.info("Debut méthode");
        BigInteger n = new BigInteger("12");
        BigInteger u = new BigInteger("12");
        BigInteger r = new BigInteger("12");

        BigInteger pgcd = e.gcd(m);
        logger.debug("Valeur du pgcd("+e+","+m+") : "+pgcd);

        return u;
    }

    public BigInteger getE() {
        return e;
    }

    public void setE(BigInteger e) {
        this.e = e;
    }

    public BigInteger getM() {
        return m;
    }

    public void setM(BigInteger m) {
        this.m = m;
    }

    public BigInteger getN() {
        return n;
    }

    public void setN(BigInteger n) {
        this.n = n;
    }
}
