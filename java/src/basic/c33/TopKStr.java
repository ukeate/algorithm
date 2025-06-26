package basic.c33;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * 字符串数组中的Top-K问题
 * 
 * 问题描述：
 * 给定一个字符串数组，找出其中出现频率最高的前K个字符串。
 * 
 * 算法思路：
 * 1. 使用HashMap统计每个字符串的出现次数
 * 2. 使用大小为K的最小堆维护Top-K结果
 * 3. 遍历HashMap，对于每个字符串频次：
 *    - 如果堆未满，直接加入
 *    - 如果堆已满且当前频次大于堆顶，替换堆顶
 * 4. 最终堆中就是Top-K的结果
 * 
 * 时间复杂度：O(N + M*logK)，其中N是数组长度，M是不同字符串个数
 * 空间复杂度：O(M + K)，HashMap存储M个不同字符串，堆大小为K
 */
public class TopKStr {
    
    /**
     * 节点类：封装字符串和其出现次数
     */
    private static class Node {
        public String str;   // 字符串内容
        public int times;    // 出现次数
        
        /**
         * 构造函数
         * @param s 字符串
         * @param t 出现次数
         */
        public Node(String s, int t) {
            str = s;
            times = t;
        }
    }

    /**
     * 比较器：按出现次数升序排列（用于最小堆）
     * 堆顶是出现次数最少的节点，便于维护Top-K
     */
    private static class Comp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.times - o2.times; // 升序：次数少的在堆顶
        }
    }

    /**
     * 找出字符串数组中出现频率最高的前topK个字符串
     * 
     * 算法步骤：
     * 1. 统计每个字符串的出现次数
     * 2. 使用最小堆维护Top-K结果
     * 3. 输出堆中的所有字符串（注意：输出顺序是从小到大）
     * 
     * @param arr 字符串数组
     * @param topK 需要找出的Top-K数量
     */
    public static void top(String[] arr, int topK) {
        // 边界条件检查
        if (arr == null || arr.length == 0 || topK < 1 || topK > arr.length) {
            return;
        }
        
        // 步骤1：统计每个字符串的出现次数
        HashMap<String, Integer> map = new HashMap<>();
        for (String str : arr) {
            if (!map.containsKey(str)) {
                map.put(str, 1);                    // 首次出现
            } else {
                map.put(str, map.get(str) + 1);     // 增加计数
            }
        }
        
        // 确保topK不超过不同字符串的数量
        topK = Math.min(map.size(), topK);
        
        // 步骤2：使用最小堆维护Top-K
        PriorityQueue<Node> heap = new PriorityQueue<>(new Comp());
        
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            Node cur = new Node(entry.getKey(), entry.getValue());
            
            if (heap.size() < topK) {
                // 堆未满，直接加入
                heap.add(cur);
            } else {
                // 堆已满，比较当前节点与堆顶
                if (heap.peek().times < cur.times) {
                    heap.poll();    // 移除堆顶（频次最小的）
                    heap.add(cur);  // 加入当前节点
                }
            }
        }
        
        // 步骤3：输出结果
        // 注意：由于使用最小堆，输出顺序是从频次小到大
        System.out.println("Top-" + topK + " 字符串（按出现次数从少到多）：");
        while (!heap.isEmpty()) {
            Node node = heap.poll();
            System.out.println(node.str + " (出现" + node.times + "次)");
        }
    }

    /**
     * 生成随机字符串数组用于测试
     * @param len 数组长度
     * @param maxVal 字符串值的最大范围（0到maxVal）
     * @return 随机字符串数组
     */
    private static String[] randomArr(int len, int maxVal) {
        String[] rst = new String[len];
        for (int i = 0; i < len; i++) {
            // 生成0到maxVal范围内的随机数作为字符串
            rst[i] = String.valueOf((int) ((maxVal + 1) * Math.random()));
        }
        return rst;
    }

    /**
     * 打印字符串数组
     * @param arr 要打印的字符串数组
     */
    private static void print(String[] arr) {
        System.out.print("原数组: ");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    /**
     * 测试方法：验证Top-K算法的正确性
     */
    public static void main(String[] args) {
        // 测试用例1：固定数组
        String[] arr1 = {"A", "B", "C", "A", "C", "B", "B", "K"};
        System.out.println("=== 测试用例1 ===");
        print(arr1);
        top(arr1, 2);
        
        // 测试用例2：随机数组
        String[] arr2 = randomArr(50, 10);
        int topK = 3;
        System.out.println("\n=== 测试用例2 ===");
        print(arr2);
        top(arr2, topK);
        
        // 测试用例3：边界情况
        String[] arr3 = {"same", "same", "same"};
        System.out.println("\n=== 测试用例3：相同字符串 ===");
        print(arr3);
        top(arr3, 1);
    }
}
