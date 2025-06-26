package basic.c36;

/**
 * 布尔表达式求值问题
 * 
 * 问题描述：
 * 给定一个包含'0', '1', '&', '|', '^'的布尔表达式字符串和一个目标值（true或false），
 * 求有多少种加括号的方式能使表达式的计算结果等于目标值。
 * 
 * 约束条件：
 * - 不能改变字符串中字符的顺序
 * - 只能通过添加括号来改变运算优先级
 * 
 * 例如：表达式"1^0|0|1"，目标值为false
 * 可能的加括号方式：(1^0)|(0|1), 1^((0|0)|1), 1^(0|(0|1)) 等
 * 
 * 解决方案：
 * 1. 方法1：递归暴力搜索 - O(4^N)
 * 2. 方法2：动态规划优化1 - O(N³)
 * 3. 方法3：动态规划优化2 - O(N³)
 * 
 * 算法核心思想：
 * 对于区间[l,r]，枚举所有可能的分割点，计算左右两部分的true/false方案数，
 * 根据运算符合并结果。
 * 
 * 时间复杂度：递归O(4^N)，DP优化O(N³)
 * 空间复杂度：O(N²)
 */
public class Expression {
    
    /**
     * 验证表达式格式是否有效
     * 
     * 有效格式要求：
     * 1. 长度必须为奇数（数字和运算符交替出现）
     * 2. 偶数位置(0,2,4...)必须是'0'或'1'
     * 3. 奇数位置(1,3,5...)必须是'&','|','^'
     * 
     * @param exp 表达式字符数组
     * @return 表达式是否有效
     */
    private static boolean isValid(char[] exp) {
        // 长度必须为奇数
        if ((exp.length & 1) == 0) {
            return false;
        }
        
        // 检查偶数位置是否都是数字
        for (int i = 0; i < exp.length; i = i + 2) {
            if ((exp[i] != '1') && (exp[i] != '0')) {
                return false;
            }
        }
        
        // 检查奇数位置是否都是运算符
        for (int i = 1; i < exp.length; i = i + 2) {
            if ((exp[i] != '&') && (exp[i] != '|') && (exp[i] != '^')) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * 方法1的递归函数：暴力搜索所有可能的加括号方式
     * 
     * 算法思想：
     * 1. 如果区间只有一个数字，直接返回对应的方案数
     * 2. 否则枚举所有可能的运算符作为最后一个执行的运算
     * 3. 递归计算左右两部分的true/false方案数
     * 4. 根据运算符类型合并左右结果
     * 
     * @param str 表达式字符数组
     * @param desired 目标布尔值
     * @param l 左边界（包含）
     * @param r 右边界（包含）
     * @return 能得到目标值的方案数
     */
    private static int f1(char[] str, boolean desired, int l, int r) {
        // 递归基：区间只有一个数字
        if (l == r) {
            if (str[l] == '1') {
                return desired ? 1 : 0;  // '1'为true，求true方案数为1，false方案数为0
            } else {
                return desired ? 0 : 1;  // '0'为false，求false方案数为1，true方案数为0
            }
        }
        
        int res = 0;
        
        if (desired) {
            // 求能得到true的方案数
            for (int i = l + 1; i < r; i += 2) { // 枚举所有运算符位置
                switch (str[i]) {
                    case '&':
                        // A&B=true 当且仅当 A=true且B=true
                        res += f1(str, true, l, i - 1) * f1(str, true, i + 1, r);
                        break;
                    case '|':
                        // A|B=true 当且仅当 A=true或B=true（或两者都为true）
                        res += f1(str, true, l, i - 1) * f1(str, false, i + 1, r);   // A=true, B=false
                        res += f1(str, false, l, i - 1) * f1(str, true, i + 1, r);   // A=false, B=true
                        res += f1(str, true, l, i - 1) * f1(str, true, i + 1, r);    // A=true, B=true
                        break;
                    case '^':
                        // A^B=true 当且仅当 A和B不同
                        res += f1(str, true, l, i - 1) * f1(str, false, i + 1, r);   // A=true, B=false
                        res += f1(str, false, l, i - 1) * f1(str, true, i + 1, r);   // A=false, B=true
                        break;
                }
            }
        } else {
            // 求能得到false的方案数
            for (int i = l + 1; i < r; i += 2) { // 枚举所有运算符位置
                switch (str[i]) {
                    case '&':
                        // A&B=false 当且仅当 A=false或B=false（或两者都为false）
                        res += f1(str, false, l, i - 1) * f1(str, true, i + 1, r);   // A=false, B=true
                        res += f1(str, true, l, i - 1) * f1(str, false, i + 1, r);   // A=true, B=false
                        res += f1(str, false, l, i - 1) * f1(str, false, i + 1, r);  // A=false, B=false
                        break;
                    case '|':
                        // A|B=false 当且仅当 A=false且B=false
                        res += f1(str, false, l, i - 1) * f1(str, false, i + 1, r);
                        break;
                    case '^':
                        // A^B=false 当且仅当 A和B相同
                        res += f1(str, true, l, i - 1) * f1(str, true, i + 1, r);    // A=true, B=true
                        res += f1(str, false, l, i - 1) * f1(str, false, i + 1, r);  // A=false, B=false
                        break;
                }
            }
        }
        return res;
    }

    /**
     * 方法1：递归暴力搜索解法
     * 
     * @param express 布尔表达式字符串
     * @param desired 目标布尔值
     * @return 能得到目标值的加括号方案数
     */
    public static int ways1(String express, boolean desired) {
        if (express == null || express.equals("")) {
            return 0;
        }
        
        char[] exp = express.toCharArray();
        if (!isValid(exp)) {
            return 0;
        }
        
        return f1(exp, desired, 0, exp.length - 1);
    }

    /**
     * 方法2：动态规划优化解法1
     * 
     * 算法思想：
     * 使用两个二维数组tMap和fMap分别记录区间[i,j]能得到true和false的方案数
     * 通过区间DP的方式避免重复计算
     * 
     * @param s 表达式字符串
     * @param d 目标布尔值
     * @return 能得到目标值的方案数
     */
    public static int dp1(String s, boolean d) {
        char[] str = s.toCharArray();
        int n = str.length;
        
        // tMap[i][j]表示区间[i,j]能得到true的方案数
        int[][] tMap = new int[n][n];
        // fMap[i][j]表示区间[i,j]能得到false的方案数
        int[][] fMap = new int[n][n];
        
        // 初始化：单个数字的情况
        for (int i = 0; i < n; i += 2) {
            tMap[i][i] = str[i] == '1' ? 1 : 0;
            fMap[i][i] = str[i] == '0' ? 1 : 0;
        }
        
        // 区间DP：从小区间到大区间
        for (int row = n - 3; row >= 0; row = row - 2) {  // 起始位置（必须是偶数）
            for (int col = row + 2; col < n; col = col + 2) {  // 结束位置（必须是偶数）
                // 枚举分割点（必须是运算符位置）
                for (int i = row + 1; i < col; i += 2) {
                    switch (str[i]) {
                        case '&':
                            tMap[row][col] += tMap[row][i - 1] * tMap[i + 1][col];
                            break;
                        case '|':
                            tMap[row][col] += tMap[row][i - 1] * fMap[i + 1][col];
                            tMap[row][col] += fMap[row][i - 1] * tMap[i + 1][col];
                            tMap[row][col] += tMap[row][i - 1] * tMap[i + 1][col];
                            break;
                        case '^':
                            tMap[row][col] += tMap[row][i - 1] * fMap[i + 1][col];
                            tMap[row][col] += fMap[row][i - 1] * tMap[i + 1][col];
                            break;
                    }
                    switch (str[i]) {
                        case '&':
                            fMap[row][col] += fMap[row][i - 1] * tMap[i + 1][col];
                            fMap[row][col] += tMap[row][i - 1] * fMap[i + 1][col];
                            fMap[row][col] += fMap[row][i - 1] * fMap[i + 1][col];
                            break;
                        case '|':
                            fMap[row][col] += fMap[row][i - 1] * fMap[i + 1][col];
                            break;
                        case '^':
                            fMap[row][col] += tMap[row][i - 1] * tMap[i + 1][col];
                            fMap[row][col] += fMap[row][i - 1] * fMap[i + 1][col];
                            break;
                    }
                }
            }
        }
        
        return d ? tMap[0][n - 1] : fMap[0][n - 1];
    }

    /**
     * 方法3：动态规划优化解法2
     * 
     * 算法思想：
     * 使用不同的填表顺序，按长度递增的方式填充DP表
     * 
     * @param express 表达式字符串
     * @param desired 目标布尔值
     * @return 能得到目标值的方案数
     */
    public static int dp2(String express, boolean desired) {
        if (express == null || express.equals("")) {
            return 0;
        }
        
        char[] exp = express.toCharArray();
        if (!isValid(exp)) {
            return 0;
        }
        
        int[][] t = new int[exp.length][exp.length];  // true方案数
        int[][] f = new int[exp.length][exp.length];  // false方案数
        
        // 初始化单个字符的情况
        t[0][0] = exp[0] == '0' ? 0 : 1;
        f[0][0] = exp[0] == '1' ? 0 : 1;
        
        // 按区间长度递增的顺序填表
        for (int i = 2; i < exp.length; i += 2) {
            t[i][i] = exp[i] == '0' ? 0 : 1;
            f[i][i] = exp[i] == '1' ? 0 : 1;
            
            // 填充长度为i+1的所有区间
            for (int j = i - 2; j >= 0; j -= 2) {
                for (int k = j; k < i; k += 2) {
                    if (exp[k + 1] == '&') {
                        t[j][i] += t[j][k] * t[k + 2][i];
                        f[j][i] += (f[j][k] + t[j][k]) * f[k + 2][i] + f[j][k] * t[k + 2][i];
                    } else if (exp[k + 1] == '|') {
                        t[j][i] += (f[j][k] + t[j][k]) * t[k + 2][i] + t[j][k] * f[k + 2][i];
                        f[j][i] += f[j][k] * f[k + 2][i];
                    } else {  // '^'
                        t[j][i] += f[j][k] * t[k + 2][i] + t[j][k] * f[k + 2][i];
                        f[j][i] += f[j][k] * f[k + 2][i] + t[j][k] * t[k + 2][i];
                    }
                }
            }
        }
        
        return desired ? t[0][t.length - 1] : f[0][f.length - 1];
    }

    /**
     * 测试方法：验证三种算法的正确性
     */
    public static void main(String[] args) {
        // 测试用例
        String express = "1^0&0|1&1^0&0^1|0|1&1";
        boolean desired = true;
        
        System.out.println("=== 布尔表达式求值测试 ===");
        System.out.println("表达式: " + express);
        System.out.println("目标值: " + desired);
        System.out.println();
        
        long start1 = System.currentTimeMillis();
        int result1 = ways1(express, desired);
        long end1 = System.currentTimeMillis();
        System.out.println("方法1（递归暴力）结果: " + result1 + "，耗时: " + (end1 - start1) + "ms");
        
        long start2 = System.currentTimeMillis();
        int result2 = dp1(express, desired);
        long end2 = System.currentTimeMillis();
        System.out.println("方法2（DP优化1）结果: " + result2 + "，耗时: " + (end2 - start2) + "ms");
        
        long start3 = System.currentTimeMillis();
        int result3 = dp2(express, desired);
        long end3 = System.currentTimeMillis();
        System.out.println("方法3（DP优化2）结果: " + result3 + "，耗时: " + (end3 - start3) + "ms");
        
        // 验证结果一致性
        if (result1 == result2 && result2 == result3) {
            System.out.println("\n✓ 所有方法结果一致，算法正确！");
        } else {
            System.out.println("\n✗ 结果不一致，存在错误！");
        }
        
        // 测试其他用例
        System.out.println("\n=== 其他测试用例 ===");
        testCase("1^0|0|1", false);
        testCase("0&0&0&1^1|0", true);
    }
    
    /**
     * 辅助测试方法
     * @param expr 表达式
     * @param target 目标值
     */
    private static void testCase(String expr, boolean target) {
        System.out.println("表达式: " + expr + ", 目标: " + target);
        System.out.println("结果: " + dp2(expr, target));
        System.out.println();
    }
}
