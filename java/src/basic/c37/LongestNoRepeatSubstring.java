package basic.c37;

/**
 * 无重复字符最长子串问题
 * 
 * 问题描述：
 * 给定一个字符串，找出其中不含有重复字符的最长子串的长度。
 * 
 * 例如：
 * - "abcabcbb" -> "abc"，长度为3
 * - "bbbbb" -> "b"，长度为1  
 * - "pwwkew" -> "wke"，长度为3
 * 
 * 算法思路：
 * 使用滑动窗口技术，维护一个无重复字符的窗口：
 * 1. 用数组记录每个字符最后出现的位置
 * 2. 遍历字符串，维护当前无重复窗口的左边界
 * 3. 当遇到重复字符时，更新左边界为该字符上次出现位置的下一位
 * 4. 持续更新最长长度
 * 
 * 核心技巧：
 * - 使用pre变量表示当前窗口必须包含的最左位置（不能再往左了）
 * - 每个字符的最后位置更新为当前位置
 * - 窗口长度 = 当前位置 - 左边界位置
 * 
 * 时间复杂度：O(N)，每个字符最多访问两次
 * 空间复杂度：O(1)，使用固定大小的数组（256个ASCII字符）
 */
public class LongestNoRepeatSubstring {
    
    /**
     * 计算无重复字符最长子串的长度
     * 
     * 算法详解：
     * 1. 初始化字符位置数组，所有位置设为-1
     * 2. 维护变量pre表示当前窗口的左边界（不能再往左的位置）
     * 3. 遍历字符串：
     *    - 更新pre为当前字符上次出现位置和当前pre的较大值
     *    - 计算当前窗口长度 = i - pre
     *    - 更新最大长度
     *    - 记录当前字符的位置
     * 
     * @param str 输入字符串
     * @return 最长无重复子串长度
     */
    public static int max(String str) {
        // 边界条件检查
        if (str == null || str.equals("")) {
            return 0;
        }
        
        char[] s = str.toCharArray();
        int[] map = new int[256];  // ASCII字符位置映射表
        
        // 初始化：所有字符位置设为-1（表示未出现过）
        for (int i = 0; i < 256; i++) {
            map[i] = -1;
        }
        
        int len = 0;    // 最长无重复子串长度
        int pre = -1;   // 当前窗口的左边界（实际左边界是pre+1）
        int cur = 0;    // 当前窗口长度
        
        for (int i = 0; i < s.length; i++) {
            // 更新窗口左边界：
            // 1. 保持当前的pre（窗口不能往左扩展）
            // 2. 如果当前字符之前出现过，左边界移到该字符上次出现位置
            pre = Math.max(pre, map[s[i]]);
            
            // 计算当前窗口长度（从pre+1到i）
            cur = i - pre;
            
            // 更新最大长度
            len = Math.max(len, cur);
            
            // 记录当前字符的位置
            map[s[i]] = i;
        }
        
        return len;
    }

    /**
     * 扩展版本：返回最长无重复子串本身
     * 
     * 与max方法类似，但需要记录最长子串的结束位置，
     * 以便提取具体的子串内容
     * 
     * @param str 输入字符串
     * @return 最长无重复子串
     */
    public static String maxStr(String str) {
        // 边界条件检查
        if (str == null || str.equals("")) {
            return str;
        }
        
        char[] s = str.toCharArray();
        int[] map = new int[256];
        
        // 初始化字符位置为-1
        for (int i = 0; i < 256; i++) {
            map[i] = -1;
        }
        
        int len = -1;   // 最长子串长度（初始化为-1便于比较）
        int pre = -1;   // 窗口左边界
        int cur = 0;    // 当前窗口长度
        int end = -1;   // 最长子串的结束位置
        
        for (int i = 0; i < s.length; i++) {
            // 更新窗口左边界
            pre = Math.max(pre, map[s[i]]);
            
            // 计算当前窗口长度
            cur = i - pre;
            
            // 如果找到更长的子串，更新记录
            if (cur > len) {
                len = cur;
                end = i;  // 记录结束位置
            }
            
            // 更新字符位置
            map[s[i]] = i;
        }
        
        // 提取最长子串：从(end-len+1)到end
        return str.substring(end - len + 1, end + 1);
    }

