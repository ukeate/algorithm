package giant.c35;

import java.util.Arrays;

/**
 * 观影时间最大化问题 (Maximum Movie Watching Time)
 * 
 * 问题来源：小红书面试题
 * 
 * 问题描述：
 * 一场电影的开始和结束时间用数组表示，例如["07:30","12:00"]
 * 已知有2000场电影，开始和结束都在同一天，这一天从00:00开始到23:59结束
 * 必须选3场完全不冲突的电影来观看，返回最大的观影时间
 * 如果无法选出3场完全不冲突的电影，返回-1
 * 
 * 解法思路：
 * 方法1：暴力枚举所有可能的3部电影组合，检查是否冲突
 * 方法2：动态规划 + 排序优化
 * 
 * 核心约束：
 * 1. 必须选择恰好3场电影
 * 2. 电影之间不能有时间冲突
 * 3. 优化目标是最大化总观影时间
 * 
 * 算法优化：
 * - 按开始时间排序，减少搜索空间
 * - 使用记忆化搜索避免重复计算
 * - 状态压缩：用时间、剩余电影数作为状态
 */
// 来自小红书
// 一场电影开始和结束时间可以用一个小数组来表示["07:30","12:00"]
// 已知有2000场电影开始和结束都在同一天，这一天从00:00开始到23:59结束
// 一定要选3场完全不冲突的电影来观看，返回最大的观影时间
// 如果无法选出3场完全不冲突的电影来观看，返回-1
public class WatchMovieMaxTime {
    
    /**
     * 交换数组中两个位置的电影信息
     * 
     * @param movies 电影数组，每个元素是[开始时间, 结束时间]
     * @param i 第一个位置
     * @param j 第二个位置
     */
    private static void swap(int[][] movies, int i, int j) {
        int[] tmp = movies[i];
        movies[i] = movies[j];
        movies[j] = tmp;
    }

    /**
     * 递归暴力搜索：尝试所有可能的3部电影组合
     * 
     * @param movies 电影数组
     * @param idx 当前选择的电影索引
     * @return 最大观影时间，如果不可能返回-1
     * 
     * 算法思路：
     * 1. 使用回溯法枚举所有可能的3部电影组合
     * 2. 对于每个组合，检查是否存在时间冲突
     * 3. 如果无冲突，计算总观影时间
     * 4. 返回所有有效组合中的最大观影时间
     * 
     * 时间复杂度：O(C(n,3)) = O(n³)，其中n是电影数量
     * 空间复杂度：O(1)，不计递归栈空间
     */
    private static int process1(int[][] movies, int idx) {
        if (idx == 3) {
            // 已选择3部电影，检查是否冲突
            int start = 0, watch = 0;
            for (int i = 0; i < 3; i++) {
                // 检查当前电影开始时间是否在上一部电影结束之后
                if (start > movies[i][0]) {
                    return -1; // 存在时间冲突
                }
                watch += movies[i][1] - movies[i][0]; // 累加观影时长
                start = movies[i][1]; // 更新结束时间
            }
            return watch;
        } else {
            // 还需要选择更多电影，继续递归
            int ans = -1;
            // 尝试将每个未选择的电影放到当前位置
            for (int i = idx; i < movies.length; i++) {
                swap(movies, idx, i);
                ans = Math.max(ans, process1(movies, idx + 1));
                swap(movies, idx, i); // 回溯
            }
            return ans;
        }
    }

    /**
     * 方法1：暴力搜索解法
     * 
     * @param movies 电影数组，每个元素是[开始时间, 结束时间]
     * @return 最大观影时间，如果无法选择3部不冲突电影返回-1
     */
    public static int maxEnjoy1(int[][] movies) {
        if (movies.length < 3) {
            return -1; // 电影数量不足3部
        }
        return process1(movies, 0);
    }

