package giant.c40;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * 拼出最大能被3整除的数问题
 * 
 * 问题描述：
 * 给定一个数组arr，里面的数字都是0~9
 * 你可以随意使用arr中的数字，哪怕打乱顺序也行
 * 请拼出一个能被3整除的、最大的数字，用字符串形式返回
 * 
 * 数学原理：
 * 一个数能被3整除，当且仅当各位数字之和能被3整除
 * 
 * 解题思路：
 * 方法1：递归枚举所有子集（暴力解法）
 * 方法2：递归+剪枝优化
 * 方法3：贪心+数学优化（最优解法）
 * 
 * 贪心策略：
 * 1. 计算所有数字之和sum
 * 2. 如果sum%3==0，说明所有数从大到小拼起来就可以了
 * 3. 如果sum%3==1，说明多了一个余数1，
 * 只需要删掉一个最小的数(该数是%3==1的数);
 * 如果没有，只需要删掉两个最小的数(这两个数都是%3==2的数);
 * 4. 如果sum%3==2，说明多了一个余数2，
 * 只需要删掉一个最小的数(该数是%3==2的数);
 * 如果没有，只需要删掉两个最小的数(这两个数都是%3==1的数);
 * 
 * 算法核心：
 * - 数学性质：利用模3运算的性质
 * - 贪心选择：优先删除对结果影响最小的数字
 * - 字典序最大：从大到小排列剩余数字
 * 
 * 时间复杂度：O(n log n)，主要是排序
 * 空间复杂度：O(n)
 * 
 * 来源：去哪儿网面试题
 * 
 * @author Zhu Runqi
 */
public class Mod3Max {

    /**
     * 递归枚举所有子集（方法1，暴力解法）
     * 
     * 算法思路：
     * 1. 对每个数字决定是否选择
     * 2. 检查选择的数字组成的数是否能被3整除
     * 3. 在所有满足条件的数中找最大的
     * 
     * 注意：此方法效率很低，仅用于验证答案正确性
     * 
     * @param arr 输入数组
     * @param idx 当前考虑的数字索引
     * @param builder 当前构建的数字字符串
     * @param set 存储所有满足条件的数字
     */
    private static void process1(int[] arr, int idx, StringBuilder builder, TreeSet<String> set) {
        if (idx == arr.length) {
            if (builder.length() != 0 && Integer.valueOf(builder.toString()) % 3 == 0) {
                set.add(builder.toString());
            }
        } else {
            // 不选择当前数字
            process1(arr, idx + 1, builder, set);
            
            // 选择当前数字
            builder.append(arr[idx]);
            process1(arr, idx + 1, builder, set);
            builder.deleteCharAt(builder.length() - 1);
        }
    }

    /**
     * 枚举所有子集求最大能被3整除的数（方法1入口）
     * 
     * @param arr 输入数组
     * @return 最大能被3整除的数字字符串
     */
    public static String max1(int[] arr) {
        // 从大到小排序，确保字典序最大
        Arrays.sort(arr);
        for (int l = 0, r = arr.length - 1; l < r; l++, r--) {
            int tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
        }
        
        StringBuilder builder = new StringBuilder();
        // 按数值大小降序排列的TreeSet
        TreeSet<String> set = new TreeSet<>((a, b) -> Integer.valueOf(b).compareTo(Integer.valueOf(a)));
        process1(arr, 0, builder, set);
        
        return set.isEmpty() ? "" : set.first();
    }

    /**
     * 计算下一个模3的值
     * 
     * 根据当前模值和要求的目标模值，计算需要加上什么值
     * 
     * @param require 目标模值
     * @param current 当前模值
     * @return 需要加上的值的模3结果
     */
    private static int nextMod(int require, int current) {
        if (require == 0) {
            if (current == 0) {
                return 0;
            } else if (current == 1) {
                return 2;  // (1 + 2) % 3 = 0
            } else {
                return 1;  // (2 + 1) % 3 = 0
            }
        } else if (require == 1) {
            if (current == 0) {
                return 1;  // (0 + 1) % 3 = 1
            } else if (current == 1) {
                return 0;  // (1 + 0) % 3 = 1
            } else {
                return 2;  // (2 + 2) % 3 = 1
            }
        } else {  // require == 2
            if (current == 0) {
                return 2;  // (0 + 2) % 3 = 2
            } else if (current == 1) {
                return 1;  // (1 + 1) % 3 = 2
            } else {
                return 0;  // (2 + 0) % 3 = 2
            }
        }
    }

