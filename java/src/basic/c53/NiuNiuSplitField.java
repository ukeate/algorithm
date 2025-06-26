package basic.c53;

import java.util.Scanner;

/**
 * 牛牛分田地问题
 * 
 * 问题描述：
 * 有一块矩形田地，需要用3刀（2横1竖或2竖1横）将田地分成4块。
 * 牛牛总是选择面积最小的一块。为了让牛牛得到尽可能大的地，
 * 应该如何切分？返回牛牛能得到的最大面积。
 * 
 * 约束条件：
 * - 必须切3刀，分成4块
 * - 牛牛选择面积最小的一块
 * - 田地用0-1矩阵表示，1表示有价值的土地
 * 
 * 算法思路：
 * 1. 枚举所有可能的3刀切法
 * 2. 对于每种切法，计算4块田地的面积
 * 3. 取最小面积，然后在所有切法中取最大值
 * 4. 使用前缀和优化区域面积计算
 * 5. 使用单调性优化枚举过程
 * 
 * 时间复杂度：O(n^3 * m^3)
 * 空间复杂度：O(n * m)
 * 
 * 来源：牛客网
 * https://www.nowcoder.com/questionTerminal/fe30a13b5fb84b339cb6cb3f70dca699
 * 
 * @author 算法学习
 */
public class NiuNiuSplitField {
    
    /**
     * 主方法：处理输入输出
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int n = in.nextInt();  // 行数
            int m = in.nextInt();  // 列数
            int[][] matrix = new int[n][m];
            
            // 读取田地矩阵
            for (int i = 0; i < n; i++) {
                char[] chas = in.next().toCharArray();
                for (int j = 0; j < m; j++) {
                    matrix[i][j] = chas[j] - '0';
                }
            }
            
            System.out.println(max(matrix));
        }
    }

    /**
     * 构建前缀和数组
     * 用于快速计算任意矩形区域的面积
     * 
     * @param matrix 原始田地矩阵
     * @return 前缀和数组
     */
    private static int[][] sum(int[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        int[][] record = new int[row][col];
        
        // 初始化第一个元素
        record[0][0] = matrix[0][0];
        
        // 填充第一行
        for (int i = 1; i < row; i++) {
            record[i][0] = record[i - 1][0] + matrix[i][0];
        }
        
        // 填充第一列
        for (int j = 1; j < col; j++) {
            record[0][j] = record[0][j - 1] + matrix[0][j];
        }
        
        // 填充其余位置
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                record[i][j] = record[i][j - 1] + record[i - 1][j] 
                             - record[i - 1][j - 1] + matrix[i][j];
            }
        }
        
