import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

class BenchmarkSorts {

    private YourSort yourSort = new YourSort();
    private int[][][] listOfDataSetsForIterativeTest = new int[10][50][];
    private int[][][] listOfDataSetsForRecursiveTest = new int[10][50][];
    private int[][][] listOfDataSetsForWarmUp = new int[10][50][];
    private int[][][] listOfDataSetsForWarmUp2 = new int[10][50][];
    private long [][] iterativeBenchmarkTimes = new long[10][50];
    private long [][] recursiveBenchmarkTimes = new long[10][50];

    public BenchmarkSorts(int[] sizes)  {

        // populate 50 int lists for each of the 10 sizes
        for (int i = 0; i < 10; i++)    {
            for (int j = 0; j < 50; j++)    {
                listOfDataSetsForIterativeTest[i][j] = createDataSet(sizes[i]);
            }
        }

        // copy the values in the randomly generated list of lists into another list of lists
        // for the recursive test
        for (int i = 0; i < 10; i++)    {
            System.arraycopy(listOfDataSetsForIterativeTest[i], 0, listOfDataSetsForRecursiveTest[i], 0, 50);
        }

        // copy the values in the randomly generated list of lists into another list of lists
        // for the pre-benchmark run of the algorithms
        for (int i = 0; i < 10; i++)    {
            System.arraycopy(listOfDataSetsForIterativeTest[i], 0, listOfDataSetsForWarmUp[i], 0, 50);
        }

        // copy the values in the randomly generated list of lists into another list of lists
        // for the pre-benchmark run of the algorithms
        for (int i = 0; i < 10; i++)    {
            System.arraycopy(listOfDataSetsForIterativeTest[i], 0, listOfDataSetsForWarmUp2[i], 0, 50);
        }
    }

    // executes the sort processes for the two versions of the algorithm
    public void runSorts() throws UnsortedException {

        // run the sorting algorithms through twice to warm up the JVM
        // set isWarmUp to true to avoid incrementing benchmark counters
        for (int i = 0; i < 10; i++)    {
            for (int j = 0; j < 50; j++)    {
                yourSort.iterativeSort(listOfDataSetsForWarmUp[i][j], true);
                verifyNumericOrderOfList(listOfDataSetsForWarmUp[i][j]);
            }
        }

        for (int i = 0; i < 10; i++)    {
            for (int j = 0; j < 50; j++)    {
                yourSort.recursiveSort(listOfDataSetsForWarmUp2[i][j], true);
                verifyNumericOrderOfList(listOfDataSetsForWarmUp[i][j]);
            }
        }

        // run the first list of lists through the iterative algorithm
        for (int i = 0; i < 10; i++)    {
            for (int j = 0; j < 50; j++)    {
                iterativeBenchmarkTimes[i][j] = System.nanoTime();
                yourSort.iterativeSort(listOfDataSetsForIterativeTest[i][j], false);
                iterativeBenchmarkTimes[i][j] = System.nanoTime() - iterativeBenchmarkTimes[i][j];
                verifyNumericOrderOfList(listOfDataSetsForIterativeTest[i][j]);
            }
        }

        // run the second list of lists through the recursive algorithm
        for (int i = 0; i < 10; i++)    {
            for (int j = 0; j < 50; j++)    {
                recursiveBenchmarkTimes[i][j] = System.nanoTime();
                yourSort.recursiveSort(listOfDataSetsForRecursiveTest[i][j], false);
                recursiveBenchmarkTimes[i][j] = System.nanoTime() - recursiveBenchmarkTimes[i][j];
                verifyNumericOrderOfList(listOfDataSetsForRecursiveTest[i][j]);
            }
        }
    }

