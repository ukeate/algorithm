package base.tree4;

import java.util.ArrayList;

/**
 * 跳表（Skip List）实现
 * 
 * 跳表是一种概率性数据结构，由William Pugh发明。
 * 它在有序链表的基础上增加了多级索引，通过随机化的方式建立索引层次。
 * 
 * 跳表的核心思想：
 * 1. 最底层（Level 0）是一个有序链表，包含所有元素
 * 2. 上层级别包含下层元素的子集，作为"快速通道"
 * 3. 每个节点随机决定它出现在多少个层级中
 * 4. 查找时从最高层开始，逐层下降，类似于二分查找
 * 
 * 跳表的优点：
 * 1. 平均时间复杂度为O(log n)
 * 2. 实现相对简单，无需复杂的旋转操作
 * 3. 支持范围查询
 * 4. 内存使用相对较少
 * 5. 可以支持并发操作（本实现为单线程版本）
 */
public class SkipList {
    
    /**
     * 跳表的节点类
     * 
     * 每个节点维护一个nexts数组，表示该节点在各个层级的下一个节点
     * 
     * @param <K> 键的类型，必须实现Comparable接口
     * @param <V> 值的类型
     */
    public static class Node<K extends Comparable<K>, V> {
        public K k;                          // 键
        public V v;                          // 值
        public ArrayList<Node<K, V>> nexts;  // 各层级的下一个节点数组

        public Node(K key, V val) {
            k = key;
            v = val;
            nexts = new ArrayList<>();
        }

        /**
         * 判断当前节点的键是否小于指定键
         * 
         * @param otherKey 要比较的键
         * @return 如果当前键小于otherKey返回true，否则返回false
         */
        public boolean isKeyLessTo(K otherKey) {
            return otherKey != null && (k == null || k.compareTo(otherKey) < 0);
        }

        /**
         * 判断当前节点的键是否等于指定键
         * 
         * @param otherKey 要比较的键
         * @return 如果键相等返回true，否则返回false
         */
        public boolean isKeyEqual(K otherKey) {
            return (k == null && otherKey == null)
                    || (k != null && otherKey != null && k.compareTo(otherKey) == 0);
        }
    }

    /**
     * 跳表实现的有序映射表
     * 
     * 支持的操作：
     * 1. put/get/remove: 基本的增删改查
     * 2. containsKey: 判断键是否存在
     * 3. firstKey/lastKey: 获取最小/最大键
     * 4. floorKey/ceilingKey: 获取小于等于/大于等于指定键的最接近键
     * 5. size: 获取元素个数
     * 
     * @param <K> 键的类型，必须实现Comparable接口
     * @param <V> 值的类型
     */
    public static class SkipListMap<K extends Comparable<K>, V> {
        private static final double PROBABILITY = 0.5;  // 节点出现在上一层的概率
        private Node<K, V> head;   // 头节点（哨兵节点）
        private int size;          // 元素个数
        private int maxLevel;      // 当前最大层级

        /**
         * 构造函数：初始化跳表
         */
        public SkipListMap() {
            head = new Node<>(null, null);  // 头节点的键为null
            head.nexts.add(null);           // 初始只有第0层
            size = 0;
            maxLevel = 0;
        }

        /**
         * 在指定层级中找到小于key的最右节点
         * 
         * @param key 要查找的键
         * @param cur 当前节点
         * @param level 指定的层级
         * @return 小于key的最右节点
         */
        private Node<K, V> mostRightLessInLevel(K key, Node<K, V> cur, int level) {
            Node<K, V> next = cur.nexts.get(level);
            // 在当前层级向右移动，直到找到大于等于key的节点
            while (next != null && next.isKeyLessTo(key)) {
                cur = next;
                next = cur.nexts.get(level);
            }
            return cur;
        }

        /**
         * 找到小于key的最右节点
         * 
         * 核心查找算法：从最高层开始，逐层向下查找
         * 在每一层找到小于key的最右节点，然后下降到下一层
         * 
         * @param key 要查找的键
         * @return 小于key的最右节点
         */
        private Node<K, V> mostRightLess(K key) {
            if (key == null) {
                return null;
            }
            int level = maxLevel;
            Node<K, V> cur = head;
            while (level >= 0) {
                cur = mostRightLessInLevel(key, cur, level--);
            }
            return cur;
        }

        /**
         * 判断是否包含指定的键
         * 
         * @param key 要查找的键
         * @return 如果包含该键返回true，否则返回false
         */
        public boolean containsKey(K key) {
            if (key == null) {
                return false;
            }
            Node<K, V> less = mostRightLess(key);
            Node<K, V> next = less.nexts.get(0);
            return next != null && next.isKeyEqual(key);
        }

        /**
         * 插入或更新键值对
         * 
         * 跳表插入算法：
         * 1. 找到每一层中key的前驱节点
         * 2. 随机决定新节点的层数
         * 3. 在相应的层级中插入新节点
         * 
         * @param key 键
         * @param val 值
         */
        public void put(K key, V val) {
            if (key == null) {
                return;
            }
            Node<K, V> less = mostRightLess(key);
            Node<K, V> find = less.nexts.get(0);
            if (find != null && find.isKeyEqual(key)) {
                find.v = val;  // 键已存在，更新值
            } else {
                size++;
                // 随机决定新节点的层数
                int newNodeLevel = 0;
                while (Math.random() < PROBABILITY) {
                    newNodeLevel++;
                }
                // 如果新节点层数超过当前最大层数，扩展跳表
                while (newNodeLevel > maxLevel) {
                    head.nexts.add(null);
                    maxLevel++;
                }
                // 创建新节点并初始化各层的next指针
                Node<K, V> newNode = new Node<>(key, val);
                for (int i = 0; i <= newNodeLevel; i++) {
                    newNode.nexts.add(null);
                }
                // 从上到下，在每一层插入新节点
                int level = maxLevel;
                Node<K, V> pre = head;
                while (level >= 0) {
                    pre = mostRightLessInLevel(key, pre, level);
                    if (level <= newNodeLevel) {
                        newNode.nexts.set(level, pre.nexts.get(level));
                        pre.nexts.set(level, newNode);
                    }
                    level--;
                }
            }
        }

