package basic.c45;

/**
 * 蛇游戏最大血量问题
 * 
 * 问题描述：
 * 在一个矩阵中，蛇可以从任意左侧位置开始，只能向右上、右、右下三个方向移动
 * 每个格子有一个数值，表示血量的变化（正数增加血量，负数减少血量）
 * 蛇有一次特殊能力，可以将某个格子的数值取反（正变负，负变正）
 * 血量在任何时候都不能为负数，求蛇能达到的最大血量
 * 
 * 移动规则：
 * - 只能从左侧任意行开始
 * - 每次只能向右上(↗)、右(→)、右下(↘)移动
 * - 血量不能为负，一旦为负就游戏结束
 * - 有且仅有一次机会将某个格子的值取反
 * 
 * 算法思路：
 * 使用动态规划，维护两个状态：
 * 1. 到达某位置且未使用能力时的最大血量
 * 2. 到达某位置且已使用能力时的最大血量
 * 
 * 状态转移：
 * - dp[i][j][0]: 到达(i,j)未使用能力的最大血量
 * - dp[i][j][1]: 到达(i,j)已使用能力的最大血量
 * 
 * 前置状态可能来自：
 * - (i, j-1): 从左边来
 * - (i-1, j-1): 从左上来
 * - (i+1, j-1): 从左下来
 * 
 * 时间复杂度：O(m*n) 其中m是行数，n是列数
 * 空间复杂度：O(m*n*2) 
 * 
 * @author 算法学习
 */
public class SnakeGame {
    
    /**
     * 递归方法：计算从最优路径到达位置(i,j)的最大血量
     * 
     * @param m 矩阵
     * @param i 目标行
     * @param j 目标列
     * @return 长度为2的数组，[未使用能力的最大血量, 使用能力的最大血量]
     * 
     * 算法思路：
     * 递归计算所有可能的前置状态，选择最优的路径
     * 返回两种情况下的最大血量：未使用能力和已使用能力
     */
    private static int[] process(int[][] m, int i, int j) {
        // 边界条件：到达最左侧列
        if (j == 0) {
            // 未使用能力：直接取当前值
            // 使用能力：取当前值的相反数
            return new int[]{m[i][j], -m[i][j]};
        }
        
        // 从左边位置(i, j-1)转移过来
        int[] preAns = process(m, i, j - 1);
        int preUnuse = preAns[0];  // 前面未使用能力的最大血量
        int preUse = preAns[1];    // 前面已使用能力的最大血量
        
        // 从左上位置(i-1, j-1)转移过来
        if (i - 1 >= 0) {
            preAns = process(m, i - 1, j - 1);
            preUnuse = Math.max(preUnuse, preAns[0]);
            preUse = Math.max(preUse, preAns[1]);
        }
        
        // 从左下位置(i+1, j-1)转移过来
        if (i + 1 < m.length) {
            preAns = process(m, i + 1, j - 1);
            preUnuse = Math.max(preUnuse, preAns[0]);
            preUse = Math.max(preUse, preAns[1]);
        }
        
        int no = -1;   // 当前位置未使用能力的最大血量
        int yes = -1;  // 当前位置已使用能力的最大血量
        
        // 如果前面未使用能力且血量非负
        if (preUnuse >= 0) {
            no = m[i][j] + preUnuse;      // 不在当前位置使用能力
            yes = -m[i][j] + preUnuse;    // 在当前位置使用能力（取反）
        }
        
        // 如果前面已使用能力且血量非负
        if (preUse >= 0) {
            yes = Math.max(yes, m[i][j] + preUse);  // 只能正常累加
        }
        
        return new int[]{no, yes};
    }

