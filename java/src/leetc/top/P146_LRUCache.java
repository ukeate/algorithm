package leetc.top;

import java.util.HashMap;

/**
 * LeetCode 146. LRU缓存 (LRU Cache)
 * 
 * 问题描述：
 * 设计一个遵循最近最少使用(LRU)缓存约束的数据结构。
 * 
 * 实现 LRUCache 类：
 * - LRUCache(int capacity) 以正整数作为容量 capacity 初始化 LRU 缓存
 * - int get(int key) 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1
 * - void put(int key, int value) 如果关键字 key 已经存在，则变更其数据值 value
 *   如果不存在，则向缓存中插入该组 key-value。如果插入操作导致关键字数量超过 capacity，
 *   则应该逐出最近最少使用的关键字
 * 
 * 函数 get 和 put 必须以 O(1) 的平均时间复杂度运行。
 * 
 * 设计思路：
 * HashMap + 双向链表的经典组合：
 * - HashMap：提供O(1)的查找和删除
 * - 双向链表：维护使用顺序，支持O(1)的节点移动和删除
 * - 最近使用的节点移动到链表尾部
 * - 当容量满时，删除链表头部（最久未使用）的节点
 */
public class P146_LRUCache {
    private Cache<Integer, Integer> cache;
    
    /**
     * 构造函数
     * @param c 缓存容量
     */
    public P146_LRUCache(int c) {
        cache = new Cache<>(c);
    }

    /**
     * 获取键对应的值
     * @param key 键
     * @return 值，如果不存在返回-1
     */
    public int get(int key) {
        Integer ans = cache.get(key);
        return ans == null ? -1 : ans;
    }
    
    /**
     * 设置键值对
     * @param key 键
     * @param val 值
     */
    public void put(int key, int val) {
        cache.set(key, val);
    }

    /**
     * 双向链表节点类
     * 同时存储键和值，方便在删除时从HashMap中移除对应的映射
     */
    private static class Node<K,V> {
        public K key;           // 键
        public V val;           // 值
        public Node<K, V> last; // 前驱节点
        public Node<K, V> next; // 后继节点
        
        public Node(K k, V v) {
            key = k;
            val = v;
        }
    }

    /**
     * 双向链表类，维护节点的使用顺序
     * 头部是最久未使用的节点，尾部是最近使用的节点
     */
    public static class NodeList<K, V> {
        private Node<K, V> head; // 链表头部，指向最久未使用的节点
        private Node<K, V> tail; // 链表尾部，指向最近使用的节点
        
        public NodeList() {
        }

        /**
         * 添加新节点到链表尾部（最近使用位置）
         * @param node 要添加的节点
         */
        public void add(Node<K, V> node) {
            if (node == null) {
                return;
            }
            if (head == null) {
                // 链表为空，新节点既是头部也是尾部
                head = node;
                tail = node;
            } else {
                // 在尾部添加新节点
                tail.next = node;
                node.last = tail;
                tail = node;
            }
        }

        /**
         * 将指定节点移动到链表尾部，表示最近访问
         * @param node 要移动的节点
         */
        public void moveToTail(Node<K, V> node) {
            if (this.tail == node) {
                return; // 已经在尾部，无需移动
            }
            
            if (this.head == node) {
                // 移动头节点
                this.head = node.next;
                this.head.last = null;
            } else {
                // 移动中间节点，更新前后节点的链接
                node.last.next = node.next;
                node.next.last = node.last;
            }
            
            // 将节点移动到尾部
            node.last = this.tail;
            node.next = null;
            this.tail.next = node;
            this.tail = node;
        }

        /**
         * 删除并返回头部节点（最久未使用的节点）
         * @return 被删除的头部节点
         */
        public Node<K, V> removeHead() {
            if (this.head == null) {
                return null;
            }
            
            Node<K, V> res = this.head;
            if (this.head == this.tail) {
                // 只有一个节点
                this.head = null;
                this.tail = null;
            } else {
                // 删除头节点，更新链接
                this.head = res.next;
                res.next = null;
                this.head.last = null;
            }
            return res;
        }
    }

    /**
     * LRU缓存的核心实现类
     * 使用HashMap和双向链表的组合实现O(1)时间复杂度的get和put操作
     */
    public static class Cache<K, V> {
        private HashMap<K, Node<K, V>> keyNodeMap; // 键到节点的映射，实现O(1)查找
        private NodeList<K, V> nodeList;           // 双向链表，维护使用顺序
        private final int capacity;                // 缓存容量

        /**
         * 构造函数
         * @param cap 缓存容量，必须大于0
         */
        public Cache(int cap) {
            if (cap < 1) {
                throw new RuntimeException("容量必须大于0");
            }
            keyNodeMap = new HashMap<>();
            nodeList = new NodeList<>();
            capacity = cap;
        }

        /**
         * 获取指定键的值
         * @param key 键
         * @return 对应的值，如果不存在返回null
         */
        public V get(K key) {
            if (keyNodeMap.containsKey(key)) {
                Node<K, V> res = keyNodeMap.get(key);
                // 访问节点，将其移动到链表尾部表示最近使用
                nodeList.moveToTail(res);
                return res.val;
            }
            return null;
        }

        /**
         * 删除最久未使用的元素（链表头部）
         */
        private void remove() {
            Node<K, V> node = nodeList.removeHead();
            keyNodeMap.remove(node.key);
        }

        /**
         * 设置键值对
         * @param key 键
         * @param val 值
         */
        public void set(K key, V val) {
            if (keyNodeMap.containsKey(key)) {
                // 键已存在，更新值并移动到尾部
                Node<K, V> node = keyNodeMap.get(key);
                node.val = val;
                nodeList.moveToTail(node);
            } else {
                // 新键值对，创建新节点并添加到尾部
                Node<K, V> node = new Node<>(key, val);
                keyNodeMap.put(key, node);
                nodeList.add(node);
                
                // 如果超过容量，删除最久未使用的元素
                if (keyNodeMap.size() == capacity + 1) {
                    remove();
                }
            }
        }
    }
}
