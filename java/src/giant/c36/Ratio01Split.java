package giant.c36;

import java.util.HashMap;

/**
 * 01字符串按比例切分问题
 * 
 * 问题描述：
 * 把一个01字符串切成多个部分，要求每一部分的0和1比例一样，同时要求尽可能多的划分
 * 
 * 示例1：01010101
 * - 切法1：01 01 01 01，0和1比例为1:1，划分数为4
 * - 切法2：0101 0101，0和1比例为1:1，划分数为2
 * - 最优切法是切法1，部分数为4
 * 
 * 示例2：00001111
 * - 只有一种切法：00001111整体作为一块，0和1比例为1:1，部分数为1
 * 
 * 问题要求：
 * 给定一个01字符串str，长度为N，返回一个长度为N的数组ans
 * ans[i] = str[0...i]这个前缀串，按上述规则尽可能多划分的部分数
 * 
 * 解题思路：
 * 1. 对于每个前缀，统计0和1的个数
 * 2. 计算0和1个数的最大公约数，得到最简比例
 * 3. 使用HashMap记录每种比例出现的次数
 * 4. 当前前缀的划分数就是该比例出现的次数
 * 
 * 关键观察：
 * - 如果前缀中0和1的比例为a:b（最简形式），那么可以切分的最大部分数
 *   就是这种比例在前缀中重复出现的次数
 * - 使用GCD将比例化简，避免重复计算
 * 
 * 来源：京东面试题
 * 
 * @author Zhu Runqi
 */
public class Ratio01Split {
    
    /**
     * 计算两个数的最大公约数（欧几里得算法）
     * 
     * @param m 第一个数
     * @param n 第二个数
     * @return m和n的最大公约数
     */
    private static int gcd(int m, int n) {
        return n == 0 ? m : gcd(n, m % n);
    }

    /**
     * 计算01数组的最优切分方案
     * 
     * 算法流程：
     * 1. 遍历数组，维护0和1的累计计数
     * 2. 对于每个位置，计算当前0和1的比例（最简形式）
     * 3. 使用双层HashMap记录每种比例的出现次数
     * 4. 当前位置的答案就是该比例的出现次数
     * 
     * @param arr 01数组（0表示字符'0'，1表示字符'1'）
     * @return 每个前缀的最大划分数组
     */
    public static int[] split(int[] arr) {
        // 外层key：0的个数在最简比例中的值，内层key：1的个数在最简比例中的值，value：出现次数
        HashMap<Integer, HashMap<Integer, Integer>> pre = new HashMap<>();
        int n = arr.length;
        int[] ans = new int[n];
        int zero = 0, one = 0;  // 累计的0和1的个数
        
        for (int i = 0; i < n; i++) {
            // 更新0和1的计数
            if (arr[i] == 0) {
                zero++;
            } else {
                one++;
            }
            
            // 特殊情况：只有0或只有1
            if (zero == 0 || one == 0) {
                // 只能整体作为一个部分，划分数等于前缀长度
                ans[i] = i + 1;
            } else {
                // 计算0和1个数的最大公约数，将比例化简
                int gcd = gcd(zero, one);
                int a = zero / gcd;  // 最简比例中0的个数
                int b = one / gcd;   // 最简比例中1的个数
                
                // 初始化HashMap结构
                if (!pre.containsKey(a)) {
                    pre.put(a, new HashMap<>());
                }
                
                // 更新该比例的出现次数
                if (!pre.get(a).containsKey(b)) {
                    pre.get(a).put(b, 1);
                } else {
                    pre.get(a).put(b, pre.get(a).get(b) + 1);
                }
                
                // 当前位置的答案就是该比例的出现次数
                ans[i] = pre.get(a).get(b);
            }
        }
        return ans;
    }

    /**
     * 辅助方法：将01字符串转换为01数组
     */
    private static int[] stringToArray(String s) {
        int[] arr = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            arr[i] = s.charAt(i) - '0';
        }
        return arr;
    }

    /**
     * 辅助方法：分析切分过程
     */
    private static void analyzeSplit(String s) {
        System.out.println("分析字符串: " + s);
        int[] arr = stringToArray(s);
        int[] result = split(arr);
        
        System.out.println("位置\t前缀\t\t0个数\t1个数\t比例\t\t最简比例\t划分数");
        System.out.println("--------------------------------------------------------------------");
        
        int zero = 0, one = 0;
        for (int i = 0; i < s.length(); i++) {
            if (arr[i] == 0) zero++;
            else one++;
            
            String prefix = s.substring(0, i + 1);
            String ratio = zero + ":" + one;
            String simpleRatio;
            
            if (zero == 0 || one == 0) {
                simpleRatio = "纯" + (zero == 0 ? "1" : "0");
            } else {
                int g = gcd(zero, one);
                simpleRatio = (zero/g) + ":" + (one/g);
            }
            
            System.out.printf("%d\t%s\t\t%d\t%d\t%s\t\t%s\t\t%d\n", 
                            i, prefix, zero, one, ratio, simpleRatio, result[i]);
        }
        System.out.println("结果数组: " + java.util.Arrays.toString(result));
        System.out.println();
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 01字符串按比例切分问题 ===\n");
        
        // 测试用例1：题目示例
        System.out.println("测试用例1：交替出现的01串");
        analyzeSplit("01010101");
        
        // 测试用例2：聚集的01串
        System.out.println("测试用例2：聚集的01串");
        analyzeSplit("00001111");
        
        // 测试用例3：复杂比例
        System.out.println("测试用例3：复杂比例");
        analyzeSplit("001011");
        
        // 测试用例4：纯0串
        System.out.println("测试用例4：纯0串");
        analyzeSplit("0000");
        
        // 测试用例5：纯1串
        System.out.println("测试用例5：纯1串");
        analyzeSplit("1111");
        
        // 测试用例6：题目原始示例
        System.out.println("测试用例6：题目原始示例");
        String input = "010100001";
        int[] arr = stringToArray(input);
        int[] result = split(arr);
        System.out.println("输入: " + input);
        System.out.println("输出: " + java.util.Arrays.toString(result));
        System.out.println("期望: [1, 1, 1, 2, 1, 2, 1, 1, 3]");
        
        // 验证结果
        int[] expected = {1, 1, 1, 2, 1, 2, 1, 1, 3};
        boolean correct = java.util.Arrays.equals(result, expected);
        System.out.println("结果正确: " + correct);
        
        System.out.println("\n=== 算法原理解析 ===");
        System.out.println("1. 核心思想：");
        System.out.println("   - 相同最简比例的前缀可以切分成同样多的部分");
        System.out.println("   - 使用HashMap记录每种比例的出现次数");
        System.out.println("   - GCD化简避免了等价比例的重复计算");
        System.out.println();
        System.out.println("2. 时间复杂度：O(n * log(max(zero, one)))");
        System.out.println("   - n次遍历，每次计算GCD的时间复杂度为O(log(max(zero, one)))");
        System.out.println();
        System.out.println("3. 空间复杂度：O(n)");
        System.out.println("   - HashMap存储不同比例的出现次数");
        System.out.println("   - 最多有O(n)种不同的最简比例");
        System.out.println();
        System.out.println("4. 关键优化：");
        System.out.println("   - 使用GCD将比例化为最简形式");
        System.out.println("   - 双层HashMap高效存储和查询比例信息");
        System.out.println("   - 特殊处理纯0或纯1的情况");
    }
}

