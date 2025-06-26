package giant.c7;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 有序数组中不同平方数的个数问题
 * 
 * 问题描述：
 * 给定一个按升序排列的整数数组，统计数组中所有元素平方之后有多少个不同的值。
 * 
 * 例如：
 * 数组 [-4, -1, 0, 3, 10]
 * 平方后：[16, 1, 0, 9, 100]
 * 不同的平方数有：0, 1, 9, 16, 100，共5个
 * 
 * 解决方案：
 * 1. 方法1：哈希集合法 - 时间复杂度O(N)，空间复杂度O(N)
 * 2. 方法2：双指针优化法 - 时间复杂度O(N)，空间复杂度O(1)
 * 
 * 核心思想：
 * 方法1：将所有平方数放入HashSet，利用Set的去重特性
 * 方法2：利用有序数组的性质，使用双指针从两端向中间收缩
 * 
 * 关键观察：
 * - 有序数组中，绝对值的分布呈现"先递减再递增"的特点
 * - 可以利用双指针技巧避免额外的存储空间
 * 
 * 时间复杂度：O(N)
 * 空间复杂度：O(1) - 方法2优化版本
 */
public class Power2Diffs {
    
    /**
     * 方法1：哈希集合法
     * 
     * 算法思路：
     * 遍历数组，将每个数的平方放入HashSet中
     * 利用HashSet自动去重的特性，最后返回集合大小
     * 
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
     * 
     * 优点：实现简单，逻辑清晰
     * 缺点：需要额外的存储空间
     * 
     * @param arr 有序整数数组
     * @return 不同平方数的个数
     */
    public static int diff1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        // 使用HashSet存储所有不同的平方数
        HashSet<Integer> set = new HashSet<>();
        for (int cur : arr) {
            set.add(cur * cur);  // 添加平方数，Set自动去重
        }
        return set.size();
    }

    /**
     * 方法2：双指针优化法
     * 
     * 算法思路：
     * 观察有序数组的特点：绝对值呈现"先递减再递增"的分布
     * 例如：[-4, -3, -1, 0, 1, 2, 5] 的绝对值为：[4, 3, 1, 0, 1, 2, 5]
     * 
     * 利用双指针从两端向中间收缩：
     * 1. 比较左右指针对应元素的绝对值
     * 2. 选择绝对值较大的一侧
     * 3. 跳过所有绝对值相同的元素
     * 4. 计数不同绝对值的个数
     * 
     * 关键观察：
     * - 相同绝对值的数字平方结果相同
     * - 通过比较绝对值可以避免实际计算平方
     * - 双指针技巧可以一次遍历完成统计
     * 
     * 时间复杂度：O(N)
     * 空间复杂度：O(1)
     * 
     * @param arr 有序整数数组
     * @return 不同平方数的个数
     */
    public static int diff2(int[] arr) {
        int n = arr.length;
        int l = 0, r = n - 1;      // 左右双指针
        int count = 0;             // 不同平方数的计数
        int leftAbs = 0, rightAbs = 0;  // 左右指针对应的绝对值
        
        while (l <= r) {
            count++;  // 发现一个新的不同绝对值
            
            leftAbs = Math.abs(arr[l]);   // 左指针绝对值
            rightAbs = Math.abs(arr[r]);  // 右指针绝对值
            
            if (leftAbs < rightAbs) {
                // 右侧绝对值更大，处理右侧
                // 跳过所有绝对值等于rightAbs的元素
                while (r >= 0 && Math.abs(arr[r]) == rightAbs) {
                    r--;
                }
            } else if (leftAbs > rightAbs) {
                // 左侧绝对值更大，处理左侧
                // 跳过所有绝对值等于leftAbs的元素
                while (l < n && Math.abs(arr[l]) == leftAbs) {
                    l++;
                }
            } else {
                // 左右绝对值相等，同时处理两侧
                // 跳过所有绝对值等于leftAbs的元素
                while (l < n && Math.abs(arr[l]) == leftAbs) {
                    l++;
                }
                while (r >= 0 && Math.abs(arr[r]) == rightAbs) {
                    r--;
                }
            }
        }
        return count;
    }

    /**
     * 生成随机有序数组用于测试
     * 
     * @param len 数组长度上限
     * @param val 数值范围
     * @return 随机生成的有序数组
     */
    private static int[] randomSortedArr(int len, int val) {
        int[] ans = new int[(int) (Math.random() * len) + 1];
        for (int i = 0; i < ans.length; i++) {
            // 生成 [-val, val] 范围内的随机数
            ans[i] = (int) (Math.random() * val) - (int) (Math.random() * val);
        }
        Arrays.sort(ans);  // 排序以满足有序数组要求
        return ans;
    }

    /**
     * 测试方法：验证两种算法的正确性和性能
     */
    public static void main(String[] args) {
        System.out.println("=== 有序数组中不同平方数个数测试 ===");
        
        // 手动测试用例
        System.out.println("1. 手动测试用例:");
        int[] test1 = {-4, -1, 0, 3, 10};
        System.out.println("测试数组: " + Arrays.toString(test1));
        System.out.println("方法1结果: " + diff1(test1));
        System.out.println("方法2结果: " + diff2(test1));
        System.out.println("分析: 平方后为 [16, 1, 0, 9, 100]，共5个不同值");
        System.out.println();
        
        // 包含重复绝对值的测试
        int[] test2 = {-3, -2, -1, 0, 1, 2, 3};
        System.out.println("测试数组: " + Arrays.toString(test2));
        System.out.println("方法1结果: " + diff1(test2));
        System.out.println("方法2结果: " + diff2(test2));
        System.out.println("分析: 平方后为 [9, 4, 1, 0, 1, 4, 9]，去重后为 [0, 1, 4, 9]，共4个");
        System.out.println();
        
        // 随机测试验证算法正确性
        int times = 200000;
        int len = 100;
        int val = 500;
        
        System.out.println("2. 随机测试验证:");
        System.out.println("测试次数: " + times);
        System.out.println("最大数组长度: " + len);
        System.out.println("数值范围: [-" + val + ", " + val + "]");
        System.out.println("开始测试...");
        
        boolean allCorrect = true;
        long start = System.currentTimeMillis();
        
        for (int i = 0; i < times; i++) {
            int[] arr = randomSortedArr(len, val);
            int ans1 = diff1(arr);
            int ans2 = diff2(arr);
            
            if (ans1 != ans2) {
                System.out.println("发现错误! 测试用例: " + i);
                System.out.println("数组: " + Arrays.toString(arr));
                System.out.println("方法1结果: " + ans1);
                System.out.println("方法2结果: " + ans2);
                allCorrect = false;
                break;
            }
            
            // 显示进度
            if ((i + 1) % 50000 == 0) {
                System.out.println("已完成 " + (i + 1) + " 次测试...");
            }
        }
        
        long end = System.currentTimeMillis();
        
        if (allCorrect) {
            System.out.println("✓ 所有测试通过，算法正确！");
        } else {
            System.out.println("✗ 发现算法错误！");
        }
        
        System.out.println("总耗时: " + (end - start) + "ms");
        System.out.println();
        
        // 性能比较测试
        System.out.println("3. 性能比较测试:");
        int[] perfTestArr = randomSortedArr(10000, 1000);
        
        start = System.currentTimeMillis();
        int result1 = diff1(perfTestArr);
        long time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result2 = diff2(perfTestArr);
        long time2 = System.currentTimeMillis() - start;
        
        System.out.println("方法1（哈希集合）: " + result1 + ", 耗时: " + time1 + "ms");
        System.out.println("方法2（双指针优化）: " + result2 + ", 耗时: " + time2 + "ms");
        System.out.println("空间复杂度对比：方法1 O(N) vs 方法2 O(1)");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
