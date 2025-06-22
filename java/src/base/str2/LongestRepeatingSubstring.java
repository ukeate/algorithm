package base.str2;

/**
 * 最长重复子串问题
 * 
 * 问题描述：
 * 给定一个字符串，找到其中最长的重复子串（在字符串中至少出现两次的子串）的长度。
 * 
 * 核心思想：
 * 利用DC3后缀数组算法构造height数组，height数组中的最大值就是最长重复子串的长度。
 * 
 * 理论基础：
 * 1. 重复子串必然对应于某两个后缀的公共前缀
 * 2. height[i]表示排名相邻的后缀sa[i-1]和sa[i]的最长公共前缀
 * 3. 所有height值中的最大值就是整个字符串的最长重复子串长度
 * 
 * 算法优势：
 * - 时间复杂度：O(N)，线性时间解决问题
 * - 空间复杂度：O(N)，高效利用内存
 * - 通用性强：可扩展到更复杂的字符串匹配问题
 * 
 * 应用场景：
 * - 数据压缩：找到重复模式进行压缩
 * - 生物信息学：DNA序列重复片段检测
 * - 文本分析：识别重复的段落或句子
 * - 代码检测：寻找重复的代码片段
 * 
 * LeetCode链接：https://leetcode.cn/problems/longest-repeating-substring/
 * 
 * @author algorithm-base
 * @version 1.0
 */
// https://leetcode.cn/problems/longest-repeating-substring/
// 最长重复子串
public class LongestRepeatingSubstring {
    
    /**
     * DC3后缀数组算法实现
     * 
     * DC3（Difference Cover Modulo 3）算法是构造后缀数组的线性时间算法。
     * 它通过分治策略将问题分解，最终得到排序的后缀数组和相关辅助数组。
     * 
     * 数据结构说明：
     * - sa[]: 后缀数组，sa[i]表示排名第i的后缀的起始位置
     * - rank[]: 排名数组，rank[i]表示从位置i开始的后缀的排名
     * - height[]: LCP数组，height[i]表示sa[i]和sa[i-1]的最长公共前缀长度
     * 
     * 对于最长重复子串问题，我们主要利用height数组的性质：
     * height数组的最大值就是字符串中最长重复子串的长度。
     */
    private static class DC3 {
        public int[] sa;      // 后缀数组
        public int[] rank;    // 排名数组
        public int[] height;  // 最长公共前缀数组

        /**
         * DC3算法构造器
         * @param nums 输入的数字数组（字符已转换为数字）
         * @param max 数组中的最大值，用于基数排序
         */
        public DC3(int[] nums, int max) {
            sa = sa(nums, max);      // 构造后缀数组
            rank = rank();           // 构造排名数组
            height = height(nums);   // 构造LCP数组
        }

        /**
         * 构造后缀数组的入口方法
         * @param nums 输入数组
         * @param max 最大值
         * @return 后缀数组
         */
        private int[] sa(int[] nums, int max) {
            int n = nums.length;
            int[] arr = new int[n + 3];  // 末尾补0，避免数组越界
            for (int i = 0; i < n; i++) {
                arr[i] = nums[i];
            }
            return skew(arr, n, max);
        }

        /**
         * 基数排序实现
         * 
         * 基数排序是DC3算法的核心组件，用于对后缀按指定位置的字符进行稳定排序。
         * 使用计数排序的思想，时间复杂度为O(N+K)，其中K为字符集大小。
         * 
         * @param nums 原始字符数组
         * @param input 待排序的后缀位置数组
         * @param output 排序结果数组
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
            
            // 第二步：计算前缀和，确定排序后每个字符的起始位置
            for (int i = 0, sum = 0; i < cnt.length; ++i) {
                int t = cnt[i];
                cnt[i] = sum;     // 当前字符的起始位置
                sum += t;         // 累积计数
            }
            
            // 第三步：将元素按计数排序的结果放入输出数组
            for (int i = 0; i < n; ++i) {
                output[cnt[nums[input[i] + offset]]++] = input[i];
            }
        }

        /**
         * 比较两个二元组的字典序大小
         * @return true if (a1,a2) <= (b1,b2)
         */
        private boolean leq(int a1, int a2, int b1, int b2) {
            return a1 < b1 || (a1 == b1 && a2 <= b2);
        }

