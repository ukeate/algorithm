package basic.c36;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * TopK两数组累加和问题 - 通用版本
 * 
 * 问题描述：
 * 给定两个有序数组arr1和arr2，返回两数组元素累加和的前K大值。
 * 这是TopKSumCrossNowCoder.java的通用版本，包含性能测试和对比验证。
 * 
 * 算法对比：
 * 方法1：优化算法 - 大根堆+二维布尔数组去重 O(K*logK)
 * 方法2：暴力算法 - 生成所有组合后排序 O(N*M*log(N*M))
 * 
 * 适用场景：
 * - 当K << N*M时，方法1效率远高于方法2
 * - 当K接近N*M时，两种方法性能相近
 * 
 * 时间复杂度：O(K*logK) vs O(N*M*log(N*M))
 * 空间复杂度：O(K + N*M) vs O(N*M)
 */
public class TopKSumCrossTwoArrays {
    
    /**
     * 节点类：存储两个数组的索引和对应的和值
     */
    public static class Node {
        public int i1;   // arr1中的索引
        public int i2;   // arr2中的索引
        public int sum;  // arr1[i1] + arr2[i2]
        
        /**
         * 构造函数
         * @param idx1 arr1的索引
         * @param idx2 arr2的索引
         * @param s 累加和
         */
        public Node(int idx1, int idx2, int s) {
            i1 = idx1;
            i2 = idx2;
            sum = s;
        }
    }

