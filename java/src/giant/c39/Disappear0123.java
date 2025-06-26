package giant.c39;

/**
 * 0123字符消除全消子序列最大长度问题
 * 
 * 问题描述：
 * 一个子序列的消除规则如下：
 * 1. 如果'1'的左边有'0'，那么"01"可以消除
 * 2. 如果'3'的左边有'2'，那么"23"可以消除  
 * 3. 消除后字符自动贴在一起，可以继续寻找消除机会
 * 
 * 示例：子序列"0231"，先消除"23"得到"01"，再消除"01"得到空串
 * 
 * 问题目标：
 * 从只包含'0'、'1'、'2'、'3'的字符串中，找到"全消子序列"的最大长度
 * 
 * 解题思路：
 * 使用区间动态规划 + 递归分治：
 * 1. 对于区间[l,r]，考虑所有可能的消除方案
 * 2. 如果s[l]是'0'或'2'，尝试找到对应的'1'或'3'进行配对
 * 3. 配对后问题分解为三部分：左部分 + 配对的2个字符 + 右部分
 * 4. 如果s[l]是'1'或'3'，无法作为左端点消除，跳过
 * 5. 递归求解所有子问题，取最大值
 * 
 * 算法核心：
 * - 状态：区间[l,r]内能消除的最大字符数
 * - 转移：尝试所有可能的配对方案
 * - 边界：空区间或单字符无法消除
 * 
 * 时间复杂度：O(n³)，区间DP的标准复杂度
 * 空间复杂度：O(n²)，可以用记忆化优化
 * 
 * 来源：真实笔试题（大厂）
 * 
 * @author Zhu Runqi
 */
public class Disappear0123 {
    
    /**
     * 递归计算区间[l,r]内能消除的最大字符数
     * 
     * 算法思路：
     * 1. 尝试跳过左端点字符s[l]
     * 2. 如果s[l]是'0'或'2'，尝试找到对应的配对字符
     * 3. 对于每个可能的配对，递归求解左中右三部分
     * 4. 返回所有方案中的最大值
     * 
     * @param s 字符数组
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 区间[l,r]内能消除的最大字符数
     */
    private static int process(char[] s, int l, int r) {
        // 边界情况：空区间或负区间
        if (l >= r) {
            return 0;
        }
        
        // 边界情况：只有两个字符
        if (l == r - 1) {
            // 检查是否能配对消除
            return (s[l] == '0' && s[r] == '1') || (s[l] == '2' && s[r] == '3') ? 2 : 0;
        }
        
        // 方案1：跳过左端点字符s[l]，递归处理剩余区间
        int p1 = process(s, l + 1, r);
        
        // 如果s[l]是'1'或'3'，无法作为左端点配对，只能跳过
        if (s[l] == '1' || s[l] == '3') {
            return p1;
        }
        
        // 方案2：s[l]是'0'或'2'，尝试找到配对字符
        int p2 = 0;
        char find = s[l] == '0' ? '1' : '3';  // 确定要找的配对字符
        
        // 遍历所有可能的配对位置
        for (int i = l + 1; i <= r; i++) {
            if (s[i] == find) {
                // 找到配对字符，分解为三部分：
                // 1. 中间部分 [l+1, i-1]
                // 2. 配对的两个字符 (贡献2分)
                // 3. 右边部分 [i+1, r]
                int leftPart = process(s, l + 1, i - 1);
                int rightPart = process(s, i + 1, r);
                p2 = Math.max(p2, leftPart + 2 + rightPart);
            }
        }
        
        // 返回跳过和配对两种方案的最大值
        return Math.max(p1, p2);
    }

