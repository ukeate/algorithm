package leetc.top;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class P751_IPToCIDR {
    private static int status(String ip) {
        int ans = 0;
        int move = 24;
        for (String str : ip.split("\\.")) {
            ans |= Integer.valueOf(str) << move;
            move -= 8;
        }
        return ans;
    }

    private static HashMap<Integer, Integer> map = new HashMap<>();

    private static int mostRightPower(int num) {
        if (map.isEmpty()) {
            map.put(0, 32);
            for (int i = 0; i < 32; i++) {
                map.put(1 << i, i);
            }
        }
        return map.get(num & (-num));
    }

    private static String content(int status, int power) {
        StringBuilder builder = new StringBuilder();
        for (int move = 24; move >= 0; move -= 8) {
            builder.append(((status & (255 << move)) >>> move) + ".");
        }
        builder.setCharAt(builder.length() - 1, '/');
        builder.append(32 - power);
        return builder.toString();
    }

    public static List<String> ipToCIDR(String ip, int n) {
        int cur = status(ip);
        int maxPower = 0;
        int solved = 0;
        int power = 0;
        List<String> ans = new ArrayList<>();
        while (n > 0) {
            maxPower = mostRightPower(cur);
            solved = 1;
            power = 0;
            while ((solved << 1) <= n && (power + 1) <= maxPower) {
                solved <<= 1;
                power++;
            }
            ans.add(content(cur, power));
            n -= solved;
            cur += solved;
        }
        return ans;
    }
}
