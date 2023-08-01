package leetc.top;

import java.util.HashMap;

public class P380_InsertDeleteGetRandom {
    public class RandomizedSet {
        private HashMap<Integer, Integer> keyIdx;
        private HashMap<Integer, Integer> idxKey;
        private int size;
        public RandomizedSet() {
            keyIdx = new HashMap<>();
            idxKey = new HashMap<>();
            size = 0;
        }

        public boolean insert(int val) {
            if (!keyIdx.containsKey(val)) {
                keyIdx.put(val, size);
                idxKey.put(size++, val);
                return true;
            }
            return false;
        }

        public boolean remove(int val) {
            if (keyIdx.containsKey(val)) {
                int delIdx = keyIdx.get(val);
                int lastIdx = --size;
                int lastKey = idxKey.get(lastIdx);
                keyIdx.put(lastKey, delIdx);
                idxKey.put(delIdx, lastKey);
                keyIdx.remove(val);
                idxKey.remove(lastIdx);
                return true;
            }
            return false;
        }

        public int getRandom() {
            if (size == 0) {
                return -1;
            }
            int idx = (int) (Math.random() * size);
            return idxKey.get(idx);
        }
    }
}
