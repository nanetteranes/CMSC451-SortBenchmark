import java.io.IOException;

class SortMain {

    private static BenchmarkSorts benchmarkSorts;

    public static void main(String[] args) {

        // create list of 10 different dataset sizes and instantiate BenchmarkSorts object
        int[] sizes = new int[] {500, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000};
        benchmarkSorts = new BenchmarkSorts(sizes);

        // run the benchmark test
        try {
            benchmarkSorts.runSorts();
        } catch (UnsortedException e) {
            e.printStackTrace();
        }

        // generate report
        try {
            benchmarkSorts.displayReport();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}