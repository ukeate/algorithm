package leetc.followup;

/**
 * 环形开关灯问题 (Circular Light Switch Problem)
 * 
 * 问题描述：
 * 有一排灯泡排成环形，初始状态为给定数组（0表示关闭，1表示开启）
 * 每次按下开关会改变该位置及其相邻两个位置的灯泡状态
 * 由于是环形排列，第一个和最后一个灯泡相邻
 * 
 * 目标：使用最少的按键次数让所有灯泡都亮起（状态为1）
 * 
 * 与直线排列的区别：
 * - 直线排列：两端的灯泡只有一个邻居
 * - 环形排列：每个灯泡都有两个邻居（首尾相连）
 * 
 * 解法思路：
 * 贪心算法 + 状态推导：
 * 1. 关键观察：每个开关按两次等于没按，所以每个开关最多按一次
 * 2. 环形排列的特点：第0个开关会影响第n-1、0、1位置的灯泡
 * 3. 需要考虑第0个开关的按与不按两种情况
 * 4. 对于每种情况，从左到右贪心决策其他开关
 * 
 * 算法步骤：
 * 1. 分别尝试按下和不按下第0个开关
 * 2. 对于每种情况，从位置1开始贪心：
 *    - 如果前一个灯泡是暗的，必须按下当前开关来点亮它
 *    - 否则不按当前开关
 * 3. 最后检查所有灯泡是否都亮起
 * 4. 返回两种情况中的最小按键次数
 * 
 * 时间复杂度：O(n) - 需要尝试两种情况，每种情况线性扫描
 * 空间复杂度：O(1) - 只需要常数额外空间
 */
public class LightLoopProblem {

    /**
     * 检查是否所有灯泡都亮起
     * 
     * @param arr 灯泡状态数组
     * @return 所有灯泡都亮起返回true，否则返回false
     */
    private static boolean valid(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取位置i的前一个位置（环形数组）
     * 
     * @param i 当前位置
     * @param n 数组大小
     * @return 前一个位置的索引
     */
    private static int lastIdx(int i, int n) {
        return i == 0 ? (n - 1) : (i - 1);
    }

    /**
     * 获取位置i的后一个位置（环形数组）
     * 
     * @param i 当前位置
     * @param n 数组大小
     * @return 后一个位置的索引
     */
    private static int nextIdx(int i, int n) {
        return i == n - 1 ? 0 : (i + 1);
    }

    /**
     * 按下第i位置的开关，改变前、当前、后三个位置的灯泡状态（环形）
     * 
     * @param arr 灯泡状态数组
     * @param i 开关位置
     */
    private static void change(int[] arr, int i) {
        arr[lastIdx(i, arr.length)] ^= 1;  // 前一个位置
        arr[i] ^= 1;                       // 当前位置
        arr[nextIdx(i, arr.length)] ^= 1;  // 后一个位置
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