    /**
     * 比较两个数字字符串的大小
     * 
     * @param p1 第一个数字字符串
     * @param p2 第二个数字字符串
     * @return p1是否小于p2
     */
    private static boolean smaller(String p1, String p2) {
        if (p1.length() != p2.length()) {
            return p1.length() < p2.length();
        }
        return p1.compareTo(p2) < 0;
    }

    /**
     * 递归求解最大能被3整除的数（方法2，带剪枝优化）
     * 
     * 算法思路：
     * 使用递归+记忆化，追踪当前的模3值
     * 
     * @param arr 从大到小排序的输入数组
     * @param idx 当前考虑的数字索引
     * @param mod 当前数字和的模3值
     * @return 从当前状态能构成的最大能被3整除的数字字符串
     */
    private static String process2(int[] arr, int idx, int mod) {
        if (idx == arr.length) {
            return mod == 0 ? "" : "$";  // "$"表示无效解
        }
        
        // 选择当前数字
        String p1 = "$";
        int nextMod = nextMod(mod, arr[idx] % 3);
        String next = process2(arr, idx + 1, nextMod);
        if (!next.equals("$")) {
            p1 = arr[idx] + next;
        }
        
        // 不选择当前数字
        String p2 = process2(arr, idx + 1, mod);
        
        if (p1.equals("$") && p2.equals("$")) {
            return "$";
        }
        if (!p1.equals("$") && !p2.equals("$")) {
            return smaller(p1, p2) ? p2 : p1;
        }
        return p1.equals("$") ? p2 : p1;
    }

    /**
     * 递归+剪枝求解（方法2入口）
     * 
     * @param arr 输入数组
     * @return 最大能被3整除的数字字符串
     */
    public static String max2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        
        // 从大到小排序
        Arrays.sort(arr);
        for (int l = 0, r = arr.length - 1; l < r; l++, r--) {
            int tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
        }
        
        // 特殊情况：最大数字是0
        if (arr[0] == 0) {
            return "0";
        }
        
