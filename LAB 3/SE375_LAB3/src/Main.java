import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Scanner input = new Scanner(System.in);

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
        String monthNameArray[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String productArray[] = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M"};
        ArrayList<int[][]> sharedAllTotalSalesPerMonth = new ArrayList<>();
        ArrayList<ArrayList<int[][]>> sharedProductBasedAllTotalSalesPerMonth = new ArrayList<>();

        int[][] totalSalesForOneYear = new int[2][1];


        Thread[] myThreads; // New array of threads
        myThreads = new Thread[12]; // Same size as our int array

        for (int i = 0; i < 3; i++) {
            wThreads wt = new wThreads(i, filePathArray[i], monthNameArray[i], sharedAllTotalSalesPerMonth, sharedProductBasedAllTotalSalesPerMonth);
            // you don't need the second loop.
            myThreads[i] = new Thread(wt);
            wt.run(); // Spins up a new thread and runs your code
        }
        System.out.println("Which product do you want to search?");
        String choice = input.next();
        displayProductsTotalSale(sharedProductBasedAllTotalSalesPerMonth, getIndex(choice, productArray));

        printTotalSalesForYear(calculateTotalSalesForOneYear(sharedAllTotalSalesPerMonth));

    }


    public static int[][] calculateTotalSalesForOneYear(ArrayList<int[][]> allTotalSalesPerMonth) {

        int[][] totalSalesForYear = new int[2][1];
        totalSalesForYear[0][0] = 0;
        totalSalesForYear[1][0] = 0;

        for (int[][] sales : allTotalSalesPerMonth) {
            totalSalesForYear[0][0] += sales[0][0];
            totalSalesForYear[1][0] += sales[1][0];
        }
        return totalSalesForYear;
    }

    public static void displayProductsTotalSale(ArrayList<ArrayList<int[][]>> productBasedAllTotalSales, int index) {
        String productArray[] = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M"};
        System.out.println("For Product " + productArray[index]);

        int[][] totalSalesForYear = new int[2][1];
        totalSalesForYear[0][0] = 0;
        totalSalesForYear[1][0] = 0;


        for (int j = 0; j < productBasedAllTotalSales.size(); j++) {
            totalSalesForYear[0][0] += productBasedAllTotalSales.get(j).get(index)[0][0];
            totalSalesForYear[1][0] += productBasedAllTotalSales.get(j).get(index)[1][0];
        }


        System.out.println("in store: " + totalSalesForYear[0][0]);
        System.out.println("online: " + totalSalesForYear[1][0]);
        System.out.println("Total Sale: " + (+totalSalesForYear[0][0] + totalSalesForYear[1][0]));

    }


    public static int getIndex(String productName, String[] productArr) {
        for (int i = 0; i < productArr.length; i++) {
            if (productName.equals(productArr[i])) {
                return i;
            }
        }
        return 0;
    }

    public static void printTotalSalesForYear(int[][] totalSalesForYear) {
        System.out.println("\n\n\n\t\t\t\tTOTAL SALES FOR YEAR");
        System.out.println("In-Store: " + totalSalesForYear[0][0] + " TL");
        System.out.println("Online: : " + totalSalesForYear[1][0] + " TL");
        System.out.println("TOTAL SALE(In-Store+Online): " + (totalSalesForYear[0][0] + totalSalesForYear[1][0]) + " TL");
    }
}




