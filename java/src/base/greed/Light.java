package base.greed;

import java.util.HashSet;

public class Light {
    private static int process1(char[] str, int idx, HashSet<Integer> lights) {
        if (idx == str.length) {
            for (int i = 0; i < str.length; i++) {
                if (str[i] == '.') {
                    if (!lights.contains(i - 1) && !lights.contains(i) && !lights.contains(i + 1)) {
                        return Integer.MAX_VALUE;
                    }
                }
            }
            return lights.size();
        }
        int no = process1(str, idx + 1, lights);
        int yes = Integer.MAX_VALUE;
        if (str[idx] == '.') {
            lights.add(idx);
            yes = process1(str, idx + 1, lights);
            lights.remove(idx);
        }
        return Math.min(no, yes);
    }

    public static int minLight1(String road) {
        if (road == null || road.length() == 0) {
            return 0;
        }
        return process1(road.toCharArray(), 0, new HashSet<>());
    }

    //

    public static int minLight2(String road) {
        char[] str = road.toCharArray();
        int i = 0;
        int light = 0;
        while (i < str.length) {
            if (str[i] == 'X') {
                i++;
            } else {
                light++;
                if (i + 1 == str.length) {
                    break;
                } else {
                    if (str[i + 1] == 'X') {
                        i = i + 2;
                    } else {
                        i = i + 3;
                    }
                }
            }
        }
        return light;
    }

    //

    public static int minLight3(String road) {
        char[] str = road.toCharArray();
        int cur = 0;
        int light = 0;
        for (char c : str) {
            if (c == 'X') {
                light += (cur + 2) / 3;
                cur = 0;
            } else {
                cur++;
            }
        }
        light += (cur + 2) / 3;
        return light;
    }

    private static String randomStr(int maxLen) {
        char[] res = new char[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < res.length; i++) {
            res[i] = Math.random() < 0.5 ? 'X' : '.';
        }
        return String.valueOf(res);
    }

    public static void main(String[] args) {
        int maxLen = 20;
        int times = 100000;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            String str = randomStr(maxLen);
            int ans1 = minLight1(str);
            int ans2 = minLight2(str);
            int ans3 = minLight3(str);
            if (ans1 != ans2 || ans2 != ans3) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
