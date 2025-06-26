package giant.c4;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 区间值查询问题
 * 
 * 问题描述：
 * 给定一个数组，需要支持多次查询操作。
 * 查询(l, r, v)表示：在数组的[l, r]区间内，值等于v的元素有多少个。
 * 
 * 例如：数组[1, 2, 3, 2, 1]，查询(0, 3, 2)返回2，因为在索引0到3范围内有2个值为2的元素。
 * 
 * 解法对比：
 * 1. 方法1：暴力查询 - 每次查询遍历区间，时间O(n)
 * 2. 方法2：预处理优化 - 建立索引映射+二分查找，查询时间O(log n)
 * 
 * 应用场景：
 * 当查询次数很多时，方法2的预处理成本能够被多次查询摊销，性能显著提升。
 * 
 * @author algorithm learning
 */
public class QueryHobby {
    
    /**
     * 方法1：暴力查询实现
     * 
     * 特点：
     * - 简单直接，无需预处理
     * - 每次查询都需要遍历指定区间
     * - 适合查询次数较少的场景
     */
    public static class QueryBox1 {
        private int[] arr;  // 存储原始数组的副本

        /**
         * 构造函数：保存原始数组
         * 时间复杂度：O(n)
         * 空间复杂度：O(n)
         * 
         * @param in 输入数组
         */
        public QueryBox1(int[] in) {
            arr = new int[in.length];
            // 复制数组，避免外部修改影响查询结果
            for (int i = 0; i < arr.length; i++) {
                arr[i] = in[i];
            }
        }

        /**
         * 区间查询：统计[l, r]范围内值为v的元素个数
         * 
         * 算法思路：
         * 直接遍历指定区间，逐个检查元素值是否等于目标值v
         * 
         * 时间复杂度：O(r - l + 1)，最坏情况O(n)
         * 空间复杂度：O(1)
         * 
         * @param l 查询区间左端点（包含）
         * @param r 查询区间右端点（包含）
         * @param v 目标值
         * @return 区间内值为v的元素个数
         */
        public int query(int l, int r, int v) {
            int ans = 0;
            // 遍历区间[l, r]，统计值为v的元素个数
            for (; l <= r; l++) {
                if (arr[l] == v) {
                    ans++;
                }
            }
            return ans;
        }
    }

    /**
     * 方法2：预处理优化实现
     * 
     * 特点：
     * - 预处理建立值到索引列表的映射
     * - 查询时使用二分查找快速定位
     * - 适合查询次数很多的场景
     */
    public static class QueryBox2 {
        private HashMap<Integer, ArrayList<Integer>> map;  // 值 -> 该值出现的所有索引位置

        /**
         * 构造函数：预处理建立索引映射
         * 
         * 预处理思路：
         * 1. 遍历数组，为每个不同的值建立索引列表
         * 2. 索引列表按照在原数组中的出现顺序自然有序
         * 3. 后续查询时可以利用有序性进行二分查找
         * 
         * 时间复杂度：O(n)
         * 空间复杂度：O(n)
         * 
         * @param arr 输入数组
         */
        public QueryBox2(int[] arr) {
            map = new HashMap<>();
            
            // 遍历数组，建立值到索引列表的映射
            for (int i = 0; i < arr.length; i++) {
                // 如果该值第一次出现，创建新的索引列表
                if (!map.containsKey(arr[i])) {
                    map.put(arr[i], new ArrayList<>());
                }
                // 将当前索引添加到对应值的索引列表中
                map.get(arr[i]).add(i);
            }
        }

        /**
         * 二分查找：在有序数组中找到小于limit的元素个数
         * 
         * 算法思路：
         * 使用二分查找找到最后一个小于limit的元素位置，
         * 该位置+1就是小于limit的元素个数。
         * 
         * 边界处理：
         * - 如果所有元素都>=limit，返回0
         * - 如果所有元素都<limit，返回数组长度
         * 
         * 时间复杂度：O(log n)
         * 空间复杂度：O(1)
         * 
         * @param arr 有序的索引列表
         * @param limit 上界值（不包含）
         * @return 小于limit的元素个数
         */
        private int countLess(ArrayList<Integer> arr, int limit) {
            int l = 0, r = arr.size() - 1;
            int mostRight = -1;  // 最后一个<limit的位置
            
            // 二分查找最右边的<limit的位置
            while (l <= r) {
                int mid = l + ((r - l) >> 1);
                if (arr.get(mid) < limit) {
                    mostRight = mid;  // 记录可能的答案
                    l = mid + 1;      // 继续向右找更大的满足条件的位置
                } else {
                    r = mid - 1;      // 当前位置>=limit，向左查找
                }
            }
            
            // mostRight+1就是<limit的元素个数
            return mostRight + 1;
        }

