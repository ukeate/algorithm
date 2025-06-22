package base.str2;

/**
 * 字典序最后子串问题
 * 
 * 问题描述：
 * 给定一个字符串，找到其所有子串中字典序最大的那一个。
 * 
 * 核心思想：
 * 利用DC3算法构造后缀数组，后缀数组中排名最高（最后一个）的后缀
 * 就是字典序最大的子串，因为任何子串都是某个后缀的前缀。
 * 
 * 算法流程：
 * 1. 将字符串转换为数字数组（字符编码）
 * 2. 使用DC3算法构造后缀数组sa[]
 * 3. sa[n-1]就是字典序最大后缀的起始位置
 * 4. 从该位置到字符串末尾就是答案
 * 
 * 示例：s = "abab"
 * 所有后缀：["abab", "bab", "ab", "b"]
 * 排序后：["ab", "abab", "b", "bab"]  
 * 字典序最大：s.substring(sa[3]) = s.substring(1) = "bab"
 * 
 * 应用场景：
 * - 字符串字典序比较问题
 * - 后缀数组相关算法
 * - 字符串处理优化
 * 
 * LeetCode链接：https://leetcode.com/problems/last-substring-in-lexicographical-order/
 * 
 * @author algorithm-base
 * @version 1.0
 */
// https://leetcode.com/problems/last-substring-in-lexicographical-order/
public class LastSubLexicographical {
    
    /**
     * DC3后缀数组算法实现
     * 
     * DC3（Difference Cover Modulo 3）是一种高效的后缀数组构造算法：
     * 1. 将所有后缀按起始位置模3分为三类：位置%3=0, 1, 2
     * 2. 优先处理位置%3≠0的后缀（约占2/3）
     * 3. 对这些后缀按三元组进行基数排序
     * 4. 如果需要，递归处理重复的三元组
     * 5. 利用已排序信息处理位置%3=0的后缀
     * 6. 最后归并得到完整的后缀数组
     * 
     * 时间复杂度：O(N)，其中N为字符串长度
     * 空间复杂度：O(N)
     */
    private static class DC3 {
        public int[] sa;  // 后缀数组：sa[i]表示排名第i的后缀起始位置

        /**
         * DC3算法构造器
         * @param nums 输入的数字数组（字符已转换）
         * @param max 数组中的最大值
         */
        public DC3(int[] nums, int max) {
            sa = sa(nums, max);
        }

        /**
         * 基数排序：稳定排序算法的核心
         * 
         * 基数排序是DC3算法的基础，用于对后缀按指定字符位置排序。
         * 采用计数排序的思想，时间复杂度O(N+K)。
         * 
         * @param nums 原始数组
         * @param input 输入的后缀位置数组
         * @param output 排序后的输出数组
         * @param offset 排序依据的字符偏移量
         * @param n 元素个数
         * @param k 字符集大小
         */
        private void radixPass(int[] nums, int[] input, int[] output, int offset, int n, int k) {
            int[] cnt = new int[k + 1];  // 计数数组
            
            // 第一步：统计每个字符的出现次数
            for (int i = 0; i < n; ++i) {
                cnt[nums[input[i] + offset]]++;
            }
            
            // 第二步：计算累积计数（前缀和），确定每个字符的最终位置
            for (int i = 0, sum = 0; i < cnt.length; ++i) {
                int t = cnt[i];
                cnt[i] = sum;     // 当前字符的起始位置
                sum += t;         // 累积计数
            }
            
            // 第三步：根据计数信息将元素放到正确位置
            for (int i = 0; i < n; ++i) {
                output[cnt[nums[input[i] + offset]]++] = input[i];
            }
        }

        /**
         * 比较两个二元组的字典序
         * @return true if (a1,a2) <= (b1,b2)
         */
        private boolean leq(int a1, int a2, int b1, int b2) {
            return a1 < b1 || (a1 == b1 && a2 <= b2);
        }

        /**
         * 比较两个三元组的字典序
         * @return true if (a1,a2,a3) <= (b1,b2,b3)
         */
        private boolean leq(int a1, int a2, int a3, int b1, int b2, int b3) {
            return a1 < b1 || (a1 == b1 && leq(a2, a3, b2, b3));
        }

