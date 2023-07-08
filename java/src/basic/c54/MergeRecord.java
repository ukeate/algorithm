package basic.c54;

// arr的长度为power, 按ops数组数字num操作，2^num一组反转arr，返回每次操作后逆序对的数量
public class MergeRecord {
    private static void reverse(int[] arr, int l, int r) {
        while (l < r) {
            int tmp = arr[l];
            arr[l++] = arr[r];
            arr[r--] = tmp;
        }
    }

    private static void reverse(int[] arr, int k) {
        if (k < 2) {
            return;
        }
        for (int i = 0; i < arr.length; i += k) {
            reverse(arr, i, i + k - 1);
        }
    }

    private static int count(int[] arr) {
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    ans++;
                }
            }
        }
        return ans;
    }

    public static int[] reversePair1(int[] arr, int[] ops, int power) {
        int[] ans = new int[ops.length];
        for (int i = 0; i < ops.length; i++) {
            reverse(arr, 1 << ops[i]);
            ans[i] = count(arr);
        }
        return ans;
    }

    //

    private static int[] copy(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    private static int merge(int[] arr, int l, int m, int r) {
        int[] help = new int[r - l + 1];
        int i = 0;
        int p1 = l;
        int p2 = m + 1;
        int ans = 0;
        while (p1 <= m && p2 <= r) {
            ans += arr[p1] > arr[p2] ? (m - p1 + 1) : 0;
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= m) {
            help[i++] = arr[p1++];
        }
        while (p2 <= r) {
            help[i++] = arr[p2++];
        }
        for (i = 0; i < help.length; i++) {
            arr[l + i] = help[i];
        }
        return ans;
    }

    private static void process(int[] arr, int l, int r, int power, int[] record) {
        if (l == r) {
            return;
        }
        int mid = l + ((r - l) >> 1);
        process(arr, l, mid, power - 1, record);
        process(arr, mid + 1, r, power - 1, record);
        record[power] += merge(arr, l, mid, r);
    }

    public static int[] reversePair2(int[] arr, int[] ops, int power) {
        int[] arr2 = copy(arr);
        reverse(arr2, 0, arr2.length - 1);
        // i个一组，左半对比右半, 逆序对数
        int[] desc = new int[power + 1];
        // 顺序对数
        int[] asc = new int[power + 1];
        process(arr, 0, arr.length - 1, power, desc);
        process(arr2, 0, arr2.length - 1, power, asc);
        int[] ans = new int[ops.length];
        for (int i = 0; i < ops.length; i++) {
            int curPower = ops[i];
            for (int p = 1; p <= curPower; p++) {
                int tmp = desc[p];
                desc[p] = asc[p];
                asc[p] = tmp;
            }
            for (int p = 1; p <= power; p++) {
                ans[i] += desc[p];
            }
        }
        return ans;
    }

    //

    private static int[] randomArr(int power, int maxVal) {
        int[] ans = new int[1 << power];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (maxVal * Math.random());
        }
        return ans;
    }

    private static int[] randomOps(int len, int power) {
        int[] ans = new int[len];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) ((power + 1) * Math.random());
        }
        return ans;
    }

    public static boolean isEqual(int[] arr1, int[] arr2) {
        if (arr1 == null ^ arr2 == null) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int times = 50000;
        int maxPower = 8;
        int maxOpsLen = 10;
        int maxVal = 30;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int power = (int) (maxPower * Math.random()) + 1;
            int opsLen = (int) (maxOpsLen * Math.random()) + 1;
            int[] arr = randomArr(power, maxVal);
            int[] arr1 = copy(arr);
            int[] arr2 = copy(arr);
            int[] ops = randomOps(opsLen, power);
            int[] ops1 = copy(ops);
            int[] ops2 = copy(ops);
            int[] ans1 = reversePair1(arr1, ops1, power);
            int[] ans2 = reversePair2(arr2, ops2, power);
            if (!isEqual(ans1, ans2)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");

        maxPower = 20;
        maxOpsLen = 1000000;
        maxVal = 1000;
        int[] arr = randomArr(maxPower, maxVal);
        int[] ops = randomOps(maxOpsLen, maxPower);
        long start = System.currentTimeMillis();
        reversePair2(arr, ops, maxPower);
        long end = System.currentTimeMillis();
        System.out.println("run time: " + (end - start) + " ms");
    }
}
