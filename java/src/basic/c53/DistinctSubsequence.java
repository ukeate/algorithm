package basic.c53;

import java.util.Arrays;

// 求字面值不同子序列的数量
public class DistinctSubsequence {
    public static int distinct1(String s) {
        char[] str = s.toCharArray();
        int rst = 0;
        // [0,i]有多少不同子序列
        int[] dp = new int[str.length];
        Arrays.fill(dp, 1);
        for (int i = 0; i < str.length; i++) {
            for (int j = 0; j < i; j++) {
                if (str[j] != str[i]) {
                    dp[i] += dp[j];
                }
            }
            rst += dp[i];
        }
        return rst;
    }

    //

    public static int distinct2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        // 当前字符结尾的数量
        int[] count = new int[26];

        // 不算空集
        int rst = 0;
        for (char x : str) {
            // 字符拼到最后，新出现时有rst种, 重复出现时减去之前出现的拼法数
            // +1是自己本身, 算空集时去掉
            int add = rst + 1 - count[x - 'a'];
            rst += add;
            // 之前拼法数要重复统计
            count[x - 'a'] += add;
        }
        return rst;
    }

    //

    private static String randomStr(int maxLen, int variable) {
        int size = (int) (maxLen * Math.random()) + 1;
        char[] str = new char[size];
        for (int i = 0; i < size; i++) {
            str[i] = (char) ((int) (variable * Math.random()) + 'a');
        }
        return String.valueOf(str);
    }

    public static void main(String[] args) {
        int maxLen = 10;
        int variable = 5;
        System.out.println("test begin");
        for (int i = 0; i < 1000000; i++) {
            String s = randomStr(maxLen, variable);
            if (distinct1(s) != distinct2(s)) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
