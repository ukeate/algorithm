package giant.c24;

/**
 * 不包含数字4的数字个数统计问题
 * 
 * 问题描述：
 * 给定一个正整数num，统计在[1, num]范围内，有多少个数字的各位数字中都不包含数字4。
 * 
 * 例如：
 * num = 25时，符合条件的数字有：
 * 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16, 17, 18, 19, 20, 21, 22, 23, 25
 * 总共22个
 * 
 * 不符合条件的数字：4, 14, 24（包含数字4）
 * 
 * 解决方案：
 * 1. 方法1：暴力枚举法 - 时间复杂度O(N*logN)
 * 2. 方法2：数位DP法 - 时间复杂度O(logN)
 * 3. 方法3：进制转换法 - 时间复杂度O(logN)，最优解
 * 
 * 核心思想：
 * 将问题转化为9进制计数问题，因为每一位可以选择的数字只有0,1,2,3,5,6,7,8,9（共9个）
 * 
 * 算法复杂度：
 * 时间复杂度：O(logN)
 * 空间复杂度：O(1)
 */
public class NotContains4 {
    
    /**
     * 判断一个数字是否不包含数字4（暴力法辅助函数）
     * 
     * @param num 待检查的数字
     * @return true表示不包含数字4，false表示包含数字4
     */
    public static boolean isNot4(long num) {
        while (num != 0) {
            if (num % 10 == 4) {
                return false;  // 发现数字4，返回false
            }
            num /= 10;
        }
        return true;  // 没有发现数字4，返回true
    }

