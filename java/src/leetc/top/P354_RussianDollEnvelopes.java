package leetc.top;

import java.util.*;

/**
 * LeetCode 354. 俄罗斯套娃信封问题 (Russian Doll Envelopes)
 * 
 * 问题描述：
 * 给你一个二维整数数组 envelopes ，其中 envelopes[i] = [wi, hi] ，表示第 i 个信封的宽度和高度。
 * 当另一个信封的宽度和高度都比这个信封大的时候，这个信封就可以放进另一个信封里，如同俄罗斯套娃一样。
 * 请计算 最多能有多少个 信封能组成一套俄罗斯套娃信封（即可以把一个信封放到另一个信封里面）。
 * 
 * 注意：不允许旋转信封。
 * 
 * 示例：
 * 输入：envelopes = [[5,4],[6,4],[6,7],[2,3]]
 * 输出：3
 * 解释：最多信封的个数为 3, 组合为: [2,3] => [5,4] => [6,7]。
 * 
 * 输入：envelopes = [[1,1],[1,1],[1,1]]
 * 输出：1
 * 
 * 解法思路：
 * 排序 + 最长递增子序列：
 * 
 * 1. 问题转化：
 *    - 这是一个二维的最长递增子序列问题
 *    - 需要同时考虑宽度和高度两个维度
 *    - 可以转化为一维LIS问题来解决
 * 
 * 2. 核心思想：
 *    - 先按宽度升序排序，宽度相同时按高度降序排序
 *    - 降序排序高度的目的：确保宽度相同的信封不会被错误地包含在LIS中
 *    - 然后在高度维度上求最长递增子序列
 * 
 * 3. 解法对比：
 *    方法一：动态规划（O(n²)）
 *    - 经典DP解法，适用于小规模数据
 *    - dp[i]表示以第i个信封结尾的最长套娃序列长度
 *    
 *    方法二：二分查找优化（O(n log n)）
 *    - 使用patience sorting的思想
 *    - 维护一个递增序列，用二分查找更新
 * 
 * 4. 算法步骤：
 *    - 按规则排序：宽度升序，高度降序
 *    - 提取高度数组
 *    - 在高度数组上应用LIS算法
 *    - 返回LIS的长度
 * 
 * 核心思想：
 * - 降维处理：将二维问题转化为一维LIS问题
 * - 排序策略：巧妙的排序避免错误的包含关系
 * - 贪心思想：在构建LIS时总是选择最小的可能值
 * 
 * 关键技巧：
 * - 高度降序：防止宽度相同的信封互相包含
 * - 二分查找：快速定位插入位置，优化时间复杂度
 * - LIS算法：核心算法的正确实现
 * 
 * 时间复杂度：O(n log n) - 排序 + 二分查找LIS
 * 空间复杂度：O(n) - 存储LIS数组
 * 
 * LeetCode链接：https://leetcode.com/problems/russian-doll-envelopes/
 */
public class P354_RussianDollEnvelopes {
    
    /**
     * 方法一：排序 + 二分查找LIS（推荐）
     * 
     * 使用二分查找优化的最长递增子序列算法
     * 
     * @param envelopes 信封数组，每个元素为[宽度, 高度]
     * @return 最多能套娃的信封数量
     */
    public int maxEnvelopes(int[][] envelopes) {
        if (envelopes == null || envelopes.length == 0) {
            return 0;
        }
        
        // 关键步骤：排序
        // 宽度升序，宽度相同时高度降序
        Arrays.sort(envelopes, (a, b) -> {
            if (a[0] != b[0]) {
                return a[0] - b[0]; // 宽度升序
            } else {
                return b[1] - a[1]; // 高度降序（关键！）
            }
        });
        
        // 提取高度数组
        int[] heights = new int[envelopes.length];
        for (int i = 0; i < envelopes.length; i++) {
            heights[i] = envelopes[i][1];
        }
        
        // 在高度数组上求最长递增子序列
        return lengthOfLIS(heights);
    }
    
    /**
     * 最长递增子序列（LIS）- 二分查找优化版本
     * 
     * 使用patience sorting的思想：
     * - 维护一个递增数组tails，tails[i]表示长度为i+1的递增子序列的最小尾部元素
     * - 对于每个新元素，用二分查找找到合适的位置进行替换或添加
     * 
     * @param nums 输入数组
     * @return 最长递增子序列的长度
     */
    private int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // tails[i] 表示长度为 i+1 的递增子序列的最小尾部元素
        List<Integer> tails = new ArrayList<>();
        
        for (int num : nums) {
            // 二分查找第一个大于等于num的位置
            int left = 0, right = tails.size();
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails.get(mid) < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            // 如果找到的位置等于tails的大小，说明num比所有元素都大
            // 直接添加到末尾，扩展LIS长度
            if (left == tails.size()) {
                tails.add(num);
            } else {
                // 否则替换找到位置的元素，保持tails[left]是长度为left+1的LIS的最小尾部
                tails.set(left, num);
            }
        }
        
