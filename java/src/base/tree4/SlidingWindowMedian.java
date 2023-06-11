package base.tree4;

public class SlidingWindowMedian {
    private static class Node<K extends Comparable<K>> {
        public K k;
        public Node<K> l;
        public Node<K> r;
        public int size;

        public Node(K key) {
            k = key;
            size = 1;
        }
    }

    private static class SBTMap<K extends Comparable<K>> {
        private Node<K> root;

        private Node<K> rightRotate(Node<K> cur) {
            Node<K> l = cur.l;
            cur.l = l.r;
            l.r = cur;
            l.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return l;
        }

        private Node<K> leftRotate(Node<K> cur) {
            Node<K> r = cur.r;
            cur.r = r.l;
            r.l = cur;
            r.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return r;
        }

        private Node<K> maintain(Node<K> cur) {
            if (cur == null) {
                return null;
            }
            int ls = cur.l != null ? cur.l.size : 0;
            int lls = cur.l != null && cur.l.l != null ? cur.l.l.size : 0;
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
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (rrs > ls) {
                cur = leftRotate(cur);
                cur.r = maintain(cur.r);
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

        private Node<K> findLast(K key) {
            Node<K> pre = null;
            Node<K> cur = root;
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

        private Node<K> add(Node<K> cur, K key) {
            if (cur == null) {
                return new Node<>(key);
            } else {
                cur.size++;
                if (key.compareTo(cur.k) < 0) {
                    cur.l = add(cur.l, key);
                } else {
                    cur.r = add(cur.r, key);
                }
                return maintain(cur);
            }
        }

        private Node<K> delete(Node<K> cur, K key) {
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
                    Node<K> pre = null;
                    Node<K> des = cur.r;
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

        private Node<K> getIndex(Node<K> cur, int kth) {
            int ls = cur.l != null ? cur.l.size : 0;
            if (kth == ls + 1) {
                return cur;
            } else if (kth <= ls) {
                return getIndex(cur.l, kth);
            } else {
                return getIndex(cur.r, kth - ls - 1);
            }
        }

        public int size() {
            return root == null ? 0 : root.size;
        }

        public boolean containsKey(K key) {
            if (key == null) {
                throw new RuntimeException("");
            }
            Node<K> n = findLast(key);
            return n != null && key.compareTo(n.k) == 0 ? true : false;
        }

        public void add(K key) {
            if (key == null) {
                throw new RuntimeException("");
            }
            Node<K> n = findLast(key);
            if (n == null || key.compareTo(n.k) != 0) {
                root = add(root, key);
            }
        }

        public void remove(K key) {
            if (key == null) {
                throw new RuntimeException("");
            }
            if (containsKey(key)) {
                root = delete(root, key);
            }
        }

        public K getIndexKey(int idx) {
            if (idx < 0 || idx >= this.size()) {
                throw new RuntimeException("");
            }
            return getIndex(root, idx + 1).k;
        }
    }

    public static class VNode implements Comparable<VNode> {
        public int i;
        public int v;
        public VNode(int idx, int val) {
            i = idx;
            v = val;
        }
        @Override
        public int compareTo(VNode o) {
            return v != o.v ? Integer.valueOf(v).compareTo(o.v)
                    : Integer.valueOf(i).compareTo(o.i);
        }
    }

    public static double[] median(int[] nums, int k) {
        SBTMap<VNode> map = new SBTMap<>();
        for (int i = 0; i < k - 1; i++) {
            map.add(new VNode(i, nums[i]));
        }
        double[] ans = new double[nums.length - k + 1];
        int idx = 0;
        for (int i = k - 1; i < nums.length; i++) {
            map.add(new VNode(i, nums[i]));
            if (map.size() % 2 == 0) {
                VNode upMid = map.getIndexKey(map.size() / 2 - 1);
                VNode downMid = map.getIndexKey(map.size() / 2);
                ans[idx++] = ((double)upMid.v + (double) downMid.v) / 2;
            } else {
                VNode mid = map.getIndexKey(map.size() / 2);
                ans[idx++] = mid.v;
            }
            map.remove(new VNode(i - k + 1, nums[i - k + 1]));
        }
        return ans;
    }
}
