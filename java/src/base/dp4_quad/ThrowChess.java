package base.dp4_quad;

// https://leetcode.com/problems/super-egg-drop
/**
 * 扔鸡蛋问题 (Super Egg Drop)
 * 有chess个鸡蛋和level层楼，需要找出鸡蛋会摔碎的最低楼层
 * 求在最坏情况下，至少需要扔几次才能确定答案
 * 
 * 这是一个经典的动态规划问题，也是四边形不等式优化的应用
 * 提供多种解法：
 * 1. 递归解法 O(chess * level²)
 * 2. 二维DP O(chess * level²)
 * 3. 空间优化DP O(chess * level²)
 * 4. 四边形不等式优化 O(chess * level)
 * 5. 逆向思维DP O(chess * T)
 * 6. 二分搜索优化 O(chess * T)
 */
public class ThrowChess {
    /**
     * 递归方法：计算在最坏情况下至少需要扔几次
     * @param rest 剩余楼层数
     * @param k 剩余鸡蛋数
     * @return 最少扔的次数
     */
    private static int process1(int rest, int k) {
        // 没有楼层了，不需要扔
        if (rest == 0) {
            return 0;
        }
        // 只有一个鸡蛋，只能从下往上逐层试
        if (k == 1) {
            return rest;
        }
        
        int min = Integer.MAX_VALUE;
        // 枚举在第i层扔的情况
        for (int i = 1; i <= rest; i++) {
            // 碎了：往下找，鸡蛋数-1，楼层范围是[1, i-1]
            // 没碎：往上找，鸡蛋数不变，楼层范围是[i+1, rest]，即rest-i层
            min = Math.min(min, Math.max(process1(i - 1, k - 1), process1(rest - i, k)));
        }
        return min + 1; // 加上当前这一次扔的次数
    }

    /**
     * 方法1：递归解法
     */
    public static int times1(int chess, int level) {
        if (chess < 1 || level < 1) {
            return 0;
        }
        return process1(level, chess);
    }

    //

    /**
     * 方法2：标准的二维动态规划
     * dp[i][j]表示有i层楼、j个鸡蛋时的最少扔次数
     * 时间复杂度O(chess * level²)
     */
    public static int times2(int chess, int level) {
        if (chess < 1 || level < 1) {
            return 0;
        }
        if (chess == 1) {
            return level;
        }
        
        int[][] dp = new int[level + 1][chess + 1];
        
        // 基础条件：只有一个鸡蛋时，需要从1层开始逐层试
        for (int i = 1; i < dp.length; i++) {
            dp[i][1] = i;
        }
        
        // 填表
        for (int i = 1; i < dp.length; i++) {
            for (int j = 2; j < dp[0].length; j++) {
                int min = Integer.MAX_VALUE;
                // 枚举在第k层扔的情况
                for (int k = 1; k <= i; k++) {
                    min = Math.min(min, Math.max(dp[k - 1][j - 1], dp[i - k][j]));
                }
                dp[i][j] = min + 1;
            }
        }
        return dp[level][chess];
    }

    //

    /**
     * 方法3：空间优化的动态规划
     * 使用滚动数组优化空间复杂度
     * 空间复杂度从O(chess * level)降到O(level)
     */
    public static int times22(int chess, int level) {
        if (chess < 1 || level < 1) {
            return 0;
        }
        if (chess == 1) {
            return level;
        }
        
        int[] preArr = new int [level + 1]; // 前一个鸡蛋数对应的dp数组
        int[] curArr = new int [level + 1]; // 当前鸡蛋数对应的dp数组
        
        // 初始化：1个鸡蛋的情况
        for (int i = 1; i < curArr.length; i++) {
            curArr[i] = i;
        }
        
        // 逐个鸡蛋数进行计算
        for (int i = 1; i < chess; i++) {
            // 交换数组
            int[] tmp = preArr;
            preArr = curArr;
            curArr = tmp;
            
            for (int j = 1; j < curArr.length; j++) {
                int min = Integer.MAX_VALUE;
                for (int k = 1; k <= j; k++) {
                    min = Math.min(min, Math.max(preArr[k - 1], curArr[j - k]));
                }
                curArr[j] = min + 1;
            }
        }
        return curArr[level];
    }

    //

