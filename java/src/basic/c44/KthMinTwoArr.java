package basic.c44;

import java.util.Arrays;

/**
 * 两个有序数组中找第K小元素问题
 * 
 * 问题描述：
 * 给定两个有序数组arr1和arr2，找出合并后第k小的元素
 * 要求时间复杂度为O(log(min(m,n)))，其中m和n分别是两个数组的长度
 * 
 * 算法思路：
 * 基于二分查找的分治算法
 * 核心是利用"上中位数"概念，通过不断缩小搜索范围来定位第k小元素
 * 
 * 关键洞察：
 * 1. 根据k的不同取值范围，采用不同的处理策略
 * 2. 利用数组有序的性质，通过比较中位数来剪枝
 * 3. 递归地在较小的范围内继续搜索
 * 
 * 分情况讨论：
 * - k ≤ s：在两个数组的前k个元素中寻找
 * - k > l：需要排除一些前面的元素
 * - s < k ≤ l：在中间范围内寻找
 * 
 * 时间复杂度：O(log(min(m,n)))
 * 空间复杂度：O(1)
 * 
 * @author 算法学习
 */
public class KthMinTwoArr {
    
    /**
     * 求两个有序数组指定范围内的上中位数
     * 
     * @param a1 第一个有序数组
     * @param s1 第一个数组的起始位置
     * @param e1 第一个数组的结束位置
     * @param a2 第二个有序数组
     * @param s2 第二个数组的起始位置
     * @param e2 第二个数组的结束位置
     * @return 两个数组范围内元素的上中位数
     * 
     * 上中位数定义：
     * 将两个数组的指定范围合并排序后，位于中间偏上位置的元素
     * 如果总长度为奇数，返回正中间的元素
     * 如果总长度为偶数，返回中间两个元素中较小的那个
     * 
     * 算法核心：
     * 通过比较两个数组的中位数，逐步缩小搜索范围
     * 保证每次都能排除一部分不可能的元素
     */
    private static int upMid(int[] a1, int s1, int e1, int[] a2, int s2, int e2) {
        int mid1 = 0, mid2 = 0;
        
        // 二分查找过程
        while (s1 < e1) {
            mid1 = (s1 + e1) / 2;  // 第一个数组的中位数位置
            mid2 = (s2 + e2) / 2;  // 第二个数组的中位数位置
            
            // 如果两个中位数相等，直接返回
            if (a1[mid1] == a2[mid2]) {
                return a1[mid1];
            }
            
            // 判断当前范围的长度是奇数还是偶数
            if (((e1 - s1 + 1) & 1) == 1) {
                // 奇数长度的特殊处理
                if (a1[mid1] > a2[mid2]) {
                    // a1[mid1] > a2[mid2]的情况
                    if (a2[mid2] >= a1[mid1 - 1]) {
                        return a2[mid2];
                    }
                    // 缩小搜索范围：排除a1的右半部分和a2的左半部分
                    e1 = mid1 - 1;
                    s2 = mid2 + 1;
                } else {
                    // a1[mid1] < a2[mid2]的情况
                    if (a1[mid1] >= a2[mid2 - 1]) {
                        return a1[mid1];
                    }
                    // 缩小搜索范围：排除a2的右半部分和a1的左半部分
                    e2 = mid2 - 1;
                    s1 = mid1 + 1;
                }
            } else {
                // 偶数长度的处理
                if (a1[mid1] > a2[mid2]) {
                    // 排除a1的右半部分和a2的左半部分
                    e1 = mid1;
                    s2 = mid2 + 1;
                } else {
                    // 排除a2的右半部分和a1的左半部分
                    e2 = mid2;
                    s1 = mid1 + 1;
                }
            }
        }
        
        // 搜索范围缩小到单个元素时，返回较小值
        return Math.min(a1[s1], a2[s2]);
    }

