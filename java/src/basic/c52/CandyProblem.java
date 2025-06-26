package basic.c52;

/**
 * 分糖果问题
 * 
 * 问题描述：
 * 老师想给孩子们分发糖果，有N个孩子站成了一条直线，老师会根据每个孩子的得分来分发糖果。
 * 分发规则：
 * 1. 每个孩子至少分到一个糖果
 * 2. 相邻的孩子中，得分高的孩子必须得到更多的糖果
 * 3. 得分相等时没有特殊要求
 * 
 * 求满足以上条件的最少糖果总数。
 * 
 * 算法思路：
 * 1. 左右扫描法：分别从左到右和从右到左扫描，确保左右约束都满足
 * 2. 单次扫描法：识别上坡、下坡和平台区段，分别处理
 * 3. 考虑相等得分情况的优化版本
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n) / O(1)
 * 
 * LeetCode: https://leetcode.com/problems/candy/
 * 
 * @author 算法学习
 */
public class CandyProblem {
    
    /**
     * 方法1：左右扫描法
     * 分别处理左边界约束和右边界约束，取最大值
     * 
     * @param arr 孩子得分数组
     * @return 最少糖果数
     */
    public static int candy1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        
        // 左扫描：处理左邻居约束
        int[] left = new int[n];
        for (int i = 1; i < n; i++) {
            if (arr[i - 1] < arr[i]) {
                left[i] = left[i - 1] + 1;
            }
        }
        
        // 右扫描：处理右邻居约束
        int[] right = new int[n];
        for (int i = n - 2; i >= 0; i--) {
            if (arr[i] > arr[i + 1]) {
                right[i] = right[i + 1] + 1;
            }
        }
        
        // 取左右约束的最大值，并加上基础糖果数
        int ans = 0;
        for (int i = 0; i < n; i++) {
            ans += Math.max(left[i], right[i]);
        }
        
