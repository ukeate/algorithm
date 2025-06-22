package base.deq;

/**
 * 双端队列(Deque)实现
 * 使用双向链表实现，支持在头部和尾部进行O(1)时间的插入和删除操作
 */
public class Deq {
    /**
     * 双向链表节点
     * @param <V> 节点存储的数据类型
     */
    public static class Node<V> {
        public V val;           // 节点值
        public Node<V> last;    // 前驱节点
        public Node<V> next;    // 后继节点

        public Node(V val) {
            this.val = val;
        }
    }

    /**
     * 双端队列实现类
     * 支持头部和尾部的插入、删除操作
     * @param <V> 队列元素类型
     */
    public static class MyDeque<V> {
        private Node<V> head;   // 头节点
        private Node<V> tail;   // 尾节点
        private int size;       // 队列大小

        public MyDeque() {
            // 初始化空队列
        }

        /**
         * 在队列头部插入元素
         * 时间复杂度：O(1)
         * @param val 要插入的值
         */
        public void pushHead(V val) {
            Node<V> cur = new Node<>(val);
            if (head == null) {
                // 队列为空，新节点既是头也是尾
                head = cur;
                tail = cur;
            } else {
                // 在头部插入新节点
                cur.next = head;
                head.last = cur;
                head = cur;
            }
            size++;
        }

        /**
         * 在队列尾部插入元素
         * 时间复杂度：O(1)
         * @param val 要插入的值
         */
        public void pushTail(V val) {
            Node<V> cur = new Node<>(val);
            if (head == null) {
                // 队列为空，新节点既是头也是尾
                head = cur;
                tail = cur;
            } else {
                // 在尾部插入新节点
                tail.next = cur;
                cur.last = tail;
                tail = cur;
            }
            size++;
        }

        /**
         * 从队列头部删除元素
         * 时间复杂度：O(1)
         * @return 被删除的元素值，如果队列为空返回null
         */
        public V pollHead() {
            V ans = null;
            if (head == null) {
                return null;  // 队列为空
            }
            size--;
            ans = head.val;
            if (head == tail) {
                // 只有一个元素，删除后队列为空
                head = null;
                tail = null;
            } else {
                // 删除头节点，更新头指针
                head = head.next;
                head.last = null;
            }
            return ans;
        }

        /**
         * 从队列尾部删除元素
         * 时间复杂度：O(1)
         * @return 被删除的元素值，如果队列为空返回null
         */
        public V pollTail() {
            V ans = null;
            if (head == null) {
                return null;  // 队列为空
            }
            size--;
            ans = tail.val;
            if (head == tail) {
                // 只有一个元素，删除后队列为空
                head = null;
                tail = null;
            } else {
                // 删除尾节点，更新尾指针
                tail = tail.last;
                tail.next = null;
            }
            return ans;
        }

        /**
         * 获取队列大小
         * @return 队列中元素的数量
         */
        public int size() {
            return size;
        }

        /**
         * 判断队列是否为空
         * @return 如果队列为空返回true，否则返回false
         */
        public boolean isEmpty() {
            return size == 0;
        }
    }
}
