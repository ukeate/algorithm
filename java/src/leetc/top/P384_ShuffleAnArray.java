package leetc.top;

/**
 * LeetCode 384. 打乱数组 (Shuffle an Array)
 * 
 * 问题描述：
 * 给你一个整数数组 nums，设计算法来打乱一个没有重复元素的数组。
 * 实现 Solution class:
 * - Solution(int[] nums) 使用整数数组 nums 初始化对象
 * - int[] reset() 重设数组到它的初始状态并返回
 * - int[] shuffle() 返回数组随机打乱后的结果
 * 
 * 解法思路：
 * 使用Fisher-Yates洗牌算法（Knuth洗牌算法）：
 * 1. 保存原数组的副本用于reset操作
 * 2. 维护一个可变的shuffle数组用于打乱操作
 * 3. 洗牌过程：从数组末尾开始，每次随机选择一个位置与当前位置交换
 * 4. 这样可以保证每种排列出现的概率相等
 * 
 * 算法正确性：
 * Fisher-Yates算法是标准的随机排列算法，能保证：
 * - 每个元素在任意位置出现的概率为1/n
 * - 生成的排列是真正的均匀分布
 * 
 * 时间复杂度：
 * - reset(): O(1) - 直接返回原数组引用
 * - shuffle(): O(n) - 需要遍历整个数组进行交换
 * 
 * 空间复杂度：O(n) - 需要额外存储原数组和shuffle数组
 */
public class P384_ShuffleAnArray {
    class Solution {
        private int[] origin;  // 原始数组的引用
        private int[] shuffle; // 用于打乱操作的副本数组
        private int n;         // 数组长度

        /**
         * 构造函数：初始化数组打乱器
         * 
         * @param nums 输入的整数数组
         */
        public Solution(int[] nums) {
            origin = nums;  // 保存原数组引用
            n = nums.length;
            shuffle = new int[n]; // 创建副本数组
            for (int i = 0; i < n; i++) {
                shuffle[i] = origin[i];
            }
        }

        /**
         * 重置数组到初始状态
         * 
         * @return 原始数组
         */
        public int[] reset() {
            return origin;
        }

        /**
         * 使用Fisher-Yates算法打乱数组
         * 
         * 原理：从数组末尾开始，每次随机选择[0,i]范围内的一个位置
         * 与当前位置交换。这样可以保证每种排列出现的概率相等。
         * 
         * @return 打乱后的数组
         */
        public int[] shuffle() {
            for (int i = n - 1; i >= 0; i--) {
                // 在[0, i]范围内随机选择一个位置
                int r = (int) (Math.random() * (i + 1));
                // 与当前位置交换
                int tmp = shuffle[r];
                shuffle[r] = shuffle[i];
                shuffle[i] = tmp;
            }
            return shuffle;
        }
    }
}
