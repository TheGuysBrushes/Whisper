/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encryption;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * An exchangeable encryption key
 *
 * @author flodavid
 */
public class PublicKey implements Serializable {
    private BigInteger n;
    private BigInteger e;

    public PublicKey(BigInteger n, BigInteger public_exponent) {
        this.n = n;
        this.e = public_exponent;
    }


    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }

    public String toString() {
        return n + ", " + e;
    }

    public void show() {
        System.out.println(this.toString());
    }

    public BigInteger get_e() {
        return e;
    }

    public BigInteger get_n() {
        return n;
    }
}
