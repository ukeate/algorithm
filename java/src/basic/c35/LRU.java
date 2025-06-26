package basic.c35;

import java.util.HashMap;

/**
 * LRU缓存实现（Least Recently Used Cache）
 * 
 * 问题描述：
 * 设计一个LRU（最近最少使用）缓存，支持以下操作：
 * - get(key)：获取键对应的值，如果存在则将该键标记为最近使用
 * - set(key, value)：设置键值对，如果缓存已满则淘汰最久未使用的项
 * 
 * 核心要求：
 * - 所有操作的时间复杂度都必须是O(1)
 * - 需要维护访问顺序，最近访问的在末尾，最久未访问的在开头
 * 
 * 数据结构设计：
 * 1. HashMap：提供O(1)的键值查找
 * 2. 双向链表：维护访问顺序，支持O(1)的插入、删除、移动操作
 * 
 * 算法思想：
 * - 每次访问时，将节点移动到链表尾部
 * - 缓存满时，删除链表头部节点（最久未使用）
 * - HashMap存储key到节点的映射，双向链表维护顺序
 * 
 * 时间复杂度：所有操作均为O(1)
 * 空间复杂度：O(capacity)
 */
public class LRU {
    
    /**
     * 双向链表节点类
     * 既用于链表的连接，也存储键值对数据
     * 
     * @param <K> 键的类型
     * @param <V> 值的类型
     */
    public static class Node<K, V> {
        public K key;           // 节点存储的键
        public V val;           // 节点存储的值
        public Node<K, V> last; // 指向前一个节点（前驱）
        public Node<K, V> next; // 指向后一个节点（后继）
        
        /**
         * 构造函数
         * @param key 键
         * @param val 值
         */
        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    /**
     * 双向链表类
     * 专门用于维护LRU缓存的访问顺序
     * - 头部：最久未使用的节点
     * - 尾部：最近使用的节点
     * 
     * @param <K> 键的类型
     * @param <V> 值的类型
     */
    private static class NodeDoubleLinkedList<K, V> {
        private Node<K, V> head; // 链表头节点（最久未使用）
        private Node<K, V> tail; // 链表尾节点（最近使用）
        
        /**
         * 构造函数：初始化空链表
         */
        public NodeDoubleLinkedList() {
            head = null;
            tail = null;
        }
        
        /**
         * 在链表尾部添加新节点
         * 新节点成为最近使用的节点
         * 
         * @param newNode 要添加的新节点
         */
        public void addNode(Node<K, V> newNode) {
            if (newNode == null) {
                return;
            }
            
            if (head == null) {
                // 链表为空，新节点既是头也是尾
                head = newNode;
                tail = newNode;
            } else {
                // 链表不为空，将新节点添加到尾部
                tail.next = newNode;
                newNode.last = tail;
                tail = newNode;
            }
        }

        /**
         * 将指定节点移动到链表尾部
         * 用于标记该节点为最近使用
         * 
         * @param node 要移动的节点
         */
        public void moveToTail(Node<K, V> node) {
            if (this.tail == node) {
                return; // 已经在尾部，无需移动
            }
            
            if (this.head == node) {
                // 移动的是头节点
                this.head = node.next;
                this.head.last = null;
            } else {
                // 移动的是中间节点，断开与前后节点的连接
                node.last.next = node.next;
                node.next.last = node.last;
            }
            
            // 将节点添加到尾部
            node.last = this.tail;
            node.next = null;
            this.tail.next = node;
            this.tail = node;
        }

        /**
         * 删除并返回链表头部节点
         * 用于淘汰最久未使用的节点
         * 
         * @return 被删除的头节点，如果链表为空则返回null
         */
        public Node<K, V> removeHead() {
            if (this.head == null) {
                return null; // 链表为空
            }
            
            Node<K, V> res = this.head;
            
            if (this.head == this.tail) {
                // 链表只有一个节点
                this.head = null;
                this.tail = null;
            } else {
                // 链表有多个节点，移除头节点
                this.head = res.next;
                res.next = null;        // 清除被删除节点的引用
                this.head.last = null;  // 新头节点的前驱置空
            }
            
            return res;
        }
    }

    /**
     * LRU缓存类
     * 结合HashMap和双向链表实现高效的LRU缓存
     * 
     * @param <K> 键的类型
     * @param <V> 值的类型
     */
    public static class Cache<K, V> {
        private HashMap<K, Node<K, V>> keyNodeMap;     // 键到节点的映射
        private NodeDoubleLinkedList<K, V> nodeList;   // 维护访问顺序的双向链表
        private final int capacity;                    // 缓存容量
        
        /**
         * 构造函数
         * @param cap 缓存容量，必须大于0
         */
        public Cache(int cap) {
            if (cap < 1) {
                throw new RuntimeException("缓存容量必须大于0");
            }
            keyNodeMap = new HashMap<>();
            nodeList = new NodeDoubleLinkedList<>();
            capacity = cap;
        }