        return ans + n;  // 每个孩子至少1个糖果
    }

    /**
     * 找到下一个极小值位置（递减序列的结束位置）
     * 
     * @param arr 数组
     * @param start 起始位置
     * @return 下一个极小值位置
     */
    private static int nextMinIdx1(int[] arr, int start) {
        for (int i = start; i < arr.length - 1; i++) {
            if (arr[i] <= arr[i + 1]) {
                return i;
            }
        }
        return arr.length - 1;
    }

    /**
     * 计算递减序列需要的糖果数
     * 对于长度为n的递减序列，需要n + (n-1) + ... + 1 = n*(n+1)/2个糖果
     * 
     * @param arr 数组
     * @param left 左边界
     * @param right 右边界
     * @return 该区间需要的糖果数
     */
    private static int rightCands(int[] arr, int left, int right) {
        int n = right - left + 1;
        return (n + 1) * n / 2;
    }

    /**
     * 方法2：单次扫描优化法
     * 识别上坡、下坡和平台，分别处理
     * 
     * @param arr 孩子得分数组
     * @return 最少糖果数
     */
    public static int candy2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        // 处理第一个下坡区段
        int idx = nextMinIdx1(arr, 0);
        int res = rightCands(arr, 0, idx++);
        int lBase = 1;  // 左坡基数
        
        while (idx < arr.length) {
            if (arr[idx] > arr[idx - 1]) {
                // 上坡：糖果数递增
                res += ++lBase;
                idx++;
            } else if (arr[idx] < arr[idx - 1]) {
                // 下坡：找到下坡结束位置
                int next = nextMinIdx1(arr, idx - 1);
                int rCands = rightCands(arr, idx - 1, next++);
                int rBase = next - idx + 1;  // 右坡基数
                
                // 避免在连接处重复计算
                res += rCands - (rBase > lBase ? lBase : rBase);
                lBase = 1;
                idx = next;
            } else {
                // 平台：得分相等，给最少糖果
                res += 1;
                lBase = 1;
                idx++;
            }
        }
        
        return res;
    }

    /**
     * 找到下一个严格递增开始的位置
     * 
     * @param arr 数组
     * @param start 起始位置
     * @return 下一个递增位置
     */
    private static int nextMinIdx2(int[] arr, int start) {
        for (int i = start; i < arr.length - 1; i++) {
            if (arr[i] < arr[i + 1]) {
                return i;
            }
        }
        return arr.length - 1;
    }

    /**
     * 计算考虑相等情况的递减序列糖果数和基数
     * 
     * @param arr 数组
     * @param left 左边界
     * @param right 右边界
     * @return [糖果数, 基数]
     */
    private static int[] rightCandsAndBase(int[] arr, int left, int right) {
        int base = 1;
        int cands = 1;
        
        // 从右到左计算
        for (int i = right - 1; i >= left; i--) {
            if (arr[i] == arr[i + 1]) {
                // 相等得分：糖果数相等
                cands += base;
            } else {
                // 得分更高：糖果数+1
                cands += ++base;
            }
        }
        
        return new int[]{cands, base};
    }

    /**
     * 方法3：处理相等得分的优化版本
     * 相等得分的孩子可以得到相同数量的糖果
     * 
     * @param arr 孩子得分数组
     * @return 最少糖果数
     */
    public static int candy3(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        // 处理第一个下坡区段
        int idx = nextMinIdx2(arr, 0);
        int[] data = rightCandsAndBase(arr, 0, idx++);
        int res = data[0];
        int lBase = 1;      // 左边基数
        int same = 1;       // 相同得分的连续个数
        
        while (idx < arr.length) {
            if (arr[idx] > arr[idx - 1]) {
                // 上坡
                res += ++lBase;
                same = 1;
                idx++;
            } else if (arr[idx] < arr[idx - 1]) {
                // 下坡
                int next = nextMinIdx2(arr, idx - 1);
                data = rightCandsAndBase(arr, idx - 1, next++);
                
                if (data[1] <= lBase) {
                    // 右坡基数不超过左坡基数
                    res += data[0] - data[1];
                } else {
                    // 右坡基数超过左坡基数，需要调整
                    res += -lBase * same + data[0] - data[1] + data[1] * same;
                }
                
                idx = next;
                lBase = 1;
                same = 1;
            } else {
                // 平台：得分相等
                res += lBase;
                same++;
                idx++;
            }
        }
        
        return res;
    }

    /**
     * 测试方法
     * 验证三种分糖果算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 分糖果问题测试 ===");
        
        // 测试用例
        int[] arr = {3, 0, 5, 5, 4, 4, 0};
        System.out.println("孩子得分: [3, 0, 5, 5, 4, 4, 0]");
        
        System.out.println("左右扫描法结果: " + candy1(arr));
        System.out.println("单次扫描法结果: " + candy2(arr));
        System.out.println("相等优化版结果: " + candy3(arr));
        
        // 更多测试用例
        System.out.println("\n=== 更多测试用例 ===");
        
        testCase(new int[]{1, 0, 2});
        testCase(new int[]{1, 2, 2});
        testCase(new int[]{1, 3, 2, 2, 1});
        testCase(new int[]{1, 2, 3, 4, 5});
        testCase(new int[]{5, 4, 3, 2, 1});
        testCase(new int[]{1, 1, 1, 1, 1});
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("方法1 - 左右扫描: 时间O(n), 空间O(n), 最直观易懂");
        System.out.println("方法2 - 单次扫描: 时间O(n), 空间O(1), 空间优化");
        System.out.println("方法3 - 相等优化: 时间O(n), 空间O(1), 处理相等情况");
    }
    
    /**
     * 测试用例辅助方法
     * 
     * @param arr 测试数组
     */
    private static void testCase(int[] arr) {
        System.out.print("输入: [");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) System.out.print(", ");
        }
        System.out.print("] => ");
        
        int result1 = candy1(arr.clone());
        int result2 = candy2(arr.clone());
        int result3 = candy3(arr.clone());
        
        System.out.printf("方法1:%d, 方法2:%d, 方法3:%d", result1, result2, result3);
        if (result1 == result2 && result2 == result3) {
            System.out.println(" ✓");
        } else {
            System.out.println(" ✗");
        }
    }
}
