package basic.c44;

import java.util.*;

// https://leetcode.com/problems/the-skyline-problem/
// [左边界,右边界,高度], 输入大楼数组, 输出轮廓线
public class BuildingOutline {
    private static class Op {
        public int x;
        public boolean isAdd;
        public int h;
        public Op(int x, boolean isAdd, int h) {
            this.x = x;
            this.isAdd = isAdd;
            this.h = h;
        }
    }

    private static class Comp implements Comparator<Op> {
        @Override
        public int compare(Op o1, Op o2) {
            if (o1.x != o2.x) {
                return o1.x - o2.x;
            }
            if (o1.isAdd != o2.isAdd) {
                return o1.isAdd ? -1 : 1;
            }
            return 0;
        }
    }

    public static List<List<Integer>> outline(int[][] matrix) {
        int n = matrix.length;
        Op[] ops = new Op[n << 1];
        for (int i = 0; i < matrix.length; i++) {
            ops[i * 2] = new Op(matrix[i][0], true, matrix[i][2]);
            ops[i * 2 + 1] = new Op(matrix[i][1], false, matrix[i][2]);
        }
        Arrays.sort(ops, new Comp());
        // <height, times>
        TreeMap<Integer, Integer> ht = new TreeMap<>();
        // <x点,height>
        TreeMap<Integer, Integer> xh = new TreeMap<>();
        for (int i = 0; i < ops.length; i++) {
            if (ops[i].isAdd) {
                if (!ht.containsKey(ops[i].h)) {
                    ht.put(ops[i].h, 1);
                } else {
                    ht.put(ops[i].h, ht.get(ops[i].h) + 1);
                }
            } else {
                if (ht.get(ops[i].h) == 1) {
                    ht.remove(ops[i].h);
                } else {
                    ht.put(ops[i].h, ht.get(ops[i].h) - 1);
                }
            }
            if (ht.isEmpty()) {
                xh.put(ops[i].x, 0);
            } else {
                xh.put(ops[i].x, ht.lastKey());
            }
        }
        List<List<Integer>> res = new ArrayList<>();
        int preX = 0;
        int preH = 0;
        for (Map.Entry<Integer, Integer> entry : xh.entrySet()) {
            int x = entry.getKey();
            int h = entry.getValue();
            if (preH != h) {
                if (preH != 0) {
                    res.add(new ArrayList<>(Arrays.asList(preX, x, preH)));
                }
                preX = x;
                preH = h;
            }
        }
        return res;
    }

}
