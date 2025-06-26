package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 119. 杨辉三角 II (Pascal's Triangle II)
 * 
 * 问题描述：
 * 给定一个非负索引 rowIndex，返回杨辉三角的第 rowIndex 行。
 * 在杨辉三角中，每个数是它左上方和右上方的数的和。
 * 
 * 进阶要求：你可以优化你的算法到 O(rowIndex) 空间复杂度吗？
 * 
 * 示例：
 * 输入: rowIndex = 3
 * 输出: [1,3,3,1]
 * 
 * 解法思路：
 * 空间优化的动态规划：
 * 1. 只使用一个数组来存储当前行，而不是存储整个三角形
 * 2. 关键观察：计算第i行时，只需要第i-1行的数据
 * 3. 从右往左更新数组，避免覆盖还需要使用的数据
 * 4. 每次迭代在数组末尾添加一个1
 * 
 * 核心技巧：
 * - 逆序更新：从后往前更新避免数据被提前覆盖
 * - 原地更新：ans[j] = ans[j-1] + ans[j]
 * - 动态扩展：每行末尾添加新的1
 * 
 * 算法步骤：
 * 1. 从第0行开始，逐行构建到第rowIndex行
 * 2. 对于每一行：
 *    - 从倒数第二个位置开始向前更新
 *    - 每个位置的新值 = 原值 + 前一个位置的值
 *    - 在末尾添加新的1
 * 
 * 正确性分析：
 * - 逆序更新保证了ans[j-1]是上一行的值
 * - ans[j]是当前行旧的值，相当于上一行的值
 * - 因此ans[j-1] + ans[j]正确计算了新的ans[j]
 * 
 * 时间复杂度：O(rowIndex²) - 需要计算所有中间结果
 * 空间复杂度：O(rowIndex) - 只使用一个数组
 * 
 * LeetCode链接：https://leetcode.com/problems/pascals-triangle-ii/
 */
public class P119_PascalTriangleII {
    
    /**
     * 获取杨辉三角的第rowIndex行
     * 
     * @param rowIndex 行索引（从0开始）
     * @return 第rowIndex行的所有元素
     */
    public List<Integer> getRow(int rowIndex) {
        List<Integer> ans = new ArrayList<>();
        
        // 逐行构建，从第0行到第rowIndex行
        for (int i = 0; i <= rowIndex; i++) {
            // 关键：从右往左更新，避免覆盖还需要使用的数据
            // 更新范围：从倒数第二个位置到第二个位置
            for (int j = i - 1; j > 0; j--) {
                // 当前位置的新值 = 上一行对应位置 + 上一行前一个位置
                // ans[j] (新) = ans[j-1] (上一行) + ans[j] (上一行)
                ans.set(j, ans.get(j - 1) + ans.get(j));
            }
            
            // 每行末尾添加新的1
            ans.add(1);
        }
        
        return ans;
    }
}