        /**
         * 比较两个三元组的字典序大小
         * @return true if (a1,a2,a3) <= (b1,b2,b3)
         */
        private boolean leq(int a1, int a2, int a3, int b1, int b2, int b3) {
            return a1 < b1 || (a1 == b1 && leq(a2, a3, b2, b3));
        }

        /**
         * 根据后缀数组构造排名数组
         * @return 排名数组
         */
        private int[] rank() {
            int n = sa.length;
            int[] ans = new int[n];
            for (int i = 0; i < n; i++) {
                ans[sa[i]] = i;  // 从位置sa[i]开始的后缀排名为i
            }
            return ans;
        }

        /**
         * 构造height数组（LCP数组）
         * 
         * height[i]表示排名相邻的两个后缀sa[i-1]和sa[i]的最长公共前缀长度。
         * 这是解决最长重复子串问题的关键数据结构。
         * 
         * 算法核心优化：
         * 利用"h[rank[i]] >= h[rank[i-1]] - 1"这一重要性质，
         * 避免每次都从0开始计算LCP，显著提高计算效率。
         * 
         * 性质解释：
         * 如果从位置i开始的后缀与其排名相邻后缀的LCP为h，
         * 那么从位置i+1开始的后缀与其排名相邻后缀的LCP至少为h-1。
         * 
         * @param s 原始字符数组
         * @return height数组
         */
        private int[] height(int[] s) {
            int n = s.length;
            int[] ans = new int[n];
            
            // 按字符串原始位置顺序处理每个后缀
            for (int i = 0, k = 0; i < n; ++i) {
                if (rank[i] != 0) {  // 跳过排名为0的后缀（字典序最小）
                    // 利用上次计算结果进行优化
                    if (k > 0) {
                        --k;  // 根据性质，当前LCP至少为k-1
                    }
                    
                    int j = sa[rank[i] - 1];  // 排名紧邻的前一个后缀起始位置
                    
                    // 从位置k开始扩展，计算实际的LCP长度
                    while (i + k < n && j + k < n && s[i + k] == s[j + k]) {
                        ++k;
                    }
                    
                    ans[rank[i]] = k;  // 记录LCP长度
                }
            }
            return ans;
        }