        /**
         * DC3算法的核心递归实现
         * 
         * 算法步骤详解：
         * 1. 收集位置%3≠0的后缀索引到S12集合
         * 2. 对S12按三元组(chars[i], chars[i+1], chars[i+2])进行三轮基数排序
         * 3. 为相同三元组分配相同名称，如果有重复则递归处理
         * 4. 构造位置%3=0的后缀集合S0并排序
         * 5. 归并S0和S12得到最终后缀数组
         * 
         * @param nums 字符数组
         * @param n 有效长度
         * @param K 字符集大小
         * @return 后缀数组
         */
        private int[] skew(int[] nums, int n, int K) {
            int n0 = (n + 2) / 3;      // 位置%3=0的后缀数量
            int n1 = (n + 1) / 3;      // 位置%3=1的后缀数量  
            int n2 = n / 3;            // 位置%3=2的后缀数量
            int n02 = n0 + n2;         // S12集合大小
            
            int[] s12 = new int[n02 + 3];   // S12后缀位置数组
            int[] sa12 = new int[n02 + 3];  // S12排序结果

            // 步骤1：收集所有位置%3≠0的后缀位置
            for (int i = 0, j = 0; i < n + (n0 - n1); ++i) {
                if (0 != i % 3) {  // 只要位置%3=1或2
                    s12[j++] = i;
                }
            }

            // 步骤2：对S12进行三轮基数排序（按三元组排序）
            radixPass(nums, s12, sa12, 2, n02, K);   // 按第三个字符排序
            radixPass(nums, sa12, s12, 1, n02, K);   // 按第二个字符排序
            radixPass(nums, s12, sa12, 0, n02, K);   // 按第一个字符排序
            
            // 步骤3：为三元组分配名称（排名）
            int name = 0;  // 当前名称编号
            int c0 = -1, c1 = -1, c2 = -1;  // 上一个三元组
            
            for (int i = 0; i < n02; ++i) {
                // 如果当前三元组与上一个不同，分配新名称
                if (c0 != nums[sa12[i]] || c1 != nums[sa12[i] + 1] || c2 != nums[sa12[i] + 2]) {
                    name++;
                    c0 = nums[sa12[i]];
                    c1 = nums[sa12[i] + 1]; 
                    c2 = nums[sa12[i] + 2];
                }
                
                // 根据位置类型存储名称
                if (1 == sa12[i] % 3) {
                    s12[sa12[i] / 3] = name;           // 位置%3=1
                } else {
                    s12[sa12[i] / 3 + n0] = name;      // 位置%3=2
                }
            }
            
            // 步骤4：如果有重复名称，递归处理
            if (name < n02) {
                sa12 = skew(s12, n02, name);  // 递归调用
                // 更新排名信息
                for (int i = 0; i < n02; i++) {
                    s12[sa12[i]] = i + 1;
                }
            } else {
                // 所有名称都不同，直接构造结果
                for (int i = 0; i < n02; i++) {
                    sa12[s12[i] - 1] = i;
                }
            }
            
            // 步骤5：处理位置%3=0的后缀（S0集合）
            int[] s0 = new int[n0];
            int[] sa0 = new int[n0];
            
            // 从S12的排序结果中提取S0的位置
            for (int i = 0, j = 0; i < n02; i++) {
                if (sa12[i] < n0) {
                    s0[j++] = 3 * sa12[i];  // 将排名转回原始位置
                }
            }
            radixPass(nums, s0, sa0, 0, n0, K);  // 对S0进行基数排序
            
            // 步骤6：归并S0和S12得到最终结果
            int[] sa = new int[n];
            for (int p = 0, t = n0 - n1, k = 0; k < n; k++) {
                // i是S12中当前后缀的位置，j是S0中当前后缀的位置
                int i = sa12[t] < n0 ? sa12[t] * 3 + 1 : (sa12[t] - n0) * 3 + 2;
                int j = sa0[p];
                
                // 比较两个后缀的字典序，选择较小的
                if (sa12[t] < n0 ? 
                    leq(nums[i], s12[sa12[t] + n0], nums[j], s12[j / 3]) :
                    leq(nums[i], nums[i + 1], s12[sa12[t] - n0 + 1], 
                        nums[j], nums[j + 1], s12[j / 3 + n0])) {
                    // S12中的后缀较小
                    sa[k] = i;
                    t++;
                    if (t == n02) {
                        // S12用完，剩余都来自S0
                        for (k++; p < n0; p++, k++) {
                            sa[k] = sa0[p];
                        }
                    }
                } else {
                    // S0中的后缀较小
                    sa[k] = j;
                    p++;
                    if (p == n0) {
                        // S0用完，剩余都来自S12
                        for (k++; t < n02; t++, k++) {
                            sa[k] = sa12[t] < n0 ? sa12[t] * 3 + 1 : (sa12[t] - n0) * 3 + 2;
                        }
                    }
                }
            }
            return sa;
        }

