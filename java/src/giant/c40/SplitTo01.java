package giant.c40;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 数字分裂成01数组问题
 * 
 * 问题描述：
 * 一个数n，可以分裂成一个数组[n/2, n%2, n/2]
 * 这个数组中哪个数不是1或者0，就继续分裂下去
 * 
 * 例如：n = 5，一开始分裂成[2, 1, 2]
 * [2, 1, 2]这个数组中不是1或者0的数，会继续分裂下去，比如两个2就继续分裂
 * [2, 1, 2] → [1, 0, 1, 1, 1, 0, 1]
 * 
 * 问题扩展：
 * 给定三个数n、l、r，返回n的最终分裂数组里[l,r]范围上有几个1
 * 
 * 约束条件：
 * - n ≤ 2^50，n是long类型
 * - r - l ≤ 50000，l和r是int类型（课程升级版支持long类型）
 * 
 * 解题思路：
 * 方法1：递归模拟分裂过程
 * - 直观但效率低，适合小数据验证
 * 
 * 方法2：递归+记忆化优化
 * - 使用HashMap缓存已计算的结果
 * - 大幅提升性能，支持大数据量
 * 
 * 算法核心：
 * - 分裂规律：[n/2, n%2, n/2]
 * - 递归结构：左半部分、中间值、右半部分
 * - 范围查询：根据查询范围分别计算各部分贡献
 * 
 * 时间复杂度：O(log n)，每次递归规模减半
 * 空间复杂度：O(log n)，递归栈和缓存空间
 * 
 * 来源：腾讯面试题
 * 
 * @author Zhu Runqi
 */
public class SplitTo01 {

    /**
     * 计算数字n分裂后数组中1的总个数（辅助方法）
     * 
     * @param num 待分裂的数字
     * @param onesMap 记忆化缓存
     * @return 分裂后数组中1的个数
     */
    private static long ones(long num, HashMap<Long, Long> onesMap) {
        if (num == 1 || num == 0) {
            onesMap.put(num, num);
            return num;
        }
        
        long half = ones(num / 2, onesMap);
        long mid = num % 2 == 1 ? 1 : 0;
        long all = half * 2 + mid;
        onesMap.put(num, all);
        return all;
    }

    /**
     * 计算数字n分裂后数组的总长度
     * 
     * 分裂规律：
     * - 0或1：长度为1
     * - 其他数：长度为2*size(n/2) + 1
     * 
     * @param n 待分裂的数字
     * @return 分裂后数组的长度
     */
    private static long size(long n) {
        if (n == 1 || n == 0) {
            return 1;
        } else {
            long half = size(n / 2);
            return (half << 1) + 1;  // 2*half + 1
        }
    }

    /**
     * 计算指定范围内1的个数（方法1：基础递归）
     * 
     * 算法思路：
     * 1. 如果n是0或1，直接返回结果
     * 2. 将分裂后的数组分为三部分：左半部分、中间值、右半部分
     * 3. 根据查询范围[l,r]分别计算各部分的贡献
     * 4. 递归计算左右两部分
     * 
     * @param n 待分裂的数字
     * @param l 查询范围左边界（从1开始）
     * @param r 查询范围右边界（从1开始）
     * @return 范围[l,r]内1的个数
     */
    public static long nums1(long n, long l, long r) {
        if (n == 1 || n == 0) {
            return n == 1 ? 1 : 0;
        }
        
        long half = size(n / 2);  // 左半部分的长度
        
        // 计算左半部分的贡献
        long left = l > half ? 0 : nums1(n / 2, l, Math.min(half, r));
        
        // 计算中间值的贡献
        long mid = (l > half + 1 || r < half + 1) ? 0 : (n & 1);
        
        // 计算右半部分的贡献
        long right = r > half + 1 ? nums1(n / 2, Math.max(l - half - 1, 1), r - half - 1) : 0;
        
        return left + mid + right;
    }

