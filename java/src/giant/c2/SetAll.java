package giant.c2;

import java.util.HashMap;

/**
 * 支持O(1)复杂度setAll操作的HashMap改进版本
 * 
 * 问题背景：
 * 普通HashMap的setAll操作需要O(n)时间遍历所有键值对。
 * 这里通过巧妙的设计实现了O(1)时间复杂度的setAll操作。
 * 
 * 核心思想：
 * 1. 使用时间戳（timestamp）来区分操作的先后顺序
 * 2. 每个key-value对都携带其创建/更新的时间戳
 * 3. setAll操作只是记录一个全局的值和时间戳
 * 4. get操作时比较具体key的时间戳和setAll的时间戳
 * 
 * 如果具体key的时间戳 > setAll的时间戳，返回具体值；
 * 否则返回setAll的值。
 * 
 * 时间复杂度：
 * - put: O(1)
 * - get: O(1)
 * - setAll: O(1) ← 这是核心优化
 * 
 * 空间复杂度：O(n)
 */
public class SetAll {
    /**
     * 带时间戳的值封装类
     * 每个值都携带其创建或最后修改的时间戳
     * @param <V> 值的类型
     */
    public static class Val<V> {
        public V val;      // 实际存储的值
        public long time;  // 时间戳，用于区分操作的先后顺序
        
        public Val(V v, long t) {
            val = v;
            time = t;
        }
    }

    /**
     * 支持O(1) setAll操作的自定义Map
     * @param <K> 键的类型
     * @param <V> 值的类型
     */
    public static class MyMap<K, V> {
        private HashMap<K, Val<V>> map; // 实际存储数据的HashMap
        private long time;              // 全局时间戳计数器，保证严格递增
        private Val<V> setAll;          // setAll操作的全局值和时间戳

        /**
         * 构造函数：初始化空的MyMap
         */
        public MyMap() {
            map = new HashMap<>();
            time = 0;                         // 时间戳从0开始
            setAll = new Val<>(null, -1);     // setAll的初始时间戳为-1，表示从未调用过
        }

        /**
         * 放入键值对，使用当前时间戳
         * 时间复杂度：O(1)
         * 
         * @param key 键
         * @param val 值
         */
        public void put(K key, V val) {
            map.put(key, new Val<>(val, time++)); // 使用当前时间戳并递增
        }

        /**
         * O(1)时间复杂度的setAll操作
         * 不需要遍历所有键，只是更新全局值和时间戳
         * 
         * @param val 要设置给所有键的值
         */
        public void setAll(V val) {
            setAll = new Val<>(val, time++); // 更新全局setAll的值和时间戳
        }

        /**
         * 获取键对应的值
         * 通过比较时间戳来决定返回具体值还是setAll值
         * 时间复杂度：O(1)
         * 
         * @param key 要查询的键
         * @return 键对应的值，如果键不存在则返回null
         */
        public V get(K key) {
            if (!map.containsKey(key)) {
                return null; // 键不存在
            }
            
            // 比较具体key的时间戳和setAll的时间戳
            if (map.get(key).time > setAll.time) {
                // 具体key的时间戳更新，说明在setAll之后被更新过
                return map.get(key).val;
            } else {
                // setAll的时间戳更新，说明在最后一次setAll之后该key没有被更新
                return setAll.val;
            }
        }
    }

}