    /**
     * 生成随机字符串用于测试
     * 
     * @param len 字符串长度
     * @return 随机字符串（只包含小写字母）
     */
    private static String randomStr(int len) {
        char[] str = new char[len];
        int base = 'a';
        int range = 'z' - 'a' + 1;
        
        for (int i = 0; i < len; i++) {
            str[i] = (char)((int) (range * Math.random()) + base);
        }
        
        return String.valueOf(str);
    }
    
    /**
     * 暴力解法：用于验证结果正确性
     * 
     * @param str 输入字符串
     * @return 最长无重复子串长度
     */
    public static int maxBruteForce(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        
        int maxLen = 0;
        
        // 枚举所有可能的子串
        for (int i = 0; i < str.length(); i++) {
            boolean[] visited = new boolean[256];
            
            for (int j = i; j < str.length(); j++) {
                char c = str.charAt(j);
                
                if (visited[c]) {
                    break;  // 遇到重复字符，结束当前子串
                }
                
                visited[c] = true;
                maxLen = Math.max(maxLen, j - i + 1);
            }
        }
        
        return maxLen;
    }

    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 无重复字符最长子串测试 ===");
        
        // 测试用例1：基本情况
        String test1 = "abcabcbb";
        System.out.println("测试用例1: \"" + test1 + "\"");
        System.out.println("最长长度: " + max(test1));
        System.out.println("最长子串: \"" + maxStr(test1) + "\"");
        System.out.println("暴力验证: " + maxBruteForce(test1));
        System.out.println();
        
        // 测试用例2：全部重复
        String test2 = "bbbbb";
        System.out.println("测试用例2: \"" + test2 + "\"");
        System.out.println("最长长度: " + max(test2));
        System.out.println("最长子串: \"" + maxStr(test2) + "\"");
        System.out.println("暴力验证: " + maxBruteForce(test2));
        System.out.println();
        
        // 测试用例3：复杂情况
        String test3 = "pwwkew";
        System.out.println("测试用例3: \"" + test3 + "\"");
        System.out.println("最长长度: " + max(test3));
        System.out.println("最长子串: \"" + maxStr(test3) + "\"");
        System.out.println("暴力验证: " + maxBruteForce(test3));
        System.out.println();
        
        // 测试用例4：无重复
        String test4 = "abcdef";
        System.out.println("测试用例4: \"" + test4 + "\"");
        System.out.println("最长长度: " + max(test4));
        System.out.println("最长子串: \"" + maxStr(test4) + "\"");
        System.out.println("暴力验证: " + maxBruteForce(test4));
        System.out.println();
        
        // 测试用例5：空字符串
        String test5 = "";
        System.out.println("测试用例5: \"\" (空字符串)");
        System.out.println("最长长度: " + max(test5));
        System.out.println("最长子串: \"" + maxStr(test5) + "\"");
        System.out.println();
        
        // 随机测试
        System.out.println("=== 随机测试 ===");
        for (int i = 0; i < 5; i++) {
            String randomTest = randomStr(20);
            int result1 = max(randomTest);
            int result2 = maxBruteForce(randomTest);
            String maxSubstr = maxStr(randomTest);
            
            System.out.println("随机字符串: \"" + randomTest + "\"");
            System.out.println("优化算法: " + result1 + ", 暴力算法: " + result2 + 
                             ", 一致性: " + (result1 == result2));
            System.out.println("最长子串: \"" + maxSubstr + "\" (长度: " + maxSubstr.length() + ")");
            System.out.println();
        }
        
        System.out.println("=== 测试完成 ===");
    }
}

