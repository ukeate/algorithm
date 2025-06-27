package base.tree4;

/**
 * Size Balanced Tree (SB树) 实现
 * 
 * SB树是一种自平衡二叉搜索树，由陈启峰发明。
 * 其平衡条件为：对于任意节点x，有size[x.child] <= size[x.sibling]
 * 即任意节点的子树大小不超过其兄弟节点的子树大小。
 * 
 * SB树的优点：
 * 1. 实现相对简单，代码量较少
 * 2. 常数因子较小，实际运行效率很高
 * 3. 支持所有有序表的标准操作
 * 4. 支持按排名查找（第k小元素）
 * 
 * 时间复杂度：所有操作均为O(log n)
 */
public class SB {
    
    /**
     * SB树的节点类
     * 
     * @param <K> 键的类型，必须实现Comparable接口
     * @param <V> 值的类型
     */
    public static class Node<K extends Comparable<K>, V> {
        public K k;           // 键
        public V v;           // 值
        public Node<K, V> l;  // 左子节点
        public Node<K, V> r;  // 右子节点
        public int size;      // 以当前节点为根的子树的节点总数
        
        public Node(K key, V val) {
            k = key;
            v = val;
            size = 1;
        }
    }

    /**
     * SB树实现的有序映射表
     * 
     * 支持的操作：
     * 1. put/get/remove: 基本的增删改查
     * 2. containsKey: 判断键是否存在
     * 3. firstKey/lastKey: 获取最小/最大键
     * 4. floorKey/ceilingKey: 获取小于等于/大于等于指定键的最接近键
     * 5. getIndexKey/getIndexValue: 按排名获取第k小的键值对
     * 6. size: 获取元素个数
     * 
     * @param <K> 键的类型，必须实现Comparable接口
     * @param <V> 值的类型
     */
    public static class SizeBalancedTreeMap<K extends Comparable<K>, V> {
        private Node<K, V> root;  // 树根

        /**
         * 右旋操作
         * 
         * 将当前节点向右旋转，用于维护SB树的平衡性质
         * 
         * @param cur 当前节点
         * @return 旋转后的新根节点
         */
        private Node<K, V> rightRotate(Node<K, V> cur) {
            Node<K, V> l = cur.l;
            cur.l = l.r;
            l.r = cur;
            // 更新size：先更新子节点，再更新父节点
            l.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return l;
        }

        /**
         * 左旋操作
         * 
         * 将当前节点向左旋转，用于维护SB树的平衡性质
         * 
         * @param cur 当前节点
         * @return 旋转后的新根节点
         */
        private Node<K, V> leftRotate(Node<K, V> cur) {
            Node<K, V> r = cur.r;
            cur.r = r.l;
            r.l = cur;
            // 更新size：先更新子节点，再更新父节点
            r.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return r;
        }

        /**
         * SB树的调整操作（核心方法）
         * 
         * 维护SB树的平衡性质：size[x.child] <= size[x.sibling]
         * 通过检查四种违规情况并进行相应的旋转来修复：
         * 1. LL违规：左子树的左子树过大
         * 2. LR违规：左子树的右子树过大
         * 3. RR违规：右子树的右子树过大
         * 4. RL违规：右子树的左子树过大
         * 
         * @param cur 当前节点
         * @return 调整后的节点
         */
        private Node<K, V> maintain(Node<K, V> cur) {
            if (cur == null) {
                return null;
            }
            // 获取各个子树的大小
            int ls = cur.l != null ? cur.l.size : 0;
            int lls = cur.l != null && cur.l.l != null ? cur.l.l.size: 0;
            int lrs = cur.l != null && cur.l.r != null ? cur.l.r.size : 0;
            int rs = cur.r != null ? cur.r.size : 0;
            int rls = cur.r != null && cur.r.l != null ? cur.r.l.size : 0;
            int rrs = cur.r != null && cur.r.r != null ? cur.r.r.size : 0;
            
            if (lls > rs) {
                // LL违规：左子树的左子树 > 右子树
                cur = rightRotate(cur);
                cur.r = maintain(cur.r);  // 递归调整右子树
                cur = maintain(cur);      // 递归调整当前节点
            } else if (lrs > rs) {
                // LR违规：左子树的右子树 > 右子树
                cur.l = leftRotate(cur.l);   // 先左旋左子树
                cur = rightRotate(cur);      // 再右旋当前节点
                cur.l = maintain(cur.l);     // 递归调整左子树
                cur.r = maintain(cur.r);     // 递归调整右子树
                cur = maintain(cur);         // 递归调整当前节点
            } else if (rrs > ls) {
                // RR违规：右子树的右子树 > 左子树
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);     // 递归调整左子树
                cur = maintain(cur);         // 递归调整当前节点
            } else if (rls > ls) {
                // RL违规：右子树的左子树 > 左子树
                cur.r = rightRotate(cur.r);  // 先右旋右子树
                cur = leftRotate(cur);       // 再左旋当前节点
                cur.l = maintain(cur.l);     // 递归调整左子树
                cur.r = maintain(cur.r);     // 递归调整右子树
                cur = maintain(cur);         // 递归调整当前节点
            }
            return cur;
        }

