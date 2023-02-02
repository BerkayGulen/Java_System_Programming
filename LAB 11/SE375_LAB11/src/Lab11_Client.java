
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Lab11_Client {

    // Code used in class, prints a byte array onto the console.
    public static String printBytes(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X:", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        // Initializing a socket, connecting to the server
        System.out.println("Client waiting to connect...");
        Socket s = new Socket("localhost", 6789);
        System.out.println("Client connected.");

        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        // Get the secret key from file as a byte array.
        // A SecretKey object is also created, but never used, so that line is irrelevant.
        byte[] keyBytes = Files.readAllBytes(new File("Keystore/secretKey").toPath());
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

        // Create the message, convert it to a byte array
        String message = "This is SE375 Systems Programming! :)";
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

        // Combine message with key
        byte[] messageWithKey = new byte[keyBytes.length + messageBytes.length];
        System.arraycopy(keyBytes, 0, messageWithKey, 0, keyBytes.length);
        System.arraycopy(messageBytes, 0, messageWithKey, keyBytes.length, messageBytes.length);
        System.out.println("keyli mesaj: "+new String(messageWithKey));

        // Create the message digest (hash) using the message + key combination
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hash = md.digest(messageWithKey);
        System.out.println("hash: "+new String(hash));


        // Append the hash to the message
        byte[] messageWithHash = new byte[messageBytes.length + hash.length];
        System.arraycopy(messageBytes, 0, messageWithHash, 0, messageBytes.length);
        System.arraycopy(hash, 0, messageWithHash, messageBytes.length, hash.length);
        System.out.println("haşli mesaj: "+new String(messageWithHash));

        // For debugging purposes. We can compare the received message with the sent message on the client side.
        // System.out.println(printBytes(messageWithHash));
        // System.out.println(printBytes(messageBytes));
        // System.out.println(printBytes(hash));

        // Send the message + hash to the server
        dout.write(messageWithHash);

        dout.close();
        din.close();
        s.close();
    }
}