    /**
     * 方法1：使用递归求解最大血量
     * 
     * @param matrix 游戏矩阵
     * @return 能达到的最大血量
     * 
     * 算法思路：
     * 枚举所有可能的终点位置，计算每个位置的最大血量，取全局最大值
     */
    public static int max1(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        int res = Integer.MIN_VALUE;
        
        // 枚举所有可能的终点位置
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                int[] ans = process(matrix, i, j);
                // 取两种状态中的最大值
                res = Math.max(res, Math.max(ans[0], ans[1]));
            }
        }
        
        return res;
    }

    /**
     * 方法2：使用动态规划求解最大血量
     * 
     * @param matrix 游戏矩阵
     * @return 能达到的最大血量
     * 
     * 算法思路：
     * 使用三维DP数组，dp[i][j][k]表示到达位置(i,j)时的状态：
     * - k=0: 未使用能力的最大血量
     * - k=1: 已使用能力的最大血量
     * 
     * 按列从左到右填表，每列内枚举所有行
     */
    public static int max2(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        int max = Integer.MIN_VALUE;
        // dp[i][j][0]: 到达(i,j)未使用能力的最大血量
        // dp[i][j][1]: 到达(i,j)已使用能力的最大血量
        int[][][] dp = new int[matrix.length][matrix[0].length][2];
        
        // 初始化第一列：从左侧边界开始
        for (int i = 0; i < dp.length; i++) {
            dp[i][0][0] = matrix[i][0];        // 不使用能力
            dp[i][0][1] = -matrix[i][0];       // 使用能力取反
            max = Math.max(max, Math.max(dp[i][0][0], dp[i][0][1]));
        }
        
        // 按列填表，从第二列开始
        for (int j = 1; j < matrix[0].length; j++) {
            for (int i = 0; i < matrix.length; i++) {
                // 收集所有可能的前置状态
                int preUnuse = dp[i][j - 1][0];     // 从左边来
                int preUse = dp[i][j - 1][1];
                
                // 从左上来
                if (i - 1 >= 0) {
                    preUnuse = Math.max(preUnuse, dp[i - 1][j - 1][0]);
                    preUse = Math.max(preUse, dp[i - 1][j - 1][1]);
                }
                
                // 从左下来
                if (i + 1 < matrix.length) {
                    preUnuse = Math.max(preUnuse, dp[i + 1][j - 1][0]);
                    preUse = Math.max(preUse, dp[i + 1][j - 1][1]);
                }
                
                // 初始化当前状态为无效
                dp[i][j][0] = -1;
                dp[i][j][1] = -1;
                
                // 如果前面未使用能力且血量非负
                if (preUnuse >= 0) {
                    dp[i][j][0] = matrix[i][j] + preUnuse;      // 不在当前使用能力
                    dp[i][j][1] = -matrix[i][j] + preUnuse;     // 在当前使用能力
                }
                
                // 如果前面已使用能力且血量非负
                if (preUse >= 0) {
                    dp[i][j][1] = Math.max(dp[i][j][1], matrix[i][j] + preUse);
                }
                
                // 更新全局最大值
                max = Math.max(max, Math.max(dp[i][j][0], dp[i][j][1]));
            }
        }
        
        return max;
    }

    /**
     * 生成随机测试矩阵
     * 
     * @param row 最大行数
     * @param col 最大列数  
     * @param val 数值的最大绝对值
     * @return 随机生成的矩阵
     */
    private static int[][] randomMatrix(int row, int col, int val) {
        int[][] arr = new int[(int) (row * Math.random()) + 1][(int) (col * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                // 生成[-val, val]范围内的随机数
                arr[i][j] = (int) ((val + 1) * Math.random() * (Math.random() > 0.5 ? -1 : 1));
            }
        }
        return arr;
    }

    /**
     * 打印矩阵（用于调试）
     */
    private static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%4d ", matrix[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 蛇游戏最大血量测试 ===");
        
        // 手工测试用例
        int[][] test1 = {
            {1, -3, 3},
            {0, 6, 1},
            {2, 3, 1}
        };
        
        System.out.println("测试矩阵1:");
        printMatrix(test1);
        System.out.println("方法1结果: " + max1(test1));
        System.out.println("方法2结果: " + max2(test1));
        System.out.println();
        
        // 另一个测试用例
        int[][] test2 = {
            {1, -4, 1, 3},
            {2, -3, 2, 1},
            {3, -2, 3, 1}
        };
        
        System.out.println("测试矩阵2:");
        printMatrix(test2);
        System.out.println("方法1结果: " + max1(test2));
        System.out.println("方法2结果: " + max2(test2));
        System.out.println();
        
        // 大规模随机测试
        System.out.println("开始大规模随机测试...");
        int times = 1000000;
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < times; i++) {
            int[][] matrix = randomMatrix(5, 5, 10);
            int ans1 = max1(matrix);
            int ans2 = max2(matrix);
            if (ans1 != ans2) {
                System.out.println("错误！结果不一致:");
                System.out.println("递归方法: " + ans1);
                System.out.println("DP方法: " + ans2);
                System.out.println("出错矩阵:");
                printMatrix(matrix);
                return;
            }
            
            // 每完成10万次测试输出进度
            if ((i + 1) % 100000 == 0) {
                System.out.printf("已完成 %d 次测试\n", i + 1);
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.printf("测试完成！共进行 %d 次测试，耗时 %d ms\n", times, endTime - startTime);
        System.out.println("所有测试通过，两种方法结果一致！");
        
        // 性能对比测试
        System.out.println("\n=== 性能对比测试 ===");
        int[][] bigMatrix = randomMatrix(8, 8, 20);
        
        startTime = System.currentTimeMillis();
        int result1 = max1(bigMatrix);
        long time1 = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int result2 = max2(bigMatrix);
        long time2 = System.currentTimeMillis() - startTime;
        
        System.out.printf("大矩阵测试（%dx%d）：\n", bigMatrix.length, bigMatrix[0].length);
        System.out.printf("递归方法: 结果=%d, 耗时=%dms\n", result1, time1);
        System.out.printf("DP方法: 结果=%d, 耗时=%dms\n", result2, time2);
        System.out.printf("DP方法比递归方法快 %.2f 倍\n", (double)time1 / time2);
    }
}
