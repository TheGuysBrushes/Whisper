/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author flodavid
 */
public class KeyGenerator {
    private final static Logger logger = LoggerFactory.getLogger(KeyGenerator.class);


    private BigInteger e;
    private BigInteger m;
    private BigInteger n;

    public BigInteger generateKey() {
        Random rand = new Random();
        BigInteger p = BigInteger.probablePrime(100, rand);
        BigInteger q = BigInteger.probablePrime(100, rand);

        // n definition
        n = p.multiply(q);

        // m definition
        BigInteger p_less = p.subtract(new BigInteger("1") );
        m = p_less.multiply(q.subtract(new BigInteger("1") ));

        return n;
    }

    public BigInteger generatePrivateKey() {
        logger.info("Debut méthode");
        BigInteger n = new BigInteger("12");
        BigInteger u = new BigInteger("12");
        BigInteger r = new BigInteger("12");

        BigInteger pgcd = e.gcd(m);
        logger.debug("Valeur du pgcd({},{}) : {}",e,m,pgcd);

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
