package leetc.top;

import java.util.HashMap;

public class P146_LRUCache {
    private Cache<Integer, Integer> cache;
    public P146_LRUCache(int c) {
        cache= new Cache<>(c);
    }

    public int get(int key) {
        Integer ans = cache.get(key);
        return ans == null ? -1 : ans;
    }
    public void put(int key, int val) {
        cache.set(key, val);
    }

    private static class Node<K,V> {
        public K key;
        public V val;
        public Node<K, V> last;
        public Node<K, V> next;
        public Node(K k, V v) {
            key = k;
            val = v;
        }
    }

    public static class NodeList<K, V> {
        private Node<K, V> head;
        private Node<K, V> tail;
        public NodeList() {
        }

        public void add(Node<K, V> node) {
            if (node == null) {
                return;
            }
            if (head == null) {
                head = node;
                tail = node;
            } else {
                tail.next = node;
                node.last = tail;
                tail = node;
            }
        }

        public void moveToTail(Node<K, V> node) {
            if (this.tail == node) {
                return;
            }
            if (this.head == node) {
                this.head = node.next;
                this.head.last = null;
            } else {
                node.last.next = node.next;
                node.next.last = node.last;
            }
            node.last = this.tail;
            node.next = null;
            this.tail.next = node;
            this.tail = node;
        }

        public Node<K, V> removeHead() {
            if (this.head == null) {
                return null;
            }
            Node<K, V> res = this.head;
            if (this.head == this.tail) {
                this.head = null;
                this.tail = null;
            } else {
                this.head = res.next;
                res.next = null;
                this.head.last = null;
            }
            return res;
        }
    }

    public static class Cache<K, V> {
        private HashMap<K, Node<K, V>> keyNodeMap;
        private NodeList<K, V> nodeList;
        private final int capacity;

        public Cache(int cap) {
            if (cap < 1) {
                throw new RuntimeException("");
            }
            keyNodeMap = new HashMap<>();
            nodeList = new NodeList<>();
            capacity = cap;
        }

        public V get(K key) {
            if (keyNodeMap.containsKey(key)) {
                Node<K, V> res = keyNodeMap.get(key);
                nodeList.moveToTail(res);
                return res.val;
            }
            return null;
        }

        private void remove() {
            Node<K, V> node = nodeList.removeHead();
            keyNodeMap.remove(node.key);
        }

        public void set(K key, V val) {
            if (keyNodeMap.containsKey(key)) {
                Node<K, V> node = keyNodeMap.get(key);
                node.val = val;
                nodeList.moveToTail(node);
            } else {
                Node<K, V> node = new Node<>(key, val);
                keyNodeMap.put(key, node);
                nodeList.add(node);
                if (keyNodeMap.size() == capacity + 1) {
                    remove();
                }
            }
        }
    }
}