        String ans = process2(arr, 0, 0);
        // 去除前导0
        String res = ans.replaceAll("^(0+)", "");
        if (!res.equals("")) {
            return res;
        }
        return ans.equals("") ? ans : "0";
    }

    /**
     * 比较函数，用于贪心算法的排序
     * 
     * 排序规则：
     * - 余数相同：数值大的在前
     * - 余数不同：余数为0的最前，余数为second的中间，余数为first的最后
     * 
     * @param a 第一个数
     * @param b 第二个数
     * @param second 次优先的余数
     * @return 比较结果
     */
    private static int compare(int a, int b, int second) {
        int ma = a % 3;
        int mb = b % 3;
        if (ma == mb) {
            return b - a;  // 相同余数，大数在前
        } else {
            if (ma == 0 || mb == 0) {
                return ma == 0 ? -1 : 1;  // 余数0优先
            } else {
                return ma == second ? -1 : 1;  // second余数次优先
            }
        }
    }

    /**
     * 移除策略：删除最小影响的数字使和能被3整除
     * 
     * 策略：
     * 1. 删除一个最小的余数为first的数
     * 2. 如果不行，删除两个最小的余数为second的数
     * 
     * @param arr 数字列表
     * @param first 优先删除的余数
     * @param second 次选删除的余数
     * @return 是否成功删除
     */
    private static boolean remove(ArrayList<Integer> arr, int first, int second) {
        if (arr.size() == 0) {
            return false;
        }
        
        // 按比较规则排序
        arr.sort((a, b) -> compare(a, b, second));
        int size = arr.size();
        
        // 尝试删除一个余数为first的数
        if (arr.get(size - 1) % 3 == first) {
            arr.remove(size - 1);
            return true;
        } 
        // 尝试删除两个余数为second的数
        else if (size > 1 && arr.get(size - 1) % 3 == second && arr.get(size - 2) % 3 == second) {
            arr.remove(size - 1);
            arr.remove(size - 2);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 贪心算法求解最大能被3整除的数（方法3，最优解法）
     * 
     * 算法思路：
     * 1. 计算所有数字之和sum
     * 2. 根据sum%3的值采取不同策略：
     *    - sum%3==0：直接从大到小拼接
     *    - sum%3==1：删除最小的余数1的数，或删除两个最小的余数2的数
     *    - sum%3==2：删除最小的余数2的数，或删除两个最小的余数1的数
     * 3. 将剩余数字从大到小排列
     * 
     * @param nums 输入数组
     * @return 最大能被3整除的数字字符串
     */
    public static String max3(int[] nums) {
        if (nums == null || nums.length == 0) {
            return "";
        }
        
        int mod = 0;
        ArrayList<Integer> arr = new ArrayList<>();
        for (int num : nums) {
            arr.add(num);
            mod += num;
            mod %= 3;
        }
        
        // 根据余数决定删除策略
        if ((mod == 1 || mod == 2) && !remove(arr, mod, 3 - mod)) {
            return "";  // 无法构造出能被3整除的数
        }
        
        if (arr.isEmpty()) {
            return "";
        }
        
        // 从大到小排序
        arr.sort((a, b) -> b - a);
        
        // 特殊情况：最大数字是0
        if (arr.get(0) == 0) {
            return "0";
        }
        
        // 构造结果字符串
        StringBuilder builder = new StringBuilder();
        for (int num : arr) {
            builder.append(num);
        }
        
        return builder.toString();
    }

    /**
     * 生成随机测试数组
     * 
     * @param len 数组长度
     * @return 随机数组（元素为0-9）
     */
    private static int[] randomArr(int len) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * 10);
        }
        return arr;
    }

    /**
     * 复制数组
     * 
     * @param arr 原数组
     * @return 复制的数组
     */
    private static int[] copy(int[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 拼出最大能被3整除的数问题 ===\n");
        
        // 测试用例1：能拼出被3整除的数
        System.out.println("测试用例1：能拼出被3整除的数");
        int[] arr1 = {3, 1, 4, 1, 5, 9};
        String result1_1 = max1(copy(arr1));
        String result1_2 = max2(copy(arr1));
        String result1_3 = max3(copy(arr1));
        System.out.println("数组: [3, 1, 4, 1, 5, 9]");
        System.out.println("结果 (方法1): " + result1_1);
        System.out.println("结果 (方法2): " + result1_2);
        System.out.println("结果 (方法3): " + result1_3);
        System.out.println("分析: 数字和=23，23%3=2，需要删除余数为2的数");
        System.out.println();
        
        // 测试用例2：包含0的情况
        System.out.println("测试用例2：包含0的情况");
        int[] arr2 = {0, 0, 0};
        String result2_1 = max1(copy(arr2));
        String result2_2 = max2(copy(arr2));
        String result2_3 = max3(copy(arr2));
        System.out.println("数组: [0, 0, 0]");
        System.out.println("结果 (方法1): " + result2_1);
        System.out.println("结果 (方法2): " + result2_2);
        System.out.println("结果 (方法3): " + result2_3);
        System.out.println("分析: 全是0，结果应该是\"0\"");
        System.out.println();
        
        // 测试用例3：无法拼出被3整除的数
        System.out.println("测试用例3：无法拼出被3整除的数");
        int[] arr3 = {1, 1};
        String result3_1 = max1(copy(arr3));
        String result3_2 = max2(copy(arr3));
        String result3_3 = max3(copy(arr3));
        System.out.println("数组: [1, 1]");
        System.out.println("结果 (方法1): " + result3_1);
        System.out.println("结果 (方法2): " + result3_2);
        System.out.println("结果 (方法3): " + result3_3);
        System.out.println("分析: 数字和=2，无法通过删除得到被3整除的数");
        System.out.println();
        
        // 测试用例4：和已经被3整除
        System.out.println("测试用例4：和已经被3整除");
        int[] arr4 = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        String result4_1 = max1(copy(arr4));
        String result4_2 = max2(copy(arr4));
        String result4_3 = max3(copy(arr4));
        System.out.println("数组: [9, 8, 7, 6, 5, 4, 3, 2, 1]");
        System.out.println("结果 (方法1): " + result4_1);
        System.out.println("结果 (方法2): " + result4_2);
        System.out.println("结果 (方法3): " + result4_3);
        System.out.println("分析: 数字和=45，45%3=0，直接从大到小排列");
        System.out.println();
        
        // 测试用例5：需要删除两个数的情况
        System.out.println("测试用例5：需要删除两个数的情况");
        int[] arr5 = {2, 2, 1};
        String result5_1 = max1(copy(arr5));
        String result5_2 = max2(copy(arr5));
        String result5_3 = max3(copy(arr5));
        System.out.println("数组: [2, 2, 1]");
        System.out.println("结果 (方法1): " + result5_1);
        System.out.println("结果 (方法2): " + result5_2);
        System.out.println("结果 (方法3): " + result5_3);
        System.out.println("分析: 数字和=5，5%3=2，删除两个余数为2的数");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int times = 1000;
        int maxLen = 10;
        boolean allPassed = true;
        
        System.out.println("开始随机测试...");
        
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * maxLen);
            int[] arr1_test = randomArr(len);
            int[] arr2_test = copy(arr1_test);
            int[] arr3_test = copy(arr1_test);
            
            String ans1 = max1(arr1_test);
            String ans2 = max2(arr2_test);
            String ans3 = max3(arr3_test);
            
            if (!ans1.equals(ans2) || !ans1.equals(ans3)) {
                System.out.println("测试失败: 结果不一致");
                System.out.print("数组: [");
                for (int j = 0; j < arr1_test.length; j++) {
                    System.out.print(arr1_test[j] + (j == arr1_test.length - 1 ? "" : ", "));
                }
                System.out.println("]");
                System.out.println("方法1结果: " + ans1);
                System.out.println("方法2结果: " + ans2);
                System.out.println("方法3结果: " + ans3);
                allPassed = false;
                break;
            }
        }
        
        if (allPassed) {
            System.out.println("所有随机测试通过！");
        }
        
        System.out.println("\n=== 算法原理解析 ===");
        System.out.println("1. 数学基础：");
        System.out.println("   - 被3整除判定：各位数字之和能被3整除");
        System.out.println("   - 模运算性质：(a+b)%3 = (a%3 + b%3)%3");
        System.out.println("   - 删除策略：根据总和的余数决定删除方案");
        System.out.println();
        System.out.println("2. 贪心策略：");
        System.out.println("   - 最大字典序：从大到小排列剩余数字");
        System.out.println("   - 最小删除：删除对结果影响最小的数字");
        System.out.println("   - 余数分类：按照模3的结果进行分类处理");
        System.out.println();
        System.out.println("3. 删除规则：");
        System.out.println("   - sum%3==1：删1个余数1的数，或删2个余数2的数");
        System.out.println("   - sum%3==2：删1个余数2的数，或删2个余数1的数");
        System.out.println("   - 优先删除数值小的数字，保证结果最大");
        System.out.println();
        System.out.println("4. 特殊情况：");
        System.out.println("   - 全0数组：返回\"0\"而不是空字符串");
        System.out.println("   - 前导0：需要去除前导0");
        System.out.println("   - 无解情况：无法构造出被3整除的数");
        System.out.println();
        System.out.println("5. 复杂度分析：");
        System.out.println("   - 时间复杂度：O(n log n)，主要是排序");
        System.out.println("   - 空间复杂度：O(n)，存储数字列表");
        System.out.println("   - 比递归O(2^n)解法快得多");
        System.out.println();
        System.out.println("6. 应用场景：");
        System.out.println("   - 数字重排问题");
        System.out.println("   - 约束优化问题");
        System.out.println("   - 贪心算法应用");
    }
}
