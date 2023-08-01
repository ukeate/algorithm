package leetc.followup;

public class LightProblem {
    private static boolean valid(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) {
                return false;
            }
        }
        return true;
    }

    public static void change(int[] arr, int i) {
        if (i == 0) {
            arr[0] ^= 1;
            arr[1] ^= 1;
        } else if (i == arr.length - 1) {
            arr[i - 1] ^= 1;
            arr[i] ^= 1;
        } else {
            arr[i - 1] ^= 1;
            arr[i] ^= 1;
            arr[i + 1] ^= 1;
        }
    }

    private static int process1(int[] arr, int i) {
        if (i == arr.length) {
            return valid(arr) ? 0 : Integer.MAX_VALUE;
        }
        // 不按
        int p1 = process1(arr, i + 1);
        change(arr, i);
        // 按
        int p2 = process1(arr, i + 1);
        change(arr, i);
        p2 = (p2 == Integer.MAX_VALUE) ? p2 : (p2 + 1);
        return Math.min(p1, p2);
    }

    public static int noLoop1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0] ^ 1;
        }
        if (arr.length == 2) {
            return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        return process1(arr, 0);
    }

    //

    // idx不变，在pre位置改变
    private static int process2(int[] arr, int idx, int prepre, int pre) {
        if (idx == arr.length) {
            return prepre != pre ? (Integer.MAX_VALUE) : (pre ^ 1);
        }
        if (prepre == 0) {
            // pre要按
            int next = process2(arr, idx + 1, pre ^ 1, arr[idx] ^ 1);
            return next == Integer.MAX_VALUE ? next : (next + 1);
        } else {
            return process2(arr, idx + 1, pre, arr[idx]);
        }
    }

    public static int noLoop2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0] == 1 ? 0 : 1;
        }
        if (arr.length == 2) {
            return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        int p1 = process2(arr, 2, arr[0], arr[1]);
        int p2 = process2(arr, 2, arr[0] ^ 1, arr[1] ^ 1);
        if (p2 != Integer.MAX_VALUE) {
            p2++;
        }
        return Math.min(p1, p2);
    }

    //

    public static int process3(int[] arr, int i, int prepre, int pre) {
        int ans = 0;
        while (i < arr.length) {
            if (prepre == 0) {
                ans++;
                prepre = pre ^ 1;
                pre = arr[i++] ^ 1;
            } else {
                prepre = pre;
                pre = arr[i++];
            }
        }
        return (prepre != pre) ? Integer.MAX_VALUE : (ans + (pre ^ 1));
    }


    public static int noLoop3(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0] == 1 ? 0 : 1;
        }
        if (arr.length == 2) {
            return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        int p1 = process3(arr, 2, arr[0], arr[1]);
        int p2 = process3(arr, 2, arr[0] ^ 1, arr[1] ^ 1);
        if (p2 != Integer.MAX_VALUE) {
            p2++;
        }
        return Math.min(p1, p2);
    }

    //


    private static int[] randomArr(int len) {
        int[] arr = new int[len];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 2);
        }
        return arr;
    }

    public static void main(String[] args) {
        System.out.println("test begin");
        int times = 20000;
        int maxLen = 12;
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * maxLen);
            int[] arr = randomArr(len);
            int ans1 = noLoop1(arr);
            int ans2 = noLoop2(arr);
            int ans3 = noLoop3(arr);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong1");
                break;
            }
        }
        System.out.println("test end");

        int len = 100000000;
        int[] arr = randomArr(len);
        long start = 0, end = 0;
        start = System.currentTimeMillis();
        noLoop3(arr);
        end = System.currentTimeMillis();
        System.out.println("noLoop3 run time" + (end - start) + " ms");
    }
}
