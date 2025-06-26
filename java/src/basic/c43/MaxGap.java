package basic.c43;

import java.util.Arrays;

/**
 * 最大间隙问题（Maximum Gap）
 * 
 * 问题描述：
 * 给定一个无序数组，如果将数组排序后，相邻元素之间的最大差值是多少？
 * 要求：尽量用线性时间复杂度解决
 * 
 * 算法思路：
 * 方法1：暴力法 - 直接排序后遍历，时间复杂度O(N*logN)
 * 方法2：桶排序优化 - 利用抽屉原理，时间复杂度O(N)
 * 
 * 核心洞察（抽屉原理）：
 * 如果有N个数，值域为[min, max]，分成N+1个桶
 * 桶的大小为(max-min)/N，则：
 * 1. 桶内元素的最大差值 < 桶大小
 * 2. 最大间隙必定出现在相邻非空桶之间
 * 3. 具体是前一个桶的最大值到后一个桶的最小值
 * 
 * 时间复杂度：O(N)
 * 空间复杂度：O(N)
 * 
 * @author 算法学习
 */
public class MaxGap {
    
    /**
     * 计算数字应该放入哪个桶
     * 
     * @param num 待分桶的数字
     * @param len 桶的数量（数组长度）
     * @param min 数组最小值
     * @param max 数组最大值
     * @return 桶的索引
     * 
     * 桶映射公式：bucket = (num - min) * len / (max - min)
     * 保证：min对应桶0，max对应桶len-1（会被特殊处理到桶len-1）
     */
    private static int bucket(long num, long len, long min, long max) {
        return (int) ((num - min) * len / (max - min));
    }

    /**
     * 方法2：桶排序优化的线性时间算法
     * 
     * @param arr 输入数组
     * @return 排序后相邻元素的最大差值
     * 
     * 算法步骤：
     * 1. 找到数组的最小值和最大值
     * 2. 将数组元素分配到N+1个桶中
     * 3. 记录每个桶的最小值和最大值
     * 4. 遍历相邻非空桶，计算最大间隙
     * 
     * 关键优化：
     * 由于最大间隙必定在桶间产生，所以只需记录每个桶的min和max
     */
    public static int max(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int len = arr.length;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        
        // 第一遍遍历：找到最小值和最大值
        for (int i = 0; i < len; i++) {
            min = Math.min(min, arr[i]);
            max = Math.max(max, arr[i]);
        }
        
        // 特殊情况：所有数字相同
        if (min == max) {
            return 0;
        }
        
        // 准备N+1个桶的信息
        boolean[] hasNum = new boolean[len + 1]; // 标记桶是否有数字
        int[] maxs = new int[len + 1];           // 每个桶的最大值
        int[] mins = new int[len + 1];           // 每个桶的最小值
        
        // 第二遍遍历：将数字分配到桶中
        int bid = 0; // bucket id
        for (int i = 0; i < len; i++) {
            bid = bucket(arr[i], len, min, max);
            
            // 更新桶的最小值和最大值
            mins[bid] = hasNum[bid] ? Math.min(mins[bid], arr[i]) : arr[i];
            maxs[bid] = hasNum[bid] ? Math.max(maxs[bid], arr[i]) : arr[i];
            hasNum[bid] = true;
        }
        
        // 第三遍遍历：计算相邻非空桶之间的最大间隙
        int res = 0;
        int lastMax = maxs[0]; // 上一个非空桶的最大值
        
        for (int i = 1; i <= len; i++) {
            if (hasNum[i]) {
                // 当前桶非空，计算间隙：当前桶最小值 - 上一个桶最大值
                res = Math.max(res, mins[i] - lastMax);
                lastMax = maxs[i]; // 更新上一个桶的最大值
            }
        }
        
        return res;
    }

    /**
     * 方法1：暴力解法（用于验证正确性）
     * 直接排序后找相邻元素的最大差值
     * 
     * @param arr 输入数组
     * @return 排序后相邻元素的最大差值
     * 
     * 时间复杂度：O(N*logN)
     * 空间复杂度：O(1)
     */
    public static int maxSure(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        // 排序数组
        Arrays.sort(arr);
        
        // 遍历相邻元素，找最大差值
        int gap = Integer.MIN_VALUE;
        for (int i = 1; i < arr.length; i++) {
            gap = Math.max(arr[i] - arr[i - 1], gap);
        }
        return gap;
    }

    /**
     * 生成随机测试数组
     * 
     * @param maxLen 数组最大长度
     * @param maxVal 数值最大绝对值
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            // 生成[-maxVal, maxVal]范围内的随机数
            arr[i] = (int) ((maxVal + 1) * Math.random() - (int) ((maxVal + 1) * Math.random()));
        }
        return arr;
    }

    /**
     * 复制数组
     * 
     * @param arr 原数组
     * @return 复制的数组
     */
    private static int[] copy(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        int times = 500000;
        int maxLen = 100;
        int maxVal = 100;
        
        System.out.println("测试开始...");
        for (int i = 0; i < times; i++) {
            int[] arr1 = randomArr(maxLen, maxVal);
            int[] arr2 = copy(arr1);
            
            // 比较两种算法的结果
            if (max(arr1) != maxSure(arr2)) {
                System.out.println("算法错误!");
                System.out.println("原数组: " + Arrays.toString(arr1));
                System.out.println("桶排序结果: " + max(copy(arr1)));
                System.out.println("暴力法结果: " + maxSure(copy(arr1)));
                break;
            }
        }
        System.out.println("测试结束");
        
        // 手动测试示例
        System.out.println("\n手动测试:");
        int[] test = {3, 6, 9, 1};
        System.out.println("数组: " + Arrays.toString(test));
        System.out.println("桶排序结果: " + max(copy(test)));
        System.out.println("暴力法结果: " + maxSure(copy(test)));
    }
}
