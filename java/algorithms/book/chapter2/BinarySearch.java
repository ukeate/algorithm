package chapter2;

import org.junit.Test;

/**
 * Created by outrun on 5/11/16.
 */
public class BinarySearch {

    private static int NOT_FOUND = -1;

    /**
     * log(N - 1) + 2
     *
     * @param a
     * @param x
     * @param <AnyType>
     * @return
     */
    public <AnyType extends Comparable<? super AnyType>>
    int binarySearch(AnyType[] a, AnyType x) {
        int low = 0, high = a.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (a[mid].compareTo(x) < 0) {
                low = mid + 1;
            } else if (a[mid].compareTo(x) > 0) {
                high = mid - 1;
            } else {
                return mid;
            }

        }

        return NOT_FOUND;
    }

    @Test
    public void testHere() {

        Integer[] arr = {1, 2, 3, 4, 5, 6, 7};
        System.out.println(binarySearch(arr, 4));
    }
}