    /**
     * 方法4：四边形不等式优化的动态规划
     * 利用最优决策点的单调性，时间复杂度优化到O(chess * level)
     */
    public static int times3(int chess, int level) {
        if (chess < 1 || level < 1) {
            return 0;
        }
        if (chess == 1) {
            return level;
        }
        
        int[][] dp = new int[level + 1][chess + 1];
        int[][] best = new int[level + 1][chess + 1]; // 记录最优决策点
        
        // 基础条件：1个鸡蛋
        for (int i = 1; i < dp.length; i++) {
            dp[i][1] = i;
        }
        
        // 基础条件：1层楼
        for (int i = 1; i < dp[0].length; i++) {
            dp[1][i] = 1;
            best[1][i] = 1;
        }
        
        // 利用四边形不等式优化
        for (int i = 2; i <= level; i++) {
            for (int j = chess; j > 1; j--) {
                int ans = Integer.MAX_VALUE;
                int bestChoose = -1;
                
                // 利用单调性缩小搜索范围
                int down = best[i - 1][j];
                int up = j == chess ? i : best[i][j + 1];
                
                for (int first = down; first <= up; first++) {
                    int cur = Math.max(dp[first - 1][j - 1], dp[i - first][j]);
                    if (cur <= ans) {
                        ans = cur;
                        bestChoose = first;
                    }
                }
                dp[i][j] = ans + 1;
                best[i][j] = bestChoose;
            }
        }
        return dp[level][chess];
    }

    //

    /**
     * 方法5：逆向思维的动态规划
     * 不问"level层楼chess个鸡蛋需要几次"，而是问"T次机会chess个鸡蛋最多能确定多少层"
     * 时间复杂度O(chess * T)，其中T是答案
     */
    public static int times4(int chess, int level) {
        if (chess < 1 || level < 1) {
            return 0;
        }
        
        // dp[i]表示有i+1个鸡蛋时，当前扔的次数下最多能确定的楼层数
        int[] dp = new int[chess];
        int ans = 0;
        
        while (true) {
            ans++; // 扔的次数+1
            int pre = 0;
            
            for (int i = 0; i < dp.length; i++) {
                int tmp = dp[i];
                // 状态转移：dp[i] = dp[i] + pre + 1
                // dp[i]表示鸡蛋没碎时往上能确定的楼层数
                // pre表示鸡蛋碎了时往下能确定的楼层数
                // +1表示当前扔的这一层
                dp[i] = dp[i] + pre + 1;
                pre = tmp;
                
                if (dp[i] >= level) {
                    return ans;
                }
            }
        }
    }

    //

    /**
     * 计算log2(n)
     */
    private static int log2N(int n) {
        int res = -1;
        while (n != 0) {
            res++;
            n >>>= 1;
        }
        return res;
    }

    /**
     * 方法6：二分搜索优化
     * 当鸡蛋数足够多时，可以使用二分搜索，时间复杂度为O(log level)
     * 否则使用逆向DP
     */
    public static int times5(int chess, int level) {
        if (chess < 1 || level < 1) {
            return 0;
        }
        
        // 计算二分搜索需要的次数
        int bsTimes = log2N(level) + 1;
        
        // 如果鸡蛋数足够，直接用二分搜索
        if (chess >= bsTimes) {
            return bsTimes;
        }
        
        // 否则使用逆向DP
        int[] dp = new int[chess];
        int ans = 0;
        while (true) {
            ans++;
            int pre = 0;
            for (int i = 0; i < dp.length; i++) {
                int tmp = dp[i];
                dp[i] = dp[i] + pre + 1;
                pre = tmp;
                if (dp[i] >= level) {
                    return ans;
                }
            }
        }
    }

    /**
     * 测试方法，验证各个算法的正确性
     */
    public static void main(String[] args) {
        int times = 10;
        int maxLevel = 30;
        int maxChess = 10;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int level = (int) ((maxLevel + 1) * Math.random()) + 1;
            int chess = (int) ((maxChess + 1) * Math.random()) + 1;
//            int ans1 = times1(chess, level);
            int ans2 = times2(chess, level);
            int ans22 = times22(chess, level);
            int ans3 = times3(chess, level);
            int ans4 = times4(chess, level);
            int ans5 = times5(chess, level);
            if (ans2 != ans3 || ans4 != ans5 || ans2 != ans4 || ans2 != ans22) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
