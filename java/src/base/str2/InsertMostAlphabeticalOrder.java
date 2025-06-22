package base.str2;

/**
 * 字符串插入字典序最大问题
 * 
 * 问题描述：
 * 给定两个字符串s1和s2，将s2插入到s1中的某个位置，使得新字符串的字典序最大。
 * 
 * 核心思想：
 * 1. 暴力方法：尝试所有可能的插入位置，取字典序最大的结果 - O(N²)
 * 2. 优化方法：利用DC3后缀数组算法，通过rank信息快速找到最优插入位置 - O(N)
 * 
 * 应用场景：
 * - 字符串拼接优化问题
 * - 字典序排序相关问题
 * - 字符串编辑问题
 * 
 * @author algorithm-base
 * @version 1.0
 */
// s2插入s1，使新字符串字典序最大
public class InsertMostAlphabeticalOrder {
    
    /**
     * 暴力解法：枚举所有插入位置找最优解
     * 
     * 算法流程：
     * 1. 尝试将s2插入到s1的开头（s2+s1）和结尾（s1+s2）
     * 2. 遍历s1的每个可能插入位置，生成新字符串
     * 3. 比较所有可能结果，返回字典序最大的字符串
     * 
     * 示例：s1="abc", s2="xy"
     * - 位置0: "xy" + "abc" = "xyabc"
     * - 位置1: "a" + "xy" + "bc" = "axybc"  
     * - 位置2: "ab" + "xy" + "c" = "abxyc"
     * - 位置3: "abc" + "xy" = "abcxy"
     * 返回字典序最大的结果
     * 
     * @param s1 待插入的主字符串
     * @param s2 要插入的字符串
     * @return 插入后字典序最大的字符串
     * 
     * 时间复杂度：O(N²)，N为s1长度，每次字符串比较O(N)
     * 空间复杂度：O(N)，存储临时字符串
     */
    public static String sure(String s1, String s2) {
        if (s1 == null || s1.length() == 0) {
            return s2;
        }
        if (s2 == null || s2.length() == 0) {
            return s1;
        }
        // 先比较前插和后插的结果
        String p1 = s1 + s2;
        String p2 = s2 + s1;
        String ans = p1.compareTo(p2) > 0 ? p1 : p2;
        // 尝试中间的每个插入位置
        for (int end = 1; end < s1.length(); end++) {
            String cur = s1.substring(0, end) + s2 + s1.substring(end);
            if (cur.compareTo(ans) > 0) {
                ans = cur;
            }
        }
        return ans;
    }

    /**
     * DC3后缀数组算法实现
     * 
     * DC3（Difference Cover Modulo 3）算法是构造后缀数组的高效算法：
     * 1. 将所有后缀按照位置模3分为三类：0, 1, 2
     * 2. 先处理位置为1和2的后缀（占2/3）
     * 3. 通过递归调用处理这些后缀的排序
     * 4. 利用已排序的1,2后缀信息来排序0后缀
     * 5. 最后归并三类后缀得到完整的后缀数组
     * 
     * 核心数据结构：
     * - sa[]: 后缀数组，sa[i]表示排名第i的后缀的起始位置
     * - rank[]: 排名数组，rank[i]表示以位置i开始的后缀的排名
     */
    public static class DC3 {
        public int[] sa;    // 后缀数组
        public int[] rank;  // 排名数组

        /**
         * DC3算法构造器
         * @param nums 输入数组（字符已转换为数字）
         * @param max 数组中的最大值
         */
        public DC3(int[] nums, int max) {
            sa = sa(nums, max);
            rank = rank();
        }

        /**
         * 构造后缀数组的入口方法
         */
        private int[] sa(int[] nums, int max) {
            int n = nums.length;
            int[] arr = new int[n + 3];  // 末尾补0，避免越界
            for (int i = 0; i < n; i++) {
                arr[i] = nums[i];
            }
            return skew(arr, n, max);
        }

        /**
         * 基数排序：对后缀进行排序
         * @param nums 原数组
         * @param input 待排序的后缀位置数组
         * @param output 排序结果数组
         * @param offset 比较时的偏移量
         * @param n 元素个数
         * @param k 最大值
         */
        private void radixPass(int[] nums, int[] input, int[] output, int offset, int n, int k) {
            int[] cnt = new int[k + 1];
            // 统计每个字符的出现次数
            for (int i = 0; i < n; ++i) {
                cnt[nums[input[i] + offset]]++;
            }
            // 计算前缀和，确定每个字符的起始位置
            for (int i = 0, sum = 0; i < cnt.length; ++i) {
                int t = cnt[i];
                cnt[i] = sum;
                sum += t;
            }
            // 根据排序后的位置放置元素
            for (int i = 0; i < n; ++i) {
                output[cnt[nums[input[i] + offset]]++] = input[i];
            }
        }

