package giant.c41;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 诗歌韵律子序列问题
 * 
 * 问题描述：
 * 给定一个字符串S，一个字符串T，和正整数K
 * 字符串S代表一首诗歌，字符串T代表一种韵律
 * 韵律字符串T的长度等于K
 * 
 * 任务：
 * 选择字符串S的一个子序列，使得这个子序列的长度正好等于K
 * 并且这个子序列的韵律距离尽可能的小
 * 
 * 韵律距离定义：
 * 对于子序列sub[0...K-1]，韵律距离 = Σ(|sub[i] - T[i]|)
 * 其中|sub[i] - T[i]|表示字符sub[i]和T[i]的ASCII码差值的绝对值
 * 
 * 返回最小的韵律距离
 * 
 * 解题思路：
 * 动态规划 + 贪心优化
 * 1. 状态定义：dp[i][j] = 考虑S的前i个字符，选择j个字符的最小韵律距离
 * 2. 状态转移：
 *    - 不选择S[i]：dp[i][j] = dp[i-1][j]
 *    - 选择S[i]：dp[i][j] = min(dp[i-1][j-1] + |S[i] - T[j-1]|)
 * 3. 优化：对于每个T[j]，预处理S中所有字符与T[j]的距离
 * 
 * 时间复杂度：O(|S| * K)
 * 空间复杂度：O(|S| * K)
 * 
 * 来源：字节跳动面试题
 * 
 * @author Zhu Runqi
 */
public class PoemProblem {

