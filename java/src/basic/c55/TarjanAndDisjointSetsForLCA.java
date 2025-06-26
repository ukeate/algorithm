package basic.c55;

import java.util.*;

/**
 * Tarjan算法求解最近公共祖先（LCA）问题
 * 
 * 算法思想：
 * 1. 使用Tarjan算法的离线查询思想
 * 2. 结合并查集数据结构维护祖先关系
 * 3. 通过DFS遍历和查询匹配来高效求解多个LCA查询
 * 
 * 时间复杂度：O(n + m * α(n))，其中n是节点数，m是查询数，α是Ackermann函数的反函数
 * 空间复杂度：O(n)
 * 
 * 核心优势：
 * - 离线算法，可以一次性处理多个查询
 * - 每个节点只访问一次，效率高
 * - 适合处理大量LCA查询的场景
 * 
 * @author: algorithm learning
 */
public class TarjanAndDisjointSetsForLCA {
    
    /**
     * 二叉树节点类
     */
    private static class Node {
        public int val;          // 节点值
        public Node left;        // 左子节点
        public Node right;       // 右子节点

        public Node(int v) {
            this.val = v;
        }
    }

    /**
     * 查询类，存储一对需要求LCA的节点
     */
    private static class Query {
        public Node o1;          // 查询节点1
        public Node o2;          // 查询节点2

        public Query(Node o1, Node o2) {
            this.o1 = o1;
            this.o2 = o2;
        }
    }

    /**
     * 收集树中所有节点
     * @param head 根节点
     * @param res 结果列表
     */
    private static void process(Node head, List<Node> res) {
        if (head == null) {
            return;
        }
        res.add(head);
        process(head.left, res);
        process(head.right, res);
    }

    /**
     * 并查集的元素包装类
     * @param <V> 元素类型
     */
    private static class Ele<V> {
        public V val;

        public Ele(V v) {
            val = v;
        }
    }

    /**
     * 并查集数据结构
     * 用于维护节点间的集合关系和祖先信息
     * @param <V> 元素类型
     */
    private static class UnionFindSet<V> {
        public HashMap<V, Ele<V>> eleMap;        // 值到元素的映射
        public HashMap<Ele<V>, Ele<V>> fatherMap; // 父节点映射
        public HashMap<Ele<V>, Integer> sizeMap;  // 集合大小映射

        /**
         * 构造函数，初始化并查集
         * @param list 所有需要管理的元素列表
         */
        public UnionFindSet(List<V> list) {
            eleMap = new HashMap<>();
            fatherMap = new HashMap<>();
            sizeMap = new HashMap<>();
            // 初始时每个元素都是独立的集合
            for (V v : list) {
                Ele<V> ele = new Ele<>(v);
                eleMap.put(v, ele);
                fatherMap.put(ele, ele);  // 自己是自己的父节点
                sizeMap.put(ele, 1);      // 集合大小为1
            }
        }

        /**
         * 查找元素的根节点（带路径压缩）
         * @param e 要查找的元素
         * @return 根节点元素
         */
        private Ele<V> find(Ele<V> e) {
            Stack<Ele<V>> path = new Stack<>();
            // 向上找到根节点，同时记录路径
            while (e != fatherMap.get(e)) {
                path.push(e);
                e = fatherMap.get(e);
            }
            // 路径压缩：将路径上所有节点直接指向根节点
            while (!path.isEmpty()) {
                fatherMap.put(path.pop(), e);
            }
            return e;
        }

        /**
         * 查找节点的根节点值
         * @param node 节点
         * @return 根节点值，不存在返回null
         */
        public V find(V node) {
            return eleMap.containsKey(node) ? find(eleMap.get(node)).val : null;
        }

        /**
         * 判断两个元素是否在同一集合中
         * @param a 元素a
         * @param b 元素b
         * @return 是否在同一集合
         */
        public boolean isSameSet(V a, V b) {
            if (eleMap.containsKey(a) && eleMap.containsKey(b)) {
                return find(eleMap.get(a)) == find(eleMap.get(b));
            }
            return false;
        }