        /**
         * 比较两个数对的字典序大小
         */
        private boolean leq(int a1, int a2, int b1, int b2) {
            return a1 < b1 || (a1 == b1 && a2 <= b2);
        }

        /**
         * 比较两个三元组的字典序大小
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
                ans[sa[i]] = i;  // 位置sa[i]的后缀排名为i
            }
            return ans;
        }

        /**
         * DC3算法核心实现：递归构造后缀数组
         * 
         * 算法步骤：
         * 1. 构造位置模3余1和余2的后缀集合S12
         * 2. 对S12中的后缀按照三元组进行基数排序
         * 3. 如果三元组不能完全区分顺序，递归处理
         * 4. 构造位置模3余0的后缀集合S0并排序
         * 5. 归并S0和S12得到最终的后缀数组
         */
        private int[] skew(int[] nums, int n, int K) {
            int n0 = (n + 2) / 3, n1 = (n + 1) / 3, n2 = n / 3, n02 = n0 + n2;
            int[] s12 = new int[n02 + 3], sa12 = new int[n02 + 3];
            
            // 收集所有位置模3不为0的后缀位置
            for (int i = 0, j = 0; i < n + (n0 - n1); ++i) {
                if (0 != i % 3) {
                    s12[j++] = i;
                }
            }
            
            // 对位置模3为1,2的后缀按三元组进行三轮基数排序
            radixPass(nums, s12, sa12, 2, n02, K);  // 按第3个字符排序
            radixPass(nums, sa12, s12, 1, n02, K);   // 按第2个字符排序  
            radixPass(nums, s12, sa12, 0, n02, K);   // 按第1个字符排序
            
            // 给相同的三元组分配相同的名字（rank值）
            int name = 0, c0 = -1, c1 = -1, c2 = -1;
            for (int i = 0; i < n02; ++i) {
                if (c0 != nums[sa12[i]] || c1 != nums[sa12[i] + 1] || c2 != nums[sa12[i] + 2]) {
                    name++;
                    c0 = nums[sa12[i]];
                    c1 = nums[sa12[i] + 1];
                    c2 = nums[sa12[i] + 2];
                }
                if (1 == sa12[i] % 3) {
                    s12[sa12[i] / 3] = name;        // 位置模3余1的后缀
                } else {
                    s12[sa12[i] / 3 + n0] = name;   // 位置模3余2的后缀
                }
            }
            
            // 如果还有重复的名字，需要递归处理
            if (name < n02) {
                sa12 = skew(s12, n02, name);
                for (int i = 0; i < n02; i++) {
                    s12[sa12[i]] = i + 1;  // 更新rank值
                }
            } else {
                // 所有后缀都不同，直接构造排序结果
                for (int i = 0; i < n02; i++) {
                    sa12[s12[i] - 1] = i;
                }
            }
            
            // 处理位置模3余0的后缀
            int[] s0 = new int[n0], sa0 = new int[n0];
            for (int i = 0, j = 0; i < n02; i++) {
                if (sa12[i] < n0) {
                    s0[j++] = 3 * sa12[i];  // 将排名转换回原始位置
                }
            }
            radixPass(nums, s0, sa0, 0, n0, K);  // 对S0进行基数排序
            
            // 归并S0和S12得到最终结果
            int[] sa = new int[n];
            for (int p = 0, t = n0 - n1, k = 0; k < n; k++) {
                int i = sa12[t] < n0 ? sa12[t] * 3 + 1 : (sa12[t] - n0) * 3 + 2;
                int j = sa0[p];
                
                // 比较S12和S0中的后缀，选择较小的放入结果
                if (sa12[t] < n0 ? leq(nums[i], s12[sa12[t] + n0], nums[j], s12[j / 3])
                        : leq(nums[i], nums[i + 1], s12[sa12[t] - n0 + 1], nums[j], nums[j + 1], s12[j / 3 + n0])) {
                    sa[k] = i;
                    t++;
                    if (t == n02) {
                        // S12用完，剩余的都来自S0
                        for (k++; p < n0; p++, k++) {
                            sa[k] = sa0[p];
                        }
                    }
                } else {
                    sa[k] = j;
                    p++;
                    if (p == n0) {
                        // S0用完，剩余的都来自S12
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
     * 找到最佳分割点的优化方法
     * 
     * 在给定起始位置first的情况下，找到插入s2的最佳位置，
     * 使得前缀部分的字典序最大。
     * 
     * @param s1 主字符串
     * @param s2 待插入字符串  
     * @param first 起始搜索位置
     * @return 最佳分割位置
     */
    public static int bestSplit(String s1, String s2, int first) {
        int n = s1.length();
        int m = s2.length();
        int end = n;
        
        // 找到第一个s1[i] < s2[j]的位置，作为搜索终点
        for (int i = first, j = 0; i < n && j < m; i++, j++) {
            if (s1.charAt(i) < s2.charAt(j)) {
                end = i;
                break;
            }
        }
        
        String bestPrefix = s2;
        int bestSplit = first;
        
        // 在[first+1, end]范围内寻找最佳分割点
        for (int i = first + 1, j = m - 1; i <= end; i++, j--) {
            String curPrefix = s1.substring(first, i) + s2.substring(0, j);
            if (curPrefix.compareTo(bestPrefix) >= 0) {
                bestPrefix = curPrefix;
                bestSplit = i;
            }
        }
        return bestSplit;
    }

    /**
     * 基于DC3后缀数组的优化解法
     * 
     * 算法思路：
     * 1. 将s1和s2用分隔符连接成一个字符串
     * 2. 构造后缀数组和rank数组
     * 3. 利用rank信息快速确定最优插入位置
     * 4. 对于每个可能的插入位置，通过rank比较避免实际字符串构造
     * 
     * 优势：避免了大量的字符串比较操作，提高效率
     * 
     * @param s1 主字符串
     * @param s2 待插入字符串
     * @return 插入后字典序最大的字符串
     * 
     * 时间复杂度：O(N)，其中N为字符串总长度
     * 空间复杂度：O(N)，主要是DC3算法的空间开销
     */
    public static String max(String s1, String s2) {
        if (s1 == null || s1.length() == 0) {
            return s2;
        }
        if (s2 == null || s2.length() == 0) {
            return s1;
        }
        
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int n = str1.length;
        int m = str2.length;
        
        // 找到字符的最小值和最大值，用于编码
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
        
        // 构造编码数组：s1 + 分隔符(1) + s2
        int[] all = new int[n + m + 1];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            all[idx++] = str1[i] - min + 2;  // 编码范围[2, max-min+2]
        }
        all[idx++] = 1;  // 分隔符，确保比所有字符都小
        for (int i = 0; i < m; i++) {
            all[idx++] = str2[i] - min + 2;
        }
        
        // 构造DC3后缀数组
        DC3 dc3 = new DC3(all, max - min + 2);
        int[] rank = dc3.rank;
        
        // 利用rank信息找到最优插入位置
        // 寻找第一个rank[i] < rank[n+1]的位置i，表示从位置i开始的s1后缀
        // 比s2的字典序小，因此应该在位置i处插入s2
        for (int i = 0; i < n; i++) {
            if (rank[i] < rank[n + 1]) {
                int best = bestSplit(s1, s2, i);
                return s1.substring(0, best) + s2 + s1.substring(best);
            }
        }
        // 所有s1后缀都比s2大，在末尾插入s2
        return s1 + s2;
    }

    /**
     * 生成随机字符串用于测试
     * @param len 字符串长度
     * @param range 字符范围（从'0'开始）
     * @return 随机字符串
     */
    public static String randomStr(int len, int range) {
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            str[i] = (char) ((int) (range * Math.random()) + '0');
        }
        return String.valueOf(str);
    }

    /**
     * 测试方法：比较暴力解法和优化解法的正确性
     * 
     * 测试流程：
     * 1. 生成大量随机测试用例
     * 2. 分别用sure()和max()方法求解
     * 3. 验证两种方法结果是否一致
     * 
     * 输出样例：
     * 测试开始...
     * 测试结束 - 所有用例通过
     * 
     * 如果发现错误会输出：
     * 错误 - 输入s1, s2及两种方法的不同结果
     */
    public static void main(String[] args) {
        int times = 100000;    // 测试用例数量
        int maxLen = 30;       // 最大字符串长度
        int maxVal = 10;       // 字符种类数
        
        System.out.println("字符串插入字典序最大问题 - 测试开始");
        System.out.println("测试用例数: " + times);
        System.out.println("最大字符串长度: " + maxLen);
        System.out.println("字符种类数: " + maxVal);
        System.out.println();
        
        for (int i = 0; i < times; i++) {
            int len1 = (int) ((maxLen + 1) * Math.random());
            int len2 = (int) ((maxLen + 1) * Math.random());
            String s1 = randomStr(len1, maxVal);
            String s2 = randomStr(len2, maxVal);
            
            String ans1 = sure(s1, s2);    // 暴力解法
            String ans2 = max(s1, s2);     // DC3优化解法
            
            if (!ans1.equals(ans2)) {
                System.out.println("发现错误用例：");
                System.out.println("s1: " + s1);
                System.out.println("s2: " + s2);
                System.out.println("暴力解法结果: " + ans1);
                System.out.println("优化解法结果: " + ans2);
                return;
            }
            
            // 每1万次测试输出进度
            if ((i + 1) % 10000 == 0) {
                System.out.println("已完成测试用例: " + (i + 1));
            }
        }
        
        System.out.println();
        System.out.println("所有测试用例通过！");
        System.out.println("两种算法结果完全一致");
        System.out.println("暴力解法时间复杂度: O(N²)");
        System.out.println("DC3优化解法时间复杂度: O(N)");
    }
}
