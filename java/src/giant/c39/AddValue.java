package giant.c39;

/**
 * 01字符串删除子序列最大价值问题
 * 
 * 问题描述：
 * 给定一个只由0和1组成的字符串S，下标从1开始，i位置的字符价值V[i]计算方式如下：
 * 1. i == 1时，V[i] = 1
 * 2. i > 1时，如果S[i] != S[i-1]，V[i] = 1
 * 3. i > 1时，如果S[i] == S[i-1]，V[i] = V[i-1] + 1
 * 
 * 问题目标：
 * 可以随意删除S中的字符，返回整个S的最大价值
 * 
 * 解题思路：
 * 使用动态规划 + 回溯的思想：
 * 1. 对于每个位置，有两种选择：选择当前字符或跳过当前字符
 * 2. 如果选择当前字符，计算其价值：与前一个字符相同则价值递增，不同则价值为1
 * 3. 递归计算剩余部分的最大价值
 * 4. 在两种选择中取最大值
 * 
 * 算法核心：
 * - 状态：当前位置、上一个选择的字符、当前连续段的基础价值
 * - 转移：选择当前字符 vs 跳过当前字符
 * - 目标：最大化总价值
 * 
 * 时间复杂度：O(2^n)，每个位置都有选择或不选择两种决策
 * 空间复杂度：O(n)，递归栈空间
 * 
 * 来源：腾讯面试题
 * 
 * @author Zhu Runqi
 */
public class AddValue {

    /**
     * 递归计算从idx位置开始的最大价值
     * 
     * 状态定义：
     * - idx：当前处理的位置
     * - lastNum：上一个选择的字符（0或1）
     * - baseVal：如果选择当前字符，其价值为多少
     * 
     * 决策：
     * 1. 选择当前字符：价值为curVal，然后递归处理下一位置
     * 2. 跳过当前字符：价值为0，状态不变，递归处理下一位置
     * 
     * @param arr 转换后的01数组
     * @param idx 当前处理的位置
     * @param lastNum 上一个选择的字符
     * @param baseVal 当前连续段的基础价值
     * @return 从idx位置开始能获得的最大价值
     */
    private static int process1(int[] arr, int idx, int lastNum, int baseVal) {
        // 递归边界：处理完所有字符
        if (idx == arr.length) {
            return 0;
        }
        
        // 计算选择当前字符的价值
        // 如果与上一个字符相同，价值递增；否则价值重置为1
        int curVal = lastNum == arr[idx] ? (baseVal + 1) : 1;
        
        // 决策1：选择当前字符
        // 更新状态：lastNum变为当前字符，baseVal变为curVal
        int next1 = process1(arr, idx + 1, arr[idx], curVal);
        
        // 决策2：跳过当前字符
        // 状态不变：lastNum和baseVal保持不变
        int next2 = process1(arr, idx + 1, lastNum, baseVal);
        
        // 返回两种决策中的最大值
        return Math.max(curVal + next1, next2);
    }

    /**
     * 求01字符串删除子序列的最大价值
     * 
     * @param s 输入的01字符串
     * @return 最大价值
     */
    public static int max1(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        // 将字符串转换为01数组，便于处理
        char[] str = s.toCharArray();
        int[] arr = new int[str.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = str[i] == '0' ? 0 : 1;
        }
        
        // 从位置0开始，初始状态：lastNum=0, baseVal=0
        return process1(arr, 0, 0, 0);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 01字符串删除子序列最大价值问题 ===\n");
        
        // 测试用例1：简单情况
        System.out.println("测试用例1：简单交替序列");
        String s1 = "0101";
        int result1 = max1(s1);
        System.out.println("输入字符串: " + s1);
        System.out.println("最大价值: " + result1);
        System.out.println("分析: 可以选择子序列\"01\"或\"10\"，价值都是1+1=2");
        System.out.println("也可以选择单个字符，价值为1");
        System.out.println();
        
        // 测试用例2：连续相同字符
        System.out.println("测试用例2：连续相同字符");
        String s2 = "0000";
        int result2 = max1(s2);
        System.out.println("输入字符串: " + s2);
        System.out.println("最大价值: " + result2);
        System.out.println("分析: 选择所有字符\"0000\"，价值为1+2+3+4=10");
        System.out.println("价值计算: V[1]=1, V[2]=2, V[3]=3, V[4]=4");
        System.out.println();
        
        // 测试用例3：混合序列
        System.out.println("测试用例3：混合序列");
        String s3 = "0011";
        int result3 = max1(s3);
        System.out.println("输入字符串: " + s3);
        System.out.println("最大价值: " + result3);
        System.out.println("分析: 最优选择可能是\"00\"(价值1+2=3)或\"11\"(价值1+2=3)");
        System.out.println("也可以选择\"0011\"，价值为1+2+1+2=6");
        System.out.println();
        
        // 测试用例4：较长序列
        System.out.println("测试用例4：较长序列");
        String s4 = "0101010";
        int result4 = max1(s4);
        System.out.println("输入字符串: " + s4);
        System.out.println("最大价值: " + result4);
        System.out.println("分析: 可以选择全部字符，每个字符价值都是1，总价值为7");
        System.out.println("或者选择连续的0或1来获得更高价值");
        System.out.println();
        
        // 测试用例5：全0序列
        System.out.println("测试用例5：全0序列");
        String s5 = "00000";
        int result5 = max1(s5);
        System.out.println("输入字符串: " + s5);
        System.out.println("最大价值: " + result5);
        System.out.println("分析: 选择所有字符，价值为1+2+3+4+5=15");
        System.out.println();
        
        // 测试用例6：空字符串
        System.out.println("测试用例6：边界情况");
        String s6 = "";
        int result6 = max1(s6);
        System.out.println("输入字符串: \"" + s6 + "\"");
        System.out.println("最大价值: " + result6 + " (空字符串)");
        System.out.println();
        
        System.out.println("=== 算法原理解析 ===");
        System.out.println("1. 价值计算规则：");
        System.out.println("   - 第一个字符价值固定为1");
        System.out.println("   - 后续字符：相同则价值递增，不同则重置为1");
        System.out.println("   - 目标是找到价值和最大的子序列");
        System.out.println();
        System.out.println("2. 动态规划状态：");
        System.out.println("   - 当前位置、上一个选择的字符、连续段长度");
        System.out.println("   - 每个位置有选择或跳过两种决策");
        System.out.println("   - 选择时需要更新连续段信息");
        System.out.println();
        System.out.println("3. 算法策略：");
        System.out.println("   - 回溯搜索所有可能的子序列");
        System.out.println("   - 动态计算每种选择的价值");
        System.out.println("   - 贪心地选择价值最大的方案");
        System.out.println();
        System.out.println("4. 复杂度分析：");
        System.out.println("   - 时间复杂度：O(2^n)，指数级搜索空间");
        System.out.println("   - 空间复杂度：O(n)，递归栈深度");
        System.out.println("   - 适合小规模输入，可进一步优化");
        System.out.println();
        System.out.println("5. 优化方向：");
        System.out.println("   - 记忆化搜索：缓存重复子问题");
        System.out.println("   - 动态规划：自底向上计算");
        System.out.println("   - 贪心策略：特定情况下的快速解法");
    }
}
