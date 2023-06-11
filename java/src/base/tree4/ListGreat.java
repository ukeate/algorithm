package base.tree4;

import java.util.ArrayList;
import java.util.LinkedList;

public class ListGreat {
    public static class Node<V> {
        public V v;
        public Node<V> l;
        public Node<V> r;
        public int size;

        public Node(V val) {
            v = val;
            size = 1;
        }
    }

    public static class SBList<V> {
        private Node<V> root;

        private Node<V> rightRotate(Node<V> cur) {
            Node<V> l = cur.l;
            cur.l = l.r;
            l.r = cur;
            l.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return l;
        }

        private Node<V> leftRotate(Node<V> cur) {
            Node<V> r = cur.r;
            cur.r = r.l;
            r.l = cur;
            r.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return r;
        }

        private Node<V> maintain(Node<V> cur) {
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

        private Node<V> add(Node<V> root, int idx, Node<V> cur) {
            if (root == null) {
                return cur;
            }
            root.size++;
            int les = (root.l != null ? root.l.size : 0) + 1;
            if (idx < les) {
                root.l = add(root.l, idx, cur);
            } else {
                root.r = add(root.r, idx - les, cur);
            }
            root = maintain(root);
            return root;
        }

        private Node<V> remove(Node<V> root, int idx) {
            root.size--;
            int rootIdx = root.l != null ? root.l.size : 0;
            if (idx != rootIdx) {
                if (idx < rootIdx) {
                    root.l = remove(root.l, idx);
                } else {
                    root.r = remove(root.r, idx - rootIdx - 1);
                }
                return root;
            }
            if (root.l == null && root.r == null) {
                return null;
            }
            if (root.l == null) {
                return root.r;
            }
            if (root.r == null) {
                return root.l;
            }
            Node<V> pre = null;
            Node<V> des = root.r;
            des.size--;
            while (des.l != null) {
                pre = des;
                des = des.l;
                des.size--;
            }
            if (pre != null) {
                pre.l = des.r;
                des.r = root.r;
            }
            des.l = root.l;
            des.size = des.l.size + (des.r == null ? 0 : des.r.size) + 1;
            return des;
        }

        private Node<V> get(Node<V> root, int idx) {
            int ls = root.l != null ? root.l.size : 0;
            if (idx < ls) {
                return get(root.l, idx);
            } else if (idx == ls) {
                return root;
            } else {
                return get(root.r, idx - ls - 1);
            }
        }

        public void add(int idx, V v) {
            Node<V> cur = new Node<>(v);
            if (root == null) {
                root = cur;
            } else {
                if (idx <= root.size) {
                    root = add(root, idx, cur);
                }
            }
        }

        public V get(int idx) {
            Node<V> ans = get(root, idx);
            return ans.v;
        }

        public void remove(int idx) {
            if (idx >= 0 && size() > idx) {
                root = remove(root, idx);
            }
        }

        public int size() {
            return root == null ? 0 : root.size;
        }
    }

    public static void main(String[] args) {
        int times = 50000;
        int maxVal = 10000000;
        ArrayList<Integer> list = new ArrayList<>();
        SBList<Integer> sbList = new SBList<>();
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            if (list.size() != sbList.size()) {
                System.out.println("Wrong");
                break;
            }
            if (list.size() > 1 && Math.random() < 0.5) {
                int removeIdx = (int)(list.size() * Math.random());
                list.remove(removeIdx);
                sbList.remove(removeIdx);
            } else {
                int idx = (int) ((list.size() + 1) * Math.random());
                int val = (int) ((maxVal + 1) * Math.random());
                list.add(idx, val);
                sbList.add(idx, val);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals(sbList.get(i))) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");

        times = 500000;
        list = new ArrayList<>();
        sbList = new SBList<>();
        long start = 0, end = 0;

		start = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			int randomIndex = (int) (Math.random() * (list.size() + 1));
			int randomValue = (int) (Math.random() * (maxVal + 1));
			list.add(randomIndex, randomValue);
		}
		end = System.currentTimeMillis();
		System.out.println("ArrayList插入总时长(毫秒) ： " + (end - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			int randomIndex = (int) (Math.random() * (i + 1));
			list.get(randomIndex);
		}
		end = System.currentTimeMillis();
		System.out.println("ArrayList读取总时长(毫秒) : " + (end - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			int randomIndex = (int) (Math.random() * list.size());
			list.remove(randomIndex);
		}
		end = System.currentTimeMillis();
		System.out.println("ArrayList删除总时长(毫秒) : " + (end - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			int randomIndex = (int) (Math.random() * (sbList.size() + 1));
			int randomValue = (int) (Math.random() * (maxVal + 1));
			sbList.add(randomIndex, randomValue);
		}
		end = System.currentTimeMillis();
		System.out.println("SBList插入总时长(毫秒) : " + (end - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			int randomIndex = (int) (Math.random() * (i + 1));
			sbList.get(randomIndex);
		}
		end = System.currentTimeMillis();
		System.out.println("SBList读取总时长(毫秒) :  " + (end - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			int randomIndex = (int) (Math.random() * sbList.size());
			sbList.remove(randomIndex);
		}
		end = System.currentTimeMillis();
		System.out.println("SBList删除总时长(毫秒) :  " + (end - start));

        times = 50000;
        LinkedList list2 = new LinkedList<>();
        sbList = new SBList<>();

        start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            int randomValue = (int) (Math.random() * (maxVal + 1));
            list2.add(randomIndex, randomValue);
        }
        end = System.currentTimeMillis();
        System.out.println("LinkedList插入总时长(毫秒) ： " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            list2.get(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("LinkedList读取总时长(毫秒) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            int randomValue = (int) (Math.random() * (maxVal + 1));
            sbList.add(randomIndex, randomValue);
        }
        end = System.currentTimeMillis();
        System.out.println("SBTree插入总时长(毫秒) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            sbList.get(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("SBTree读取总时长(毫秒) :  " + (end - start));

    }

}
