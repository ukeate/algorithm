package base.print;

/**
 * 螺旋打印矩阵
 * 
 * 问题描述：
 * 按照螺旋顺序（顺时针）打印矩阵中的所有元素
 * 
 * 算法思路：
 * 1. 定义四个边界：上边界、下边界、左边界、右边界
 * 2. 按照 "右->下->左->上" 的顺序遍历当前圈
 * 3. 每完成一圈，边界向内收缩
 * 4. 特殊处理只有一行或一列的情况
 * 
 * 时间复杂度：O(M×N) - 每个元素访问一次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * 应用场景：
 * - 矩阵的螺旋遍历
 * - 图像处理中的螺旋扫描
 * - 数据展示的特殊排列
 */
public class SpiralMatrix {
    /**
     * 打印矩阵的一个螺旋边界
     * 
     * 处理三种情况：
     * 1. 只有一行：从左到右打印
     * 2. 只有一列：从上到下打印  
     * 3. 完整矩形：按螺旋顺序打印
     * 
     * @param m 矩阵
     * @param tr 当前圈的上边界行号
     * @param tc 当前圈的左边界列号
     * @param dr 当前圈的下边界行号
     * @param dc 当前圈的右边界列号
     */
    private static void spiralEdge(int[][] m, int tr, int tc, int dr, int dc) {
        if (tr == dr) {
            // 只有一行的情况：从左到右打印
            for (int i = tc; i <= dc; i++) {
                System.out.print(m[tr][i] + " ");
            }
        } else if (tc == dc) {
            // 只有一列的情况：从上到下打印
            for (int i = tr; i <= dr; i++) {
                System.out.print(m[i][tc] + " ");
            }
        } else {
            // 完整矩形的情况：按螺旋顺序打印
            int curC = tc;  // 当前列指针
            int curR = tr;  // 当前行指针
            
            // 第一步：从左到右打印上边界（不包括右下角）
            while (curC != dc) {
                System.out.print(m[tr][curC] + " ");
                curC++;
            }
            
            // 第二步：从上到下打印右边界（不包括左下角）
            while (curR != dr) {
                System.out.print(m[curR][dc] + " ");
                curR++;
            }
            
            // 第三步：从右到左打印下边界（不包括左上角）
            while (curC != tc) {
                System.out.print(m[dr][curC] + " ");
                curC--;
            }
            
            // 第四步：从下到上打印左边界（不包括右上角）
            while (curR != tr) {
                System.out.print(m[curR][tc] + " ");
                curR--;
            }
        }
    }

    /**
     * 螺旋打印整个矩阵
     * 
     * 算法步骤：
     * 1. 初始化四个边界指针
     * 2. 当边界合法时，打印当前圈
     * 3. 边界向内收缩，继续下一圈
     * 4. 直到所有元素都被打印
     * 
     * @param matrix 待打印的矩阵
     */
    public static void spiral(int[][] matrix) {
        int tr = 0;                     // 上边界
        int tc = 0;                     // 左边界
        int dr = matrix.length - 1;     // 下边界
        int dc = matrix[0].length - 1;  // 右边界
        
        // 当边界有效时，继续螺旋打印
        while (tr <= dr && tc <= dc) {
            spiralEdge(matrix, tr++, tc++, dr--, dc--);
        }
    }

    /**
     * 测试方法 - 演示螺旋打印效果
     */
    public static void main(String[] args) {
        // 创建4×4测试矩阵
        int[][] matrix = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
        
        System.out.println("矩阵内容:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
        
        System.out.println("\n螺旋打印结果:");
        spiral(matrix);
        System.out.println();
    }
}
