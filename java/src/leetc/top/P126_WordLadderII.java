package leetc.top;

import java.util.*;

public class P126_WordLadderII {
    private static List<String> getNext(String word, HashSet<String> dict) {
        ArrayList<String> res = new ArrayList<>();
        char[] chs = word.toCharArray();
        for (char cur = 'a'; cur <= 'z'; cur++) {
            for (int i = 0; i < chs.length; i++) {
                if (chs[i] != cur) {
                    char tmp = chs[i];
                    chs[i] = cur;
                    if (dict.contains(String.valueOf(chs))) {
                        res.add(String.valueOf(chs));
                    }
                    chs[i] = tmp;
                }
            }
        }
        return res;
    }

    private static HashMap<String, List<String>> nexts(List<String> words) {
        HashSet<String> dict = new HashSet<>(words);
        HashMap<String, List<String>> nexts = new HashMap<>();
        for (int i = 0; i < words.size(); i++) {
            nexts.put(words.get(i), getNext(words.get(i), dict));
        }
        return nexts;
    }

    private static HashMap<String, Integer> distances(String start, HashMap<String, List<String>> nexts) {
        HashMap<String, Integer> distances = new HashMap<>();
        distances.put(start, 0);
        Queue<String> que = new LinkedList<>();
        que.add(start);
        HashSet<String> set = new HashSet<>();
        set.add(start);
        while (!que.isEmpty()) {
            String cur = que.poll();
            for (String next : nexts.get(cur)) {
                if (!set.contains(next)) {
                    distances.put(next, distances.get(cur) + 1);
                    que.add(next);
                    set.add(next);
                }
            }
        }
        return distances;
    }

    private static void paths(String cur, String to, HashMap<String, List<String>> nexts,
                              HashMap<String, Integer> fromDistances, HashMap<String, Integer> toDistances,
                              LinkedList<String> path, List<List<String>> res) {
        path.add(cur);
        if (to.equals(cur)) {
            res.add(new LinkedList<>(path));
        } else {
            for (String next : nexts.get(cur)) {
                if (fromDistances.get(next) == fromDistances.get(cur) + 1
                    && toDistances.get(next) == toDistances.get(cur) - 1) {
                    paths(next, to, nexts, fromDistances, toDistances, path, res);
                }
            }
        }
        path.pollLast();
    }

    public static List<List<String>> findLadders(String start, String end, List<String> list) {
        list.add(start);
        HashMap<String, List<String>> nexts = nexts(list);
        HashMap<String, Integer> fromDistances = distances(start, nexts);
        List<List<String>> res = new ArrayList<>();
        if (!fromDistances.containsKey(end)) {
            return res;
        }
        HashMap<String, Integer> toDistances = distances(end, nexts);
        LinkedList<String> pathList = new LinkedList<>();
        paths(start, end, nexts, fromDistances, toDistances, pathList, res);
        return res;
    }
}
