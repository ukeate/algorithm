package leetc.top;

/**
 * LeetCode 48. 旋转图像 (Rotate Image)
 * 
 * 问题描述：
 * 给定一个 n × n 的二维矩阵 matrix 表示一个图像。请你将图像顺时针旋转 90 度。
 * 你必须在原地旋转图像，这意味着你需要直接修改输入的二维矩阵。请不要使用另一个矩阵来旋转图像。
 * 
 * 示例：
 * 输入：matrix = [[1,2,3],[4,5,6],[7,8,9]]
 * 输出：[[7,4,1],[8,5,2],[9,6,3]]
 * 
 * 解法思路：
 * 分层旋转 - 一圈一圈地进行旋转：
 * 1. 将矩阵看作多个同心的正方形环
 * 2. 从外到内，逐层旋转每个环
 * 3. 对于每一层，进行四个位置的循环交换：
 *    上→右，右→下，下→左，左→上
 * 4. 每次交换一组四个对应位置的元素
 * 
 * 位置映射（顺时针90度旋转）：
 * (i,j) → (j, n-1-i)
 * 即：上边的第i个元素 → 右边的第i个位置
 * 
 * 四点循环交换：
 * A→temp, B→A, C→B, D→C, temp→D
 * 
 * 时间复杂度：O(n²) - 需要处理每个矩阵元素
 * 空间复杂度：O(1) - 原地旋转，只使用常数额外空间
 */
public class P48_RotateImage {
    
    /**
     * 旋转矩阵的一条边（一层的四条边）
     * 
     * 算法思路：
     * 对于矩阵的一层，需要旋转四条边：
     * - 上边：(tr, tc+i) 到 (tr, dc-i)
     * - 右边：(tr+i, dc) 到 (dr-i, dc)  
     * - 下边：(dr, dc-i) 到 (dr, tc+i)
     * - 左边：(dr-i, tc) 到 (tr+i, tc)
     * 
     * @param m 矩阵
     * @param tr 当前层左上角行坐标
     * @param tc 当前层左上角列坐标  
     * @param dr 当前层右下角行坐标
     * @param dc 当前层右下角列坐标
     */
    private static void rotateEdge(int[][] m, int tr, int tc, int dr, int dc) {
        int times = dc - tc;  // 当前层需要处理的元素个数
        int tmp = 0;          // 临时变量用于交换
        
        // 对当前层的每个位置进行四点循环交换
        for (int i = 0; i < times; i++) {
            // 保存上边的元素
            tmp = m[tr][tc + i];
            
            // 左边 → 上边：(dr-i, tc) → (tr, tc+i)
            m[tr][tc + i] = m[dr - i][tc];
            
            // 下边 → 左边：(dr, dc-i) → (dr-i, tc)
            m[dr - i][tc] = m[dr][dc - i];
            
            // 右边 → 下边：(tr+i, dc) → (dr, dc-i)
            m[dr][dc - i] = m[tr + i][dc];
            
            // 上边 → 右边：tmp → (tr+i, dc)
            m[tr + i][dc] = tmp;
        }
    }

    /**
     * 将图像顺时针旋转90度
     * 
     * 算法步骤：
     * 1. 确定矩阵的边界：左上角(tr,tc)和右下角(dr,dc)
     * 2. 从外层到内层，逐层旋转
     * 3. 每旋转完一层，边界向内收缩
     * 4. 当tr >= dr时，所有层都已处理完毕
     * 
     * @param matrix n×n的二维矩阵
     */
    public void rotate(int[][] matrix) {
        // 初始化边界：整个矩阵的边界
        int tr = 0, tc = 0;                              // 左上角坐标
        int dr = matrix.length - 1, dc = matrix[0].length - 1;  // 右下角坐标
        
        // 从外层到内层逐层旋转
        while (tr < dr) {
            rotateEdge(matrix, tr++, tc++, dr--, dc--);  // 旋转当前层并收缩边界
        }
    }
}
