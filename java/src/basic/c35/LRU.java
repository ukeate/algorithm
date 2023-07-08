package basic.c35;

import java.util.HashMap;

// Least Recently Used Cache
public class LRU {
    public static class Node<K, V> {
        public K key;
        public V val;
        public Node<K, V> last;
        public Node<K, V> next;
        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    private static class NodeDoubleLinkedList<K, V> {
        private Node<K, V> head;
        private Node<K, V> tail;
        public NodeDoubleLinkedList() {
            head = null;
            tail = null;
        }
        public void addNode(Node<K, V> newNode) {
            if (newNode == null) {
                return;
            }
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                newNode.last = tail;
                tail = newNode;
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
        private NodeDoubleLinkedList<K, V> nodeList;
        private final int capacity;
        public Cache(int cap) {
            if (cap < 1) {
                throw new RuntimeException("");
            }
            keyNodeMap = new HashMap<>();
            nodeList = new NodeDoubleLinkedList<>();
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

        private void removeMostUnused() {
            Node<K, V> node = nodeList.removeHead();
            keyNodeMap.remove(node.key);
        }

        public void set(K key, V val) {
            if (keyNodeMap.containsKey(key)) {
                Node<K, V> node = keyNodeMap.get(key);
                node.val = val;
                nodeList.moveToTail(node);
            } else {
                if (keyNodeMap.size() == capacity) {
                    removeMostUnused();
                }
                Node<K, V> newNode = new Node<>(key, val);
                keyNodeMap.put(key, newNode);
                nodeList.addNode(newNode);
            }
        }
    }

    public static void main(String[] args) {
        Cache<String, Integer> cache = new Cache<>(3);
        cache.set("A", 1);
        cache.set("B", 2);
        cache.set("C", 3);
        System.out.println(cache.get("B"));
        System.out.println(cache.get("A"));
        cache.set("D", 4);
        System.out.println(cache.get("D"));
        System.out.println(cache.get("C"));
    }
}
