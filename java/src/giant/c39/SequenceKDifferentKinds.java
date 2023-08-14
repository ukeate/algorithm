package giant.c39;

// 来自百度
// 给定一个字符串str，和一个正数k
// str子序列的字符种数必须是k种，返回有多少子序列满足这个条件
// 已知str中都是小写字母
public class SequenceKDifferentKinds {
    private static int math(int n) {
        return (1 << n) - 1;
    }

    private static int ways(int[] c, int i, int rest) {
       if (rest == 0) {
           return 1;
       }
       if (i == c.length) {
           return 0;
       }
       return math(c[i]) * ways(c, i + 1, rest - 1) + ways(c, i + 1, rest);
    }

    public static int nums(String s, int k) {
        char[] str = s.toCharArray();
        int[] counts = new int[26];
        for (char c : str) {
            counts[c - 97]++;
        }
        return ways(counts, 0, k);
    }

    public static void main(String[] args) {
        String s = "acbbca";
        int k = 3;
        System.out.println(nums(s, k));
    }
}
