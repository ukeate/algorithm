package basic.c32;

/**
 * 数组划分使左右部分最大值差值最大的问题
 * 
 * 问题描述：给定一个数组，将其划分为左右两个非空部分，
 * 使得左部分的最大值与右部分的最大值的差值绝对值最大
 * 
 * 解题方案：提供三种不同复杂度的解决方法
 * 1. 暴力法：枚举所有划分位置，分别计算左右最大值 - O(N²)
 * 2. 预处理法：预先计算左右最大值数组 - O(N)
 * 3. 数学优化法：利用数学性质直接计算 - O(N)
 * 
 * 关键洞察：最优划分位置一定是使得其中一边只包含数组的第一个或最后一个元素
 * 因为这样可以让另一边包含数组的最大值，从而获得最大的差值
 */
public class MaxLeftRightABS {

    /**
     * 方法1：暴力解法
     * 枚举所有可能的划分位置，计算每种划分的左右最大值差
     * 时间复杂度：O(N²)
     * 空间复杂度：O(1)
     * 
     * @param arr 输入数组
     * @return 左右最大值差的最大值
     */
    public static int max1(int[] arr) {
        int res = Integer.MIN_VALUE;
        int maxLeft = 0;
        int maxRight = 0;
        
        // 枚举所有可能的划分位置
        for (int i = 0; i < arr.length - 1; i++) {
            // 计算左半部分的最大值 [0...i]
            maxLeft = Integer.MIN_VALUE;
            for (int j = 0; j <= i; j++) {
                maxLeft = Math.max(arr[j], maxLeft);
            }
            
            // 计算右半部分的最大值 [i+1...n-1]
            maxRight = Integer.MIN_VALUE;
            for (int j = i + 1; j < arr.length; j++) {
                maxRight = Math.max(arr[j], maxRight);
            }
            
            // 更新最大差值
            res = Math.max(Math.abs(maxLeft - maxRight), res);
        }
        
        return res;
    }

    /**
     * 方法2：预处理优化法
     * 预先计算每个位置的左侧最大值和右侧最大值，避免重复计算
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
     * 
     * @param arr 输入数组
     * @return 左右最大值差的最大值
     */
    public static int max2(int[] arr) {
        // lm[i]表示从0到i位置的最大值
        int[] lm = new int[arr.length];
        // rm[i]表示从i到末尾位置的最大值
        int[] rm = new int[arr.length];
        
        // 预处理左侧最大值数组
        lm[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            lm[i] = Math.max(lm[i - 1], arr[i]);
        }
        
        // 预处理右侧最大值数组
        rm[arr.length - 1] = arr[arr.length - 1];
        for (int i = arr.length - 2; i >= 0; i--) {
            rm[i] = Math.max(rm[i + 1], arr[i]);
        }
        
        // 计算所有划分位置的最大差值
        int max = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            max = Math.max(max, Math.abs(lm[i] - rm[i + 1]));
        }
        
        return max;
    }

    /**
     * 方法3：数学优化法（最优解）
     * 关键洞察：最优答案一定是数组最大值减去首尾元素的较小值
     * 
     * 证明思路：
     * - 如果左部分只包含首元素，右部分包含其余元素（包含最大值）
     * - 如果右部分只包含尾元素，左部分包含其余元素（包含最大值）
     * - 这两种情况中必有一种能达到最优解
     * 
     * 时间复杂度：O(N)
     * 空间复杂度：O(1)
     * 
     * @param arr 输入数组
     * @return 左右最大值差的最大值
     */
    public static int max3(int[] arr) {
        // 找出数组的最大值
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            max = Math.max(arr[i], max);
        }
        
        // 最优解 = 最大值 - min(首元素, 尾元素)
        return max - Math.min(arr[0], arr[arr.length - 1]);
    }

    private static int[] randomArr(int len) {
        int[] arr = new int[len];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 1000) - 499;
        }
        return arr;
    }

    public static void main(String[] args) {
        int[] arr = randomArr(200);
        System.out.println(max1(arr));
        System.out.println(max2(arr));
        System.out.println(max3(arr));
    }

}
