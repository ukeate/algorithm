package leetc.top;

/**
 * LeetCode 189. 旋转数组 (Rotate Array)
 * 
 * 问题描述：
 * 给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。
 * 
 * 进阶：
 * - 尽可能想出更多的解决方案，至少有三种不同的方法可以解决这个问题。
 * - 你可以使用空间复杂度为 O(1) 的原地算法解决这个问题吗？
 * 
 * 示例：
 * 输入: nums = [1,2,3,4,5,6,7], k = 3
 * 输出: [5,6,7,1,2,3,4]
 * 解释: 向右旋转 3 步: [1,2,3,4,5,6,7] -> [7,1,2,3,4,5,6] -> [6,7,1,2,3,4,5] -> [5,6,7,1,2,3,4]
 * 
 * 解法思路：
 * 本类提供了两种不同的解法：
 * 
 * 方法1：三次反转法
 * 1. 将问题转化为数组分段反转
 * 2. 先反转前n-k个元素，再反转后k个元素，最后反转整个数组
 * 3. 利用反转操作的性质达到旋转效果
 * 
 * 方法2：分块交换法
 * 1. 将数组分为左右两部分：左边n-k个元素，右边k个元素
 * 2. 通过交换操作逐步调整元素位置
 * 3. 使用递归思想处理不同大小的分块
 * 
 * 两种方法的核心思想：
 * - 都利用了数组旋转的数学性质
 * - 都实现了O(1)空间复杂度的原地算法
 * - 都需要处理k大于数组长度的情况
 * 
 * 时间复杂度：O(n) - 两种方法都需要遍历数组常数次
 * 空间复杂度：O(1) - 都是原地算法，只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/rotate-array/
 */
public class P189_RotateArray {
    
    /**
     * 反转数组的指定区间
     * 
     * @param nums 数组
     * @param l 左边界（包含）
     * @param r 右边界（包含）
     */
    private static void reverse(int[] nums, int l, int r) {
        while (l < r) {
            int tmp = nums[l];
            nums[l++] = nums[r];
            nums[r--] = tmp;
        }
    }

    /**
     * 方法1：三次反转法旋转数组
     * 
     * 算法思路：
     * 1. 原数组：[1,2,3,4,5,6,7]，k=3
     * 2. 反转前n-k个元素：[4,3,2,1,5,6,7]
     * 3. 反转后k个元素：[4,3,2,1,7,6,5]
     * 4. 反转整个数组：[5,6,7,1,2,3,4]
     * 
     * @param nums 待旋转的数组
     * @param k 旋转步数
     */
    public void rotate1(int[] nums, int k) {
        int n = nums.length;
        k = k % n;  // 处理k大于n的情况
        
        // 三步反转：分段反转 + 整体反转
        reverse(nums, 0, n - k - 1);     // 反转前n-k个元素
        reverse(nums, n - k, n - 1);     // 反转后k个元素
        reverse(nums, 0, n - 1);         // 反转整个数组
    }

    /**
     * 交换数组中两个区间的元素
     * 
     * @param nums 数组
     * @param start 起始位置
     * @param end 结束位置
     * @param size 交换的元素个数
     */
    private static void exchange(int[] nums, int start, int end, int size) {
        for (int i = end - size + 1; size > 0; start++, i++, size--) {
            int tmp = nums[start];
            nums[start] = nums[i];
            nums[i] = tmp;
        }
    }

    /**
     * 方法2：分块交换法旋转数组
     * 
     * 算法思路：
     * 1. 将数组分为左部分(n-k个元素)和右部分(k个元素)
     * 2. 目标是将右部分移到左部分前面
     * 3. 通过交换较小部分与对方的对应部分
     * 4. 递归处理剩余不平衡的部分
     * 
     * 分块交换的数学原理：
     * - 每次交换min(lpart, rpart)个元素
     * - 交换后更新区间和部分大小
     * - 直到左右部分大小平衡(diff = 0)
     * 
     * @param nums 待旋转的数组
     * @param k 旋转步数
     */
    public static void rotate2(int[] nums, int k) {
        int n = nums.length;
        k = k % n;  // 处理k大于n的情况
        
        if (k == 0) {
            return;  // 无需旋转
        }
        
        int l = 0, r = n - 1;           // 左右边界
        int lpart = n - k, rpart = k;   // 左右部分的大小
        
        // 不断交换直到左右平衡
        while (true) {
            int same = Math.min(lpart, rpart);  // 较小部分的大小
            int diff = lpart - rpart;           // 大小差值
            
            // 交换两部分的对应元素
            exchange(nums, l, r, same);
            
            if (diff == 0) {
                break;  // 完全平衡，旋转完成
            } else if (diff > 0) {
                // 左部分更大，调整左边界和左部分大小
                l += same;
                lpart = diff;
            } else {
                // 右部分更大，调整右边界和右部分大小
                r -= same;
                rpart = -diff;
            }
        }
    }
}
