interface SortInterface {

    int[] recursiveSort(int[] list, boolean isWarmUp);

    int[] iterativeSort(int[] list, boolean isWarmUp);

    int[] getCountIterative(int i);

    int[] getCountRecursive(int i);

    long getTime();
}