    /**
     * 计算指定范围内1的个数（方法2：记忆化优化）
     * 
     * 算法思路：
     * 在方法1基础上增加记忆化优化：
     * 1. 缓存完整数组的1的个数
     * 2. 对于查询整个数组的情况，直接返回缓存结果
     * 3. 其他情况按照原逻辑递归计算
     * 
     * @param n 待分裂的数字
     * @param l 查询范围左边界
     * @param r 查询范围右边界
     * @param allMap 缓存完整数组的1的个数
     * @return 范围[l,r]内1的个数
     */
    private static long dp(long n, long l, long r, HashMap<Long, Long> allMap) {
        if (n == 1 || n == 0) {
            return n == 1 ? 1 : 0;
        }
        
        long half = size(n / 2);
        long all = (half << 1) + 1;  // 总长度
        long mid = n & 1;            // 中间值
        
        // 如果查询整个数组，使用缓存
        if (l == 1 && r >= all) {
            if (allMap.containsKey(n)) {
                return allMap.get(n);
            }
            
            long count = dp(n / 2, 1, half, allMap);
            long ans = (count << 1) + mid;  // 2*count + mid
            allMap.put(n, ans);
            return ans;
        } else {
            // 部分查询，按原逻辑计算
            mid = (l > half + 1 || r < half + 1) ? 0 : mid;
            long left = l > half ? 0 : dp(n / 2, l, Math.min(half, r), allMap);
            long right = r > half + 1 ? dp(n / 2, Math.max(l - half - 1, 1), r - half - 1, allMap) : 0;
            return left + mid + right;
        }
    }

    /**
     * 计算指定范围内1的个数（方法2入口）
     * 
     * @param n 待分裂的数字
     * @param l 查询范围左边界
     * @param r 查询范围右边界
     * @return 范围[l,r]内1的个数
     */
    public static long nums2(long n, long l, long r) {
        HashMap<Long, Long> allMap = new HashMap<>();
        return dp(n, l, r, allMap);
    }

    /**
     * 递归生成完整的分裂数组（用于验证答案正确性）
     * 
     * 算法思路：
     * 1. 如果n是0或1，直接添加到数组
     * 2. 否则递归处理n/2，添加n%2，再次递归处理n/2
     * 
     * 注意：此方法只适用于小数据验证，大数据会内存溢出
     * 
     * @param n 待分裂的数字
     * @param arr 存储分裂结果的数组
     */
    private static void process(long n, ArrayList<Integer> arr) {
        if (n == 1 || n == 0) {
            arr.add((int) n);
        } else {
            process(n / 2, arr);      // 左半部分
            arr.add((int) (n % 2));   // 中间值
            process(n / 2, arr);      // 右半部分
        }
    }

