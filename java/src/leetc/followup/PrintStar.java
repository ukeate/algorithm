package leetc.followup;

/**
 * 打印星号螺旋图案 (Print Star Spiral Pattern)
 * 
 * 问题描述：
 * 在一个n×n的矩阵中，按照螺旋方式打印星号(*)
 * 从外圈到内圈，每一圈都是一个完整的矩形框
 * 
 * 图案特点：
 * - 外层是一个完整的矩形框
 * - 内层也是矩形框，但每圈之间间隔1行1列
 * - 继续向内，直到无法形成完整矩形
 * 
 * 示例 (n=5)：
 * * * * * *
 * *       *
 * * * * * *
 * 
 * 解法思路：
 * 分层绘制：
 * 1. 确定当前层的左上角(leftUp)和右下角(rightDown)坐标
 * 2. 按顺序绘制矩形的四条边：
 *    - 上边：从左到右
 *    - 右边：从上到下（跳过右上角，避免重复）
 *    - 下边：从右到左（跳过右下角，避免重复）
 *    - 左边：从下到上（跳过左下角和左上角，避免重复）
 * 3. 向内收缩：leftUp += 2, rightDown -= 2
 * 4. 重复直到无法形成有效矩形
 * 
 * 边界处理：
 * - 确保不会绘制重复的角点
 * - 处理单行或单列的特殊情况
 * 
 * 时间复杂度：O(n²) - 需要填充整个矩阵
 * 空间复杂度：O(n²) - 存储矩阵空间
 */
public class PrintStar {
    /**
     * 为指定矩形区域绘制边框
     * 
     * @param m 矩阵
     * @param lu 左上角坐标值（同时代表行和列）
     * @param rd 右下角坐标值（同时代表行和列）
     */
    private static void set(char[][] m, int lu, int rd) {
        // 第一条边：上边框，从左到右
        for (int col = lu; col <= rd; col++) {
            m[lu][col] = '*';
        }
        
        // 第二条边：右边框，从上到下（跳过右上角避免重复）
        for (int row = lu + 1; row <= rd; row++) {
            m[row][rd] = '*';
        }
        
        // 第三条边：下边框，从右到左（跳过右下角避免重复）
        for (int col = rd - 1; col > lu; col--) {
            m[rd][col] = '*';
        }
        
        // 第四条边：左边框，从下到上（跳过左下角和左上角避免重复）
        // 注意：这里是 lu+1，因为需要给内层留出空间
        for (int row = rd - 1; row > lu + 1; row--) {
            m[row][lu + 1] = '*';
        }
    }

    /**
     * 打印n×n的星号螺旋图案
     * 
     * @param n 矩阵大小
     */
    public static void printStar(int n) {
        int leftUp = 0, rightDown = n - 1;  // 初始化外层矩形的边界
        char[][] m = new char[n][n];        // 创建矩阵
        
        // 初始化矩阵，全部填充空格
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = ' ';
            }
        }
        
        // 分层绘制矩形框，从外层到内层
        while (leftUp <= rightDown) {
            set(m, leftUp, rightDown);  // 绘制当前层的矩形框
            leftUp += 2;                // 向内收缩，间隔2个位置
            rightDown -= 2;
        }
        
        // 输出结果矩阵
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println(); // 换行
        }
    }

    public static void main(String[] args) {
        printStar(20);
    }
}
