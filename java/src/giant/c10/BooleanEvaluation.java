package giant.c10;

/**
 * 布尔表达式求值问题
 * 
 * 问题描述：
 * 给定一个布尔表达式，由0、1以及运算符&、|、^组成，统计有多少种添加括号的方式
 * 能使整个表达式的结果等于desired（0或1）。
 * 
 * 例如：
 * 表达式 "1^0|0|1"，desired = 0
 * 可能的括号方案：
 * - (1^0)|(0|1) = 1|1 = 1 ≠ 0
 * - (1^(0|0))|1 = (1^0)|1 = 1|1 = 1 ≠ 0
 * - 1^((0|0)|1) = 1^(0|1) = 1^1 = 0 ✓
 * - 1^(0|(0|1)) = 1^(0|1) = 1^1 = 0 ✓
 * - ((1^0)|0)|1 = (1|0)|1 = 1|1 = 1 ≠ 0
 * 
 * LeetCode链接：https://leetcode-cn.com/problems/boolean-evaluation-lcci/
 * 
 * 解决方案：
 * 1. 方法1：记忆化递归法 - 区间DP，时间复杂度O(N^3)
 * 2. 方法2：迭代动态规划法 - 优化空间和常数，时间复杂度O(N^3)
 * 
 * 核心思想：
 * 对于区间[l,r]，枚举所有可能的分割点k，将表达式分为左右两部分
 * 根据运算符类型，计算左右部分的结果组合能产生多少种目标结果
 * 
 * 时间复杂度：O(N^3)
 * 空间复杂度：O(N^2)
 */
public class BooleanEvaluation {
    
    /**
     * 信息类，存储区间内表达式可能的结果统计
     * t: 结果为true(1)的方案数
     * f: 结果为false(0)的方案数
     */
    private static class Info {
        public int t;  // 结果为true的方案数
        public int f;  // 结果为false的方案数

        public Info(int tr, int fa) {
            t = tr;
            f = fa;
        }
    }

    /**
     * 递归处理函数（方法1）
     * 
     * 算法思路：
     * 使用区间动态规划的思想，对于每个区间[l,r]：
     * 1. 如果l==r，说明是单个数字，直接返回对应的方案数
     * 2. 否则枚举所有可能的分割点，将区间分为左右两部分
     * 3. 根据运算符类型计算组合结果
     * 
     * 运算符计算规则：
     * - & (AND): true & true = true, 其他组合都是false
     * - | (OR): false | false = false, 其他组合都是true  
     * - ^ (XOR): 相同为false，不同为true
     * 
     * @param str 表达式字符数组
     * @param l 区间左端点
     * @param r 区间右端点
     * @param dp 记忆化数组
     * @return 该区间的结果统计信息
     */
    private static Info process1(char[] str, int l, int r, Info[][] dp) {
        // 如果已经计算过，直接返回缓存结果
        if (dp[l][r] != null) {
            return dp[l][r];
        }
        
        int t = 0, f = 0;  // true和false的方案数
        
        if (l == r) {
            // 基础情况：单个字符
            t = str[l] == '1' ? 1 : 0;
            f = str[l] == '0' ? 1 : 0;
        } else {
            // 枚举所有可能的分割点（只能在运算符位置分割）
            for (int split = l + 1; split < r; split += 2) {
                Info li = process1(str, l, split - 1, dp);      // 左部分结果
                Info ri = process1(str, split + 1, r, dp);      // 右部分结果
                
                int a = li.t, b = li.f, c = ri.t, d = ri.f;    // 左右部分的true/false方案数
                
                // 根据运算符类型计算组合结果
                switch (str[split]) {
                    case '&':
                        // AND运算：只有true & true = true
                        t += a * c;                    // true & true
                        f += b * c + b * d + a * d;    // false & true, false & false, true & false
                        break;
                    case '|':
                        // OR运算：只有false | false = false
                        t += a * c + a * d + b * c;    // true | true, true | false, false | true
                        f += b * d;                    // false | false
                        break;
                    case '^':
                        // XOR运算：相同为false，不同为true
                        t += a * d + b * c;            // true ^ false, false ^ true
                        f += a * c + b * d;            // true ^ true, false ^ false
                        break;
                }
            }
        }
        
        // 缓存结果并返回
        dp[l][r] = new Info(t, f);
        return dp[l][r];
    }

    /**
     * 方法1：记忆化递归法
     * 
     * 算法特点：
     * - 使用自顶向下的递归方式
     * - 通过Info类封装返回信息
     * - 使用记忆化避免重复计算
     * 
     * 时间复杂度：O(N^3)
     * 空间复杂度：O(N^2)
     * 
     * @param express 布尔表达式字符串
     * @param desired 期望的结果（0或1）
     * @return 能得到期望结果的括号添加方案数
     */
    public static int countEval1(String express, int desired) {
        if (express == null || express.equals("")) {
            return 0;
        }
        
        char[] exp = express.toCharArray();
        int n = exp.length;
        Info[][] dp = new Info[n][n];  // 记忆化数组
        
        Info all = process1(exp, 0, exp.length - 1, dp);
        return desired == 1 ? all.t : all.f;
    }

