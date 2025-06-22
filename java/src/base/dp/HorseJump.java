package base.dp;

/**
 * 马走日问题
 * 
 * 问题描述：
 * 中国象棋中的马按照"日"字形走法，在10×9的棋盘上，给定起始位置(0,0)，
 * 目标位置(a,b)，以及必须走的步数k。求有多少种方法能在恰好k步内从起始位置到达目标位置。
 * 
 * 棋盘坐标范围：x∈[0,9]，y∈[0,8]
 * 马的8种走法：(±2,±1)和(±1,±2)的组合
 * 
 * 解法分析：
 * 1. 暴力递归：从目标位置反向思考，递归尝试马的8种走法
 * 2. 动态规划：使用三维数组dp[x][y][rest]表示从位置(x,y)出发，走rest步到达目标位置的方法数
 * 
 * 时间复杂度：O(10 * 9 * k)
 * 空间复杂度：O(10 * 9 * k)
 */
public class HorseJump {
    
    /**
     * 暴力递归解法
     * 
     * @param x 当前x坐标
     * @param y 当前y坐标
     * @param rest 剩余步数
     * @param a 目标x坐标
     * @param b 目标y坐标
     * @return 从(x,y)走rest步到(a,b)的方法数
     */
    private static int process(int x, int y, int rest, int a, int b) {
        // 如果越界，返回0种方法
        if (x < 0 || x > 9 || y < 0 || y > 8) {
            return 0;
        }
        
        // base case：没有剩余步数
        if (rest == 0) {
            return (x == a && y == b) ? 1 : 0;
        }
        
        // 马的8种走法：日字形移动
        int ways = process(x + 2, y + 1, rest - 1, a, b);  // 右下方向1
        ways += process(x + 2, y - 1, rest - 1, a, b);     // 右上方向1
        ways += process(x - 2, y + 1, rest - 1, a, b);     // 左下方向1
        ways += process(x - 2, y - 1, rest - 1, a, b);     // 左上方向1
        ways += process(x + 1, y + 2, rest - 1, a, b);     // 下右方向2
        ways += process(x + 1, y - 2, rest - 1, a, b);     // 上右方向2
        ways += process(x - 1, y + 2, rest - 1, a, b);     // 下左方向2
        ways += process(x - 1, y - 2, rest - 1, a, b);     // 上左方向2
        
        return ways;
    }

    /**
     * 暴力递归解法入口
     * 
     * @param a 目标x坐标
     * @param b 目标y坐标
     * @param k 必须走的步数
     * @return 从(0,0)走k步到(a,b)的方法数
     */
    public static int ways(int a, int b, int k) {
        return process(0, 0, k, a, b);
    }

    /**
     * 边界检查辅助函数
     * 
     * @param dp 三维dp数组
     * @param x x坐标
     * @param y y坐标
     * @param rest 剩余步数
     * @return 如果坐标合法返回dp值，否则返回0
     */
    private static int pick(int[][][] dp, int x, int y, int rest) {
        if (x < 0 || x > 9 || y < 0 || y > 8) {
            return 0;
        }
        return dp[x][y][rest];
    }
    
    /**
     * 动态规划解法
     * 
     * dp[x][y][rest]表示从位置(x,y)出发，走rest步到达目标位置(a,b)的方法数
     * 
     * @param a 目标x坐标
     * @param b 目标y坐标
     * @param k 必须走的步数
     * @return 从(0,0)走k步到(a,b)的方法数
     */
    public static int dp(int a, int b, int k) {
        // dp[x][y][rest]表示从(x,y)走rest步到(a,b)的方法数
        int[][][] dp = new int[10][9][k + 1];
        
        // base case：当步数为0时，只有目标位置的方法数为1
        dp[a][b][0] = 1;
        
        // 从步数1开始填充dp表
        for (int rest = 1; rest <= k; rest++) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 9; y++) {
                    // 马的8种走法，累加所有可能的前一步位置的方法数
                    int ways = pick(dp, x + 2, y + 1, rest - 1);
                    ways += pick(dp, x + 2, y - 1, rest - 1);
                    ways += pick(dp, x - 2, y + 1, rest - 1);
                    ways += pick(dp, x - 2, y - 1, rest - 1);
                    ways += pick(dp, x + 1, y + 2, rest - 1);
                    ways += pick(dp, x + 1, y - 2, rest - 1);
                    ways += pick(dp, x - 1, y + 2, rest - 1);
                    ways += pick(dp, x - 1, y - 2, rest - 1);
                    
                    dp[x][y][rest] = ways;
                }
            }
        }
        
        return dp[0][0][k];
    }

    public static void main(String[] args) {
        int x = 7;
        int y = 7;
        int step = 10;
        System.out.println(ways(x, y, step));
        System.out.println(dp(x, y, step));
    }
}
