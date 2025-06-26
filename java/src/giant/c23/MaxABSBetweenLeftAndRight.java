package giant.c23;

/**
 * 数组左右划分最大值差值最大问题
 * 
 * 问题描述：
 * 给定一个数组，在某个位置将数组分为左右两部分（左边至少1个元素，右边至少1个元素），
 * 分别求出左右两部分的最大值，计算这两个最大值的差的绝对值，
 * 返回所有可能划分中，这个绝对值的最大值。
 * 
 * 例如：
 * arr = [2, 7, 3, 1, 6]
 * 可能的划分：
 * [2] | [7,3,1,6]  -> |2-7| = 5
 * [2,7] | [3,1,6]  -> |7-6| = 1
 * [2,7,3] | [1,6]  -> |7-6| = 1
 * [2,7,3,1] | [6]  -> |7-6| = 1
 * 最大值是5
 * 
 * 解决方案：
 * 1. 方法1：暴力法 - 时间复杂度O(N^2)
 * 2. 方法2：预处理优化 - 时间复杂度O(N)
 * 3. 方法3：数学分析法 - 时间复杂度O(N)，最优解
 * 
 * 核心洞察：
 * 答案一定是 max(全数组) - min(arr[0], arr[n-1])
 * 因为数组的全局最大值一定会出现在某一侧，而另一侧的最大值不会超过全局最大值
 * 
 * 算法复杂度：
 * 时间复杂度：O(N)
 * 空间复杂度：O(1)
 */
public class MaxABSBetweenLeftAndRight {
    
    /**
     * 方法1：暴力枚举法
     * 
     * 算法思路：
     * 对每个可能的分割点，重新计算左右两部分的最大值，然后计算差值的绝对值
     * 
     * 算法步骤：
     * 1. 遍历所有可能的分割点i（从0到n-2）
     * 2. 对每个分割点，线性扫描左半部分[0...i]找最大值
     * 3. 线性扫描右半部分[i+1...n-1]找最大值
     * 4. 计算两个最大值的差的绝对值，更新全局最大值
     * 
     * 算法特点：
     * - 思路直观，易于理解
     * - 时间复杂度高，存在大量重复计算
     * - 适用于小规模数据的验证
     * 
     * 时间复杂度：O(N^2)，N个分割点，每个分割点需要O(N)时间计算左右最大值
     * 空间复杂度：O(1)
     * 
     * @param arr 输入数组
     * @return 最大的差值绝对值
     */
    public static int maxABS1(int[] arr) {
        int res = Integer.MIN_VALUE;  // 记录最大的差值绝对值
        int maxLeft = 0, maxRight = 0;
        
        // 遍历所有可能的分割点
        for (int i = 0; i < arr.length - 1; i++) {
            // 计算左半部分[0...i]的最大值
            maxLeft = Integer.MIN_VALUE;
            for (int j = 0; j <= i; j++) {
                maxLeft = Math.max(arr[j], maxLeft);
            }
            
            // 计算右半部分[i+1...n-1]的最大值
            maxRight = Integer.MIN_VALUE;
            for (int j = i + 1; j < arr.length; j++) {
                maxRight = Math.max(arr[j], maxRight);
            }
            
            // 更新最大差值绝对值
            res = Math.max(Math.abs(maxLeft - maxRight), res);
        }
        return res;
    }

