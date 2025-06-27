package leetc.top;

/**
 * LeetCode 41. 缺失的第一个正数
 * https://leetcode.cn/problems/first-missing-positive/
 * 
 * 问题描述：
 * 给你一个未排序的整数数组 nums ，请你找出其中没有出现的最小的正整数。
 * 请你实现时间复杂度为 O(n) 并且只使用常数级别额外空间的解决方案。
 * 
 * 解题思路：
 * 使用原地哈希（桶排序思想）的方法
 * 
 * 核心思想：
 * 1. 对于长度为n的数组，缺失的第一个正数一定在[1, n+1]范围内
 * 2. 我们可以把数组当作哈希表，让数字i放在下标i-1的位置上
 * 3. 最后从左到右扫描，第一个不满足arr[i] = i+1的位置i，答案就是i+1
 * 
 * 算法流程：
 * 1. 遍历数组，对于每个位置：
 *    - 如果arr[l] = l+1，说明该位置已经放了正确的数字，l++
 *    - 如果arr[l]不在[1,n]范围内或者已经在正确位置，删除它（用最后一个元素覆盖）
 *    - 否则将arr[l]交换到正确位置arr[arr[l]-1]
 * 2. 最终l的值就是第一个缺失正数-1
 * 
 * 关键技巧：
 * - 使用l和r两个指针，l指向当前处理位置，r指向有效数据的右边界
 * - 通过交换和覆盖操作，实现原地重排
 * 
 * 时间复杂度：O(n)，每个元素最多被移动一次
 * 空间复杂度：O(1)，只使用常数额外空间
 */
public class P41_FirstMissingPositive {
    
    /**
     * 交换数组中两个位置的元素
     * 
     * @param arr 数组
     * @param i 第一个位置
     * @param j 第二个位置
     */
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 找到数组中缺失的第一个正数
     * 
     * @param arr 输入的整数数组
     * @return 缺失的第一个正数
     */
    public int firstMissingPositive(int[] arr) {
        int l = 0;              // 左指针，指向当前处理的位置
        int r = arr.length;     // 右指针，指向有效数据范围的右边界
        
        // 原地哈希过程：让每个数字尽可能放到其应该在的位置
        while (l < r) {
            // 情况1：当前位置的数字已经正确（arr[l] = l+1）
            if (arr[l] == l + 1) {
                l++; // 继续处理下一个位置
            } 
            // 情况2：当前数字无效（<=l, >r, 或者目标位置已经有正确数字）
            else if (arr[l] <= l || arr[l] > r || arr[arr[l] - 1] == arr[l]) {
                // 用最后一个有效数字覆盖当前位置，缩小有效范围
                arr[l] = arr[--r];
            } 
            // 情况3：当前数字可以放到正确位置
            else {
                // 将arr[l]交换到位置arr[l]-1，使其满足arr[arr[l]-1] = arr[l]
                swap(arr, l, arr[l] - 1);
            }
        }
        
        // 此时l就是第一个不满足arr[i] = i+1的位置
        // 所以缺失的第一个正数是l+1
        return l + 1;
    }
}
