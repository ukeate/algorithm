package base.dp;

/**
 * 机器人走路问题
 * 
 * 问题描述：
 * 假设有排成一行的N个位置，记为1~N，机器人初始在start位置，每次可以往左走或往右走，
 * 如果机器人来到1位置，那么下一步只能往右来到2位置；如果机器人来到N位置，那么下一步只能往左来到N-1位置。
 * 规定机器人必须走K步，最终能来到aim位置的方法数有多少种？
 * 
 * 给定四个参数n、start、aim、k，返回方法数。
 * 
 * 解法分析：
 * 1. 暴力递归：定义递归函数process(cur, rest, aim, n)，表示当前在cur位置，还剩rest步，目标是aim位置，总共有n个位置的方法数
 * 2. 记忆化搜索：使用二维数组缓存递归结果，避免重复计算
 * 3. 动态规划：bottom-up方式填充dp表，dp[cur][rest]表示从cur位置出发，走rest步到达aim位置的方法数
 * 
 * 时间复杂度：O(n*k)
 * 空间复杂度：O(n*k)
 */
public class RobotWalk {
    
    /**
     * 暴力递归解法
     * 
     * @param cur 当前位置
     * @param rest 剩余步数
     * @param aim 目标位置
     * @param n 位置总数[1-n]
     * @return 到达目标位置的方法数
     */
    private static int process1(int cur, int rest, int aim, int n) {
        // base case：没有剩余步数
        if (rest == 0) {
            return cur == aim ? 1 : 0;
        }
        
        // 在位置1，只能向右走
        if (cur == 1) {
            return process1(2, rest - 1, aim, n);
        }
        
        // 在位置n，只能向左走
        if (cur == n) {
            return process1(n - 1, rest - 1, aim, n);
        }
        
        // 在中间位置，可以向左或向右走
        return process1(cur - 1, rest - 1, aim, n) + process1(cur + 1, rest - 1, aim, n);
    }

    /**
     * 暴力递归解法入口
     * 
     * @param n 位置总数
     * @param start 起始位置
     * @param aim 目标位置
     * @param k 必须走的步数
     * @return 方法数，参数不合法返回-1
     */
    public static int ways1(int n, int start, int aim, int k) {
        if (n < 2 || start < 1 || start > n || aim < 1 || aim > n || k < 1) {
            return -1;
        }
        return process1(start, k, aim, n);
    }

    /**
     * 记忆化搜索解法
     * 
     * @param cur 当前位置
     * @param rest 剩余步数
     * @param aim 目标位置
     * @param n 位置总数
     * @param dp 缓存数组，dp[cur][rest]表示从cur位置走rest步到aim的方法数
     * @return 到达目标位置的方法数
     */
    private static int process2(int cur, int rest, int aim, int n, int[][] dp) {
        // 如果已经计算过，直接返回缓存结果
        if (dp[cur][rest] != -1) {
            return dp[cur][rest];
        }
        
        int ans = 0;
        
        // base case：没有剩余步数
        if (rest == 0) {
            ans = cur == aim ? 1 : 0;
        } else if (cur == 1) {
            // 在位置1，只能向右走
            ans = process2(2, rest - 1, aim, n, dp);
        } else if (cur == n) {
            // 在位置n，只能向左走
            ans = process2(n - 1, rest - 1, aim, n, dp);
        } else {
            // 在中间位置，可以向左或向右走
            ans = process2(cur - 1, rest - 1, aim, n, dp) + process2(cur + 1, rest - 1, aim, n, dp);
        }
        
        // 缓存结果
        dp[cur][rest] = ans;
        return ans;
    }

    /**
     * 记忆化搜索解法入口
     * 
     * @param n 位置总数
     * @param start 起始位置
     * @param aim 目标位置
     * @param k 必须走的步数
     * @return 方法数，参数不合法返回-1
     */
    public static int ways2(int n, int start, int aim, int k) {
        if (n < 2 || start < 1 || start > n || aim < 1 || aim > n || k < 1) {
            return -1;
        }
        
        // 初始化dp数组，-1表示未计算过
        int[][] dp = new int[n + 1][k + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= k; j++) {
                dp[i][j] = -1;
            }
        }
        return process2(start, k, aim, n, dp);
    }

    /**
     * 动态规划解法（Bottom-Up）
     * 
     * dp[cur][rest]表示从cur位置出发，走rest步到达aim位置的方法数
     * 
     * @param n 位置总数
     * @param start 起始位置
     * @param aim 目标位置
     * @param k 必须走的步数
     * @return 方法数，参数不合法返回-1
     */
    public static int ways3(int n, int start, int aim, int k) {
        if (n < 2 || start < 1 || start > n || aim < 1 || aim > n || k < 1) {
            return -1;
        }
        
        // dp[cur][rest]表示从cur位置走rest步到aim的方法数
        int[][] dp = new int[n + 1][k + 1];
        
        // base case：当剩余0步时，只有当前位置等于目标位置才有1种方法
        dp[aim][0] = 1;
        
        // 填充dp表，从步数1开始到k
        for (int rest = 1; rest <= k; rest++) {
            // 位置1只能向右走
            dp[1][rest] = dp[2][rest - 1];
            
            // 中间位置可以向左或向右走
            for (int cur = 2; cur < n; cur++) {
                dp[cur][rest] = dp[cur - 1][rest - 1] + dp[cur + 1][rest - 1];
            }
            
            // 位置n只能向左走
            dp[n][rest] = dp[n - 1][rest - 1];
        }
        
        return dp[start][k];
    }

    public static void main(String[] args) {
        System.out.println(ways1(5, 2, 4, 6));
        System.out.println(ways2(5, 2, 4, 6));
        System.out.println(ways3(5, 2, 4, 6));
    }
}
