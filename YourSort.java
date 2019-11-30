public class YourSort implements SortInterface {

    private int[][] criticalOperationCountIterative = new int[10][50];
    private int[][] criticalOperationCountRecursive = new int[10][50];
    private int setcounter = 0;
    private int subsetcounter = 0;

    @Override
    public int[] recursiveSort(int[] inputList, boolean isWarmUp) {

        recurSelectionSort(inputList, inputList.length, 0, isWarmUp);
        return inputList;
    }

    // Recursive selection sort code
    // from GeeksForGeeks
    // https://www.geeksforgeeks.org/recursive-selection-sort/
    private void recurSelectionSort(int[] a, int n, int index, boolean isWarmUp)
    {
        // Return when starting and size are same
        if (index == n) {

            // increment counters for critical operation lists
            subsetcounter++;
            if (subsetcounter == 50)    {
                setcounter++;
                subsetcounter = 0;
            }
            if (setcounter == 10)   {
                setcounter = 0;
            }

            return;
        }

        // calling minimum index function for minimum index
        int k = minIndex(a, index, n-1, isWarmUp);

        // Swapping when index and minimum index are not same
        if (k != index){
            if (!isWarmUp) {
                criticalOperationCountRecursive[setcounter][subsetcounter]++;
            }
            // swap
            int temp = a[k];
            a[k] = a[index];
            a[index] = temp;
        }

        // Recursively calling selection sort function
        if (!isWarmUp) {
            criticalOperationCountRecursive[setcounter][subsetcounter]++;
        }
        recurSelectionSort(a, n, index + 1, isWarmUp);
    }

    // Return minimum index
    private int minIndex(int a[], int i, int j, boolean isWarmUp)
    {
        if (i == j) {
            if (!isWarmUp) {
                criticalOperationCountRecursive[setcounter][subsetcounter]++;
            }
            return i;
        }

        // Find minimum of remaining elements
        int k = minIndex(a, i + 1, j, isWarmUp);

        // Return minimum of current and remaining.
        if (!isWarmUp) {
            criticalOperationCountRecursive[setcounter][subsetcounter]++;
        }
        return (a[i] < a[k])? i : k;
    }

    @Override
    public int[] iterativeSort(int[] list, boolean isWarmUp) {

        int n = list.length;

        for (int firstUnsortedIndex = 0; firstUnsortedIndex < n - 1; firstUnsortedIndex++)
        {
            if (!isWarmUp) {
                criticalOperationCountIterative[setcounter][subsetcounter]++;
            }
            int arrayIndexForCurrentMinimum = firstUnsortedIndex;

            for (int unsorted = firstUnsortedIndex + 1; unsorted < n; unsorted++)
            {
                if (!isWarmUp) {
                    criticalOperationCountIterative[setcounter][subsetcounter]++;
                }
                if (list[unsorted] < list[arrayIndexForCurrentMinimum])
                    arrayIndexForCurrentMinimum = unsorted;
            }

            int temp = list[arrayIndexForCurrentMinimum];
            list[arrayIndexForCurrentMinimum] = list[firstUnsortedIndex];
            list[firstUnsortedIndex] = temp;
        }

        // increment counters for critical operation lists
        subsetcounter++;
        if (subsetcounter == 50)    {
            setcounter++;
            subsetcounter = 0;
        }
        if (setcounter == 10)   {
            setcounter = 0;
        }

        return list;
    }

    @Override
    public int[] getCountIterative(int i) {
        return criticalOperationCountIterative[i];
    }

    @Override
    public int[] getCountRecursive(int i) {
        return criticalOperationCountRecursive[i];
    }

    @Override
    public long getTime() {
        return 0;
    }
}