import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class wThreads implements Runnable {

    private final int i;
    private final String path;
    private final String monthName;
    private String data[][] = new String[12][4];
    private int[][] amountArray = new int[2][12];
    private int[][] priceArray = new int[12][1];
    private int[][] totalSales = new int[2][1];
    private ArrayList<int[][]> sharedAllTotalSalesPerMonth;

    public wThreads(int i, String path, String monthName,ArrayList<int[][]> sharedAllTotalSalesPerMonth ) {
        this.i = i;
        this.path = path;
        this.monthName = monthName;
        this.sharedAllTotalSalesPerMonth = sharedAllTotalSalesPerMonth;
    }

    //Set member values to 1
    @Override
    public void run() {

        data = read(this.path);
        amountArray = calculateAmountsArray(data);
        priceArray = calculatePriceArray(data);
        totalSales = calculateTotalSales(amountArray, priceArray);
        System.out.println(this.monthName);
        printTotalSalesArray(totalSales);
        System.out.println();
        sharedAllTotalSalesPerMonth.add(totalSales);

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


    public static int[][] calculateAmountsArray(String[][] data) {
        int count = 2; // it starts from 2 because In-Store purchases is on the second position in the .csv file
        int amountArray[][] = new int[2][12];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 12; j++) {
                amountArray[i][j] = Integer.parseInt(data[j][count]);
            }
            count++; // we increment the count by 1 because Online purchases is on the third position in the .csv file
        }
        return amountArray;
    }

    public static int[][] calculatePriceArray(String[][] data) {
        //Price is on the first position in the .csv file
        int priceArray[][] = new int[12][1];
        for (int i = 0; i < 12; i++) {
            priceArray[i][0] = Integer.parseInt(data[i][1]);
        }
        return priceArray;
    }


    // it returns [2][1] array, and it contains total sales Of the month
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

    //This method calculates Total Sales for each month and store into ArrayList and returns the ArrayList. (it uses the 'calculateTotalSales' method for finding total sales of the month)
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

    //This method takes the allTotalSalesPerMonth as a parameter and sums the rows. it returns [2][1] array, and it contains Total Sales For in store[0][0] and total sales online[1][0]
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