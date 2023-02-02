import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class UDPServer {
    public static void main(String[] args) throws IOException {
        String januaryFilePath = "datasets/01-January.txt";
        String februaryFilePath = "datasets/02-February.txt";
        String marchFilePath = "datasets//03-March.txt";
        String aprilFilePath = "datasets/04-April.txt";
        String mayFilePath = "datasets/05-May.txt";
        String juneFilePath = "datasets/06-June.txt";
        String julyFilePath = "datasets/07-July.txt";
        String augustFilePath = "datasets/08-August.txt";
        String septemberFilePath = "datasets/09-September.txt";
        String octoberFilePath = "datasets/10-October.txt";
        String novemberFilePath = "datasets/11-November.txt";
        String decemberFilePath = "datasets/12-December.txt";
        Random random = new Random();
        int[] sharedTotalSale = new int[3];// Index 0: In-Store sails, Index 1: Online sails, Index 2: In-Store + Online

        ArrayList<String> filePathArray = new ArrayList<>() {
            {
                add(januaryFilePath);
                add(februaryFilePath);
                add(marchFilePath);
                add(aprilFilePath);
                add(mayFilePath);
                add(juneFilePath);
                add(julyFilePath);
                add(augustFilePath);
                add(septemberFilePath);
                add(octoberFilePath);
                add(novemberFilePath);
                add(decemberFilePath);
            }
        };
        System.out.println("server is up");

        while (filePathArray.size() != 0) {
            System.out.println("Server is waiting for connection...");

            //packet received from client for connection and getting the port address
            DatagramSocket datagramSocket = new DatagramSocket(7);
            byte[] b1 = new byte[150];//received message to get port address
            DatagramPacket request = new DatagramPacket(new byte[1], 1); // for connection
                datagramSocket.receive(request); //client is connected

            //******************************************


            //client is connected so preparing the message to be sent and send it
            System.out.println("Client is connected!!!");
            int index = random.nextInt(filePathArray.size());//get random Path
            byte[] path = filePathArray.get(index).getBytes(); // message to be sent
            InetAddress inetAddress = InetAddress.getLocalHost();
            DatagramPacket datagramPacket1 = new DatagramPacket(path, path.length, inetAddress, request.getPort());
            datagramSocket.send(datagramPacket1);
            //******************************************

            filePathArray.remove(index);

            //get the total sale from client and add to the array
            byte b2[] = new byte[1024];
            DatagramPacket result = new DatagramPacket(b2, b2.length); // for connection
            datagramSocket.receive(result); //client is connected

            String msg = new String(result.getData(), 0, result.getLength());
            msg = msg.replaceAll("\\p{P}", "");
            String[] parsedMsg = msg.split(" ");
            for (int i = 0; i < sharedTotalSale.length; i++) {
                sharedTotalSale[i] += Integer.parseInt(parsedMsg[i]);
            }
            System.out.println("********************** Total Sale For Year **********************");
            System.out.println("In-Store: " + sharedTotalSale[0]);
            System.out.println("Online: : " + sharedTotalSale[1]);
            System.out.println("Total: : " + sharedTotalSale[2]);
            datagramSocket.close();
        }


    }
}

