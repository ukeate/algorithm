package base.dp6_biz;

/**
 * 数组恢复问题
 * 给定一个数组，其中0表示缺失的数字，非0表示已知数字
 * 需要用1到200的数字填充0位置，使得每个位置的值都不大于其相邻位置的最大值
 * 即：arr[i] <= max(arr[i-1], arr[i+1])（边界位置只考虑一边）
 * 求有多少种填充方式
 * 
 * 这是一个约束满足的动态规划问题，关键在于状态定义和转移
 */
public class RestoreWays {
    /**
     * 验证数组是否满足约束条件
     */
    private static boolean isValid(int[] arr) {
        // 检查首元素
        if (arr[0] > arr[1]) {
            return false;
        }
        // 检查末元素
        if (arr[arr.length - 1] > arr[arr.length - 2]) {
            return false;
        }
        // 检查中间元素
        for (int i = 1; i < arr.length - 1; i++) {
            if (arr[i] > Math.max(arr[i - 1], arr[i + 1])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 方法0：暴力递归解法 - 穷举所有可能的填充方式
     */
    private static int process0(int[] arr, int idx) {
        if (idx == arr.length) {
            return isValid(arr) ? 1 : 0;
        } else {
            if (arr[idx] != 0) {
                return process0(arr, idx + 1);
            } else {
                int ways = 0;
                for (int v = 1; v <= 200; v++) {
                    arr[idx] = v;
                    ways += process0(arr, idx + 1);
                }
                arr[idx] = 0; // 恢复现场
                return ways;
            }
        }
    }

    public static int ways0(int[] arr) {
        return process0(arr, 0);
    }

    //

    /**
     * 方法1：优化递归解法 - 从右往左处理，利用约束条件剪枝
     * @param arr 原数组
     * @param i 当前位置
     * @param v 当前位置的值
     * @param s 与右邻居的关系：0-右边大，1-右边相等，2-右边小
     * @return 填充方案数
     */
    private static int process1(int[] arr, int i, int v, int s) {
        if (i == 0) {
            // 边界条件：第0个位置只需满足与右邻居的关系约束
            return ((s == 0 || s == 1) && (arr[0] == 0 || v == arr[0])) ? 1 : 0;
        }
        if (arr[i] != 0 && v != arr[i]) {
            return 0; // 位置已有值且不匹配
        }
        
        int ways = 0;
        // 根据当前位置与右邻居的关系，确定与左邻居的可能关系
        if (s == 0 || s == 1) {
            // 当前位置 <= 右邻居，所以左邻居可以取任何 < 当前值的数
            for (int pre = 1; pre < v; pre++) {
                ways += process1(arr, i - 1, pre, 0); // pre < v，所以右边（当前位置）更大
            }
        }
        // 左邻居 = 当前值
        ways += process1(arr, i - 1, v, 1);
        // 左邻居 > 当前值
        for (int pre = v + 1; pre <= 200; pre++) {
            ways += process1(arr, i - 1, pre, 2); // pre > v，所以右边（当前位置）更小
        }
        return ways;
    }

    /**
     * 方法1的对外接口
     */
    public static int ways1(int[] arr) {
        int n = arr.length;
        if (arr[n - 1] != 0) {
            // 最后一个位置已确定，关系设为2（右边没有元素，可视为右边更小）
            return process1(arr, n - 1, arr[n - 1], 2);
        } else {
            int ways = 0;
            // 枚举最后一个位置的所有可能值
            for (int v = 1; v <= 200; v++) {
                ways += process1(arr, n - 1, v, 2);
            }
            return ways;
        }
    }

    //

    /**
     * 方法2：动态规划解法
     * dp[i][v][s] = 位置i填入值v，与右邻居关系为s时的方案数
     * s: 0-右边大，1-右边相等，2-右边小
     */
    public static int ways2(int[] arr) {
        int n = arr.length;
        int[][][] dp = new int[n][201][3];
        
        // 初始化第0个位置
        if (arr[0] != 0) {
            dp[0][arr[0]][0] = 1;
            dp[0][arr[0]][1] = 1;
        } else {
            for (int v = 1; v <= 200; v++) {
                dp[0][v][0] = 1;
                dp[0][v][1] = 1;
            }
        }
        
        // 填表
        for (int i = 1; i < n; i++) {
            for (int v = 1; v <= 200; v++) {
                for (int s = 0; s < 3; s++) {
                    if (arr[i] == 0 || v == arr[i]) {
                        // 根据约束条件转移
                        if (s == 0 || s == 1) {
                            for (int pre = 1; pre < v; pre++) {
                                dp[i][v][s] += dp[i - 1][pre][0];
                            }
                        }
                        dp[i][v][s] += dp[i - 1][v][1];
                        for (int pre = v + 1; pre <= 200; pre++) {
                            dp[i][v][s] += dp[i - 1][pre][2];
                        }
                    }
                }
            }
        }
        
        // 统计最终答案
        if (arr[n - 1] != 0) {
            return dp[n - 1][arr[n - 1]][2];
        } else {
            int ways = 0;
            for (int v = 1; v <= 200; v++) {
                ways += dp[n - 1][v][2];
            }
            return ways;
        }
    }

    //

    /**
     * 获取前缀和，用于优化区间求和
     */
    private static int sum(int begin, int end, int relation, int[][] presum) {
        return presum[end][relation] - presum[begin - 1][relation];
    }

    /**
     * 方法3：前缀和优化的动态规划
     * 使用前缀和优化区间求和操作，将时间复杂度从O(n*200²*3)降到O(n*200*3)
     */
    public static int ways3(int[] arr) {
        int n = arr.length;
        int[][][] dp = new int[n][201][3];
        
        // 初始化第0个位置
        if (arr[0] != 0) {
            dp[0][arr[0]][0] = 1;
            dp[0][arr[0]][1] = 1;
        } else {
            for (int v = 1; v <= 200; v++) {
                dp[0][v][0] = 1;
                dp[0][v][1] = 1;
            }
        }
        
        // 构建第0层的前缀和
        int[][] presum = new int[201][3];
        for (int v = 1; v <= 200; v++) {
            for (int s = 0; s < 3; s++) {
                presum[v][s] = presum[v - 1][s] + dp[0][v][s];
            }
        }
        
        // 逐层填表
        for (int i = 1; i < n; i++) {
            for (int v = 1; v <= 200; v++) {
                for (int s = 0; s < 3; s++) {
                    if (arr[i] == 0 || v == arr[i]) {
                        if (s == 0 || s == 1) {
                            dp[i][v][s] += sum(1, v - 1, 0, presum);
                        }
                        dp[i][v][s] += dp[i - 1][v][1];
                        dp[i][v][s] += sum(v + 1, 200, 2, presum);
                    }
                }
            }
            // 更新前缀和
            for (int v = 1; v <= 200; v++) {
                for (int s = 0; s < 3; s++) {
                    presum[v][s] = presum[v - 1][s] + dp[i][v][s];
                }
            }
        }
        
        if (arr[n - 1] != 0) {
            return dp[n - 1][arr[n - 1]][2];
        } else {
            return sum(1, 200, 2, presum);
        }
    }

    //

    /**
     * 生成随机测试数组
     */
    private static int[] randomArr(int len) {
        int[] ans = new int[len];
        for (int i = 0; i < ans.length; i++) {
            if (Math.random() < 0.5) {
                ans[i] = 0; // 50%概率为缺失值
            } else {
                ans[i] = (int) (Math.random() * 200) + 1;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 15;
        int maxLen = 5;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int n = (int) (maxLen * Math.random()) + 2;
            int[] arr = randomArr(n);
            int ans0 = ways0(arr);
            int ans1 = ways1(arr);
            int ans2 = ways2(arr);
            int ans3 = ways3(arr);
            if (ans0 != ans1 || ans2 != ans3 || ans0 != ans2) {
                System.out.println("Wrong");
                System.out.println(ans0 + "|" + ans1 + "|" + ans2 + "|" + ans3);
                break;
            }
        }
        System.out.println("test end");
        int n = 1000000;
        int[] arr = randomArr(n);
        long begin = System.currentTimeMillis();
        ways3(arr);
        long end = System.currentTimeMillis();
        System.out.println("ways3 time:" + (end - begin) + " ms");
    }
}
