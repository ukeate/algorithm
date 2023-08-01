package leetc.top;

import java.util.LinkedList;

public class P134_GasStation {
    private static boolean[] good(int[] g, int[] c) {
        int n = g.length, m = n << 1;
        int[] arr = new int[m];
        for (int i = 0; i < n; i++) {
            arr[i] = g[i] - c[i];
            arr[i + n] = g[i] - c[i];
        }
        for (int i = 1; i < m; i++) {
            arr[i] += arr[i - 1];
        }
        LinkedList<Integer> w = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            while (!w.isEmpty() && arr[w.peekLast()] >= arr[i]) {
                w.pollLast();
            }
            w.addLast(i);
        }
        boolean[] ans = new boolean[n];
        for (int offset = 0, i = 0, j = n; j < m; offset = arr[i++], j++) {
            // [i, j]所有累加和>=0
            if (arr[w.peekFirst()] - offset >= 0) {
                ans[i] = true;
            }
            if (w.peekFirst() == i) {
                w.pollFirst();
            }
            while (!w.isEmpty() && arr[w.peekLast()] >= arr[j]) {
                w.pollLast();
            }
            w.addLast(j);
        }
        return ans;
    }

    public static int canCompleteCircuit1(int[] gas, int[] cost) {
        boolean[] good = good(gas, cost);
        for (int i = 0; i < gas.length; i++) {
            if (good[i]) {
                return i;
            }
        }
        return -1;
    }

    //

    private static int init(int[] dis, int[] oil) {
        int init = -1;
        for (int i = 0; i < dis.length; i++) {
            dis[i] = oil[i] - dis[i];
            if (dis[i] >= 0) {
                init = i;
            }
        }
        return init;
    }

    private static int nextIdx(int idx, int size) {
        return idx == size - 1 ? 0 : (idx + 1);
    }

    private static int lastIdx(int idx, int size) {
        return idx == 0 ? (size - 1) : idx - 1;
    }

    private static void enlargeConnect(int[] dis, int start, int init, boolean[] res) {
        int need = 0;
        while (start != init) {
            if (dis[start] < need) {
                need -= dis[start];
            } else {
                res[start] = true;
                need = 0;
            }
            start = lastIdx(start, dis.length);
        }
    }

    private static boolean[] enlarge(int[] dis, int init) {
        boolean[] res = new boolean[dis.length];
        int start = init, end = nextIdx(init, dis.length);
        // 头需
        int need = 0;
        // 尾剩
        int rest = 0;
        do {
            if (start != init && start == lastIdx(end, dis.length)) {
                break;
            }
            if (dis[start] < need) {
                need -= dis[start];
            } else {
                rest += dis[start] - need;
                need = 0;
                while (rest >= 0 && end != start) {
                    rest += dis[end];
                    end = nextIdx(end, dis.length);
                }
                if (rest >= 0) {
                    res[start] = true;
                    enlargeConnect(dis, lastIdx(start, dis.length), init, res);
                    break;
                }
            }
            start = lastIdx(start, dis.length);
        } while (start != init);
        return res;
    }

    private static boolean[] good2(int[] cost, int[] gas) {
        if (cost == null || gas == null || cost.length < 2 || cost.length != gas.length) {
            return new boolean[cost.length];
        }
        int init = init(cost, gas);
        return init == -1 ? new boolean[cost.length] : enlarge(cost, init);
    }

    public static int canCompleteCircuit2(int[] gas, int[] cost) {
        if (gas == null || gas.length == 0) {
            return -1;
        }
        if (gas.length == 1) {
            return gas[0] < cost[0] ? -1 : 0;
        }
        boolean[] good = good2(cost, gas);
        for (int i = 0; i < gas.length; i++) {
            if (good[i]) {
                return i;
            }
        }
        return -1;
    }
}
