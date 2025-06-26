package basic.c33;

import java.util.*;

/**
 * 实时Top-K字符串问题 - TreeSet实现版本
 * 
 * 问题描述：
 * 设计一个数据结构，支持实时维护出现频率最高的前K个字符串。
 * 与TopKStrRealtime.java不同，这个版本使用TreeSet来维护Top-K结果。
 * 
 * 核心思想：
 * 1. 使用HashMap统计字符串出现次数
 * 2. 使用TreeSet维护Top-K，自动保持有序
 * 3. TreeSet按频次升序+字典序降序排列，便于淘汰和输出
 * 
 * 与方法1的区别：
 * - 方法1使用最小堆 + 位置映射，操作复杂但空间效率高
 * - 方法2使用TreeSet，代码简洁但可能有额外开销
 * 
 * 时间复杂度：每次add操作O(logK)
 * 空间复杂度：O(N+K)
 * 
 * 参考链接：https://www.lintcode.com/problem/top-k-frequent-words-ii/description
 */
public class TopKStrRealtime2 {
    
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
     * 堆比较器：用于TreeSet中维护Top-K
     * 排序规则：
     * 1. 按出现次数升序（频次低的排在前面，便于淘汰）
     * 2. 次数相同时按字典序降序（字典序大的排在前面）
     */
    public static class HeapComp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.times != o2.times ? 
                   (o1.times - o2.times) :          // 频次升序
                   (o2.str.compareTo(o1.str));      // 字典序降序
        }
    }

    /**
     * 输出比较器：用于最终结果排序
     * 排序规则：
     * 1. 按出现次数降序（频次高的排在前面）
     * 2. 次数相同时按字典序升序（字典序小的排在前面）
     */
    public static class TreeComp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.times != o2.times ? 
                   (o2.times - o1.times) :          // 频次降序
                   (o1.str.compareTo(o2.str));      // 字典序升序
        }
    }

    private int k;                                    // Top-K的K值
    private HashMap<String, Node> strNodeMap;        // 字符串到节点的映射
    private HashMap<Node, Integer> nodeIdxMap;       // 节点到索引的映射（兼容性保留）
    private HeapComp comp;                           // TreeSet使用的比较器
    private TreeSet<Node> treeSet;                   // 维护Top-K的有序集合

    /**
     * 构造函数
     * @param k Top-K的K值
     */
    public TopKStrRealtime2(int k) {
        this.k = k;
        strNodeMap = new HashMap<>();
        nodeIdxMap = new HashMap<>();
        comp = new HeapComp();
        treeSet = new TreeSet<>(comp);
    }

    /**
     * 添加一个字符串
     * 
     * 处理流程：
     * 1. 如果是新字符串，创建新节点
     * 2. 如果是已存在字符串，增加计数并从TreeSet中移除旧节点
     * 3. 根据TreeSet大小和节点频次决定是否加入TreeSet
     * 
     * @param str 要添加的字符串
     */
    public void add(String str) {
        Node cur = null;
        int idx = -1;  // -1表示不在TreeSet中
        
        if (!strNodeMap.containsKey(str)) {
            // 新字符串，创建新节点
            cur = new Node(str, 1);
            strNodeMap.put(str, cur);
            nodeIdxMap.put(cur, -1);  // 初始不在TreeSet中
        } else {
            // 已存在字符串，更新频次
            cur = strNodeMap.get(str);
            
            // 如果节点已在TreeSet中，先移除（因为频次要变化）
            if (treeSet.contains(cur)) {
                treeSet.remove(cur);
            }
            
            cur.times++;  // 增加频次
            idx = nodeIdxMap.get(cur);
        }
        
        // 决定是否将节点加入TreeSet
        if (idx == -1) {
            // 节点不在TreeSet中
            if (treeSet.size() == k) {
                // TreeSet已满，需要比较是否替换最小频次的节点
                if (comp.compare(treeSet.first(), cur) < 0) {
                    // 当前节点比TreeSet中最小的节点大，替换
                    treeSet.pollFirst();  // 移除最小节点
                    treeSet.add(cur);     // 加入当前节点
                }
                // 否则不加入TreeSet
            } else {
                // TreeSet未满，直接加入
                treeSet.add(cur);
            }
        } else {
            // 节点原本在TreeSet中，重新加入（频次已更新）
            treeSet.add(cur);
        }
    }

    /**
     * 获取当前Top-K字符串列表
     * 
     * 返回顺序：按频次降序，频次相同时按字典序升序
     * 
     * @return Top-K字符串列表
     */
    public List<String> topK() {
        int n = treeSet.size();
        String[] arr = new String[n];
        Iterator<Node> it = treeSet.iterator();
        
        // TreeSet中的顺序是按HeapComp排序的（频次升序）
        // 需要反向遍历得到频次降序的结果
        for (int i = n - 1; i >= 0; i--) {
            arr[i] = it.next().str;
        }
        
        return Arrays.asList(arr);
    }

    /**
     * 测试方法：验证算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== TreeSet版本实时Top-K测试 ===");
        
        TopKStrRealtime2 top = new TopKStrRealtime2(2);
        
        System.out.println("1. 添加 'aaa':");
        top.add("aaa");
        for (String s : top.topK()) {
            System.out.println("  " + s);
        }
        
        System.out.println("\n2. 添加 'bbb' 两次:");
        top.add("bbb");
        top.add("bbb");
        for (String s : top.topK()) {
            System.out.println("  " + s);
        }
        
        System.out.println("\n3. 添加 'ccc' 两次:");
        top.add("ccc");
        top.add("ccc");
        for (String s : top.topK()) {
            System.out.println("  " + s);
        }
        
        System.out.println("\n4. 添加 'ddd' 三次:");
        top.add("ddd");
        top.add("ddd");
        top.add("ddd");
        for (String s : top.topK()) {
            System.out.println("  " + s);
        }
        
        System.out.println("\n=== 测试完成 ===");
    }
}
