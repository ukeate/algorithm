package base.dp6_biz;

/**
 * 移除盒子问题
 * 有一排不同颜色的盒子，每次可以移除k个连续的相同颜色盒子，获得k²分
 * 求能获得的最大总分数
 * 
 * 这是一个复杂的区间DP问题，关键在于：
 * 1. 不一定要连续地移除盒子
 * 2. 可以先移除中间部分，让两端相同颜色的盒子连接
 * 3. 状态定义：dp[l][r][k] = 移除区间[l,r]的盒子，且在l前面有k个与boxes[l]相同颜色的盒子时的最大得分
 */
public class RemoveBoxes {
    /**
     * 方法1：基础递归解法
     * @param boxes 盒子颜色数组
     * @param l 左边界
     * @param r 右边界
     * @param k 左边界前面相同颜色盒子的个数
     * @param dp 记忆化数组
     * @return 最大得分
     */
    private static int process1(int[] boxes, int l, int r, int k, int[][][] dp) {
        if (l > r) {
            return 0;
        }
        if (dp[l][r][k] > 0) {
            return dp[l][r][k];
        }
        
        // 策略1：直接移除左边界及其相同颜色的盒子
        int ans = process1(boxes, l + 1, r, 0, dp) + (k + 1) * (k + 1);
        
        // 策略2：寻找后面相同颜色的盒子进行合并
        for (int i = l + 1; i <= r; i++) {
            if (boxes[i] == boxes[l]) {
                // 先移除中间部分[l+1, i-1]，然后处理[i, r]，此时i前面有k+1个相同颜色
                ans = Math.max(ans, 
                    process1(boxes, l + 1, i - 1, 0, dp) + 
                    process1(boxes, i, r, k + 1, dp));
            }
        }
        
        dp[l][r][k] = ans;
        return ans;
    }

    /**
     * 方法1的对外接口
     */
    public static int remove1(int[] boxes) {
        int n = boxes.length;
        int[][][] dp = new int[n][n][n];
        int ans = process1(boxes, 0, n - 1, 0, dp);
        return ans;
    }

    //

    /**
     * 方法2：优化的递归解法
     * 优化点：合并连续相同颜色的盒子，减少重复计算
     * @param boxes 盒子颜色数组
     * @param l 左边界
     * @param r 右边界
     * @param k 左边界前面相同颜色盒子的个数
     * @param dp 记忆化数组
     * @return 最大得分
     */
    private static int process2(int[] boxes, int l, int r, int k, int[][][] dp) {
        if (l > r) {
            return 0;
        }
        if (dp[l][r][k] > 0) {
            return dp[l][r][k];
        }
        
        // 找到从l开始的连续相同颜色盒子的结束位置
        int last = l;
        while (last + 1 <= r && boxes[last + 1] == boxes[l]) {
            last++;
        }
        
        int pre = k + last - l; // 总的相同颜色盒子数（包括前置的k个）
        
        // 策略1：直接移除这些连续的盒子
        int ans = (pre + 1) * (pre + 1) + process2(boxes, last + 1, r, 0, dp);
        
        // 策略2：寻找后面相同颜色的盒子进行合并
        for (int i = last + 2; i <= r; i++) {
            // 找到相同颜色且与前一个不同的位置（新的连续段开始）
            if (boxes[i] == boxes[l] && boxes[i - 1] != boxes[l]) {
                ans = Math.max(ans, 
                    process2(boxes, last + 1, i - 1, 0, dp) + 
                    process2(boxes, i, r, pre + 1, dp));
            }
        }
        
        dp[l][r][k] = ans;
        return ans;
    }

    /**
     * 方法2的对外接口 - 优化版本
     */
    public static int remove2(int[] boxes) {
        int n = boxes.length;
        int[][][] dp = new int[n][n][n];
        int ans = process2(boxes, 0, n - 1, 0, dp);
        return ans;
    }
}
