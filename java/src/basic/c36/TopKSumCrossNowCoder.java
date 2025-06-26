package basic.c36;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * TopK两数组累加和问题 - NowCoder版本
 * 
 * 问题描述：
 * 给定两个有序数组arr1和arr2，返回两数组元素累加和的前K大值。
 * 两数和是指从arr1中选一个数，从arr2中选一个数，两数相加的结果。
 * 
 * 例如：
 * arr1 = [1, 3, 5], arr2 = [2, 4, 6]
 * 所有可能的和：[3, 5, 7, 5, 7, 9, 7, 9, 11]
 * TopK=3: [11, 9, 9]
 * 
 * 算法思路：
 * 使用大根堆+去重集合的经典TopK策略：
 * 1. 从最大可能和开始（arr1最大元素 + arr2最大元素）
 * 2. 每次取出堆顶元素，扩展其相邻的候选解
 * 3. 使用HashSet避免重复加入堆中
 * 4. 直到取出K个元素
 * 
 * 关键优化：
 * - 从右下角开始，向左上扩展（因为数组有序）
 * - 使用哈希编码避免重复访问同一位置
 * 
 * 时间复杂度：O(K*logK)
 * 空间复杂度：O(K)
 * 
 * 参考链接：https://www.nowcoder.com/practice/7201cacf73e7495aa5f88b223bbbf6d1
 */
public class TopKSumCrossNowCoder {
    
    /**
     * 节点类：存储两个数组的索引和对应的和值
     */
    private static class Node {
        public int i1;   // arr1中的索引
        public int i2;   // arr2中的索引
        public int sum;  // arr1[i1] + arr2[i2]

        /**
         * 构造函数
         * @param index1 arr1的索引
         * @param index2 arr2的索引
         * @param s 累加和
         */
        public Node(int index1, int index2, int s) {
            i1 = index1;
            i2 = index2;
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
     * 位置编码函数：将二维坐标编码为唯一的长整数
     * 用于HashSet去重，避免重复访问同一位置
     * 
     * @param i1 arr1的索引
     * @param i2 arr2的索引
     * @param m arr2的长度（用作编码基数）
     * @return 唯一的位置编码
     */
    private static long x(int i1, int i2, int m) {
        return (long) i1 * (long) m + (long) i2;
    }

    /**
     * 获取TopK两数组累加和
     * 
     * 算法流程：
     * 1. 初始化大根堆，加入最大可能和（右下角位置）
     * 2. 循环K次，每次：
     *    a. 取出堆顶元素作为当前最大和
     *    b. 扩展该位置的相邻候选位置（向左、向上）
     *    c. 使用HashSet避免重复加入
     * 3. 返回K个最大和值
     * 
     * @param arr1 第一个有序数组
     * @param arr2 第二个有序数组
     * @param k 需要返回的前K大和值个数
     * @return TopK累加和数组，按降序排列
     */
    public static int[] topK(int[] arr1, int[] arr2, int k) {
        // 边界条件检查
        if (arr1 == null || arr2 == null || k < 1) {
            return null;
        }
        
        int n = arr1.length;
        int m = arr2.length;
        k = Math.min(k, n * m);  // k不能超过总的组合数
        
        int[] res = new int[k];  // 结果数组
        int resIdx = 0;          // 结果数组索引
        
        // 大根堆：存储候选的累加和节点
        PriorityQueue<Node> heap = new PriorityQueue<>(new Comp());
        
        // 去重集合：存储已经加入堆的位置编码
        HashSet<Long> set = new HashSet<>();
        
        // 从最大可能和开始（右下角位置）
        int i1 = n - 1;
        int i2 = m - 1;
        heap.add(new Node(i1, i2, arr1[i1] + arr2[i2]));
        set.add(x(i1, i2, m));
        
        // 循环K次，每次取出一个最大和
        while (resIdx < k) {
            Node cur = heap.poll();  // 取出当前最大和
            res[resIdx++] = cur.sum;
            
            i1 = cur.i1;
            i2 = cur.i2;
            set.remove(x(i1, i2, m));  // 从去重集合中移除当前位置
            
            // 扩展相邻位置：向左移动（i1-1, i2）
            if (i1 - 1 >= 0 && !set.contains(x(i1 - 1, i2, m))) {
                set.add(x(i1 - 1, i2, m));
                heap.add(new Node(i1 - 1, i2, arr1[i1 - 1] + arr2[i2]));
            }
            
            // 扩展相邻位置：向上移动（i1, i2-1）
            if (i2 - 1 >= 0 && !set.contains(x(i1, i2 - 1, m))) {
                set.add(x(i1, i2 - 1, m));
                heap.add(new Node(i1, i2 - 1, arr1[i1] + arr2[i2 - 1]));
            }
        }
        
        return res;
    }

    /**
     * 主方法：读取输入并输出TopK结果
     * 输入格式：
     * - 第一行：n（数组长度）和k（TopK的K值）
     * - 第二行：arr1的n个元素
     * - 第三行：arr2的n个元素
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // 读取输入
        int n = sc.nextInt();
        int k = sc.nextInt();
        
        int[] arr1 = new int[n];
        int[] arr2 = new int[n];
        
        for (int i = 0; i < n; i++) {
            arr1[i] = sc.nextInt();
        }
        
        for (int i = 0; i < n; i++) {
            arr2[i] = sc.nextInt();
        }
        
        // 计算TopK结果
        int[] topK = topK(arr1, arr2, k);
        
        // 输出结果
        for (int i = 0; i < k; i++) {
            System.out.print(topK[i] + " ");
        }
        System.out.println();
        
        sc.close();
    }
}
