import java.io.*;
import java.net.*;
import java.util.Arrays;

class UDPClient {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 12; i++) {
            String data[][] = new String[12][4];
            int[] price = new int[12];
            int[] onlineSails = new int[12];
            int[] inStoreSails = new int[12];
            int[] totalSale = new int[3];// Index 0: In-Store sails, Index 1: Online sails, Index 2: In-Store + Online

            DatagramSocket datagramSocket = new DatagramSocket();
            InetAddress inetAddress = InetAddress.getByName("localhost");
            System.out.println(inetAddress);
            DatagramPacket request = new DatagramPacket(new byte[1], 1, inetAddress, 7);
            datagramSocket.send(request);

            byte[] receivedPath = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(receivedPath, receivedPath.length);
            datagramSocket.receive(datagramPacket);

            //get the path
            String path = new String(datagramPacket.getData());
            path = path.trim();
            System.out.println("result: " + path);
            //******************************************

            //Calculate data
            data = read(path);
            price = calculatePrice(data);
            inStoreSails = calculateInStoreSails(data);
            onlineSails = calculateOnlineSails(data);
            totalSale = calculateTotalSale(inStoreSails, onlineSails, price);
            //******************************************

            System.out.println(totalSale[0] + " " + totalSale[1] + " " + totalSale[2]);

            //send total sale to the server
            byte msg[] = (Arrays.toString(totalSale).getBytes());
            DatagramPacket datagramPacket1 = new DatagramPacket(msg, msg.length, inetAddress, datagramPacket.getPort());
            datagramSocket.send(datagramPacket1);
            //******************************************
            datagramSocket.close();
        }


    }

    public static String[][] read(String path) throws IOException {
        URL url = new URL("http://homes.ieu.edu.tr/culudagli/files/SE375/" + path);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        InputStream in = httpCon.getInputStream();
        OutputStream out = new FileOutputStream("file.dat");
        out = new BufferedOutputStream(out);
        byte[] buf = new byte[8192];
        int len = 0;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        out.close();
        String data[][] = new String[12][4];
        String delimiter = ",";
        int count = 0; // For skipping the first line
        int count2 = 0; // multi-dimensional arrays Row (Up to 4)
        int count3 = -1; // multi-dimensional arrays Column (Up to 12) it starts from -1 because First line is ignored

        try {
            File file = new File("C:\\Users\\Berkay\\Desktop\\SE375_LAB8\\file.dat");
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

    public static int[] calculateInStoreSails(String data[][]) {
        int[] inStoreSails = new int[12];
        for (int i = 0; i < 12; i++) {
            inStoreSails[i] = Integer.parseInt(data[i][2]);
        }
        return inStoreSails;
    }

    public static int[] calculateOnlineSails(String data[][]) {
        int[] onlineSails = new int[12];
        for (int i = 0; i < 12; i++) {
            onlineSails[i] = Integer.parseInt(data[i][3]);
        }
        return onlineSails;
    }

    public static int[] calculateTotalSale(int[] inStoreSails, int[] onlineSails, int[] price) {
        int[] totalSale = new int[3];

        totalSale[0] = 0;
        for (int i = 0; i < 12; i++) {
            totalSale[0] += inStoreSails[i] * price[i];
        }
        for (int i = 0; i < 12; i++) {
            totalSale[1] += onlineSails[i] * price[i];
        }

        totalSale[2] += totalSale[0] + totalSale[1];
        return totalSale;
    }

}


