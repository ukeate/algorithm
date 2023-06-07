package base.binary;

/**
 * 相临数不重复，找一个局部最小
 */
public class FindOneMin {

    public static int findOneMin(int[] arr) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        if (arr.length == 1) {
            return 0;
        }
        if (arr[0] < arr[1]) {
            return 0;
        }
        if (arr[arr.length - 2] > arr[arr.length - 1]) {
            return arr.length - 1;
        }

        int l = 1;
        int r = arr.length - 2;
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (arr[mid] > arr[mid - 1]) {
                r = mid - 1;
            } else if (arr[mid] > arr[mid + 1]) {
                l = mid + 1;
            } else {
                return mid;
            }
        }
        return l;
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
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int ans = findOneMin(arr);
            if (!check(arr, ans)) {
                System.out.println("Wrong Answer " + ans + ":");
                print(arr);
            }
        }
        System.out.println("test end");
    }

}
