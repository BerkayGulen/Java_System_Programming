import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

public class Server {
    public static void main(String[] args) throws IOException {

        //Parse and Calculate AVG
        String[][] data = read("01-January.csv");
        int[] price = calculatePrice(data);
        double avg = 0;
        for (int i : price) {
            avg += i;
        }
        avg = avg / price.length;


        //Create Server Socket
        ServerSocket welcomeSocket = new ServerSocket(6789);


        System.out.println("Server is up");
        while (!welcomeSocket.isClosed()) {
            System.out.println("Waiting for connections...");
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Client connected!!!");

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            outToClient.writeBytes(avg + "\n");

            String successMsg = inFromClient.readLine();
            System.out.println(successMsg);

            if (successMsg.equals("success")) {
                welcomeSocket.close();
            }

        }


    }

    public static String[][] read(String csvFile) {
        String data[][] = new String[12][4];
        String delimiter = ",";
        int count = 0; // For skipping the first line
        int count2 = 0; // multi-dimensional arrays Row (Up to 4)
        int count3 = -1; // multi-dimensional arrays Column (Up to 12) it starts from -1 because First line is ignored

        try {
            File file = new File(csvFile);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = " ";
            String[] tempArr;

            while ((line = br.readLine()) != null) {
                tempArr = line.split(delimiter);
                if (count >= 1) {
                    for (String tempStr : tempArr) {
                        data[count3][count2] = tempStr;
                        count2++;
                    }
                    count2 = 0;
                }
                count++;
                count3++;
                if (count3 == 12) count3 = 0;
            }

            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return data;
    }

    public static int[] calculatePrice(String[][] data) {
        int[] price = new int[12];

        for (int i = 0; i < 12; i++) {
            price[i] = Integer.parseInt(data[i][1]);
        }
        return price;
    }


}

