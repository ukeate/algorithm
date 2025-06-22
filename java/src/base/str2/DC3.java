package base.str2;

/**
 * DC3后缀数组算法实现
 * 问题描述：构建字符串的后缀数组（Suffix Array）和相关辅助数组
 * 
 * 后缀数组定义：
 * - sa[i]: 排名第i的后缀的起始位置
 * - rank[i]: 起始位置为i的后缀的排名  
 * - height[i]: 排名第i的后缀与排名第i-1的后缀的最长公共前缀长度
 * 
 * DC3算法特点：
 * 1. 基于分治思想，将后缀分为3组：位置模3余0、1、2
 * 2. 先递归处理位置模3余1和2的后缀
 * 3. 再处理位置模3余0的后缀
 * 4. 最后合并三组结果得到完整的后缀数组
 * 
 * 算法优势：
 * - 时间复杂度：O(N*log(alphabet_size))，在常见情况下接近O(N)
 * - 空间复杂度：O(N)
 * - 实现相对简单，常数因子较小
 * 
 * 应用场景：
 * - 最长公共子串、重复子串查找
 * - 字符串匹配、模式搜索
 * - 字典序问题、后缀树构建
 * 
 * 参考：
 * Kärkkäinen & Sanders. "Simple Linear Work Suffix Array Construction"
 */
public class DC3 {

    // 下标是排名, 值是后缀开始位置
    public int[] sa;

    // 下标是后缀开始位置, 值是排名
    public int[] rank;

    // 下标是排名，值是与上一名的最长前缀
    public int[] height;

    /**
     * 构造函数：初始化DC3算法并计算所有相关数组
     * 
     * @param nums 输入的整数数组（字符串的数值表示）
     * @param max 数组中的最大值（用于基数排序）
     */
    public DC3(int[] nums, int max) {
        sa = sa(nums, max);        // 计算后缀数组
        rank = rank();             // 计算排名数组
        height = height(nums);     // 计算LCP数组
    }

    /**
     * 计算后缀数组的接口方法
     * 对输入数组进行预处理，然后调用DC3核心算法
     * 
     * @param nums 原始数组
     * @param max 数组中的最大值
     * @return 后缀数组
     */
    private int[] sa(int[] nums, int max) {
        int n = nums.length;
        int[] arr = new int[n + 3];  // 添加哨兵，避免越界
        for (int i = 0; i < n; i++) {
            arr[i] = nums[i];
        }
        return skew(arr, n, max);
    }

    /**
     * 两个二元组的字典序比较
     * 
     * @param a1, a2 第一个二元组
     * @param b1, b2 第二个二元组
     * @return 第一个二元组是否小于等于第二个
     */
    private boolean leq(int a1, int a2, int b1, int b2) {
        return a1 < b1 || (a1 == b1 && a2 <= b2);
    }

    /**
     * 三个三元组的字典序比较
     * 
     * @param a1, a2, a3 第一个三元组
     * @param b1, b2, b3 第二个三元组
     * @return 第一个三元组是否小于等于第二个
     */
    private boolean leq(int a1, int a2, int a3, int b1, int b2, int b3) {
        return a1 < b1 || (a1 == b1 && leq(a2, a3, b2, b3));
    }

    /**
     * 基数排序的一趟排序
     * 对指定偏移位置的字符进行计数排序
     * 
     * 算法步骤：
     * 1. 统计每个字符出现的频次
     * 2. 将频次转换为起始位置（累加和）
     * 3. 根据起始位置将元素放到正确的输出位置
     * 
     * @param nums 原始数组
     * @param input 输入的位置数组
     * @param output 输出的位置数组
     * @param offset 排序键的偏移量
     * @param n 要排序的元素数量
     * @param k 字符集大小
     */
    private void radixPass(int[] nums, int[] input, int[] output, int offset, int n, int k) {
        int[] cnt = new int[k + 1];  // 计数数组
        
        // 统计频次
        for (int i = 0; i < n; ++i) {
            cnt[nums[input[i] + offset]]++;
        }
        
        // 转换为起始位置（前缀和）
        for (int i = 0, sum = 0; i < cnt.length; ++i) {
            int t = cnt[i];
            cnt[i] = sum;
            sum += t;
        }
        
        // 将元素放到正确位置
        for (int i = 0; i < n; ++i) {
            output[cnt[nums[input[i] + offset]]++] = input[i];
        }
    }

