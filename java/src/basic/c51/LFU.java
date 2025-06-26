package basic.c51;

import java.util.HashMap;

/**
 * LFU缓存实现 (Least Frequently Used)
 * 
 * 问题描述：
 * 设计并实现最不经常使用（LFU）缓存的数据结构。
 * 它应该支持以下操作：get 和 put。
 * 
 * get(key) - 如果键存在于缓存中，则获取键的值（总是正数），否则返回 null。
 * put(key, value) - 如果键不存在，请设置或插入值。
 * 当缓存达到其容量时，它应该在插入新项目之前，移除最不经常使用的项目。
 * 在此问题中，当存在平局（即两个或更多个键具有相同使用频率）时，
 * 最近最少使用的键将被去除。
 * 
 * 数据结构设计：
 * 1. Node：存储key-value和访问频次
 * 2. NodeList：按访问频次分组的双向链表（桶结构）
 * 3. HashMap<Integer, Node>：key到node的映射
 * 4. HashMap<Node, NodeList>：node到其所在桶的映射
 * 5. headList：指向频次最小的桶
 * 
 * 时间复杂度：get和put操作都是O(1)
 * 空间复杂度：O(capacity)
 * 
 * LeetCode: https://leetcode.com/problems/lfu-cache/
 * 
 * @author 算法学习
 */
public class LFU {
    
    /**
     * 缓存节点类
     * 存储键值对和访问频次信息
     */
    private static class Node {
        public Integer key;        // 键
        public Integer val;        // 值
        public Integer times;      // 访问频次
        public Node up;           // 上一个节点
        public Node down;         // 下一个节点
        
        /**
         * 构造缓存节点
         * 
         * @param k 键
         * @param v 值
         * @param t 访问频次
         */
        public Node(int k, int v, int t) {
            key = k;
            val = v;
            times = t;
        }
    }

    /**
     * 节点桶类 - 按访问频次分组的双向链表
     * 每个桶内的节点具有相同的访问频次
     * 桶内按照访问时间排序（头部是最近访问的）
     */
    public static class NodeList {
        public Node head;          // 桶的头节点（最近访问）
        public Node tail;          // 桶的尾节点（最早访问）
        public NodeList last;      // 前一个桶（频次更小）
        public NodeList next;      // 后一个桶（频次更大）
        
        /**
         * 创建新桶，初始包含一个节点
         * 
         * @param node 初始节点
         */
        public NodeList(Node node) {
            head = node;
            tail = node;
        }

        /**
         * 在桶的头部添加节点
         * 
         * @param n 要添加的节点
         */
        public void addHead(Node n) {
            n.down = head;
            head.up = n;
            head = n;
        }

        /**
         * 从桶中删除指定节点
         * 
         * @param n 要删除的节点
         */
        public void delete(Node n) {
            if (head == tail) {
                // 桶中只有一个节点
                head = null;
                tail = null;
            } else {
                if (n == head) {
                    // 删除头节点
                    head = n.down;
                    head.up = null;
                } else if (n == tail) {
                    // 删除尾节点
                    tail = n.up;
                    tail.down = null;
                } else {
                    // 删除中间节点
                    n.up.down = n.down;
                    n.down.up = n.up;
                }
            }
            // 清理节点连接
            n.up = null;
            n.down = null;
        }

        /**
         * 判断桶是否为空
         * 
         * @return 桶是否为空
         */
        public boolean isEmpty() {
            return head == null;
        }
    }

    /**
     * LFU缓存主类
     */
    public static class LFUCache {
        private int capacity;                           // 缓存容量
        private int size;                              // 当前缓存大小
        private HashMap<Integer, Node> records;         // key -> node 映射
        private HashMap<Node, NodeList> heads;          // node -> 所在桶 映射
        private NodeList headList;                      // 频次最小的桶
        
        /**
         * 初始化LFU缓存
         * 
         * @param k 缓存容量
         */
        public LFUCache(int k) {
            capacity = k;
            size = 0;
            records = new HashMap<>();
            heads = new HashMap<>();
            headList = null;
        }

        /**
         * 修改桶链表结构，删除空桶
         * 
         * @param removeNodeList 要检查删除的桶
         * @return 是否删除了桶
         */
        private boolean modifyHeadList(NodeList removeNodeList) {
            if (removeNodeList.isEmpty()) {
                if (headList == removeNodeList) {
                    // 删除的是频次最小的桶
                    headList = removeNodeList.next;
                    if (headList != null) {
                        headList.last = null;
                    }
                } else {
                    // 删除中间的桶
                    removeNodeList.last.next = removeNodeList.next;
                    if (removeNodeList.next != null) {
                        removeNodeList.next.last = removeNodeList.last;
                    }
                }
                return true;
            }
            return false;
        }

