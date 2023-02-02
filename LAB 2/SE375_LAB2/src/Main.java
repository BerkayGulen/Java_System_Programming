import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws InterruptedException {

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

        ArrayList<int[][]> sharedAllTotalSalesPerMonth = new ArrayList<>();
        int[][] totalSalesForOneYear = new int[2][1];


        Thread[] myThreads; // New array of threads
        myThreads = new Thread[12]; // Same size as our int array

        for (int i = 0; i < 12; i++) {
            wThreads wt = new wThreads(i, filePathArray[i], monthNameArray[i],sharedAllTotalSalesPerMonth);
            // you don't need the second loop.
            myThreads[i] = new Thread(wt);
            wt.run(); // Spins up a new thread and runs your code
        }
        totalSalesForOneYear = calculateTotalSalesForOneYear(sharedAllTotalSalesPerMonth);
        printTotalSalesForYear(totalSalesForOneYear);
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

    public static void printTotalSalesForYear(int[][] totalSalesForYear) {
        System.out.println("\n\n\n\t\t\t\tTOTAL SALES FOR YEAR");
        System.out.println("In-Store: " + totalSalesForYear[0][0] + " TL");
        System.out.println("Online: : " + totalSalesForYear[1][0] + " TL");
        System.out.println("TOTAL SALE(In-Store+Online): " + (totalSalesForYear[0][0] + totalSalesForYear[1][0]) + " TL");
    }
}