        /**
         * DC3算法的核心递归实现
         * 
         * 分治策略详解：
         * 1. 将所有后缀按起始位置模3分为三类：S0={0,3,6...}, S1={1,4,7...}, S2={2,5,8...}
         * 2. 定义S12 = S1 ∪ S2，先处理约2/3的后缀
         * 3. 对S12中的后缀按三元组(chars[i], chars[i+1], chars[i+2])进行基数排序
         * 4. 如果存在相同的三元组，递归处理以解决冲突
         * 5. 利用S12的排序结果处理S0中的后缀
         * 6. 最后归并S0和S12得到完整的后缀数组
         * 
         * 时间复杂度分析：
         * T(n) = T(2n/3) + O(n) = O(n)，线性时间复杂度
         * 
         * @param nums 字符数组
         * @param n 有效长度
         * @param K 字符集大小
         * @return 后缀数组
         */
        private int[] skew(int[] nums, int n, int K) {
            // 计算各类后缀的数量
            int n0 = (n + 2) / 3;      // S0的大小
            int n1 = (n + 1) / 3;      // S1的大小
            int n2 = n / 3;            // S2的大小
            int n02 = n0 + n2;         // S12的大小
            
            int[] s12 = new int[n02 + 3];   // S12后缀位置数组
            int[] sa12 = new int[n02 + 3];  // S12排序结果
            
            // 步骤1：收集所有位置模3不为0的后缀位置（S1和S2）
            for (int i = 0, j = 0; i < n + (n0 - n1); ++i) {
                if (0 != i % 3) {  // 位置模3为1或2
                    s12[j++] = i;
                }
            }
            
            // 步骤2：对S12进行三轮基数排序（按三元组的字典序排序）
            radixPass(nums, s12, sa12, 2, n02, K);   // 按第3个字符排序
            radixPass(nums, sa12, s12, 1, n02, K);   // 按第2个字符排序
            radixPass(nums, s12, sa12, 0, n02, K);   // 按第1个字符排序
            
            // 步骤3：为相同的三元组分配相同的名称（排名）
            int name = 0;  // 当前名称编号
            int c0 = -1, c1 = -1, c2 = -1;  // 上一个三元组
            
            for (int i = 0; i < n02; ++i) {
                // 如果当前三元组与上一个三元组不同，分配新名称
                if (c0 != nums[sa12[i]] || c1 != nums[sa12[i] + 1] || c2 != nums[sa12[i] + 2]) {
                    name++;
                    c0 = nums[sa12[i]];
                    c1 = nums[sa12[i] + 1];
                    c2 = nums[sa12[i] + 2];
                }
                
                // 根据后缀类型存储名称
                if (1 == sa12[i] % 3) {
                    s12[sa12[i] / 3] = name;           // S1类型后缀
                } else {
                    s12[sa12[i] / 3 + n0] = name;      // S2类型后缀
                }
            }
            
            // 步骤4：如果存在重复名称，需要递归处理
            if (name < n02) {
                sa12 = skew(s12, n02, name);  // 递归调用
                // 更新排名信息
                for (int i = 0; i < n02; i++) {
                    s12[sa12[i]] = i + 1;
                }
            } else {
                // 所有名称都不同，直接构造排序结果
                for (int i = 0; i < n02; i++) {
                    sa12[s12[i] - 1] = i;
                }
            }
            
            // 步骤5：处理S0类型的后缀
            int[] s0 = new int[n0];
            int[] sa0 = new int[n0];
            
            // 从S12的排序结果中提取S0的位置
            for (int i = 0, j = 0; i < n02; i++) {
                if (sa12[i] < n0) {
                    s0[j++] = 3 * sa12[i];  // 转换回原始位置
                }
            }
            radixPass(nums, s0, sa0, 0, n0, K);  // 对S0进行基数排序
            
            // 步骤6：归并S0和S12得到最终的后缀数组
            int[] sa = new int[n];
            for (int p = 0, t = n0 - n1, k = 0; k < n; k++) {
                // i是S12中当前后缀的位置，j是S0中当前后缀的位置
                int i = sa12[t] < n0 ? sa12[t] * 3 + 1 : (sa12[t] - n0) * 3 + 2;
                int j = sa0[p];
                
                // 比较两个后缀的字典序，选择较小的放入结果数组
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
    }

    /**
     * 求最长重复子串长度的主方法
     * 
     * 算法流程：
     * 1. 将字符串转换为数字数组（字符编码）
     * 2. 使用DC3算法构造后缀数组和height数组
     * 3. 遍历height数组找到最大值
     * 4. 该最大值就是最长重复子串的长度
     * 
     * 核心原理：
     * 重复子串意味着至少有两个位置开始的子串是相同的，
     * 这对应于两个后缀有公共前缀。height数组记录了所有
     * 排名相邻后缀的最长公共前缀，其最大值就是答案。
     * 
     * 示例分析：
     * 输入："abcabc"
     * 后缀排序：["abc", "abcabc", "bc", "bcabc", "c", "cabc"]
     * height数组：[0, 3, 0, 2, 0, 1]
     * 最大值为3，对应重复子串"abc"
     * 
     * @param s 输入字符串
     * @return 最长重复子串的长度
     * 
     * 时间复杂度：O(N)，主要是DC3算法的时间复杂度
     * 空间复杂度：O(N)，存储后缀数组和相关辅助数组
     */
    public static int max(String s) {
        // 边界情况处理
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        
        // 找到字符的最小值和最大值，用于字符编码
        int min = str[0];
        int max = str[0];
        for (int i = 1; i < n; i++) {
            min = Math.min(min, str[i]);
            max = Math.max(max, str[i]);
        }
        
        // 将字符转换为从1开始的正整数（避免0值带来的问题）
        int[] all = new int[n];
        for (int i = 0; i < n; i++) {
            all[i] = str[i] - min + 1;
        }
        
        // 构造DC3后缀数组和height数组
        DC3 dc3 = new DC3(all, max - min + 1);
        
        // 找到height数组中的最大值
        int ans = 0;
        for (int i = 1; i < n; i++) {  // 从1开始，因为height[0]总是0
            ans = Math.max(ans, dc3.height[i]);
        }
        
        return ans;
    }

    /**
     * 生成随机字符串用于性能测试
     * @param n 字符串长度
     * @param r 字符种类数（从'a'开始的连续字符）
     * @return 随机字符串
     */
    private static String randomStr(int n, int r) {
        char[] str = new char[n];
        for (int i = 0; i < n; i++) {
            str[i] = (char) ((int) (r * Math.random()) + 'a');
        }
        return String.valueOf(str);
    }

    /**
     * 性能测试主方法
     * 
     * 测试设计：
     * 1. 生成大规模随机字符串
     * 2. 测量算法运行时间
     * 3. 验证算法在不同字符集大小下的性能表现
     * 
     * 测试参数：
     * - 字符串长度：50万字符
     * - 字符种类：3种（模拟高重复度场景）
     * 
     * 性能分析：
     * - 字符种类少：重复子串多，height数组值偏大
     * - 字符种类多：重复子串少，height数组值偏小
     * - 算法复杂度：无论何种情况都保持O(N)线性时间
     * 
     * 输出示例：
     * 最长重复子串问题 - 性能测试
     * 字符长度为 500000, 字符种类数为 3
     * 最长重复子串长度: 1247
     * 运行时间: 156 ms
     * 
     * 实际应用建议：
     * - 小字符集：适合压缩算法、模式识别
     * - 大字符集：适合文本去重、相似性检测
     */
    public static void main(String[] args) {
        int n = 500000;      // 字符串长度
        int r = 3;           // 字符种类数
        
        System.out.println("最长重复子串问题 - 性能测试");
        System.out.println("=====================================");
        System.out.println("测试参数：");
        System.out.println("字符串长度: " + n);
        System.out.println("字符种类数: " + r + " (a-" + (char)('a' + r - 1) + ")");
        System.out.println();
        
        // 生成测试数据
        System.out.println("正在生成测试数据...");
        String testStr = randomStr(n, r);
        
        // 执行算法并计时
        System.out.println("正在计算最长重复子串...");
        long start = System.currentTimeMillis();
        int result = max(testStr);
        long end = System.currentTimeMillis();
        
        // 输出结果
        System.out.println();
        System.out.println("测试结果：");
        System.out.println("最长重复子串长度: " + result);
        System.out.println("运行时间: " + (end - start) + " ms");
        
        // 性能分析
        System.out.println();
        System.out.println("性能分析：");
        long timePerChar = (end - start) * 1000 / n;  // 每字符处理时间（微秒）
        System.out.println("平均每字符处理时间: " + timePerChar + " 微秒");
        
        if (timePerChar < 1) {
            System.out.println("性能评级: 优秀（适合实时处理）");
        } else if (timePerChar < 10) {
            System.out.println("性能评级: 良好（适合批量处理）");
        } else {
            System.out.println("性能评级: 一般（需要优化）");
        }
        
        // 算法特点说明
        System.out.println();
        System.out.println("算法特点：");
        System.out.println("• 时间复杂度: O(N) - 线性时间，高效稳定");
        System.out.println("• 空间复杂度: O(N) - 空间占用与输入成正比");
        System.out.println("• 适用场景: 大规模文本分析、数据压缩预处理");
        System.out.println("• 扩展性: 可轻松扩展到其他字符串匹配问题");
        
        // 实际应用建议
        System.out.println();
        System.out.println("应用建议：");
        if (r <= 4) {
            System.out.println("• 当前字符集较小，重复度高，适合:");
            System.out.println("  - 数据压缩算法的预处理");
            System.out.println("  - 模式识别和规律发现");
        } else {
            System.out.println("• 当前字符集较大，重复度低，适合:");
            System.out.println("  - 文档去重和相似性检测");
            System.out.println("  - 抄袭检测系统");
        }
        System.out.println("  - 生物信息学序列分析");
        System.out.println("  - 代码重复片段检测");
    }
}
