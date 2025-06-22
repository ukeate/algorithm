package base.que;

/**
 * 简单队列实现 - 基于链表的队列数据结构
 * 
 * 队列特点：
 * 1. FIFO (First In First Out) - 先进先出
 * 2. 只能在队尾添加元素，在队头删除元素
 * 3. 主要操作：offer(入队)、poll(出队)、size(获取大小)
 * 
 * 实现方式：
 * 使用单向链表实现，维护头尾指针
 * - head指向队头（用于出队）
 * - tail指向队尾（用于入队）
 * - size记录队列元素个数
 * 
 * 时间复杂度：
 * - offer: O(1) - 在尾部添加
 * - poll: O(1) - 在头部删除
 * - size: O(1) - 直接返回计数器
 * 
 * 空间复杂度：O(n) - n为队列中元素个数
 */
public class SimpleQueue {
    /**
     * 链表节点类 - 队列的基本存储单元
     * 
     * @param <V> 节点存储的数据类型
     */
    public static class Node<V> {
        public V val;           // 节点存储的值
        public Node<V> next;    // 指向下一个节点的指针

        /**
         * 节点构造函数
         * 
         * @param v 节点要存储的值
         */
        public Node(V v) {
            this.val = v;
        }
    }

    /**
     * 基于链表的队列实现
     * 
     * @param <V> 队列存储的数据类型
     */
    public static class MyQueue<V> {
        private Node<V> head;    // 队头指针，指向第一个元素
        private Node<V> tail;    // 队尾指针，指向最后一个元素
        private int size;        // 队列中元素的个数

        /**
         * 队列构造函数
         * 
         * 初始化时队列为空：
         * - head和tail都为null
         * - size为0
         */
        public MyQueue() {
        }

        /**
         * 入队操作 - 在队尾添加元素
         * 
         * 算法步骤：
         * 1. 创建新节点存储要添加的值
         * 2. 如果队列为空，新节点既是头也是尾
         * 3. 如果队列不为空，将新节点连接到当前尾节点后面，并更新尾指针
         * 4. 队列大小加1
         * 
         * @param val 要入队的元素
         */
        public void offer(V val) {
            Node<V> cur = new Node<V>(val);
            if (tail == null) {
                // 队列为空，新节点既是头也是尾
                head = cur;
                tail = cur;
            } else {
                // 队列不为空，在尾部添加新节点
                tail.next = cur;
                tail = cur;
            }
            size++;
        }

        /**
         * 出队操作 - 从队头删除并返回元素
         * 
         * 算法步骤：
         * 1. 如果队列为空，返回null
         * 2. 保存队头元素的值
         * 3. 将head指针移动到下一个节点
         * 4. 队列大小减1
         * 5. 如果队列变为空，将tail也设为null
         * 6. 返回保存的值
         * 
         * @return 队头元素，如果队列为空则返回null
         */
        public V poll() {
            V ans = null;
            if (head != null) {
                ans = head.val;     // 保存要返回的值
                head = head.next;   // 头指针后移
                size--;
            }
            // 如果队列变为空，尾指针也要置空
            if (head == null) {
                tail = null;
            }
            return ans;
        }

        /**
         * 获取队列大小
         * 
         * @return 队列中元素的个数
         */
        public int size() {
            return size;
        }

        /**
         * 判断队列是否为空
         * 
         * @return 队列为空返回true，否则返回false
         */
        public boolean isEmpty() {
            return size == 0;
        }
    }

    /**
     * 测试方法 - 演示队列的基本操作
     */
    public static void main(String[] args) {
        MyQueue<Integer> queue = new MyQueue<>();
        
        System.out.println("=== 队列基本操作演示 ===");
        
        // 测试入队操作
        System.out.println("入队操作：");
        for (int i = 1; i <= 5; i++) {
            queue.offer(i);
            System.out.println("入队: " + i + "，队列大小: " + queue.size());
        }
        
        // 测试出队操作
        System.out.println("\n出队操作：");
        while (!queue.isEmpty()) {
            int val = queue.poll();
            System.out.println("出队: " + val + "，队列大小: " + queue.size());
        }
        
        // 测试空队列
        System.out.println("\n空队列测试：");
        System.out.println("队列是否为空: " + queue.isEmpty());
        System.out.println("从空队列出队: " + queue.poll());
    }
}
