package basic.c31;

/**
 * 边框全1的最大正方形问题
 * 
 * 问题描述：给定一个由0和1组成的二维矩阵，找到边框全为1的最大正方形的边长
 * 
 * 解题思路：
 * 1. 预处理：对每个位置计算向右和向下连续1的个数
 * 2. 枚举：从大到小枚举可能的正方形边长
 * 3. 检验：对每个可能的左上角位置，检查是否能构成指定边长的正方形
 * 4. 验证：检查正方形的四条边是否都有足够长度的连续1
 * 
 * 时间复杂度：O(N*M*min(N,M))，其中N和M分别是矩阵的行数和列数
 * 空间复杂度：O(N*M)，用于存储预处理的辅助数组
 */
public class MaxOneBorderSize {
    
    /**
     * 预处理矩阵，计算每个位置向右和向下连续1的个数
     * @param m 原始矩阵
     * @param right right[i][j]表示从位置(i,j)开始向右连续1的个数
     * @param down down[i][j]表示从位置(i,j)开始向下连续1的个数
     */
    private static void setBorderMap(int[][] m, int[][] right, int[][] down) {
        int r = m.length;
        int c = m[0].length;
        
        // 处理右下角位置
        if (m[r - 1][c - 1] == 1) {
            right[r - 1][c - 1] = 1;
            down[r - 1][c - 1] = 1;
        }
        
        // 处理最右列（从下往上）
        for (int i = r - 2; i >= 0; i--) {
            if (m[i][c - 1] == 1) {
                right[i][c - 1] = 1; // 最右列向右只能是1
                down[i][c - 1] = down[i + 1][c - 1] + 1; // 累积向下的长度
            }
        }
        
        // 处理最下行（从右往左）
        for (int i = c - 2; i >= 0; i--) {
            if (m[r - 1][i] == 1) {
                right[r - 1][i] = right[r - 1][i + 1] + 1; // 累积向右的长度
                down[r - 1][i] = 1; // 最下行向下只能是1
            }
        }
        
        // 处理其余位置（从右下往左上）
        for (int i = r - 2; i >= 0; i--) {
            for (int j = c - 2; j >= 0; j--) {
                if (m[i][j] == 1) {
                    right[i][j] = right[i][j + 1] + 1; // 当前位置向右的长度
                    down[i][j] = down[i + 1][j] + 1;   // 当前位置向下的长度
                }
            }
        }
    }

    /**
     * 检查是否存在指定边长的正方形
     * @param size 正方形边长
     * @param right 向右连续1的个数矩阵
     * @param down 向下连续1的个数矩阵
     * @return 是否存在指定边长的正方形
     */
    private static boolean hasSize(int size, int[][] right, int[][] down) {
        // 遍历所有可能的左上角位置
        for (int i = 0; i < right.length - size + 1; i++) {
            for (int j = 0; j < right[0].length - size + 1; j++) {
                // 检查正方形的四个关键位置是否都有足够的连续1
                if (right[i][j] >= size &&              // 左上角向右
                    down[i][j] >= size &&               // 左上角向下
                    right[i + size - 1][j] >= size &&  // 左下角向右
                    down[i][j + size - 1] >= size) {    // 右上角向下
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 求边框全1的最大正方形边长
     * @param m 由0和1组成的二维矩阵
     * @return 最大正方形的边长
     */
    public static int max(int[][] m) {
        // 创建辅助数组
        int[][] right = new int[m.length][m[0].length];
        int[][] down = new int[m.length][m[0].length];
        
        // 预处理矩阵
        setBorderMap(m, right, down);
        
        // 从最大可能的边长开始检查
        for (int size = Math.min(m.length, m[0].length); size > 0; size--) {
            if (hasSize(size, right, down)) {
                return size;
            }
        }
        return 0;
    }

    private static int[][] randomMatrix(int rowSize, int colSize) {
        int[][] res = new int[rowSize][colSize];
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                res[i][j] = (int) (Math.random() * 2);
            }
        }
        return res;
    }

    private static void print(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][] matrix = randomMatrix(7, 8);
        print(matrix);
        System.out.println(max(matrix));
    }

}
