import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

// This File is for 1 shared Lock
public class Main {

    public static void main(String[] args) throws InterruptedException {
        Scanner input = new Scanner(System.in);
        String choice;
        String januaryFilePath = "Datasets\\01-January.csv";
        String februaryFilePath = "Datasets\\02-February.csv";
        String marchFilePath = "Datasets\\03-March.csv";
        String aprilFilePath = "Datasets\\04-April.csv";
        String mayFilePath = "Datasets\\05-May.csv";
        String juneFilePath = "Datasets\\06-June.csv";
        String julyFilePath = "Datasets\\07-July.csv";
        String augustFilePath = "Datasets\\08-August.csv";
        String septemberFilePath = "Datasets\\09-September.csv";
        String octoberFilePath = "Datasets\\10-October.csv";
        String novemberFilePath = "Datasets\\11-November.csv";
        String decemberFilePath = "Datasets\\12-December.csv";
        String filePathArray[] = {januaryFilePath, februaryFilePath, marchFilePath, aprilFilePath, mayFilePath, juneFilePath, julyFilePath, augustFilePath, septemberFilePath, octoberFilePath, novemberFilePath, decemberFilePath};
        String monthNameArray[] = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
        String productNames[] = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M"};

        ReentrantLock lock = new ReentrantLock();
        ExecutorService service = Executors.newFixedThreadPool(10);

        //Shared Variables
        int[] sharedTotalSale = new int[3];// Index 0: In-Store sails, Index 1: Online sails, Index 2: In-Store + Online

        //Initialize sharedProductsTotalSale hashMap for avoiding null values
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


        long sum = 0;
        for (int j = 0; j < 100; j++) {
            long startTime = System.nanoTime();
            for (int i = 0; i < 12; i++) {
                service.execute(new CustomThread(i, filePathArray[i], monthNameArray[i], sharedTotalSale, sharedProductsTotalSale, sharedTotalSalesOfTheMonths, lock));

            }
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            sum += elapsedTime;


        }
        service.shutdown();
        try {
            if (!service.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
        }
        System.out.println("Threads are complete.");

        System.out.println("Elapsed time for part 1 : " + sum / 100);

    }

}

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
    private Lock lock;

    //Shared Variables
    private int[] sharedTotalSale;
    private HashMap<String, int[]> sharedProductsTotalSale;
    private HashMap<String, int[]> sharedTotalSalesOfTheMonths;


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
        calculateArrays();
        updateSharedProductTotalSale();
        sharedTotalSalesOfTheMonths();
    }

    public void read(String csvFile) {
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
        this.data = data;
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
        lock.lock();
        try {
            sharedTotalSale[0] += totalSale[0];
            sharedTotalSale[1] += totalSale[1];
            sharedTotalSale[2] += totalSale[2];

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void sharedTotalSalesOfTheMonths() {
        lock.lock();
        try {
            sharedTotalSalesOfTheMonths.put(monthName, totalSale);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void updateSharedProductTotalSale() {
        lock.lock();
        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void calculateArrays() {
        read(this.path);// this method also set the data variable(data[12][4])
        calculatePrice();
        calculateInStoreSails();
        calculateOnlineSails();
        calculateTotalSale();
    }

}




