package base.dp3_knap;

// 怪兽有能力和钱，给钱变成个人能力，能力高时直接通过,求最小钱数
/**
 * 怪兽金钱问题
 * 有一系列怪兽，每个怪兽有能力值d[i]和花钱价格p[i]
 * 可以选择花钱p[i]获得能力值d[i]，也可以不花钱直接挑战（如果当前能力值 >= 怪兽能力值）
 * 求通过所有怪兽所需的最小金钱
 */
public class MonsterMoney {
    /**
     * 递归方法1 - 暴力递归
     * @param d 怪兽能力数组
     * @param p 花费价格数组
     * @param ability 当前能力值
     * @param idx 当前处理的怪兽索引
     * @return 从idx开始通过所有怪兽的最小花费
     */
    private static long process1(int[] d, int[] p, int ability, int idx) {
        // 基础条件：所有怪兽都处理完了
        if (idx == d.length) {
            return 0;
        }
        // 如果当前能力值小于怪兽能力值，必须花钱
        if (ability < d[idx]) {
            return p[idx] + process1(d, p, ability + d[idx], idx + 1);
        } else {
            // 能力值够，可以选择花钱或不花钱，取最小值
            return Math.min(p[idx] + process1(d, p, ability + d[idx], idx + 1),
                    process1(d, p, ability, idx + 1));
        }
    }

    /**
     * 方法1的对外接口
     */
    public static long min1(int[] d, int[] p) {
        return process1(d, p, 0, 0);
    }

    //

    /**
     * 递归方法2 - 逆向思考，从后往前推算
     * @param d 怪兽能力数组
     * @param p 花费价格数组
     * @param idx 当前处理的怪兽索引（从后往前）
     * @param money 花费的总金钱
     * @return 花费money金钱能获得的最大能力值，-1表示无法达到
     */
    private static long process2(int[] d, int[] p, int idx, int money) {
        // 基础条件：没有更多怪兽了
        if (idx == -1) {
            return money == 0 ? 0 : -1;
        }

        // 不花这个怪兽的钱，看能得到什么能力
        long ability = process2(d, p, idx - 1, money);
        long p1 = -1;
        if (ability != -1 && ability >= d[idx]) {
            p1 = ability; // 不需要花钱就能通过
        }
        
        // 花这个怪兽的钱，看能得到什么能力
        long ability2 = process2(d, p, idx - 1, money - p[idx]);
        long p2 = -1;
        if (ability2 != -1) {
            p2 = d[idx] + ability2; // 花钱获得能力值
        }
        return Math.max(p1, p2);
    }

    /**
     * 方法2的对外接口 - 通过二分查找最小金钱
     */
    public static long min2(int[] d, int[] p) {
        int allMoney = 0;
        // 计算最大可能花费
        for (int i = 0; i < p.length; i++) {
            allMoney += p[i];
        }
        int n = d.length;
        // 从0开始尝试每个金钱数量
        for (int money = 0; money < allMoney; money++) {
            if (process2(d, p, n - 1, money) != -1) {
                return money;
            }
        }
        return allMoney;
    }

    //

    /**
     * 动态规划方法1 - 基于能力值的dp
     * dp[i][j]表示从第i个怪兽开始，当前能力值为j时的最小花费
     */
    public static long dp1(int[] d, int[] p) {
        int sum = 0;
        // 计算所有能力值的总和，作为dp表的上界
        for (int ability : d) {
            sum += ability;
        }
        long[][] dp = new long[d.length + 1][sum + 1];
        // 从后往前填表
        for (int cur = d.length - 1; cur >= 0; cur--) {
            for (int hp = 0; hp <= sum; hp++) {
                if (hp + d[cur] > sum) {
                    continue;
                }
                if (hp < d[cur]) {
                    // 必须花钱
                    dp[cur][hp] = p[cur] + dp[cur + 1][hp + d[cur]];
                } else {
                    // 可以选择花钱或不花钱
                    dp[cur][hp] = Math.min(p[cur] + dp[cur + 1][hp + d[cur]], dp[cur + 1][hp]);
                }
            }
        }
        return dp[0][0];
    }

    //

    /**
     * 动态规划方法2 - 基于金钱的dp
     * dp[i][j]表示处理完前i个怪兽，花费j金钱能获得的最大能力值
     */
    public static long dp2(int[] d, int[] p) {
        int sum = 0;
        // 计算所有价格的总和
        for (int money : p) {
            sum += money;
        }
        int[][] dp = new int[d.length][sum + 1];
        // 初始化为-1，表示无法达到
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j <= sum; j++) {
                dp[i][j] = -1;
            }
        }
        // 第一个怪兽花费p[0]金钱获得d[0]能力
        dp[0][p[0]] = d[0];
        
        // 填表
        for (int i = 1; i < d.length; i++) {
            for (int j = 0; j <= sum; j++) {
                // 花钱购买当前怪兽的能力
                if (j >= p[i] && dp[i - 1][j - p[i]] != -1) {
                    dp[i][j] = dp[i - 1][j - p[i]] + d[i];
                }
                // 不花钱，但能力值必须足够
                if (dp[i - 1][j] >= d[i]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j]);
                }
            }
        }
        
        // 找到最小花费
        int ans = 0;
        for (int j = 0; j <= sum; j++) {
            if (dp[d.length - 1][j] != -1) {
                ans = j;
                break;
            }
        }
        return ans;
    }

    //

    /**
     * 生成随机测试数组
     */
    public static int[][] randomTwoArr(int maxLen, int maxVal) {
        int size = (int) ((maxLen + 1) * Math.random()) + 1;
        int[][] res = new int[2][size];
        for (int i = 0; i < size; i++) {
            res[0][i] = (int) ((maxVal + 1) * Math.random()) + 1;
            res[1][i] = (int) ((maxVal + 1) * Math.random()) + 1;
        }
        return res;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        int times = 10000;
        int maxLen = 10;
        int maxVal = 20;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[][] twoArr = randomTwoArr(maxLen, maxVal);
            int[] d = twoArr[0];
            int[] p = twoArr[1];
            long ans1 = min1(d, p);
            long ans2 = min2(d, p);
            long ans3 = dp1(d, p);
            long ans4 = dp2(d, p);
            if (ans1 != ans2 || ans2 != ans3 || ans1 != ans4) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2 + "|" + ans3 + "|" + ans4);
                break;
            }
        }
        System.out.println("test end");
    }
}
