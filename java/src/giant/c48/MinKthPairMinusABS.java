package giant.c48;

import java.util.Arrays;

/**
 * 第K小数字对差值绝对值问题
 * 
 * 问题描述：
 * 给定一个数组arr，和一个正数k
 * 返回arr中所有数字对差值的绝对值，第k小的是多少
 * 
 * 示例：
 * arr = {5, 3, 1, 4}, k = 4
 * 全部数字对：(5,3)、(5,1)、(5,4)、(3,1)、(3,4)、(1,4)
 * 数字对的差值绝对值：2、4、1、2、1、3
 * 差值绝对值排序后：1、1、2、2、3、4
 * 第4小的是2
 * 
 * 解题思路：
 * 方法1：暴力解法
 * - 生成所有数字对的差值绝对值
 * - 排序后返回第k小的值
 * - 时间复杂度：O(n^2 log n)
 * 
 * 方法2：二分搜索 + 双指针
 * - 对数组排序
 * - 二分搜索答案
 * - 用双指针统计≤mid的数字对个数
 * - 时间复杂度：O(n log n + n log(max-min))
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(1)
 * 
 * @author Zhu Runqi
 */
public class MinKthPairMinusABS {
    
    /**
     * 暴力解法：生成所有差值并排序（方法1：验证用）
     * 
     * 算法思路：
     * 1. 生成所有可能的数字对
     * 2. 计算每对数字的差值绝对值
     * 3. 对所有差值排序
     * 4. 返回第k小的差值
     * 
     * @param arr 输入数组
     * @param k 第k小（1-based）
     * @return 第k小的差值绝对值，无效输入返回-1
     */
    public static int sure(int[] arr, int k) {
        int n = arr.length;
        int m = ((n - 1) * n) >> 1;  // 数字对总数：C(n,2)
        
        if (m == 0 || k < 1 || k > m) {
            return -1;  // 无效输入
        }
        
        // 生成所有数字对的差值绝对值
        int[] abs = new int[m];
        int size = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                abs[size++] = Math.abs(arr[i] - arr[j]);
            }
        }
        
        // 排序后返回第k小的值
        Arrays.sort(abs);
        return abs[k - 1];
    }

    /**
     * 验证某个limit值是否满足条件
     * 
     * 算法思路：
     * 统计差值绝对值≤limit的数字对个数
     * 如果个数 < k，说明limit太小，返回true（需要增大limit）
     * 如果个数 >= k，说明limit足够大，返回false（可以减小limit）
     * 
     * 使用双指针技术：
     * - 对于每个左端点l，找到最大的右端点r使得arr[r]-arr[l]≤limit
     * - 区间[l+1, r]内的所有元素都可以与arr[l]组成满足条件的数字对
     * 
     * @param arr 已排序的数组
     * @param limit 差值限制
     * @param k 目标排名
     * @return 是否需要增大limit
     */
    private static boolean valid(int[] arr, int limit, int k) {
        int x = 0;  // 差值≤limit的数字对个数
        
        // 双指针：l为左端点，r为右端点
        for (int l = 0, r = 1; l < arr.length; r = Math.max(r, ++l)) {
            // 找到最大的r使得arr[r] - arr[l] <= limit
            while (r < arr.length && arr[r] - arr[l] <= limit) {
                r++;
            }
            // 区间[l+1, r-1]内的所有元素都可以与arr[l]组成满足条件的数字对
            x += r - l - 1;
        }
        
        return x < k;  // 如果数字对个数不足k，需要增大limit
    }

    /**
     * 二分搜索求第k小差值绝对值（方法2：最优解法）
     * 
     * 算法思路：
     * 1. 对数组排序
     * 2. 二分搜索答案
     * 3. 用双指针统计≤mid的数字对个数
     * 4. 根据个数调整搜索范围
     * 5. 找到满足条件的最大值，答案为该值+1
     * 
     * 关键洞察：
     * - 排序后，arr[i]和arr[j](i<j)的差值为arr[j]-arr[i]
     * - 单调性：差值≤x的数字对个数随x单调递增
     * - 二分搜索：找到最大的x使得个数<k
     * 
     * @param arr 输入数组
     * @param k 第k小（1-based）
     * @return 第k小的差值绝对值，无效输入返回-1
     */
    public static int kthABS(int[] arr, int k) {
        int n = arr.length;
        if (n < 2 || k < 1 || k > ((n * (n - 1)) >> 1)) {
            return -1;  // 无效输入
        }
        
        // 对数组排序
        Arrays.sort(arr);
        
        // 二分搜索答案
        int left = 0;
        int right = arr[n - 1] - arr[0];  // 最大可能差值
        int mid = 0;
        int rightest = -1;
        
        while (left <= right) {
            mid = (left + right) / 2;
            if (valid(arr, mid, k)) {
                rightest = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return rightest + 1;
    }

    /**
     * 生成随机数组
     * 
     * @param n 数组长度
     * @param v 数值上界
     * @return 随机数组
     */
    private static int[] randomArr(int n, int v) {
        int[] ans = new int[n];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (Math.random() * v);
        }
        return ans;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 第K小数字对差值绝对值问题 ===\n");
        
        // 测试用例1：题目示例
        System.out.println("测试用例1：题目示例");
        int[] arr1 = {5, 3, 1, 4};
        int k1 = 4;
        int result1_1 = sure(arr1.clone(), k1);
        int result1_2 = kthABS(arr1.clone(), k1);
        System.out.print("数组: [");
        for (int i = 0; i < arr1.length; i++) {
            System.out.print(arr1[i] + (i == arr1.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("k = " + k1);
        System.out.println("结果 (方法1): " + result1_1);
        System.out.println("结果 (方法2): " + result1_2);
        System.out.println("分析: 所有差值[1,1,2,2,3,4]，第4小是2");
        System.out.println();
        
        // 正确性验证
        System.out.println("=== 正确性验证 ===");
        int size = 100;
        int val = 100;
        int times = 10000;
        boolean allPassed = true;
        
        System.out.println("开始随机测试...");
        for (int i = 0; i < times; i++) {
            int n = (int) (Math.random() * size);
            if (n < 2) continue;
            
            int k = (int) (Math.random() * (n * (n - 1) / 2)) + 1;
            int[] arr = randomArr(n, val);
            int ans1 = sure(arr.clone(), k);
            int ans2 = kthABS(arr.clone(), k);
            
            if (ans1 != ans2) {
                System.out.println("测试失败!");
                System.out.print("数组: [");
                for (int j = 0; j < arr.length; j++) {
                    System.out.print(arr[j] + (j == arr.length - 1 ? "" : ", "));
                }
                System.out.println("]");
                System.out.println("k = " + k);
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
        long start, end;
        int n = 500000, v = 50000000;
        int[] arr = randomArr(n, v);
        int k = (int) (Math.random() * (n * (n - 1) / 2)) + 1;
        
        start = System.currentTimeMillis();
        kthABS(arr, k);
        end = System.currentTimeMillis();
        
        System.out.println("数组长度: " + n);
        System.out.println("执行时间: " + (end - start) + " 毫秒");
        System.out.println();
        
        System.out.println("=== 算法原理解析 ===");
        System.out.println("1. 问题特征：");
        System.out.println("   - 数字对总数：C(n,2) = n*(n-1)/2");
        System.out.println("   - 差值范围：[0, max-min]");
        System.out.println("   - 单调性：差值≤x的数字对个数随x单调递增");
        System.out.println();
        System.out.println("2. 二分搜索策略：");
        System.out.println("   - 搜索空间：差值的可能范围");
        System.out.println("   - 判定条件：差值≤mid的数字对个数");
        System.out.println("   - 边界处理：找到最大的不满足条件的值");
        System.out.println();
        System.out.println("3. 双指针统计：");
        System.out.println("   - 排序预处理：确保单调性");
        System.out.println("   - 指针移动：l递增，r单调递增");
        System.out.println("   - 计数优化：区间长度直接计算");
        System.out.println();
        System.out.println("4. 复杂度分析：");
        System.out.println("   - 方法1：O(n^2 log n)，生成+排序");
        System.out.println("   - 方法2：O(n log n + n log(max-min))");
        System.out.println("   - 空间复杂度：O(1)除了排序空间");
        System.out.println();
        System.out.println("5. 应用场景：");
        System.out.println("   - 第k小问题的经典应用");
        System.out.println("   - 数组统计分析");
        System.out.println("   - 近似算法中的距离度量");
    }
}