    /**
     * 大根堆比较器：按sum降序排列
     */
    private static class Comp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o2.sum - o1.sum;  // 大根堆：sum大的优先级高
        }
    }

    /**
     * 方法1：优化算法 - 大根堆+二维布尔数组去重
     * 
     * 相比NowCoder版本的改进：
     * 1. 使用二维布尔数组而非HashSet，避免哈希计算开销
     * 2. 更直观的去重逻辑，易于理解和调试
     * 
     * 算法流程：
     * 1. 从最大可能和开始（右下角）
     * 2. 使用大根堆维护候选位置
     * 3. 每次取出最大和，扩展其左、上相邻位置
     * 4. 用二维布尔数组标记已访问位置
     * 
     * @param arr1 第一个有序数组（递增）
     * @param arr2 第二个有序数组（递增）
     * @param k TopK的K值
     * @return TopK累加和数组，按降序排列
     */
    public static int[] topK(int[] arr1, int[] arr2, int k) {
        // 边界条件检查
        if (arr1 == null || arr2 == null || k < 1) {
            return null;
        }
        
        k = Math.min(k, arr1.length * arr2.length);  // k不超过总组合数
        int[] res = new int[k];
        int resIdx = 0;
        
        // 大根堆：存储候选节点
        PriorityQueue<Node> heap = new PriorityQueue<>(new Comp());
        
        // 二维布尔数组：标记已加入堆的位置
        boolean[][] set = new boolean[arr1.length][arr2.length];
        
        // 从右下角开始（最大可能和）
        int i1 = arr1.length - 1;
        int i2 = arr2.length - 1;
        heap.add(new Node(i1, i2, arr1[i1] + arr2[i2]));
        set[i1][i2] = true;
        
        // 循环提取K个最大和
        while (resIdx != k) {
            Node cur = heap.poll();  // 取出当前最大和
            res[resIdx++] = cur.sum;
            
            i1 = cur.i1;
            i2 = cur.i2;
            
            // 向左扩展：(i1-1, i2)
            if (i1 - 1 >= 0 && !set[i1 - 1][i2]) {
                set[i1 - 1][i2] = true;
                heap.add(new Node(i1 - 1, i2, arr1[i1 - 1] + arr2[i2]));
            }
            
            // 向上扩展：(i1, i2-1)
            if (i2 - 1 >= 0 && !set[i1][i2 - 1]) {
                set[i1][i2 - 1] = true;
                heap.add(new Node(i1, i2 - 1, arr1[i1] + arr2[i2 - 1]));
            }
        }
        
        return res;
    }

    /**
     * 方法2：暴力算法 - 生成所有组合后排序
     * 
     * 算法思路：
     * 1. 生成所有可能的两数累加和
     * 2. 对所有和值进行排序
     * 3. 返回最大的K个值
     * 
     * 优点：简单直观，逻辑清晰
     * 缺点：当数组很大而K较小时，效率低下
     * 
     * @param arr1 第一个有序数组
     * @param arr2 第二个有序数组
     * @param k TopK的K值
     * @return TopK累加和数组，按降序排列
     */
    public static int[] topKSure(int[] arr1, int[] arr2, int k) {
        // 生成所有可能的累加和
        int[] all = new int[arr1.length * arr2.length];
        int idx = 0;
        
        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr2.length; j++) {
                all[idx++] = arr1[i] + arr2[j];
            }
        }
        
        // 排序所有累加和
        Arrays.sort(all);
        
        // 提取最大的K个值（从后往前取）
        int[] res = new int[Math.min(k, all.length)];
        idx = all.length - 1;
        for (int i = 0; i < res.length; i++) {
            res[i] = all[idx--];
        }
        
        return res;
    }

    /**
     * 生成随机有序数组用于测试
     * 
     * @param len 数组长度
     * @return 随机生成的递增有序数组
     */
    private static int[] randomSortedArr(int len) {
        int[] res = new int[len];
        for (int i = 0; i < res.length; i++) {
            res[i] = (int) (50000 * Math.random()) + 1;
        }
        Arrays.sort(res);  // 确保数组有序
        return res;
    }

    /**
     * 比较两个数组是否相等
     * 
     * @param arr1 第一个数组
     * @param arr2 第二个数组
     * @return 是否相等
     */
    private static boolean isEqual(int[] arr1, int[] arr2) {
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1 == null ^ arr2 == null) {
            return false;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 辅助方法：打印数组
     * @param arr 要打印的数组
     * @param maxLen 最多打印的元素个数
     */
    private static void printArray(int[] arr, int maxLen) {
        if (arr == null) {
            System.out.println("null");
            return;
        }
        
        System.out.print("[");
        int limit = Math.min(arr.length, maxLen);
        for (int i = 0; i < limit; i++) {
            System.out.print(arr[i]);
            if (i < limit - 1) System.out.print(", ");
        }
        if (arr.length > maxLen) {
            System.out.print("...");
        }
        System.out.println("]");
    }

    /**
     * 性能测试主方法
     * 
     * 测试场景：
     * - 大规模数组（5000 x 4000）
     * - 大K值（2000000）
     * - 对比两种算法的性能差异
     */
    public static void main(String[] args) {
        System.out.println("=== TopK两数组累加和性能测试 ===");
        
        // 测试参数
        int a1len = 5000;   // arr1长度
        int a2len = 4000;   // arr2长度
        int k = 2000000;    // TopK的K值
        
        System.out.println("测试参数：");
        System.out.println("arr1长度: " + a1len);
        System.out.println("arr2长度: " + a2len);
        System.out.println("K值: " + k);
        System.out.println("总组合数: " + (a1len * a2len));
        System.out.println();
        
        // 生成测试数据
        int[] arr1 = randomSortedArr(a1len);
        int[] arr2 = randomSortedArr(a2len);
        
        // 测试优化算法
        System.out.println("测试优化算法（大根堆）：");
        long start = System.currentTimeMillis();
        int[] ans1 = topK(arr1, arr2, k);
        long end = System.currentTimeMillis();
        System.out.println("耗时: " + (end - start) + " ms");
        System.out.print("前10个结果: ");
        printArray(ans1, 10);
        System.out.println();
        
        // 测试暴力算法
        System.out.println("测试暴力算法（全排序）：");
        start = System.currentTimeMillis();
        int[] ans2 = topKSure(arr1, arr2, k);
        end = System.currentTimeMillis();
        System.out.println("耗时: " + (end - start) + " ms");
        System.out.print("前10个结果: ");
        printArray(ans2, 10);
        System.out.println();
        
        // 验证结果正确性
        boolean correct = isEqual(ans1, ans2);
        System.out.println("结果正确性验证: " + (correct ? "✅ 通过" : "❌ 失败"));
        
        if (!correct) {
            System.out.println("发现结果不一致！");
            System.out.print("优化算法前20个: ");
            printArray(ans1, 20);
            System.out.print("暴力算法前20个: ");
            printArray(ans2, 20);
        }
        
        System.out.println("\n=== 测试完成 ===");
    }
}
