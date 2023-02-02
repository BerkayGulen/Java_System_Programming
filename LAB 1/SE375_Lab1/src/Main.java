import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // write your code here
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

        ArrayList<int[][]> allTotalSalesPerMonth = new ArrayList<>();

        String csvFile = januaryFilePath;
        String data[][] = new String[12][4];
        int[][] amountArray = new int[2][12];
        int[][] priceArray = new int[12][1];
        int[][] totalSales = new int[2][1];
        int[][] totalSalesForYear = new int[2][1];

        data = read(csvFile);
        //amountArray = calculateAmountsArray(data);
        //priceArray = calculatePriceArray(data);
        //totalSales = calculateTotalSales(amountArray, priceArray);

        allTotalSalesPerMonth = calculateAllTotalSalesPerMonth(filePathArray);
        totalSalesForYear = calculateTotalSalesForOneYear(allTotalSalesPerMonth);
        printTotalSalesForAllMonths(allTotalSalesPerMonth);
        printTotalSalesForYear(totalSalesForYear);
        calculateAndPrintAllProductBasedTotalSales(filePathArray);
        printTotalSalesForYear(calculateTotalSalesForOneYear(allTotalSalesPerMonth));


    }

    public static String[][] read(String csvFile) {
        String data[][] = new String[12][4];
        String delimiter = ",";
        int count = 0;
        int count2 = 0;
        int count3 = -1;

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
                        //System.out.print(tempStr + " ");
                        //System.out.println();
                        data[count3][count2] = tempStr;
                        count2++;
                    }
                    count2 = 0;
                    //System.out.println();
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

    public static int[][] calculateAmountsArray(String[][] data) {
        int count = 2;
        int amountArray[][] = new int[2][12];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 12; j++) {
                amountArray[i][j] = Integer.parseInt(data[j][count]);
            }
            count++;
        }
        return amountArray;
    }

    public static int[][] calculatePriceArray(String[][] data) {

        int priceArray[][] = new int[12][1];
        for (int i = 0; i < 12; i++) {
            priceArray[i][0] = Integer.parseInt(data[i][1]);
        }


        return priceArray;
    }




    public static int[][] calculateTotalSales(int[][] amountArray, int[][] priceArray) {
        int totalSalesArray[][] = new int[2][1];

        // Multiply the two matrices
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 1; j++) {
                for (int k = 0; k < 12; k++)
                    totalSalesArray[i][j] += amountArray[i][k] * priceArray[k][j];
            }
        }
        return totalSalesArray;
    }

    public static void printTotalSalesArray(int[][] totalSales) {
//        for (int i = 0; i < 2; i++) {
//            System.out.println(totalSales[i][0]);
//        }
        System.out.println("In-Store: " + totalSales[0][0] + " TL");
        System.out.println("Online: : " + totalSales[1][0] + " TL");

    }

    public static ArrayList<int[][]> calculateAllTotalSalesPerMonth(String filePathArray[]) {
        ArrayList<int[][]> allTotalSalesPerMonth = new ArrayList<>();
        String data[][] = new String[12][4];
        int[][] amountArray = new int[2][12];
        int[][] priceArray = new int[12][1];
        int[][] totalSales = new int[2][1];

        for (String filePath : filePathArray) {
            data = read(filePath);
            amountArray = calculateAmountsArray(data);
            priceArray = calculatePriceArray(data);
            totalSales = calculateTotalSales(amountArray, priceArray);
            allTotalSalesPerMonth.add(totalSales);
        }
        return allTotalSalesPerMonth;
    }

    public static void printTotalSalesForAllMonths(ArrayList<int[][]> allTotalSalesPerMonth) {
        String monthNameArray[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        System.out.println("\n\n\n\t\t\t\tTOTAL ONLINE AND IN-STORE SALES FOR ALL MONTHS");
        for (int i = 0; i < allTotalSalesPerMonth.size(); i++) {
            System.out.println(monthNameArray[i]);
            printTotalSalesArray(allTotalSalesPerMonth.get(i));
            System.out.println();
        }
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

    public static void printTotalSalesForYear(int[][] totalSalesForYear){
        System.out.println("\n\n\n\t\t\t\tTOTAL SALES FOR YEAR");
        System.out.println("In-Store: " + totalSalesForYear[0][0] + " TL");
        System.out.println("Online: : " + totalSalesForYear[1][0] + " TL");
        System.out.println("TOTAL SALE(In-Store+Online): "+(totalSalesForYear[0][0] + totalSalesForYear[1][0])+" TL");
    }

    public static void calculateAndPrintAllProductBasedTotalSales(String filePathArray[]) {
        int productBasedTotalSaleForYear[][] = new int[2][1];
        int count = 0; // for incrementing the rows (A,B,C...)

        String data[][] = new String[12][4];
        System.out.println("\n\n\n\t\t\t\tPRODUCT BASED TOTAL SALES FOR YEAR");
        for (int i = 0; i < 12; i++) {
            productBasedTotalSaleForYear[0][0] = 0;
            productBasedTotalSaleForYear[1][0] = 0;
            for (String filePath : filePathArray) {
                data = read(filePath);
                productBasedTotalSaleForYear[0][0] += Integer.parseInt(data[0][1]) * Integer.parseInt(data[count][2]); // in store 2. element (price * in store)
                productBasedTotalSaleForYear[1][0] += Integer.parseInt(data[0][1]) * Integer.parseInt(data[count][3]); // (price * Online)
            }
            System.out.println(data[i][0]);
            System.out.println("In-Store: " + productBasedTotalSaleForYear[0][0] + " TL");
            System.out.println("Online: : " + productBasedTotalSaleForYear[1][0] + " TL");
            count++;
        }
    }

    public static void printAmountArray(int[][] amountArray) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 12; j++) {
                System.out.print(amountArray[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void printData(String data[][]) {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }

    }

    public static void printPriceArray(int[][] priceArray) {
        for (int i = 0; i < 12; i++) {
            System.out.println(priceArray[i][0]);
        }
    }


}





