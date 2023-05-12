package basic;

public class Sort {

    public static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[j];
        arr[j] = arr[i];
        arr[i] = tmp;
    }

    public static void select(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                minIdx = arr[j] < arr[minIdx] ? j : minIdx;
            }
            swap(arr, i, minIdx);
        }
    }

    public static void bubble(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int n = arr.length;

        for (int end = n - 1; end >= 0; end--) {
            for (int i = 0; i < end; i++) {
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                }
            }
        }
    }

    public static void insert(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int n = arr.length;

        for (int end = 1; end < n; end++) {
            for (int i = end; i > 0 && arr[i - 1] > arr[i]; i--) {
                swap(arr, i - 1, i);
            }
        }
    }


    public static void main(String[] args) {
        int[] arr = {7, 1, 3, 4, 5, 8};
        insert(arr);
        print(arr);
    }
}