    /**
     * 动态规划递归函数（记忆化搜索）
     * 
     * @param movies 已排序的电影数组
     * @param idx 当前考虑的电影索引
     * @param time 当前时间点（上一部电影结束时间）
     * @param rest 还需要选择的电影数量
     * @param dp 记忆化数组
     * @return 最大观影时间，如果不可能返回-1
     * 
     * 状态定义：dp[idx][time][rest] = 从第idx部电影开始，当前时间为time，
     *          还需要选择rest部电影时的最大观影时间
     * 
     * 状态转移：
     * 1. 不选当前电影：process(idx+1, time, rest)
     * 2. 选择当前电影（如果时间允许且rest>0）：
     *    电影时长 + process(idx+1, 电影结束时间, rest-1)
     */
    private static int process2(int[][] movies, int idx, int time, int rest, int[][][] dp) {
        if (idx == movies.length) {
            // 所有电影都考虑完了
            return rest == 0 ? 0 : -1; // 如果恰好选了3部，返回0；否则返回-1
        }
        
        // 记忆化：如果已经计算过，直接返回
        if (dp[idx][time][rest] != -2) {
            return dp[idx][time][rest];
        }
        
        // 选择1：不选当前电影
        int p1 = process2(movies, idx + 1, time, rest, dp);
        
        // 选择2：选择当前电影（如果时间允许且还需要选电影）
        int next = movies[idx][0] >= time && rest > 0 ?
                process2(movies, idx + 1, movies[idx][1], rest - 1, dp) : -1;
        int p2 = next != -1 ? (movies[idx][1] - movies[idx][0] + next) : -1;
        
        // 取两种选择的最大值
        int ans = Math.max(p1, p2);
        dp[idx][time][rest] = ans;
        return ans;
    }

    /**
     * 方法2：动态规划优化解法
     * 
     * @param movies 电影数组，每个元素是[开始时间, 结束时间]
     * @return 最大观影时间，如果无法选择3部不冲突电影返回-1
     * 
     * 优化策略：
     * 1. 按开始时间排序，保证时间顺序
     * 2. 使用三维DP数组记忆化搜索结果
     * 3. 状态空间：电影索引 × 时间 × 剩余电影数
     * 
     * 时间复杂度：O(n × maxTime × 4)，其中n是电影数量
     * 空间复杂度：O(n × maxTime × 4)
     */
    public static int maxEnjoy2(int[][] movies) {
        // 按开始时间排序，如果开始时间相同则按结束时间排序
        Arrays.sort(movies, (a, b) -> a[0] != b[0] ? (a[0] - b[0]) : (a[1] - b[1]));
        
        // 找到最大结束时间，用于确定DP数组大小
        int max = 0;
        for (int[] movie : movies) {
            max = Math.max(max, movie[1]);
        }
        
        // 初始化DP数组：[电影索引][时间][剩余电影数]
        int[][][] dp = new int[movies.length][max + 1][4];
        for (int i = 0; i < movies.length; i++) {
            for (int j = 0; j <= max; j++) {
                for (int k = 0; k <= 3; k++) {
                    dp[i][j][k] = -2; // -2表示未计算，-1表示不可能，>=0表示有效结果
                }
            }
        }
        
        // 从第0部电影开始，当前时间为0，需要选择3部电影
        return process2(movies, 0, 0, 3, dp);
    }

    /**
     * 生成随机电影数据用于测试
     * 
     * @param len 电影数量
     * @param time 时间范围上限
     * @return 随机生成的电影数组
     */
    private static int[][] randomMovies(int len, int time) {
        int[][] movies = new int[len][2];
        for (int i = 0; i < len; i++) {
            int a = (int) (Math.random() * time);
            int b = (int) (Math.random() * time);
            movies[i][0] = Math.min(a, b); // 开始时间
            movies[i][1] = Math.max(a, b); // 结束时间
        }
        return movies;
    }

    /**
     * 测试方法：验证两种算法的正确性
     */
    public static void main(String[] args) {
        int times = 10000; // 测试次数
        int n = 10;        // 最大电影数量
        int t = 20;        // 时间范围
        System.out.println("test begin");
        
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * n) + 1;
            int[][] movies = randomMovies(len, t);
            
            // 对比两种方法的结果
            int ans1 = maxEnjoy1(movies);
            int ans2 = maxEnjoy2(movies);
            
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
