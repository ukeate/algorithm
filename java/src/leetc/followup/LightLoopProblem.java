package leetc.followup;

/*
0是灭灯，1是亮灯，按开关同时改变相邻灯
排成直线时，最少按多少开关，灯全亮
排成环时，最少按多少开关
 */
public class LightLoopProblem {

    private static boolean valid(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) {
                return false;
            }
        }
        return true;
    }

    private static int lastIdx(int i, int n) {
        return i == 0 ? (n - 1) : (i - 1);
    }

    private static int nextIdx(int i, int n) {
        return i == n - 1 ? 0 : (i + 1);
    }

    private static void change(int[] arr, int i) {
        arr[lastIdx(i, arr.length)] ^= 1;
        arr[i] ^= 1;
        arr[nextIdx(i, arr.length)] ^= 1;
    }

    private static int process1(int[] arr, int i) {
        if (i == arr.length) {
            return valid(arr) ? 0 : Integer.MAX_VALUE;
        }
        int p1 = process1(arr, i + 1);
        change(arr, i);
        int p2 = process1(arr, i + 1);
        change(arr, i);
        p2 = (p2 == Integer.MAX_VALUE) ? p2 : (p2 + 1);
        return Math.min(p1, p2);
    }

    public static int loop1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0] == 1 ? 0 : 1;
        }
        if (arr.length == 2) {
            return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        return process1(arr, 0);
    }

    //

    // pre做决定
    private static int process2(int[] arr, int idx, int prepre, int pre, int end, int first) {
        if (idx == arr.length) {
            // pre == n - 1
            return (end != first || end != prepre) ? Integer.MAX_VALUE : (end ^ 1);
        }
        if (idx < arr.length - 1) {
            // pre < n - 2
            if (prepre == 0) {
                int next = process2(arr, idx + 1, pre ^ 1, arr[idx] ^ 1, end, first);
                return next == Integer.MAX_VALUE ? next : (next + 1);
            } else {
                return process2(arr, idx + 1, pre, arr[idx], end, first);
            }
        } else {
            // pre == n - 2
            if (prepre == 0) {
                int next = process2(arr, idx + 1, pre ^ 1, end ^ 1, end ^ 1, first);
                return next == Integer.MAX_VALUE ? next : (next + 1);
            } else {
                return process2(arr, idx + 1, pre, end, end, first);
            }
        }
    }

    public static int loop2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0] == 1 ? 0 : 1;
        }
        if (arr.length == 2) {
            return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        if (arr.length == 3) {
            return (arr[0] != arr[1] || arr[0] != arr[2]) ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        // 0不变，1不变
        int p1 = process2(arr, 3, arr[1], arr[2], arr[arr.length - 1], arr[0]);
        // 0变，1不变
        int p2 = process2(arr, 3, arr[1] ^ 1, arr[2], arr[arr.length - 1] ^ 1, arr[0] ^ 1);
        // 0不变，1变
        int p3 = process2(arr, 3, arr[1] ^ 1, arr[2] ^ 1, arr[arr.length - 1], arr[0] ^ 1);
        // 0变，1变
        int p4 = process2(arr, 3, arr[1], arr[2] ^ 1, arr[arr.length - 1] ^ 1, arr[0]);
        p2 = p2 != Integer.MAX_VALUE ? (p2 + 1) : p2;
        p3 = p3 != Integer.MAX_VALUE ? (p3 + 1) : p3;
        // p4改变了2个, +2
        p4 = p4 != Integer.MAX_VALUE ? (p4 + 2) : p4;
        return Math.min(Math.min(p1, p2), Math.min(p3, p4));
    }

    //

    private static int process3(int[] arr, int i, int prepre, int pre, int end, int first) {
        int ans = 0;
        while (i < arr.length - 1) {
            if (prepre == 0) {
                ans++;
                prepre = pre ^ 1;
                pre = (arr[i++] ^ 1);
            } else {
                prepre = pre;
                pre = arr[i++];
            }
        }
        // pre == n - 2
        if (prepre == 0) {
            ans++;
            prepre = pre ^ 1;
            end ^= 1;
            pre = end;
        } else {
            prepre = pre;
            pre = end;
        }
        // pre == n - 1
        return (end != first || end != prepre) ? Integer.MAX_VALUE : (ans + (end ^ 1));
    }

    public static int loop3(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0] == 1 ? 0 : 1;
        }
        if (arr.length == 2) {
            return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        if (arr.length == 3) {
            return (arr[0] != arr[1] || arr[0] != arr[2]) ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        // 0不变，1不变
        int p1 = process3(arr, 3, arr[1], arr[2], arr[arr.length - 1], arr[0]);
        // 0变，1不变
        int p2 = process3(arr, 3, arr[1] ^ 1, arr[2], arr[arr.length - 1] ^ 1, arr[0] ^ 1);
        // 0不变，1变
        int p3 = process3(arr, 3, arr[1] ^ 1, arr[2] ^ 1, arr[arr.length - 1], arr[0] ^ 1);
        // 0变，1变
        int p4 = process3(arr, 3, arr[1], arr[2] ^ 1, arr[arr.length - 1] ^ 1, arr[0]);
        p2 = p2 != Integer.MAX_VALUE ? (p2 + 1) : p2;
        p3 = p3 != Integer.MAX_VALUE ? (p3 + 1) : p3;
        p4 = p4 != Integer.MAX_VALUE ? (p4 + 2) : p4;
        return Math.min(Math.min(p1, p2), Math.min(p3, p4));
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
            int ans1 = loop1(arr);
            int ans2 = loop2(arr);
            int ans3 = loop3(arr);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong2");
                System.out.println(ans1 + "|" + ans2 + "|" + ans3);
                break;
            }
        }
        System.out.println("test end");

        int len = 100000000;
        int[] arr = randomArr(len);
        long start = 0, end = 0;
        start = System.currentTimeMillis();
        loop3(arr);
        end = System.currentTimeMillis();
        System.out.println("loop3 run time: " + (end - start) + " ms");
    }

}
