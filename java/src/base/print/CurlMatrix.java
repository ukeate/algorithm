package base.print;

/**
 * 螺旋矩阵打印 - 顺时针螺旋填充矩阵
 * 
 * 问题描述：
 * 在n×n的矩阵中，按照顺时针螺旋的方式用'*'填充边界
 * 形成多个同心的矩形框
 * 
 * 算法思路：
 * 1. 从最外层开始，逐层向内填充
 * 2. 每一层按照：上→右→下→左的顺序填充
 * 3. 每填充完一层，边界向内缩小2个单位
 * 
 * 应用场景：
 * - 螺旋矩阵的生成和遍历
 * - 图形图案的程序化绘制
 * - 矩阵边界处理算法的练习
 */
public class CurlMatrix {
    /**
     * 填充单层螺旋边界
     * 
     * 填充顺序：
     * 1. 上边：从左到右填充第leftUp行
     * 2. 右边：从上到下填充第rightDown列（跳过已填充的第一个）
     * 3. 下边：从右到左填充第rightDown行（跳过已填充的最后一个）
     * 4. 左边：从下到上填充第leftUp+1列（跳过已填充的第一个和最后一个）
     * 
     * @param m 矩阵
     * @param leftUp 当前层的左上角坐标
     * @param rightDown 当前层的右下角坐标
     */
    private static void set(char[][] m, int leftUp, int rightDown) {
        // 填充上边：从左到右
        for (int col = leftUp; col <= rightDown; col++) {
            m[leftUp][col] = '*';
        }
        
        // 填充右边：从上到下（跳过第一个，避免重复）
        for (int row = leftUp + 1; row <= rightDown; row++) {
            m[row][rightDown] = '*';
        }
        
        // 填充下边：从右到左（跳过最后一个，避免重复）
        for (int col = rightDown - 1; col >= leftUp + 1; col--) {
            m[rightDown][col] = '*';
        }
        
        // 填充左边：从下到上（跳过第一个和最后一个，避免重复）
        for (int row = rightDown - 1; row >= leftUp + 2; row--) {
            m[row][leftUp + 1] = '*';
        }
    }

    /**
     * 生成并打印螺旋矩阵
     * 
     * 算法步骤：
     * 1. 初始化n×n矩阵，所有位置填充空格
     * 2. 从最外层开始，逐层向内填充边界
     * 3. 每填充一层，边界向内收缩2个单位
     * 4. 直到没有更多层可以填充
     * 
     * @param n 矩阵大小
     */
    public static void curl(int n) {
        int leftUp = 0;         // 当前层左上角坐标
        int rightDown = n - 1;  // 当前层右下角坐标
        char[][] m = new char[n][n];
        
        // 初始化矩阵，所有位置填充空格
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = ' ';
            }
        }
        
        // 逐层填充螺旋边界
        while (leftUp <= rightDown) {
            set(m, leftUp, rightDown);
            leftUp += 2;        // 左上角向右下移动2个单位
            rightDown -= 2;     // 右下角向左上移动2个单位
        }
        
        // 打印结果矩阵
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * 测试方法 - 生成10×10的螺旋矩阵
     */
    public static void main(String[] args) {
        curl(10);
    }
}
