package leetc.top;

import java.util.HashMap;

public class P952_LargestComponentSizeByCommonFactor {
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

        public int maxSize() {
            int ans = 0;
            for (int size : sizes) {
                ans = Math.max(ans, size);
            }
            return ans;
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
    }

    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static int largestComponentSize1(int[] arr) {
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

    public static int largestComponentSize2(int[] arr) {
        int n = arr.length;
        UnionFind unionFind = new UnionFind(n);
        HashMap<Integer, Integer> factorsMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int num = arr[i];
            int limit = (int) Math.sqrt(num);
            for (int j = 1; j <= limit; j++) {
                if (num % j == 0) {
                    if (j != 1) {
                        if (!factorsMap.containsKey(j)) {
                            factorsMap.put(j, i);
                        } else {
                            unionFind.union(factorsMap.get(j), i);
                        }
                    }
                    int other = num / j;
                    if (other != 1) {
                        if (!factorsMap.containsKey(other)) {
                            factorsMap.put(other, i);
                        } else {
                            unionFind.union(factorsMap.get(other), i);
                        }
                    }
                }
            }
        }
        return unionFind.maxSize();
    }
}
