import java.io.*;
import java.net.*;
import java.util.*;

class TCPServer {
    public static void main(String argv[]) throws Exception {
        Scanner input = new Scanner(System.in);
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
        String[] productNames = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M"};

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

        ServerSocket welcomeSocket = new ServerSocket(6789);
        System.out.println("Server is up");
        while (!welcomeSocket.isClosed()) {
            System.out.println("Waiting for connections...");
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Client connected!!!");

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            outToClient.writeBytes(filePathArray+"\n");

            //GET THE Super Store RESULT, CONVERT IT AND DISPLAY
            String superStoreResult = inFromClient.readLine();
            sharedTotalSale = convert(superStoreResult);

            System.out.println("********************** Total Sale For Year **********************");
            System.out.println("In-Store: " + sharedTotalSale[0]);
            System.out.println("Online: : " + sharedTotalSale[1]);
            System.out.println("Total: : " + sharedTotalSale[2]);
            //***********************************************************

            //GET THE Per Product Sales RESULT, CONVERT IT AND DISPLAY
            String numberOfQuery = inFromClient.readLine();
            System.out.println(numberOfQuery);

            HashMap<String, int[]> sharedProductsTotalSale = new HashMap<>();
            for (int i =0;i<Integer.parseInt(numberOfQuery);i++){
                 String result = inFromClient.readLine();
                 sharedProductsTotalSale.put(productNames[i],convert(result));
            }
            while (true){
                System.out.println("Which product do you want to search?");
                System.out.print("Product: ");
                String choice = input.next().toUpperCase(Locale.ROOT);
                if (choice.equals("-1")) break;
                System.out.println("In-Store: "+sharedProductsTotalSale.get(choice)[0]);
                System.out.println("Online: "+sharedProductsTotalSale.get(choice)[1]);
                System.out.println("Total: "+sharedProductsTotalSale.get(choice)[2]);
            }
            connectionSocket.close();
            welcomeSocket.close();






        }
    }
    public static int[] convert(String s){
        int[] totalSale = new int[3];// Index 0: In-Store sails, Index 1: Online sails, Index 2: In-Store + Online

        s = s.replaceAll("\\p{P}", "");
        String[] parsedResult = s.split(" ");

        for (int i = 0; i < totalSale.length; i++) {
            totalSale[i] = Integer.parseInt(parsedResult[i]);
        }

        return totalSale;
    }
}