package base.tree4;

public class AVL {
    public static class Node<K extends Comparable<K>, V> {
        public K k;
        public V v;
        public Node<K, V> l;
        public Node<K, V> r;
        public int h;

        public Node(K key, V val) {
            k = key;
            v = val;
            h = 1;
        }
    }

    public static class AVLTreeMap<K extends Comparable<K>, V> {
        private Node<K, V> root;
        private int size;

        public AVLTreeMap() {
        }

        private Node<K, V> rightRotate(Node<K, V> cur) {
            Node<K, V> left = cur.l;
            cur.l = left.r;
            left.r = cur;
            cur.h = Math.max((cur.l != null ? cur.l.h : 0), (cur.r != null ? cur.r.h : 0)) + 1;
            left.h = Math.max((left.l != null ? left.l.h : 0), (left.r != null ? left.r.h : 0)) + 1;
            return left;
        }

        private Node<K, V> leftRotate(Node<K, V> cur) {
            Node<K, V> right = cur.r;
            cur.r = right.l;
            right.l = cur;
            cur.h = Math.max((cur.l != null ? cur.l.h : 0), (cur.r != null ? cur.r.h : 0)) + 1;
            right.h = Math.max((right.l != null ? right.l.h : 0), (right.r != null ? right.r.h : 0)) + 1;
            return right;
        }

        private Node<K, V> maintain(Node<K, V> cur) {
            if (cur == null) {
                return null;
            }
            int lh = cur.l != null ? cur.l.h : 0;
            int rh = cur.r != null ? cur.r.h : 0;
            if (Math.abs(lh - rh) > 1) {
                if (lh > rh) {
                    int llh = cur.l.l != null ? cur.l.l.h : 0;
                    int lrh = cur.l.r != null ? cur.l.r.h : 0;
                    if (llh >= lrh) {
                        cur = rightRotate(cur);
                    } else {
                        cur.l = leftRotate(cur.l);
                        cur = rightRotate(cur);
                    }
                } else {
                    int rlh = cur.r.l != null ? cur.r.l.h : 0;
                    int rrh = cur.r.r != null ? cur.r.r.h : 0;
                    if (rrh >= rlh) {
                        cur = leftRotate(cur);
                    } else {
                        cur.r = rightRotate(cur.r);
                        cur = leftRotate(cur);
                    }
                }
            }
            return cur;
        }

        private Node<K, V> findLast(K key) {
            Node<K, V> pre = root, cur = root;
            while (cur != null) {
                pre = cur;
                if (key.compareTo(cur.k) == 0) {
                    break;
                } else if (key.compareTo(cur.k) < 0) {
                    cur = cur.l;
                } else {
                    cur = cur.r;
                }
            }
            return pre;
        }

        private Node<K, V> findLastSmallEqual(K key) {
            Node<K, V> ans = null;
            Node<K, V> cur = root;
            while (cur != null) {
                if (key.compareTo(cur.k) == 0) {
                    ans = cur;
                    break;
                } else if (key.compareTo(cur.k) < 0) {
                    cur = cur.l;
                } else {
                    ans = cur;
                    cur = cur.r;
                }
            }
            return ans;
        }

        private Node<K, V> findLastBigEqual(K key) {
            Node<K, V> ans = null;
            Node<K, V> cur = root;
            while (cur != null) {
                if (key.compareTo(cur.k) == 0) {
                    ans = cur;
                    break;
                } else if (key.compareTo(cur.k) < 0) {
                    ans = cur;
                    cur = cur.l;
                } else {
                    cur = cur.r;
                }
            }
            return ans;
        }

        public boolean containsKey(K key) {
            if (key == null) {
                return false;
            }
            Node<K, V> n = findLast(key);
            return n != null && key.compareTo(n.k) == 0 ? true : false;
        }

        private Node<K, V> add(Node<K, V> cur, K key, V val) {
            if (cur == null) {
                return new Node<>(key, val);
            } else {
                if (key.compareTo(cur.k) < 0) {
                    cur.l = add(cur.l, key, val);
                } else {
                    cur.r = add(cur.r, key, val);
                }
                cur.h = Math.max(cur.l != null ? cur.l.h : 0, cur.r != null ? cur.r.h : 0) + 1;
                return maintain(cur);
            }
        }

        private Node<K, V> delete(Node<K, V> cur, K key) {
            if (key.compareTo(cur.k) > 0) {
                cur.r = delete(cur.r, key);
            } else if (key.compareTo(cur.k) < 0) {
                cur.l = delete(cur.l, key);
            } else {
                if (cur.l == null && cur.r == null) {
                    cur = null;
                } else if (cur.l == null && cur.r != null) {
                    cur = cur.r;
                } else if (cur.l != null && cur.r == null) {
                    cur = cur.l;
                } else {
                    Node<K, V> des = cur.r;
                    while (des.l != null) {
                        des = des.l;
                    }
                    cur.r = delete(cur.r, des.k);
                    des.l = cur.l;
                    des.r = cur.r;
                    cur = des;
                }
            }
            if (cur != null) {
                cur.h = Math.max(cur.l != null ? cur.l.h : 0, cur.r != null ? cur.r.h : 0) + 1;
            }
            return maintain(cur);
        }

        public void put(K key, V val) {
            if (key == null) {
                return;
            }
            Node<K, V> lastNode = findLast(key);
            if (lastNode != null && key.compareTo(lastNode.k) == 0) {
                lastNode.v = val;
            } else {
                size++;
                root = add(root, key, val);
            }
        }

        public void remove(K key) {
            if (key == null) {
                return;
            }
            if (containsKey(key)) {
                size--;
                root = delete(root, key);
            }
        }

        public V get(K key) {
            if (key == null) {
                return null;
            }
            Node<K, V> n = findLast(key);
            if (n != null && key.compareTo(n.k) == 0) {
                return n.v;
            }
            return null;
        }

        public K firstKey() {
            if (root == null) {
                return null;
            }
            Node<K, V> cur = root;
            while (cur.l != null) {
                cur = cur.l;
            }
            return cur.k;
        }

        public K lastKey() {
            if (root == null) {
                return null;
            }
            Node<K, V> cur = root;
            while (cur.r != null) {
                cur = cur.r;
            }
            return cur.k;
        }

        public K floorKey(K key) {
            if (key == null) {
                return null;
            }
            Node<K, V> n = findLastSmallEqual(key);
            return n == null ? null : n.k;
        }

        public K ceilingKey(K key) {
            if (key == null) {
                return null;
            }
            Node<K, V> n = findLastBigEqual(key);
            return n == null ? null : n.k;
        }

        public int size() {
            return size;
        }
    }
}
