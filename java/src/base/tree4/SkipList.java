package base.tree4;

import java.util.ArrayList;

public class SkipList {
    public static class Node<K extends Comparable<K>, V> {
        public K k;
        public V v;
        public ArrayList<Node<K, V>> nexts;

        public Node(K key, V val) {
            k = key;
            v = val;
            nexts = new ArrayList<>();
        }

        public boolean isKeyLessTo(K otherKey) {
            return otherKey != null && (k == null || k.compareTo(otherKey) < 0);
        }

        public boolean isKeyEqual(K otherKey) {
            return (k == null && otherKey == null)
                    || (k != null && otherKey != null && k.compareTo(otherKey) == 0);
        }
    }

    public static class SkipListMap<K extends Comparable<K>, V> {
        private static final double PROBABILITY = 0.5;
        private Node<K, V> head;
        private int size;
        private int maxLevel;

        public SkipListMap() {
            head = new Node<>(null, null);
            head.nexts.add(null);
            size = 0;
            maxLevel = 0;
        }

        private Node<K, V> mostRightLessInLevel(K key, Node<K, V> cur, int level) {
            Node<K, V> next = cur.nexts.get(level);
            while (next != null && next.isKeyLessTo(key)) {
                cur = next;
                next = cur.nexts.get(level);
            }
            return cur;
        }

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

        public boolean containsKey(K key) {
            if (key == null) {
                return false;
            }
            Node<K, V> less = mostRightLess(key);
            Node<K, V> next = less.nexts.get(0);
            return next != null && next.isKeyEqual(key);
        }

        public void put(K key, V val) {
            if (key == null) {
                return;
            }
            Node<K, V> less = mostRightLess(key);
            Node<K, V> find = less.nexts.get(0);
            if (find != null && find.isKeyEqual(key)) {
                find.v = val;
            } else {
                size++;
                int newNodeLevel = 0;
                while (Math.random() < PROBABILITY) {
                    newNodeLevel++;
                }
                while (newNodeLevel > maxLevel) {
                    head.nexts.add(null);
                    maxLevel++;
                }
                Node<K, V> newNode = new Node<>(key, val);
                for (int i = 0; i <= newNodeLevel; i++) {
                    newNode.nexts.add(null);
                }
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

        public V get(K key) {
            if (key == null) {
                return null;
            }
            Node<K, V> less = mostRightLess(key);
            Node<K, V> next = less.nexts.get(0);
            return next != null && next.isKeyEqual(key) ? next.v : null;
        }

        public void remove(K key) {
            if (containsKey(key)) {
                size--;
                int level = maxLevel;
                Node<K, V> pre = head;
                while (level >= 0) {
                    pre = mostRightLessInLevel(key, pre, level);
                    Node<K, V> next = pre.nexts.get(level);
                    if (next != null && next.isKeyEqual(key)) {
                        pre.nexts.set(level, next.nexts.get(level));
                    }
                    if (level != 0 && pre == head && pre.nexts.get(level) == null) {
                        head.nexts.remove(level);
                        maxLevel--;
                    }
                    level--;
                }
            }
        }

        public K firstKey() {
            return head.nexts.get(0) != null ? head.nexts.get(0).k : null;
        }

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

        public K ceilingKey(K key) {
            if (key == null) {
                return null;
            }
            Node<K, V> less = mostRightLess(key);
            Node<K, V> next = less.nexts.get(0);
            return next != null ? next.k : null;
        }

        public K floorKey(K key) {
            if (key == null) {
                return null;
            }
            Node<K, V> less = mostRightLess(key);
            Node<K, V> next = less.nexts.get(0);
            return next != null && next.isKeyEqual(key) ? next.k : less.k;
        }

        public int size() {
            return size;
        }
    }

    //

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

    public static void main(String[] args) {
        SkipListMap<String, String> l = new SkipListMap<>();
        print(l);
        System.out.println("========");
        l.put("A", "10");
        print(l);
        System.out.println("========");
        l.remove("A");
        print(l);
        System.out.println("========");
        l.put("E", "E");
        l.put("B", "B");
        l.put("A","A");
        l.put("F","F");
        l.put("C","C");
        l.put("D","D");
        print(l);
        System.out.println("========");
        System.out.println(l.containsKey("B"));
        System.out.println(l.containsKey("Z"));
        System.out.println(l.firstKey());
        System.out.println(l.lastKey());
        System.out.println(l.floorKey("D"));
        System.out.println(l.ceilingKey("D"));
        System.out.println("========");
        l.remove("D");
        print(l);
        System.out.println("========");
        System.out.println(l.floorKey("D"));
        System.out.println(l.ceilingKey("D"));
    }

}