    /**
     * 方法2：迭代动态规划法
     * 
     * 算法思路：
     * 使用自底向上的迭代方式，避免递归调用的开销
     * dp[desired][l][r] 表示区间[l,r]内结果为desired的方案数
     * 
     * 状态转移：
     * 对于每个区间[j,i]，枚举分割点k：
     * - 计算左部分[j,k]和右部分[k+2,i]的结果
     * - 根据运算符k+1的类型组合左右结果
     * 
     * 优化点：
     * - 使用三维数组直接存储desired维度
     * - 按区间长度从小到大的顺序填表
     * - 避免Info对象的创建开销
     * 
     * 时间复杂度：O(N^3)
     * 空间复杂度：O(N^2)
     * 
     * @param express 布尔表达式字符串
     * @param desired 期望的结果（0或1）
     * @return 能得到期望结果的括号添加方案数
     */
    public static int countEval2(String express, int desired) {
        if (express == null || express.equals("")) {
            return 0;
        }
        
        char[] exp = express.toCharArray();
        int n = exp.length;
        
        // dp[desired][l][r]: 区间[l,r]结果为desired的方案数
        // desired=0表示false，desired=1表示true
        int[][][] dp = new int[2][n][n];
        
        // 初始化单个字符的情况
        dp[0][0][0] = exp[0] == '0' ? 1 : 0;  // 如果字符是'0'，false方案数为1
        dp[1][0][0] = dp[0][0][0] ^ 1;        // true方案数与false方案数互补
        
        // 按区间长度从小到大枚举（只考虑奇数位置，因为偶数位置是运算符）
        for (int i = 2; i < exp.length; i += 2) {
            // 初始化长度为1的区间
            dp[0][i][i] = exp[i] == '1' ? 0 : 1;
            dp[1][i][i] = exp[i] == '0' ? 0 : 1;
            
            // 枚举区间起点
            for (int j = i - 2; j >= 0; j -= 2) {
                // 枚举分割点
                for (int k = j; k < i; k += 2) {
                    // 根据运算符类型进行状态转移
                    if (exp[k + 1] == '&') {
                        // AND运算
                        dp[1][j][i] += dp[1][j][k] * dp[1][k + 2][i];  // true & true = true
                        dp[0][j][i] += (dp[0][j][k] + dp[1][j][k]) * dp[0][k + 2][i] + 
                                      dp[0][j][k] * dp[1][k + 2][i];   // 其他组合为false
                    } else if (exp[k + 1] == '|') {
                        // OR运算
                        dp[1][j][i] += (dp[0][j][k] + dp[1][j][k]) * dp[1][k + 2][i] + 
                                      dp[1][j][k] * dp[0][k + 2][i];   // 除false|false外都为true
                        dp[0][j][i] += dp[0][j][k] * dp[0][k + 2][i];  // false | false = false
                    } else {
                        // XOR运算
                        dp[1][j][i] += dp[0][j][k] * dp[1][k + 2][i] + 
                                      dp[1][j][k] * dp[0][k + 2][i];   // 不同为true
                        dp[0][j][i] += dp[0][j][k] * dp[0][k + 2][i] + 
                                      dp[1][j][k] * dp[1][k + 2][i];   // 相同为false
                    }
                }
            }
        }
        
        return dp[desired][0][n - 1];
    }
    
    /**
     * 测试方法：验证两种算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 布尔表达式求值问题测试 ===");
        
        // 测试用例1：LeetCode示例
        System.out.println("1. LeetCode示例测试:");
        String expr1 = "1^0|0|1";
        int desired1 = 0;
        System.out.println("表达式: " + expr1 + ", 期望结果: " + desired1);
        System.out.println("方法1结果: " + countEval1(expr1, desired1));
        System.out.println("方法2结果: " + countEval2(expr1, desired1));
        System.out.println();
        
        // 测试用例2：简单情况
        System.out.println("2. 简单测试用例:");
        String expr2 = "0&0&0&1^1|0";
        int desired2 = 1;
        System.out.println("表达式: " + expr2 + ", 期望结果: " + desired2);
        System.out.println("方法1结果: " + countEval1(expr2, desired2));
        System.out.println("方法2结果: " + countEval2(expr2, desired2));
        System.out.println();
        
        // 测试用例3：单个字符
        System.out.println("3. 边界测试用例:");
        String expr3 = "1";
        System.out.println("表达式: " + expr3);
        System.out.println("期望结果0 - 方法1: " + countEval1(expr3, 0) + ", 方法2: " + countEval2(expr3, 0));
        System.out.println("期望结果1 - 方法1: " + countEval1(expr3, 1) + ", 方法2: " + countEval2(expr3, 1));
        System.out.println();
        
        // 性能比较测试
        System.out.println("4. 性能比较测试:");
        String perfExpr = "1^0|0|1&1^0|0|1";  // 更复杂的表达式
        System.out.println("复杂表达式: " + perfExpr);
        
        long start = System.currentTimeMillis();
        int result1 = countEval1(perfExpr, 1);
        long time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result2 = countEval2(perfExpr, 1);
        long time2 = System.currentTimeMillis() - start;
        
        System.out.println("方法1（记忆化递归）: " + result1 + ", 耗时: " + time1 + "ms");
        System.out.println("方法2（迭代DP）: " + result2 + ", 耗时: " + time2 + "ms");
        
        if (result1 == result2) {
            System.out.println("✓ 两种方法结果一致，算法正确！");
        } else {
            System.out.println("✗ 算法结果不一致，存在错误！");
        }
        
        System.out.println("\n=== 测试完成 ===");
    }
}