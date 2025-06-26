package leetc.top;

/**
 * LeetCode 135. 分发糖果 (Candy)
 * 
 * 问题描述：
 * n 个孩子站成一排。给你一个整数数组 ratings 表示每个孩子的评分。
 * 你需要按照以下要求，给这些孩子分发糖果：
 * 1. 每个孩子至少分配到 1 个糖果
 * 2. 相邻的孩子中，评分高的孩子必须获得更多的糖果
 * 请你给出满足上述条件的最少糖果数目。
 * 
 * 解法思路：
 * 方法一：左右扫描法
 * 1. 从左到右扫描，处理递增序列
 * 2. 从右到左扫描，处理递减序列  
 * 3. 每个位置取两次扫描的最大值
 * 
 * 方法二：一次遍历优化（状态机）
 * 1. 识别序列的模式：递增、递减、平坦
 * 2. 对于递增序列：线性递增分配糖果
 * 3. 对于递减序列：计算总需求，考虑与前序列的连接
 * 4. 对于平坦序列：重置为最少糖果
 * 
 * 核心洞察：
 * - 问题本质是在约束条件下的最优分配
 * - 递增序列处理简单，递减序列需要特殊处理
 * - 递减序列的长度影响糖果分配策略
 * 
 * 优化策略：
 * - 避免额外空间的左右扫描
 * - 实时计算递减序列的糖果需求
 * - 处理递减序列与前序列的边界情况
 * 
 * 时间复杂度：O(n) - 遍历数组
 * 空间复杂度：方法一O(n)，方法二O(1)
 * 
 * LeetCode链接：https://leetcode.com/problems/candy/
 */
public class P135_Candy {
    
    /**
     * 方法一：左右扫描法
     * 
     * @param arr 评分数组
     * @return 最少糖果数
     */
    public static int candy1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        
        // 从左到右扫描：处理递增情况
        int[] left = new int[n];
        for (int i = 1; i < n; i++) {
            if (arr[i - 1] < arr[i]) {
                left[i] = left[i - 1] + 1;
            }
        }
        
        // 从右到左扫描：处理递减情况
        int[] right = new int[n];
        for (int i = n - 2; i >= 0; i--) {
            if (arr[i] > arr[i + 1]) {
                right[i] = right[i + 1] + 1;
            }
        }
        
        // 取每个位置的最大值
        int ans = 0;
        for (int i = 0; i < n; i++) {
            ans += Math.max(left[i], right[i]);
        }
        
        return ans + n;  // 加上每个孩子的基础1个糖果
    }

    /**
     * 找到下一个递减序列的结束位置（局部最小值）
     * 
     * @param arr 评分数组
     * @param start 搜索起始位置
     * @return 局部最小值位置
     */
    private static int nextMinIdx2(int[] arr, int start) {
        for (int i = start; i < arr.length - 1; i++) {
            if (arr[i] <= arr[i + 1]) {  // 找到非递减位置
                return i;
            }
        }
        return arr.length - 1;  // 一直递减到末尾
    }

    /**
     * 计算递减序列所需的糖果数
     * 对于长度为n的递减序列，需要 n + n*(n-1)/2 个糖果
     * 
     * @param arr 评分数组
     * @param left 序列左边界
     * @param right 序列右边界
     * @return 该递减序列所需糖果数
     */
    private static int rightCands(int[] arr, int left, int right) {
        int n = right - left + 1;  // 序列长度
        return n + n * (n - 1) / 2;  // 1+2+...+n = n + n*(n-1)/2
    }

    /**
     * 方法二：一次遍历优化解法
     * 
     * @param arr 评分数组
     * @return 最少糖果数
     */
    public static int candy2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        // 处理开头的递减序列
        int idx = nextMinIdx2(arr, 0);
        int res = rightCands(arr, 0, idx++);
        
        int lbase = 1;      // 左侧序列的糖果基数
        int next = 0;       // 下一个处理位置
        int rcands = 0;     // 右侧递减序列糖果数
        int rbase = 0;      // 右侧递减序列基数
        
        while (idx != arr.length) {
            if (arr[idx] > arr[idx - 1]) {
                // 递增：糖果数递增
                res += ++lbase;
                idx++;
            } else if (arr[idx] < arr[idx - 1]) {
                // 递减：处理递减序列
                next = nextMinIdx2(arr, idx - 1);
                rcands = rightCands(arr, idx - 1, next++);
                rbase = next - idx + 1;  // 递减序列在连接点的糖果数
                
                // 递减序列糖果数 + 连接点补偿（取较大的基数）
                res += rcands + (rbase > lbase ? -lbase : -rbase);
                lbase = 1;  // 重置基数
                idx = next;
            } else {
                // 相等：重置为最少糖果
                res += 1;
                lbase = 1;
                idx++;
            }
        }
        
        return res;
    }
}
