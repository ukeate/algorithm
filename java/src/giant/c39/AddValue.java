package giant.c39;

// 来自腾讯
// 给定一个只由0和1组成的字符串S，假设下标从1开始，规定i位置的字符价值V[i]计算方式如下 :
// 1) i == 1时，V[i] = 1
// 2) i > 1时，如果S[i] != S[i-1]，V[i] = 1
// 3) i > 1时，如果S[i] == S[i-1]，V[i] = V[i-1] + 1
// 你可以随意删除S中的字符，返回整个S的最大价值
// 字符串长度<=5000
public class AddValue {

    private static int process1(int[] arr, int idx, int lastNum, int baseVal) {
        if (idx == arr.length) {
            return 0;
        }
        int curVal = lastNum == arr[idx] ? (baseVal + 1) : 1;
        int next1 = process1(arr, idx + 1, arr[idx], curVal);
        int next2 = process1(arr, idx + 1, lastNum, baseVal);
        return Math.max(curVal + next1, next2);
    }

    public static int max1(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        int[] arr = new int[str.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = str[i] == '0' ? 0 : 1;
        }
        return process1(arr, 0, 0, 0);
    }
}
