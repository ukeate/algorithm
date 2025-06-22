package base.str2;

/**
 * 从两个数组中取k个数构造最大数字问题
 * 问题描述：给定两个非负整数数组nums1和nums2，从中选择k个数字拼接成最大的数字
 * 
 * 核心思路：
 * 1. 枚举所有可能的分配方案：从nums1取i个，从nums2取k-i个
 * 2. 对每个数组使用动态规划找出选取指定数量的最优子序列
 * 3. 使用后缀数组技术将两个最优子序列合并成最大数字
 * 4. 在所有可能的合并结果中选择最大的
 * 
 * 算法特色：
 * - 结合动态规划和后缀数组的高级算法
 * - 巧妙处理子序列选择和合并两个子问题
 * - 通过字典序比较实现最优合并
 * 
 * 时间复杂度：O(k*(N+M)*log(N+M)) - N和M为两数组长度
 * 空间复杂度：O(N+M) - 动态规划表和后缀数组空间
 * 
 * 应用场景：
 * - 数字排列优化问题
 * - 序列合并最优化
 * - 贪心算法与动态规划结合
 * 
 * @see <a href="https://leetcode.com/problems/create-maximum-number/">LeetCode 321</a>
 */
// https://leetcode.com/problems/create-maximum-number/
// 两个集合顺序挑k个，从左向右排列使形成的数最大
public class CreateMaxNum {

    /**
     * 预计算动态规划表：dp[i][j] = 从位置i开始选j个数的最优起始位置
     * 
     * 核心思想：
     * 对于每个位置i和选择数量j，找到从i开始能选择j个数的最优首选位置
     * 这里的"最优"是指能让后续选择的数字序列最大
     * 
     * 算法原理：
     * 1. dp[i][j]表示从[i, n-1]中选j个数时，第一个数应该选择的位置
     * 2. 从后往前填表，利用已计算的结果
     * 3. 对于位置i，比较选择i和选择dp[i+1][j]的优劣
     * 
     * 状态转移：
     * - 如果arr[i] >= arr[maxIdx]，则选择i更优
     * - 否则保持maxIdx不变
     * 
     * 时间复杂度：O(N²) - N为数组长度
     * 空间复杂度：O(N²) - 二维DP表
     * 
     * @param arr 输入数组
     * @return 二维DP表，dp[i][j]表示从位置i选j个数的最优起始位置
     */
    private static int[][] preDp(int[] arr) {
        int size = arr.length;
        int pick = arr.length + 1;  // 选择数量范围：0到size
        int[][] dp = new int[size][pick];
        
        // 对每个选择数量从1开始计算（选0个没有意义）
        for (int get = 1; get < pick; get++) {
            int maxIdx = size - get;  // 最后一个可能的起始位置
            
            // 从后往前填充DP表
            for (int i = size - get; i >= 0; i--) {
                // 如果当前位置的值大于等于之前找到的最大值位置
                if (arr[i] >= arr[maxIdx]) {
                    maxIdx = i;  // 更新最优位置
                }
                dp[i][get] = maxIdx;  // 记录从位置i选get个数的最优起始位置
            }
        }
        return dp;
    }

    /**
     * 根据动态规划表从数组中选择指定数量的最优子序列
     * 
     * 算法步骤：
     * 1. 使用预计算的DP表找到每一步的最优选择位置
     * 2. 选择该位置的数字加入结果
     * 3. 更新起始位置为选择位置的下一个位置
     * 4. 重复直到选择完所有需要的数字
     * 
     * 关键洞察：
     * DP表确保每一步的选择都是局部最优的
     * 局部最优的贪心选择能够导致全局最优解
     * 
     * @param arr 原数组
     * @param dp 预计算的动态规划表
     * @param pick 需要选择的数字数量
     * @return 选择的最优子序列
     */
    private static int[] maxPick(int[] arr, int[][] dp, int pick) {
        int[] res = new int[pick];
        
        // 依次选择每个位置的最优数字
        for (int resIdx = 0, dpRow = 0; pick > 0; pick--, resIdx++) {
            res[resIdx] = arr[dp[dpRow][pick]];  // 选择DP表指示的最优位置
            dpRow = dp[dpRow][pick] + 1;         // 下次从选择位置的下一个开始
        }
        return res;
    }

