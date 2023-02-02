
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class Lab11_Server {

    // Code used in class, writes a byte array to a file.
    public static void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    // Code used in class, prints a byte array onto the console.
    public static String printBytes(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X:", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        // Generate the secret key, convert it to a byte array & write it to a file
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom();
        keyGen.init(256, secureRandom);
        SecretKey secretKey = keyGen.generateKey();

        byte[] keyBytes = secretKey.getEncoded();
        writeToFile("Keystore/secretKey", keyBytes);

        // Initializing a socket, waiting to connect to a client
        System.out.println("The server waiting to be connected...");
        ServerSocket ss = new ServerSocket(6789);
        Socket s = ss.accept();

        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        // Read data from client
        byte[] received_bytes = din.readAllBytes();
        System.out.println("received bytes: "+new String(received_bytes));

        // Separate hash from the message
        byte[] received_hash = new byte[20];
        byte[] received_msg = new byte[received_bytes.length - 20];
        System.arraycopy(received_bytes, 0, received_msg, 0, received_msg.length);
        System.arraycopy(received_bytes, received_bytes.length - 20, received_hash, 0, received_hash.length);
        System.out.println("hashsiz mesaj: "+new String(received_msg));
        System.out.println("hash: "+new String(received_bytes));

        // For debugging purposes. We can compare the received message with the sent message on the client side.
        // System.out.println(printBytes(received_hash));
        // System.out.println(printBytes(received_msg));
        // System.out.println(printBytes(received_bytes));

        // Combine message with key
        byte[] messageWithKey = new byte[keyBytes.length + received_msg.length];
        System.arraycopy(keyBytes, 0, messageWithKey, 0, keyBytes.length);
        System.arraycopy(received_msg, 0, messageWithKey, keyBytes.length, received_msg.length);
        System.out.println(new String(received_bytes));
        System.out.println(new String(messageWithKey));
        System.out.println(new String(keyBytes));
        System.out.println(new String(received_msg));


        // Create the message digest (hash) using the message + key combination
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hash = md.digest(messageWithKey);

        // Compare the computed hash with the received hash. If they're equal, the message is authentic.
        if (Arrays.equals(hash, received_hash)) {
            System.out.println("The message is authentic!!");
            System.out.println("Received message: " + new String(received_msg));
        } else
            System.out.println("The hash comparison failed! The message is not authentic...");


        dout.close();
        din.close();
        s.close();
        ss.close();
    }
}