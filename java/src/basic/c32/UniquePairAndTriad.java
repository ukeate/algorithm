package basic.c32;

/**
 * 在有序数组中查找所有和为k的非重复二元组和三元组
 * 
 * 问题描述：
 * 给定一个有序数组，查找所有和为目标值k的不重复二元组和三元组
 * 
 * 算法思路：
 * 1. 二元组：使用双指针技术，左右指针向中间移动
 * 2. 三元组：固定一个数，对剩余部分使用双指针查找二元组
 * 3. 去重：通过跳过重复元素来确保结果的唯一性
 * 
 * 时间复杂度：O(n^2) 对于三元组，O(n) 对于二元组
 * 空间复杂度：O(1)
 */
public class UniquePairAndTriad {
    
    /**
     * 在有序数组中查找所有和为k的非重复二元组
     * 
     * @param arr 有序数组
     * @param k 目标和
     */
    public static void uniquePair(int[] arr, int k) {
        // 边界条件检查
        if (arr == null || arr.length < 2) {
            return;
        }
        
        int left = 0;                    // 左指针，从数组开始
        int right = arr.length - 1;     // 右指针，从数组末尾
        
        // 双指针向中间移动
        while (left < right) {
            if (arr[left] + arr[right] < k) {
                // 和小于目标值，左指针右移增大和
                left++;
            } else if (arr[left] + arr[right] > k) {
                // 和大于目标值，右指针左移减小和
                right--;
            } else {
                // 找到一对和为k的数对
                // 去重：只有当left为0或者当前数不等于前一个数时才打印
                if (left == 0 || arr[left - 1] != arr[left]) {
                    System.out.println(arr[left] + "," + arr[right]);
                }
                left++;
                right--;
            }
        }
    }

    /**
     * 辅助方法：在指定范围内查找和为k的二元组
     * 
     * @param arr 有序数组
     * @param first 第一个元素的索引（用于三元组的第一个数）
     * @param l 左边界
     * @param r 右边界
     * @param k 目标和
     */
    private static void rest(int[] arr, int first, int l, int r, int k) {
        while (l < r) {
            if (arr[l] + arr[r] < k) {
                // 和小于目标值，左指针右移
                l++;
            } else if (arr[l] + arr[r] > k) {
                // 和大于目标值，右指针左移
                r--;
            } else {
                // 找到一对和为k的数对
                // 去重：只有当l为first+1或者当前数不等于前一个数时才打印
                if (l == first + 1 || arr[l - 1] != arr[l]) {
                    System.out.println(arr[first] + "," + arr[l] + "," + arr[r]);
                }
                l++;
                r--;
            }
        }
    }

    /**
     * 在有序数组中查找所有和为k的非重复三元组
     * 
     * @param arr 有序数组
     * @param k 目标和
     */
    public static void uniqueTriad(int[] arr, int k) {
        // 边界条件检查
        if (arr == null || arr.length < 3) {
            return;
        }
        
        // 遍历数组，固定第一个数
        for (int i = 0; i < arr.length - 2; i++) {
            // 去重：只有当i为0或者当前数不等于前一个数时才处理
            if (i == 0 || arr[i] != arr[i - 1]) {
                // 在剩余部分查找和为(k - arr[i])的二元组
                rest(arr, i, i + 1, arr.length - 1, k - arr[i]);
            }
        }
    }

    /**
     * 打印数组的辅助方法
     * 
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        int sum = 10;
        int[] arr = {-8, -4, -3, 0, 1, 2, 4, 5, 8, 9};
        
        System.out.println("原数组：");
        print(arr);
        System.out.println("=====");
        
        System.out.println("和为" + sum + "的二元组：");
        uniquePair(arr, sum);
        System.out.println("=====");
        
        System.out.println("和为" + sum + "的三元组：");
        uniqueTriad(arr, sum);
    }
}
