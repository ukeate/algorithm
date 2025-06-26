package giant.c7;

/**
 * 数组中两数按位与的最大值问题
 * 
 * 问题描述：
 * 给定一个正整数数组，找出其中任意两个数按位与(&)操作的结果的最大值。
 * 
 * 例如：
 * 数组 [3, 10, 5, 25, 2, 8]
 * 所有可能的按位与结果中，最大的是 10 & 8 = 8
 * 
 * 解决方案：
 * 1. 方法1：暴力枚举法 - 时间复杂度O(N^2)
 * 2. 方法2：位操作优化法 - 时间复杂度O(N*logV)，其中V是数值范围
 * 
 * 核心思想：
 * 方法1：枚举所有数对，计算按位与的最大值
 * 方法2：从最高位开始，贪心地选择能保留该位的数字集合
 * 
 * 位操作优化的关键观察：
 * - 按位与的结果不会超过参与运算的任一个数
 * - 要使结果最大，应该尽可能保留高位的1
 * - 从高位到低位逐位确定答案
 * 
 * 时间复杂度：O(N*logV) - 方法2
 * 空间复杂度：O(1)
 */
public class MaxAndVal {
    
    /**
     * 方法1：暴力枚举法
     * 
     * 算法思路：
     * 枚举所有可能的数对(i,j)，计算arr[i] & arr[j]的最大值
     * 
     * 时间复杂度：O(N^2)
     * 空间复杂度：O(1)
     * 
     * 适用场景：数组长度较小时使用
     * 
     * @param arr 输入的正整数数组
     * @return 任意两数按位与的最大值
     */
    public static int sure(int[] arr) {
        int n = arr.length;
        int max = Integer.MIN_VALUE;
        
        // 枚举所有数对
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // 计算按位与并更新最大值
                max = Math.max(max, arr[i] & arr[j]);
            }
        }
        return max;
    }

    /**
     * 交换数组中两个位置的元素
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
     * 方法2：位操作贪心优化法
     * 
     * 算法思路：
     * 1. 从最高位（第30位）开始向低位遍历
     * 2. 对于每一位，将数组分为两部分：该位为1的数和该位为0的数
     * 3. 如果该位为1的数有至少2个，那么答案的该位可以为1
     * 4. 保留该位为1的数，继续处理下一位
     * 5. 如果该位为1的数少于2个，恢复原数组，继续下一位
     * 
     * 关键观察：
     * - 要使按位与结果最大，应该尽可能保留高位的1
     * - 只有当某一位上至少有2个数为1时，该位才可能在结果中为1
     * - 贪心策略：能确定某位为1时，就过滤掉该位为0的数
     * 
     * 位操作技巧：
     * - (1 << bit) 生成第bit位为1的掩码
     * - (num & (1 << bit)) == 0 检查第bit位是否为0
     * - ans |= (1 << bit) 将答案的第bit位设为1
     * 
     * 时间复杂度：O(N*logV)，其中V是数值范围（最大31位）
     * 空间复杂度：O(1)
     * 
     * @param arr 输入的正整数数组（注意：此方法会修改原数组）
     * @return 任意两数按位与的最大值
     */
    public static int max(int[] arr) {
        int m = arr.length;  // 当前考虑的数组长度
        int ans = 0;         // 最终答案
        
        // 从第30位开始向第0位遍历（Java中int的有效位是31位，第31位是符号位）
        for (int bit = 30; bit >= 0; bit--) {
            int i = 0;
            int tmp = m;  // 备份当前数组长度
            
            // 将数组分区：前面是该位为1的数，后面是该位为0的数
            while (i < m) {
                if ((arr[i] & (1 << bit)) == 0) {
                    // 如果第bit位为0，将该数交换到数组后部
                    swap(arr, i, --m);
                } else {
                    // 如果第bit位为1，继续检查下一个数
                    i++;
                }
            }
            
            // 检查该位为1的数的个数
            if (m == 2) {
                // 如果恰好剩下2个数，直接返回它们的按位与结果
                return arr[0] & arr[1];
            }
            
            if (m < 2) {
                // 如果该位为1的数少于2个，无法确定该位，恢复数组
                m = tmp;
            } else {
                // 如果该位为1的数有2个或以上，答案的该位可以确定为1
                ans |= (1 << bit);
                // 继续用该位为1的数进行下一位的判断
            }
        }
        return ans;
    }

    /**
     * 生成随机数组用于测试
     * @param size 数组大小
     * @param range 数值范围
     * @return 随机生成的数组
     */
    private static int[] randomArr(int size, int range) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = (int) (Math.random() * range) + 1;
        }
        return arr;
    }

    /**
     * 测试方法：验证两种算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 数组中两数按位与最大值测试 ===");
        
        // 手动测试用例
        System.out.println("1. 手动测试用例:");
        int[] test1 = {3, 10, 5, 25, 2, 8};
        System.out.println("测试数组: [3, 10, 5, 25, 2, 8]");
        System.out.println("方法1结果: " + sure(test1.clone()));
        System.out.println("方法2结果: " + max(test1.clone()));
        System.out.println("分析: 10(1010) & 8(1000) = 8(1000)");
        System.out.println();
        
        // 随机测试验证算法正确性
        int times = 1000000;
        int maxLen = 50;
        int maxVal = 30;
        
        System.out.println("2. 随机测试验证:");
        System.out.println("测试次数: " + times);
        System.out.println("最大数组长度: " + maxLen);
        System.out.println("最大数值: " + maxVal);
        System.out.println("开始测试...");
        
        boolean allCorrect = true;
        long start = System.currentTimeMillis();
        
        for (int i = 0; i < times; i++) {
            int size = (int) (Math.random() * maxLen) + 2;
            int[] arr = randomArr(size, maxVal);
            
            int ans1 = sure(arr.clone());  // 使用副本避免原数组被修改
            int ans2 = max(arr.clone());
            
            if (ans1 != ans2) {
                System.out.println("发现错误! 测试用例: " + i);
                System.out.println("数组: " + java.util.Arrays.toString(arr));
                System.out.println("方法1结果: " + ans1);
                System.out.println("方法2结果: " + ans2);
                allCorrect = false;
                break;
            }
            
            // 显示进度
            if ((i + 1) % 200000 == 0) {
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
        int[] perfTestArr = randomArr(1000, 1000);
        
        start = System.currentTimeMillis();
        int result1 = sure(perfTestArr.clone());
        long time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result2 = max(perfTestArr.clone());
        long time2 = System.currentTimeMillis() - start;
        
        System.out.println("方法1（暴力枚举）: " + result1 + ", 耗时: " + time1 + "ms");
        System.out.println("方法2（位操作优化）: " + result2 + ", 耗时: " + time2 + "ms");
        
        if (time2 > 0) {
            System.out.println("性能提升: " + (time1 / (double) time2) + "倍");
        }
        
        System.out.println("\n=== 测试完成 ===");
    }
}