    public void displayReport()  throws IOException {

        FileWriter fileWriter = new FileWriter("output.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.println("---Iterative Version---");
        printWriter.println();

        for (int i = 0; i < 10; i++)    {
            printWriter.println("Dataset size: " + listOfDataSetsForIterativeTest[i][0].length);
            printWriter.println("Average runtime: " + getAverageExecutionTime(iterativeBenchmarkTimes[i]) + " nanoseconds.");
            printWriter.println("Coefficient of variance of runtime: " + getCoefficientOfVariance(iterativeBenchmarkTimes[i]));
            printWriter.println("Average critical op count: " + getAverageCriticalExecutionCount(yourSort.getCountIterative(i)));
            printWriter.println("Coefficient of variance of count: " + getCoefficientOfVariance(yourSort.getCountIterative(i)));
            printWriter.println("");
        }

        printWriter.println();
        printWriter.println();

        printWriter.println("---Recursive Version---");
        printWriter.println();

        for (int i = 0; i < 10; i++)    {
            printWriter.println("Dataset size: " + listOfDataSetsForRecursiveTest[i][0].length);
            printWriter.println("Average runtime: " + getAverageExecutionTime(recursiveBenchmarkTimes[i]) + " nanoseconds.");
            printWriter.println("Coefficient of variance of runtime: " + getCoefficientOfVariance(recursiveBenchmarkTimes[i]));
            printWriter.println("Average critical op count: " + getAverageCriticalExecutionCount(yourSort.getCountRecursive(i)));
            printWriter.println("Coefficient of variance of count: " + getCoefficientOfVariance(yourSort.getCountRecursive(i)));
            printWriter.println("");
        }

        printWriter.close();
    }

    // helper method to create the datasets
    private int[] createDataSet(int n)  {

        Random rand = new Random();
        int[] dataset = new int[n];

        for (int i = 0; i < n; i++) {
            dataset[i] = rand.nextInt(n * 10) + 1;
        }
        return dataset;
    }

    // helper method to verify list is in numeric order
    private void verifyNumericOrderOfList(int[] list) throws UnsortedException {
        for (int i = 1; i < list.length; i++)   {
            if (list[i] < list[i-1])    {
                throw new UnsortedException("List is not in order!");
            }
        }
    }

    // for the report, get the average execution time based on the
    // nanoseconds recorded at the start and end of each dataset
    private long getAverageExecutionTime(long[] listOfRunTimes)   {
        long sum = 0;
        for (long i:listOfRunTimes
             ) {
            sum += i;
        }
        return sum / listOfRunTimes.length;
    }

    // for the report, get the average critical execution count
    private int getAverageCriticalExecutionCount(int[] listOfCriticalExecutionCounts)   {
        int sum = 0;
        for (int i:listOfCriticalExecutionCounts
                ) {
            sum += i;
        }
        return sum / listOfCriticalExecutionCounts.length;
    }

    // calculate the coefficient of variance with a long type input
    private long getCoefficientOfVariance(long[] list)  {

        // get standard deviation
        // calculate mean
        long mean = 0;
        for (long i:list
             ) {
            mean += i;
        }
        mean = mean / 50;

        // for each number, subtract mean
        long[] listOfDifferences = new long[50];
        for(int j = 0; j < 50; j++) {
            listOfDifferences[j] = (list[j] - mean);
        }

        // square each difference
        for(int j = 0; j < 50; j++) {
            listOfDifferences[j] = listOfDifferences[j] * listOfDifferences[j];
        }

        // take the mean of differences
        long mean2 = 0;
        for (long i:listOfDifferences
                ) {
            mean2 += i;
        }
        mean2 = mean2 / 49;

        return (long) Math.sqrt(mean2);
    }

    // calculate the coefficient of variance with an int type input
    private double getCoefficientOfVariance(int[] list)  {

        // get standard deviation

        // calculate mean
        int mean = 0;
        for (int i:list
                ) {
            mean += i;
        }
        mean = mean / 50;

        // for each number in original list, subtract mean
        int[] listOfDifferences = new int[50];
        for(int j = 0; j < 50; j++) {
            listOfDifferences[j] = (list[j] - mean);
        }

        // square each difference
        for(int j = 0; j < 50; j++) {
            listOfDifferences[j] = listOfDifferences[j] * listOfDifferences[j];
        }

        // take the mean of differences
        int mean2 = 0;
        for (int i:listOfDifferences
                ) {
            mean2 += i;
        }
        mean2 = mean2 / 49;

        return Math.sqrt(mean2);
    }
}