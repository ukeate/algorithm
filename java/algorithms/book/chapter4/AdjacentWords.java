package chapter4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by outrun on 5/16/16.
 */
public class AdjacentWords {

    public static Map<String, List<String>> computeAdjacentWords(List<String> words) {

        Map<String, List<String>> adjWords = new TreeMap<String, List<String>>();
        Map<Integer, List<String>> wordsByLength = new TreeMap<Integer, List<String>>();

        for (String w : words) {
            update(wordsByLength, w.length(), w);
        }

        for (Map.Entry<Integer, List<String>> entry : wordsByLength.entrySet()) {

            List<String> groupsWords = entry.getValue();
            int groupNum = entry.getKey();

            for (int i = 0; i < groupNum; i++) {
                Map<String, List<String>> repToWord = new TreeMap<String, List<String>>();

                for (String str : groupsWords) {
                    String rep = str.substring(0, i) + str.substring(i + 1);
                    update(repToWord, rep, str);
                }

                for (List<String> wordClique : repToWord.values()) {
                    if (wordClique.size() >= 2) {
                        for (String s1: wordClique) {
                            for (String s2 : wordClique) {
                                if (s1 != s2) {
                                    update(adjWords, s1, s2);
                                }
                            }
                        }
                    }
                }
            }
        }

        return adjWords;
    }

    private static <KeyType> void update (Map<KeyType, List<String>> m, KeyType key, String value) {
        List<String> lst = m.get(key);
        if (lst == null) {
            lst = new ArrayList<String>();
            m.put(key, lst);
        }

        lst.add(value);
    }
}