        return record;
    }

    /**
     * 计算矩形区域的面积
     * 使用前缀和数组快速计算
     * 
     * @param record 前缀和数组
     * @param i1 矩形左上角行坐标
     * @param j1 矩形左上角列坐标
     * @param i2 矩形右下角行坐标
     * @param j2 矩形右下角列坐标
     * @return 矩形区域的面积
     */
    private static int area(int[][] record, int i1, int j1, int i2, int j2) {
        int all = record[i2][j2];                                    // 到右下角的总和
        int left = j1 > 0 ? record[i2][j1 - 1] : 0;                 // 左边需要减去的部分
        int up = i1 > 0 ? record[i1 - 1][j2] : 0;                   // 上面需要减去的部分
        int makeUp = (i1 > 0 && j1 > 0) ? record[i1 - 1][j1 - 1] : 0; // 重复减去的部分要加回来
        
        return all - left - up + makeUp;
    }

    /**
     * 计算用三刀分割后四块区域的最小面积
     * 三刀为：3条竖线
     * 
     * @param record 前缀和数组
     * @param c1 第1条竖线位置
     * @param c2 第2条竖线位置
     * @param c3 第3条竖线位置
     * @param prow 上边界行
     * @param crow 下边界行
     * @return 四块区域的最小面积
     */
    private static int val(int[][] record, int c1, int c2, int c3, int prow, int crow) {
        // 计算四块区域的面积
        int v1 = area(record, prow, 0, crow, c1);                           // 第1块
        int v2 = area(record, prow, c1 + 1, crow, c2);                      // 第2块
        int v3 = area(record, prow, c2 + 1, crow, c3);                      // 第3块
        int v4 = area(record, prow, c3 + 1, crow, record[0].length - 1);    // 第4块
        
        // 返回最小面积
        return Math.min(Math.min(v1, v2), Math.min(v3, v4));
    }

    /**
     * 计算用一条横线分割后，上下两部分的最小面积
     * 
     * @param record 前缀和数组
     * @param c1 第1条竖线位置
     * @param c2 第2条竖线位置
     * @param c3 第3条竖线位置
     * @param i 区域上边界
     * @param split 横线分割位置
     * @param j 区域下边界
     * @return 上下两部分的最小面积
     */
    private static int twoRowMin(int[][] record, int c1, int c2, int c3, int i, int split, int j) {
        int upperMin = val(record, c1, c2, c3, i, split);         // 上半部分的最小面积
        int lowerMin = val(record, c1, c2, c3, split + 1, j);     // 下半部分的最小面积
        return Math.min(upperMin, lowerMin);
    }

    /**
     * 计算上半部分的最优分割方案
     * 对于每个位置i，找到最优的横线分割位置
     * 
     * @param record 前缀和数组
     * @param c1 第1条竖线位置
     * @param c2 第2条竖线位置
     * @param c3 第3条竖线位置
     * @return 每个位置的最优分割结果
     */
    private static int[] upSplit(int[][] record, int c1, int c2, int c3) {
        int size = record.length;
        int[] up = new int[size];
        int split = 0;  // 当前最优的分割位置
        
        // 处理边界情况
        up[1] = Math.min(
            val(record, c1, c2, c3, 0, 0),      // 上部分只有第0行
            val(record, c1, c2, c3, 1, 1)       // 下部分只有第1行
        );
        
        // 动态计算每个位置的最优分割
        for (int i = 2; i < size; i++) {
            int upSplitMax = twoRowMin(record, c1, c2, c3, 0, split, i);
            
            // 利用单调性优化：尝试移动分割线
            while (split < i - 1) {
                int moved = twoRowMin(record, c1, c2, c3, 0, split + 1, i);
                if (moved < upSplitMax) {
                    // 发现更优解不存在，后续只会更差
                    break;
                } else {
                    upSplitMax = moved;
                    split++;
                }
            }
            up[i] = upSplitMax;
        }
        
        return up;
    }

    /**
     * 计算下半部分的最优分割方案
     * 对于每个位置i，找到最优的横线分割位置
     * 
     * @param record 前缀和数组
     * @param c1 第1条竖线位置
     * @param c2 第2条竖线位置
     * @param c3 第3条竖线位置
     * @return 每个位置的最优分割结果
     */
    private static int[] downSplit(int[][] record, int c1, int c2, int c3) {
        int size = record.length;
        int[] down = new int[size];
        int split = size - 1;  // 当前最优的分割位置
        
        // 处理边界情况
        down[size - 2] = Math.min(
            val(record, c1, c2, c3, size - 2, size - 2),    // 上部分只有倒数第2行
            val(record, c1, c2, c3, size - 1, size - 1)     // 下部分只有最后1行
        );
        
        // 从下往上动态计算
        for (int i = size - 3; i >= 0; i--) {
            int downSplitMax = twoRowMin(record, c1, c2, c3, i, split - 1, size - 1);
            
            // 利用单调性优化：尝试移动分割线
            while (split > i + 1) {
                int moved = twoRowMin(record, c1, c2, c3, i, split - 2, size - 1);
                if (moved < downSplitMax) {
                    break;
                } else {
                    downSplitMax = moved;
                    split--;
                }
            }
            down[i] = downSplitMax;
        }
        
        return down;
    }

    /**
     * 对于给定的三条竖线，找到最优的横线分割方案
     * 
     * @param help 前缀和数组
     * @param c1 第1条竖线位置
     * @param c2 第2条竖线位置
     * @param c3 第3条竖线位置
     * @return 该竖线配置下的最优结果
     */
    private static int best(int[][] help, int c1, int c2, int c3) {
        // 预计算上下分割的最优方案
        int[] up = upSplit(help, c1, c2, c3);      // [0,i]切1刀后8块的最大最小值
        int[] down = downSplit(help, c1, c2, c3);  // [i,n-1]切1刀后8块的最大最小值
        
        int res = Integer.MIN_VALUE;
        
        // 枚举中间分割位置
        for (int mid = 1; mid < help.length - 2; mid++) {
            res = Math.max(res, Math.min(up[mid], down[mid + 1]));
        }
        
        return res;
    }

    /**
     * 主算法：找到最优的分田地方案
     * 
     * @param matrix 田地矩阵
     * @return 牛牛能得到的最大面积
     */
    public static int max(int[][] matrix) {
        // 边界检查
        if (matrix == null || matrix.length < 4 || matrix[0].length < 4) {
            return 0;
        }
        
        // 构建前缀和数组
        int[][] help = sum(matrix);
        int col = matrix[0].length;
        int res = Integer.MIN_VALUE;
        
        // 枚举所有可能的三条竖线位置
        for (int c1 = 0; c1 < col - 3; c1++) {           // 第1条竖线
            for (int c2 = c1 + 1; c2 < col - 2; c2++) {   // 第2条竖线
                for (int c3 = c2 + 1; c3 < col - 1; c3++) { // 第3条竖线
                    res = Math.max(res, best(help, c1, c2, c3));
                }
            }
        }
        
        return res;
    }
    
    /**
     * 示例测试
     */
    public static void testExample() {
        System.out.println("=== 牛牛分田地问题测试 ===");
        
        // 创建测试用例
        int[][] testMatrix = {
            {1, 1, 1, 1, 1, 1},
            {1, 0, 1, 1, 1, 1},
            {1, 1, 1, 0, 1, 1},
            {1, 1, 1, 1, 1, 0},
            {1, 1, 1, 1, 1, 1}
        };
        
        System.out.println("测试田地矩阵:");
        for (int i = 0; i < testMatrix.length; i++) {
            for (int j = 0; j < testMatrix[0].length; j++) {
                System.out.print(testMatrix[i][j] + " ");
            }
            System.out.println();
        }
        
        int result = max(testMatrix);
        System.out.println("牛牛能得到的最大面积: " + result);
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度: O(n³ * m³) - 三重循环枚举竖线，内部计算涉及行的枚举");
        System.out.println("空间复杂度: O(n * m) - 前缀和数组");
        System.out.println("核心思想: 枚举 + 前缀和优化 + 单调性剪枝");
        System.out.println("关键技巧: 预计算上下分割的最优方案，避免重复计算");
    }
}
