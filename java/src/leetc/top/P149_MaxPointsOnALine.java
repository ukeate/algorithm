package leetc.top;

import java.util.HashMap;
import java.util.Map;

public class P149_MaxPointsOnALine {
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b,  a % b);
    }

    public static int maxPoints(int[][] points) {
        if (points == null) {
            return 0;
        }
        if (points.length <= 2) {
            return points.length;
        }
        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        int rst = 0;
        for (int i = 0; i < points.length; i++) {
            map.clear();
            int samePos = 1, sameX = 0, sameY = 0, line = 0;
            for (int j = i + 1; j < points.length; j++) {
                int x = points[j][0] - points[i][0];
                int y = points[j][1] - points[i][1];
                if (x == 0 && y == 0) {
                    samePos++;
                } else if (x == 0) {
                    sameX++;
                } else if (y == 0) {
                    sameY++;
                } else {
                    int gcd = gcd(x, y);
                    x /= gcd;
                    y /= gcd;
                    if (!map.containsKey(x)) {
                        map.put(x, new HashMap<>());
                    }
                    if (!map.get(x).containsKey(y)) {
                        map.get(x).put(y, 0);
                    }
                    map.get(x).put(y, map.get(x).get(y) + 1);
                    line = Math.max(line, map.get(x).get(y));
                }
            }
            rst = Math.max(rst, Math.max(Math.max(sameX, sameY), line) + samePos);
        }
        return rst;
    }
}
