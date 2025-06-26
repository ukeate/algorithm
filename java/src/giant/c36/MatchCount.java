package giant.c36;

/**
 * 字符串匹配计数问题
 * 
 * 问题描述：
 * 给定两个字符串s1和s2，返回在s1中有多少个子串等于s2
 * 
 * 解题思路：
 * 使用KMP算法的变形来解决多次匹配问题
 * 1. 构建模式串s2的next数组（失配函数）
 * 2. 在主串s1中使用KMP算法进行匹配
 * 3. 每次找到完全匹配时，计数器加1，然后利用next数组继续寻找下一个匹配
 * 
 * 算法优势：
 * - 时间复杂度：O(n + m)，其中n是主串长度，m是模式串长度
 * - 空间复杂度：O(m)，用于存储next数组
 * - 相比暴力匹配O(nm)的复杂度，效率大大提升
 * 
 * 来源：美团面试题
 * 
 * @author Zhu Runqi
 */
public class MatchCount {

    /**
     * 构建KMP算法的next数组（失配函数）
     * next数组记录了模式串中每个位置的最长相等前后缀长度
     * 
     * @param str2 模式串字符数组
     * @return next数组，长度比原字符串多1
     */
    private static int[] next(char[] str2) {
        if (str2.length == 1) {
            return new int[]{-1, 0};
        }
        
        int[] next = new int[str2.length + 1];
        // 初始化：next[0] = -1表示没有前缀，next[1] = 0表示第一个字符的最长前后缀为0
        next[0] = -1;
        next[1] = 0;
        
        int i = 2;  // 当前要计算next值的位置
        int cn = 0; // 前一个位置的最长前后缀长度
        
        while (i < next.length) {
            if (str2[i - 1] == str2[cn]) {
                // 当前字符和前缀字符匹配，next值递增
                next[i++] = ++cn;
            } else if (cn > 0) {
                // 不匹配时，跳到更短的前缀尝试
                cn = next[cn];
            } else {
                // 没有可匹配的前缀，next值为0
                next[i++] = 0;
            }
        }
        return next;
    }

    /**
     * 统计str1中包含str2的子串个数
     * 使用KMP算法进行高效字符串匹配
     * 
     * @param str1 主串字符数组
     * @param str2 模式串字符数组
     * @return str1中包含str2的子串个数
     */
    public static int count(char[] str1, char[] str2) {
        int x = 0;      // str1的当前位置
        int y = 0;      // str2的当前位置  
        int count = 0;  // 匹配次数统计
        int[] next = next(str2);
        
        while (x < str1.length) {
            if (str1[x] == str2[y]) {
                // 字符匹配，两个指针都前进
                x++;
                y++;
                if (y == str2.length) {
                    // 找到一个完整匹配
                    count++;
                    // 利用next数组继续寻找下一个可能的匹配位置
                    y = next[y];
                }
            } else if (next[y] == -1) {
                // 模式串指针已经到达起始位置，主串指针前进
                x++;
            } else {
                // 利用next数组跳过部分字符，避免重复比较
                y = next[y];
            }
        }
        return count;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：重叠匹配
        String s1 = "abababa";
        String s2 = "aba";
        int result1 = count(s1.toCharArray(), s2.toCharArray());
        System.out.println("测试用例1:");
        System.out.println("主串: " + s1);
        System.out.println("模式串: " + s2);
        System.out.println("匹配次数: " + result1 + " (期望: 3)");
        System.out.println("匹配位置: 0-2, 2-4, 4-6");
        System.out.println();
        
        // 测试用例2：无重叠匹配
        String s3 = "abcabcabc";
        String s4 = "abc";
        int result2 = count(s3.toCharArray(), s4.toCharArray());
        System.out.println("测试用例2:");
        System.out.println("主串: " + s3);
        System.out.println("模式串: " + s4);
        System.out.println("匹配次数: " + result2 + " (期望: 3)");
        System.out.println();
        
        // 测试用例3：无匹配
        String s5 = "abcdef";
        String s6 = "xyz";
        int result3 = count(s5.toCharArray(), s6.toCharArray());
        System.out.println("测试用例3:");
        System.out.println("主串: " + s5);
        System.out.println("模式串: " + s6);
        System.out.println("匹配次数: " + result3 + " (期望: 0)");
        System.out.println();
        
        // 测试用例4：单字符重复
        String s7 = "aaaaaaa";
        String s8 = "aa";
        int result4 = count(s7.toCharArray(), s8.toCharArray());
        System.out.println("测试用例4:");
        System.out.println("主串: " + s7);
        System.out.println("模式串: " + s8);
        System.out.println("匹配次数: " + result4 + " (期望: 6)");
        
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("时间复杂度: O(n + m)，其中n是主串长度，m是模式串长度");
        System.out.println("空间复杂度: O(m)，用于存储next数组");
        System.out.println("相比暴力算法O(nm)，效率提升显著");
    }
}