        /**
         * 构造后缀数组的主入口
         * @param nums 字符数组
         * @param max 最大字符值
         * @return 后缀数组
         */
        private int[] sa(int[] nums, int max) {
            int n = nums.length;
            int[] arr = new int[n + 3];  // 末尾补0避免越界
            for (int i = 0; i < n; i++) {
                arr[i] = nums[i];
            }
            return skew(arr, n, max);
        }
    }

    /**
     * 求字典序最后（最大）子串的主方法
     * 
     * 算法思路：
     * 1. 将字符串转换为数字数组（避免字符比较）
     * 2. 构造DC3后缀数组
     * 3. 后缀数组的最后一个元素sa[n-1]对应字典序最大的后缀
     * 4. 该后缀就是所求的字典序最大子串
     * 
     * 示例分析：
     * 输入："abab"
     * 转换：[1,2,1,2] (a->1, b->2)
     * 所有后缀及其字典序：
     * - 位置0: "abab" -> [1,2,1,2]
     * - 位置1: "bab"  -> [2,1,2]  ← 字典序最大
     * - 位置2: "ab"   -> [1,2]
     * - 位置3: "b"    -> [2]
     * 
     * @param s 输入字符串
     * @return 字典序最大的子串
     * 
     * 时间复杂度：O(N)，主要是DC3算法的复杂度
     * 空间复杂度：O(N)，存储数组和后缀数组
     */
    public static String last(String s) {
        // 边界情况处理
        if (s == null || s.length() == 0) {
            return s;
        }
        
        int n = s.length();
        char[] str = s.toCharArray();
        
        // 找到字符的最小值和最大值，用于字符编码
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (char cha : str) {
            min = Math.min(min, cha);
            max = Math.max(max, cha);
        }
        
        // 将字符转换为从1开始的正整数（避免0值干扰）
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = str[i] - min + 1;
        }
        
        // 构造DC3后缀数组
        DC3 dc3 = new DC3(arr, max - min + 1);
        
        // sa[n-1]是字典序最大后缀的起始位置
        // 从该位置到字符串末尾就是答案
        return s.substring(dc3.sa[n - 1]);
    }

    /**
     * 测试方法：验证算法正确性
     * 
     * 测试用例：
     * 输入："abab"
     * 预期输出："bab"
     * 
     * 算法验证：
     * 1. 所有子串：["a", "ab", "aba", "abab", "b", "ba", "bab", "a", "ab", "b"]
     * 2. 字典序排序后最大：bab
     * 3. 使用DC3后缀数组验证结果正确性
     */
    public static void main(String[] args) {
        String testCase = "abab";
        String result = last(testCase);
        
        System.out.println("字典序最后子串问题测试");
        System.out.println("输入字符串: \"" + testCase + "\"");
        System.out.println("字典序最大子串: \"" + result + "\"");
        System.out.println();
        
        // 详细分析过程
        System.out.println("分析过程：");
        System.out.println("所有后缀：");
        for (int i = 0; i < testCase.length(); i++) {
            System.out.println("位置" + i + ": \"" + testCase.substring(i) + "\"");
        }
        System.out.println();
        
        System.out.println("字典序排序：[\"ab\", \"abab\", \"b\", \"bab\"]");
        System.out.println("最大值：\"" + result + "\"");
        System.out.println();
        
        System.out.println("算法复杂度：");
        System.out.println("时间复杂度: O(N) - DC3后缀数组构造");
        System.out.println("空间复杂度: O(N) - 存储后缀数组");
        System.out.println("优势: 相比暴力O(N²)解法大幅提升效率");
    }
}
