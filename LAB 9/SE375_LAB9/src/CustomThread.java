import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class CustomThread implements Runnable {

    private final int i; //Thread's id
    private final String path;//Thread's path
    private final String monthName;//Thread's corresponding month

    //Local Data's
    private String data[][] = new String[12][4];
    private int[] price = new int[12];
    private int[] onlineSails = new int[12];
    private int[] inStoreSails = new int[12];
    private int[] totalSale = new int[3];// Index 0: In-Store sails, Index 1: Online sails, Index 2: In-Store + Online

    //Shared Variables
    private int[] sharedTotalSale;
    private HashMap<String, int[]> sharedProductsTotalSale;
    private HashMap<String, int[]> sharedTotalSalesOfTheMonths;
    private Lock lock;


    public CustomThread(int i, String path, String monthName, int[] sharedTotalSale, HashMap<String, int[]> sharedProductsTotalSale, HashMap<String, int[]> sharedTotalSalesOfTheMonths, ReentrantLock reentrantLock) {
        this.i = i;
        this.path = path;
        this.monthName = monthName;
        this.sharedTotalSale = sharedTotalSale;
        this.sharedProductsTotalSale = sharedProductsTotalSale;
        this.sharedTotalSalesOfTheMonths = sharedTotalSalesOfTheMonths;
        this.lock = reentrantLock;

    }

    @Override
    public void run() {
        System.out.println("Thread Parsing " + this.path);
        read();
        calculateArrays();
        updateSharedTotalSale();
        updateSharedProductTotalSale();
        sharedTotalSalesOfTheMonths();
    }

    public void read() {

        try {
            lock.lock();

            String data[][] = new String[12][4];
            String delimiter = ",";
            int count = 0; // For skipping the first line
            int count2 = 0; // multi-dimensional arrays Row (Up to 4)
            int count3 = -1; // multi-dimensional arrays Column (Up to 12) it starts from -1 because First line is ignored

            URL url = new URL("http://homes.ieu.edu.tr/culudagli/files/SE375/" + this.path);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));

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
            this.data = data;

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            lock.unlock();
        }


    }

    public void calculatePrice() {
        for (int i = 0; i < 12; i++) {
            price[i] = Integer.parseInt(data[i][1]);
        }
    }

    public void calculateInStoreSails() {
        for (int i = 0; i < 12; i++) {
            inStoreSails[i] = Integer.parseInt(data[i][2]);
        }
    }

    public void calculateOnlineSails() {
        for (int i = 0; i < 12; i++) {
            onlineSails[i] = Integer.parseInt(data[i][3]);
        }
    }

    public void calculateTotalSale() {
        totalSale[0] = 0;
        for (int i = 0; i < 12; i++) {
            totalSale[0] += inStoreSails[i] * price[i];
        }
        for (int i = 0; i < 12; i++) {
            totalSale[1] += onlineSails[i] * price[i];
        }

        totalSale[2] += totalSale[0] + totalSale[1];
    }

    // This method calculates the total sale of the all product for 1 year. It updates the shared variable.
    // Shared Variable is an array[3] (Index 0: In-Store sails, Index 1: Online sails, Index 2: In-Store + Online)
    // This Method Has to be synchronized. However, if I use HashTable, I can use it without synchronized.
    //HashTable is thread-safe collection but HashMap is not.
    public void updateSharedTotalSale() {
        synchronized (sharedTotalSale) {
            sharedTotalSale[0] += totalSale[0];
            sharedTotalSale[1] += totalSale[1];
            sharedTotalSale[2] += totalSale[2];
        }
    }

    public void sharedTotalSalesOfTheMonths() {
        synchronized (sharedTotalSalesOfTheMonths) {
            sharedTotalSalesOfTheMonths.put(monthName, totalSale);
        }
    }

    public synchronized void updateSharedProductTotalSale() {
        synchronized (sharedProductsTotalSale) {
            for (int i = 0; i < 12; i++) {
                int[] productTotalSale = new int[3];

                //Calculating the Products total sale array
                productTotalSale[0] = Integer.parseInt(data[i][1]) * Integer.parseInt(data[i][2]);
                productTotalSale[1] = Integer.parseInt(data[i][1]) * Integer.parseInt(data[i][3]);
                productTotalSale[2] = productTotalSale[0] + productTotalSale[1];

                //Fetching the product's sale data and adding into product array.
                productTotalSale[0] += sharedProductsTotalSale.get(data[i][0])[0];
                productTotalSale[1] += sharedProductsTotalSale.get(data[i][0])[1];
                productTotalSale[2] += sharedProductsTotalSale.get(data[i][0])[2];

                sharedProductsTotalSale.put(data[i][0], productTotalSale);
            }
        }
    }

    public void calculateArrays() {
       // this method also set the data variable(data[12][4])
        calculatePrice();
        calculateInStoreSails();
        calculateOnlineSails();
        calculateTotalSale();
    }

}