        /**
         * 查找键值对应的节点（或最接近的节点）
         * 
         * @param key 要查找的键
         * @return 键对应的节点，或者查找路径上的最后一个节点
         */
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

        /**
         * 查找大于等于指定键的最小键对应的节点
         * 
         * @param key 指定的键
         * @return 大于等于key的最小键对应的节点，如果不存在则返回null
         */
        private Node<K, V> findLastBigEqual(K key) {
            Node<K, V> ans = null;
            Node<K, V> cur = root;
            while (cur != null) {
                if (key.compareTo(cur.k) == 0) {
                    ans = cur;
                    break;
                } else if (key.compareTo(cur.k) < 0) {
                    ans = cur;       // 当前节点可能是答案
                    cur = cur.l;
                } else {
                    cur = cur.r;
                }
            }
            return ans;
        }

        /**
         * 查找小于等于指定键的最大键对应的节点
         * 
         * @param key 指定的键
         * @return 小于等于key的最大键对应的节点，如果不存在则返回null
         */
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
                    ans = cur;       // 当前节点可能是答案
                    cur = cur.r;
                }
            }
            return ans;
        }

        /**
         * 向子树中添加新的键值对
         * 
         * @param cur 当前子树的根节点
         * @param key 要添加的键
         * @param val 要添加的值
         * @return 添加后的子树根节点
         */
        private Node<K, V> add(Node<K, V> cur, K key, V val) {
            if (cur == null) {
                return new Node<>(key, val);
            } else {
                cur.size++;  // 子树大小增加1
                if (key.compareTo(cur.k) < 0) {
                    cur.l = add(cur.l, key, val);
                } else {
                    cur.r = add(cur.r, key, val);
                }
                return maintain(cur);  // 维护平衡
            }
        }

        /**
         * 从子树中删除指定键的节点
         * 
         * @param cur 当前子树的根节点
         * @param key 要删除的键
         * @return 删除后的子树根节点
         */
        private Node<K, V> delete(Node<K, V> cur, K key) {
            cur.size--;  // 子树大小减少1
            if (key.compareTo(cur.k) > 0) {
                cur.r = delete(cur.r, key);
            } else if (key.compareTo(cur.k) < 0) {
                cur.l = delete(cur.l, key);
            } else {
                // 找到要删除的节点
                if (cur.l == null && cur.r == null) {
                    cur = null;  // 叶子节点直接删除
                } else if (cur.l == null && cur.r != null) {
                    cur = cur.r;  // 只有右子树
                } else if (cur.l != null && cur.r == null) {
                    cur = cur.l;  // 只有左子树
                } else {
                    // 有左右子树：用右子树的最小节点替换当前节点
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

        /**
         * 获取排名为kth的节点（1-indexed）
         * 
         * @param cur 当前子树的根节点
         * @param kth 排名（1表示最小值）
         * @return 排名为kth的节点
         */
        private Node<K, V> getIndex(Node<K, V> cur, int kth) {
            int ls = cur.l != null ? cur.l.size : 0;
            if (kth == ls + 1) {
                return cur;  // 当前节点就是第kth小
            } else if (kth <= ls) {
                return getIndex(cur.l, kth);  // 在左子树中查找
            } else {
                return getIndex(cur.r, kth - ls - 1);  // 在右子树中查找
            }
        }

        /**
         * 判断是否包含指定的键
         * 
         * @param key 要查找的键
         * @return 如果包含该键返回true，否则返回false
         */
        public boolean containsKey(K key) {
            if (key == null) {
                throw new RuntimeException("invalid parameter");
            }
            Node<K, V> n = findLast(key);
            return n != null && key.compareTo(n.k) == 0 ? true : false;
        }

        /**
         * 插入或更新键值对
         * 
         * @param key 键
         * @param val 值
         */
        public void put(K key, V val) {
            if (key == null) {
                throw new RuntimeException("invalid parameter");
            }
            Node<K, V> n = findLast(key);
            if (n != null && key.compareTo(n.k) == 0) {
                n.v = val;  // 键已存在，更新值
            } else {
                root = add(root, key, val);  // 键不存在，插入新节点
            }
        }

        /**
         * 删除指定键的键值对
         * 
         * @param key 要删除的键
         */
        public void remove(K key) {
            if (key == null) {
                throw new RuntimeException("invalid parameter");
            }
            if (containsKey(key)) {
                root = delete(root, key);
            }
        }

        /**
         * 获取排名为idx的键（0-indexed）
         * 
         * @param idx 排名索引（0表示最小值）
         * @return 排名为idx的键
         */
        public K getIndexKey(int idx) {
            if (idx < 0 || idx >= this.size()) {
                throw new RuntimeException("invalid parameter");
            }
            return getIndex(root, idx + 1).k;
        }

        /**
         * 获取排名为idx的值（0-indexed）
         * 
         * @param idx 排名索引（0表示最小值）
         * @return 排名为idx的值
         */
        public V getIndexValue(int idx) {
            if (idx < 0 || idx >= this.size()) {
                throw new RuntimeException("invalid parameter");
            }
            return getIndex(root, idx + 1).v;
        }

        /**
         * 获取指定键对应的值
         * 
         * @param key 键
         * @return 对应的值，如果键不存在则返回null
         */
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

        /**
         * 获取最小的键
         * 
         * @return 最小的键，如果树为空则返回null
         */
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

        /**
         * 获取最大的键
         * 
         * @return 最大的键，如果树为空则返回null
         */
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

        /**
         * 获取小于等于指定键的最大键（向下取整）
         * 
         * @param key 指定的键
         * @return 小于等于key的最大键，如果不存在则返回null
         */
        public K floorKey(K key) {
            if (key == null) {
                throw new RuntimeException("invalid parameter");
            }
            Node<K, V> n = findLastSmallEqual(key);
            return n == null ? null : n.k;
        }

        /**
         * 获取大于等于指定键的最小键（向上取整）
         * 
         * @param key 指定的键
         * @return 大于等于key的最小键，如果不存在则返回null
         */
        public K ceilingKey(K key) {
            if (key == null) {
                throw new RuntimeException("invalid parameter");
            }
            Node<K, V> n = findLastBigEqual(key);
            return n == null ? null : n.k;
        }

        /**
         * 获取树中节点的总数
         * 
         * @return 节点总数
         */
        public int size() {
            return root == null ? 0 : root.size;
        }
    }

    // 以下是打印和测试相关的辅助方法

    /**
     * 生成指定数量的空格字符串
     */
    private static String getSpace(int num) {
        String space = " ";
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < num; i++) {
            buf.append(space);
        }
        return buf.toString();
    }
    
    /**
     * 递归打印树的结构（用于调试）
     */
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
    
    /**
     * 打印树的结构（用于调试）
     */
    private static void print(Node<String, Integer> head) {
        printIn(head, 0, "H", 17);
    }

    /**
     * 主方法：测试SB树的各种功能
     */
    public static void main(String[] args) {
        SizeBalancedTreeMap<String, Integer> sbt = new SizeBalancedTreeMap<>();
        
        // 测试插入操作
        sbt.put("d", 4);
        sbt.put("c", 3);
        sbt.put("a", 1);
        sbt.put("b", 2);
        sbt.put("g", 7);
        sbt.put("f", 6);
        sbt.put("h", 8);
        sbt.put("i", 9);
        
        // 测试更新操作
        sbt.put("a", 111);
        System.out.println(sbt.get("a"));  // 输出111
        sbt.put("a", 1);
        System.out.println(sbt.get("a"));  // 输出1
        
        // 测试按排名查找
        for (int i = 0; i < sbt.size(); i++) {
            System.out.println(sbt.getIndexKey(i) + "," + sbt.getIndexValue(i));
        }
        
        // 打印树结构
        print(sbt.root);
        
        // 测试边界查找
        System.out.println(sbt.firstKey());      // 最小键
        System.out.println(sbt.lastKey());       // 最大键
        System.out.println(sbt.floorKey("g"));   // <= g 的最大键
        System.out.println(sbt.ceilingKey("g")); // >= g 的最小键
        System.out.println(sbt.floorKey("e"));   // <= e 的最大键
        System.out.println(sbt.ceilingKey("e")); // >= e 的最小键
        System.out.println(sbt.floorKey(""));    // <= "" 的最大键
        System.out.println(sbt.ceilingKey(""));  // >= "" 的最小键
        System.out.println(sbt.floorKey("j"));   // <= j 的最大键
        System.out.println(sbt.ceilingKey("j")); // >= j 的最小键
        
        // 测试删除操作
        sbt.remove("d");
        print(sbt.root);
        sbt.remove("f");
        print(sbt.root);
    }
}
