package basic.c31;

// 染色后，红色都比绿色离左近, RGRGR -> RRRGG, 返回最小涂改
public class ColorLeftRight {
    public static int min(String s) {
        if (s == null || s.length() < 2) {
            return 0;
        }
        char[] str = s.toCharArray();
        int n = str.length;
        // 从辅助数组简化来
        int rAll = 0;
        for (int i = 0; i < n; i++) {
            rAll += str[i] == 'R' ? 1 : 0;
        }
        int ans = rAll;
        int left  = 0;
        // 枚举分界线,[0, i]是左侧
        for (int i = 0; i < n - 1; i++) {
            left += str[i] == 'G' ? 1 : 0;
            rAll -= str[i] == 'R' ? 1 : 0;
            ans = Math.min(ans, left + rAll);
        }
        // [0, n-1]为左侧
        ans = Math.min(ans, left + (str[n - 1] == 'G' ? 1 : 0));
        return ans;
    }

    public static void main(String[] args) {
        String s = "GGGGGR";
        System.out.println(min(s));
    }
}