        /**
         * 合并两个元素所在的集合
         * @param a 元素a
         * @param b 元素b
         */
        public void union(V a, V b) {
            if (!eleMap.containsKey(a) || !eleMap.containsKey(b)) {
                return;
            }
            Ele<V> af = find(eleMap.get(a));
            Ele<V> bf = find(eleMap.get(b));
            if (af != bf) {
                int as = sizeMap.get(af);
                int bs = sizeMap.get(bf);
                // 按大小合并，小集合合并到大集合
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

    /**
     * 获取树中的所有节点
     * @param head 根节点
     * @return 所有节点的列表
     */
    private static List<Node> allNodes(Node head) {
        List<Node> res = new ArrayList<>();
        process(head, res);
        return res;
    }

    /**
     * 设置查询映射关系
     * 将每个查询拆分为双向的映射关系，便于后续处理
     * 
     * @param ques 查询数组
     * @param ans 答案数组
     * @param queryMap 节点到其查询对象的映射
     * @param idxMap 节点到查询索引的映射
     */
    private static void setQueries(Query[] ques, Node[] ans, HashMap<Node, LinkedList<Node>> queryMap, HashMap<Node, LinkedList<Integer>> idxMap) {
        Node o1, o2;
        for (int i = 0; i < ans.length; i++) {
            o1 = ques[i].o1;
            o2 = ques[i].o2;
            // 处理特殊情况：节点相同或有空节点
            if (o1 == o2 || o1 == null || o2 == null) {
                ans[i] = o1 != null ? o1 : o2;
            } else {
                // 建立双向查询映射
                if (!queryMap.containsKey(o1)) {
                    queryMap.put(o1, new LinkedList<>());
                    idxMap.put(o1, new LinkedList<>());
                }
                if (!queryMap.containsKey(o2)) {
                    queryMap.put(o2, new LinkedList<>());
                    idxMap.put(o2, new LinkedList<>());
                }
                // o1查询与o2的LCA
                queryMap.get(o1).add(o2);
                idxMap.get(o1).add(i);
                // o2查询与o1的LCA
                queryMap.get(o2).add(o1);
                idxMap.get(o2).add(i);
            }
        }
    }

    /**
     * Tarjan算法的核心递归函数
     * 通过DFS遍历和并查集维护，在合适的时机计算LCA
     * 
     * 算法关键思想：
     * 1. DFS遍历时，先处理子树
     * 2. 处理完子树后，将子树与当前节点合并到同一集合
     * 3. 设置集合的代表元素为当前节点（祖先标记）
     * 4. 检查当前节点的所有查询，如果查询的另一个节点已经被标记，则找到LCA
     * 
     * @param head 当前处理的节点
     * @param ans 答案数组
     * @param queryMap 查询映射
     * @param idxMap 索引映射
     * @param tagMap 祖先标记映射
     * @param sets 并查集
     */
    private static void setAnswers(Node head, Node[] ans, HashMap<Node, LinkedList<Node>> queryMap, HashMap<Node, LinkedList<Integer>> idxMap, HashMap<Node, Node> tagMap, UnionFindSet<Node> sets) {
        if (head == null) {
            return;
        }
        
        // 递归处理左子树
        setAnswers(head.left, ans, queryMap, idxMap, tagMap, sets);
        // 将左子树与当前节点合并
        sets.union(head.left, head);
        // 标记合并后集合的祖先为当前节点
        tagMap.put(sets.find(head), head);
        
        // 递归处理右子树
        setAnswers(head.right, ans, queryMap, idxMap, tagMap, sets);  
        // 将右子树与当前节点合并
        sets.union(head.right, head);
        // 标记合并后集合的祖先为当前节点
        tagMap.put(sets.find(head), head);

        // 处理当前节点的所有查询
        // 此时左右子树都已处理完毕，可以回答相关查询
        LinkedList<Node> nList = queryMap.get(head);
        LinkedList<Integer> iList = idxMap.get(head);
        while (nList != null && !nList.isEmpty()) {
            Node node = nList.poll();      // 查询的另一个节点
            int idx = iList.poll();        // 查询索引
            Node father = sets.find(node); // 找到另一个节点所在集合的代表
            // 如果该集合已经被标记了祖先，说明LCA已确定
            if (tagMap.containsKey(father)) {
                ans[idx] = tagMap.get(father);
            }
        }
    }

    /**
     * Tarjan算法求解多个LCA查询
     * 
     * @param head 树的根节点
     * @param queries 查询数组
     * @return 每个查询对应的LCA节点数组
     */
    public static Node[] tarjan(Node head, Query[] queries) {
        // 建立查询映射
        HashMap<Node, LinkedList<Node>> queryMap = new HashMap<>();
        HashMap<Node, LinkedList<Integer>> idxMap = new HashMap<>();
        // 祖先标记映射
        HashMap<Node, Node> tagMap = new HashMap<>();
        // 初始化并查集
        UnionFindSet<Node> sets = new UnionFindSet<>(allNodes(head));
        // 初始化答案数组
        Node[] ans = new Node[queries.length];
        
        // 设置查询映射
        setQueries(queries, ans, queryMap, idxMap);
        // 执行Tarjan算法
        setAnswers(head, ans, queryMap, idxMap, tagMap, sets);
        return ans;
    }

    /**
     * 生成指定数量的空格字符串
     * @param num 空格数量
     * @return 空格字符串
     */
    private static String getSpace(int num) {
        String space = " ";
        StringBuffer buf = new StringBuffer("");
        for (int i= 0; i < num; i++) {
            buf.append(space);
        }
        return buf.toString();
    }

    /**
     * 递归打印二叉树（可视化展示）
     * @param head 当前节点
     * @param height 当前高度
     * @param to 方向标识
     * @param len 每个节点的显示宽度
     */
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

    /**
     * 打印二叉树结构
     * @param head 根节点
     */
    private static void print(Node head) {
        printIn(head, 0, "H", 17);
        System.out.println();
    }

    /**
     * 测试方法
     * 构建测试树并执行多个LCA查询，验证算法正确性
     */
    public static void main(String[] args) {
        // 构建测试二叉树
        //       1
        //      / \
        //     2   3
        //    / \ / \
        //   4  5 6  7
        //           /
        //          8
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.right.left = new Node(6);
        head.right.right = new Node(7);
        head.right.right.left = new Node(8);
        
        // 打印树结构
        print(head);
        System.out.println("===============");

        // 构造测试查询
        Query[] qs = new Query[7];
        qs[0] = new Query(head.left.right, head.right.left);      // 5和6的LCA
        qs[1] = new Query(head.left.left, head.left);             // 4和2的LCA
        qs[2] = new Query(head.right.left, head.right.right.left); // 6和8的LCA
        qs[3] = new Query(head.left.left, head.right.right);      // 4和7的LCA
        qs[4] = new Query(head.right.right, head.right.right.left); // 7和8的LCA
        qs[5] = new Query(head, head);                             // 1和1的LCA
        qs[6] = new Query(head.left, head.right.right.left);      // 2和8的LCA

        // 执行Tarjan算法求解LCA
        Node[] ans = tarjan(head, qs);

        // 输出结果
        for (int i = 0; i != ans.length; i++) {
            System.out.println("o1 : " + qs[i].o1.val);
            System.out.println("o2 : " + qs[i].o2.val);
            System.out.println("ancestor : " + ans[i].val);
            System.out.println("===============");
        }
    }
}
