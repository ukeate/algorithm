package giant.c3;

import java.util.Arrays;

/**
 * 最大配对数量问题
 * 
 * 问题描述：
 * 给定一个数组arr，代表每个人的能力值。再给定一个非负数k。
 * 如果两个人能力差值正好为k，那么可以凑在一起比赛，一局比赛只有两个人。
 * 返回最多可以同时有多少场比赛。
 * 
 * 关键约束：
 * 1. 两人能力差值必须正好等于k
 * 2. 每个人只能参加一场比赛
 * 3. 目标是最大化比赛场数
 * 
 * 解法对比：
 * 1. 方法1：暴力递归 - 枚举所有排列，时间O(n!)，用于验证
 * 2. 方法2：贪心算法 - 排序后双指针，时间O(n*log n)，实用解法
 * 
 * 核心思想：
 * 将问题转化为在有序数组中寻找差值为k的配对，使用双指针技术高效求解
 * 
 * @author algorithm learning
 */
public class MaxPairNumber {
    
    /**
     * 辅助方法：交换数组中两个元素的位置
     * @param arr 数组
     * @param i 位置i
     * @param j 位置j
     */
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 递归生成所有排列并检查最大配对数
     * 
     * 算法思路：
     * 1. 通过全排列枚举所有可能的人员安排
     * 2. 对每种安排，相邻的人尝试配对（arr[i], arr[i+1]）
     * 3. 统计能够成功配对的数量，返回最大值
     * 
     * 配对规则：
     * - 按照排列顺序，第0和第1个人一组，第2和第3个人一组，依此类推
     * - 只有当两人能力差值等于k时才能配对成功
     * 
     * 时间复杂度：O(n! * n)，需要枚举所有排列
     * 空间复杂度：O(n)，递归栈深度
     * 
     * @param arr 能力值数组
     * @param idx 当前处理的位置
     * @param k 要求的能力差值
     * @return 当前排列下的最大配对数
     */
    private static int process1(int[] arr, int idx, int k) {
        int ans = 0;
        
        // 递归终止条件：已经处理完所有位置，开始计算配对数
        if (idx == arr.length) {
            // 相邻的人两两配对，检查能力差值是否等于k
            for (int i = 1; i < arr.length; i += 2) {
                if (arr[i] - arr[i - 1] == k) {
                    ans++;
                }
            }
        } else {
            // 递归过程：尝试将每个剩余的人放在当前位置idx
            for (int r = idx; r < arr.length; r++) {
                swap(arr, idx, r);                              // 选择第r个人放在位置idx
                ans = Math.max(ans, process1(arr, idx + 1, k)); // 递归处理下一个位置
                swap(arr, idx, r);                              // 回溯，恢复原状态
            }
        }
        return ans;
    }

    /**
     * 方法1：暴力递归解法（用于验证正确性）
     * @param arr 能力值数组
     * @param k 能力差值要求
     * @return 最大配对数
     */
    public static int max1(int[] arr, int k) {
        if (k < 0) {
            return -1; // k必须非负
        }
        return process1(arr, 0, k);
    }

