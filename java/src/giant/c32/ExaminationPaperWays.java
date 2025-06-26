package giant.c32;

import java.util.Arrays;

/**
 * 试卷组卷方案数问题
 * 
 * 问题描述：
 * 给定一个数组arr，arr[i] = j表示第i号试题的难度为j。给定一个非负数M。
 * 要出一张卷子，对于任何相邻的两道题目，前一题的难度不能超过后一题的难度+M。
 * 即：对于相邻题目i和i+1，需要满足 difficulty[i] <= difficulty[i+1] + M
 * 返回所有可能的卷子种数。
 * 
 * 例如：
 * arr = [1, 3, 2], M = 1
 * 所有排列：[1,3,2], [1,2,3], [3,1,2], [3,2,1], [2,1,3], [2,3,1]
 * 检查约束：
 * [1,3,2]: 1<=3+1=4✓, 3<=2+1=3✓ → 有效
 * [1,2,3]: 1<=2+1=3✓, 2<=3+1=4✓ → 有效
 * [3,1,2]: 3<=1+1=2✗ → 无效
 * [3,2,1]: 3<=2+1=3✓, 2<=1+1=2✓ → 有效
 * [2,1,3]: 2<=1+1=2✓, 1<=3+1=4✓ → 有效
 * [2,3,1]: 2<=3+1=4✓, 3<=1+1=2✗ → 无效
 * 结果：4种有效方案
 * 
 * 解决方案：
 * 1. 方法1：暴力枚举 - 时间复杂度O(N! * N)
 * 2. 方法2：排序+数学分析 - 时间复杂度O(N*logN)
 * 3. 方法3：树状数组优化 - 时间复杂度O(N*logV)
 * 
 * 核心思想：
 * 先对难度排序，然后对每个位置计算可以选择的前序题目数量
 * 
 * 算法复杂度：
 * 时间复杂度：O(N*logV)
 * 空间复杂度：O(V)
 */
public class ExaminationPaperWays {
    
    /**
     * 交换数组中两个元素（暴力法使用）
     */
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 递归生成所有排列并检查约束（暴力法使用）
     * 
     * @param arr 题目难度数组
     * @param idx 当前考虑的位置
     * @param m 约束参数M
     * @return 满足约束的排列数量
     */
    private static int process(int[] arr, int idx, int m) {
        if (idx == arr.length) {
            // 检查当前排列是否满足约束
            for (int i = 1; i < idx; i++) {
                if (arr[i - 1] > arr[i] + m) {
                    return 0;  // 违反约束
                }
            }
            return 1;  // 满足约束
        }
        
        int ans = 0;
        // 尝试所有可能的交换
        for (int i = idx; i < arr.length; i++) {
            swap(arr, idx, i);
            ans += process(arr, idx + 1, m);
            swap(arr, idx, i);  // 回溯
        }
        return ans;
    }