        /**
         * 将节点从旧桶移动到新桶（频次+1）
         * 
         * @param node 要移动的节点
         * @param oldNodeList 节点当前所在的桶
         */
        private void move(Node node, NodeList oldNodeList) {
            // 从旧桶中删除节点
            oldNodeList.delete(node);
            
            // 确定新桶的前一个桶位置
            NodeList preList = modifyHeadList(oldNodeList) ? oldNodeList.last : oldNodeList;
            NodeList nextList = oldNodeList.next;
            
            if (nextList == null) {
                // 没有下一个桶，创建新桶
                NodeList newList = new NodeList(node);
                if (preList != null) {
                    preList.next = newList;
                }
                newList.last = preList;
                if (headList == null) {
                    headList = newList;
                }
                heads.put(node, newList);
            } else {
                if (nextList.head.times.equals(node.times)) {
                    // 下一个桶的频次匹配，加入该桶
                    nextList.addHead(node);
                    heads.put(node, nextList);
                } else {
                    // 下一个桶的频次不匹配，创建新桶
                    NodeList newList = new NodeList(node);
                    if (preList != null) {
                        preList.next = newList;
                    }
                    newList.last = preList;
                    newList.next = nextList;
                    nextList.last = newList;
                    if (headList == nextList) {
                        headList = newList;
                    }
                    heads.put(node, newList);
                }
            }
        }

        /**
         * 向缓存中插入或更新键值对
         * 
         * @param key 键
         * @param val 值
         */
        public void put(int key, int val) {
            if (capacity == 0) {
                return;
            }
            
            if (records.containsKey(key)) {
                // 更新已存在的键
                Node node = records.get(key);
                node.val = val;
                node.times++;
                NodeList curNodeList = heads.get(node);
                move(node, curNodeList);
            } else {
                // 插入新键
                if (size == capacity) {
                    // 缓存已满，删除频次最小且最早访问的节点
                    Node node = headList.tail;
                    headList.delete(node);
                    modifyHeadList(headList);
                    records.remove(node.key);
                    heads.remove(node);
                    size--;
                }
                
                // 创建新节点
                Node node = new Node(key, val, 1);
                if (headList == null) {
                    // 第一个节点，创建第一个桶
                    headList = new NodeList(node);
                } else {
                    if (headList.head.times.equals(node.times)) {
                        // 频次匹配，加入现有桶
                        headList.addHead(node);
                    } else {
                        // 频次不匹配，创建新的频次最小桶
                        NodeList newList = new NodeList(node);
                        newList.next = headList;
                        headList.last = newList;
                        headList = newList;
                    }
                }
                records.put(key, node);
                heads.put(node, headList);
                size++;
            }
        }

        /**
         * 从缓存中获取键对应的值
         * 
         * @param key 键
         * @return 对应的值，不存在返回null
         */
        public Integer get(int key) {
            if (!records.containsKey(key)) {
                return null;
            }
            
            // 找到节点并增加访问频次
            Node node = records.get(key);
            node.times++;
            NodeList curNodeList = heads.get(node);
            move(node, curNodeList);
            return node.val;
        }
    }
    
    /**
     * 测试LFU缓存功能
     */
    public static void main(String[] args) {
        System.out.println("=== LFU缓存测试 ===");
        
        // 创建容量为2的LFU缓存
        LFUCache cache = new LFUCache(2);
        
        // 测试基本操作
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println("put(1,1), put(2,2)");
        
        System.out.println("get(1) = " + cache.get(1));  // 返回 1
        
        cache.put(3, 3);    // 去除键 2，因为键 2 的使用频次最少
        System.out.println("put(3,3) - 应该移除key=2");
        
        System.out.println("get(2) = " + cache.get(2));  // 返回 null (未找到)
        System.out.println("get(3) = " + cache.get(3));  // 返回 3
        
        cache.put(4, 4);    // 去除键 1，因为键 1 的使用频次最少
        System.out.println("put(4,4) - 应该移除key=1");
        
        System.out.println("get(1) = " + cache.get(1));  // 返回 null (未找到)
        System.out.println("get(3) = " + cache.get(3));  // 返回 3
        System.out.println("get(4) = " + cache.get(4));  // 返回 4
        
        System.out.println("\n=== 复杂测试用例 ===");
        
        // 测试频次相同时的LRU淘汰
        LFUCache cache2 = new LFUCache(3);
        cache2.put(1, 1);
        cache2.put(2, 2);
        cache2.put(3, 3);
        
        // 让所有键的频次都变为2
        cache2.get(1);
        cache2.get(2);
        cache2.get(3);
        
        // 再访问1和2
        cache2.get(1);
        cache2.get(2);
        
        // 此时频次: 1->3, 2->3, 3->2
        // 插入新元素应该淘汰频次最小的3
        cache2.put(4, 4);
        
        System.out.println("复杂测试 - get(3) = " + cache2.get(3)); // 应该返回null
        System.out.println("复杂测试 - get(1) = " + cache2.get(1)); // 应该返回1
        System.out.println("复杂测试 - get(2) = " + cache2.get(2)); // 应该返回2
        System.out.println("复杂测试 - get(4) = " + cache2.get(4)); // 应该返回4
    }
}
