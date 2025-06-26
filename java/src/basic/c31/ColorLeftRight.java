package basic.c31;

/**
 * 颜色左右排列问题
 * 
 * 问题描述：
 * 给定一个只包含'R'（红色）和'G'（绿色）的字符串，要求重新染色，
 * 使得所有红色都在绿色的左边，即形如"RRR...GGG"的模式。
 * 返回最少需要重新染色的字符数量。
 * 
 * 例如：RGRGR -> RRRGG，需要重新染色2个字符
 * 
 * 解法思路：
 * 动态规划思想，枚举所有可能的分界线位置：
 * 1. 对于每个位置i，计算将[0,i]全部染成红色，[i+1,n-1]全部染成绿色的代价
 * 2. 代价 = 左侧绿色字符数量 + 右侧红色字符数量
 * 3. 遍历所有分界线，取最小代价
 * 
 * 优化技巧：
 * - 预先统计总的红色字符数量
 * - 从左到右遍历时，动态维护左侧绿色数和右侧红色数
 * - 避免重复计算，一次遍历完成
 * 
 * 时间复杂度：O(n) - 只需遍历字符串两次
 * 空间复杂度：O(1) - 只使用常数额外空间
 */
public class ColorLeftRight {
    /**
     * 计算最少重新染色数量
     * 
     * @param s 输入的颜色字符串，只包含'R'和'G'
     * @return 最少需要重新染色的字符数量
     */
    public static int min(String s) {
        if (s == null || s.length() < 2) {
            return 0;  // 少于2个字符无需染色
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        
        // 第一步：统计字符串中红色字符的总数
        int rAll = 0;
        for (int i = 0; i < n; i++) {
            rAll += str[i] == 'R' ? 1 : 0;
        }
        
        // 初始答案：将所有字符都染成绿色的代价（即所有红色字符数量）
        int ans = rAll;
        int left = 0;  // 左侧绿色字符数量
        
        // 枚举分界线位置：[0, i]作为红色区域，[i+1, n-1]作为绿色区域
        for (int i = 0; i < n - 1; i++) {
            // 如果当前字符是绿色，需要染成红色，左侧绿色数+1
            left += str[i] == 'G' ? 1 : 0;
            // 如果当前字符是红色，从右侧红色总数中减去，因为它已经在左侧了
            rAll -= str[i] == 'R' ? 1 : 0;
            // 更新最小代价：左侧绿色数量 + 右侧红色数量
            ans = Math.min(ans, left + rAll);
        }
        
        // 考虑边界情况：整个字符串都作为红色区域
        ans = Math.min(ans, left + (str[n - 1] == 'G' ? 1 : 0));
        
        return ans;
    }

    public static void main(String[] args) {
        String s = "GGGGGR";
        System.out.println(min(s));
    }
}
