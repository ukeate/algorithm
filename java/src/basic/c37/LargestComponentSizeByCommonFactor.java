package basic.c37;

import java.util.HashMap;

/**
 * 最大连通分量问题 - 按公共因子连通
 * 
 * 问题描述：
 * 给定一个数组，如果两个数有大于1的公共因子，则认为它们是连通的。
 * 求最大连通分量的大小。
 * 
 * 例如：
 * 数组[4, 6, 15, 35]
 * - 4和6有公共因子2，连通
 * - 15和35有公共因子5，连通
 * - 6和15有公共因子3，连通
 * 最终所有数字都连通，最大连通分量大小为4
 * 
 * 算法思路：
 * 方法1：暴力法 - 对每两个数计算最大公约数 O(N²*log(max))
 * 方法2：因数分解+并查集优化 - O(N*√max + 因数个数)
 * 
 * 核心优化思想：
 * 不直接比较数字间的公共因子，而是让每个数字与其所有因数建立连接，
 * 这样有相同因数的数字会自动连通。
 * 
 * 时间复杂度：O(N*√max)，其中max是数组中的最大值
 * 空间复杂度：O(N + 因数个数)
 * 
 * LeetCode链接：https://leetcode.com/problems/largest-component-size-by-common-factor/
 */
public class LargestComponentSizeByCommonFactor {
    
    /**
     * 并查集数据结构
     * 支持路径压缩和按大小合并的优化
     */
    private static class UnionFind {
        private int[] parents;  // 父节点数组
        private int[] sizes;    // 连通分量大小数组
        private int[] help;     // 路径压缩辅助数组

        /**
         * 构造函数：初始化n个独立的节点
         * @param n 节点数量
         */
        public UnionFind(int n) {
            parents = new int[n];
            sizes = new int[n];
            help = new int[n];
            
            // 初始化：每个节点的父节点是自己，大小为1
            for (int i = 0; i < n; i++) {
                parents[i] = i;
                sizes[i] = 1;
            }
        }

        /**
         * 查找操作：找到节点i的根节点，并进行路径压缩
         * 
         * 路径压缩：将查找路径上的所有节点直接连接到根节点，
         * 大大减少后续查找的时间复杂度
         * 
         * @param i 要查找的节点
         * @return 节点i的根节点
         */
        private int find(int i) {
            int hi = 0;
            
            // 沿着父节点指针向上找到根节点
            while (i != parents[i]) {
                help[hi++] = i;  // 记录路径上的节点
                i = parents[i];
            }
            
            // 路径压缩：将路径上所有节点的父节点都设为根节点
            for (hi--; hi >= 0; hi--) {
                parents[help[hi]] = i;
            }
            
            return i;
        }

        /**
         * 合并操作：将节点i和节点j所在的连通分量合并
         * 
         * 按大小合并：总是将小的连通分量合并到大的连通分量上，
         * 以保持树的相对平衡，提高查找效率
         * 
         * @param i 第一个节点
         * @param j 第二个节点
         */
        public void union(int i, int j) {
            int f1 = find(i);  // 找到i的根节点
            int f2 = find(j);  // 找到j的根节点
            
            if (f1 != f2) {  // 如果不在同一连通分量中
                // 按大小合并：大的作为新根，小的合并到大的上
                int big = sizes[f1] >= sizes[f2] ? f1 : f2;
                int small = big == f1 ? f2 : f1;
                
                parents[small] = big;                    // 小的指向大的
                sizes[big] = sizes[f1] + sizes[f2];     // 更新大连通分量的大小
            }
        }

        /**
         * 获取最大连通分量的大小
         * @return 最大连通分量大小
         */
        public int maxSize() {
            int ans = 0;
            for (int size : sizes) {
                ans = Math.max(ans, size);
            }
            return ans;
        }
    }

    /**
     * 计算两个数的最大公约数（欧几里得算法）
     * @param m 第一个数
     * @param n 第二个数
     * @return 最大公约数
     */
    private static int gcd(int m, int n) {
        return n == 0 ? m : gcd(n, m % n);
    }

