package base.tree4;

public class SB {
    public static class Node<K extends Comparable<K>, V> {
        public K k;
        public V v;
        public Node<K, V> l;
        public Node<K, V> r;
        public int size;
        public Node(K key, V val) {
            k = key;
            v = val;
            size = 1;
        }
    }

    public static class SizeBalancedTreeMap<K extends Comparable<K>, V> {
        private Node<K, V> root;
        private Node<K, V> rightRotate(Node<K, V> cur) {
            Node<K, V> l = cur.l;
            cur.l = l.r;
            l.r = cur;
            l.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return l;
        }

        private Node<K, V> leftRotate(Node<K, V> cur) {
            Node<K, V> r = cur.r;
            cur.r = r.l;
            r.l = cur;
            r.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return r;
        }

        private Node<K, V> maintain(Node<K, V> cur) {
            if (cur == null) {
                return null;
            }
            int ls = cur.l != null ? cur.l.size : 0;
            int lls = cur.l != null && cur.l.l != null ? cur.l.l.size: 0;
            int lrs = cur.l != null && cur.l.r != null ? cur.l.r.size : 0;
            int rs = cur.r != null ? cur.r.size : 0;
            int rls = cur.r != null && cur.r.l != null ? cur.r.l.size : 0;
            int rrs = cur.r != null && cur.r.r != null ? cur.r.r.size : 0;
            if (lls > rs) {
                cur = rightRotate(cur);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (lrs > rs) {
                cur.l = leftRotate(cur.l);
                cur = rightRotate(cur);
                cur.l = maintain(cur.l);
                cur = maintain(cur);
            } else if (rrs > ls) {
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur = maintain(cur);
            } else if (rls > ls) {
                cur.r = rightRotate(cur.r);
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            }
            return cur;
        }

        private Node<K, V> findLast(K key) {
            Node<K, V> pre = null;
            Node<K, V> cur = root;
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

        private Node<K, V> add(Node<K, V> cur, K key, V val) {
            if (cur == null) {
                return new Node<>(key, val);
            } else {
                cur.size++;
                if (key.compareTo(cur.k) < 0) {
                    cur.l = add(cur.l, key, val);
                } else {
                    cur.r = add(cur.r, key, val);
                }
                return maintain(cur);
            }
        }

        private Node<K, V> delete(Node<K, V> cur, K key) {
            cur.size--;
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
                    Node<K, V> pre = null;
                    Node<K, V> des = cur.r;
                    des.size--;
                    while (des.l != null) {
                        pre = des;
                        des = des.l;
                        des.size--;
                    }
                    if (pre != null) {
                        pre.l = des.r;
                        des.r = cur.r;
                    }
                    des.l = cur.l;
                    des.size = des.l.size + (des.r == null ? 0 : des.r.size) + 1;
                    cur = des;
                }
            }
            return cur;
        }

        private Node<K, V> getIndex(Node<K, V> cur, int kth) {
            int ls = cur.l != null ? cur.l.size : 0;
            if (kth == ls + 1) {
                return cur;
            } else if (kth <= ls) {
                return getIndex(cur.l, kth);
            } else {
                return getIndex(cur.r, kth - ls - 1);
            }
        }

        public boolean containsKey(K key) {
            if (key == null) {
                throw new RuntimeException("invalid parameter");
            }
            Node<K, V> n = findLast(key);
            return n != null && key.compareTo(n.k) == 0 ? true : false;
        }

        public void put(K key, V val) {
            if (key == null) {
                throw new RuntimeException("invalid parameter");
            }
            Node<K, V> n = findLast(key);
            if (n != null && key.compareTo(n.k) == 0) {
                n.v = val;
            } else {
                root = add(root, key, val);
            }
        }

        public void remove(K key) {
            if (key == null) {
                throw new RuntimeException("invalid parameter");
            }
            if (containsKey(key)) {
                root = delete(root, key);
            }
        }

        public K getIndexKey(int idx) {
            if (idx < 0 || idx >= this.size()) {
                throw new RuntimeException("invalid parameter");
            }
            return getIndex(root, idx + 1).k;
        }

        public V getIndexValue(int idx) {
            if (idx < 0 || idx >= this.size()) {
                throw new RuntimeException("invalid parameter");
            }
            return getIndex(root, idx + 1).v;
        }

        public V get(K key) {
            if (key == null) {
                throw new RuntimeException("invalid parameter");
            }
            Node<K, V> n = findLast(key);
            if (n != null && key.compareTo(n.k) == 0) {
                return n.v;
            } else {
                return null;
            }
        }

        public K firstKey() {
            if (root == null) {
                return  null;
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
                throw new RuntimeException("invalid parameter");
            }
            Node<K, V> n = findLastSmallEqual(key);
            return n == null ? null : n.k;
        }

        public K ceilingKey(K key) {
            if (key == null) {
                throw new RuntimeException("invalid parameter");
            }
            Node<K, V> n = findLastBigEqual(key);
            return n == null ? null : n.k;
        }

        public int size() {
            return root == null ? 0 : root.size;
        }
    }

    //

    private static String getSpace(int num) {
        String space = " ";
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < num; i++) {
            buf.append(space);
        }
        return buf.toString();
    }
    private static void printIn(Node<String, Integer> head, int height, String to, int len) {
        if (head == null) {
            return;
        }
        printIn(head.r, height + 1, "v", len);
        String val = to + "(" + head.k + "," + head.v + ")" + to;
        int lenM = val.length();
        int lenL = (len - lenM) / 2;
        int lenR = len - lenM - lenL;
        val = getSpace(lenL) + val + getSpace(lenR);
        System.out.println(getSpace(height * len) + val);
        printIn(head.l, height + 1, "^", len);
    }
    private static void print(Node<String, Integer> head) {
        printIn(head, 0, "H", 17);
    }

    public static void main(String[] args) {
        SizeBalancedTreeMap<String, Integer> sbt = new SizeBalancedTreeMap<>();
        sbt.put("d", 4);
        sbt.put("c", 3);
        sbt.put("a", 1);
        sbt.put("b", 2);
        sbt.put("g", 7);
        sbt.put("f", 6);
        sbt.put("h", 8);
        sbt.put("i", 9);
        sbt.put("a", 111);
        System.out.println(sbt.get("a"));
        sbt.put("a", 1);
        System.out.println(sbt.get("a"));
        for (int i = 0; i < sbt.size(); i++) {
            System.out.println(sbt.getIndexKey(i) + "," + sbt.getIndexValue(i));
        }
        print(sbt.root);
        System.out.println(sbt.firstKey());
        System.out.println(sbt.lastKey());
        System.out.println(sbt.floorKey("g"));
        System.out.println(sbt.ceilingKey("g"));
        System.out.println(sbt.floorKey("e"));
        System.out.println(sbt.ceilingKey("e"));
        System.out.println(sbt.floorKey(""));
        System.out.println(sbt.ceilingKey(""));
        System.out.println(sbt.floorKey("j"));
        System.out.println(sbt.ceilingKey("j"));
        sbt.remove("d");
        print(sbt.root);
        sbt.remove("f");
        print(sbt.root);
    }
}