    /**
     * DC3算法的核心实现（skew算法）
     * 
     * 算法思路：
     * 1. 将所有后缀按起始位置模3分为三组：S0、S1、S2
     * 2. 先处理S12 = S1 ∪ S2：
     *    a) 构造三元组并排序
     *    b) 递归求解S12的后缀数组
     * 3. 利用S12的结果处理S0
     * 4. 合并S0和S12的结果得到最终答案
     * 
     * 关键洞察：
     * - S1和S2的相对顺序可以通过三元组比较确定
     * - S0的顺序可以通过S1的信息辅助确定
     * - 合并时利用已知的排名信息进行比较
     * 
     * @param nums 输入数组
     * @param n 数组长度
     * @param K 字符集大小
     * @return 后缀数组
     */
    private int[] skew(int[] nums, int n, int K) {
        // 计算各组的大小
        int n0 = (n + 2) / 3, n1 = (n + 1) / 3, n2 = n / 3, n02 = n0 + n2;
        int[] s12 = new int[n02 + 3], sa12 = new int[n02 + 3];
        
        // 构造S12：收集所有位置模3不为0的位置
        for (int i = 0, j = 0; i < n + (n0 - n1) ; ++i) {
            if (0 != i % 3) {
                s12[j++] = i;
            }
        }
        
        // 对S12按三元组进行基数排序（从第3位到第1位）
        radixPass(nums, s12, sa12, 2, n02, K);  // 按第3个字符排序
        radixPass(nums, sa12, s12, 1, n02, K);  // 按第2个字符排序  
        radixPass(nums, s12, sa12, 0, n02, K);  // 按第1个字符排序
        
        // 对排序后的三元组进行重命名
        int name = 0, c0 = -1, c1 = -1, c2 = -1;
        for (int i = 0; i < n02; ++i) {
            // 如果当前三元组与前一个不同，分配新的名字
            if (c0 != nums[sa12[i]] || c1 != nums[sa12[i] + 1] || c2 != nums[sa12[i] + 2]) {
                name++;
                c0 = nums[sa12[i]];
                c1 = nums[sa12[i] + 1];
                c2 = nums[sa12[i] + 2];
            }
            
            // 根据位置模3的结果决定在s12中的存放位置
            if (1 == sa12[i] % 3) {
                s12[sa12[i] / 3] = name;           // S1组
            } else {
                s12[sa12[i] / 3 + n0] = name;      // S2组
            }
        }
        
        // 如果重命名后仍有重复，递归求解
        if (name < n02) {
            sa12 = skew(s12, n02, name);
            // 根据递归结果计算rank值
            for (int i = 0; i < n02; i++) {
                s12[sa12[i]] = i + 1;
            }
        } else {
            // 没有重复，直接根据名字计算后缀数组
            for (int i = 0; i < n02; i++) {
                sa12[s12[i] - 1] = i;
            }
        }
        
        // 处理S0：利用S1的信息对S0进行排序
        int[] s0 = new int[n0], sa0 = new int[n0];
        for (int i = 0, j = 0; i < n02; i++) {
            if (sa12[i] < n0) {
                s0[j++] = 3 * sa12[i];  // 从S1位置推导出对应的S0位置
            }
        }
        radixPass(nums, s0, sa0, 0, n0, K);  // 对S0进行基数排序
        
        // 合并S0和S12的结果
        int[] sa = new int[n];
        for (int p = 0, t = n0 - n1, k = 0; k < n; k++) {
            // 计算S12中当前元素对应的原始位置
            int i = sa12[t] < n0 ? sa12[t] * 3 + 1 : (sa12[t] - n0) * 3 + 2;
            int j = sa0[p];  // S0中当前元素的位置
            
            // 比较i和j位置开始的后缀，选择较小的
            if (sa12[t] < n0 ? leq(nums[i], s12[sa12[t] + n0], nums[j], s12[j / 3])
                    : leq(nums[i], nums[i + 1], s12[sa12[t] - n0 + 1], nums[j], nums[j + 1], s12[j / 3 + n0])) {
                // S12的当前后缀更小
                sa[k] = i;
                t++;
                // S12处理完毕，添加剩余的S0
                if (t == n02) {
                    for (k++; p < n0; p++, k++) {
                        sa[k] = sa0[p];
                    }
                }
            } else {
                // S0的当前后缀更小
                sa[k] = j;
                p++;
                // S0处理完毕，添加剩余的S12
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
     * 根据后缀数组计算排名数组
     * rank[i] = j 表示以位置i开始的后缀在所有后缀中排名第j
     * 
     * @return 排名数组
     */
    private int[] rank() {
        int n = sa.length;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[sa[i]] = i + 1;  // 排名从1开始
        }
        return ans;
    }

    /**
     * 计算LCP数组（最长公共前缀数组）
     * height[i] = 排名第i的后缀与排名第i-1的后缀的最长公共前缀长度
     * 
     * 算法优化：
     * 利用h数组的性质：h[i] >= h[i-1] - 1
     * 这样可以避免每次都从0开始计算LCP
     * 
     * @param s 原始字符串数组
     * @return LCP数组
     */
    private int[] height(int[] s) {
        int n = s.length;
        int[] ans = new int[n];
        
        // 利用h数组的递减性质优化计算
        for (int i = 0, k = 0; i < n; ++i) {
            if (rank[i] != 0) {  // 排名不为0才有前一个后缀
                if (k > 0) {
                    --k;  // 利用递减性质
                }
                int j = sa[rank[i] - 1];  // 前一个后缀的起始位置
                
                // 扩展公共前缀
                while (i + k < n && j + k < n && s[i + k] == s[j + k]) {
                    ++k;
                }
                ans[rank[i]] = k;  // 记录LCP长度
            }
        }
        return ans;
    }
}
