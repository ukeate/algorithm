package basic.deq;

public class Deq {
    public static class Node<V> {
        public V val;
        public Node<V> last;
        public Node<V> next;

        public Node(V val) {
            this.val = val;
        }
    }

    //

    public static class MyDeque<V> {
        private Node<V> head;
        private Node<V> tail;
        private int size;

        public MyDeque() {

        }

        public void pushHead(V val) {
            Node<V> cur = new Node<>(val);
            if (head == null) {
                head = cur;
                tail = cur;
            } else {
                cur.next = head;
                head.last = cur;
                head = cur;
            }
            size++;
        }

        public void pushTail(V val) {
            Node<V> cur = new Node<>(val);
            if (head == null) {
                head = cur;
                tail = cur;
            } else {
                tail.next = cur;
                cur.last = tail;
                tail = cur;
            }
            size++;
        }

        public V pollHead() {
            V ans = null;
            if (head == null) {
                return null;
            }
            size--;
            ans = head.val;
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                head = head.next;
                head.last = null;
            }
            return ans;
        }

        public V pollTail() {
            V ans = null;
            if (head == null) {
                return null;
            }
            size--;
            ans = tail.val;
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                tail = tail.last;
                tail.next = null;
            }
            return ans;
        }
    }
}