    /**
     * 求字符串中全消子序列的最大长度
     * 
     * @param str 输入字符串，只包含'0','1','2','3'
     * @return 全消子序列的最大长度
     */
    public static int maxDisappear(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        return process(str.toCharArray(), 0, str.length() - 1);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 0123字符消除全消子序列最大长度问题 ===\n");
        
        // 测试用例1：简单配对
        System.out.println("测试用例1：简单配对");
        String str1 = "010101";
        int result1 = maxDisappear(str1);
        System.out.println("输入字符串: " + str1);
        System.out.println("最大长度: " + result1);
        System.out.println("分析: 可以配对消除\"01\", \"01\", \"01\"，总长度为6");
        System.out.println("消除过程: 010101 -> 0101 -> 01 -> 空");
        System.out.println();
        
        // 测试用例2：混合配对
        System.out.println("测试用例2：混合配对");
        String str2 = "021331";
        int result2 = maxDisappear(str2);
        System.out.println("输入字符串: " + str2);
        System.out.println("最大长度: " + result2);
        System.out.println("分析: 可以配对\"01\"和\"23\"");
        System.out.println("可能的消除方案: 0-1配对, 2-3配对, 剩下3-1无法配对");
        System.out.println();
        
        // 测试用例3：题目示例
        System.out.println("测试用例3：题目示例");
        String str3 = "0231";
        int result3 = maxDisappear(str3);
        System.out.println("输入字符串: " + str3);
        System.out.println("最大长度: " + result3);
        System.out.println("分析: 先消除\"23\"得到\"01\"，再消除\"01\"得到空串");
        System.out.println("消除过程: 0231 -> 01 -> 空，总长度为4");
        System.out.println();
        
        // 测试用例4：无法配对
        System.out.println("测试用例4：无法配对");
        String str4 = "1100";
        int result4 = maxDisappear(str4);
        System.out.println("输入字符串: " + str4);
        System.out.println("最大长度: " + result4);
        System.out.println("分析: 没有有效的配对，最大长度为0");
        System.out.println();
        
        // 测试用例5：复杂嵌套
        System.out.println("测试用例5：复杂嵌套");
        String str5 = "002233011";
        int result5 = maxDisappear(str5);
        System.out.println("输入字符串: " + str5);
        System.out.println("最大长度: " + result5);
        System.out.println("分析: 多种配对方案，需要动态规划找到最优解");
        System.out.println();
        
        // 测试用例6：边界情况
        System.out.println("测试用例6：边界情况");
        String str6 = "01";
        int result6 = maxDisappear(str6);
        System.out.println("输入字符串: " + str6);
        System.out.println("最大长度: " + result6);
        System.out.println("分析: 简单的01配对，长度为2");
        System.out.println();
        
        String str7 = "23";
        int result7 = maxDisappear(str7);
        System.out.println("输入字符串: " + str7);
        System.out.println("最大长度: " + result7);
        System.out.println("分析: 简单的23配对，长度为2");
        System.out.println();
        
        System.out.println("=== 算法原理解析 ===");
        System.out.println("1. 消除规则：");
        System.out.println("   - \"01\"可以消除：'1'的左边有'0'");
        System.out.println("   - \"23\"可以消除：'3'的左边有'2'");
        System.out.println("   - 消除后字符自动贴合，可能产生新的消除机会");
        System.out.println();
        System.out.println("2. 区间DP思想：");
        System.out.println("   - 对于区间[l,r]，考虑左端点的所有可能配对");
        System.out.println("   - 配对后问题分解为独立的子问题");
        System.out.println("   - 递归求解，取最大值");
        System.out.println();
        System.out.println("3. 状态转移：");
        System.out.println("   - 跳过当前字符：process(l+1, r)");
        System.out.println("   - 配对消除：process(l+1, i-1) + 2 + process(i+1, r)");
        System.out.println("   - 取两者最大值");
        System.out.println();
        System.out.println("4. 复杂度分析：");
        System.out.println("   - 时间复杂度：O(n³)，标准区间DP");
        System.out.println("   - 空间复杂度：O(n)，递归栈 + O(n²)记忆化");
        System.out.println("   - 可以用记忆化进一步优化");
        System.out.println();
        System.out.println("5. 实际应用：");
        System.out.println("   - 括号匹配问题的变种");
        System.out.println("   - 序列配对优化问题");
        System.out.println("   - 动态规划经典案例");
    }
}