    /**
     * 方法1：暴力枚举法
     * 
     * 算法思路：
     * 生成所有可能的排列，对每个排列检查是否满足约束条件
     * 
     * 算法特点：
     * - 思路直观，易于理解
     * - 时间复杂度极高，只适用于小规模数据验证
     * - 通过全排列保证了结果的正确性
     * 
     * 时间复杂度：O(N! * N)
     * 空间复杂度：O(N)，递归栈深度
     * 
     * @param arr 题目难度数组
     * @param m 约束参数
     * @return 满足约束的排列数量
     */
    public static int ways1(int[] arr, int m) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        return process(arr, 0, m);
    }

    /**
     * 二分查找：在有序数组中查找 >= t 的元素个数
     * 
     * @param arr 有序数组
     * @param r 搜索右边界
     * @param t 目标值
     * @return >= t 的元素个数
     */
    private static int num(int[] arr, int r, int t) {
        int i = 0, j = r, m = 0, a = r + 1;
        while (i <= j) {
            m = (i + j) / 2;
            if (arr[m] >= t) {
                a = m;      // 记录找到的位置
                j = m - 1;  // 继续在左半部分寻找更小的位置
            } else {
                i = m + 1;  // 在右半部分寻找
            }
        }
        return r - a + 1;  // 返回 >= t 的元素个数
    }

    /**
     * 方法2：排序+数学分析法
     * 
     * 算法思路：
     * 1. 对难度数组排序
     * 2. 对于排序后的每个位置i，计算可以放在它前面的题目数量
     * 3. 利用乘法原理计算总方案数
     * 
     * 核心洞察：
     * - 排序后，约束变为：前一题难度 <= 当前题难度 + M
     * - 对于位置i，可以选择的前序题目是那些难度 >= arr[i] - M 的题目
     * - 使用乘法原理：总方案数 = ∏(每个位置的选择数量)
     * 
     * 数学证明：
     * 设排序后数组为 a[0] <= a[1] <= ... <= a[n-1]
     * 对于位置i，前面已经放了i个题目，还需要从剩下的题目中选择
     * 约束条件：选择的题目难度 >= a[i] - M
     * 因此选择数量 = 位置i前面满足条件的题目数量 + 1
     * 
     * 时间复杂度：O(N*logN)，排序O(NlogN) + N次二分查找O(NlogN)
     * 空间复杂度：O(1)
     * 
     * @param arr 题目难度数组
     * @param m 约束参数
     * @return 满足约束的排列数量
     */
    public static int ways2(int[] arr, int m) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        Arrays.sort(arr);  // 排序是关键步骤
        int all = 1;
        
        for (int i = 1; i < arr.length; i++) {
            // 对于位置i，计算前面可以选择的题目数量
            // 需要满足：前序题目难度 >= arr[i] - m
            int choices = num(arr, i - 1, arr[i] - m) + 1;
            all = all * choices;
        }
        return all;
    }

    /**
     * 树状数组（Fenwick Tree）实现
     * 用于高效地进行单点更新和前缀和查询
     */
    private static class IndexTree {
        private int[] tree;  // 树状数组
        private int n;       // 数组大小

        public IndexTree(int size) {
            n = size;
            tree = new int[n + 1];
        }

        /**
         * 在位置idx增加值d
         */
        public void add(int idx, int d) {
            while (idx <= n) {
                tree[idx] += d;
                idx += idx & -idx;  // 移动到下一个需要更新的位置
            }
        }

        /**
         * 计算前缀和：从1到idx的和
         */
        public int sum(int idx) {
            int ret = 0;
            while (idx > 0) {
                ret += tree[idx];
                idx -= idx & -idx;  // 移动到父节点
            }
            return ret;
        }
    }

    /**
     * 方法3：树状数组优化法
     * 
     * 算法思路：
     * 使用树状数组维护已处理题目的难度分布，快速查询满足条件的题目数量
     * 
     * 优化思想：
     * - 方法2中的二分查找可以用树状数组的前缀和查询代替
     * - 树状数组支持O(logV)的更新和查询，其中V是值域大小
     * - 特别适合值域不太大的情况
     * 
     * 算法步骤：
     * 1. 建立值域到索引的映射（处理负数和稀疏值域）
     * 2. 按难度排序
     * 3. 逐个处理每个题目：
     *    - 查询已处理题目中难度 >= 当前难度-M 的数量
     *    - 将当前题目加入树状数组
     *    - 更新方案数
     * 
     * 时间复杂度：O(N*logV)，其中V是值域大小
     * 空间复杂度：O(V)
     * 
     * @param arr 题目难度数组
     * @param m 约束参数
     * @return 满足约束的排列数量
     */
    public static int ways3(int[] arr, int m) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        // 找到值域范围
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int num : arr) {
            max = Math.max(max, num);
            min = Math.min(min, num);
        }
        
        // 建立树状数组，大小为值域+2（留出边界）
        IndexTree tree = new IndexTree(max - min + 2);
        Arrays.sort(arr);
        
        int all = 1;
        tree.add(arr[0] - min + 1, 1);  // 第一个题目加入树状数组
        
        for (int i = 1; i < arr.length; i++) {
            int a = arr[i] - min + 1;        // 当前题目在树状数组中的位置
            int threshold = a - m - 1;       // 查询阈值位置
            
            // 计算可选择的题目数量
            // total = i，已处理的题目总数
            // excluded = 难度 < arr[i] - m 的题目数量
            // available = total - excluded
            int excluded = threshold >= 1 ? tree.sum(threshold) : 0;
            int available = i - excluded;
            
            all = all * (available + 1);  // +1是因为当前位置也是一种选择
            tree.add(a, 1);  // 将当前题目加入树状数组
        }
        return all;
    }

    /**
     * 生成随机数组用于测试
     */
    private static int[] randomArr(int len, int val) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * (val + 1));
        }
        return arr;
    }

    /**
     * 测试方法：验证三种算法的正确性和性能
     */
    public static void main(String[] args) {
        System.out.println("=== 试卷组卷方案数问题测试 ===");
        
        // 小规模正确性验证
        System.out.println("1. 小规模正确性验证:");
        int n = 10;
        int val = 20;
        int times = 1000;
        
        System.out.println("测试参数: 数组长度≤" + n + ", 值域≤" + val + ", 测试次数=" + times);
        System.out.println("开始验证...");
        
        boolean allCorrect = true;
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * (n + 1));
            int[] arr = randomArr(len, val);
            int m = (int) (Math.random() * (val + 1));
            
            int ans1 = ways1(arr, m);
            int ans2 = ways2(arr, m);
            int ans3 = ways3(arr, m);
            
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("发现错误!");
                System.out.println("数组: " + Arrays.toString(arr));
                System.out.println("M: " + m);
                System.out.println("暴力法: " + ans1);
                System.out.println("排序法: " + ans2);
                System.out.println("树状数组法: " + ans3);
                allCorrect = false;
                break;
            }
            
            if ((i + 1) % 200 == 0) {
                System.out.println("已完成 " + (i + 1) + " 次测试...");
            }
        }
        
        if (allCorrect) {
            System.out.println("✓ 小规模验证通过，算法正确！");
        } else {
            System.out.println("✗ 发现算法错误！");
        }
        
        // 大规模性能测试
        System.out.println("\n2. 大规模性能测试:");
        int[] largeArr = randomArr(100000000, 10000000);
        int testM = 10;
        long start, end;
        
        System.out.println("测试数组长度: " + largeArr.length);
        System.out.println("M = " + testM);
        System.out.println("暴力法由于时间过长，跳过测试");
        
        start = System.currentTimeMillis();
        int ans2 = ways2(largeArr, testM);
        end = System.currentTimeMillis();
        System.out.println("排序法: 结果 = " + ans2 + ", 耗时 = " + (end - start) + " ms");

        start = System.currentTimeMillis();
        int ans3 = ways3(largeArr, testM);
        end = System.currentTimeMillis();
        System.out.println("树状数组法: 结果 = " + ans3 + ", 耗时 = " + (end - start) + " ms");
        
        System.out.println("结果一致性: " + (ans2 == ans3 ? "✓" : "✗"));
        
        // 手动测试用例
        System.out.println("\n3. 手动测试用例:");
        int[] test = {1, 3, 2};
        int testM2 = 1;
        System.out.println("数组: [1, 3, 2], M = " + testM2);
        System.out.println("暴力法结果: " + ways1(test, testM2));
        System.out.println("排序法结果: " + ways2(test, testM2));
        System.out.println("树状数组法结果: " + ways3(test, testM2));
        System.out.println("期望: 4");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
