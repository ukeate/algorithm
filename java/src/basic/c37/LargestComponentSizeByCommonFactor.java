package basic.c37;

import java.util.HashMap;

// https://leetcode.com/problems/largest-component-size-by-common-factor/
// 数字有非1公共因子则连通，求最大连通节点数
public class LargestComponentSizeByCommonFactor {
    private static class UnionFind {
        private int[] parents;
        private int[] sizes;
        private int[] help;

        public UnionFind(int n) {
            parents = new int[n];
            sizes = new int[n];
            help = new int[n];
            for (int i = 0; i < n; i++) {
                parents[i] = i;
                sizes[i] = 1;
            }
        }

        private int find(int i) {
            int hi = 0;
            while (i != parents[i]) {
                help[hi++] = i;
                i = parents[i];
            }
            for (hi--; hi >= 0; hi--) {
                parents[help[hi]] = i;
            }
            return i;
        }

        public void union(int i, int j) {
            int f1 = find(i);
            int f2 = find(j);
            if (f1 != f2) {
                int big = sizes[f1] >= sizes[f2] ? f1 : f2;
                int small = big == f1 ? f2 : f1;
                parents[small] = big;
                sizes[big] = sizes[f1] + sizes[f2];
            }
        }

        public int maxSize() {
            int ans = 0;
            for (int size : sizes) {
                ans = Math.max(ans, size);
            }
            return ans;
        }
    }

    private static int gcd(int m, int n) {
        return n == 0 ? m : gcd(n, m % n);
    }

    public static int largest1(int[] arr) {
        int n = arr.length;
        UnionFind set = new UnionFind(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (gcd(arr[i], arr[j]) != 1) {
                    set.union(i, j);
                }
            }
        }
        return set.maxSize();
    }

    //

    public static int largest2(int[] arr) {
        int n = arr.length;
        UnionFind unionFind = new UnionFind(n);
        HashMap<Integer, Integer> factorMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int num = arr[i];
            int limit = (int) Math.sqrt(num);
            for (int j = 1; j <= limit; j++) {
                if (num % j == 0) {
                    if (j != 1) {
                        if (!factorMap.containsKey(j)) {
                            factorMap.put(j, i);
                        } else {
                            unionFind.union(factorMap.get(j), i);
                        }
                    }
                    int other = num / j;
                    if (other != 1) {
                        if (!factorMap.containsKey(other)) {
                            factorMap.put(other, i);
                        } else {
                            unionFind.union(factorMap.get(other), i);
                        }
                    }
                }
            }
        }
        return unionFind.maxSize();
    }
}