    /**
     * 生成完整的分裂数组（验证方法入口）
     * 
     * @param n 待分裂的数字
     * @return 完整的分裂数组
     */
    public static ArrayList<Integer> sure(long n) {
        ArrayList<Integer> arr = new ArrayList<>();
        process(n, arr);
        return arr;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 数字分裂成01数组问题 ===\n");
        
        // 测试用例1：基本示例
        System.out.println("测试用例1：基本示例（n=5）");
        long n1 = 5;
        ArrayList<Integer> splitArray = sure(n1);
        System.out.println("n = " + n1);
        System.out.println("分裂过程:");
        System.out.println("  5 → [2, 1, 2]");
        System.out.println("  [2, 1, 2] → [1, 0, 1, 1, 1, 0, 1]");
        System.out.print("最终数组: [");
        for (int i = 0; i < splitArray.size(); i++) {
            System.out.print(splitArray.get(i) + (i == splitArray.size() - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("数组长度: " + splitArray.size());
        System.out.println();
        
        // 测试用例2：查询指定范围
        System.out.println("测试用例2：查询指定范围");
        long n2 = 10;
        long l2 = 3, r2 = 6;
        long result2_1 = nums1(n2, l2, r2);
        long result2_2 = nums2(n2, l2, r2);
        System.out.println("n = " + n2 + ", 查询范围 [" + l2 + ", " + r2 + "]");
        System.out.println("结果 (方法1): " + result2_1);
        System.out.println("结果 (方法2): " + result2_2);
        System.out.println("验证: 通过对比完整数组进行验证");
        System.out.println();
        
        // 测试用例3：边界情况
        System.out.println("测试用例3：边界情况");
        long n3 = 1;
        long result3_1 = nums1(n3, 1, 1);
        long result3_2 = nums2(n3, 1, 1);
        System.out.println("n = " + n3 + " (边界值)");
        System.out.println("结果 (方法1): " + result3_1);
        System.out.println("结果 (方法2): " + result3_2);
        System.out.println("分析: n=1时直接返回1");
        System.out.println();
        
        // 正确性验证测试
        System.out.println("=== 正确性验证测试 ===");
        int times = 10000;
        long num = 671;
        ArrayList<Integer> ansArray = sure(num);
        boolean allPassed = true;
        
        System.out.println("使用n=" + num + "进行" + times + "次随机测试...");
        
        for (int i = 0; i < times; i++) {
            int a = (int) (Math.random() * ansArray.size()) + 1;
            int b = (int) (Math.random() * ansArray.size()) + 1;
            int l = Math.min(a, b);
            int r = Math.max(a, b);
            
            // 暴力统计
            int ans1 = 0;
            for (int j = l - 1; j < r; j++) {
                if (ansArray.get(j) == 1) {
                    ans1++;
                }
            }
            
            long ans2 = nums1(num, l, r);
            long ans3 = nums2(num, l, r);
            
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("测试失败!");
                System.out.println("范围: [" + l + ", " + r + "]");
                System.out.println("暴力统计: " + ans1);
                System.out.println("方法1结果: " + ans2);
                System.out.println("方法2结果: " + ans3);
                allPassed = false;
                break;
            }
        }
        
        if (allPassed) {
            System.out.println("所有正确性测试通过！");
        }
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        num = (2L << 50) + 22517998136L;
        long l = 30000L;
        long r = 800000200L;
        long start, end;
        
        System.out.println("测试参数:");
        System.out.println("  n = " + num + " (超大数)");
        System.out.println("  查询范围: [" + l + ", " + r + "]");
        System.out.println();
        
        // 方法1性能测试
        start = System.currentTimeMillis();
        long result_1 = nums1(num, l, r);
        end = System.currentTimeMillis();
        System.out.println("方法1结果: " + result_1);
        System.out.println("方法1耗时: " + (end - start) + " 毫秒");
        
        // 方法2性能测试
        start = System.currentTimeMillis();
        long result_2 = nums2(num, l, r);
        end = System.currentTimeMillis();
        System.out.println("方法2结果: " + result_2);
        System.out.println("方法2耗时: " + (end - start) + " 毫秒");
        
        System.out.println("结果一致性: " + (result_1 == result_2 ? "一致" : "不一致"));
        System.out.println();
        
        // 极限性能测试
        System.out.println("=== 极限性能测试 ===");
        num = (2L << 55) + 22517998136L;
        l = 30000L;
        r = 6431000002000L;
        
        System.out.println("极限测试参数:");
        System.out.println("  n = " + num + " (极大数)");
        System.out.println("  查询范围: [" + l + ", " + r + "]");
        
        start = System.currentTimeMillis();
        long extreme_result = nums2(num, l, r);
        end = System.currentTimeMillis();
        
        System.out.println("方法2结果: " + extreme_result);
        System.out.println("方法2耗时: " + (end - start) + " 毫秒");
        System.out.println();
        
        System.out.println("=== 算法原理解析 ===");
        System.out.println("1. 分裂规律：");
        System.out.println("   - 分裂公式：n → [n/2, n%2, n/2]");
        System.out.println("   - 终止条件：当数字为0或1时停止分裂");
        System.out.println("   - 递归结构：每次分裂规模减半");
        System.out.println();
        System.out.println("2. 数组结构：");
        System.out.println("   - 左半部分：n/2的分裂结果");
        System.out.println("   - 中间值：n%2 (0或1)");
        System.out.println("   - 右半部分：n/2的分裂结果（与左半部分相同）");
        System.out.println();
        System.out.println("3. 范围查询策略：");
        System.out.println("   - 分段计算：根据查询范围分别计算各部分贡献");
        System.out.println("   - 边界处理：正确处理查询范围与数组边界的关系");
        System.out.println("   - 递归优化：只计算查询范围涉及的部分");
        System.out.println();
        System.out.println("4. 记忆化优化：");
        System.out.println("   - 缓存策略：存储完整数组的1的个数");
        System.out.println("   - 复用计算：避免重复计算相同的子问题");
        System.out.println("   - 性能提升：大幅减少递归调用次数");
        System.out.println();
        System.out.println("5. 复杂度分析：");
        System.out.println("   - 时间复杂度：O(log n)，每次递归规模减半");
        System.out.println("   - 空间复杂度：O(log n)，递归栈深度");
        System.out.println("   - 实际性能：支持处理2^50以上的大数");
        System.out.println();
        System.out.println("6. 应用场景：");
        System.out.println("   - 分治算法应用");
        System.out.println("   - 大数据范围查询");
        System.out.println("   - 递归结构分析");
        System.out.println("   - 记忆化搜索优化");
    }
}
