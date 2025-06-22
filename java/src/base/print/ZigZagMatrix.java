package base.print;

/**
 * Z字形打印矩阵（对角线打印）
 * 
 * 问题描述：
 * 按照Z字形路径打印矩阵中的所有元素
 * 即按对角线交替从右上到左下、从左下到右上的方向打印
 * 
 * 算法思路：
 * 1. 使用两个点A和B来定义当前对角线的起点和终点
 * 2. A点负责寻找对角线的起点，B点负责寻找对角线的终点
 * 3. A点移动规则：先向右，到边界后向下
 * 4. B点移动规则：先向下，到边界后向右  
 * 5. 每条对角线交替改变打印方向
 * 
 * 时间复杂度：O(M×N) - 每个元素访问一次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * 应用场景：
 * - 图像处理中的对角线扫描
 * - 数据的特殊遍历模式
 * - 矩阵压缩存储的读取顺序
 */
public class ZigZagMatrix {
    /**
     * 打印一条对角线上的所有元素
     * 
     * @param m 矩阵
     * @param tR 对角线起点的行坐标
     * @param tC 对角线起点的列坐标
     * @param dR 对角线终点的行坐标
     * @param dC 对角线终点的列坐标
     * @param f 打印方向标志，true表示从右上到左下，false表示从左下到右上
     */
    private static void printPath(int[][] m, int tR, int tC, int dR, int dC, boolean f) {
        if (f) {
            // 从右上到左下打印：行递增，列递减
            while (tR <= dR) {
                System.out.print(m[tR++][tC--] + " ");
            }
        } else {
            // 从左下到右上打印：行递减，列递增
            while (dR >= tR) {
                System.out.print(m[dR--][dC++] + " ");
            }
        }
    }
    
    /**
     * Z字形打印整个矩阵
     * 
     * 算法步骤：
     * 1. 初始化两个指针A(tR, tC)和B(dR, dC)，都指向(0,0)
     * 2. 在每次循环中：
     *    - 打印从A到B的对角线
     *    - 移动A点：优先向右，到边界后向下
     *    - 移动B点：优先向下，到边界后向右
     *    - 切换打印方向
     * 3. 直到遍历完所有对角线
     * 
     * @param matrix 待打印的矩阵
     */
    public static void zigzag(int[][] matrix) {
        int tR = 0, tC = 0;             // A点坐标（对角线起点）
        int dR = 0, dC = 0;             // B点坐标（对角线终点）
        int endR = matrix.length - 1;    // 矩阵最大行号
        int endC = matrix[0].length - 1; // 矩阵最大列号
        boolean fromUp = false;          // 打印方向标志
        
        // 当A点还没有超出矩阵边界时
        while (tR <= endR) {
            // 打印当前对角线
            printPath(matrix, tR, tC, dR, dC, fromUp);
            
            // A点移动：优先向右，到边界后向下
            tR = tC == endC ? tR + 1 : tR;    // 如果列到达边界，行加1
            tC = tC == endC ? tC : tC + 1;    // 如果列未到边界，列加1
            
            // B点移动：优先向下，到边界后向右
            dC = dR == endR ? dC + 1 : dC;    // 如果行到达边界，列加1
            dR = dR == endR ? dR : dR + 1;    // 如果行未到边界，行加1
            
            // 切换打印方向
            fromUp = !fromUp;
        }
    }

    /**
     * 测试方法 - 演示Z字形打印效果
     */
    public static void main(String[] args) {
        // 创建3×4测试矩阵
        int[][] matrix = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
        
        System.out.println("矩阵内容:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
        
        System.out.println("\nZ字形打印结果:");
        zigzag(matrix);
        System.out.println();
    }
}
