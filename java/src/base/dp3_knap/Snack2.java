package base.dp3_knap;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

// https://www.nowcoder.com/questionTerminal/d94bb2fa461d42bcb4c0f2b94f5d4281
public class Snack2 {
    private static long process(int[] arr, int idx, long w, int end, int bag, TreeMap<Long, Long> map) {
        if (w > bag) {
            return 0;
        }
        if (idx > end) {
            if (w == 0) {
                return 0;
            }
            if (!map.containsKey(w)) {
                map.put(w, 1L);
            } else {
                map.put(w, map.get(w) + 1);
            }
            return 1;
        } else {
            long ways = process(arr, idx + 1, w, end, bag, map);
            ways += process(arr, idx + 1, w + arr[idx], end, bag, map);
            return ways;
        }
    }

    private static long ways(int[] arr, int bag) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0] <= bag ? 2 : 1;
        }
        int mid = (arr.length - 1) >> 1;
        TreeMap<Long, Long> lmap = new TreeMap<>();
        long ways = process(arr, 0, 0, mid, bag, lmap);
        TreeMap<Long, Long> rmap = new TreeMap<>();
        ways += process(arr, mid + 1, 0, arr.length - 1, bag, rmap);
        TreeMap<Long, Long> rpre = new TreeMap<>();
        long pre = 0;
        for (Map.Entry<Long, Long> entry : rmap.entrySet()) {
            pre += entry.getValue();
            rpre.put(entry.getKey(), pre);
        }
        for (Map.Entry<Long, Long> entry : lmap.entrySet()) {
            long lweight = entry.getKey();
            long lways = entry.getValue();
            Long floor = rpre.floorKey(bag - lweight);
            if (floor != null) {
                long rways = rpre.get(floor);
                ways += lways * rways;
            }
        }
        return ways + 1;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((System.in)));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            in.nextToken();
            int bag = (int) in.nval;
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                in.nextToken();
                arr[i] = (int) in.nval;
            }
            long ways = ways(arr, bag);
            out.println(ways);
            out.flush();
        }
    }
}
