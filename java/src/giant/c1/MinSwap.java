package giant.c1;

// 可以左全G右全B，也可以左全B右全G，返回最小交换次数
public class MinSwap {
    public static int min(String s) {
        if (s == null || s.equals("")) {
            return 0;
        }
        char[] str = s.toCharArray();
        int steps1 = 0;
        int gi = 0;
        int steps2 = 0;
        int bi = 0;
        for (int i = 0; i < str.length; i++) {
            if (str[i] == 'G') {
                steps1 += i - (gi++);
            } else {
                steps2 += i - (bi++);
            }
        }
        return Math.min(steps1, steps2);
    }

}