    /**
     * 在两个有序数组中找第k小的元素
     * 
     * @param arr1 第一个有序数组
     * @param arr2 第二个有序数组
     * @param k 要找的第k小元素（1-based）
     * @return 第k小的元素，如果k不合法返回-1
     * 
     * 算法分类讨论：
     * 设短数组长度为s，长数组长度为l
     * 1. k ≤ s：第k小元素一定在两个数组的前k个元素中
     * 2. k > l：需要排除一些较小的元素
     * 3. s < k ≤ l：在中间范围内寻找
     */
    public static int kth(int[] arr1, int[] arr2, int k) {
        // 参数合法性检查
        if (arr1 == null || arr2 == null) {
            return -1;
        }
        if (k < 1 || k > arr1.length + arr2.length) {
            return -1;
        }
        
        // 确定长短数组，简化后续处理
        int[] longs = arr1.length >= arr2.length ? arr1 : arr2;
        int[] shorts = arr1.length < arr2.length ? arr1 : arr2;
        int l = longs.length;   // 长数组长度
        int s = shorts.length;  // 短数组长度
        
        if (k <= s) {
            // 情况1：k ≤ s
            // 第k小元素一定在两个数组的前k个元素中
            return upMid(shorts, 0, k - 1, longs, 0, k - 1);
        }
        
        if (k > l) {
            // 情况2：k > l
            // 需要考虑是否能直接确定答案
            
            // 如果短数组的第(k-l-1)个元素 ≥ 长数组的最大值
            // 说明长数组全部元素都比第k小元素小
            if (shorts[k - l - 1] >= longs[l - 1]) {
                return shorts[k - l - 1];
            }
            
            // 如果长数组的第(k-s-1)个元素 ≥ 短数组的最大值
            // 说明短数组全部元素都比第k小元素小
            if (longs[k - s - 1] >= shorts[s - 1]) {
                return longs[k - s - 1];
            }
            
            // 在剩余范围内寻找上中位数
            return upMid(shorts, k - l, s - 1, longs, k - s, l - 1);
        }
        
        // 情况3：s < k ≤ l
        // 如果长数组的第(k-s-1)个元素 ≥ 短数组的最大值
        // 说明短数组全部元素都比第k小元素小
        if (longs[k - s - 1] >= shorts[s - 1]) {
            return longs[k - s - 1];
        }
        
        // 在指定范围内寻找上中位数
        return upMid(shorts, 0, s - 1, longs, k - s, k - 1);
    }

    /**
     * 生成随机有序数组的辅助方法
     * 
     * @param len 数组长度
     * @param maxVal 最大值
     * @return 随机生成的有序数组
     */
    private static int[] randomSortedArr(int len, int maxVal) {
        int[] res = new int[len];
        for (int i = 0; i < len; i++) {
            res[i] = (int) ((maxVal + 1) * Math.random());
        }
        Arrays.sort(res);
        return res;
    }

    /**
     * 合并两个数组并排序（用于验证结果）
     * 
     * @param arr1 第一个数组
     * @param arr2 第二个数组
     * @return 合并后的有序数组
     */
    private static int[] sortAll(int[] arr1, int[] arr2) {
        if (arr1 == null || arr2 == null) {
            return null;
        }
        
        int[] all = new int[arr1.length + arr2.length];
        int idx = 0;
        
        // 复制第一个数组
        for (int i = 0; i < arr1.length; i++) {
            all[idx++] = arr1[i];
        }
        
        // 复制第二个数组
        for (int i = 0; i < arr2.length; i++) {
            all[idx++] = arr2[i];
        }
        
        // 排序
        Arrays.sort(all);
        return all;
    }

    /**
     * 打印数组的辅助方法
     * 
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        for (int i = 0 ; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        int len1 = 10;
        int len2 = 23;
        int maxVal1 = 20;
        int maxVal2 = 100;
        
        // 生成测试数据
        int[] arr1 = randomSortedArr(len1, maxVal1);
        int[] arr2 = randomSortedArr(len2, maxVal2);
        
        System.out.println("数组1：");
        print(arr1);
        System.out.println("数组2：");
        print(arr2);
        
        // 生成合并后的排序数组用于验证
        int[] all = sortAll(arr1, arr2);
        System.out.println("合并排序后：");
        print(all);
        
        int k = 17;
        System.out.println("\n寻找第" + k + "小的元素：");
        System.out.println("算法结果: " + kth(arr1, arr2, k));
        System.out.println("验证结果: " + all[k - 1]);
        
        // 测试边界情况
        System.out.println("\n边界测试：");
        System.out.println("第1小: " + kth(arr1, arr2, 1) + " vs " + all[0]);
        System.out.println("最大值: " + kth(arr1, arr2, len1 + len2) + " vs " + all[len1 + len2 - 1]);
        
        // 测试多个k值
        System.out.println("\n多值测试：");
        for (int i = 1; i <= Math.min(10, len1 + len2); i++) {
            int result = kth(arr1, arr2, i);
            int expected = all[i - 1];
            System.out.println("k=" + i + ": " + result + (result == expected ? " ✓" : " ✗"));
        }
    }
}