    /**
     * 比较两个数组的字典序大小
     * 用于合并过程中决定优先选择哪个数组的元素
     * 
     * 比较规则：
     * 1. 逐位比较对应位置的元素
     * 2. 如果某个数组先结束，检查另一个数组的首元素
     * 3. 返回arr1是否字典序大于arr2
     * 
     * 应用场景：
     * 在合并两个子序列时，需要决定下一个选择哪个序列的元素
     * 选择能让整体结果更大的序列
     * 
     * @param arr1 第一个数组
     * @param arr2 第二个数组
     * @return arr1是否字典序大于arr2
     */
    private static boolean moreThan(int[] arr1, int[] arr2) {
        int i = 0;
        int j = 0;
        
        // 逐位比较直到某个数组结束或发现不同
        while (i < arr1.length && j < arr2.length && arr1[i] == arr2[j]) {
            i++;
            j++;
        }
        
        // 如果arr2先结束，或arr1[i] > arr2[j]，则arr1更大
        return j == arr2.length || (i < arr1.length && arr1[i] > arr2[j]);
    }

    /**
     * 使用后缀数组技术合并两个数组
     * 将两个已排序的数字序列合并成字典序最大的结果
     * 
     * 核心思想：
     * 1. 构造一个包含两个数组和分隔符的组合数组
     * 2. 使用DC3算法计算后缀数组的rank信息
     * 3. 利用rank值决定合并时的选择顺序
     * 
     * 后缀数组应用：
     * - rank[i]表示以位置i开始的后缀在所有后缀中的排名
     * - rank值越大，对应的后缀字典序越大
     * - 通过比较rank值可以快速决定合并顺序
     * 
     * 数组构造：
     * [nums1] + [分隔符1] + [nums2] 
     * 分隔符确保两个数组不会混淆
     * 
     * @param nums1 第一个数组
     * @param nums2 第二个数组
     * @return 合并后的最大数组
     */
    private static int[] mergeBySA(int[] nums1, int[] nums2) {
        int size1 = nums1.length;
        int size2 = nums2.length;
        
        // 构造组合数组：nums1 + 分隔符 + nums2
        int[] nums = new int[size1 + 1 + size2];
        
        // 复制第一个数组（值+2避免与分隔符冲突）
        for (int i = 0; i < size1; i++) {
            nums[i] = nums1[i] + 2;
        }
        
        // 添加分隔符（值为1）
        nums[size1] = 1;
        
        // 复制第二个数组（值+2）
        for (int j = 0; j < size2; j++) {
            nums[j + size1 + 1] = nums2[j] + 2;
        }
        
        // 使用DC3算法计算后缀数组
        DC3 dc3 = new DC3(nums, 11);  // 最大值为9+2=11
        int[] rank = dc3.rank;
        
        // 根据rank信息合并两个原数组
        int[] ans = new int[size1 + size2];
        int i = 0;      // nums1的指针
        int j = 0;      // nums2的指针  
        int r = 0;      // 结果数组的指针
        
        // 根据rank值决定选择顺序
        while (i < size1 && j < size2) {
            // 比较两个位置的rank值，选择rank更大的（字典序更大的）
            ans[r++] = rank[i] > rank[j + size1 + 1] ? nums1[i++] : nums2[j++];
        }
        
        // 处理剩余元素
        while (i < size1) {
            ans[r++] = nums1[i++];
        }
        while (j < size2) {
            ans[r++] = nums2[j++];
        }
        
        return ans;
    }

    /**
     * DC3后缀数组算法的实现
     * 用于高效计算字符串的后缀数组和rank信息
     */
    public static class DC3 {
        public int[] sa;     // 后缀数组：sa[i]表示排名第i的后缀起始位置
        public int[] rank;   // 排名数组：rank[i]表示以位置i开始的后缀的排名

        public DC3(int[] nums, int max) {
            sa = sa(nums, max);
            rank = rank();
        }

