package giant.c39;

/**
 * 有效子数组计数问题
 * 
 * 问题描述：
 * 给定一个长度为n的数组arr，求有多少个子数组满足：
 * 子数组两端的值，是这个子数组的最小值和次小值
 * 最小值和次小值谁在最左和最右无所谓
 * 
 * 问题约束：
 * - n ≤ 100000（10^5）
 * - 要求O(n*logn)或O(n)时间复杂度
 * 
 * 解题思路：
 * 使用单调栈 + 组合数学：
 * 1. 对每个位置，找出以它为最小值的所有子数组
 * 2. 在这些子数组中，找出次小值在两端的情况
 * 3. 使用单调栈高效处理，避免暴力枚举
 * 
 * 算法核心：
 * - 单调栈：维护递增序列，处理每个位置的贡献
 * - 组合数学：计算相同值的组合贡献
 * - 双向处理：从左到右和从右到左各处理一次
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * 来源：腾讯面试题
 * 
 * @author Zhu Runqi
 */
public class ValidSequence {
    
    /**
     * 计算组合数C(n,2) = n*(n-1)/2
     * 
     * 用于计算相同值的元素组成有效子数组的数量
     * 当有n个相同的值时，可以选择其中任意2个作为子数组的两端
     * 
     * @param n 相同元素的个数
     * @return C(n,2)的值
     */
    private static int cn2(int n) {
        return (n * (n - 1)) >> 1;  // n*(n-1)/2
    }

    /**
     * 计算满足条件的子数组数量（最优解法）
     * 
     * 算法思路：
     * 使用单调栈分两次处理：
     * 1. 从左到右：处理每个元素作为最小值时的贡献
     * 2. 从右到左：处理次小值在右端的情况
     * 
     * 关键点：
     * - 单调栈维护递增序列
     * - 相同值的处理：累加计数
     * - 组合数学：计算相同值的贡献
     * 
     * @param arr 输入数组
     * @return 满足条件的子数组数量
     */
    public static int nums(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int n = arr.length;
        int[] values = new int[n];  // 单调栈存储的值
        int[] times = new int[n];   // 每个值的出现次数
        int size = 0;               // 栈的大小
        int ans = 0;
        
        // 第一次遍历：从左到右
        // 处理每个位置作为某个子数组最小值的情况
        for (int i = 0; i < arr.length; i++) {
            // 维护单调递增栈
            while (size != 0 && values[size - 1] > arr[i]) {
                size--;
                // 弹出元素时，计算其贡献
                // times[size]: 该值作为最小值的子数组中，次小值在右端的情况
                // cn2(times[size]): 相同值作为两端的情况
                ans += times[size] + cn2(times[size]);
            }
            
            // 如果栈顶元素与当前元素相同，累加计数
            if (size != 0 && values[size - 1] == arr[i]) {
                times[size - 1]++;
            } else {
                // 否则，将新元素入栈
                values[size] = arr[i];
                times[size++] = 1;
            }
        }
        
        // 处理栈中剩余元素
        // 这些元素作为最小值，且只有相同值作为两端的情况
        while (size != 0) {
            ans += cn2(times[--size]);
        }
        
        // 第二次遍历：从右到左
        // 处理次小值在左端的情况
        for (int i = arr.length - 1; i >= 0; i--) {
            // 维护单调递增栈
            while (size != 0 && values[size - 1] > arr[i]) {
                // 弹出元素时，计算其作为次小值（在左端）的贡献
                ans += times[--size];
            }
            
            // 相同值累加计数
            if (size != 0 && values[size - 1] == arr[i]) {
                times[size - 1]++;
            } else {
                values[size] = arr[i];
                times[size++] = 1;
            }
        }
        
        return ans;
    }

    /**
     * 暴力解法：用于验证结果正确性
     * 
     * 算法思路：
     * 枚举所有可能的子数组，检查是否满足条件：
     * 1. 子数组两端的值是该子数组的最小值和次小值
     * 2. 中间的所有值都不小于两端的较大值
     * 
     * @param arr 输入数组
     * @return 满足条件的子数组数量
     */
    public static int sure(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int ans = 0;
        
        // 枚举所有子数组
        for (int s = 0; s < arr.length; s++) {
            for (int e = s + 1; e < arr.length; e++) {
                // 两端的较大值
                int max = Math.max(arr[s], arr[e]);
                boolean valid = true;
                
                // 检查中间的所有值是否都不小于较大值
                for (int i = s + 1; i < e; i++) {
                    if (arr[i] < max) {
                        valid = false;
                        break;
                    }
                }
                
                ans += valid ? 1 : 0;
            }
        }
        
        return ans;
    }

