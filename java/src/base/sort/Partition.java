package base.sort;

public class Partition {

    private static void swap(int[] arr, int i, int j) {
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

    public static int partition(int[] arr, int l, int r) {
        if (l > r) {
            return -1;
        }
        if (l == r) {
            return l;
        }

        int le = l - 1;
        int i = l;
        while (i < r) {
            if (arr[i] <= arr[r]) {
                swap(arr, ++le, i++);
            } else {
                i++;
            }
        }
        swap(arr, ++le, r);
        return le;
    }

    public static int[] netherlandsFlag(int[] arr, int l, int r) {
        if (l > r) {
            return new int[]{-1, -1};
        }
        if (l == r) {
            return new int[]{l, r};
        }

        int less = l - 1;
        int more = r;
        int i = l;
        while (i < more) {
            if (arr[i] < arr[r]) {
                swap(arr, ++l, i++);
            } else if (arr[i] > arr[r]) {
                swap(arr, --more, i);
            } else {
                i++;
            }
        }
        swap(arr, more, r);
        return new int[]{less + 1, more};
    }

}
