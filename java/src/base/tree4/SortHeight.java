package base.tree4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

// https://leetcode.com/problems/queue-reconstruction-by-height/
public class SortHeight {
    private static class Unit {
        public int h;
        public int k;

        public Unit(int height, int greater) {
            h = height;
            k = greater;
        }
    }

    private static class Comp implements Comparator<Unit> {
        @Override
        public int compare(Unit o1, Unit o2) {
            return o1.h != o2.h ? (o2.h - o1.h) : (o1.k - o2.k);
        }
    }

    public static int[][] sort1(int[][] people) {
        int n = people.length;
        Unit[] units = new Unit[n];
        for (int i = 0; i < n; i++) {
            units[i] = new Unit(people[i][0], people[i][1]);
        }
        Arrays.sort(units, new Comp());
        ArrayList<Unit> arr = new ArrayList<>();
        for (Unit unit : units) {
            arr.add(unit.k, unit);
        }
        int[][] ans = new int[n][2];
        int idx = 0;
        for (Unit unit : arr) {
            ans[idx][0] = unit.h;
            ans[idx++][1] = unit.k;
        }
        return ans;
    }

    //

    private static class Node {
        public int v;
        public Node l;
        public Node r;
        public int size;

        public Node(int arrIdx) {
            v = arrIdx;
            size = 1;
        }
    }

    private static class SBTree {
        private Node root;

        private Node rightRotate(Node cur) {
            Node l = cur.l;
            cur.l = l.r;
            l.r = cur;
            l.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return l;
        }

        private Node leftRotate(Node cur) {
            Node r = cur.r;
            cur.r = r.l;
            r.l = cur;
            r.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return r;
        }

        private Node maintain(Node cur) {
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

        private Node insert(Node root, int idx, Node cur) {
            if (root == null) {
                return cur;
            }
            root.size++;
            int les = (root.l != null ? root.l.size : 0) + 1;
            if (idx < les) {
                root.l = insert(root.l, idx, cur);
            } else {
                root.r = insert(root.r, idx - les, cur);
            }
            root = maintain(root);
            return root;
        }

        private Node get(Node root, int idx) {
            int ls = root.l != null ? root.l.size : 0;
            if (idx < ls) {
                return get(root.l, idx);
            } else if (idx == ls) {
                return root;
            } else {
                return get(root.r, idx - ls - 1);
            }
        }

        public void insert(int idx, int val) {
            Node cur = new Node(val);
            if (root == null) {
                root = cur;
            } else {
                if (idx <= root.size) {
                    root = insert(root, idx, cur);
                }
            }
        }

        public int get(int idx) {
            Node ans = get(root, idx);
            return ans.v;
        }

        private void process(Node head, LinkedList<Integer> indexes) {
            if (head == null) {
                return;
            }
            process(head.l, indexes);
            indexes.addLast(head.v);
            process(head.r, indexes);
        }

        public LinkedList<Integer> allIndexes() {
            LinkedList<Integer> indexes = new LinkedList<>();
            process(root, indexes);
            return indexes;
        }
    }

    public static int[][] sort2(int[][] people) {
        int n = people.length;
        Unit[] units = new Unit[n];
        for (int i = 0; i < n; i++) {
            units[i] = new Unit(people[i][0], people[i][1]);
        }
        Arrays.sort(units, new Comp());
        SBTree tree = new SBTree();
        for (int i = 0; i < n; i++) {
            tree.insert(units[i].k, i);
        }
        LinkedList<Integer> all = tree.allIndexes();
        int[][] ans = new int[n][2];
        int idx = 0;
        for (Integer i : all) {
            ans[idx][0] = units[i].h;
            ans[idx++][1] = units[i].k;
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxVal = 10000000;

    }
}