        /**
         * 获取指定键的值
         * 如果键存在，将对应节点移动到链表尾部（标记为最近使用）
         * 
         * @param key 要查找的键
         * @return 对应的值，如果键不存在则返回null
         */
        public V get(K key) {
            if (keyNodeMap.containsKey(key)) {
                Node<K, V> res = keyNodeMap.get(key);
                nodeList.moveToTail(res); // 标记为最近使用
                return res.val;
            }
            return null; // 键不存在
        }

        /**
         * 删除最久未使用的键值对
         * 从HashMap和链表中同时删除
         */
        private void removeMostUnused() {
            Node<K, V> node = nodeList.removeHead(); // 获取最久未使用的节点
            keyNodeMap.remove(node.key);             // 从HashMap中删除
        }

        /**
         * 设置键值对
         * 
         * 处理逻辑：
         * 1. 如果键已存在：更新值并移动到尾部
         * 2. 如果键不存在且缓存未满：直接添加到尾部
         * 3. 如果键不存在且缓存已满：先删除最久未使用的，再添加新的
         * 
         * @param key 要设置的键
         * @param val 要设置的值
         */
        public void set(K key, V val) {
            if (keyNodeMap.containsKey(key)) {
                // 键已存在，更新值并移动到尾部
                Node<K, V> node = keyNodeMap.get(key);
                node.val = val;
                nodeList.moveToTail(node);
            } else {
                // 键不存在，需要添加新节点
                if (keyNodeMap.size() == capacity) {
                    // 缓存已满，先删除最久未使用的
                    removeMostUnused();
                }
                
                // 创建新节点并添加到缓存
                Node<K, V> newNode = new Node<>(key, val);
                keyNodeMap.put(key, newNode);
                nodeList.addNode(newNode);
            }
        }
        
        /**
         * 获取当前缓存大小
         * @return 当前存储的键值对数量
         */
        public int size() {
            return keyNodeMap.size();
        }
        
        /**
         * 检查缓存是否为空
         * @return 如果缓存为空返回true，否则返回false
         */
        public boolean isEmpty() {
            return keyNodeMap.isEmpty();
        }
        
        /**
         * 检查缓存是否已满
         * @return 如果缓存已满返回true，否则返回false
         */
        public boolean isFull() {
            return keyNodeMap.size() == capacity;
        }
    }

    /**
     * 测试方法：验证LRU缓存的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== LRU缓存测试 ===");
        
        // 创建容量为3的LRU缓存
        Cache<String, Integer> cache = new Cache<>(3);
        
        System.out.println("创建容量为3的LRU缓存");
        System.out.println("当前缓存大小: " + cache.size());
        System.out.println();
        
        // 测试基本的set和get操作
        System.out.println("1. 设置键值对:");
        cache.set("A", 1);
        System.out.println("设置 A=1, 缓存大小: " + cache.size());
        
        cache.set("B", 2);
        System.out.println("设置 B=2, 缓存大小: " + cache.size());
        
        cache.set("C", 3);
        System.out.println("设置 C=3, 缓存大小: " + cache.size());
        System.out.println("缓存已满: " + cache.isFull());
        System.out.println();
        
        // 测试get操作
        System.out.println("2. 获取值:");
        System.out.println("获取 B: " + cache.get("B")); // B变成最近使用
        System.out.println("获取 A: " + cache.get("A")); // A变成最近使用
        System.out.println("获取不存在的键 X: " + cache.get("X"));
        System.out.println();
        
        // 测试LRU淘汰机制
        System.out.println("3. 测试LRU淘汰机制:");
        System.out.println("当前访问顺序(从久到新): C -> B -> A");
        cache.set("D", 4); // 应该淘汰C
        System.out.println("设置 D=4, 应该淘汰最久未使用的 C");
        
        System.out.println("获取 D: " + cache.get("D"));
        System.out.println("获取 C: " + cache.get("C")); // 应该返回null
        System.out.println("获取 B: " + cache.get("B"));
        System.out.println("获取 A: " + cache.get("A"));
        System.out.println();
        
        // 测试更新已存在键的值
        System.out.println("4. 测试更新已存在键的值:");
        cache.set("B", 20); // 更新B的值
        System.out.println("更新 B=20");
        System.out.println("获取 B: " + cache.get("B"));
        System.out.println();
        
        // 测试继续添加新值
        System.out.println("5. 继续测试LRU淘汰:");
        cache.set("E", 5); // 应该淘汰A（最久未使用）
        System.out.println("设置 E=5, 应该淘汰最久未使用的 A");
        System.out.println("获取 A: " + cache.get("A")); // 应该返回null
        System.out.println("获取 D: " + cache.get("D"));
        System.out.println("获取 B: " + cache.get("B"));
        System.out.println("获取 E: " + cache.get("E"));
        
        System.out.println("\n=== 测试完成 ===");
    }
}
