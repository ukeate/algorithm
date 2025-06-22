package base.dp5_cmpr;

import java.util.ArrayList;
import java.util.List;

// Travelling Salesman Problem
/**
 * 旅行商问题 (TSP - Travelling Salesman Problem)
 * 给定n个城市和城市间的距离矩阵，找到一条最短路径访问所有城市并回到起点
 * 这是一个经典的NP-hard问题，使用状态压缩DP可以在O(n²*2^n)时间内解决
 */
public class TSP {
    /**
     * 方法1：使用List表示访问状态的递归解法
     * @param matrix 距离矩阵
     * @param set 城市访问状态列表，null表示已访问
     * @param start 当前所在城市
     * @return 从当前城市出发访问剩余城市并回到起点的最短距离
     */
    private static int f1(int[][] matrix, List<Integer> set, int start) {
        // 统计剩余未访问的城市数量
        int cityNum = 0;
        for (int i = 0; i < set.size(); i++) {
            if (set.get(i) != null) {
                cityNum++;
            }
        }
        // 只剩当前城市，直接回到起点
        if (cityNum == 1) {
            return matrix[start][0];
        }
        
        // 标记当前城市为已访问
        set.set(start, null);
        int min = Integer.MAX_VALUE;
        
        // 尝试访问每个未访问的城市
        for (int i = 0; i < set.size(); i++) {
            if (set.get(i) != null) {
                int cur = matrix[start][i] + f1(matrix, set, i);
                min = Math.min(min, cur);
            }
        }
        
        // 恢复状态
        set.set(start, 1);
        return min;
    }

