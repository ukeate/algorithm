package basic.c35;

import java.util.*;

// 字符串变换，只使用list中的步骤, 返回最小步数
public class WordMinPaths {
    private static ArrayList<String> getNext(String word, Set<String> dict) {
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

    private static HashMap<String, ArrayList<String>> getNexts(List<String> words) {
        Set<String> dict = new HashSet<>(words);
        HashMap<String, ArrayList<String>> nexts = new HashMap<>();
        for (int i = 0; i < words.size(); i++) {
            nexts.put(words.get(i), getNext(words.get(i), dict));
        }
        return nexts;
    }

    private static HashMap<String, Integer> getDistances(String start, HashMap<String, ArrayList<String>> nexts) {
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

    private static void minPath(String cur, String to, HashMap<String, ArrayList<String>> nexts, HashMap<String, Integer> distances, LinkedList<String> path, List<List<String>> res) {
        path.add(cur);
        if (to.equals(cur)) {
            res.add(new LinkedList<>(path));
        } else {
            for (String next : nexts.get(cur)) {
                if (distances.get(next) == distances.get(cur) + 1) {
                    minPath(next, to, nexts, distances, path, res);
                }
            }
        }
        path.pollLast();
    }


    public static List<List<String>> min(String start, String end, List<String> list) {
        list.add(start);
        HashMap<String, ArrayList<String>> nexts = getNexts(list);
        HashMap<String, Integer> distances = getDistances(start, nexts);
        LinkedList<String> pathList = new LinkedList<>();
        List<List<String>> res = new ArrayList<>();
        minPath(start, end, nexts, distances, pathList, res);
        return res;
    }

    public static void main(String[] args) {
        String start = "abc";
        String end = "cab";
        String[] listArr = {"abc", "cab", "acc", "cbc", "ccc", "cac", "cbb", "aab", "abb"};
        List<String> list = new ArrayList<>();
        for (int i = 0; i < listArr.length; i++) {
            list.add(listArr[i]);
        }
        List<List<String>> res = min(start, end, list);
        for (List<String> obj : res) {
            for (String str : obj) {
                System.out.print(str + " -> ");
            }
            System.out.println();
        }
    }
}
