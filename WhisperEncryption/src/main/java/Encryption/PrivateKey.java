package Encryption;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by etudiant on 19/01/17.
 */
public class PrivateKey implements Serializable {

    private BigInteger n;
    private BigInteger u;

    public PrivateKey(BigInteger n, BigInteger u_coeff) {
        this.n = n;
        this.u = u_coeff;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }

    public String toString() {
        return n + ", " + u;
    }

    public void show() {
        System.out.println(this.toString());
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
