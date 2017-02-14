package MessageExchange;

import Encryption.Encryptor;
import Encryption.PrivateKey;
import Encryption.PublicKey;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Work on 1/30/2017.
 */

public class Whisper implements Serializable {
    private static final long serialVersionUID = 3487495895819393L;

    public final String SYSTEM = "SYS";
    final private Date time;
    private String content;
    final private String sender_name;
    private static String my_name;
    private boolean completed;
    private int id;


    public Whisper(String message) {
        content = message;
        sender_name = my_name;
        completed = false;
        time = new Date();
    }

    public Whisper(String message, String name) {
        content = message;
        sender_name = name;
        completed = false;
        time = new Date();
    }

    public Whisper(Whisper other) {
        this.time = other.time;
        this.content = other.content;
        this.sender_name = other.sender_name;
        this.completed = other.completed;
        this.id = other.id;
    }

    public static void setMyName(String name) {
        my_name = name;
    }

    public Date getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public boolean hasBeenSendByMe() {
        return sender_name.equals(my_name);
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted() {
        this.completed = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenderName() {
        return sender_name;
    }

    public static String getSYSTEM() {
        return "SYS";
    }

    public void decrypt(Encryptor encryptor, PrivateKey decryptKey) {
        content = encryptor.decrypt(content, decryptKey);
    }

    public void encrypt(Encryptor encryptor, PublicKey encryptKey) {
        content = encryptor.encryptToString(content, encryptKey);
    }


    public String toString() {
        if (sender_name.equals(SYSTEM)) {
            return getContent();
        }
        else return sender_name + " : " + getContent();
    }

}