    /**
     * 方法1的对外接口
     */
    public static int t1(int[][] matrix) {
        int n = matrix.length;
        List<Integer> set = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            set.add(i);
        }
        return f1(matrix, set, 0);
    }

    //

    /**
     * 方法2：使用位运算表示访问状态的递归解法
     * @param matrix 距离矩阵
     * @param cityStatus 城市访问状态，第i位为1表示城市i未访问
     * @param start 当前所在城市
     * @return 最短距离
     */
    private static int f2(int[][] matrix, int cityStatus, int start) {
        // 只剩一个城市（当前城市），直接回到起点
        if (cityStatus == (cityStatus & -cityStatus)) {
            return matrix[start][0];
        }
        
        // 标记当前城市为已访问
        cityStatus &= (~(1 << start));
        int min = Integer.MAX_VALUE;
        
        // 尝试访问每个未访问的城市
        for (int move = 0; move < matrix.length; move++) {
            if ((cityStatus & (1 << move)) != 0) {
                int cur = matrix[start][move] + f2(matrix, cityStatus, move);
                min = Math.min(min, cur);
            }
        }
        
        // 恢复状态
        cityStatus |= (1 << start);
        return min;
    }

    /**
     * 方法2的对外接口 - 使用位运算优化
     */
    public static int t2(int[][] matrix) {
        int n = matrix.length;
        int allCity = (1 << n) - 1; // 所有城市都未访问的状态
        return f2(matrix, allCity, 0);
    }

    //

    /**
     * 方法3：带记忆化的状态压缩DP
     * @param matrix 距离矩阵
     * @param cityStatus 城市访问状态
     * @param start 当前所在城市
     * @param dp 记忆化数组
     * @return 最短距离
     */
    private static int f3(int[][] matrix, int cityStatus, int start, int[][] dp) {
        // 查询记忆化结果
        if (dp[cityStatus][start] != -1) {
            return dp[cityStatus][start];
        }
        
        // 基础情况：只剩当前城市
        if (cityStatus == (cityStatus & -cityStatus)) {
            dp[cityStatus][start] = matrix[start][0];
            return dp[cityStatus][start];
        }
        
        cityStatus &= (~(1 << start));
        int min = Integer.MAX_VALUE;
        
        for (int move = 0; move < matrix.length; move++) {
            if ((cityStatus & (1 << move)) != 0) {
                int cur = matrix[start][move] + f3(matrix, cityStatus, move, dp);
                min = Math.min(min, cur);
            }
        }
        
        cityStatus |= (1 << start);
        dp[cityStatus][start] = min;
        return dp[cityStatus][start];
    }

    /**
     * 方法3：记忆化搜索版本
     */
    public static int t3(int[][] matrix) {
        int n = matrix.length;
        int allCity = (1 << n) - 1;
        int[][] dp = new int[1 << n][n];
        // 初始化为-1表示未计算
        for (int i = 0; i < (1 << n); i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = -1;
            }
        }
        return f3(matrix, allCity, 0, dp);
    }

    //

    /**
     * 方法4：标准的状态压缩DP
     * dp[status][start]表示在状态status下，当前在城市start的最短距离
     */
    public static int t4(int[][] matrix) {
        int n = matrix.length;
        int statusNums = 1 << n;
        int[][] dp = new int[statusNums][n];
        
        // 枚举所有状态
        for (int status = 0; status < statusNums; status++) {
            for (int start = 0; start < n; start++) {
                // 当前城市必须在状态中
                if ((status & (1 << start)) != 0) {
                    // 基础情况：只有当前城市
                    if (status == (status & -status)) {
                        dp[status][start] = matrix[start][0];
                    } else {
                        int min = Integer.MAX_VALUE;
                        int preStatus = status & (~(1 << start)); // 移除当前城市的状态
                        
                        // 枚举前一个城市
                        for (int i = 0; i < n; i++) {
                            if ((preStatus & (1 << i)) != 0) {
                                int cur = matrix[start][i] + dp[preStatus][i];
                                min = Math.min(min, cur);
                            }
                        }
                        dp[status][start] = min;
                    }
                }
            }
        }
        return dp[statusNums - 1][0]; // 访问所有城市后回到起点
    }

    //

    /**
     * 方法5：指定起点的TSP问题
     * @param matrix 距离矩阵
     * @param origin 起点城市
     * @return 最短距离
     */
    public static int t5(int[][] matrix, int origin) {
        if (matrix == null || matrix.length < 2 || origin < 0 || origin >= matrix.length) {
            return 0;
        }
        
        // 去除起点，只考虑其他城市
        int n = matrix.length - 1;
        int s = 1 << n;
        int[][] dp = new int[s][n];
        
        int icity = 0;
        int kcity = 0;
        
        // 基础情况：从起点到各个城市
        for (int i = 0; i < n; i++) {
            icity = i < origin ? i : i + 1; // 映射到原矩阵的城市编号
            dp[0][i] = matrix[icity][origin];
        }
        
        // 填表
        for (int status = 1; status < s; status++) {
            for (int i = 0; i < n; i++) {
                dp[status][i] = Integer.MAX_VALUE;
                if ((1 << i & status) != 0) {
                    icity = i < origin ? i : i + 1;
                    for (int k = 0; k < n; k++) {
                        if ((1 << k & status) != 0) {
                            kcity = k < origin ? k : k + 1;
                            dp[status][i] = Math.min(dp[status][i], 
                                matrix[icity][kcity] + dp[status ^ (1 << i)][k]);
                        }
                    }
                }
            }
        }
        
        // 找到最短路径：访问所有城市后回到起点
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            icity = i < origin ? i : i + 1;
            ans = Math.min(ans, dp[s - 1][i] + matrix[origin][icity]);
        }
        return ans;
    }

    /**
     * 生成随机图用于测试
     */
    public static int[][] randomGraph(int maxSize, int maxVal) {
        int len = (int) (maxSize * Math.random()) + 1;
        int[][] matrix = new int[len][len];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                matrix[i][j] = (int) (maxVal + Math.random()) + 1;
            }
        }
        // 对角线设为0（自己到自己的距离）
        for (int i = 0; i < len; i++) {
            matrix[i][i] = 0;
        }
        return matrix;
    }

    /**
     * 测试方法，验证各个算法的正确性
     */
    public static void main(String[] args) {
        int times = 1000;
        int maxLen = 10;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[][] matrix = randomGraph(maxLen, maxVal);
            int origin = (int) (matrix.length * Math.random());
            int ans1 = t1(matrix);
            int ans2 = t2(matrix);
            int ans3 = t3(matrix);
            int ans4 = t4(matrix);
            int ans5 = t5(matrix, 0);
            if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3 || ans1 != ans5) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2 + "|" + ans3 + "|" + ans4 + "|" + ans5);
                break;
            }
        }
        System.out.println("test end");
    }
}
