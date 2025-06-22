package base.stack;

/**
 * 全1子矩阵计数问题
 * 问题描述：给定一个二进制矩阵，计算有多少个子矩阵的所有元素都是1
 * 
 * 算法思路：
 * 1. 将二维问题转化为一维问题：对每一行，计算以该行为底边的直方图
 * 2. 对每个直方图，使用单调栈计算以每个柱子为最矮柱子的矩形数量
 * 3. 累加所有行的结果得到最终答案
 * 
 * 核心思想：
 * - 以每一行为底边，向上统计连续1的高度，形成直方图
 * - 在直方图中，以每个位置为最矮高度，计算能形成多少个矩形
 * - 使用组合数学：宽度为n的区间能形成n*(n+1)/2个子区间
 * 
 * 时间复杂度：O(M*N) - M为行数，N为列数，每个元素最多进栈出栈一次
 * 空间复杂度：O(N) - 单调栈和高度数组的空间
 * 
 * 示例：
 * 输入矩阵：
 * [[1,0,1],
 *  [1,1,1],
 *  [1,1,1]]
 * 
 * 处理过程：
 * 第0行：heights=[1,0,1] -> 贡献2个矩形
 * 第1行：heights=[2,1,2] -> 贡献6个矩形  
 * 第2行：heights=[3,2,3] -> 贡献10个矩形
 * 总计：18个全1子矩阵
 * 
 * @see <a href="https://leetcode.com/problems/count-submatrices-with-all-ones">LeetCode 1504</a>
 */
// https://leetcode.com/problems/count-submatrices-with-all-ones
public class SubMatrices {
    
    /**
     * 计算组合数：1+2+...+n = n*(n+1)/2
     * 表示长度为n的区间能形成多少个子区间
     * 
     * 数学原理：
     * 对于长度为n的区间，可以选择的子区间有：
     * - 长度为1的：n个
     * - 长度为2的：n-1个
     * - ...
     * - 长度为n的：1个
     * 总计：n + (n-1) + ... + 1 = n*(n+1)/2
     * 
     * @param n 区间长度
     * @return 子区间的数量
     */
    private static int num(int n) {
        return ((1 + n) * n) >> 1;  // 使用位运算除以2
    }
    
    /**
     * 计算直方图中所有矩形的数量（以每个柱子为最矮柱子）
     * 使用单调栈找到每个位置左右边界，然后计算贡献
     * 
     * 算法步骤：
     * 1. 维护一个单调递增栈
     * 2. 当遇到比栈顶小的元素时，计算栈顶元素的贡献
     * 3. 栈顶元素的贡献 = (高度差) * (宽度的组合数)
     * 4. 处理栈中剩余元素
     * 
     * 关键思想：
     * 对于高度为h的柱子，在区间[left+1, right-1]中：
     * - 它是最矮的柱子
     * - 可以形成的矩形数量 = (h - max(左边界高度, 右边界高度)) * 区间宽度的组合数
     * 
     * @param heights 直方图高度数组
     * @return 该直方图中所有矩形的数量
     */
    private static int bottomCount(int[] heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }
        
        int nums = 0;  // 矩形总数
        int[] stack = new int[heights.length];  // 单调栈，存储下标
        int si = -1;  // 栈顶指针
        
        // 遍历每个柱子
        for (int i = 0; i < heights.length; i++) {
            // 当栈不为空且栈顶元素高度大于等于当前元素时
            while (si != -1 && heights[stack[si]] >= heights[i]) {
                int cur = stack[si--];  // 弹出栈顶元素
                
                // 相等忽略，只处理严格大于的情况
                if (heights[cur] > heights[i]) {
                    int left = si == -1 ? -1 : stack[si];  // 左边界
                    int n = i - left - 1;  // 区间宽度
                    
                    // 计算有效高度：当前高度减去左右边界的最大高度
                    int down = Math.max(left == -1 ? 0 : heights[left], heights[i]);
                    
                    // 累加贡献：(有效高度) * (宽度组合数)
                    nums += (heights[cur] - down) * num(n);
                }
            }
            stack[++si] = i;  // 当前元素入栈
        }
        
        // 处理栈中剩余元素（右边没有更小的元素）
        while (si != -1) {
            int cur = stack[si--];  // 弹出栈顶元素
            int left = si == -1 ? -1 : stack[si];  // 左边界
            int n = heights.length - left - 1;  // 区间宽度
            
            // 计算有效高度：当前高度减去左边界高度
            int down = left == -1 ? 0 : heights[left];
            
            // 累加贡献
            nums += (heights[cur] - down) * num(n);
        }
        
        return nums;
    }
    
    /**
     * 计算二进制矩阵中全1子矩阵的数量
     * 
     * 算法流程：
     * 1. 逐行处理矩阵，将二维问题转化为多个一维直方图问题
     * 2. 对每一行，计算以该行为底边的直方图高度数组
     * 3. 调用bottomCount计算该直方图的矩形数量
     * 4. 累加所有行的结果
     * 
     * 高度数组更新规则：
     * - 如果当前位置是0，则高度重置为0
     * - 如果当前位置是1，则高度在前一行基础上加1
     * 
     * 示例分析：
     * 矩阵：[[1,0,1],
     *        [1,1,1],
     *        [1,1,1]]
     * 
     * 第0行处理后：heights=[1,0,1]
     * - 位置0：高度1，宽度1，贡献1个矩形
     * - 位置2：高度1，宽度1，贡献1个矩形
     * - 小计：2个矩形
     * 
     * 第1行处理后：heights=[2,1,2]
     * - 位置0：高度2，在宽度1内，贡献2个矩形
     * - 位置1：高度1，在宽度3内，贡献3个矩形
     * - 位置2：高度1（超出部分），在宽度1内，贡献1个矩形
     * - 小计：6个矩形
     * 
     * @param mat 二进制矩阵
     * @return 全1子矩阵的数量
     */
    public static int num(int[][] mat) {
        if (mat == null || mat.length == 0 || mat[0].length == 0) {
            return 0;
        }
        
        int nums = 0;  // 总矩形数
        int[] heights = new int[mat[0].length];  // 高度数组
        
        // 逐行处理矩阵
        for (int i = 0; i < mat.length; i++) {
            // 更新高度数组：以当前行为底边的直方图
            for (int j = 0; j < mat[0].length; j++) {
                heights[j] = mat[i][j] == 0 ? 0 : heights[j] + 1;
            }
            
            // 计算当前直方图的矩形数量并累加
            nums += bottomCount(heights);
        }
        
        return nums;
    }
}
