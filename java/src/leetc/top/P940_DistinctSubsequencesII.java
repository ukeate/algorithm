package leetc.top;

public class P940_DistinctSubsequencesII {
    public static int distinctSubseqII(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        long m = 1000000007;
        char[] str = s.toCharArray();
        long[] count = new long[26];
        // 空集
        long all = 1;
        for (char x : str) {
            long add = (all - count[x - 'a'] + m) % m;
            count[x - 'a'] = (count[x - 'a'] + add) % m;
            all = (all + add) % m;
        }
        all = (all - 1 + m) % m;
        return (int) all;
    }

}
