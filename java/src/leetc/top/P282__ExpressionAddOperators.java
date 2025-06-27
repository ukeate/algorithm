package leetc.top;

import java.util.LinkedList;
import java.util.List;

/**
 * LeetCode 282. 给表达式添加运算符 (Expression Add Operators)
 * 
 * 问题描述：
 * 给定一个仅包含数字 0-9 的字符串 num 和一个目标值整数 target ，
 * 在 num 的数字之间添加二元运算符（不是一元）+、- 或 * ，返回所有能够得到目标值的表达式。
 * 
 * 注意：操作数不会以零开头。
 * 
 * 示例：
 * 输入: num = "123", target = 6
 * 输出: ["1+2+3", "1*2*3"]
 * 
 * 输入: num = "232", target = 8
 * 输出: ["2*3+2", "2+3*2"]
 * 
 * 解法思路：
 * DFS深度优先搜索 + 回溯：
 * 1. 对于每个位置，尝试不同长度的数字组合
 * 2. 对于每个数字，尝试添加+、-、*三种运算符
 * 3. 维护当前的计算状态：已确定部分(left) + 当前项(cur)
 * 4. 处理运算符优先级：乘法需要与前一项结合
 * 5. 避免前导零的情况
 * 
 * 核心技巧：
 * - 用left和cur分别维护已确定的部分和当前处理的项
 * - 乘法操作时，cur与新数字相乘，left保持不变
 * - 加减操作时，将cur加入left，新数字作为新的cur
 * 
 * LeetCode链接：https://leetcode.com/problems/expression-add-operators/
 */
public class P282__ExpressionAddOperators {
    
    /**
     * DFS递归搜索所有可能的表达式
     * 
     * @param res 结果列表
     * @param path 当前构建的表达式路径
     * @param len 当前路径长度
     * @param left 已确定的部分计算结果
     * @param cur 当前项的值
     * @param num 原始数字字符数组
     * @param idx 当前处理的位置
     * @param aim 目标值
     */
    private static void dfs(List<String> res, char[] path, int len, long left, long cur, char[] num, int idx, int aim) {
        // 递归终止条件：处理完所有数字
        if (idx == num.length) {
            if (left + cur == aim) {  // 检查是否达到目标值
                res.add(new String(path, 0, len));
            }
            return;
        }
        
        long n = 0;      // 当前构建的数字
        int j = len + 1; // 下一个数字在path中的起始位置（跳过运算符位置）
        
        // 尝试不同长度的数字组合
        for (int i = idx; i < num.length; i++) {
            n = n * 10 + num[i] - '0';  // 构建当前数字
            path[j++] = num[i];         // 将数字字符加入路径
            
            // 尝试添加加号
            path[len] = '+';
            dfs(res, path, j, left + cur, n, num, i + 1, aim);
            
            // 尝试添加减号
            path[len] = '-';
            dfs(res, path, j, left + cur, -n, num, i + 1, aim);
            
            // 尝试添加乘号（注意运算符优先级）
            path[len] = '*';
            dfs(res, path, j, left, cur * n, num, i + 1, aim);
            
            // 避免前导零：如果当前数字以0开头，只能是单独的0
            if (num[idx] == '0') {
                break;
            }
        }
    }

    /**
     * 给表达式添加运算符，返回所有可能达到目标值的表达式
     * 
     * 算法流程：
     * 1. 初始化结果列表和路径数组
     * 2. 尝试不同长度的第一个数字
     * 3. 对每个第一个数字，调用DFS搜索后续可能性
     * 4. 处理前导零的特殊情况
     * 
     * @param num 包含数字的字符串
     * @param target 目标值
     * @return 所有可能的表达式列表
     */
    public static List<String> addOperators(String num, int target) {
        List<String> ret = new LinkedList<>();
        if (num.length() == 0) {
            return ret;
        }
        
        // path数组大小：最多在每两个数字间插入运算符
        char[] path = new char[num.length() * 2 - 1];
        char[] digits = num.toCharArray();
        long n = 0;
        
        // 尝试不同长度的第一个数字
        for (int i = 0; i < digits.length; i++) {
            n = n * 10 + digits[i] - '0';  // 构建第一个数字
            path[i] = digits[i];           // 将数字字符加入路径
            
            // 从第一个数字开始DFS搜索
            // left=0, cur=n（第一个数字作为当前项）
            dfs(ret, path, i + 1, 0, n, digits, i + 1, target);
            
            // 避免前导零：如果第一个数字是0，只能是单独的0
            if (n == 0) {
                break;
            }
        }
        return ret;
    }
}