    /**
     * 方法2：预处理优化法
     * 
     * 算法思路：
     * 通过预处理避免重复计算，使用两个数组分别记录每个位置向左和向右的最大值
     * 
     * 预处理过程：
     * - lArr[i]：数组[0...i]的最大值
     * - rArr[i]：数组[i...n-1]的最大值
     * 
     * 算法步骤：
     * 1. 从左到右计算每个位置的左侧最大值数组lArr
     * 2. 从右到左计算每个位置的右侧最大值数组rArr
     * 3. 遍历所有分割点，直接使用预处理结果计算差值绝对值
     * 
     * 优化效果：
     * - 避免了重复计算最大值
     * - 将时间复杂度从O(N^2)降低到O(N)
     * - 以O(N)的空间换取时间优化
     * 
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
     * 
     * @param arr 输入数组
     * @return 最大的差值绝对值
     */
    public static int maxABS2(int[] arr) {
        // 预处理：计算每个位置的左侧最大值
        int[] lArr = new int[arr.length];
        lArr[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            lArr[i] = Math.max(lArr[i - 1], arr[i]);
        }
        
        // 预处理：计算每个位置的右侧最大值
        int[] rArr = new int[arr.length];
        rArr[arr.length - 1] = arr[arr.length - 1];
        for (int i = arr.length - 2; i >= 0; i--) {
            rArr[i] = Math.max(rArr[i + 1], arr[i]);
        }
        
        // 利用预处理结果计算最大差值绝对值
        int max = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            // lArr[i]是左半部分[0...i]的最大值
            // rArr[i+1]是右半部分[i+1...n-1]的最大值
            max = Math.max(max, Math.abs(lArr[i] - rArr[i + 1]));
        }
        return max;
    }

    /**
     * 方法3：数学分析法（最优解）
     * 
     * 核心洞察：
     * 答案一定是 max(全数组) - min(arr[0], arr[n-1])
     * 
     * 数学证明：
     * 1. 设全局最大值为M，它一定出现在某个位置k
     * 2. 对于任意分割点i：
     *    - 如果k <= i（M在左半部分），左侧最大值=M，右侧最大值<=M
     *      差值 = M - 右侧最大值 <= M - min(右边界元素)
     *    - 如果k > i（M在右半部分），右侧最大值=M，左侧最大值<=M
     *      差值 = M - 左侧最大值 <= M - min(左边界元素)
     * 3. 最优分割一定出现在边界：要么分割点在位置0，要么在位置n-2
     *    - 分割点在0：左侧=[arr[0]]，右侧=[arr[1]...arr[n-1]]
     *      差值 = |arr[0] - max(arr[1]...arr[n-1])| 
     *           = |arr[0] - M| (如果M不在位置0)
     *           = M - arr[0] (当M != arr[0])
     *    - 分割点在n-2：左侧=[arr[0]...arr[n-2]]，右侧=[arr[n-1]]
     *      差值 = |max(arr[0]...arr[n-2]) - arr[n-1]|
     *           = |M - arr[n-1]| (如果M不在位置n-1)
     *           = M - arr[n-1] (当M != arr[n-1])
     * 4. 因此答案是 M - min(arr[0], arr[n-1])
     * 
     * 算法优势：
     * - 时间复杂度最优：O(N)
     * - 空间复杂度最优：O(1)
     * - 基于数学分析，避免了不必要的计算
     * 
     * 时间复杂度：O(N)，只需要一次遍历找全局最大值
     * 空间复杂度：O(1)
     * 
     * @param arr 输入数组
     * @return 最大的差值绝对值
     */
    public static int maxABS3(int[] arr) {
        // 找到数组中的全局最大值
        int max = Integer.MIN_VALUE;
        for(int i = 0; i < arr.length; i++) {
            max = Math.max(arr[i], max);
        }
        
        // 答案就是全局最大值减去两个边界元素中的较小值
        return max - Math.min(arr[0], arr[arr.length - 1]);
    }

    /**
     * 生成随机数组用于测试
     * @param len 数组长度
     * @return 随机数组
     */
    private static int[] randomArr(int len) {
        int[] arr = new int[len];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 1000) - 499;  // 范围[-499, 500]
        }
        return arr;
    }

    /**
     * 测试方法：验证三种算法的正确性和性能
     */
    public static void main(String[] args) {
        System.out.println("=== 数组左右划分最大值差值最大问题测试 ===");
        
        // 手动测试用例
        System.out.println("1. 手动测试用例:");
        int[] test1 = {2, 7, 3, 1, 6};
        System.out.println("数组: [2, 7, 3, 1, 6]");
        System.out.println("方法1结果: " + maxABS1(test1));
        System.out.println("方法2结果: " + maxABS2(test1));
        System.out.println("方法3结果: " + maxABS3(test1));
        System.out.println("期望: 5 (全局最大值7 - min(2,6) = 7-2 = 5)");
        System.out.println();
        
        // 随机测试验证正确性
        System.out.println("2. 随机测试验证:");
        boolean allCorrect = true;
        for (int i = 0; i < 10000; i++) {
            int[] arr = randomArr(20 + (int)(Math.random() * 30));  // 长度20-49
            int result1 = maxABS1(arr);
            int result2 = maxABS2(arr);
            int result3 = maxABS3(arr);
            
            if (result1 != result2 || result2 != result3) {
                System.out.println("发现错误! 数组: " + java.util.Arrays.toString(arr));
                System.out.println("方法1: " + result1 + ", 方法2: " + result2 + ", 方法3: " + result3);
                allCorrect = false;
                break;
            }
            
            if ((i + 1) % 2000 == 0) {
                System.out.println("已完成 " + (i + 1) + " 次测试...");
            }
        }
        
        if (allCorrect) {
            System.out.println("✓ 所有测试通过，算法正确！");
        } else {
            System.out.println("✗ 发现算法错误！");
        }
        
        // 性能测试
        System.out.println("\n3. 性能测试:");
        int[] largeArr = randomArr(100000);
        
        long start = System.currentTimeMillis();
        int result2 = maxABS2(largeArr);
        long time2 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result3 = maxABS3(largeArr);
        long time3 = System.currentTimeMillis() - start;
        
        System.out.println("数组长度: " + largeArr.length);
        System.out.println("方法2耗时: " + time2 + " ms, 结果: " + result2);
        System.out.println("方法3耗时: " + time3 + " ms, 结果: " + result3);
        System.out.println("方法3比方法2快 " + (time2 == 0 ? "很多" : String.format("%.1f", (double)time2/time3)) + " 倍");
        
        System.out.println("\n=== 测试完成 ===");
    }
}

