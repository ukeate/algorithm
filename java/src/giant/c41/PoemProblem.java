package giant.c41;

import java.util.Arrays;
import java.util.HashMap;

// 来自小红书
// 有四种诗的韵律分别为: AABB、ABAB、ABBA、AAAA
// 比如 : 1 1 3 3就属于AABB型的韵律、6 6 6 6就属于AAAA型的韵律等等
// 一个数组arr，当然可以生成很多的子序列，如果某个子序列一直以韵律的方式连接起来，我们称这样的子序列是有效的
// 比如, arr = { 1, 1, 15, 1, 34, 1, 2, 67, 3, 3, 2, 4, 15, 3, 17, 4, 3, 7, 52, 7, 81, 9, 9 }
// arr的一个子序列为{1, 1, 1, 1, 2, 3, 3, 2, 4, 3, 4, 3, 7, 7, 9, 9}
// 其中1, 1, 1, 1是AAAA、2, 3, 3, 2是ABBA、4, 3, 4, 3是ABAB、7, 7, 9, 9是AABB
// 可以看到，整个子序列一直以韵律的方式连接起来，所以这个子序列是有效的
// 给定一个数组arr, 返回最长的有效子序列长度
// 题目限制 : arr长度 <= 4000, arr中的值<= 10^9
// 离散化之后，arr长度 <= 4000,  arr中的值<= 4000
public class PoemProblem {

    private static boolean valid(int[] p, int i) {
        return (p[i] == p[i + 1] && p[i + 2] == p[i + 3]) // AABB AAAA
                || (p[i] == p[i + 2] && p[i + 1] == p[i + 3] && p[i] != p[i + 1]) // ABAB
                || (p[i] == p[i + 3] && p[i + 1] == p[i + 2] && p[i] != p[i + 1]); // ABBA
    }

    private static int process1(int[] arr, int idx, int[] path, int size) {
        if (idx == arr.length) {
            if (size % 4 != 0) {
                return 0;
            } else {
                for (int i = 0; i < size; i += 4) {
                    if (!valid(path, i)) {
                        return 0;
                    }
                }
                return size;
            }
        } else {
            int p1 = process1(arr, idx + 1, path, size);
            path[size] = arr[idx];
            int p2 = process1(arr, idx + 1, path, size + 1);
            return Math.max(p1, p2);
        }
    }

    public static int max1(int[] arr) {
        if (arr == null || arr.length < 4) {
            return 0;
        }
        int[] path = new int[arr.length];
        return process1(arr, 0, path, 0);
    }

    //

