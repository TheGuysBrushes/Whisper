package Encryption;

import java.math.BigInteger;

/**
 * Created by etudiant on 19/01/17.
 */
public class PrivateKey {

    private BigInteger n;
    private BigInteger u;

    public PrivateKey(BigInteger n, BigInteger u_coeff) {
        this.n= n;
        this.u= u_coeff;
    }

    public String toString() {
        return n +", "+ u;
    }

    public void show(){
        System.out.println(this.toString());
    }
}
