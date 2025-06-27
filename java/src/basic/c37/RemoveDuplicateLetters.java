package basic.c37;

/**
 * 删除重复字符使字典序最小问题
 * 
 * 问题描述：
 * 给定一个字符串，删除重复字符，使得每个字符只出现一次，
 * 并且结果字符串的字典序最小。
 * 
 * 例如：
 * - "bcabc" -> "abc"
 * - "cbacdcbc" -> "acdb"
 * 
 * 核心思想：
 * 贪心策略 + 递归分治 或 单调栈优化
 * 
 * 方法1：递归分治
 * 1. 找到字符串中字典序最小且后续还会出现的字符
 * 2. 将该字符作为结果的第一个字符
 * 3. 递归处理剩余子串（去掉该字符的所有出现）
 * 
 * 方法2：单调栈优化（更高效）
 * 1. 维护单调递增栈作为候选结果
 * 2. 对于每个字符，如果栈顶字符大于当前字符且后续还会出现，则弹出
 * 3. 避免重复字符进入结果
 * 
 * 时间复杂度：O(N) (方法2) / O(N²) (方法1)
 * 空间复杂度：O(1) (只考虑字符集大小)
 */
public class RemoveDuplicateLetters {
    
    /**
     * 方法1：递归分治解法
     * 
     * 算法思路：
     * 1. 统计每个字符的出现次数
     * 2. 找到字典序最小且后续还会出现的字符作为当前最优选择
     * 3. 递归处理该字符之后的子串，并移除该字符的所有出现
     * 
     * 关键洞察：
     * - 对于每个位置，选择字典序最小的字符作为结果的下一个字符
     * - 但前提是该字符在后续位置还会出现，保证所有字符都能被包含
     * - 一旦字符的计数为0，必须在当前位置之前选定一个字符
     * 
     * @param str 输入字符串
     * @return 删除重复字符后字典序最小的字符串
     */
    public static String remove1(String str) {
        // 边界条件
        if (str == null || str.length() < 2) {
            return str;
        }
        
        // 统计每个字符的出现次数
        int[] map = new int[256];  // ASCII字符计数数组
        for (int i = 0; i < str.length(); i++) {
            map[str.charAt(i)]++;
        }
        
        // 找到字典序最小且后续还会出现的字符位置
        int minIdx = 0;  // 当前最小字符的位置
        
        for (int i = 0; i < str.length(); i++) {
            // 更新最小字符位置：选择字典序更小的字符
            minIdx = str.charAt(minIdx) > str.charAt(i) ? i : minIdx;
            
            // 减少当前字符的计数
            if (--map[str.charAt(i)] == 0) {
                // 当前字符是最后一次出现，必须在此处确定选择
                break;
            }
        }
        
        // 递归处理：
        // 1. 当前选择的字符 + 
        // 2. 递归处理该字符之后的子串（移除所选字符的所有出现）
        return str.charAt(minIdx) + 
               remove1(str.substring(minIdx + 1).replaceAll(String.valueOf(str.charAt(minIdx)), ""));
    }

    /**
     * 方法2：单调栈优化解法
     * 
     * 算法思路：
     * 1. 统计每个字符的总出现次数
     * 2. 维护一个单调递增的结果栈
     * 3. 对于每个字符：
     *    - 如果已在结果中，跳过
     *    - 否则，尝试将其加入结果：
     *      * 如果栈顶字符 > 当前字符 且 栈顶字符后续还会出现，则弹出栈顶
     *      * 重复直到栈为空或栈顶字符 <= 当前字符或栈顶字符不再出现
     *      * 将当前字符入栈
     * 
     * 核心优化：
     * - 使用map数组标记字符状态：-1表示已在结果中，>=0表示剩余出现次数
     * - l和r指针维护当前处理的区间，避免重复扫描
     * 
     * @param s 输入字符串
     * @return 删除重复字符后字典序最小的字符串
     */
    public static String remove2(String s) {
        char[] str = s.toCharArray();
        
        // 统计每个字符的出现次数（只考虑小写字母a-z）
        int[] map = new int[26];
        for (int i = 0; i < str.length; i++) {
            map[str[i] - 'a']++;
        }
        
        char[] res = new char[26];  // 结果数组
        int idx = 0;                // 结果数组的当前位置
        int l = 0;                  // 左指针：当前处理的起始位置
        int r = 0;                  // 右指针：当前扫描位置
        
        while (r < str.length) {
            // 如果当前字符已在结果中(map值为-1)或还有剩余出现次数
            if (map[str[r] - 'a'] == -1 || --map[str[r] - 'a'] > 0) {
                r++;  // 继续扫描
            } else {
                // 当前字符是最后一次出现，必须处理
                
                // 在区间[l, r]中找到最小的未处理字符
                int pick = -1;
                for (int i = l; i <= r; i++) {
                    if (map[str[i] - 'a'] != -1 && 
                        (pick == -1 || str[i] < str[pick])) {
                        pick = i;
                    }
                }
                
                // 将选中的字符加入结果
                res[idx++] = str[pick];
                map[str[pick] - 'a'] = -1;  // 标记为已使用
                
                // 恢复被选字符之后的字符计数（因为它们可能被错误减少）
                for (int i = pick + 1; i <= r; i++) {
                    if (map[str[i] - 'a'] != -1) {
                        map[str[i] - 'a']++;
                    }
                }
                
                // 更新指针：从选中字符的下一个位置开始新一轮
                l = pick + 1;
                r = l;
            }
        }
        
        return String.valueOf(res, 0, idx);
    }
    
    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 删除重复字符使字典序最小测试 ===");
        
        // 测试用例1：基本情况
        String test1 = "bcabc";
        System.out.println("测试用例1:");
        System.out.println("输入: \"" + test1 + "\"");
        System.out.println("方法1结果: \"" + remove1(test1) + "\"");
        System.out.println("方法2结果: \"" + remove2(test1) + "\"");
        System.out.println("期望结果: \"abc\"");
        System.out.println();
        
        // 测试用例2：复杂情况
        String test2 = "cbacdcbc";
        System.out.println("测试用例2:");
        System.out.println("输入: \"" + test2 + "\"");
        System.out.println("方法1结果: \"" + remove1(test2) + "\"");
        System.out.println("方法2结果: \"" + remove2(test2) + "\"");
        System.out.println("期望结果: \"acdb\"");
        System.out.println();
        
        // 测试用例3：已经最优
        String test3 = "abc";
        System.out.println("测试用例3（已最优）:");
        System.out.println("输入: \"" + test3 + "\"");
        System.out.println("方法1结果: \"" + remove1(test3) + "\"");
        System.out.println("方法2结果: \"" + remove2(test3) + "\"");
        System.out.println("期望结果: \"abc\"");
        System.out.println();
        
        // 测试用例4：逆序
        String test4 = "dcba";
        System.out.println("测试用例4（逆序）:");
        System.out.println("输入: \"" + test4 + "\"");
        System.out.println("方法1结果: \"" + remove1(test4) + "\"");
        System.out.println("方法2结果: \"" + remove2(test4) + "\"");
        System.out.println("期望结果: \"abcd\"");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
