package base.str2;

/**
 * 最长公共子串问题（多种解法）
 * 
 * 问题描述：
 * 给定两个字符串，找到它们之间最长的公共子串（连续字符序列）的长度。
 * 
 * 解法对比：
 * 1. 滚动数组DP：通过对角线遍历，空间复杂度O(1)
 * 2. DC3后缀数组：构造后缀数组+height数组，时间复杂度O(N)
 * 
 * 核心思想：
 * - 方法1：利用DP思想，但通过对角线遍历避免存储完整DP表
 * - 方法2：将两字符串合并，利用后缀数组的height数组找最长公共前缀
 * 
 * 应用场景：
 * - 字符串相似度分析
 * - 生物信息学序列比对
 * - 文档差异检测
 * - 数据去重和模糊匹配
 * 
 * @author algorithm-base
 * @version 1.0
 */
public class LongestCommonSubstring {
    
    /**
     * 方法1：滚动数组DP优化解法
     * 
     * 核心思想：
     * 传统DP需要O(M*N)空间存储二维表格，这里通过对角线遍历的方式，
     * 用O(1)空间完成相同的计算任务。
     * 
     * 对角线遍历策略：
     * 1. 从右上角(0, n2-1)开始，沿对角线向右下遍历
     * 2. 每条对角线代表DP表格中的一条斜线
     * 3. 在对角线上连续匹配时累计长度，不匹配时重置为0
     * 4. 记录遍历过程中的最大匹配长度
     * 
     * 对角线遍历示例（4x4矩阵）：
     * 对角线1: (0,3)
     * 对角线2: (0,2)→(1,3) 
     * 对角线3: (0,1)→(1,2)→(2,3)
     * 对角线4: (0,0)→(1,1)→(2,2)→(3,3)
     * 对角线5: (1,0)→(2,1)→(3,2)
     * 对角线6: (2,0)→(3,1)
     * 对角线7: (3,0)
     * 
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     * @return 最长公共子串的长度
     * 
     * 时间复杂度：O(M*N)，每个位置访问一次
     * 空间复杂度：O(1)，只使用常量额外空间
     */
    // 滚动数组dp, 左上向右下填
    public static int lcs1(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
            return 0;
        }
        
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        
        // 对角线遍历的起始位置
        int row = 0;                    // 当前对角线在str1中的起始行
        int col = str2.length - 1;      // 当前对角线在str2中的起始列
        int max = 0;                    // 记录最长公共子串长度
        
