package leetc.top;

/**
 * LeetCode 76. 最小覆盖子串 (Minimum Window Substring)
 * 
 * 问题描述：
 * 给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。
 * 如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 ""。
 * 
 * 注意：
 * - 对于 t 中重复字符，我们寻找的子字符串中该字符数量必须不少于 t 中该字符数量
 * - 如果 s 中存在这样的子串，我们保证它是唯一的答案
 * 
 * 示例：
 * - 输入：s = "ADOBECODEBANC", t = "ABC"，输出："BANC"
 * - 解释：最小覆盖子串"BANC"包含来自字符串t的'A'、'B'和'C'
 * 
 * 解法思路：
 * 滑动窗口算法：
 * 1. 使用左右指针l、r维护一个滑动窗口
 * 2. 右指针r不断扩展窗口，直到窗口包含t的所有字符
 * 3. 当窗口满足条件时，尝试收缩左边界l，寻找更小的窗口
 * 4. 用map数组记录每个字符的需求量和当前窗口中的数量
 * 5. 用need变量记录还需要多少个字符才能满足条件
 * 
 * 核心技巧：
 * - 负数表示窗口中该字符数量超过了需求
 * - 0表示恰好满足需求
 * - 正数表示还需要更多该字符
 * 
 * 时间复杂度：O(m + n) - m和n分别是s和t的长度，每个字符最多被访问2次
 * 空间复杂度：O(1) - 使用固定大小的字符计数数组（256个ASCII字符）
 */
public class P76_MinimumWindowSubstring {
    
    /**
     * 寻找最小覆盖子串
     * 
     * 算法步骤：
     * 1. 初始化字符需求数组：map[c]表示字符c还需要多少个
     * 2. 使用双指针维护滑动窗口：
     *    - 右指针r扩展窗口：将字符加入窗口，更新需求
     *    - 左指针l收缩窗口：当窗口满足条件时，尝试缩小
     * 3. 窗口状态判断：
     *    - need == 0：窗口包含t的所有字符
     *    - need > 0：窗口还不满足条件
     * 4. 记录最小窗口的位置和长度
     * 
     * @param str 源字符串s
     * @param target 目标字符串t
     * @return s中涵盖t所有字符的最小子串
     */
    public static String minWindow(String str, String target) {
        if (str == null || target == null || str.length() < target.length()) {
            return "";
        }
        
        char[] s = str.toCharArray();
        char[] t = target.toCharArray();
        
        // map[c]记录字符c的需求量（正数表示需要，负数表示多余）
        int[] map = new int[256];
        for (char c : t) {
            map[c]++;  // 初始化需求：每个字符需要多少个
        }
        
        int l = 0, r = 0;              // 滑动窗口的左右边界
        int need = t.length;           // 还需要多少个字符才能满足条件
        int minLen = -1;               // 最小窗口长度（-1表示未找到）
        int ansl = -1, ansr = -1;      // 最小窗口的左右边界
        
        // 滑动窗口主循环
        while (r < s.length) {
            // 右指针扩展：将s[r]加入窗口
            map[s[r]]--;  // 减少该字符的需求量
            
            // 如果map[s[r]] >= 0，说明这个字符是t中需要的
            // （如果map[s[r]] < 0，说明这个字符多余了）
            if (map[s[r]] >= 0) {
                need--;  // 满足了一个字符的需求
            }
            
            // 当窗口满足条件时（包含t的所有字符）
            if (need == 0) {
                // 尝试收缩左边界，寻找更小的窗口
                while (map[s[l]] < 0) {
                    // s[l]是多余的字符，可以移出窗口
                    map[s[l++]]++;
                }
                
                // 更新最小窗口
                if (minLen == -1 || minLen > r - l + 1) {
                    minLen = r - l + 1;
                    ansl = l;
                    ansr = r;
                }
                
                // 移动左边界，破坏当前窗口的满足条件
                need++;          // 即将失去一个必需字符
                map[s[l++]]++;   // 恢复该字符的需求
            }
            
            r++;  // 右指针继续扩展
        }
        
        // 返回结果
        return minLen == -1 ? "" : str.substring(ansl, ansr + 1);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准情况
        String s1 = "ADOBECODEBANC", t1 = "ABC";
        System.out.println("输入: s=\"" + s1 + "\", t=\"" + t1 + "\"");
        System.out.println("输出: \"" + minWindow(s1, t1) + "\"");
        System.out.println("期望: \"BANC\"");
        System.out.println();
        
        // 测试用例2：无解情况
        String s2 = "a", t2 = "aa";
        System.out.println("输入: s=\"" + s2 + "\", t=\"" + t2 + "\"");
        System.out.println("输出: \"" + minWindow(s2, t2) + "\"");
        System.out.println("期望: \"\"");
        System.out.println();
        
        // 测试用例3：完全匹配
        String s3 = "a", t3 = "a";
        System.out.println("输入: s=\"" + s3 + "\", t=\"" + t3 + "\"");
        System.out.println("输出: \"" + minWindow(s3, t3) + "\"");
        System.out.println("期望: \"a\"");
        System.out.println();
        
        // 测试用例4：重复字符
        String s4 = "ADOBECODEBANC", t4 = "AABC";
        System.out.println("输入: s=\"" + s4 + "\", t=\"" + t4 + "\"");
        System.out.println("输出: \"" + minWindow(s4, t4) + "\"");
        System.out.println("期望: \"ADOBECODEBA\" 或其他包含2个A、1个B、1个C的最小子串");
        System.out.println();
        
        // 测试用例5：t比s长
        String s5 = "ab", t5 = "abc";
        System.out.println("输入: s=\"" + s5 + "\", t=\"" + t5 + "\"");
        System.out.println("输出: \"" + minWindow(s5, t5) + "\"");
        System.out.println("期望: \"\"");
        System.out.println();
        
        // 性能测试提示
        System.out.println("算法特点：");
        System.out.println("- 时间复杂度：O(m + n)，每个字符最多被访问2次");
        System.out.println("- 空间复杂度：O(1)，使用固定大小的字符计数数组");
        System.out.println("- 核心思想：滑动窗口 + 字符计数");
    }
}
