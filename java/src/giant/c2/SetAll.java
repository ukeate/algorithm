package giant.c2;

import java.util.HashMap;

// 改写HashMap支持O(1)复杂度setAll
public class SetAll {
    public static class Val<V> {
        public V val;
        public long time;
        public Val(V v, long t) {
            val = v;
            time = t;
        }
    }

    public static class MyMap<K, V> {
        private HashMap<K, Val<V>> map;
        private long time;
        private Val<V> setAll;

        public MyMap() {
            map = new HashMap<>();
            time = 0;
            setAll = new Val<>(null, -1);
        }

        public void put(K key, V val) {
            map.put(key, new Val<>(val, time++));
        }

        public void setAll(V val) {
            setAll = new Val<>(val, time++);
        }

        public V get(K key) {
            if (!map.containsKey(key)) {
                return null;
            }
            if (map.get(key).time > setAll.time) {
                return map.get(key).val;
            } else {
                return setAll.val;
            }
        }
    }

}