        // 遍历所有对角线
        while (row < str1.length) {
            int i = row;      // 当前行指针
            int j = col;      // 当前列指针
            int len = 0;      // 当前对角线上的连续匹配长度
            
            // 沿当前对角线向右下方向遍历
            while (i < str1.length && j < str2.length) {
                if (str1[i] != str2[j]) {
                    len = 0;  // 字符不匹配，重置连续长度
                } else {
                    len++;    // 字符匹配，连续长度加1
                }
                
                // 更新全局最大值
                if (len > max) {
                    max = len;
                }
                
                i++;  // 向下移动一行
                j++;  // 向右移动一列
            }
            
            // 移动到下一条对角线的起点
            if (col > 0) {
                col--;  // 列向左移动（处理上半部分对角线）
            } else {
                row++;  // 行向下移动（处理下半部分对角线）
            }
        }
        return max;
    }

    /**
     * DC3后缀数组算法实现
     * 
     * DC3（Difference Cover Modulo 3）是线性时间构造后缀数组的高效算法。
     * 该算法将后缀按起始位置模3分类，分治处理，最后归并得到完整排序。
     * 
     * 主要组件：
     * - sa[]: 后缀数组，sa[i]表示排名第i的后缀起始位置
     * - rank[]: 排名数组，rank[i]表示从位置i开始的后缀排名
     * - height[]: LCP数组，height[i]表示sa[i]和sa[i-1]的最长公共前缀长度
     * 
     * 时间复杂度：O(N)，其中N为字符串总长度
     * 空间复杂度：O(N)
     */
    private static class DC3 {
        public int[] sa;      // 后缀数组
        public int[] rank;    // 排名数组  
        public int[] height;  // LCP数组（最长公共前缀）

        /**
         * DC3算法构造器
         * @param nums 输入数组（字符转数字）
         * @param max 数组中的最大值
         */
        public DC3(int[] nums, int max) {
            sa = sa(nums, max);
            rank = rank();
            height = height(nums);
        }

        /**
         * 基数排序实现
         * 
         * 基数排序是DC3算法的核心组件，用于对后缀进行稳定排序。
         * 采用计数排序思想，按指定偏移位置的字符值进行排序。
         * 
         * @param nums 原始数组
         * @param input 待排序的位置数组
         * @param output 排序结果数组
         * @param offset 排序基准的偏移量
         * @param n 待排序元素个数
         * @param k 字符集大小
         */
        private void radixPass(int[] nums, int[] input, int[] output, int offset, int n, int k) {
            int[] cnt = new int[k + 1];  // 计数数组
            
            // 统计每个字符的出现频次
            for (int i = 0; i < n; ++i) {
                cnt[nums[input[i] + offset]]++;
            }
            
            // 计算前缀和，确定每个字符的起始位置
            for (int i = 0, sum = 0; i < cnt.length; ++i) {
                int t = cnt[i];
                cnt[i] = sum;
                sum += t;
            }
            
            // 将元素放置到正确的排序位置
            for (int i = 0; i < n; ++i) {
                output[cnt[nums[input[i] + offset]]++] = input[i];
            }
        }

        /**
         * 比较两个二元组的字典序
         */
        private boolean leq(int a1, int a2, int b1, int b2) {
            return a1 < b1 || (a1 == b1 && a2 <= b2);
        }

        /**
         * 比较两个三元组的字典序
         */
        private boolean leq(int a1, int a2, int a3, int b1, int b2, int b3) {
            return a1 < b1 || (a1 == b1 && leq(a2, a3, b2, b3));
        }

        /**
         * 根据后缀数组构造排名数组
         */
        private int[] rank() {
            int n = sa.length;
            int[] ans = new int[n];
            for (int i = 0; i < n; i++) {
                ans[sa[i]] = i;  // 起始位置sa[i]的后缀排名为i
            }
            return ans;
        }

        /**
         * 构造height数组（LCP数组）
         * 
         * height[i]表示排名相邻的两个后缀sa[i-1]和sa[i]的最长公共前缀长度。
         * 这是解决最长公共子串问题的关键数据结构。
         * 
         * 算法核心：
         * 利用"h[rank[i]] >= h[rank[i-1]] - 1"这一性质，
         * 避免每次都从0开始计算LCP，大幅提高效率。
         * 
         * @param s 原始字符数组
         * @return height数组
         */
        private int[] height(int[] s) {
            int n = s.length;
            int[] ans = new int[n];
            
            // 按原字符串位置顺序处理每个后缀
            for (int i = 0, k = 0; i < n; ++i) {
                if (rank[i] != 0) {  // 跳过排名为0的后缀（字典序最小）
                    // 利用上一次计算的结果，优化LCP计算
                    if (k > 0) {
                        --k;  // 根据性质，当前LCP至少为k-1
                    }
                    
                    int j = sa[rank[i] - 1];  // 排名紧邻的前一个后缀起始位置
                    
                    // 从位置k开始向后扩展，计算实际LCP长度
                    while (i + k < n && j + k < n && s[i + k] == s[j + k]) {
                        ++k;
                    }
                    
                    ans[rank[i]] = k;  // 记录LCP长度
                }
            }
            return ans;
        }

        /**
         * DC3算法核心递归实现
         * 
         * 分治策略：
         * 1. 将所有后缀按起始位置模3分为三组：S0, S1, S2
         * 2. 先处理S12 = S1 ∪ S2（约占总数的2/3）
         * 3. 对S12按三元组进行基数排序
         * 4. 如果存在重复三元组，递归处理
         * 5. 利用S12的排序结果处理S0
         * 6. 归并S0和S12得到最终的后缀数组
         */
        private int[] skew(int[] nums, int n, int K) {
            int n0 = (n + 2) / 3, n1 = (n + 1) / 3, n2 = n / 3, n02 = n0 + n2;
            int[] s12 = new int[n02 + 3], sa12 = new int[n02 + 3];
            
            // 收集位置模3不为0的后缀索引（S1和S2）
            for (int i = 0, j = 0; i < n + (n0 - n1); ++i) {
                if (0 != i % 3) {
                    s12[j++] = i;
                }
            }
            
            // 对S12进行三轮基数排序（按三元组排序）
            radixPass(nums, s12, sa12, 2, n02, K);  // 按第3个字符排序
            radixPass(nums, sa12, s12, 1, n02, K);   // 按第2个字符排序
            radixPass(nums, s12, sa12, 0, n02, K);   // 按第1个字符排序
            
            // 为相同的三元组分配相同的名字
            int name = 0, c0 = -1, c1 = -1, c2 = -1;
            for (int i = 0; i < n02; ++i) {
                if (c0 != nums[sa12[i]] || c1 != nums[sa12[i] + 1] || c2 != nums[sa12[i] + 2]) {
                    name++;
                    c0 = nums[sa12[i]];
                    c1 = nums[sa12[i] + 1];
                    c2 = nums[sa12[i] + 2];
                }
                if (1 == sa12[i] % 3) {
                    s12[sa12[i] / 3] = name;
                } else {
                    s12[sa12[i] / 3 + n0] = name;
                }
            }
            
            // 如果存在重复名字，递归处理
            if (name < n02) {
                sa12 = skew(s12, n02, name);
                for (int i = 0; i < n02; i++) {
                    s12[sa12[i]] = i + 1;
                }
            } else {
                for (int i = 0; i < n02; i++) {
                    sa12[s12[i] - 1] = i;
                }
            }
            
            // 处理S0集合
            int[] s0 = new int[n0], sa0 = new int[n0];
            for (int i = 0, j = 0; i < n02; i++) {
                if (sa12[i] < n0) {
                    s0[j++] = 3 * sa12[i];
                }
            }
            radixPass(nums, s0, sa0, 0, n0, K);
            
            // 归并S0和S12
            int[] sa = new int[n];
            for (int p = 0, t = n0 - n1, k = 0; k < n; k++) {
                int i = sa12[t] < n0 ? sa12[t] * 3 + 1 : (sa12[t] - n0) * 3 + 2;
                int j = sa0[p];
                if (sa12[t] < n0 ? leq(nums[i], s12[sa12[t] + n0], nums[j], s12[j / 3])
                : leq(nums[i], nums[i + 1], s12[sa12[t] - n0 + 1], nums[j], nums[j + 1], s12[j / 3 + n0])) {
                    sa[k] = i;
                    t++;
                    if (t == n02) {
                        for (k++; p < n0; p++, k++) {
                            sa[k] = sa0[p];
                        }
                    }
                } else {
                    sa[k] = j;
                    p++;
                    if (p == n0) {
                        for (k++; t < n02; t++, k++) {
                            sa[k] = sa12[t] < n0 ? sa12[t] * 3 + 1 : (sa12[t] - n0) * 3 + 2;
                        }
                    }
                }
            }
            return sa;
        }

        /**
         * 构造后缀数组的入口方法
         */
        private int[] sa(int[] nums, int max) {
            int n = nums.length;
            int[] arr = new int[n + 3];  // 末尾补0防止越界
            for (int i = 0; i < n; i++) {
                arr[i] = nums[i];
            }
            return skew(arr, n, max);
        }
    }

    /**
     * 方法2：基于DC3后缀数组的最长公共子串求解
     * 
     * 算法思路：
     * 1. 将两个字符串s1和s2用特殊分隔符连接：s1 + separator + s2
     * 2. 构造这个合并字符串的后缀数组和height数组
     * 3. 遍历height数组，找到来自不同原字符串的相邻后缀
     * 4. 这些相邻后缀的LCP长度的最大值就是最长公共子串长度
     * 
     * 核心观察：
     * 任何两个字符串的公共子串，必然对应于合并字符串中某对后缀的公共前缀。
     * 通过height数组可以高效找到所有相邻后缀的最长公共前缀。
     * 
     * 示例：
     * s1 = "abc", s2 = "bcd"
     * 合并后："abc#bcd" (其中#为分隔符)
     * 后缀："abc#bcd", "bc#bcd", "c#bcd", "#bcd", "bcd", "cd", "d"
     * 排序后找height数组中跨越分隔符的最大LCP
     * 
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     * @return 最长公共子串的长度
     * 
     * 时间复杂度：O(M+N)，线性时间构造后缀数组
     * 空间复杂度：O(M+N)，存储合并字符串和相关数组
     */
    public static int lcs2(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
            return 0;
        }
        
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int n = str1.length;
        int m = str2.length;
        
        // 找到字符范围，用于编码
        int min = str1[0];
        int max = str1[0];
        for (int i = 1; i < n; i++) {
            min = Math.min(min, str1[i]);
            max = Math.max(max, str1[i]);
        }
        for (int i = 0; i < m; i++) {
            min = Math.min(min, str2[i]);
            max = Math.max(max, str2[i]);
        }
        
        // 构造合并数组：str1 + separator(1) + str2
        int[] all = new int[n + m + 1];
        int idx = 0;
        
        // 添加第一个字符串（编码为[2, max-min+2]）
        for (int i = 0; i < n; i++) {
            all[idx++] = str1[i] - min + 2;
        }
        
        // 添加分隔符（编码为1，小于所有字符）
        all[idx++] = 1;
        
        // 添加第二个字符串
        for (int i = 0; i < m; i++) {
            all[idx++] = str2[i] - min + 2;
        }
        
        // 构造DC3后缀数组
        DC3 dc3 = new DC3(all, max - min + 2);
        int nn = all.length;
        int[] sa = dc3.sa;
        int[] height = dc3.height;
        
        int ans = 0;
        // 遍历height数组，找跨越分隔符的相邻后缀
        for (int i = 1; i < nn; i++) {
            int y = sa[i - 1];  // 前一个后缀起始位置
            int x = sa[i];      // 当前后缀起始位置
            
            // 检查两个后缀是否来自不同的原字符串
            if (Math.min(x, y) < n && Math.max(x, y) > n) {
                ans = Math.max(ans, height[i]);
            }
        }
        return ans;
    }

    /**
     * 生成随机字符串用于测试
     * @param len 字符串长度
     * @param range 字符种类数（从'a'开始）
     * @return 随机字符串
     */
    public static String randomStr(int len, int range) {
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            str[i] = (char) ((int) (range * Math.random()) + 'a');
        }
        return String.valueOf(str);
    }

    /**
     * 测试方法：验证两种算法的正确性并比较性能
     * 
     * 测试包含：
     * 1. 正确性验证：大量随机用例测试两种方法结果一致性
     * 2. 性能测试：在大数据量下比较两种方法的运行时间
     * 
     * 算法对比：
     * - lcs1：滚动数组，空间O(1)，适合内存受限场景
     * - lcs2：后缀数组，时间O(N)，适合超大字符串处理
     * 
     * 输出示例：
     * 正确性测试通过
     * 性能测试：
     * lcs1结果: 15, 运行时间：1250 ms
     * lcs2结果: 15, 运行时间：180 ms
     */
    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 30;
        int range = 5;
        
        System.out.println("最长公共子串问题 - 算法测试");
        System.out.println("=================================");
        System.out.println("正确性测试开始");
        System.out.println("测试用例数：" + times);
        System.out.println("最大字符串长度：" + maxLen);
        System.out.println("字符种类数：" + range);
        
        // 正确性测试
        for (int i = 0; i < times; i++) {
            int n1 = (int)((maxLen + 1) * Math.random());
            int n2 = (int)((maxLen + 1) * Math.random());
            String str1 = randomStr(n1, range);
            String str2 = randomStr(n2, range);
            int ans1 = lcs1(str1, str2);
            int ans2 = lcs2(str1, str2);
            if (ans1 != ans2) {
                System.out.println("发现错误！");
                System.out.println("字符串1: " + str1);
                System.out.println("字符串2: " + str2);
                System.out.println("滚动数组结果: " + ans1);
                System.out.println("后缀数组结果: " + ans2);
                return;
            }
        }
        System.out.println("正确性测试通过！");
        System.out.println();

        // 性能测试
        System.out.println("性能测试开始");
        int len = 80000;
        range = 26;
        long start;
        long end;
        String str1 = randomStr(len, range);
        String str2 = randomStr(len, range);
        
        System.out.println("测试数据：两个长度为 " + len + " 的随机字符串");
        System.out.println("字符集大小：" + range + " (a-z)");
        System.out.println();
        
        // 测试滚动数组方法
        start = System.currentTimeMillis();
        int ans1 = lcs1(str1, str2);
        end = System.currentTimeMillis();
        System.out.println("方法1 - 滚动数组DP：");
        System.out.println("  结果: " + ans1);
        System.out.println("  运行时间：" + (end - start) + " ms");
        System.out.println("  空间复杂度: O(1)");
        System.out.println();

        // 测试后缀数组方法
        start = System.currentTimeMillis();
        int ans2 = lcs2(str1, str2);
        end = System.currentTimeMillis();
        System.out.println("方法2 - DC3后缀数组：");
        System.out.println("  结果: " + ans2);
        System.out.println("  运行时间: " + (end - start) + " ms");
        System.out.println("  时间复杂度: O(N)");
        System.out.println();
        
        // 算法选择建议
        System.out.println("算法选择建议：");
        if (end - start < 1000) {
            System.out.println("• 当前数据规模下，DC3后缀数组方法性能更优");
        } else {
            System.out.println("• 当前数据规模下，滚动数组方法内存效率更高");
        }
        System.out.println("• 超大字符串（>100万字符）推荐使用DC3方法");
        System.out.println("• 内存受限环境推荐使用滚动数组方法");
        System.out.println("• 需要实际子串内容时，两种方法都需要额外处理");
    }
}
