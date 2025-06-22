package base.dp5_cmpr;

/**
 * "我能赢吗"博弈问题
 * 两个玩家轮流从1到choose中选择数字，选过的数字不能再选
 * 先让累计和达到或超过total的玩家获胜
 * 判断先手是否有必胜策略
 * 使用状态压缩DP优化，用位运算表示数字的使用状态
 */
public class CanIWin {
    /**
     * 递归方法1 - 使用数组表示状态
     * @param arr 可选数字数组，-1表示已被选择
     * @param rest 剩余需要达到的目标值
     * @return 当前玩家是否能获胜
     */
    private static boolean process1(int[] arr, int rest) {
        // 目标已达到，当前玩家败北（因为是对手达到的）
        if (rest <= 0) {
            return false;
        }
        // 尝试选择每个可用的数字
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != -1) {
                int cur = arr[i];
                arr[i] = -1; // 标记为已选择
                boolean next = process1(arr, rest - cur);
                arr[i] = cur; // 恢复状态
                // 如果对手在接下来的状态中败北，当前玩家获胜
                if (!next) {
                    return true;
                }
            }
        }
        return false; // 所有选择都让对手获胜，当前玩家败北
    }

    /**
     * 方法1的对外接口
     */
    public static boolean canIWin0(int choose, int total) {
        if (total == 0) {
            return true;
        }
        // 如果所有数字的和都小于目标，无人能获胜
        if ((choose * (choose + 1) >> 1) < total) {
            return false;
        }
        int[] arr = new int[choose];
        for (int i = 0; i < choose; i++) {
            arr[i] = i + 1;
        }
        return process1(arr, total);
    }

    //

    /**
     * 递归方法2 - 使用位运算表示状态
     * @param choose 可选择的最大数字
     * @param status 位状态，第i位为1表示数字i已被选择
     * @param rest 剩余需要达到的目标值
     * @return 当前玩家是否能获胜
     */
    private static boolean process1(int choose, int status, int rest) {
        // 目标已达到，当前玩家败北
        if (rest <= 0) {
            return false;
        }
        // 尝试选择每个未被选择的数字
        for (int i = 1; i <= choose; i++) {
            if (((1 << i) & status) == 0) { // 数字i未被选择
                // 选择数字i，更新状态
                if (!process1(choose, (status | (1 << i)), rest - i)) {
                    return true; // 对手败北，当前玩家获胜
                }
            }
        }
        return false;
    }

    /**
     * 方法2的对外接口 - 使用位运算优化
     */
    public static boolean  canIWin1(int choose, int total) {
        if (total == 0) {
            return true;
        }
        if ((choose * (choose + 1) >> 1) < total) {
            return false;
        }
        return process1(choose, 0, total);
    }

    //

    /**
     * 递归方法3 - 带记忆化的状态压缩DP
     * @param choose 可选择的最大数字
     * @param status 位状态
     * @param rest 剩余目标值
     * @param dp 记忆化数组，1表示能赢，-1表示会输，0表示未计算
     * @return 当前玩家是否能获胜
     */
    private static boolean process2(int choose, int status, int rest, int[] dp) {
        // 查询记忆化结果
        if (dp[status] != 0) {
            return dp[status] == 1 ? true : false;
        }
        boolean ans = false;
        if (rest > 0) {
            // 尝试每个可选的数字
            for (int i = 1; i <= choose; i++) {
                if (((1 << i) & status) == 0) {
                    // 如果选择数字i后对手败北，当前玩家获胜
                    if (!process2(choose, (status | (1 << i)), rest - i, dp)) {
                        ans = true;
                        break;
                    }
                }
            }
        }
        // 记忆化存储结果
        dp[status] = ans ? 1 : -1;
        return ans;
    }

    /**
     * 方法3：状态压缩DP with 记忆化
     * rest由status决定，所以dp参数不用rest
     */
    public static boolean canIWin2(int choose, int total) {
        if (total == 0) {
            return true;
        }
        if ((choose * (choose + 1) >> 1) < total) {
            return false;
        }
        // dp数组大小为2^(choose+1)，对应所有可能的状态
        int[] dp = new int[1 << (choose + 1)];
        return process2(choose, 0, total, dp);
    }

}