        return tails.size();
    }
    
    /**
     * 方法二：动态规划解法（O(n²)）
     * 
     * 经典的动态规划解法，适用于理解和小规模数据
     * 
     * @param envelopes 信封数组
     * @return 最多能套娃的信封数量
     */
    public int maxEnvelopesDP(int[][] envelopes) {
        if (envelopes == null || envelopes.length == 0) {
            return 0;
        }
        
        int n = envelopes.length;
        
        // 排序：宽度升序，高度升序
        Arrays.sort(envelopes, (a, b) -> {
            if (a[0] != b[0]) {
                return a[0] - b[0];
            } else {
                return a[1] - b[1];
            }
        });
        
        // dp[i] 表示以第i个信封结尾的最长套娃序列长度
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // 每个信封至少可以形成长度为1的序列
        
        int maxLength = 1;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // 如果第j个信封可以放入第i个信封
                if (envelopes[j][0] < envelopes[i][0] && envelopes[j][1] < envelopes[i][1]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }
        
        return maxLength;
    }
    
    /**
     * 辅助方法：使用Collections.binarySearch进行二分查找
     * 
     * 这是另一种实现LIS的方式，利用Java标准库的二分查找
     * 
     * @param nums 输入数组
     * @return LIS长度
     */
    private int lengthOfLISWithCollections(int[] nums) {
        List<Integer> tails = new ArrayList<>();
        
        for (int num : nums) {
            int index = Collections.binarySearch(tails, num);
            
            if (index < 0) {
                // 如果没找到，binarySearch返回 -(插入点) - 1
                index = -(index + 1);
            }
            
            if (index == tails.size()) {
                tails.add(num);
            } else {
                tails.set(index, num);
            }
        }
        
        return tails.size();
    }
    
    /**
     * 方法三：使用TreeMap的解法（适用于有重复元素的情况）
     * 
     * 当数组中有大量重复元素时，TreeMap可能有更好的性能
     * 
     * @param envelopes 信封数组
     * @return 最多能套娃的信封数量
     */
    public int maxEnvelopesTreeMap(int[][] envelopes) {
        if (envelopes == null || envelopes.length == 0) {
            return 0;
        }
        
        // 排序
        Arrays.sort(envelopes, (a, b) -> {
            if (a[0] != b[0]) {
                return a[0] - b[0];
            } else {
                return b[1] - a[1];
            }
        });
        
        // 使用TreeMap来维护LIS
        TreeMap<Integer, Integer> dp = new TreeMap<>();
        
        for (int[] envelope : envelopes) {
            int height = envelope[1];
            
            // 找到第一个键值大于等于height的条目
            Integer ceiling = dp.ceilingKey(height);
            
            if (ceiling == null) {
                // 如果没有找到，说明height是最大的，直接添加
                dp.put(height, dp.size() + 1);
            } else {
                // 找到了，更新对应的值
                int length = dp.get(ceiling);
                dp.remove(ceiling);
                dp.put(height, length);
            }
        }
        
        return dp.size();
    }
    
    /**
     * 调试方法：打印LIS序列
     * 
     * 帮助理解算法过程的调试方法
     * 
     * @param nums 输入数组
     * @return LIS序列
     */
    public List<Integer> findLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new ArrayList<>();
        }
        
        int n = nums.length;
        int[] dp = new int[n];       // dp[i]表示以nums[i]结尾的LIS长度
        int[] parent = new int[n];   // parent[i]表示LIS中nums[i]的前一个元素的索引
        Arrays.fill(dp, 1);
        Arrays.fill(parent, -1);
        
        int maxLength = 1;
        int maxIndex = 0;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    parent[i] = j;
                }
            }
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                maxIndex = i;
            }
        }
        
        // 重构LIS序列
        List<Integer> lis = new ArrayList<>();
        int current = maxIndex;
        while (current != -1) {
            lis.add(nums[current]);
            current = parent[current];
        }
        Collections.reverse(lis);
        
        return lis;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P354_RussianDollEnvelopes solution = new P354_RussianDollEnvelopes();
        
        // 测试用例1
        int[][] envelopes1 = {{5,4},{6,4},{6,7},{2,3}};
        System.out.println("测试1 - 二分查找法: " + solution.maxEnvelopes(envelopes1));
        System.out.println("测试1 - 动态规划法: " + solution.maxEnvelopesDP(envelopes1));
        
        // 测试用例2
        int[][] envelopes2 = {{1,1},{1,1},{1,1}};
        System.out.println("测试2 - 相同信封: " + solution.maxEnvelopes(envelopes2));
        
        // 测试用例3
        int[][] envelopes3 = {{4,5},{4,6},{6,7},{2,3},{1,1}};
        System.out.println("测试3 - 复杂情况: " + solution.maxEnvelopes(envelopes3));
        
        // 测试用例4：边界情况
        int[][] envelopes4 = {};
        System.out.println("测试4 - 空数组: " + solution.maxEnvelopes(envelopes4));
        
        // 演示LIS序列
        int[] heights = {4, 4, 7, 3, 1};
        System.out.println("LIS序列示例: " + solution.findLIS(heights));
    }
}