    private static int rightClosed(int[][] imap, int v, int i) {
        int left = 0;
        int right = imap[v].length - 1;
        int ans = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (imap[v][mid] <= i) {
                left = mid + 1;
            } else {
                ans = mid;
                right = mid - 1;
            }
        }
        return ans == -1 ? -1 : imap[v][ans];
    }

    private static int process2(int[] arr, int[][] imap, int i) {
        if (i + 4 > arr.length) {
            return 0;
        }
        int p0 = process2(arr, imap, i + 1);
        int a1, a2, a3, a4, b1, b2;
        // AABB
        int p1 = 0;
        a2 = rightClosed(imap, arr[i], i);
        if (a2 != -1) {
            for (int next = a2 + 1; next < arr.length; next++) {
                if (arr[i] != arr[next]) {
                    b2 = rightClosed(imap, arr[next], next);
                    if (b2 != -1) {
                        p1 = Math.max(p1, 4 + process2(arr, imap, b2 + 1));
                    }
                }
            }
        }
        // ABAB
        int p2 = 0;
        for (b1 = i + 1; b1 < arr.length; b1++) {
            if (arr[i] != arr[b1]) {
                a2 = rightClosed(imap, arr[i], b1);
                if (a2 != -1) {
                    b2 = rightClosed(imap, arr[b1], a2);
                    if (b2 != -1) {
                        p2 = Math.max(p2, 4 + process2(arr, imap, b2 + 1));
                    }
                }
            }
        }
        // ABBA
        int p3 = 0;
        for (b1 = i + 1; b1 < arr.length; b1++) {
            if (arr[i] != arr[b1]) {
                b2 = rightClosed(imap, arr[b1], b1);
                if (b2 != -1) {
                    a2 = rightClosed(imap, arr[i], b2);
                    if (a2 != -1) {
                        p3 = Math.max(p3, 4 + process2(arr, imap, a2 + 1));
                    }
                }
            }
        }
        // AAAA
        int p4 = 0;
        a2 = rightClosed(imap, arr[i], i);
        a3 = a2 == -1 ? -1 : rightClosed(imap, arr[i], a2);
        a4 = a3 == -1 ? -1 : rightClosed(imap, arr[i], a3);
        if (a4 != -1) {
            p4 = Math.max(p4, 4 + process2(arr, imap, a4 + 1));
        }
        return Math.max(p0, Math.max(Math.max(p1, p2), Math.max(p3, p4)));
    }

    public static int max2(int[] arr) {
        if (arr == null || arr.length < 4) {
            return 0;
        }
        int n = arr.length;
        int[] sorted = Arrays.copyOf(arr, n);
        Arrays.sort(sorted);
        // 离散化
        HashMap<Integer, Integer> vmap = new HashMap<>();
        int idx = 0;
        vmap.put(sorted[0], idx++);
        for (int i = 1; i < n; i++) {
            if (sorted[i] != sorted[i - 1]) {
                vmap.put(sorted[i], idx++);
            }
        }
        int[] sizeArr = new int[idx];
        for (int i = 0; i < n; i++) {
            arr[i] = vmap.get(arr[i]);
            sizeArr[arr[i]]++;
        }
        // [离散化值][出现位置]
        int[][] imap = new int[idx][];
        for (int i = 0; i < idx; i++) {
            imap[i] = new int[sizeArr[i]];
        }
        for (int i = n - 1; i >= 0; i--) {
            imap[arr[i]][--sizeArr[arr[i]]] = i;
        }
        return process2(arr, imap, 0);
    }

    public static int dp(int[] arr) {
        if (arr == null || arr.length < 4) {
            return 0;
        }
        int n = arr.length;
        int[] sorted = Arrays.copyOf(arr, n);
        Arrays.sort(sorted);
        HashMap<Integer, Integer> vmap = new HashMap<>();
        int idx = 0;
        vmap.put(sorted[0], idx++);
        for (int i = 1; i < n; i++) {
            if (sorted[i] != sorted[i - 1]) {
                vmap.put(sorted[i], idx++);
            }
        }
        int[] sizeArr = new int[idx];
        for (int i = 0; i < n; i++) {
            arr[i] = vmap.get(arr[i]);
            sizeArr[arr[i]]++;
        }
        int[][] imap = new int[idx][];
        for (int i = 0; i < idx; i++) {
            imap[i] = new int[sizeArr[i]];
        }
        for (int i = n - 1; i >= 0; i--) {
            imap[arr[i]][--sizeArr[arr[i]]] = i;
        }
        int[] dp = new int[n + 1];
        for (int i = n - 4; i >= 0; i--) {
            int p0 = dp[i + 1];
            int a2, a3, a4, b1, b2;
            // AABB
            int p1 = 0;
            a2 = rightClosed(imap, arr[i], i);
            if (a2 != -1) {
                for (int next = a2 + 1; next < arr.length; next++) {
                    if (arr[i] != arr[next]) {
                        b2 = rightClosed(imap, arr[next], next);
                        if (b2 != -1) {
                            p1 = Math.max(p1, 4 + dp[b2 + 1]);
                        }
                    }
                }
            }
            // ABAB
            int p2 = 0;
            for (b1 = i + 1; b1 < arr.length; b1++) {
                if (arr[i] != arr[b1]) {
                    a2 = rightClosed(imap, arr[i], b1);
                    if (a2 != -1) {
                        b2 = rightClosed(imap, arr[b1], a2);
                        if (b2 != -1) {
                            p2 = Math.max(p2, 4 + dp[b2 + 1]);
                        }
                    }
                }
            }
            // ABBA
            int p3 = 0;
            for (b1 = i + 1; b1 < arr.length; b1++) {
                if (arr[i] != arr[b1]) {
                    b2 = rightClosed(imap, arr[b1], b1);
                    if (b2 != -1) {
                        a2 = rightClosed(imap, arr[i], b2);
                        if (a2 != -1) {
                            p3 = Math.max(p3, 4 + dp[a2 + 1]);
                        }
                    }
                }
            }
            // AAAA
            int p4 = 0;
            a2 = rightClosed(imap, arr[i], i);
            a3 = a2 == -1 ? -1 : rightClosed(imap, arr[i], a2);
            a4 = a3 == -1 ? -1 : rightClosed(imap, arr[i], a3);
            if (a4 != -1) {
                p4 = Math.max(p4, 4 + dp[a4 + 1]);
            }
            dp[i] = Math.max(p0, Math.max(Math.max(p1, p2), Math.max(p3, p4)));
        }
        return dp[0];
    }

    // 课堂有同学提出了贪心策略（这题还真是有贪心策略），是正确的
    // AABB
    // ABAB
    // ABBA
    // AAAA
    // 先看前三个规则：AABB、ABAB、ABBA
    // 首先A、A、B、B的全排列为:
    // AABB -> AABB
    // ABAB -> ABAB
    // ABBA -> ABBA
    // BBAA -> 等同于AABB，因为A和B谁在前、谁在后都算是 : AABB的范式
    // BABA -> 等同于ABAB，因为A和B谁在前、谁在后都算是 : ABAB的范式
    // BAAB -> 等同于ABBA，因为A和B谁在前、谁在后都算是 : ABBA的范式
    // 也就是说，AABB、ABAB、ABBA这三个规则，可以这么用：
    // 只要有两个不同的数，都出现2次，那么这一共4个数就一定符合韵律规则。
    // 所以：
    // 1) 当来到arr中的一个数字num的时候，
    // 如果num已经出现了2次了, 只要之前还有一个和num不同的数，
    // 也出现了两次，则一定符合了某个规则, 长度直接+4，然后清空所有的统计
    // 2) 当来到arr中的一个数字num的时候,
    // 如果num已经出现了4次了(规则四), 长度直接+4，然后清空所有的统计
    // 但是如果我去掉某个规则，该贪心直接报废，比如韵律规则变成:
    // AABB、ABAB、AAAA
    // 因为少了ABBA, 所以上面的化简不成立了, 得重新分析新规则下的贪心策略
    // 而尝试的方法就更通用(也就是maxLen3)，只是减少一个分支而已
    public static int max3(int[] arr) {
        HashMap<Integer, Integer> map = new HashMap<>();
        // 有多少数出现了2次
        int two = 0;
        // 子序列增长到多少
        int ans = 0;
        // 当前num次数
        int numTimes = 0;
        for (int num : arr) {
            map.put(num, map.getOrDefault(num, 0) + 1);
            numTimes = map.get(num);
            two += numTimes == 2 ? 1 : 0;
            if (two == 2 || numTimes == 4) {
                ans += 4;
                map.clear();
                two = 0;
            }
        }
        return ans;
    }

    //

    private static int[] randomArr(int len, int val) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * val);
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 1000;
        int maxLen = 16;
        int maxVal = 10;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int[] arr1 = Arrays.copyOf(arr, arr.length);
            int[] arr2 = Arrays.copyOf(arr, arr.length);
            int[] arr3 = Arrays.copyOf(arr, arr.length);
            int[] arr4 = Arrays.copyOf(arr, arr.length);
            int ans1 = max1(arr1);
            int ans2 = max2(arr2);
            int ans3 = dp(arr3);
            int ans4 = max3(arr3);
            if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");

        long start;
        long end;
        int[] longArr = randomArr(4000, 20);
        start = System.currentTimeMillis();
        System.out.println(dp(longArr));
        end = System.currentTimeMillis();
        System.out.println("运行时间(毫秒) : " + (end - start));
        System.out.println("===========");

        start = System.currentTimeMillis();
        System.out.println(max3(longArr));
        end = System.currentTimeMillis();
        System.out.println("运行时间(毫秒) : " + (end - start));
        System.out.println("===========");

    }
}
