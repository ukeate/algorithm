package base.binary;

/**
 * 无重复数，找一个局部最小
 */
public class OneMinInd {

    public static int oneMinInd(int[] arr) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        int n = arr.length;
        if (n == 1) {
            return 0;
        }
        if (arr[0] < arr[1]) {
            return 0;
        }
        if (arr[n - 2] > arr[n - 1]) {
            return n - 1;
        }
        int l = 0;
        int r = n - 1;
        int ans = -1;
        while (l < r - 1) {
            int mid = (l + r) / 2;
            if (arr[mid] < arr[mid - 1] && arr[mid] < arr[mid + 1]) {
                return mid;
            }
            if (arr[mid - 1] < arr[mid]) {
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return arr[l] < arr[r] ? l : r;
    }

    private static boolean check(int[] arr, int minInd) {
        if (arr.length == 0) {
            return minInd == -1;
        }
        int left = minInd - 1;
        int right = minInd + 1;
        boolean leftBigger = left >= 0 ? arr[left] > arr[minInd] : true;
        boolean rightBigger = right < arr.length ? arr[right] > arr[minInd] : true;
        return leftBigger && rightBigger;
    }


    private static void print(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }
        System.out.println();
    }

    private static int[] randomArr(int maxLen, int maxVal) {
        int len = (int) (Math.random() * maxLen);
        int[] arr = new int[len];
        if (len > 0) {
            arr[0] = (int) (Math.random() * maxVal);
            for (int i = 1; i < len; i++) {
                do {
                    arr[i] = (int) (Math.random() * maxVal);
                } while (arr[i] == arr[i - 1]);
            }
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 5;
        int maxVal = 20;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int ans = oneMinInd(arr);
            if (!check(arr, ans)) {
                System.out.println("Wrong Answer " + ans + ":");
                print(arr);
            }
        }
        System.out.println("test end");
    }

}
