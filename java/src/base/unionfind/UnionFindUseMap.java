package base.unionfind;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class UnionFindUseMap {
    public static class UnionFind<V> {
        public HashMap<V, V> father;
        public HashMap<V, Integer> size;

        public UnionFind(List<V> values) {
            father = new HashMap<>();
            size = new HashMap<>();
            for (V cur : values) {
                father.put(cur, cur);
                size.put(cur, 1);
            }
        }

        private V findFather(V cur) {
            Stack<V> path = new Stack<>();
            while (cur != father.get(cur)) {
                path.push(cur);
                cur = father.get(cur);
            }
            while (!path.isEmpty()) {
                father.put(path.pop(), cur);
            }
            return cur;
        }

        public boolean isSameSet(V a, V b) {
            return findFather(a) == findFather(b);
        }

        public void union(V a, V b) {
            V af = findFather(a);
            V bf = findFather(b);
            if (af != bf) {
                int as = size.get(af);
                int bs = size.get(bf);
                if (as >= bs) {
                    father.put(bf, af);
                    size.put(af, as + bs);
                    size.remove(bf);
                } else {
                    father.put(af, bf);
                    size.put(bf, as + bs);
                    size.remove(af);
                }
            }
        }

        public int sets() {
            return size.size();
        }
    }
}
