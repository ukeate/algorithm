package leetc.top;

import java.util.*;

public class P127_WordLadder {
    private static ArrayList<String> next(String word, HashSet<String> dict) {
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

    private static HashMap<String, ArrayList<String>> nexts(List<String> words) {
        HashSet<String> dict = new HashSet<>(words);
        HashMap<String, ArrayList<String>> nexts = new HashMap<>();
        for (int i = 0; i < words.size(); i++) {
            nexts.put(words.get(i), next(words.get(i), dict));
        }
        return nexts;
    }

    public static int ladderLength(String start, String to, List<String> list) {
        list.add(start);
        HashMap<String, ArrayList<String>> nexts = nexts(list);
        HashMap<String, Integer> distanceMap = new HashMap<>();
        distanceMap.put(start, 1);
        HashSet<String> set = new HashSet<>();
        set.add(start);
        Queue<String> que = new LinkedList<>();
        que.add(start);
        while (!que.isEmpty()) {
            String cur = que.poll();
            Integer distance = distanceMap.get(cur);
            for (String next : nexts.get(cur)) {
                if (next.equals(to)) {
                    return distance + 1;
                }
                if (!set.contains(next)) {
                    set.add(next);
                    que.add(next);
                    distanceMap.put(next, distance + 1);
                }
            }
        }
        return 0;
    }

    //

    public static int ladderLength2(String start, String end, List<String> words) {
        HashSet<String> dict = new HashSet<>(words);
        if (!dict.contains(end)) {
            return 0;
        }
        HashSet<String> startSet = new HashSet<>();
        HashSet<String> endSet = new HashSet<>();
        HashSet<String> visit = new HashSet<>();
        startSet.add(start);
        endSet.add(end);
        for (int len = 2; !startSet.isEmpty(); len++) {
            HashSet<String> nextSet = new HashSet<>();
            for (String w : startSet) {
                for (int j = 0; j < w.length(); j++) {
                    char[] ch = w.toCharArray();
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c != w.charAt(j)) {
                            ch[j] = c;
                            String next = String.valueOf(ch);
                            if (endSet.contains(next)) {
                                return len;
                            }
                            if (dict.contains(next) && !visit.contains(next)) {
                                nextSet.add(next);
                                visit.add(next);
                            }
                        }
                    }
                }
            }
            startSet = (nextSet.size() < endSet.size()) ? nextSet : endSet;
            endSet = (startSet == nextSet) ? endSet : nextSet;
        }
        return 0;
    }
}