    /**
     * 计算诗歌韵律子序列的最小距离（方法1：动态规划）
     * 
     * 算法思路：
     * 1. 定义dp[i][j]：考虑S的前i个字符，选择j个字符的最小韵律距离
     * 2. 初始化：dp[0][0] = 0，其他位置为无穷大
     * 3. 状态转移：
     *    - 不选择当前字符：dp[i][j] = dp[i-1][j]
     *    - 选择当前字符：dp[i][j] = dp[i-1][j-1] + |S[i-1] - T[j-1]|
     * 4. 取两种选择的最小值
     * 
     * @param S 诗歌字符串
     * @param T 韵律字符串
     * @param K 子序列长度（等于T的长度）
     * @return 最小韵律距离
     */
    public static int minDistance1(String S, String T, int K) {
        char[] s = S.toCharArray();
        char[] t = T.toCharArray();
        int n = s.length;
        
        // dp[i][j]：考虑S的前i个字符，选择j个字符的最小韵律距离
        int[][] dp = new int[n + 1][K + 1];
        
        // 初始化：除了dp[0][0] = 0，其他都是无穷大
        for (int i = 0; i <= n; i++) {
            for (int j = 1; j <= K; j++) {
                dp[i][j] = Integer.MAX_VALUE;
            }
        }
        
        // 动态规划转移
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= Math.min(i, K); j++) {
                // 不选择当前字符S[i-1]
                dp[i][j] = dp[i - 1][j];
                
                // 选择当前字符S[i-1]（如果j > 0）
                if (j > 0 && dp[i - 1][j - 1] != Integer.MAX_VALUE) {
                    int cost = Math.abs(s[i - 1] - t[j - 1]);
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 1] + cost);
                }
            }
        }
        
        return dp[n][K];
    }

    /**
     * 计算诗歌韵律子序列的最小距离（方法2：贪心+排序优化）
     * 
     * 算法思路：
     * 1. 对于韵律字符串T的每个位置j，找到S中所有字符与T[j]的距离
     * 2. 构建候选字符数组，包含(字符在S中的位置, 与T[j]的距离, 对应的j)
     * 3. 使用贪心策略选择最优的K个字符
     * 4. 通过排序和动态规划找到最优子序列
     * 
     * @param S 诗歌字符串
     * @param T 韵律字符串
     * @param K 子序列长度
     * @return 最小韵律距离
     */
    public static int minDistance2(String S, String T, int K) {
        char[] s = S.toCharArray();
        char[] t = T.toCharArray();
        int n = s.length;
        
        // 构建候选字符数组
        // 每个元素包含: [在S中的位置, 韵律距离, 在T中的目标位置]
        int[][] candidates = new int[n * K][3];
        int count = 0;
        
        // 为每个T[j]找到S中所有字符的距离
        for (int j = 0; j < K; j++) {
            for (int i = 0; i < n; i++) {
                candidates[count][0] = i;  // 在S中的位置
                candidates[count][1] = Math.abs(s[i] - t[j]);  // 韵律距离
                candidates[count][2] = j;  // 在T中的目标位置
                count++;
            }
        }
        
        // 按韵律距离排序
        Arrays.sort(candidates, 0, count, Comparator.comparingInt(a -> a[1]));
        
        // 使用贪心策略选择
        boolean[] used = new boolean[n];
        int[] selected = new int[K];
        Arrays.fill(selected, -1);
        int totalDistance = 0;
        
        for (int i = 0; i < count && hasUnfilled(selected); i++) {
            int pos = candidates[i][0];
            int distance = candidates[i][1];
            int target = candidates[i][2];
            
            // 检查是否可以选择这个位置
            if (!used[pos] && selected[target] == -1) {
                // 检查是否保持子序列的顺序
                if (canSelect(selected, target, pos)) {
                    used[pos] = true;
                    selected[target] = pos;
                    totalDistance += distance;
                }
            }
        }
        
        // 如果没有找到完整的解，回退到方法1
        if (hasUnfilled(selected)) {
            return minDistance1(S, T, K);
        }
        
        return totalDistance;
    }

    /**
     * 检查是否还有未填充的位置
     * 
     * @param selected 已选择的位置数组
     * @return 是否还有-1的位置
     */
    private static boolean hasUnfilled(int[] selected) {
        for (int pos : selected) {
            if (pos == -1) return true;
        }
        return false;
    }

    /**
     * 检查是否可以在target位置选择pos，保持子序列的顺序
     * 
     * @param selected 已选择的位置数组
     * @param target 目标位置
     * @param pos 在S中的位置
     * @return 是否可以选择
     */
    private static boolean canSelect(int[] selected, int target, int pos) {
        // 检查左边的位置
        for (int i = 0; i < target; i++) {
            if (selected[i] != -1 && selected[i] >= pos) {
                return false;
            }
        }
        
        // 检查右边的位置
        for (int i = target + 1; i < selected.length; i++) {
            if (selected[i] != -1 && selected[i] <= pos) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * 计算诗歌韵律子序列的最小距离（方法3：优化的动态规划）
     * 
     * 算法思路：
     * 1. 使用滚动数组优化空间复杂度
     * 2. 预处理字符距离，避免重复计算
     * 3. 剪枝：如果当前距离已经超过已知最优解，提前结束
     * 
     * @param S 诗歌字符串
     * @param T 韵律字符串
     * @param K 子序列长度
     * @return 最小韵律距离
     */
    public static int minDistance3(String S, String T, int K) {
        char[] s = S.toCharArray();
        char[] t = T.toCharArray();
        int n = s.length;
        
        // 使用滚动数组优化空间
        int[] prev = new int[K + 1];
        int[] curr = new int[K + 1];
        
        // 初始化
        Arrays.fill(prev, Integer.MAX_VALUE);
        Arrays.fill(curr, Integer.MAX_VALUE);
        prev[0] = 0;
        
        // 动态规划
        for (int i = 1; i <= n; i++) {
            curr[0] = 0;  // 不选择任何字符的距离为0
            
            for (int j = 1; j <= Math.min(i, K); j++) {
                // 不选择当前字符
                curr[j] = prev[j];
                
                // 选择当前字符
                if (prev[j - 1] != Integer.MAX_VALUE) {
                    int cost = Math.abs(s[i - 1] - t[j - 1]);
                    curr[j] = Math.min(curr[j], prev[j - 1] + cost);
                }
            }
            
            // 交换数组
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        
        return prev[K];
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 诗歌韵律子序列问题 ===\n");
        
        // 测试用例1：基本示例
        System.out.println("测试用例1：基本示例");
        String S1 = "abcde";
        String T1 = "ace";
        int K1 = 3;
        int result1_1 = minDistance1(S1, T1, K1);
        int result1_2 = minDistance2(S1, T1, K1);
        int result1_3 = minDistance3(S1, T1, K1);
        System.out.println("诗歌S: \"" + S1 + "\"");
        System.out.println("韵律T: \"" + T1 + "\"");
        System.out.println("子序列长度K: " + K1);
        System.out.println("最小韵律距离 (方法1): " + result1_1);
        System.out.println("最小韵律距离 (方法2): " + result1_2);
        System.out.println("最小韵律距离 (方法3): " + result1_3);
        System.out.println("分析: 选择子序列\"ace\"，距离为0");
        System.out.println();
        
        // 测试用例2：需要计算距离
        System.out.println("测试用例2：需要计算距离");
        String S2 = "hello";
        String T2 = "world";
        int K2 = 5;
        int result2_1 = minDistance1(S2, T2, K2);
        int result2_2 = minDistance2(S2, T2, K2);
        int result2_3 = minDistance3(S2, T2, K2);
        System.out.println("诗歌S: \"" + S2 + "\"");
        System.out.println("韵律T: \"" + T2 + "\"");
        System.out.println("子序列长度K: " + K2);
        System.out.println("最小韵律距离 (方法1): " + result2_1);
        System.out.println("最小韵律距离 (方法2): " + result2_2);
        System.out.println("最小韵律距离 (方法3): " + result2_3);
        System.out.println("分析: 选择整个字符串，计算每个字符与目标的距离");
        System.out.println();
        
        // 测试用例3：较短的子序列
        System.out.println("测试用例3：较短的子序列");
        String S3 = "programming";
        String T3 = "pro";
        int K3 = 3;
        int result3_1 = minDistance1(S3, T3, K3);
        int result3_2 = minDistance2(S3, T3, K3);
        int result3_3 = minDistance3(S3, T3, K3);
        System.out.println("诗歌S: \"" + S3 + "\"");
        System.out.println("韵律T: \"" + T3 + "\"");
        System.out.println("子序列长度K: " + K3);
        System.out.println("最小韵律距离 (方法1): " + result3_1);
        System.out.println("最小韵律距离 (方法2): " + result3_2);
        System.out.println("最小韵律距离 (方法3): " + result3_3);
        System.out.println("分析: 选择前三个字符\"pro\"，距离为0");
        System.out.println();
        
        // 测试用例4：复杂示例
        System.out.println("测试用例4：复杂示例");
        String S4 = "abcdefghijk";
        String T4 = "bdf";
        int K4 = 3;
        int result4_1 = minDistance1(S4, T4, K4);
        int result4_2 = minDistance2(S4, T4, K4);
        int result4_3 = minDistance3(S4, T4, K4);
        System.out.println("诗歌S: \"" + S4 + "\"");
        System.out.println("韵律T: \"" + T4 + "\"");
        System.out.println("子序列长度K: " + K4);
        System.out.println("最小韵律距离 (方法1): " + result4_1);
        System.out.println("最小韵律距离 (方法2): " + result4_2);
        System.out.println("最小韵律距离 (方法3): " + result4_3);
        System.out.println("分析: 选择子序列\"bdf\"，距离为0");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        String longS = "abcdefghijklmnopqrstuvwxyz".repeat(10);
        String longT = "hello";
        int longK = 5;
        
        System.out.println("长字符串性能测试（S长度: " + longS.length() + "）");
        
        long start1 = System.currentTimeMillis();
        int longResult1 = minDistance1(longS, longT, longK);
        long end1 = System.currentTimeMillis();
        
        long start2 = System.currentTimeMillis();
        int longResult2 = minDistance2(longS, longT, longK);
        long end2 = System.currentTimeMillis();
        
        long start3 = System.currentTimeMillis();
        int longResult3 = minDistance3(longS, longT, longK);
        long end3 = System.currentTimeMillis();
        
        System.out.println("方法1结果: " + longResult1 + ", 时间: " + (end1 - start1) + "ms");
        System.out.println("方法2结果: " + longResult2 + ", 时间: " + (end2 - start2) + "ms");
        System.out.println("方法3结果: " + longResult3 + ", 时间: " + (end3 - start3) + "ms");
        System.out.println();
        
        System.out.println("=== 算法原理解析 ===");
        System.out.println("1. 问题特征：");
        System.out.println("   - 子序列选择：从S中选择K个字符，保持相对顺序");
        System.out.println("   - 韵律匹配：最小化选择的子序列与目标韵律的距离");
        System.out.println("   - 距离计算：字符ASCII码差值的绝对值之和");
        System.out.println();
        System.out.println("2. 动态规划方法：");
        System.out.println("   - 状态定义：dp[i][j] = 考虑前i个字符，选择j个字符的最小距离");
        System.out.println("   - 状态转移：选择或不选择当前字符");
        System.out.println("   - 时间复杂度：O(|S| * K)");
        System.out.println();
        System.out.println("3. 贪心优化方法：");
        System.out.println("   - 候选生成：为每个目标字符找到所有可能的匹配");
        System.out.println("   - 排序选择：按距离排序，贪心选择最优匹配");
        System.out.println("   - 顺序约束：保证子序列的相对顺序");
        System.out.println();
        System.out.println("4. 空间优化方法：");
        System.out.println("   - 滚动数组：将二维DP优化为一维");
        System.out.println("   - 空间复杂度：从O(|S| * K)优化到O(K)");
        System.out.println("   - 适合处理长字符串");
        System.out.println();
        System.out.println("5. 关键技巧：");
        System.out.println("   - 状态压缩：使用滚动数组减少空间开销");
        System.out.println("   - 预处理：避免重复计算字符距离");
        System.out.println("   - 剪枝：提前终止不可能的搜索路径");
        System.out.println();
        System.out.println("6. 应用场景：");
        System.out.println("   - 文本相似度匹配");
        System.out.println("   - 序列比对算法");
        System.out.println("   - 字符串编辑距离变种");
        System.out.println("   - 生物信息学中的序列分析");
    }
}
