package basic.c43;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FindKMajority {
    // 某数出现次数超过一半
    // 两两pk剩下的数
    public static void printHalfMajor(int[] arr) {
        int cand = 0;
        int hp = 0;
        for (int i = 0; i < arr.length; i++) {
            if (hp == 0) {
                cand = arr[i];
                hp = 1;
            } else if (arr[i] == cand) {
                hp++;
            } else {
                hp--;
            }
        }
        if (hp == 0) {
            System.out.println("no");
            return;
        }
        hp = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == cand) {
                hp++;
            }
        }
        if (hp > arr.length / 2) {
            System.out.println(cand);
        } else {
            System.out.println("no");
        }
    }

    public static void allMinusOne(HashMap<Integer, Integer> map) {
        List<Integer> removeList = new LinkedList<>();
        for (Map.Entry<Integer, Integer> set : map.entrySet()) {
            Integer key = set.getKey();
            Integer val = set.getValue();
            if (val == 1) {
                removeList.add(key);
            }
            map.put(key, val - 1);
        }
        for (Integer key : removeList) {
            map.remove(key);
        }
    }

    public static HashMap<Integer, Integer> getReals(int[] arr, HashMap<Integer, Integer> cands) {
        HashMap<Integer, Integer> reals = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            int cur = arr[i];
            if (cands.containsKey(cur)) {
                if (reals.containsKey(cur)) {
                    reals.put(cur, reals.get(cur) + 1);
                } else {
                    reals.put(cur, 1);
                }
            }
        }
        return reals;
    }

    // 某数出现次数超过n/k
    public static void printKMajor(int[] arr, int k) {
        if (k < 2) {
            System.out.println("k < 2");
            return;
        }
        HashMap<Integer, Integer> cands = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            if (cands.containsKey(arr[i])) {
                cands.put(arr[i], cands.get(arr[i] + 1));
            } else {
                if (cands.size() == k - 1) {
                    // 一次删掉k个不同的数
                    allMinusOne(cands);
                } else {
                    cands.put(arr[i], 1);
                }
            }
        }
        HashMap<Integer, Integer> reals = getReals(arr, cands);
        boolean hasPrint = false;
        for (Map.Entry<Integer, Integer> set : cands.entrySet()) {
            Integer key = set.getKey();
            if (reals.get(key) > arr.length / k) {
                hasPrint = true;
                System.out.print(key + " ");
            }
        }
        System.out.println(hasPrint ? "" : "no >k number");
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 1, 1, 2, 1};
        printHalfMajor(arr);
        int k = 4;
        printKMajor(arr, k);
    }
}
