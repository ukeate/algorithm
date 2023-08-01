package giant.c4;

import java.util.ArrayList;
import java.util.HashMap;

// 查询(0, 3, 2)表示0到3范围内2的数量
public class QueryHobby {
    public static class QueryBox1 {
        private int[] arr;

        public QueryBox1(int[] in) {
            arr = new int[in.length];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = in[i];
            }
        }

        public int query(int l, int r, int v) {
            int ans = 0;
            for (; l <= r; l++) {
                if (arr[l] == v) {
                    ans++;
                }
            }
            return ans;
        }
    }

    //

    public static class QueryBox2 {
        private HashMap<Integer, ArrayList<Integer>> map;
        public QueryBox2(int[] arr) {
            map = new HashMap<>();
            for (int i = 0; i < arr.length; i++) {
                if (!map.containsKey(arr[i])) {
                    map.put(arr[i], new ArrayList<>());
                }
                map.get(arr[i]).add(i);
            }
        }

        private int countLess(ArrayList<Integer> arr, int limit) {
            int l = 0, r = arr.size() - 1;
            int mostRight = -1;
            while (l <= r) {
                int mid = l + ((r - l) >> 1);
                if (arr.get(mid) < limit) {
                    mostRight = mid;
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }
            return mostRight + 1;
        }

        public int query(int l, int r, int v) {
            if (!map.containsKey(v)) {
                return 0;
            }
            ArrayList<Integer> idxArr = map.get(v);
            int a = countLess(idxArr, l);
            int b = countLess(idxArr, r + 1);
            return b - a;
        }
    }

    //

    private static int[] randomArr(int len, int val) {
        int[] ans = new int[(int) (Math.random() * len) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (Math.random() * val) + 1;
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 1000;
        int queryTimes = 1000;
        int len = 300;
        int val = 20;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(len, val);
            int n = arr.length;
            QueryBox1 box1 = new QueryBox1(arr);
            QueryBox2 box2 = new QueryBox2(arr);
            for (int j = 0; j < queryTimes; j++) {
                int a = (int) (Math.random() * n);
                int b = (int) (Math.random() * n);
                int l = Math.min(a, b);
                int r = Math.max(a, b);
                int v = (int) (Math.random() * val) + 1;
                if (box1.query(l, r, v) != box2.query(l, r, v)) {
                    System.out.println("Wrong");
                    break;
                }
            }
        }
        System.out.println("test end");
    }
}
