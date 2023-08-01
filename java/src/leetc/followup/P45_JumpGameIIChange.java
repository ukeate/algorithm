package leetc.followup;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

// 可左右跳，从start到end的最小步数
public class P45_JumpGameIIChange {
    private static int process1(int[] arr, int n, int end, int i, boolean[] walk) {
        if (i < 1) {
            return -1;
        }
        if (i > n) {
            return -1;
        }
        if (walk[i - 1]) {
            return -1;
        }
        if (i == end) {
            return 0;
        }
        walk[i - 1] = true;
        int left = i - arr[i - 1];
        int right = i + arr[i - 1];
        int next = -1;
        int ans1 = process1(arr, n, end, left, walk);
        int ans2 = process1(arr, n, end, right, walk);
        if (ans1 != -1 && ans2 != -1) {
            next = Math.min(ans1, ans2);
        } else if (ans1 != -1) {
            next = ans1;
        } else if (ans2 != -1) {
            next = ans2;
        }
        walk[i - 1] = false;
        if (next == -1) {
            return -1;
        }
        return next + 1;
    }

    // 下标1-base
    public static int jump1(int n, int start, int end, int[] arr) {
        boolean[] walk = new boolean[n];
        return process1(arr, n, end, start, walk);
    }

    //

    private static int process2(int[] arr, int n, int end, int i, int k) {
        if (i < 1) {
            return -1;
        }
        if (i > n) {
            return -1;
        }
        if (k > n - 1) {
            return -1;
        }
        if (i == end) {
            return k;
        }
        int left = i - arr[i - 1];
        int right = i + arr[i - 1];
        int ans = -1;
        int ans1 = process2(arr, n, end, left, k + 1);
        int ans2 = process2(arr, n, end, right, k + 1);
        if (ans1 != -1 && ans2 != -1) {
            ans = Math.min(ans1, ans2);
        } else if (ans1 != -1) {
            ans = ans1;
        } else if (ans2 != -1) {
            ans = ans2;
        }
        return ans;
    }

    public static int jump2(int n, int start, int end, int[] arr) {
        return process2(arr, n, end, start, 0);
    }

    //

    // bfs
    public static int jump3(int n, int start, int end, int[] arr) {
        if (start < 1 || start > n || end < 1 || end > n) {
            return -1;
        }
        Queue<Integer> que = new LinkedList<>();
        HashMap<Integer, Integer> levelMap = new HashMap<>();
        que.add(start);
        levelMap.put(start, 0);
        while (!que.isEmpty()) {
            int cur = que.poll();
            int level = levelMap.get(cur);
            if (cur == end) {
                return level;
            } else {
                int left = cur - arr[cur - 1];
                int right = cur + arr[cur - 1];
                if (left > 0 && !levelMap.containsKey(left)) {
                    que.add(left);
                    levelMap.put(left, level + 1);
                }
                if (right <= n && !levelMap.containsKey(right)) {
                    que.add(right);
                    levelMap.put(right, level + 1);
                }
            }
        }
        return -1;
    }

    //

    private static int[] randomArr(int n, int r) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int) (r * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 200;
        int maxLen = 20;
        int maxVal = 10;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int n = arr.length;
            int start = (int) (n * Math.random()) + 1;
            int end = (int) (n * Math.random()) + 1;
            int ans1 = jump1(n, start, end, arr);
            int ans2 = jump2(n, start, end, arr);
            int ans3 = jump3(n, start, end, arr);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
