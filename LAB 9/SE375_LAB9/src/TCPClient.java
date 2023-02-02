import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

class TCPClient {
    public static void main(String argv[]) throws Exception {

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket("localhost", 6789);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


        //GET URLs FROM SERVER
        String URLpathsAsStr;
        URLpathsAsStr = inFromServer.readLine();
        System.out.println("FROM SERVER: " + URLpathsAsStr);
        URLpathsAsStr = URLpathsAsStr.replaceAll("\\[", "").replaceAll("]", "");
        String URLPathsArray[] = URLpathsAsStr.split(", ");
        //***********************************************************


        //CREATING THREADS AND PROCESSING THE FILES
        Thread[] threads = new Thread[12];
        ReentrantLock lock = new ReentrantLock();

        String monthNameArray[] = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
        String productNames[] = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M"};
        int[] sharedTotalSale = new int[3];// Index 0: In-Store sails, Index 1: Online sails, Index 2: In-Store + Online

        HashMap<String, int[]> sharedProductsTotalSale = new HashMap<>();
        int[] demo = {0, 0, 0};
        for (int i = 0; i < 12; i++) {
            sharedProductsTotalSale.put(productNames[i], demo);
        }

        //Initialize sharedTotalSalesOfTheMonths hashMap for avoiding null values
        HashMap<String, int[]> sharedTotalSalesOfTheMonths = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            sharedTotalSalesOfTheMonths.put(monthNameArray[i], demo);
        }

        for (int i = 0; i < 12; i++) {
            Thread thread = new Thread(new CustomThread(i, URLPathsArray[i], monthNameArray[i], sharedTotalSale, sharedProductsTotalSale, sharedTotalSalesOfTheMonths, lock));
            threads[i] = thread;
            threads[i].start();
        }
        for (int i = 0; i < 12; i++) {
            threads[i].join();
        }
        System.out.println("Threads are complete.");
        //***********************************************************

        //SEND THE RESULTS TO THE SERVER
        //send Super Store to Server
        outToServer.writeBytes(Arrays.toString(sharedTotalSale) + '\n');


        // send Per Product Sales to Server
        //send the number of query needed
        outToServer.writeBytes(Integer.toString(sharedProductsTotalSale.size()) + '\n');

        //send every product data to the server
        for (int i = 0; i < sharedProductsTotalSale.size(); i++) {
            outToServer.writeBytes(Arrays.toString(sharedProductsTotalSale.get(productNames[i])) + "\n");
        }

        clientSocket.close();
    }


}