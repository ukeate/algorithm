package giant.c39;

/**
 * K种字符的子序列计数问题
 * 
 * 问题描述：
 * 给定一个字符串str和一个正数k，str子序列的字符种数必须是k种
 * 返回有多少子序列满足这个条件
 * 
 * 问题约束：
 * - str中都是小写字母
 * - 子序列必须恰好包含k种不同的字符
 * 
 * 解题思路：
 * 使用递归 + 组合数学：
 * 1. 统计每种字符的出现次数
 * 2. 对于每种字符，要么选择（贡献所有可能的子序列），要么不选择
 * 3. 如果选择某种字符，其贡献为2^count - 1（非空子集）
 * 4. 递归决策，确保恰好选择k种字符
 * 
 * 算法核心：
 * - 状态：当前考虑的字符种类、还需要选择的字符种数
 * - 转移：选择当前字符 vs 不选择当前字符
 * - 目标：恰好选择k种字符的方案数
 * 
 * 时间复杂度：O(26 * k)，最多26种字符，k层递归
 * 空间复杂度：O(k)，递归栈深度
 * 
 * 来源：百度面试题
 * 
 * @author Zhu Runqi
 */
public class SequenceKDifferentKinds {
    
    /**
     * 计算包含n个相同字符时的子序列数量
     * 
     * 对于n个相同字符，可以选择0个、1个、2个...n个，共2^n种选择
     * 但由于要求非空，所以是2^n - 1种选择
     * 
     * @param n 字符出现次数
     * @return 该字符能贡献的子序列数量
     */
    private static int math(int n) {
        return (1 << n) - 1;  // 2^n - 1
    }

    /**
     * 递归计算恰好包含k种字符的子序列数量
     * 
     * 算法思路：
     * 对于第i种字符，有两种选择：
     * 1. 不选择这种字符：直接递归到下一种字符
     * 2. 选择这种字符：该字符贡献math(c[i])种选择，剩余需要rest-1种字符
     * 
     * @param c 字符计数数组，c[i]表示第i种字符的出现次数
     * @param i 当前考虑的字符种类（0-25对应a-z）
     * @param rest 还需要选择多少种字符
     * @return 满足条件的子序列数量
     */
    private static int ways(int[] c, int i, int rest) {
        // 递归边界：恰好选够了k种字符
        if (rest == 0) {
            return 1;
        }
        
        // 递归边界：字符种类用完了，但还没选够k种
        if (i == c.length) {
            return 0;
        }
        
        // 决策1：不选择第i种字符
        int way1 = ways(c, i + 1, rest);
        
        // 决策2：选择第i种字符（前提是这种字符存在）
        int way2 = 0;
        if (c[i] > 0) {
            // 该字符贡献math(c[i])种子序列选择
            // 剩余需要选择rest-1种字符
            way2 = math(c[i]) * ways(c, i + 1, rest - 1);
        }
        
        return way1 + way2;
    }

    /**
     * 计算字符串中恰好包含k种不同字符的子序列数量
     * 
     * @param s 输入字符串
     * @param k 要求的字符种数
     * @return 满足条件的子序列数量
     */
    public static int nums(String s, int k) {
        char[] str = s.toCharArray();
        
        // 统计每种字符的出现次数
        int[] counts = new int[26];
        for (char c : str) {
            counts[c - 'a']++;  // 转换为0-25的索引
        }
        
        // 递归计算结果
        return ways(counts, 0, k);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== K种字符的子序列计数问题 ===\n");
        
        // 测试用例1：题目示例
        System.out.println("测试用例1：题目示例");
        String s1 = "acbbca";
        int k1 = 3;
        int result1 = nums(s1, k1);
        System.out.println("输入字符串: " + s1);
        System.out.println("要求字符种数: " + k1);
        System.out.println("子序列数量: " + result1);
        System.out.println("分析: 字符统计 a:2, b:2, c:2");
        System.out.println("选择3种字符: 选择a,b,c");
        System.out.println("贡献: (2^2-1) * (2^2-1) * (2^2-1) = 3 * 3 * 3 = 27");
        System.out.println();
        
        // 测试用例2：简单情况
        System.out.println("测试用例2：简单情况");
        String s2 = "abc";
        int k2 = 2;
        int result2 = nums(s2, k2);
        System.out.println("输入字符串: " + s2);
        System.out.println("要求字符种数: " + k2);
        System.out.println("子序列数量: " + result2);
        System.out.println("分析: 可以选择{a,b}, {a,c}, {b,c}");
        System.out.println("每种组合贡献: 1 * 1 = 1");
        System.out.println("总数: 3种组合 * 1 = 3");
        System.out.println();
        
        // 测试用例3：重复字符
        System.out.println("测试用例3：重复字符");
        String s3 = "aaabbb";
        int k3 = 2;
        int result3 = nums(s3, k3);
        System.out.println("输入字符串: " + s3);
        System.out.println("要求字符种数: " + k3);
        System.out.println("子序列数量: " + result3);
        System.out.println("分析: 字符统计 a:3, b:3");
        System.out.println("选择a和b: (2^3-1) * (2^3-1) = 7 * 7 = 49");
        System.out.println();
        
        // 测试用例4：k大于字符种数
        System.out.println("测试用例4：k大于字符种数");
        String s4 = "ab";
        int k4 = 3;
        int result4 = nums(s4, k4);
        System.out.println("输入字符串: " + s4);
        System.out.println("要求字符种数: " + k4);
        System.out.println("子序列数量: " + result4);
        System.out.println("分析: 只有2种字符，无法组成3种字符的子序列");
        System.out.println();
        
        // 测试用例5：k=1的情况
        System.out.println("测试用例5：k=1的情况");
        String s5 = "aabbcc";
        int k5 = 1;
        int result5 = nums(s5, k5);
        System.out.println("输入字符串: " + s5);
        System.out.println("要求字符种数: " + k5);
        System.out.println("子序列数量: " + result5);
        System.out.println("分析: 只选择一种字符");
        System.out.println("选择a: 2^2-1 = 3种");
        System.out.println("选择b: 2^2-1 = 3种");  
        System.out.println("选择c: 2^2-1 = 3种");
        System.out.println("总数: 3 + 3 + 3 = 9");
        System.out.println();
        
        System.out.println("=== 算法原理解析 ===");
        System.out.println("1. 组合数学思想：");
        System.out.println("   - 将问题分解为字符种类的选择");
        System.out.println("   - 每种字符独立贡献子序列数量");
        System.out.println("   - 总数量为各字符贡献的乘积");
        System.out.println();
        System.out.println("2. 字符贡献计算：");
        System.out.println("   - n个相同字符可以组成2^n个子集");
        System.out.println("   - 去除空集，实际贡献2^n - 1个子序列");
        System.out.println("   - 这包括选择1个、2个...n个字符的所有情况");
        System.out.println();
        System.out.println("3. 递归策略：");
        System.out.println("   - 对每种字符，决策是否将其包含在最终的k种字符中");
        System.out.println("   - 选择时该字符贡献所有可能的子序列");
        System.out.println("   - 不选择时该字符不影响结果");
        System.out.println();
        System.out.println("4. 复杂度分析：");
        System.out.println("   - 时间复杂度：O(26 * k)，26种字母，k层递归");
        System.out.println("   - 空间复杂度：O(k)，递归栈深度");
        System.out.println("   - 实际运行效率很高，适合在线查询");
        System.out.println();
        System.out.println("5. 应用场景：");
        System.out.println("   - 字符串组合计数问题");
        System.out.println("   - 约束条件下的方案数统计");
        System.out.println("   - 组合数学在算法中的应用");
    }
}