    /**
     * 方法1：暴力枚举法
     * 
     * 算法思路：
     * 从1到num逐个检查每个数字，统计不包含数字4的个数
     * 
     * 算法特点：
     * - 思路简单直观，易于理解
     * - 时间复杂度高，不适用于大数据
     * - 适用于验证结果正确性
     * 
     * 时间复杂度：O(N*logN)，N个数字，每个数字需要O(logN)时间检查是否包含4
     * 空间复杂度：O(1)
     * 
     * @param num 上限数字
     * @return [1, num]中不包含数字4的数字个数
     */
    public static long notContains4Nums1(long num) {
        long count = 0;
        for (long i = 1; i <= num; i++) {
            if (isNot4(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 预计算数组：arr[i]表示i-1位数字（允许前导0）中不包含4的数字个数
     * 
     * 计算方法：
     * - 1位数字：0,1,2,3,5,6,7,8,9 共9个
     * - 2位数字：9*9 = 81个
     * - 3位数字：9*9*9 = 729个
     * - ...
     * - i位数字：9^i个
     */
    public static long[] arr = {0L, 1L, 9L, 81L, 729L, 6561L, 59049L, 531441L, 4782969L, 43046721L, 387420489L,
            3486784401L, 31381059609L, 282429536481L, 2541865828329L, 22876792454961L, 205891132094649L,
            1853020188851841L, 16677181699666569L, 150094635296999121L, 1350851717672992089L};

    /**
     * 计算数字的位数
     * @param num 输入数字
     * @return 数字的位数
     */
    private static int len(long num) {
        int len = 0;
        while (num != 0) {
            len++;
            num /= 10;
        }
        return len;
    }

    /**
     * 计算10的幂次，用于提取最高位数字
     * @param len 位数
     * @return 10^(len-1)
     */
    private static long offset(int len) {
        long offset = 1;
        for (int i = 1; i < len; i++) {
            offset *= 10L;
        }
        return offset;
    }

    /**
     * 递归处理函数（方法2使用）
     * 
     * 算法思路：
     * 处理去掉最高位后的剩余数字，计算不包含4的数字个数
     * 
     * @param num 当前处理的数字
     * @param offset 当前位的权重（10^位数-1）
     * @param len 当前数字的位数
     * @return 在限制条件下不包含4的数字个数
     */
    private static long process(long num, long offset, int len) {
        if (len == 0) {
            return 1;  // 没有数字需要处理，返回1种情况（空数字）
        }
        
        // 提取最高位数字
        long first = num / offset;
        
        // 计算当前位可以选择的数字个数
        // 如果first < 4，可以选择0,1,2,...,first
        // 如果first >= 4，可以选择0,1,2,3,5,6,...,first（跳过4）
        long choices = (first < 4 ? first : (first - 1));
        
        // 递归处理剩余位数
        return choices * arr[len] + process(num % offset, offset / 10, len - 1);
    }

    /**
     * 方法2：数位DP法
     * 
     * 算法思路：
     * 使用数位动态规划的思想，按位分析每一位上可以选择的数字
     * 
     * 详细分析：
     * 对于n位数字num：
     * 1. 先统计所有位数小于n的不包含4的数字个数
     * 2. 再统计位数等于n但小于等于num的不包含4的数字个数
     * 
     * 关键计算：
     * - 小于n位的数字个数：arr[1] + arr[2] + ... + arr[n-1]
     * - n位数字的处理：按最高位分类讨论
     * 
     * 时间复杂度：O(logN)，递归深度等于数字位数
     * 空间复杂度：O(1)，不考虑预计算数组
     * 
     * @param num 上限数字
     * @return [1, num]中不包含数字4的数字个数
     */
    public static long notContains4Nums2(long num) {
        if (num <= 0) {
            return 0;
        }
        
        int len = len(num);           // 数字位数
        long offset = offset(len);    // 最高位权重
        long first = num / offset;    // 最高位数字
        
        // 计算结果：
        // 1. arr[len] - 1：所有len位不包含4的数字个数（减1是因为要排除0）
        // 2. (first - (first < 4 ? 1 : 2)) * arr[len]：最高位从1到first-1的情况
        // 3. process(...)：最高位等于first时剩余位的情况
        return arr[len] - 1 + (first - (first < 4 ? 1 : 2)) * arr[len]
                + process(num % offset, offset / 10, len - 1);
    }

    /**
     * 方法3：进制转换法（最优解）
     * 
     * 核心洞察：
     * 不包含数字4的数字计数问题等价于9进制计数问题
     * 
     * 转换原理：
     * - 10进制中可用数字：0,1,2,3,4,5,6,7,8,9
     * - 不包含4的数字：0,1,2,3,5,6,7,8,9 (共9个)
     * - 建立映射：0→0, 1→1, 2→2, 3→3, 5→4, 6→5, 7→6, 8→7, 9→8
     * - 问题转化为：给定10进制数num，求对应的9进制表示的数值
     * 
     * 算法步骤：
     * 1. 从低位到高位逐位处理
     * 2. 对每一位数字cur：
     *    - 如果cur < 4，直接使用cur
     *    - 如果cur >= 4，使用cur-1（跳过4）
     * 3. 将处理后的数字按9进制计算最终结果
     * 
     * 算法优势：
     * - 思路清晰，基于进制转换的数学原理
     * - 代码简洁，易于实现
     * - 时间和空间复杂度都是最优的
     * 
     * 时间复杂度：O(logN)，处理每一位数字
     * 空间复杂度：O(1)，只使用常数额外空间
     * 
     * @param num 上限数字
     * @return [1, num]中不包含数字4的数字个数
     */
    public static long notContains4Nums3(long num) {
        if (num <= 0) {
            return 0;
        }
        
        long ans = 0;    // 最终结果
        long base = 1;   // 当前位的权重（9进制）
        long cur = 0;    // 当前位的数字
        
        // 从低位到高位逐位处理
        for (; num > 0; num /= 10, base *= 9) {
            cur = num % 10;  // 提取当前位数字
            
            // 进制转换：将10进制数字映射到9进制
            // 0,1,2,3 -> 0,1,2,3
            // 5,6,7,8,9 -> 4,5,6,7,8 (跳过4)
            ans += (cur < 4 ? cur : cur - 1) * base;
        }
        
        return ans;
    }

    /**
     * 测试方法：验证三种算法的正确性和性能
     */
    public static void main(String[] args) {
        System.out.println("=== 不包含数字4的数字个数统计问题测试 ===");
        
        // 小规模正确性验证
        System.out.println("1. 小规模正确性验证:");
        long[] testCases = {10, 25, 50, 100, 444, 1000};
        
        for (long test : testCases) {
            if (test <= 1000) {  // 暴力法只适用于小数据
                long result1 = notContains4Nums1(test);
                long result2 = notContains4Nums2(test);
                long result3 = notContains4Nums3(test);
                System.out.printf("num = %4d: 暴力法 = %4d, 数位DP = %4d, 进制转换 = %4d, %s%n", 
                    test, result1, result2, result3, 
                    (result1 == result2 && result2 == result3) ? "✓" : "✗");
            } else {
                long result2 = notContains4Nums2(test);
                long result3 = notContains4Nums3(test);
                System.out.printf("num = %4d: 数位DP = %4d, 进制转换 = %4d, %s%n", 
                    test, result2, result3, (result2 == result3) ? "✓" : "✗");
            }
        }
        
        System.out.println();
        
        // 大规模正确性验证
        System.out.println("2. 大规模算法一致性验证:");
        long max = 88888888L;
        System.out.println("验证范围: [1, " + max + "]");
        System.out.println("开始验证...");
        
        boolean allCorrect = true;
        for (long i = 1; i <= max; i++) {
            if (notContains4Nums2(i) != notContains4Nums3(i)) {
                System.out.println("发现错误! num = " + i);
                System.out.println("数位DP结果: " + notContains4Nums2(i));
                System.out.println("进制转换结果: " + notContains4Nums3(i));
                allCorrect = false;
                break;
            }
            
            if (i % 10000000 == 0) {
                System.out.println("已验证到 " + i + "...");
            }
        }
        
        if (allCorrect) {
            System.out.println("✓ 大规模验证通过，算法正确！");
        } else {
            System.out.println("✗ 发现算法错误！");
        }
        
        // 性能测试
        System.out.println("\n3. 性能测试:");
        long num = 8173528638135L;
        long start, end;
        
        System.out.println("测试数字: " + num);
        System.out.println("暴力法由于时间过长，跳过测试");
        
        start = System.currentTimeMillis();
        long ans2 = notContains4Nums2(num);
        end = System.currentTimeMillis();
        System.out.println("数位DP法: 结果 = " + ans2 + ", 耗时 = " + (end - start) + " ms");

        start = System.currentTimeMillis();
        long ans3 = notContains4Nums3(num);
        end = System.currentTimeMillis();
        System.out.println("进制转换法: 结果 = " + ans3 + ", 耗时 = " + (end - start) + " ms");
        
        System.out.println("结果一致性: " + (ans2 == ans3 ? "✓" : "✗"));
        
        System.out.println("\n=== 测试完成 ===");
    }
}
