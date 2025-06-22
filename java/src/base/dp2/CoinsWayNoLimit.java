package base.dp2;

/**
 * 无限数量硬币组合问题
 * 
 * 问题描述：
 * 给定一个唯一硬币面值数组和一个目标金额，
 * 找出组成目标金额的方法数。每种硬币类型可以使用无限次
 * （但每个硬币面值在数组中只出现一次）。
 * 
 * 解法分析：
 * 1. 暴力递归：尝试每种硬币类型的所有可能数量
 * 2. 标准DP：使用2D DP表，内部枚举硬币数量
 * 3. 优化DP：使用递推关系避免内层循环
 * 
 * 时间复杂度：
 * - 递归：指数级
 * - DP1：O(n * 金额 * 金额) - 有内层枚举循环
 * - DP2：O(n * 金额) - 使用递推关系优化
 * 
 * 空间复杂度：O(n * 金额)
 * 
 * @author Algorithm Practice
 */
// 不重复，无限张, 求方法数
public class CoinsWayNoLimit {

    /**
     * 递归方法计算组成目标金额的方法数
     * 
     * @param arr 唯一硬币面值数组
     * @param idx 当前考虑的硬币索引
     * @param rest 剩余需要组成的金额
     * @return 组成剩余金额的方法数
     */
    private static int process(int[] arr, int idx, int rest) {
        // base case：处理完所有硬币类型
        if (idx == arr.length) {
            return rest == 0 ? 1 : 0;  // 恰好组成目标金额则为1种方法，否则为0
        }
        
        int ways = 0;
        // 尝试使用0、1、2、...个当前类型的硬币（无限制）
        for (int num = 0; num * arr[idx] <= rest; num++) {
            ways += process(arr, idx + 1, rest - (num * arr[idx]));
        }
        return ways;
    }

    /**
     * 使用递归方法计算组成目标金额的方法数
     * 
     * @param arr 唯一硬币面值数组
     * @param aim 目标金额
     * @return 组成目标金额的方法数
     */
    public static int ways(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }
        return process(arr, 0, aim);
    }

    /**
     * 动态规划解法 - 带枚举的标准方法
     * 
     * @param arr 唯一硬币面值数组
     * @param aim 目标金额
     * @return 组成目标金额的方法数
     */
    public static int dp1(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }
        int n = arr.length;
        
        // dp[i][j] = 使用索引i及之后的硬币组成金额j的方法数
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 1;  // base case：组成0金额有1种方法
        
        // 自底向上填充DP表
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= aim; rest++) {
                int ways = 0;
                // 尝试使用0、1、2、...个当前类型的硬币
                for (int num = 0; num * arr[idx] <= rest; num++) {
                    ways += dp[idx + 1][rest - num * arr[idx]];
                }
                dp[idx][rest] = ways;
            }
        }
        return dp[0][aim];
    }

    /**
     * 使用递推关系的优化动态规划解法
     * 
     * 关键思路：dp[i][j] = dp[i+1][j] + dp[i][j-arr[i]]
     * - dp[i+1][j]：不使用当前硬币的方法数
     * - dp[i][j-arr[i]]：使用至少一个当前硬币的方法数
     * 
     * 这消除了内层枚举循环，降低了时间复杂度。
     * 
     * @param arr 唯一硬币面值数组
     * @param aim 目标金额
     * @return 组成目标金额的方法数
     */
    public static int dp2(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }
        int n = arr.length;
        
        // dp[i][j] = 使用索引i及之后的硬币组成金额j的方法数
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 1;  // base case：组成0金额有1种方法
        
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= aim; rest++) {
                // 不使用当前硬币类型的方法数
                dp[idx][rest] = dp[idx + 1][rest];
                
                // 加上使用至少一个当前硬币的方法数（如果可能）
                if (rest - arr[idx] >= 0) {
                    dp[idx][rest] += dp[idx][rest - arr[idx]];
                }
            }
        }
        return dp[0][aim];
    }

    /**
     * 生成唯一硬币面值的随机数组用于测试
     */
    public static int[] randomArr(int maxLen, int maxVal) {
        int n = (int) ((maxLen + 1) * Math.random());
        int[] arr = new int[n];
        boolean[] has = new boolean[maxVal + 2];  // 跟踪已使用的值以确保唯一性
        
        for (int i = 0; i < n; i++) {
            do {
                arr[i] = (int) ((maxVal + 1) * Math.random()) + 1;
            } while (has[arr[i]]);  // 确保无重复
            has[arr[i]] = true;
        }
        return arr;
    }

    /**
     * 打印数组用于调试
     */
    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    /**
     * 测试方法验证所有方法给出相同结果
     */
    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 10;
        int maxVal = 30;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int aim = (int) ((maxVal + 1) * Math.random());
            int ans1 = ways(arr, aim);
            int ans2 = dp1(arr, aim);
            int ans3 = dp2(arr, aim);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
                print(arr);
                System.out.println(aim + "|" + ans1 + "|" + ans2);
                break;
            }
        }
        System.out.println("test end");
    }
}