    /**
     * 方法1：暴力解法
     * 对每两个数字计算最大公约数，如果大于1则连通
     * 
     * 算法流程：
     * 1. 初始化并查集
     * 2. 遍历所有数字对
     * 3. 计算最大公约数，如果>1则合并
     * 4. 返回最大连通分量大小
     * 
     * 时间复杂度：O(N²*log(max))
     * 空间复杂度：O(N)
     * 
     * @param arr 输入数组
     * @return 最大连通分量大小
     */
    public static int largest1(int[] arr) {
        int n = arr.length;
        UnionFind set = new UnionFind(n);
        
        // 检查所有数字对
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // 如果有大于1的公共因子，则连通
                if (gcd(arr[i], arr[j]) != 1) {
                    set.union(i, j);
                }
            }
        }
        
        return set.maxSize();
    }

    /**
     * 方法2：因数分解+并查集优化解法
     * 
     * 核心思想：
     * 不直接比较数字间的关系，而是让每个数字与其所有大于1的因数建立连接。
     * 这样，有相同因数的数字会通过该因数自动连通。
     * 
     * 算法流程：
     * 1. 遍历每个数字，分解出所有因数
     * 2. 对于每个因数，如果之前出现过，则与之前的数字连通
     * 3. 否则记录该因数对应的数字位置
     * 
     * 优化策略：
     * - 只需要分解到√n，因为因数成对出现
     * - 使用HashMap快速查找因数是否出现过
     * 
     * 时间复杂度：O(N*√max)，其中max是数组最大值
     * 空间复杂度：O(N + 因数个数)
     * 
     * @param arr 输入数组
     * @return 最大连通分量大小
     */
    public static int largest2(int[] arr) {
        int n = arr.length;
        UnionFind unionFind = new UnionFind(n);
        
        // factorMap: 因数 -> 第一次出现该因数的数字在数组中的位置
        HashMap<Integer, Integer> factorMap = new HashMap<>();
        
        for (int i = 0; i < n; i++) {
            int num = arr[i];
            int limit = (int) Math.sqrt(num);  // 只需要检查到√num
            
            // 分解因数：从1到√num
            for (int j = 1; j <= limit; j++) {
                if (num % j == 0) {  // j是num的因数
                    
                    // 处理因数j（排除1）
                    if (j != 1) {
                        if (!factorMap.containsKey(j)) {
                            // 第一次遇到因数j，记录对应的数字位置
                            factorMap.put(j, i);
                        } else {
                            // 之前遇到过因数j，与之前的数字连通
                            unionFind.union(factorMap.get(j), i);
                        }
                    }
                    
                    // 处理对应的另一个因数num/j（排除1和重复）
                    int other = num / j;
                    if (other != 1 && other != j) {  // 避免重复处理和排除1
                        if (!factorMap.containsKey(other)) {
                            factorMap.put(other, i);
                        } else {
                            unionFind.union(factorMap.get(other), i);
                        }
                    }
                }
            }
        }
        
        return unionFind.maxSize();
    }
    
    /**
     * 测试方法：验证算法正确性和性能对比
     */
    public static void main(String[] args) {
        System.out.println("=== 最大连通分量测试 ===");
        
        // 测试用例1：简单情况
        int[] arr1 = {4, 6, 15, 35};
        System.out.println("测试用例1: [4, 6, 15, 35]");
        System.out.println("方法1结果: " + largest1(arr1));
        System.out.println("方法2结果: " + largest2(arr1));
        System.out.println("分析：4和6有公因数2，15和35有公因数5，6和15有公因数3，所有数连通");
        System.out.println();
        
        // 测试用例2：无连通
        int[] arr2 = {2, 3, 5, 7};
        System.out.println("测试用例2: [2, 3, 5, 7]（质数数组）");
        System.out.println("方法1结果: " + largest1(arr2));
        System.out.println("方法2结果: " + largest2(arr2));
        System.out.println("分析：都是质数，互相没有公因数，最大连通分量为1");
        System.out.println();
        
        // 测试用例3：部分连通
        int[] arr3 = {20, 50, 9, 63};
        System.out.println("测试用例3: [20, 50, 9, 63]");
        System.out.println("方法1结果: " + largest1(arr3));
        System.out.println("方法2结果: " + largest2(arr3));
        System.out.println("分析：20和50有公因数5和10，9和63有公因数3和9");
        System.out.println();
        
        // 测试用例4：单元素
        int[] arr4 = {12};
        System.out.println("测试用例4: [12]（单元素）");
        System.out.println("方法1结果: " + largest1(arr4));
        System.out.println("方法2结果: " + largest2(arr4));
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int[] largeArr = generateTestArray(1000, 10000);
        
        long start = System.currentTimeMillis();
        int result2 = largest2(largeArr);
        long end = System.currentTimeMillis();
        System.out.println("方法2（优化算法）耗时: " + (end - start) + " ms，结果: " + result2);
        
        // 注意：方法1在大数据上会很慢，这里只做小规模测试
        if (largeArr.length <= 100) {
            start = System.currentTimeMillis();
            int result1 = largest1(largeArr);
            end = System.currentTimeMillis();
            System.out.println("方法1（暴力算法）耗时: " + (end - start) + " ms，结果: " + result1);
            System.out.println("结果一致性: " + (result1 == result2));
        }
        
        System.out.println("=== 测试完成 ===");
    }
    
    /**
     * 生成测试数组
     * @param size 数组大小
     * @param maxVal 最大值
     * @return 测试数组
     */
    private static int[] generateTestArray(int size, int maxVal) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = (int) (Math.random() * maxVal) + 2;  // 避免1
        }
        return arr;
    }
}
