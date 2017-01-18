/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encryption;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author flodavid
 */
public class KeyGenerator {
    BigInteger m;
    BigInteger n;

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

}
