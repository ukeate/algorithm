package base.dp2;

/**
 * 换钱的方法数（不限使用张数）
 * 
 * 问题描述：
 * 给定数组arr，arr中所有的值都为正数且不重复。每个值代表一种面值的货币，
 * 每种面值的货币可以使用任意张，再给定一个正数aim，代表要找的钱数。
 * 求组成aim的方法数。
 * 
 * 例如：arr = [5,10,25,1]，aim = 0，返回1（什么都不选）
 *      arr = [5,10,25,1]，aim = 15，返回6
 *      因为：3张5可以组成15、1张10+1张5可以组成15、1张10+5张1可以组成15、
 *           1张5+10张1可以组成15、15张1可以组成15、0张5+1张10+0张25+5张1可以组成15
 * 
 * 解法分析：
 * 1. 递归思路：对于每种货币，决定要使用多少张（0张、1张、2张...）
 * 2. 状态定义：dp[i][j]表示使用arr[0...i]范围内的货币组成金额j的方法数
 * 3. 状态转移：dp[i][j] = dp[i+1][j] + dp[i+1][j-arr[i]]（如果j>=arr[i]）
 * 
 * 时间复杂度：O(n * aim)
 * 空间复杂度：O(n * aim)
 */
public class CoinsWay {
    
    /**
     * 暴力递归解法
     * 
     * @param arr 货币面值数组
     * @param idx 当前考虑的货币索引
     * @param rest 剩余需要组成的金额
     * @return 组成rest金额的方法数
     */
    private static int process(int[] arr, int idx, int rest) {
        // 如果剩余金额为负数，无法组成
        if (rest < 0) {
            return 0;
        }
        
        // base case：所有货币都考虑完了
        if (idx == arr.length) {
            return rest == 0 ? 1 : 0;  // 剩余金额为0才算一种有效方法
        }
        
        // 两种选择：
        // 1. 不使用当前货币
        // 2. 使用当前货币（由于可以无限使用，所以递归时idx不变）
        return process(arr, idx + 1, rest) + process(arr, idx + 1, rest - arr[idx]);
    }

    /**
     * 暴力递归解法入口
     * 
     * @param arr 货币面值数组
     * @param aim 目标金额
     * @return 组成目标金额的方法数
     */
    public static int ways(int[] arr, int aim) {
        return process(arr, 0, aim);
    }

    /**
     * 动态规划解法
     * 
     * dp[idx][rest]表示使用arr[idx...]货币组成rest金额的方法数
     * 
     * 状态转移分析：
     * - 不使用arr[idx]：dp[idx+1][rest]
     * - 使用arr[idx]：dp[idx+1][rest-arr[idx]]（注意这里是idx+1，不是idx）
     * 
     * 注意：这个版本的递归逻辑是每个货币只能使用一次，如果要实现无限使用，
     *      应该在使用货币时保持idx不变，即：dp[idx][rest-arr[idx]]
     * 
     * @param arr 货币面值数组
     * @param aim 目标金额
     * @return 组成目标金额的方法数
     */
    public static int dp(int[] arr, int aim) {
        if (aim == 0) {
            return 1;
        }
        
        int n = arr.length;
        // dp[idx][rest]表示使用arr[idx...]货币组成rest金额的方法数
        int[][] dp = new int[n + 1][aim + 1];
        
        // base case：所有货币用完后，只有金额为0时才有1种方法
        dp[n][0] = 1;
        
        // 从后往前填充dp表
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= aim; rest++) {
                // 不使用当前货币
                dp[idx][rest] = dp[idx + 1][rest];
                
                // 使用当前货币（如果金额足够）
                if (rest - arr[idx] >= 0) {
                    dp[idx][rest] += dp[idx + 1][rest - arr[idx]];
                }
            }
        }
        
        return dp[0][aim];
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param maxLen 最大长度
     * @param maxVal 最大值
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] res = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < res.length; i++) {
            res[i] = (int) ((maxVal + 1) * Math.random()) + 1;
        }
        return res;
    }

    /**
     * 打印数组
     * 
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 20;
        int maxVal = 30;
        System.out.println("test begin");
        
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int aim = (int) ((maxVal + 1) * Math.random());
            int ans1 = ways(arr, aim);
            int ans2 = dp(arr, aim);
            
            if (ans1 != ans2) {
                System.out.println("Wrong");
                print(arr);
                System.out.println(aim + "|" + ans1 + "|" + ans2);
            }
        }
        
        System.out.println("test end");
    }
}
