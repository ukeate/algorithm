package giant.c1;

import java.util.Arrays;

/**
 * 绳子覆盖最多点数问题
 * 
 * 问题描述：
 * 在数轴上有若干个点，给定一根长度为l的绳子，绳子可以任意放置在数轴上。
 * 求绳子最多能覆盖多少个点。
 * 
 * 核心思想：
 * 对于每个点作为绳子右端点时，计算能覆盖的最多点数。
 * 使用滑动窗口或二分查找来优化查找过程。
 * 
 * 算法分析：
 * 1. 二分查找方法：对每个点，二分查找绳子左端点能到达的最左位置
 * 2. 滑动窗口方法：使用双指针维护一个长度不超过l的窗口
 * 
 * 时间复杂度：
 * - max1: O(n*log(n))，每个点都进行一次二分查找
 * - max2: O(n)，双指针滑动窗口
 * - maxSure: O(n^2)，暴力方法用于对拍验证
 * 
 * 空间复杂度：O(1)
 */
public class CordCoverMaxPoint {
    /**
     * 二分查找：在arr[0...r]中找到第一个>=val的位置
     * 这是一个经典的"找左边界"二分查找模板
     * @param arr 有序数组
     * @param r 查找的右边界（包含）
     * @param val 目标值
     * @return 第一个>=val的位置索引，如果不存在则返回r+1的位置
     */
    private static int nearestIdx(int[] arr, int r, int val) {
        int l = 0;
        int idx = r; // 默认返回r，表示没找到合适的位置
        while (l <= r) {
            int mid = l + ((r - l) >> 1); // 防止溢出的中点计算
            if (arr[mid] >= val) {
                idx = mid;     // 记录可能的答案
                r = mid - 1;   // 继续向左查找更小的满足条件的位置
            } else {
                l = mid + 1;   // 当前值太小，向右查找
            }
        }
        return idx;
    }

    /**
     * 二分查找解法：对每个点作为绳子右端点，二分查找左端点位置
     * 
     * 算法思路：
     * 1. 遍历每个点i，将其作为绳子的右端点
     * 2. 绳子左端点最左可以到达arr[i] - l的位置
     * 3. 使用二分查找找到这个位置对应的点的索引
     * 4. 计算从该索引到i能覆盖的点数
     * 
     * @param arr 点的位置数组（已排序）
     * @param l 绳子长度
     * @return 绳子能覆盖的最多点数
     */
    public static int max1(int[] arr, int l) {
        int res = 1; // 至少能覆盖一个点
        for (int i = 0; i < arr.length; i++) {
            // 以arr[i]作为绳子右端点，左端点最左到arr[i] - l
            int nearest = nearestIdx(arr, i, arr[i] - l);
            // 从nearest到i的点都能被覆盖
            res = Math.max(res, i - nearest + 1);
        }
        return res;
    }

    /**
     * 滑动窗口解法：使用双指针维护一个长度不超过l的窗口
     * 
     * 算法思路：
     * 1. 使用left和right两个指针维护一个窗口
     * 2. right指针不断右移，直到窗口长度超过l
     * 3. 记录窗口内的最大点数，然后left指针右移
     * 4. 重复直到遍历完所有点
     * 
     * 优势：时间复杂度O(n)，每个点最多被访问两次
     * 
     * @param arr 点的位置数组（已排序）
     * @param l 绳子长度
     * @return 绳子能覆盖的最多点数
     */
    public static int max2(int[] arr, int l) {
        int left = 0;  // 窗口左边界
        int right = 0; // 窗口右边界
        int n = arr.length;
        int max = 0;
        
        while (left < n) {
            // 扩展右边界，直到窗口长度超过l
            while (right < n && arr[right] - arr[left] <= l) {
                right++;
            }
            // 当前窗口[left, right)内的点数为right - left
            max = Math.max(max, right - (left++));
            // left++后继续处理下一个窗口
        }
        return max;
    }

    /**
     * 暴力解法：用于对拍验证其他算法的正确性
     * 
     * 算法思路：
     * 对每个点i作为绳子右端点，向左扫描找到最远能覆盖的点
     * 
     * @param arr 点的位置数组（已排序）
     * @param l 绳子长度
     * @return 绳子能覆盖的最多点数
     */
    public static int maxSure(int[] arr, int l) {
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            int pre = i - 1;
            // 向左扫描，找到绳子能覆盖的最左边的点
            while (pre >= 0 && arr[i] - arr[pre] <= l) {
                pre--;
            }
            // pre+1是最左边能覆盖的点，到i共有i-pre个点
            max = Math.max(max, i - pre);
        }
        return max;
    }

    /**
     * 生成随机测试数组
     * @param maxLen 数组最大长度
     * @param max 数组元素最大值
     * @return 排序后的随机数组
     */
    private static int[] randomArr(int maxLen, int max) {
        int[] ans = new int[(int) (maxLen * Math.random()) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) ((max + 1) * Math.random());
        }
        Arrays.sort(ans); // 点的位置需要有序
        return ans;
    }

    /**
     * 测试方法：对拍验证三种算法的正确性
     */
    public static void main(String[] args) {
        int times = 100000;  // 测试次数
        int maxLen = 100;    // 最大数组长度
        int maxVal = 1000;   // 最大点位置值
        
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int l = (int) ((maxVal + 1) * Math.random()); // 随机绳子长度
            int[] arr = randomArr(maxLen, maxVal);        // 随机点位置数组
            
            // 三种方法对拍
            int ans1 = max1(arr, l);    // 二分查找方法
            int ans2 = max2(arr, l);    // 滑动窗口方法
            int ans3 = maxSure(arr, l); // 暴力方法
            
            if (ans1 != ans2 || ans2 != ans3) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
