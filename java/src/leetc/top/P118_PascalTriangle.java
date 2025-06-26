package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 118. 杨辉三角 (Pascal's Triangle)
 * 
 * 问题描述：
 * 给定一个非负整数 numRows，生成杨辉三角的前 numRows 行。
 * 在杨辉三角中，每个数是它左上方和右上方的数的和。
 * 
 * 示例：
 * 输入: 5
 * 输出:
 * [
 *      [1],
 *     [1,1],
 *    [1,2,1],
 *   [1,3,3,1],
 *  [1,4,6,4,1]
 * ]
 * 
 * 解法思路：
 * 动态规划 + 数学递推：
 * 1. 杨辉三角的性质：
 *    - 每行的第一个和最后一个数都是1
 *    - 其他位置的数 = 上一行对应位置 + 上一行前一个位置
 *    - 即：triangle[i][j] = triangle[i-1][j-1] + triangle[i-1][j]
 * 2. 构造方法：
 *    - 先为每行添加第一个元素1
 *    - 然后计算中间元素（基于上一行）
 *    - 最后为每行添加最后一个元素1
 * 
 * 数学背景：
 * - 杨辉三角实际上是二项式系数的排列
 * - 第n行第k个数等于组合数C(n,k)
 * - 具有对称性：C(n,k) = C(n,n-k)
 * 
 * 算法步骤：
 * 1. 初始化结果列表
 * 2. 为每行创建列表并添加第一个元素1
 * 3. 从第二行开始，计算中间元素
 * 4. 为每行添加最后一个元素1
 * 
 * 时间复杂度：O(numRows²) - 需要生成所有元素
 * 空间复杂度：O(numRows²) - 存储整个三角形
 * 
 * LeetCode链接：https://leetcode.com/problems/pascals-triangle/
 */
public class P118_PascalTriangle {
    
    /**
     * 生成杨辉三角
     * 
     * @param numRows 要生成的行数
     * @return 杨辉三角的前numRows行
     */
    public static List<List<Integer>> generate(int numRows) {
        List<List<Integer>> ans = new ArrayList<>();
        
        // 第一步：为每行创建列表，并添加第一个元素1
        for (int i = 0; i < numRows; i++) {
            ans.add(new ArrayList<>());
            ans.get(i).add(1); // 每行第一个元素都是1
        }
        
        // 第二步：计算中间元素（从第二行开始）
        for (int i = 1; i < numRows; i++) {
            for (int j = 1; j < i; j++) {
                // 当前位置 = 上一行对应位置 + 上一行前一个位置
                // triangle[i][j] = triangle[i-1][j-1] + triangle[i-1][j]
                int value = ans.get(i - 1).get(j - 1) + ans.get(i - 1).get(j);
                ans.get(i).add(value);
            }
            
            // 第三步：为每行添加最后一个元素1
            ans.get(i).add(1);
        }
        
        return ans;
    }
}