    /**
     * 方法2：贪心算法优化解法
     * 
     * 算法思路：
     * 1. 对数组进行排序，使相近能力值的人相邻
     * 2. 使用双指针技术，左指针l指向较小能力值，右指针r指向较大能力值
     * 3. 根据能力差值调整指针位置：
     *    - 差值等于k：配对成功，两指针都移动
     *    - 差值小于k：右指针右移，寻找更大的能力值
     *    - 差值大于k：左指针右移，寻找更大的基准能力值
     * 4. 使用标记数组避免重复使用已配对的人
     * 
     * 贪心策略的正确性：
     * 排序后，对于每个左指针位置的人，第一个能配对的右指针位置就是最优选择。
     * 因为如果不选择这个配对，后面也不会有更好的机会了。
     * 
     * 时间复杂度：O(n*log n)，主要是排序的复杂度
     * 空间复杂度：O(n)，用于标记数组
     * 
     * @param arr 能力值数组
     * @param k 能力差值要求
     * @return 最大配对数
     */
    public static int max2(int[] arr, int k) {
        // 边界条件检查
        if (k < 0 || arr == null || arr.length < 2) {
            return 0;
        }
        
        // 排序：使相近能力值的人相邻
        Arrays.sort(arr);
        
        int ans = 0;                    // 成功配对的数量
        int n = arr.length;
        int l = 0, r = 0;              // 双指针：l指向能力较低的人，r指向能力较高的人
        boolean[] usedR = new boolean[n]; // 标记数组：记录右指针位置的人是否已被配对
        
        // 双指针扫描
        while (l < n && r < n) {
            // 跳过已经被配对的左指针位置
            if (usedR[l]) {
                l++;
            } 
            // 确保右指针在左指针右侧
            else if (l >= r) {
                r++;
            } 
            // 核心配对逻辑
            else {
                int distance = arr[r] - arr[l]; // 计算能力差值
                
                if (distance == k) {
                    // 配对成功：记录结果，标记右指针位置，移动双指针
                    ans++;
                    usedR[r++] = true; // 标记r位置的人已配对，r右移
                    l++;               // l右移寻找下一个待配对的人
                } 
                else if (distance < k) {
                    // 差值太小：右指针右移，寻找能力更强的人
                    r++;
                } 
                else {
                    // 差值太大：左指针右移，提高基准能力值
                    l++;
                }
            }
        }
        
        return ans;
    }

    /**
     * 生成随机测试数组
     * @param len 数组长度
     * @param val 能力值上限
     * @return 随机数组
     */
    private static int[] randomArr(int len, int val) {
        int[] arr = new int[len];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * val);
        }
        return arr;
    }

    /**
     * 打印数组内容
     * @param arr 待打印的数组
     */
    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    /**
     * 复制数组
     * @param arr 原数组
     * @return 副本数组
     */
    private static int[] copy(int[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    /**
     * 测试方法：对拍验证两种算法的正确性
     */
    public static void main(String[] args) {
        int times = 1000;    // 测试次数
        int maxLen = 10;     // 最大数组长度
        int maxVal = 20;     // 最大能力值
        int maxK = 5;        // 最大k值
        
        System.out.println("=== 最大配对数量算法测试 ===");
        
        // 手动测试用例
        int[] testArr = {1, 3, 5, 7, 9, 11};
        int testK = 2;
        System.out.println("手动测试用例:");
        System.out.print("数组: ");
        print(testArr);
        System.out.println("k = " + testK);
        System.out.println("暴力解法结果: " + max1(copy(testArr), testK));
        System.out.println("贪心解法结果: " + max2(copy(testArr), testK));
        System.out.println("期望结果: 3 (配对: (1,3), (5,7), (9,11))");
        System.out.println();
        
        // 随机对拍测试
        System.out.println("开始随机对拍测试...");
        for (int i = 0; i < times; i++) {
            int n = (int) (Math.random() * (maxLen + 1));
            int[] arr = randomArr(n, maxVal);
            int[] arr1 = copy(arr);
            int[] arr2 = copy(arr);
            int k = (int) (Math.random() * (maxK + 1));
            
            int ans1 = max1(arr1, k);
            int ans2 = max2(arr2, k);
            
            if (ans1 != ans2) {
                System.out.println("发现错误!");
                System.out.print("数组: ");
                print(arr);
                System.out.println("k = " + k);
                System.out.println("暴力解法: " + ans1);
                System.out.println("贪心解法: " + ans2);
                break;
            }
        }
        System.out.println("对拍测试完成");
        
        System.out.println("\n=== 算法特点总结 ===");
        System.out.println("暴力解法: 枚举所有排列，时间复杂度O(n!)，仅用于验证");
        System.out.println("贪心解法: 排序+双指针，时间复杂度O(n*log n)，实际应用");
        System.out.println("关键洞察: 排序后的贪心选择策略是最优的");
    }
}