        private int[] sa(int[] nums, int max) {
            int n = nums.length;
            int[] arr = new int[n + 3];
            for (int i = 0; i < n; i++) {
                arr[i] = nums[i];
            }
            return skew(arr, n, max);
        }

        private void radixPass(int[] nums, int[] input, int[] output, int offset, int n, int k) {
            int[] cnt = new int[k + 1];
            for (int i = 0; i < n; ++i) {
                cnt[nums[input[i] + offset]]++;
            }
            for (int i = 0, sum = 0; i < cnt.length; ++i) {
                int t = cnt[i];
                cnt[i] = sum;
                sum += t;
            }
            for (int i = 0; i < n; ++i) {
                output[cnt[nums[input[i] + offset]]++] = input[i];
            }
        }

        private boolean leq(int a1, int a2, int b1, int b2) {
            return a1 < b1 || (a1 == b1 && a2 <= b2);
        }

        private boolean leq(int a1, int a2, int a3, int b1, int b2, int b3) {
            return a1 < b1 || (a1 == b1 && leq(a2, a3, b2, b3));
        }

        private int[] rank() {
            int n = sa.length;
            int[] ans = new int[n];
            for (int i = 0; i < n; i++) {
                ans[sa[i]] = i;
            }
            return ans;
        }

        private int[] skew(int[] nums, int n, int K) {
            int n0 = (n + 2) / 3, n1 = (n + 1) / 3, n2 = n / 3, n02 = n0 + n2;
            int[] s12 = new int[n02 + 3], sa12 = new int[n02 + 3];
            for (int i = 0, j = 0; i < n + (n0 - n1) ; ++i) {
                if (0 != i % 3) {
                    s12[j++] = i;
                }
            }
            radixPass(nums, s12, sa12, 2, n02, K);
            radixPass(nums, sa12, s12, 1, n02, K);
            radixPass(nums, s12, sa12, 0, n02, K);
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
            int[] s0 = new int[n0], sa0 = new int[n0];
            for (int i = 0, j = 0; i < n02; i++) {
                if (sa12[i] < n0) {
                    s0[j++] = 3 * sa12[i];
                }
            }
            radixPass(nums, s0, sa0, 0, n0, K);
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
    }

    /**
     * 主算法：从两个数组中选择k个数构造最大数字
     * 
     * 算法流程：
     * 1. 枚举所有可能的分配方案：从nums1选get1个，从nums2选k-get1个
     * 2. 对每个分配方案：
     *    a) 使用DP找到每个数组的最优子序列
     *    b) 使用后缀数组技术合并两个子序列
     *    c) 与当前最优结果比较，保留更大的
     * 3. 返回所有方案中的最优结果
     * 
     * 分配约束：
     * - get1的范围：max(0, k-len2) 到 min(k, len1)
     * - 确保两个数组都有足够的元素可选
     * 
     * @param nums1 第一个数组
     * @param nums2 第二个数组  
     * @param k 需要选择的总数字数量
     * @return 构造的最大数字数组
     */
    public static int[] max(int[] nums1, int[] nums2, int k) {
        int len1 = nums1.length;
        int len2 = nums2.length;
        
        // 边界检查
        if (k < 0 || k > len1 + len2) {
            return null;
        }
        
        int[] res = new int[k];  // 最终结果
        
        // 预计算两个数组的DP表
        int[][] dp1 = preDp(nums1);
        int[][] dp2 = preDp(nums2);
        
        // 枚举所有可能的分配方案
        for (int get1 = Math.max(0, k - len2); get1 <= Math.min(k, len1); get1++) {
           // 从第一个数组选get1个数
           int[] pick1 = maxPick(nums1, dp1, get1);
           
           // 从第二个数组选k-get1个数
           int[] pick2 = maxPick(nums2, dp2, k - get1);
           
           // 使用后缀数组技术合并两个子序列
           int[] merge = mergeBySA(pick1, pick2);
           
           // 更新最优结果
           res = moreThan(res, merge) ? res : merge;
        }
        
        return res;
    }
}
