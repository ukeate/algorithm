package basic.c41;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 表达式添加运算符问题
 * 
 * 问题描述：
 * 给定一个仅包含数字0-9的字符串和一个目标值，
 * 在数字之间添加二元运算符（+、-、*）使得表达式的结果等于目标值
 * 
 * 关键难点：
 * 1. 运算符优先级：乘法优先于加减法
 * 2. 数字组合：可以将连续数字组合成多位数
 * 3. 前导零：需要正确处理'0'和以'0'开头的数字
 * 
 * 算法思路：
 * 方法1：枚举所有可能的表达式字符串，然后计算结果
 * 方法2：递归计算，维护left（已确定部分）和cur（当前项）
 * 方法3：DFS深度优先搜索，构建完整的表达式路径
 * 
 * @author 算法学习
 */
public class ExpressionAddOperators {

    /**
     * 检查表达式是否等于目标值
     * 注意：此方法未实现，需要完善表达式计算逻辑
     * 
     * @param path 表达式字符串
     * @param target 目标值
     * @return 是否等于目标值
     */
    private static boolean check(String path, int target) {
        // TODO: 实现表达式计算逻辑
        // 需要考虑运算符优先级：乘法优先于加减法
        return true;
    }

    /**
     * 方法1：暴力枚举所有可能的表达式
     * 在每个数字后面尝试添加+、-、*或不添加运算符
     * 
     * @param str 数字字符数组
     * @param idx 当前处理位置
     * @param target 目标值
     * @param path 当前构建的表达式路径
     * @param ans 结果列表
     * 
     * 注意：此实现有缺陷，check方法未完善
     */
    private static void process1(char[] str, int idx, int target, String path, List<String> ans) {
        if (idx == str.length) {
            // 去除末尾可能的运算符
            char last = path.charAt(path.length() - 1);
            path = (last == '+' || last == '-' || last == '*') ? path.substring(0, path.length() - 1) : path;
            if (check(path, target)) {
                ans.add(path);
            }
            return;
        }
        
        // 尝试四种选择：单个数字、数字+加号、数字+减号、数字+乘号
        String p0 = String.valueOf(str[idx]);
        String p1 = p0 + "+";
        String p2 = p0 + "-";
        String p3 = p0 + "*";
        
        process1(str, idx + 1, target, path + p0, ans);  // 不加运算符
        process1(str, idx + 1, target, path + p1, ans);  // 加+
        process1(str, idx + 1, target, path + p2, ans);  // 加-
        process1(str, idx + 1, target, path + p3, ans);  // 加*
    }

    /**
     * 方法1的入口函数
     * 
     * @param num 数字字符串
     * @param target 目标值
     * @return 所有可能的表达式列表
     */
    public static List<String> ways1(String num, int target) {
        List<String> ans = new ArrayList<>();
        char[] str = num.toCharArray();
        process1(str, 0, target, "", ans);
        return ans;
    }

    /**
     * 方法2：递归计算表达式值
     * 使用left和cur分别维护已确定部分和当前项
     * 
     * @param str 数字字符数组
     * @param idx 当前处理位置
     * @param left 已确定的累计值（加减法的结果）
     * @param cur 当前项的值（可能被乘法修改）
     * @param target 目标值
     * @return 符合条件的表达式数量
     * 
     * 核心思想：
     * - left保存已经确定的加减法结果
     * - cur保存当前正在计算的项（可能被连续乘法影响）
     * - 最终结果 = left + cur
     */
    private static int process2(char[] str, int idx, int left, int cur, int target) {
        if (idx == str.length) {
            return (left + cur) == target ? 1 : 0;
        }
        
        int ways = 0;
        int num = str[idx] - '0';
        
        // 选择1：加上当前数字
        // 将cur加入left，新的cur为num
        ways += process2(str, idx + 1, left + cur, num, target);
        
        // 选择2：减去当前数字
        // 将cur加入left，新的cur为-num
        ways += process2(str, idx + 1, left + cur, -num, target);
        
        // 选择3：乘以当前数字
        // left不变，cur乘以num（乘法优先级高）
        ways += process2(str, idx + 1, left, cur * num, target);
        
        // 选择4：不加运算符，将数字合并到cur中
        if (cur != 0) {  // 避免前导零问题
            if (cur > 0) {
                ways += process2(str, idx + 1, left, cur * 10 + num, target);
            } else {
                ways += process2(str, idx + 1, left, cur * 10 - num, target);
            }
        }
        return ways;
    }

    /**
     * 方法2的入口函数
     * 
     * @param num 数字字符串
     * @param target 目标值
     * @return 符合条件的表达式数量
     */
    public static int ways2(String num, int target) {
        char[] str = num.toCharArray();
        int first = str[0] - '0';
        return process2(str, 1, 0, first, target);
    }

    /**
     * 方法3：DFS深度优先搜索构建表达式
     * 
     * @param res 结果列表
     * @param path 路径字符数组
     * @param pathIdx 路径当前长度
     * @param left 已确定的累计值
     * @param cur 当前项的值
     * @param digits 原始数字数组
     * @param idx 当前处理位置
     * @param target 目标值
     * 
     * 算法核心：
     * 1. 枚举从当前位置开始的所有可能数字组合
     * 2. 对每个数字组合尝试+、-、*三种运算符
     * 3. 正确处理前导零的情况
     */
    private static void dfs(List<String> res, char[] path, int pathIdx, long left, long cur, 
                           char[] digits, int idx, int target) {
        if (idx == digits.length) {
            if (left + cur == target) {
                res.add(new String(path, 0, pathIdx));
            }
            return;
        }
        
        long num = 0;
        int j = pathIdx + 1; // 为运算符预留位置
        
        // 枚举从当前位置开始的所有可能数字
        for (int i = idx; i < digits.length; i++) {
            num = num * 10 + digits[i] - '0';
            path[j++] = digits[i];
            
            // 尝试三种运算符
            path[pathIdx] = '+';
            dfs(res, path, j, left + cur, num, digits, i + 1, target);
            
            path[pathIdx] = '-';
            dfs(res, path, j, left + cur, -num, digits, i + 1, target);
            
            path[pathIdx] = '*';
            dfs(res, path, j, left, cur * num, digits, i + 1, target);
            
            // 处理前导零：如果当前数字是'0'，不能继续组合
            if (digits[idx] == '0') {
                break;
            }
        }
    }

    /**
     * 方法3的入口函数：DFS构建所有可能的表达式
     * 
     * @param num 数字字符串
     * @param target 目标值
     * @return 所有符合条件的表达式列表
     * 
     * 时间复杂度：O(4^N) - 每个位置有4种选择
     * 空间复杂度：O(N) - 递归栈和路径数组
     */
    public static List<String> ways3(String num, int target) {
        List<String> ret = new LinkedList<>();
        if (num.length() == 0) {
            return ret;
        }
        
        // 路径数组：最多需要2N-1个字符（N个数字+N-1个运算符）
        char[] path = new char[num.length() * 2 - 1];
        char[] digits = num.toCharArray();
        
        // 枚举第一个数字的所有可能组合
        long cur = 0;
        for (int i = 0; i < digits.length; i++) {
            cur = cur * 10 + digits[i] - '0';
            path[i] = digits[i];
            
            // 从第一个数字后开始DFS
            dfs(ret, path, i + 1, 0, cur, digits, i + 1, target);
            
            // 处理前导零：如果第一个数字是0，不能继续组合
            if (cur == 0) {
                break;
            }
        }
        return ret;
    }
}
