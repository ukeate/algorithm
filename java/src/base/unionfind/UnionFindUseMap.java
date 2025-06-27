package base.unionfind;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * 基于HashMap的并查集（Union-Find）实现
 * 
 * 与基于数组的并查集相比，这个实现有以下特点：
 * 
 * 优点：
 * 1. 支持任意类型的元素作为集合成员（泛型支持）
 * 2. 不需要预先知道元素的数量上限
 * 3. 元素可以是字符串、自定义对象等，不局限于整数
 * 4. 动态分配内存，只为实际存在的元素分配空间
 * 
 * 缺点：
 * 1. HashMap操作有额外的开销（哈希计算、碰撞处理等）
 * 2. 内存占用通常比数组实现更大
 * 3. 缓存局部性较差，可能影响性能
 * 
 * 适用场景：
 * - 元素类型复杂，不便映射为整数
 * - 元素数量不确定或变化范围很大
 * - 内存使用优先于性能的场景
 */
public class UnionFindUseMap {
    /**
     * 基于HashMap的并查集实现
     * @param <V> 集合元素的类型，可以是任意类型
     */
    public static class UnionFind<V> {
        public HashMap<V, V> father;      // 父节点映射：每个元素映射到其父节点
        public HashMap<V, Integer> size;  // 集合大小映射：只有根节点记录其所在集合的大小

        /**
         * 构造函数：初始化并查集
         * @param values 要加入并查集的元素列表
         */
        public UnionFind(List<V> values) {
            father = new HashMap<>();
            size = new HashMap<>();
            // 初始化：每个元素的父节点是自己，每个集合的大小是1
            for (V cur : values) {
                father.put(cur, cur);  // 自己是自己的父节点
                size.put(cur, 1);      // 集合大小初始为1
            }
        }

        /**
         * 查找元素所在集合的根节点（代表元素）
         * 使用路径压缩优化：将查找路径上的所有节点直接连接到根节点
         * 
         * 算法步骤：
         * 1. 向上查找根节点，用栈记录查找路径
         * 2. 将路径上的所有节点直接连接到根节点（路径压缩）
         * 
         * @param cur 要查找的元素
         * @return 该元素所在集合的根节点
         */
        private V findFather(V cur) {
            Stack<V> path = new Stack<>();
            // 第一阶段：向上查找根节点，记录路径
            while (cur != father.get(cur)) {
                path.push(cur);           // 将当前节点加入路径
                cur = father.get(cur);    // 向上移动到父节点
            }
            // 第二阶段：路径压缩，将路径上的所有节点直接连接到根节点
            while (!path.isEmpty()) {
                father.put(path.pop(), cur);
            }
            return cur;  // 返回根节点
        }

        /**
         * 判断两个元素是否在同一个集合中
         * @param a 元素a
         * @param b 元素b
         * @return 如果在同一集合中返回true，否则返回false
         */
        public boolean isSameSet(V a, V b) {
            return findFather(a) == findFather(b);
        }

        /**
         * 合并两个元素所在的集合
         * 使用按大小合并优化：将小集合合并到大集合中，以保持树的平衡
         * 
         * 算法步骤：
         * 1. 找到两个元素所在集合的根节点
         * 2. 如果已经在同一集合中，则无需操作
         * 3. 否则按集合大小进行合并，并更新相关信息
         * 
         * @param a 元素a
         * @param b 元素b
         */
        public void union(V a, V b) {
            V af = findFather(a);  // 找到a所在集合的根节点
            V bf = findFather(b);  // 找到b所在集合的根节点
            
            if (af != bf) {  // 如果不在同一集合中才需要合并
                int as = size.get(af);  // a所在集合的大小
                int bs = size.get(bf);  // b所在集合的大小
                
                // 按大小合并：将小集合合并到大集合中
                if (as >= bs) {
                    // 将bf集合合并到af集合中
                    father.put(bf, af);        // bf的父节点指向af
                    size.put(af, as + bs);     // 更新af集合的大小
                    size.remove(bf);           // 移除bf的大小记录（bf不再是根节点）
                } else {
                    // 将af集合合并到bf集合中
                    father.put(af, bf);        // af的父节点指向bf
                    size.put(bf, as + bs);     // 更新bf集合的大小
                    size.remove(af);           // 移除af的大小记录（af不再是根节点）
                }
            }
        }

        /**
         * 获取当前的集合数量
         * 由于只有根节点在size映射中保留记录，所以size的大小就是集合数量
         * @return 当前的集合数量
         */
        public int sets() {
            return size.size();
        }
    }
}
