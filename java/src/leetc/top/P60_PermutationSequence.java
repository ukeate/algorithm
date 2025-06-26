package leetc.top;

/**
 * LeetCode 60. 排列序列 (Permutation Sequence)
 * 
 * 问题描述：
 * 给出集合 [1,2,3,...,n]，其所有元素共有 n! 种排列。
 * 按大小顺序列出所有排列情况，并一一标记，当 n = 3 时，所有排列如下：
 * "123", "132", "213", "231", "312", "321"
 * 给定 n 和 k，返回第 k 个排列。
 * 
 * 示例：
 * - 输入：n = 3, k = 3
 * - 输出："213"
 * - 输入：n = 4, k = 9
 * - 输出："2314"
 * 
 * 解法思路：
 * 康托编码 (Cantor Encoding) + 数学计算：
 * 1. 利用阶乘数系统直接计算第k个排列，避免生成所有排列
 * 2. 对于n个数的排列，第一位有n种选择，每种选择后面有(n-1)!种排列
 * 3. 通过除法和取模运算确定每个位置应该选择第几个剩余数字
 * 4. 使用预计算的阶乘表和康托编码表加速计算
 * 
 * 核心优化：
 * - 预计算阶乘数组factorial
 * - 预计算康托编码表fdp[i][j]：i个数字中以j开头的排列数量
 * - 直接数学计算，时间复杂度O(n)而非O(n!)
 * 
 * 时间复杂度：O(n) - 计算每个位置需要O(1)时间
 * 空间复杂度：O(n) - 需要存储剩余可用数字
 * 
 * LeetCode链接：https://leetcode.com/problems/permutation-sequence/
 */
public class P60_PermutationSequence {
    
    /**
     * 预计算的阶乘数组 [0!, 1!, 2!, ..., 9!]
     * factorial[i] = i!
     */
    public static int[] factorial = {0, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880};
    
    /**
     * 预计算的康托编码表
     * fdp[i][j] 表示：i个数字中，以第j个数字开头的所有排列的累计数量
     * 例如：fdp[3][2] = 2，表示3个数字中以第2个数字开头的排列有2个
     */
    public static int[][] fdp = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 2, 0, 0, 0, 0, 0, 0, 0},
            {0, 2, 4, 6, 0, 0, 0, 0, 0, 0},
            {0, 6, 12, 18, 24, 0, 0, 0, 0, 0},
            {0, 24, 48, 72, 96, 120, 0, 0, 0, 0},
            {0, 120, 240, 360, 480, 600, 720, 0, 0, 0},
            {0, 720, 1440, 2160, 2880, 3600, 4320, 5040, 0, 0},
            {0, 5040, 10080, 15120, 20160, 25200, 30240, 35280, 40320, 0},
            {0, 40320, 80640, 120960, 161280, 201600, 241920, 282240, 322560, 362880}};

    /**
     * 将第k个排列转换为数字选择序列
     * 
     * 算法思想：
     * 对于n个数字的第k个排列，我们需要确定每个位置选择第几个剩余数字
     * 1. 第一个位置：选择第kth个数字，使得前面的排列数 < k <= 当前排列数
     * 2. 更新k和n，递归处理剩余位置
     * 
     * @param n 数字个数
     * @param k 目标排列序号（1-indexed）
     * @return 每个位置应该选择第几个剩余数字的数组
     */
    private static int[] thArr(int n, int k) {
        int[] arr = new int[n];
        int restK = k, restN = n;  // 剩余的k和n
        
        for (int i = 0; i < n; i++) {
            int kth = 1;  // 当前位置选择第几个剩余数字
            
            // 找到第一个使得 fdp[restN][kth] >= restK 的kth
            // 即找到应该选择的数字位置
            while (fdp[restN][kth] < restK) {
                kth++;
            }
            
            arr[i] = kth;  // 记录选择第kth个剩余数字
            
            // 更新：减去前面已经排列的数量，减少剩余数字个数
            restK -= fdp[restN--][kth - 1];
        }
        return arr;
    }

    /**
     * 从可用字符数组中取出第kth个字符，并将其标记为已使用
     * 
     * @param kth 要取出第几个可用字符（1-indexed）
     * @param chas 字符数组，0表示已使用，非0表示可用
     * @return 取出的字符
     */
    private static char restKthChar(int kth, char[] chas) {
        int idx = 0;
        
        // 找到第kth个可用字符的位置
        for (int i = 1; i < chas.length; i++) {
            if (chas[i] != 0) {  // 字符可用
                if (--kth == 0) {  // 找到第kth个
                    idx = i;
                    break;
                }
            }
        }
        
        char ans = chas[idx];  // 取出字符
        chas[idx] = 0;         // 标记为已使用
        return ans;
    }

    /**
     * 获取第k个排列
     * 
     * 算法流程：
     * 1. 参数验证：检查n和k的有效性
     * 2. 计算数字选择序列：确定每个位置选择第几个剩余数字
     * 3. 模拟选择过程：依次选择数字并构建结果字符串
     * 
     * @param n 集合大小 [1,2,3,...,n]
     * @param k 目标排列序号（1-indexed）
     * @return 第k个排列的字符串表示
     */
    public static String getPermutation(int n, int k) {
        // 参数验证
        if (n < 1 || n > 9 || k < 1 || k > factorial[n]) {
            return "";
        }
        
        // 计算每个位置应该选择第几个剩余数字
        int[] thArr = thArr(n, k);
        
        // 初始化可用字符数组 [1,2,3,...,n]
        char[] chas = new char[n + 1];
        for (int i = 1; i <= n; i++) {
            chas[i] = (char) ('0' + i);
        }
        
        // 按照thArr的指示依次选择字符
        char[] ans = new char[n];
        for (int i = 0; i < thArr.length; i++) {
            ans[i] = restKthChar(thArr[i], chas);
        }
        
        return String.valueOf(ans);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：n=3, k=3
        System.out.println("输入: n=3, k=3");
        System.out.println("输出: \"" + getPermutation(3, 3) + "\"");
        System.out.println("期望: \"213\"");
        System.out.println("解释: [1,2,3]的排列为: \"123\", \"132\", \"213\", \"231\", \"312\", \"321\"");
        System.out.println();
        
        // 测试用例2：n=4, k=9
        System.out.println("输入: n=4, k=9");
        System.out.println("输出: \"" + getPermutation(4, 9) + "\"");
        System.out.println("期望: \"2314\"");
        System.out.println();
        
        // 测试用例3：边界情况 - 第1个排列
        System.out.println("输入: n=3, k=1");
        System.out.println("输出: \"" + getPermutation(3, 1) + "\"");
        System.out.println("期望: \"123\"");
        System.out.println();
        
        // 测试用例4：边界情况 - 最后一个排列
        System.out.println("输入: n=3, k=6");
        System.out.println("输出: \"" + getPermutation(3, 6) + "\"");
        System.out.println("期望: \"321\"");
        System.out.println();
        
        // 展示所有n=3的排列
        System.out.println("n=3的所有排列：");
        for (int k = 1; k <= 6; k++) {
            System.out.println("k=" + k + ": \"" + getPermutation(3, k) + "\"");
        }
        System.out.println();
        
        // 算法优势说明
        System.out.println("算法优势：");
        System.out.println("- 时间复杂度：O(n) vs 暴力解法的O(n!)");
        System.out.println("- 空间复杂度：O(n)");
        System.out.println("- 核心思想：康托编码直接计算，避免生成所有排列");
    }
}