    /**
     * 生成随机测试数组
     * 
     * @param n 数组长度
     * @param v 最大值
     * @return 随机数组
     */
    private static int[] randomArr(int n, int v) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int) (Math.random() * v);
        }
        return arr;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 有效子数组计数问题 ===\n");
        
        // 测试用例1：简单示例
        System.out.println("测试用例1：简单示例");
        int[] arr1 = {3, 1, 4, 1, 5};
        int result1 = nums(arr1);
        System.out.println("数组: [3, 1, 4, 1, 5]");
        System.out.println("有效子数组数量: " + result1);
        System.out.println("分析:");
        System.out.println("  - [3,1]: 3和1是最大值和最小值，满足条件");
        System.out.println("  - [1,4]: 1和4是最小值和最大值，满足条件");
        System.out.println("  - [4,1]: 4和1是最大值和最小值，满足条件");
        System.out.println("  - [1,5]: 1和5是最小值和最大值，满足条件");
        System.out.println();
        
        // 测试用例2：相同元素
        System.out.println("测试用例2：相同元素");
        int[] arr2 = {2, 2, 2, 2};
        int result2 = nums(arr2);
        System.out.println("数组: [2, 2, 2, 2]");
        System.out.println("有效子数组数量: " + result2);
        System.out.println("分析: 所有相同元素的子数组都满足条件");
        System.out.println("计算: C(4,2) = 6种组合");
        System.out.println();
        
        // 测试用例3：递增序列
        System.out.println("测试用例3：递增序列");
        int[] arr3 = {1, 2, 3, 4, 5};
        int result3 = nums(arr3);
        System.out.println("数组: [1, 2, 3, 4, 5]");
        System.out.println("有效子数组数量: " + result3);
        System.out.println("分析: 每个相邻元素对都满足条件");
        System.out.println();
        
        // 测试用例4：递减序列
        System.out.println("测试用例4：递减序列");
        int[] arr4 = {5, 4, 3, 2, 1};
        int result4 = nums(arr4);
        System.out.println("数组: [5, 4, 3, 2, 1]");
        System.out.println("有效子数组数量: " + result4);
        System.out.println("分析: 每个相邻元素对都满足条件");
        System.out.println();
        
        // 测试用例5：复杂示例
        System.out.println("测试用例5：复杂示例");
        int[] arr5 = {1, 3, 2, 4, 2, 1};
        int result5 = nums(arr5);
        int sure5 = sure(arr5);
        System.out.println("数组: [1, 3, 2, 4, 2, 1]");
        System.out.println("有效子数组数量 (优化算法): " + result5);
        System.out.println("有效子数组数量 (暴力验证): " + sure5);
        System.out.println("结果一致: " + (result5 == sure5 ? "是" : "否"));
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int times = 10000;
        int n = 30;
        int v = 30;
        boolean allPassed = true;
        
        System.out.println("开始随机测试...");
        
        for (int i = 0; i < times; i++) {
            int m = (int) (Math.random() * n);
            int[] arr = randomArr(m, v);
            int ans1 = nums(arr);
            int ans2 = sure(arr);
            
            if (ans1 != ans2) {
                System.out.println("测试失败: 结果不一致");
                System.out.println("数组长度: " + m);
                allPassed = false;
                break;
            }
        }
        
        if (allPassed) {
            System.out.println("所有随机测试通过！");
        }
        
        System.out.println("\n=== 算法原理解析 ===");
        System.out.println("1. 问题特征：");
        System.out.println("   - 子数组两端值是该子数组的最小值和次小值");
        System.out.println("   - 中间所有值都不小于两端的较大值");
        System.out.println("   - 最小值和次小值的位置可以交换");
        System.out.println();
        System.out.println("2. 单调栈思想：");
        System.out.println("   - 维护递增序列，处理每个位置的贡献");
        System.out.println("   - 弹出元素时计算其作为最小值的贡献");
        System.out.println("   - 相同值累加计数，利用组合数学");
        System.out.println();
        System.out.println("3. 双向处理：");
        System.out.println("   - 从左到右：处理最小值的贡献");
        System.out.println("   - 从右到左：处理次小值在左端的情况");
        System.out.println("   - 避免重复计算和遗漏");
        System.out.println();
        System.out.println("4. 组合数学：");
        System.out.println("   - C(n,2) = n*(n-1)/2");
        System.out.println("   - 相同值组成的有效子数组数量");
        System.out.println("   - 优化计算效率");
        System.out.println();
        System.out.println("5. 复杂度分析：");
        System.out.println("   - 时间复杂度：O(n)，每个元素最多进出栈一次");
        System.out.println("   - 空间复杂度：O(n)，单调栈空间");
        System.out.println("   - 比暴力O(n^3)算法快得多");
        System.out.println();
        System.out.println("6. 应用场景：");
        System.out.println("   - 子数组统计问题");
        System.out.println("   - 单调栈优化");
        System.out.println("   - 组合数学在算法中的应用");
    }
}
