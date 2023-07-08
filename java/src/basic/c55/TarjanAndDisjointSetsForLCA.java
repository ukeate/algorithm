package basic.c55;

import java.util.*;

// 离线求最近公共祖先
// tarjan求强联通分量
public class TarjanAndDisjointSetsForLCA {
    private static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int v) {
            this.val = v;
        }
    }

    private static class Query {
        public Node o1;
        public Node o2;

        public Query(Node o1, Node o2) {
            this.o1 = o1;
            this.o2 = o2;
        }
    }

    private static void process(Node head, List<Node> res) {
        if (head == null) {
            return;
        }
        res.add(head);
        process(head.left, res);
        process(head.right, res);
    }

    private static class Ele<V> {
        public V val;

        public Ele(V v) {
            val = v;
        }
    }

    private static class UnionFindSet<V> {
        public HashMap<V, Ele<V>> eleMap;
        public HashMap<Ele<V>, Ele<V>> fatherMap;
        public HashMap<Ele<V>, Integer> sizeMap;

        public UnionFindSet(List<V> list) {
            eleMap = new HashMap<>();
            fatherMap = new HashMap<>();
            sizeMap = new HashMap<>();
            for (V v : list) {
                Ele<V> ele = new Ele<>(v);
                eleMap.put(v, ele);
                fatherMap.put(ele, ele);
                sizeMap.put(ele, 1);
            }
        }

        private Ele<V> find(Ele<V> e) {
            Stack<Ele<V>> path = new Stack<>();
            while (e != fatherMap.get(e)) {
                path.push(e);
                e = fatherMap.get(e);
            }
            while (!path.isEmpty()) {
                fatherMap.put(path.pop(), e);
            }
            return e;
        }

        public V find(V node) {
            return eleMap.containsKey(node) ? find(eleMap.get(node)).val : null;
        }

        public boolean isSameSet(V a, V b) {
            if (eleMap.containsKey(a) && eleMap.containsKey(b)) {
                return find(eleMap.get(a)) == find(eleMap.get(b));
            }
            return false;
        }

        public void union(V a, V b) {
            if (!eleMap.containsKey(a) || !eleMap.containsKey(b)) {
                return;
            }
            Ele<V> af = find(eleMap.get(a));
            Ele<V> bf = find(eleMap.get(b));
            if (af != bf) {
                int as = sizeMap.get(af);
                int bs = sizeMap.get(bf);
                if (as >= bs) {
                    fatherMap.put(bf, af);
                    sizeMap.put(af, as + bs);
                    sizeMap.remove(bf);
                } else {
                    fatherMap.put(af, bf);
                    sizeMap.put(bf, as + bs);
                    sizeMap.remove(af);
                }
            }
        }
    }

    private static List<Node> allNodes(Node head) {
        List<Node> res = new ArrayList<>();
        process(head, res);
        return res;
    }

    private static void setQueries(Query[] ques, Node[] ans, HashMap<Node, LinkedList<Node>> queryMap, HashMap<Node, LinkedList<Integer>> idxMap) {
        Node o1, o2;
        for (int i = 0; i < ans.length; i++) {
            o1 = ques[i].o1;
            o2 = ques[i].o2;
            if (o1 == o2 || o1 == null || o2 == null) {
                ans[i] = o1 != null ? o1 : o2;
            } else {
                if (!queryMap.containsKey(o1)) {
                    queryMap.put(o1, new LinkedList<>());
                    idxMap.put(o1, new LinkedList<>());
                }
                if (!queryMap.containsKey(o2)) {
                    queryMap.put(o2, new LinkedList<>());
                    idxMap.put(o2, new LinkedList<>());
                }
                queryMap.get(o1).add(o2);
                idxMap.get(o1).add(i);
                queryMap.get(o2).add(o1);
                idxMap.get(o2).add(i);
            }
        }
    }

    private static void setAnswers(Node head, Node[] ans, HashMap<Node, LinkedList<Node>> queryMap, HashMap<Node, LinkedList<Integer>> idxMap, HashMap<Node, Node> tagMap, UnionFindSet<Node> sets) {
        if (head == null) {
            return;
        }
        setAnswers(head.left, ans, queryMap, idxMap, tagMap, sets);
        sets.union(head.left, head);
        tagMap.put(sets.find(head), head);
        setAnswers(head.right, ans, queryMap, idxMap, tagMap, sets);
        sets.union(head.right, head);
        tagMap.put(sets.find(head), head);

        // 最后一次路过, 且配对节点已标记, 找到LCA
        LinkedList<Node> nList = queryMap.get(head);
        LinkedList<Integer> iList = idxMap.get(head);
        while (nList != null && !nList.isEmpty()) {
            Node node = nList.poll();
            int idx = iList.poll();
            Node father = sets.find(node);
            if (tagMap.containsKey(father)) {
                ans[idx] = tagMap.get(father);
            }
        }
    }

    public static Node[] tarjan(Node head, Query[] queries) {
        HashMap<Node, LinkedList<Node>> queryMap = new HashMap<>();
        HashMap<Node, LinkedList<Integer>> idxMap = new HashMap<>();
        HashMap<Node, Node> tagMap = new HashMap<>();
        UnionFindSet<Node> sets = new UnionFindSet<>(allNodes(head));
        Node[] ans = new Node[queries.length];
        setQueries(queries, ans, queryMap, idxMap);
        setAnswers(head, ans, queryMap, idxMap, tagMap, sets);
        return ans;
    }

    //

    private static String getSpace(int num) {
        String space = " ";
        StringBuffer buf = new StringBuffer("");
        for (int i= 0; i < num; i++) {
            buf.append(space);
        }
        return buf.toString();
    }

    private static void printIn(Node head, int height, String to, int len) {
        if (head == null) {
            return;
        }
        printIn(head.right, height + 1, "v", len);
        String val = to + head.val + to;
        int lenM = val.length();
        int lenL = (len - lenM) / 2;
        int lenR = len - lenM - lenL;
        val = getSpace(lenL) + val + getSpace(lenR);
        System.out.println(getSpace(height * len) + val);
        printIn(head.left, height + 1, "^", len);
    }

    private static void print(Node head) {
        printIn(head, 0, "H", 17);
        System.out.println();
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.right.left = new Node(6);
        head.right.right = new Node(7);
        head.right.right.left = new Node(8);
        print(head);
        System.out.println("===============");

        Query[] qs = new Query[7];
        qs[0] = new Query(head.left.right, head.right.left);
        qs[1] = new Query(head.left.left, head.left);
        qs[2] = new Query(head.right.left, head.right.right.left);
        qs[3] = new Query(head.left.left, head.right.right);
        qs[4] = new Query(head.right.right, head.right.right.left);
        qs[5] = new Query(head, head);
        qs[6] = new Query(head.left, head.right.right.left);

        Node[] ans = tarjan(head, qs);

        for (int i = 0; i != ans.length; i++) {
            System.out.println("o1 : " + qs[i].o1.val);
            System.out.println("o2 : " + qs[i].o2.val);
            System.out.println("ancestor : " + ans[i].val);
            System.out.println("===============");
        }
    }
}