        /**
         * 区间查询：统计[l, r]范围内值为v的元素个数
         * 
         * 算法思路：
         * 1. 获取值v对应的所有索引位置列表（已按索引大小有序）
         * 2. 使用二分查找计算：
         *    - a = 索引<l的元素个数
         *    - b = 索引<(r+1)的元素个数  
         * 3. 答案就是 b - a，即索引在[l, r]范围内的元素个数
         * 
         * 核心洞察：
         * 利用前缀和的思想：[l, r]范围内的数量 = [0, r]的数量 - [0, l-1]的数量
         * 
         * 时间复杂度：O(log n)，两次二分查找
         * 空间复杂度：O(1)
         * 
         * @param l 查询区间左端点（包含）
         * @param r 查询区间右端点（包含）
         * @param v 目标值
         * @return 区间内值为v的元素个数
         */
        public int query(int l, int r, int v) {
            // 如果目标值v在数组中不存在，直接返回0
            if (!map.containsKey(v)) {
                return 0;
            }
            
            // 获取值v的所有索引位置
            ArrayList<Integer> idxArr = map.get(v);
            
            // 计算索引<l的元素个数（即[0, l-1]范围内v的个数）
            int a = countLess(idxArr, l);
            
            // 计算索引<(r+1)的元素个数（即[0, r]范围内v的个数）
            int b = countLess(idxArr, r + 1);
            
            // [l, r]范围内v的个数 = [0, r]内的个数 - [0, l-1]内的个数
            return b - a;
        }
    }

    /**
     * 生成随机测试数组
     * @param len 数组长度上限
     * @param val 元素值上限
     * @return 随机数组
     */
    private static int[] randomArr(int len, int val) {
        int[] ans = new int[(int) (Math.random() * len) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (Math.random() * val) + 1;
        }
        return ans;
    }

    /**
     * 测试方法：对拍验证两种实现的正确性，并比较性能
     */
    public static void main(String[] args) {
        int times = 1000;        // 测试轮数
        int queryTimes = 1000;   // 每轮查询次数
        int len = 300;           // 数组长度上限
        int val = 20;            // 元素值上限
        
        System.out.println("=== 区间值查询算法测试 ===");
        
        // 手动测试用例
        int[] testArr = {1, 2, 3, 2, 1, 4, 2, 3};
        QueryBox1 box1Test = new QueryBox1(testArr);
        QueryBox2 box2Test = new QueryBox2(testArr);
        
        System.out.println("手动测试用例: [1, 2, 3, 2, 1, 4, 2, 3]");
        System.out.println("查询(0, 3, 2): ");
        System.out.println("  方法1结果: " + box1Test.query(0, 3, 2));
        System.out.println("  方法2结果: " + box2Test.query(0, 3, 2));
        System.out.println("  期望结果: 2");
        
        System.out.println("查询(2, 7, 3): ");
        System.out.println("  方法1结果: " + box1Test.query(2, 7, 3));
        System.out.println("  方法2结果: " + box2Test.query(2, 7, 3));
        System.out.println("  期望结果: 2");
        System.out.println();
        
        // 正确性对拍测试
        System.out.println("开始正确性对拍测试...");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(len, val);
            int n = arr.length;
            QueryBox1 box1 = new QueryBox1(arr);
            QueryBox2 box2 = new QueryBox2(arr);
            
            // 对每个数组进行多次查询测试
            for (int j = 0; j < queryTimes; j++) {
                int a = (int) (Math.random() * n);
                int b = (int) (Math.random() * n);
                int l = Math.min(a, b);
                int r = Math.max(a, b);
                int v = (int) (Math.random() * val) + 1;
                
                if (box1.query(l, r, v) != box2.query(l, r, v)) {
                    System.out.println("发现错误!");
                    System.out.println("数组: " + java.util.Arrays.toString(arr));
                    System.out.println("查询: query(" + l + ", " + r + ", " + v + ")");
                    System.out.println("方法1结果: " + box1.query(l, r, v));
                    System.out.println("方法2结果: " + box2.query(l, r, v));
                    return;
                }
            }
        }
        System.out.println("正确性测试通过!");
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        int[] bigArr = new int[10000];
        for (int i = 0; i < bigArr.length; i++) {
            bigArr[i] = (int) (Math.random() * 100) + 1;
        }
        
        QueryBox1 box1 = new QueryBox1(bigArr);
        QueryBox2 box2 = new QueryBox2(bigArr);
        
        // 测试方法1性能
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            int l = (int) (Math.random() * 5000);
            int r = l + (int) (Math.random() * (5000 - l));
            int v = (int) (Math.random() * 100) + 1;
            box1.query(l, r, v);
        }
        long end1 = System.currentTimeMillis();
        
        // 测试方法2性能
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            int l = (int) (Math.random() * 5000);
            int r = l + (int) (Math.random() * (5000 - l));
            int v = (int) (Math.random() * 100) + 1;
            box2.query(l, r, v);
        }
        long end2 = System.currentTimeMillis();
        
        System.out.println("数组大小: " + bigArr.length);
        System.out.println("查询次数: 100000");
        System.out.println("方法1耗时: " + (end1 - start1) + "ms");
        System.out.println("方法2耗时: " + (end2 - start2) + "ms");
        System.out.println("性能提升: " + ((double)(end1 - start1) / (end2 - start2)) + "倍");
        
        System.out.println("\n=== 算法总结 ===");
        System.out.println("方法1 - 暴力查询:");
        System.out.println("  优点: 实现简单，无预处理成本");
        System.out.println("  缺点: 每次查询O(n)时间复杂度");
        System.out.println("  适用: 查询次数少的场景");
        
        System.out.println("方法2 - 预处理+二分:");
        System.out.println("  优点: 查询时间O(log n)，高频查询性能优秀");
        System.out.println("  缺点: 预处理O(n)时间和空间成本");
        System.out.println("  适用: 查询次数多的场景");
    }
}