        /**
         * 获取指定键对应的值
         * 
         * @param key 键
         * @return 对应的值，如果键不存在则返回null
         */
        public V get(K key) {
            if (key == null) {
                return null;
            }
            Node<K, V> less = mostRightLess(key);
            Node<K, V> next = less.nexts.get(0);
            return next != null && next.isKeyEqual(key) ? next.v : null;
        }

        /**
         * 删除指定键的键值对
         * 
         * 跳表删除算法：
         * 1. 从每一层中删除目标节点
         * 2. 清理空的层级
         * 
         * @param key 要删除的键
         */
        public void remove(K key) {
            if (containsKey(key)) {
                size--;
                int level = maxLevel;
                Node<K, V> pre = head;
                while (level >= 0) {
                    pre = mostRightLessInLevel(key, pre, level);
                    Node<K, V> next = pre.nexts.get(level);
                    // 如果下一个节点就是要删除的节点，修改指针
                    if (next != null && next.isKeyEqual(key)) {
                        pre.nexts.set(level, next.nexts.get(level));
                    }
                    // 清理空的层级（除了第0层）
                    if (level != 0 && pre == head && pre.nexts.get(level) == null) {
                        head.nexts.remove(level);
                        maxLevel--;
                    }
                    level--;
                }
            }
        }

        /**
         * 获取最小的键
         * 
         * @return 最小的键，如果跳表为空则返回null
         */
        public K firstKey() {
            return head.nexts.get(0) != null ? head.nexts.get(0).k : null;
        }

        /**
         * 获取最大的键
         * 
         * 算法：从最高层开始，在每一层都走到最右端
         * 
         * @return 最大的键，如果跳表为空则返回null
         */
        public K lastKey() {
            int level = maxLevel;
            Node<K, V> cur = head;
            while (level >= 0) {
                Node<K, V> next = cur.nexts.get(level);
                while (next != null) {
                    cur = next;
                    next = cur.nexts.get(level);
                }
                level--;
            }
            return cur.k;
        }

        /**
         * 获取大于等于指定键的最小键（向上取整）
         * 
         * @param key 指定的键
         * @return 大于等于key的最小键，如果不存在则返回null
         */
        public K ceilingKey(K key) {
            if (key == null) {
                return null;
            }
            Node<K, V> less = mostRightLess(key);
            Node<K, V> next = less.nexts.get(0);
            return next != null ? next.k : null;
        }

        /**
         * 获取小于等于指定键的最大键（向下取整）
         * 
         * @param key 指定的键
         * @return 小于等于key的最大键，如果不存在则返回null
         */
        public K floorKey(K key) {
            if (key == null) {
                return null;
            }
            Node<K, V> less = mostRightLess(key);
            Node<K, V> next = less.nexts.get(0);
            return next != null && next.isKeyEqual(key) ? next.k : less.k;
        }

        /**
         * 获取跳表中元素的个数
         * 
         * @return 元素个数
         */
        public int size() {
            return size;
        }
    }

    // 以下是打印和测试相关的辅助方法

    /**
     * 打印跳表的结构（用于调试）
     * 
     * @param l 要打印的跳表
     */
    public static void print(SkipListMap<String, String> l) {
        for (int i = l.maxLevel; i >= 0; i--) {
            System.out.println("Level " + i + " : ");
            Node<String, String> cur = l.head;
            while (cur.nexts.get(i) != null) {
                Node<String, String> next = cur.nexts.get(i);
                System.out.println("(" + next.k + " , " + next.v + ")");
                cur = next;
            }
            System.out.println();
        }
    }

    /**
     * 主方法：测试跳表的各种功能
     */
    public static void main(String[] args) {
        SkipListMap<String, String> l = new SkipListMap<>();
        
        // 测试空跳表
        print(l);
        System.out.println("========");
        
        // 测试插入单个元素
        l.put("A", "10");
        print(l);
        System.out.println("========");
        
        // 测试删除元素
        l.remove("A");
        print(l);
        System.out.println("========");
        
        // 测试插入多个元素
        l.put("E", "E");
        l.put("B", "B");
        l.put("A","A");
        l.put("F","F");
        l.put("C","C");
        l.put("D","D");
        print(l);
        System.out.println("========");
        
        // 测试查找功能
        System.out.println(l.containsKey("B"));  // 应该输出true
        System.out.println(l.containsKey("Z"));  // 应该输出false
        System.out.println(l.firstKey());        // 最小键
        System.out.println(l.lastKey());         // 最大键
        System.out.println(l.floorKey("D"));     // <= D 的最大键
        System.out.println(l.ceilingKey("D"));   // >= D 的最小键
        System.out.println("========");
        
        // 测试删除后的边界查找
        l.remove("D");
        print(l);
        System.out.println("========");
        System.out.println(l.floorKey("D"));     // <= D 的最大键（D已被删除）
        System.out.println(l.ceilingKey("D"));   // >= D 的最小键（D已被删除）
    }

}