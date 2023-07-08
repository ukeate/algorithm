package basic.c34;

// 子矩阵最大累加和
public class SubMatrixMaxSum {
    public static int max(int[][] m) {
        if (m == null || m.length == 0 || m[0].length == 0) {
            return 0;
        }
        int max = Integer.MIN_VALUE;
        int cur = 0;
        int[] s = null;
        for (int i = 0; i < m.length; i++) {
            s = new int[m[0].length];
            for (int j = i; j < m.length; j++) {
                cur = 0;
                for (int k = 0; k < s.length; k++) {
                    s[k] += m[j][k];
                    cur += s[k];
                    max = Math.max(max, cur);
                    cur = cur < 0 ? 0 : cur;
                }
            }
        }
        return max;
    }

    public static void main(String[] args) {
        int[][] m = {{-90, 48, 78}, {64, -40, 64}, {-81, -7, 66}};
        System.out.println(max(m));
    }
}

