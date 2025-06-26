package basic.c33;

import java.util.HashMap;

/**
 * 实时Top-K字符串问题
 * 
 * 问题描述：
 * 设计一个数据结构，支持以下操作：
 * 1. add(String str): 添加一个字符串（可能重复）
 * 2. printTopK(): 实时输出当前出现频率最高的K个字符串
 * 
 * 核心挑战：
 * - 需要实时维护Top-K，不能每次都重新计算
 * - 当字符串频次更新时，需要高效调整堆结构
 * - 字符串可能在堆内或堆外，需要分别处理
 * 
 * 算法思路：
 * 1. 使用最小堆维护Top-K，堆顶是频次最小的
 * 2. 用HashMap<String, Node>快速定位字符串对应的节点
 * 3. 用HashMap<Node, Integer>快速定位节点在堆中的位置
 * 4. 当字符串频次增加时：
 *    - 如果在堆内：堆化调整位置
 *    - 如果不在堆内：判断是否应该进入堆
 * 
 * 时间复杂度：每次add操作O(logK)
 * 空间复杂度：O(N+K)，N为不同字符串数量，K为Top-K大小
 * 
 * 参考链接：https://www.lintcode.com/problem/top-k-frequent-words-ii/
 */
public class TopKStrRealtime {

    /**
     * Top-K数据结构的主类
     */
    public static class TopK {
        private Node[] heap;                           // 最小堆数组
        private int heapSize;                          // 堆的实际大小
        private HashMap<String, Node> strNodeMap;     // 字符串到节点的映射
        private HashMap<Node, Integer> nodeIdxMap;    // 节点到堆索引的映射

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
         * 构造函数：初始化Top-K数据结构
         * @param k Top-K的K值，堆的最大容量
         */
        public TopK(int k) {
            heap = new Node[k];              // 初始化堆数组
            heapSize = 0;                    // 堆初始为空
            strNodeMap = new HashMap<>();    // 字符串映射
            nodeIdxMap = new HashMap<>();    // 位置映射
        }

        /**
         * 交换堆中两个位置的节点
         * 同时更新位置映射表
         * @param i1 位置1
         * @param i2 位置2
         */
        private void swap(int i1, int i2) {
            // 更新位置映射
            nodeIdxMap.put(heap[i1], i2);
            nodeIdxMap.put(heap[i2], i1);
            
            // 交换节点
            Node tmp = heap[i1];
            heap[i1] = heap[i2];
            heap[i2] = tmp;
        }

        /**
         * 向上调整堆（用于插入新节点）
         * 将指定位置的节点向上移动到合适位置
         * @param idx 要调整的节点位置
         */
        private void heapInsert(int idx) {
            while (idx != 0){
                int parent = (idx - 1) / 2;  // 父节点位置
                
                // 如果当前节点次数小于父节点，向上移动（最小堆）
                if (heap[idx].times < heap[parent].times) {
                    swap(parent, idx);
                    idx = parent;
                } else {
                    break; // 已到达正确位置
                }
            }
        }

        /**
         * 向下调整堆（用于删除或更新节点）
         * 将指定位置的节点向下移动到合适位置
         * @param idx 要调整的节点位置
         * @param heapSize 堆的大小
         */
        private void heapify(int idx, int heapSize) {
            int l = idx * 2 + 1;  // 左子节点
            int r = idx * 2 + 2;  // 右子节点（注意这里原代码有错误）
            int small = idx;      // 最小值位置
            
            while (l < heapSize) {
                // 找到父子三节点中次数最小的
                if (heap[l].times < heap[idx].times) {
                    small = l;
                }
                if (r < heapSize && heap[r].times < heap[small].times) {
                    small = r;
                }
                
                // 如果需要调整，交换并继续向下
                if (small != idx) {
                    swap(small, idx);
                    idx = small;
                    l = idx * 2 + 1;
                    r = idx * 2 + 2;
                } else {
                    break; // 已到达正确位置
                }
            }
        }

        /**
         * 添加一个字符串
         * 这是核心方法，需要处理字符串在堆内外的各种情况
         * @param str 要添加的字符串
         */
        public void add(String str) {
            Node curNode = null;
            int preIdx = -1;  // -1表示不在堆中
            
            // 第一步：获取或创建节点，更新频次
            if (!strNodeMap.containsKey(str)) {
                // 新字符串，创建新节点
                curNode = new Node(str, 1);
                strNodeMap.put(str, curNode);
                nodeIdxMap.put(curNode, -1);  // 初始不在堆中
            } else {
                // 已存在字符串，增加频次
                curNode = strNodeMap.get(str);
                curNode.times++;
                preIdx = nodeIdxMap.get(curNode);  // 获取在堆中的位置
            }
            
            // 第二步：根据节点当前状态进行处理
            if (preIdx == -1) {
                // 节点不在堆中
                if (heapSize == heap.length) {
                    // 堆已满，需要比较是否应该替换堆顶
                    if (heap[0].times < curNode.times) {
                        // 当前节点频次大于堆顶，应该进入堆
                        nodeIdxMap.put(heap[0], -1);      // 堆顶移出堆
                        nodeIdxMap.put(curNode, 0);       // 当前节点进入堆顶
                        heap[0] = curNode;
                        heapify(0, heapSize);              // 向下调整
                    }
                    // 如果当前节点频次不大于堆顶，则不进入堆
                } else {
                    // 堆未满，直接加入堆
                    nodeIdxMap.put(curNode, heapSize);
                    heap[heapSize] = curNode;
                    heapInsert(heapSize++);               // 向上调整并增加堆大小
                }
            } else {
                // 节点已在堆中，频次增加后需要重新调整位置
                // 由于频次增加，可能需要向上移动（在最小堆中向上意味着频次变大）
                heapify(preIdx, heapSize);
            }
        }

        /**
         * 打印当前Top-K字符串
         * 注意：由于使用最小堆，输出顺序是频次从小到大
         */
        public void printTopK() {
            System.out.println("=== 当前Top-K字符串 ===");
            for (int i = 0; i < heap.length; i++) {
                if (heap[i] == null) {
                    break; // 堆未满的情况
                }
                System.out.println("字符串: " + heap[i].str + ", 出现次数: " + heap[i].times);
            }
            System.out.println();
        }
    }

    /**
     * 测试方法：验证实时Top-K算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 实时Top-K字符串测试 ===");
        
        // 创建Top-2的数据结构
        TopK top = new TopK(2);
        
        System.out.println("添加 'aaa':");
        top.add("aaa");
        top.printTopK();
        
        System.out.println("添加 'bbb' 两次:");
        top.add("bbb");
        top.add("bbb");
        top.printTopK();
        
        System.out.println("添加 'ccc' 两次:");
        top.add("ccc");
        top.add("ccc");
        top.printTopK();
        
        System.out.println("再次添加 'aaa' 三次:");
        top.add("aaa");
        top.add("aaa");
        top.add("aaa");
        top.printTopK();
    }
}
