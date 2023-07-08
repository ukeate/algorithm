package basic.c35;

import java.util.*;

// 输入活动收益、活动时间，活动依赖图。参加活动就要按依赖做到最后
public class MaxRevenue {
    public static int[] max(int allTime, int[] revenue, int[] times, int[][] dependents) {
        int size = revenue.length;
        HashMap<Integer, ArrayList<Integer>> parents = new HashMap<>();
        for (int i = 0; i < size; i++) {
            parents.put(i, new ArrayList<>());
        }
        int end = -1;
        for (int i = 0; i < dependents.length; i++) {
            boolean allZero = true;
            for (int j = 0; j < dependents[0].length; j++) {
                if (dependents[i][j] != 0) {
                    parents.get(j).add(i);
                    allZero = false;
                }
            }
            if (allZero) {
                end = i;
            }
        }
        HashMap<Integer, TreeMap<Integer, Integer>> actMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            actMap.put(i, new TreeMap<>());
        }
        actMap.get(end).put(times[end], revenue[end]);
        LinkedList<Integer> que = new LinkedList<>();
        que.add(end);
        while (!que.isEmpty()) {
            int cur = que.poll();
            for (int last : parents.get(cur)) {
                for (Map.Entry<Integer, Integer> entry : actMap.get(cur).entrySet()) {
                    int lastCost = entry.getKey() + times[last];
                    int lastRevenue = entry.getValue() + revenue[last];
                    TreeMap<Integer, Integer> lastMap = actMap.get(last);
                    if (lastMap.floorKey(lastCost) == null || lastMap.get(lastMap.floorKey(lastCost)) < lastRevenue) {
                        lastMap.put(lastCost, lastRevenue);
                    }
                }
                que.add(last);
            }
        }
        TreeMap<Integer, Integer> allMap = new TreeMap<>();
        for (TreeMap<Integer, Integer> curMap : actMap.values()) {
            for (Map.Entry<Integer, Integer> entry : curMap.entrySet()) {
                int t = entry.getKey();
                int r = entry.getValue();
                if (allMap.floorKey(t) == null || allMap.get(allMap.floorKey(t)) < r) {
                    allMap.put(t, r);
                }
            }
        }
        return new int[]{allMap.floorKey(allTime), allMap.get(allMap.floorKey(allTime))};
    }

    public static void main(String[] args) {
        int allTime = 10;
        int[] revenue = {2000, 4000, 2500, 1600, 3800, 2600, 4000, 3500};
        int[] times = {3, 3, 2, 1, 4, 2, 4, 3};
        int[][] dependents = {
                {0, 1, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0}};
        int[] res = max(allTime, revenue, times, dependents);
        System.out.println(res[0] + "," + res[1]);
    }
}
