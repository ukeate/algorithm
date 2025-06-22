package base.print;

/**
 * 正方形矩阵顺时针旋转90度
 * 
 * 问题描述：
 * 将一个N×N的正方形矩阵顺时针旋转90度，要求原地操作
 * 
 * 算法思路：
 * 1. 分层处理，从外层到内层逐层旋转
 * 2. 对于每一层，按照外边界进行4个位置的循环交换
 * 3. 每次交换4个对称位置的元素
 * 
 * 时间复杂度：O(N²) - 每个元素访问一次
 * 空间复杂度：O(1) - 原地操作
 * 
 * 旋转规律：
 * (i,j) -> (j, n-1-i)
 * 即：第i行第j列的元素旋转后移动到第j行第(n-1-i)列
 */
public class RotateMatrix {
    /**
     * 旋转矩阵的一条边界
     * 
     * 对于矩阵的一层边界，按照顺时针方向进行4个位置的循环交换：
     * 上边界 -> 右边界 -> 下边界 -> 左边界 -> 上边界
     * 
     * @param m 矩阵
     * @param a 当前层左上角行坐标
     * @param b 当前层左上角列坐标  
     * @param c 当前层右下角行坐标
     * @param d 当前层右下角列坐标
     */
    private static void rotateEdge(int[][] m, int a, int b, int c, int d) {
        int tmp = 0;
        // 对当前层的每个位置组进行4个位置的循环交换
        for (int i = 0; i < d - b; i++) {
            tmp = m[a][b + i];                  // 保存上边界元素
            m[a][b + i] = m[c - i][b];         // 左边界 -> 上边界
            m[c - i][b] = m[c][d - i];         // 下边界 -> 左边界  
            m[c][d - i] = m[a + i][d];         // 右边界 -> 下边界
            m[a + i][d] = tmp;                  // 上边界 -> 右边界
        }
    }
    
    /**
     * 将正方形矩阵顺时针旋转90度
     * 
     * 算法步骤：
     * 1. 从最外层开始，逐层向内处理
     * 2. 对每一层调用rotateEdge进行边界旋转
     * 3. 每处理完一层，边界向内收缩一圈
     * 
     * @param matrix 待旋转的正方形矩阵
     */
    public static void rotate(int[][] matrix) {
        int a = 0;                          // 当前层左上角行坐标
        int b = 0;                          // 当前层左上角列坐标
        int c = matrix.length - 1;          // 当前层右下角行坐标
        int d = matrix[0].length - 1;       // 当前层右下角列坐标
        
        // 当还有层需要处理时
        while (a < c) {
            rotateEdge(matrix, a++, b++, c--, d--);
        }
    }

    /**
     * 打印矩阵（辅助方法）
     * 
     * @param matrix 要打印的矩阵
     */
    public static void print(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * 测试方法 - 演示矩阵旋转效果
     */
    public static void main(String[] args) {
        // 创建4×4测试矩阵
        int[][] matrix = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
        System.out.println("旋转前:");
        print(matrix);
        
        // 顺时针旋转90度
        rotate(matrix);
        System.out.println("旋转后:");
        print(matrix);
    }
}
