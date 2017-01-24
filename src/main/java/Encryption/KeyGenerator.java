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

    private BigInteger n;
    private BigInteger m;
    private BigInteger e;
    private BigInteger u;
    
    public boolean is_initiated= false;
    public boolean private_initiated= false;

    public void initParameters() {

        logger.info("Key generation parameters initialization");
        
        Random rand = new Random();

        // p and q definition
        BigInteger p = BigInteger.probablePrime(100, rand);
        BigInteger q = BigInteger.probablePrime(100, rand);

        // n definition
        n = p.multiply(q);

        // m definition
        BigInteger p_less = p.subtract(new BigInteger("1"));
        m = p_less.multiply(q.subtract(new BigInteger("1")));

        // e (public exponent) definition
        e = new BigInteger("2");

        while ((m.gcd(e)).compareTo(new BigInteger("1")) != 0) {
            e = e.nextProbablePrime();
        }
        
        // set flag to indicate values have been initiated
        is_initiated= true;
    }
    
    public void defineParameters(BigInteger N, BigInteger M, BigInteger E) {
        this.n= N;
        this.m= M;
        this.e= E;
        
        is_initiated= true;
    }
    
    public void defineU(BigInteger U) {
        this.u= U;
        
        private_initiated= true;
    }
    
    private void calculateU() {
                BigInteger r_prec = e;
        BigInteger u_prec = new BigInteger("1");
        BigInteger v_prec = new BigInteger("0");
        BigInteger r = m;
        u = new BigInteger("0");
        BigInteger v = new BigInteger("1");
        BigInteger r_next;
        BigInteger u_next;
        BigInteger v_next;

        logger.debug("Lancement avec paramètres : ");
        logger.debug("r_prec = " + r_prec + "\tr = " + r);
        logger.debug("u_prec = " + u_prec + "\tu = " + u);
        logger.debug("v_prec = " + v_prec + "\tv = " + v + "\n");

        do {
            r_next = r_prec.subtract((r_prec.divide(r)).multiply(r));
            u_next = u_prec.subtract((r_prec.divide(r)).multiply(u));
            v_next = v_prec.subtract((r_prec.divide(r)).multiply(v));

            r_prec = r;
            r = r_next;
            u_prec = u;
            u = u_next;
            v_prec = v;
            v = v_next;

            if (u_next.compareTo(new BigInteger("0")) == -1) {
                logger.debug("Valeur de u négative (u = " + u_next + ")");
                u_next = solveNegativeValue(u_next);
            }

            logger.debug("r = " + r_next);
            logger.debug("u = " + u_next);
            logger.debug("v = " + v_next + "\n");
        } while (r_next.compareTo(new BigInteger("0")) != 0);
        
        private_initiated= true;
    }
    
    public void eraseParameters(){
        is_initiated= false;
        private_initiated= false;
    }
    
    /**
     * Generate a public key
     *
     * @return a generated public key
     */
    public PublicKey generatePublicKey() {
        if (!is_initiated) {
            initParameters();
        }
        logger.info("Public key generation");
        
        // Create the key from calculated values
        PublicKey key = new PublicKey(n, e);

        return key;
    }

    public PrivateKey generatePrivateKey() {
        
        if (!is_initiated) {
            initParameters();
        }
        
        // U value calculation if needed
        if (!private_initiated) {
            calculateU();
        }

        logger.info("Private key generation");

        // Create the key from calculated values
        PrivateKey key = new PrivateKey(n, u);
        return key;
    }

    private BigInteger solveNegativeValue(BigInteger u) {
        BigInteger k = new BigInteger("-1");
        BigInteger u_new = u;

        do {
            u_new = u.subtract(k.multiply(m));
            k = k.subtract(new BigInteger("1"));
        } while (new BigInteger("2").compareTo(u_new) != -1 || u_new.compareTo(m) != -1);

        return u_new;
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

    public BigInteger getU() {
        return u;
    }

    public void setU(BigInteger u) {
        this.u = u;
    }
}
