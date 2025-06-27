package leetc.top;

import java.util.Arrays;

/**
 * LeetCode 204. 计算质数 (Count Primes)
 * 
 * 问题描述：
 * 统计所有小于非负整数 n 的质数的数量。
 * 
 * 示例：
 * 输入：n = 10
 * 输出：4
 * 解释：小于 10 的质数一共有 4 个, 它们是 2, 3, 5, 7。
 * 
 * 解法思路：
 * 本类提供了两种不同的筛法：
 * 
 * 方法1：埃拉托斯特尼筛法 (Sieve of Eratosthenes)
 * - 基本思路：标记所有质数的倍数为合数
 * - 优化：只处理奇数，跳过偶数（除了2以外都是合数）
 * - 从3开始，每次增加2，只筛选奇数的倍数
 * 
 * 方法2：线性筛法 (Linear Sieve / Sieve of Euler)
 * - 核心思想：每个合数只被其最小质因子筛选一次
 * - 保证每个数最多被标记一次，达到O(n)的时间复杂度
 * - 当 i % prime[j] == 0 时停止，避免重复标记
 * 
 * 时间复杂度：
 * - 方法1：O(n log log n) - 埃拉托斯特尼筛法
 * - 方法2：O(n) - 线性筛法
 * 
 * 空间复杂度：O(n) - 需要数组标记每个数是否为质数
 * 
 * LeetCode链接：https://leetcode.com/problems/count-primes/
 */
public class P204_CountPrimes {
    
    /**
     * 方法1：优化的埃拉托斯特尼筛法
     * 
     * 算法思路：
     * 1. 偶数除了2以外都是合数，所以只需要处理奇数
     * 2. 初始时count = n/2，表示假设所有奇数都是质数
     * 3. 从3开始遍历奇数，标记其奇数倍数为合数
     * 4. 只需要检查到√n即可，因为大于√n的质数的倍数已经被较小的质数标记过了
     * 
     * 优化要点：
     * - 跳过偶数，只处理奇数，减少一半的计算量
     * - 从i²开始标记，因为i*(小于i的数)已经被之前的质数标记过了
     * - 每次增加2*i，只标记奇数倍数
     * 
     * @param n 上界（不包含）
     * @return 小于n的质数个数
     */
    public static int countPrimes(int n) {
        if (n < 3) {
            return 0;  // 小于3时没有质数
        }
        
        // f[i]表示数字i是否为合数，true表示合数，false表示质数
        boolean[] f = new boolean[n];
        
        // 初始计数：n/2表示所有奇数的个数（假设都是质数）
        // 需要减去1（因为1不是质数），但2是质数，所以实际上就是n/2
        int count = n / 2;
        
        // 只需要检查到√n
        for (int i = 3; i * i < n; i += 2) {
            if (f[i]) {
                continue;  // i已经被标记为合数，跳过
            }
            
            // 标记i的所有奇数倍数为合数
            // 从i²开始，因为小于i²的i的倍数已经被更小的质数标记过了
            // j += 2 * i 确保只标记奇数倍数
            for (int j = i * i; j < n; j += 2 * i) {
                if (!f[j]) {
                    --count;    // 发现一个新的合数，质数个数减1
                    f[j] = true; // 标记为合数
                }
            }
        }
        return count;
    }

    /**
     * 方法2：线性筛法（欧拉筛法）
     * 
     * 算法思路：
     * 1. 维护一个质数数组prime，存储已找到的质数
     * 2. 对每个数i，用已知的质数去筛选i的倍数
     * 3. 关键优化：当i % prime[j] == 0时停止筛选
     *    - 这保证每个合数只被其最小质因子筛选一次
     *    - 避免重复标记，实现线性时间复杂度
     * 
     * 核心思想：
     * - 每个合数都有一个最小的质因子
     * - 让每个合数只被其最小质因子筛选，避免重复
     * 
     * @param n 上界（不包含）
     * @return 小于n的质数个数
     */
    public static int countPrimes2(int n) {
        if (n < 3) {
            return 0;
        }
        
        // f[i]表示i是否为合数
        boolean[] f = new boolean[n];
        // 存储找到的质数
        int[] prime = new int[n];
        int cnt = 0;              // 质数个数
        int ans = n - 2;          // 初始假设所有数都是质数，减去0和1
        
        for (int i = 2; i < n; i++) {
            // 如果i没有被标记为合数，则i是质数
            if (!f[i]) {
                prime[cnt++] = i;
            }
            
            // 用已知质数去筛选
            for (int j = 0; j < cnt && prime[j] * i < n; j++) {
                f[prime[j] * i] = true;  // 标记prime[j] * i为合数
                ans--;                   // 质数个数减1
                
                // 关键优化：当i能被prime[j]整除时停止
                // 这保证每个合数只被其最小质因子筛选一次
                if (i % prime[j] == 0) {
                    break;
                }
            }
        }
        return ans;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println(countPrimes(20));   // 输出：8 (质数：2,3,5,7,11,13,17,19)
        System.out.println(countPrimes2(20));  // 输出：8
    }
}
