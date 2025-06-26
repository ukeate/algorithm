package basic.c33;

/**
 * 最小路径和问题
 * 
 * 问题描述：
 * 给定一个包含非负整数的m x n网格，找到一条从左上角到右下角的路径，
 * 使得路径上的数字总和最小。每次只能向右或向下移动一步。
 * 
 * 例如：
 * 输入网格：
 * [1, 3, 1]
 * [1, 5, 1]
 * [4, 2, 1]
 * 最小路径：1→3→1→1→1，总和为7
 * 
 * 算法思路：
 * 使用动态规划解决：
 * 1. dp[i][j]表示到达位置(i,j)的最小路径和
 * 2. 状态转移方程：dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
 * 3. 边界条件：第一行和第一列只能从一个方向到达
 * 
 * 时间复杂度：O(m*n)
 * 空间复杂度：O(m*n)，可优化至O(min(m,n))
 */
public class MinPathSum {
    
    /**
     * 方法1：标准动态规划解法
     * 使用二维dp数组存储每个位置的最小路径和
     * 
     * @param m 输入的二维网格
     * @return 从左上角到右下角的最小路径和
     */
    public static int min1(int[][] m) {
        // 边界条件检查
        if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
            return 0;
        }
        
        int row = m.length;     // 行数
        int col = m[0].length;  // 列数
        
        // dp[i][j]表示到达位置(i,j)的最小路径和
        int[][] dp = new int[row][col];
        
        // 初始化起点
        dp[0][0] = m[0][0];
        
        // 初始化第一列：只能从上方到达
        for (int i = 1; i < row; i++) {
            dp[i][0] = dp[i - 1][0] + m[i][0];
        }
        
        // 初始化第一行：只能从左方到达
        for (int j = 1; j < col; j++) {
            dp[0][j] = dp[0][j - 1] + m[0][j];
        }
        
        // 填充dp表：每个位置都可以从上方或左方到达
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                // 选择上方和左方路径中较小的一条
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + m[i][j];
            }
        }
        
        // 返回终点的最小路径和
        return dp[row - 1][col - 1];
    }

    /**
     * 方法2：空间优化版本
     * 使用一维数组滚动更新，将空间复杂度优化到O(min(m,n))
     * 
     * 优化原理：
     * 由于每个位置只依赖于上方和左方的值，可以用一维数组逐行(或逐列)更新
     * 选择较短的维度作为数组长度，进一步节省空间
     * 
     * @param m 输入的二维网格
     * @return 从左上角到右下角的最小路径和
     */
    public static int min2(int[][] m) {
        // 边界条件检查
        if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
            return 0;
        }
        
        int more = Math.max(m.length, m[0].length);    // 较长的维度
        int less = Math.min(m.length, m[0].length);    // 较短的维度
        boolean isRowMore = more == m.length;          // 判断是否行数更多
        
        // 使用较短维度的长度创建一维dp数组
        int[] arr = new int[less];
        
        // 初始化数组的第一个元素
        arr[0] = m[0][0];
        
        // 初始化数组：沿着较短维度的第一行/列
        for (int i = 1; i < less; i++) {
            arr[i] = arr[i - 1] + (isRowMore ? m[0][i] : m[i][0]);
        }
        
        // 逐步更新：沿着较长维度进行滚动更新
        for (int i = 1; i < more; i++) {
            // 更新边界元素：只能从一个方向到达
            arr[0] = arr[0] + (isRowMore ? m[i][0] : m[0][i]);
            
            // 更新其他元素：可以从两个方向到达，选择较小值
            for (int j = 1; j < less; j++) {
                arr[j] = Math.min(arr[j - 1], arr[j]) + (isRowMore ? m[i][j] : m[j][i]);
            }
        }
        
        // 返回最后一个元素，即终点的最小路径和
        return arr[less - 1];
    }

    /**
     * 生成随机矩阵用于测试
     * @param rowSize 行数
     * @param colSize 列数
     * @return 随机生成的二维整数矩阵
     */
    private static int[][] randomMatrix(int rowSize, int colSize) {
        if (rowSize < 0 || colSize < 0) {
            return null;
        }
        
        int[][] rst = new int[rowSize][colSize];
        for (int i = 0; i < rst.length; i++) {
            for (int j = 0; j < rst[0].length; j++) {
                rst[i][j] = (int) (10 * Math.random()); // 生成0-9的随机数
            }
        }
        return rst;
    }

    /**
     * 打印矩阵的辅助方法
     * @param matrix 要打印的二维矩阵
     */
    private static void print(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * 测试方法：验证两种算法的正确性
     */
    public static void main(String[] args) {
        // 测试用例1：固定矩阵
        int[][] m = {{1, 3, 5, 9}, {8, 1, 3, 4}, {5, 0, 6, 1}, {8, 8, 4, 0}};
        
        System.out.println("测试矩阵：");
        print(m);
        System.out.println("标准DP算法结果：" + min1(m));
        System.out.println("空间优化算法结果：" + min2(m));
        
        // 测试用例2：随机矩阵验证
        System.out.println("\n随机测试验证：");
        for (int i = 0; i < 10; i++) {
            int[][] randomM = randomMatrix(5, 4);
            int result1 = min1(randomM);
            int result2 = min2(randomM);
            if (result1 != result2) {
                System.out.println("算法结果不一致！");
                print(randomM);
                System.out.println("标准DP：" + result1 + ", 优化版：" + result2);
                break;
            }
        }
        System.out.println("随机测试通过！");
    }
}
