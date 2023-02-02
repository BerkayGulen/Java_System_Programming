import java.net.StandardSocketOptions;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Scanner input = new Scanner(System.in);
        String choice;
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
        String filePathArray[] = {januaryFilePath, februaryFilePath, marchFilePath, aprilFilePath, mayFilePath, juneFilePath, julyFilePath, augustFilePath, septemberFilePath, octoberFilePath, novemberFilePath, decemberFilePath};
        String monthNameArray[] = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
        String productNames[] = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M"};


        ExecutorService service = Executors.newFixedThreadPool(10);
        ReentrantLock lock = new ReentrantLock();


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
        //Aşağıdaki 1 satır Lab 2 ve 3 için.
        //Thread[] threads = new Thread[12];
        for (int i = 0; i < 12; i++) {
            service.execute(new CustomThread(i, filePathArray[i], monthNameArray[i], sharedTotalSale, sharedProductsTotalSale, sharedTotalSalesOfTheMonths,lock));

            // Aşağıdaki 3 satır Lab 2 ve 3 için.
//            Thread thread = new Thread(new CustomThread(i, filePathArray[i], monthNameArray[i],sharedTotalSale,sharedProductsTotalSale));
//            threads[i] = thread;
//            threads[i].start();
        }
        //Aşağıdaki for döngüsü lab 2 ve 3 için.
//        for (int i = 0; i < 12; i++) {
//            threads[i].join();
//        }
        service.shutdown();
        try {
            if (!service.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
        }

        System.out.println("Threads are complete.");


        //LAB 4 PART 2
//        while (true){
//            System.out.println("Which product do you want to search?");
//            System.out.print("Choice: ");
//            choice = input.next().toUpperCase(Locale.ROOT);
//            if (choice.equals("-1")) break;
//            System.out.println("In-Store: "+sharedProductsTotalSale.get(choice)[0]);
//            System.out.println("Online: "+sharedProductsTotalSale.get(choice)[1]);
//            System.out.println("Total: "+sharedProductsTotalSale.get(choice)[2]);
//        }

        //LAB 4 PART 1
        while (true) {
            System.out.print("Enter Month Name: ");
            choice = input.next().toLowerCase(Locale.ROOT);
            if (choice.equals("-1")) break;

            System.out.println("In-Store: " + sharedTotalSalesOfTheMonths.get(choice)[0]);
            System.out.println("Online: " + sharedTotalSalesOfTheMonths.get(choice)[1]);
            System.out.println("Total: " + sharedTotalSalesOfTheMonths.get(choice)[2]);
        }

        System.out.println("********************** Total Sale For Year **********************");
        System.out.println("In-Store: " + sharedTotalSale[0]);
        System.out.println("Online: : " + sharedTotalSale[1]);
        System.out.println("Total: : " + sharedTotalSale[2]);

    }

}




