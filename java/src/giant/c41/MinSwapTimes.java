package giant.c41;

/**
 * 数组最小交换次数问题
 * 
 * 问题描述：
 * 一个无序数组长度为n，所有数字都不一样，并且值都在[0...n-1]范围上
 * 返回让这个无序数组变成有序数组的最小交换次数
 * 
 * 问题特征：
 * - 数组是[0, n-1]的一个排列
 * - 每个元素都有唯一的目标位置
 * - 目标：使array[i] = i
 * 
 * 解题思路：
 * 方法1：递归+回溯（暴力解法）
 * - 尝试所有可能的交换组合
 * - 时间复杂度极高，仅用于验证
 * 
 * 方法2：贪心算法（最优解法）
 * - 核心思想：每次交换都让至少一个元素到达正确位置
 * - 对于位置i，如果arr[i] != i，将arr[i]交换到正确位置
 * 
 * 算法核心：
 * - 置换环：将排列看作置换，每个环最少需要(环长度-1)次交换
 * - 贪心策略：优先处理当前位置的元素
 * - 最优性：每次交换都减少一个错位元素
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * 
 * 来源：小红书面试题
 * 
 * @author Zhu Runqi
 */
public class MinSwapTimes {

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
     * 递归尝试所有可能的交换组合（方法1：暴力解法）
     * 
     * 算法思路：
     * 1. 检查当前数组是否已经有序
     * 2. 如果有序，返回当前交换次数
     * 3. 如果交换次数超过n-1，剪枝返回
     * 4. 尝试所有可能的交换，递归求解
     * 
     * 注意：此方法时间复杂度为O(n! * n^2)，仅用于小规模验证
     * 
     * @param arr 当前数组状态
     * @param times 已进行的交换次数
     * @return 使数组有序的最小交换次数
     */
    private static int process1(int[] arr, int times) {
        // 检查数组是否已经有序
        boolean sorted = true;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) {
                sorted = false;
                break;
            }
        }
        if (sorted) {
            return times;
        }
        
        // 剪枝：最多需要n-1次交换
        if (times >= arr.length - 1) {
            return Integer.MAX_VALUE;
        }
        
        int ans = Integer.MAX_VALUE;
        // 尝试所有可能的交换
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                swap(arr, i, j);
                ans = Math.min(ans, process1(arr, times + 1));
                swap(arr, i, j);  // 回溯
            }
        }
        return ans;
    }

    /**
     * 递归方法的入口函数
     * 
     * @param arr 输入数组
     * @return 最小交换次数
     */
    public static int min1(int[] arr) {
        return process1(arr, 0);
    }

    /**
     * 贪心算法求最小交换次数（方法2：最优解法）
     * 
     * 算法思路：
     * 1. 遍历数组的每个位置i
     * 2. 如果arr[i] != i，说明元素不在正确位置
     * 3. 将arr[i]交换到它应该在的位置arr[i]
     * 4. 重复步骤2-3，直到arr[i] == i
     * 5. 每次交换都会让至少一个元素到达正确位置
     * 
     * 正确性证明：
     * - 每次交换都减少至少一个错位元素
     * - 最终所有元素都会到达正确位置
     * - 交换次数等于所有置换环的长度之和减去环的个数
     * 
     * @param arr 输入数组
     * @return 最小交换次数
     */
    public static int min2(int[] arr) {
        int ans = 0;
        
        for (int i = 0; i < arr.length; i++) {
            // 如果当前位置的元素不正确，一直交换直到正确
            while (i != arr[i]) {
                // 将arr[i]交换到它应该在的位置
                swap(arr, i, arr[i]);
                ans++;
            }
        }
        
        return ans;
    }

    /**
     * 生成随机排列数组
     * 
     * @param len 数组长度
     * @return [0, len-1]的随机排列
     */
    private static int[] randomArr(int len) {
        int[] arr = new int[len];
        
        // 初始化为有序数组
        for (int i = 0; i < len; i++) {
            arr[i] = i;
        }
        
        // 随机打乱
        for (int i = 0; i < len; i++) {
            swap(arr, i, (int) (Math.random() * len));
        }
        
        return arr;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 数组最小交换次数问题 ===\n");
        
        // 测试用例1：简单示例
        System.out.println("测试用例1：简单示例");
        int[] arr1 = {2, 0, 1, 3};
        int[] arr1_copy = arr1.clone();
        int result1_1 = min1(arr1);
        int result1_2 = min2(arr1_copy);
        System.out.print("数组: [");
        for (int i = 0; i < arr1.length; i++) {
            System.out.print(arr1[i] + (i == arr1.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("最小交换次数 (方法1): " + result1_1);
        System.out.println("最小交换次数 (方法2): " + result1_2);
        System.out.println("分析: 交换(0,2)和(1,0)，总共2次交换");
        System.out.println();
        
        // 测试用例2：已经有序
        System.out.println("测试用例2：已经有序");
        int[] arr2 = {0, 1, 2, 3, 4};
        int result2 = min2(arr2.clone());
        System.out.print("数组: [");
        for (int i = 0; i < arr2.length; i++) {
            System.out.print(arr2[i] + (i == arr2.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("最小交换次数: " + result2);
        System.out.println("分析: 数组已经有序，不需要交换");
        System.out.println();
        
        // 测试用例3：完全逆序
        System.out.println("测试用例3：完全逆序");
        int[] arr3 = {3, 2, 1, 0};
        int result3 = min2(arr3.clone());
        System.out.print("数组: [");
        for (int i = 0; i < arr3.length; i++) {
            System.out.print(arr3[i] + (i == arr3.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("最小交换次数: " + result3);
        System.out.println("分析: 交换(0,3)和(1,2)，总共2次交换");
        System.out.println();
        
        // 测试用例4：单个置换环
        System.out.println("测试用例4：单个置换环");
        int[] arr4 = {1, 2, 0, 3, 4};
        int result4 = min2(arr4.clone());
        System.out.print("数组: [");
        for (int i = 0; i < arr4.length; i++) {
            System.out.print(arr4[i] + (i == arr4.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("最小交换次数: " + result4);
        System.out.println("分析: 置换环(0→1→2→0)，需要2次交换");
        System.out.println();
        
        // 测试用例5：多个置换环
        System.out.println("测试用例5：多个置换环");
        int[] arr5 = {1, 0, 3, 2, 4};
        int result5 = min2(arr5.clone());
        System.out.print("数组: [");
        for (int i = 0; i < arr5.length; i++) {
            System.out.print(arr5[i] + (i == arr5.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("最小交换次数: " + result5);
        System.out.println("分析: 两个长度为2的置换环，各需要1次交换");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int times = 2000;
        int n = 6;
        boolean allPassed = true;
        
        System.out.println("开始随机测试（数组长度≤" + n + "）...");
        
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * n) + 1;
            int[] arr = randomArr(len);
            int[] arr_copy = arr.clone();
            int ans1 = min1(arr);
            int ans2 = min2(arr_copy);
            
            if (ans1 != ans2) {
                System.out.println("测试失败: 结果不一致");
                System.out.print("数组: [");
                for (int j = 0; j < arr.length; j++) {
                    System.out.print(arr[j] + (j == arr.length - 1 ? "" : ", "));
                }
                System.out.println("]");
                System.out.println("方法1结果: " + ans1);
                System.out.println("方法2结果: " + ans2);
                allPassed = false;
                break;
            }
        }
        
        if (allPassed) {
            System.out.println("所有随机测试通过！");
        }
        
        // 大规模性能测试
        System.out.println("\n=== 大规模性能测试 ===");
        int[] largeArr = randomArr(10000);
        long start = System.currentTimeMillis();
        int largeResult = min2(largeArr);
        long end = System.currentTimeMillis();
        
        System.out.println("数组长度: 10000");
        System.out.println("最小交换次数: " + largeResult);
        System.out.println("执行时间: " + (end - start) + " 毫秒");
        System.out.println();
        
        System.out.println("=== 算法原理解析 ===");
        System.out.println("1. 问题本质：");
        System.out.println("   - 排列复原：将[0,n-1]的排列还原为自然顺序");
        System.out.println("   - 置换分解：每个排列可以分解为若干个不相交的置换环");
        System.out.println("   - 最优策略：每次交换让至少一个元素到达正确位置");
        System.out.println();
        System.out.println("2. 置换环理论：");
        System.out.println("   - 环的定义：a[i]→a[a[i]]→...→a[k]→i");
        System.out.println("   - 环的处理：长度为k的环需要k-1次交换");
        System.out.println("   - 总交换次数：Σ(环长度-1) = n - 环的个数");
        System.out.println();
        System.out.println("3. 贪心策略：");
        System.out.println("   - 位置优先：对每个位置，确保其元素正确");
        System.out.println("   - 直接交换：将错位元素直接交换到目标位置");
        System.out.println("   - 无后效性：每次交换都不影响之前的正确元素");
        System.out.println();
        System.out.println("4. 算法正确性：");
        System.out.println("   - 单调性：每次交换都减少错位元素数量");
        System.out.println("   - 最优性：交换次数等于理论最小值");
        System.out.println("   - 终止性：有限步骤内必定完成排序");
        System.out.println();
        System.out.println("5. 复杂度分析：");
        System.out.println("   - 时间复杂度：O(n)，每个位置最多访问常数次");
        System.out.println("   - 空间复杂度：O(1)，原地算法");
        System.out.println("   - 实际性能：线性时间，效率极高");
        System.out.println();
        System.out.println("6. 应用场景：");
        System.out.println("   - 数组排序优化");
        System.out.println("   - 排列问题求解");
        System.out.println("   - 组合数学应用");
    }
}
