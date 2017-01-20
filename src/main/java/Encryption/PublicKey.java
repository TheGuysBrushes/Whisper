/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encryption;

import java.math.BigInteger;

/**
 * An exchangeable encryption key
 * @author flodavid
 */
public class PublicKey {
    private BigInteger n;
    private BigInteger e;
    
    public PublicKey(BigInteger n, BigInteger public_exponent) {
        this.n= n;
        this.e= public_exponent;
    }
    
    public String toString() {
        return n +", "+ e;
    }
    
    public void show(){
        System.out.println(this.toString());
    }
    
    public BigInteger get_e() {
        return e;
    }
    
    public BigInteger get_n() {
        return n;
    }
}
