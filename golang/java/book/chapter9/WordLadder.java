package chapter9;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by outrun on 6/2/16.
 */
public class WordLadder {

    public static List<String> readWords(BufferedReader in) throws IOException {
        String oneLine;
        List<String> lst = new ArrayList<String>();

        while ((oneLine = in.readLine()) != null) {
            lst.add(oneLine);
        }

        return lst;
    }

    private static boolean oneCharOff(String word1, String word2) {
        if (word1.length() != word2.length()) {
            return false;
        }
        int diffs = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                if (++diffs > 1) {
                    return false;
                }
            }
        }

        return diffs == 1;
    }

    private static <KeyType> void update(Map<KeyType, List<String>> m, KeyType key, String value) {
        List<String> lst = m.get(key);
        if (lst == null) {
            lst = new ArrayList<String>();
            m.put(key, lst);
        }

        lst.add(value);
    }

    public static Map<String, List<String>> computeAdjacentWordsSlow(List<String> theWords) {
        Map<String, List<String>> adjWords = new HashMap<String, List<String>>();

        String[] words = new String[theWords.size()];

        theWords.toArray(words);
        for (int i = 0; i < words.length; i++) {
            for (int j = i + 1; j < words.length; j++) {
                if (oneCharOff(words[i], words[j])) {
                    update(adjWords, words[i], words[j]);
                    update(adjWords, words[j], words[i]);
                }
            }
        }

        return adjWords;
    }

    public static Map<String, List<String>> computeAdjacentWordsMedium(List<String> theWords) {

        Map<String, List<String>> adjWords = new HashMap<String, List<String>>();
        Map<Integer, List<String>> wordsByLength = new HashMap<Integer, List<String>>();

        for (String w : theWords) {
            update(wordsByLength, w.length(), w);
        }

        for (List<String> groupsWords : wordsByLength.values()) {
            String[] words = new String[groupsWords.size()];

            groupsWords.toArray(words);
            for (int i = 0; i < words.length; i++) {
                for (int j = i + 1; j < words.length; j++) {
                    if (oneCharOff(words[i], words[j])) {
                        update(adjWords, words[i], words[j]);
                        update(adjWords, words[j], words[i]);
                    }
                }
            }
        }

        return adjWords;
    }

    public static Map<String, List<String>> computeAdjacentWords(List<String> words) {
        Map<String, List<String>> adjWords = new HashMap<String, List<String>>();
        Map<Integer, List<String>> wordsByLength = new HashMap<Integer, List<String>>();

        for (String w : words) {
            update(wordsByLength, w.length(), w);
        }

        for (Map.Entry<Integer, List<String>> entry : wordsByLength.entrySet()) {
            List<String> groupsWords = entry.getValue();
            int groupNum = entry.getKey();

            for (int i = 0; i < groupNum; i++) {
                Map<String, List<String>> repToWord = new HashMap<String, List<String>>();

                for (String str : groupsWords) {
                    String rep = str.substring(0, i) + str.substring(i + 1);
                    update(repToWord, rep, str);
                }

                for (List<String> wordClique : repToWord.values()) {
                    if (wordClique.size() >= 2) {
                        for (String s1 : wordClique) {
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

    public static List<String> findMostChangeable(Map<String, List<String>> adjacentWords) {
        List<String> mostChangeableWords = new ArrayList<String>();
        int maxNumberOfAdjacentWords = 0;

        for (Map.Entry<String, List<String>> entry : adjacentWords.entrySet()) {
            List<String> changes = entry.getValue();

            if (changes.size() > maxNumberOfAdjacentWords) {
                maxNumberOfAdjacentWords = changes.size();
                mostChangeableWords.clear();
            }

            if (changes.size() == maxNumberOfAdjacentWords) {
                mostChangeableWords.add(entry.getKey());
            }
        }

        return mostChangeableWords;
    }

    public static void printMostChnageables(List<String> mostChangeable, Map<String, List<String>> adjacentWords) {
        for (String word : mostChangeable) {
            System.out.print(word + ":");
            List<String> adjacents = adjacentWords.get(word);
            for (String str : adjacents) {
                System.out.print(" " + str);
            }
            System.out.println("(" + adjacents.size() + " words)");
        }
    }

    public static void printHighChangeables(Map<String, List<String>> adjacentWords, int minWords) {
        for (Map.Entry<String, List<String>> entry : adjacentWords.entrySet()) {
            List<String> words = entry.getValue();

            if (words.size() >= minWords) {
                System.out.println(entry.getKey() + ")" + words.size() + "):");
                for (String w : words) {
                    System.out.print(" " + w);
                }
                System.out.println();
            }
        }
    }

    public static List<String> getChainFromPreviousMap(Map<String, String> prev, String first, String second) {
        LinkedList<String> result = new LinkedList<String>();

        if (prev.get(second) != null) {
            for (String str = second; str != null; str = prev.get(str)) {
                result.addFirst(str);
            }
        }

        return result;
    }

    public static List<String> findChain(Map<String, List<String>> adjacentWords, String first, String second) {
        Map<String, String> previousWord = new HashMap<String, String>();
        Queue<String> q = new LinkedList<String>();

        q.add(first);
        while (!q.isEmpty()) {
            String current = q.element();
            q.remove();

            List<String> adj = adjacentWords.get(current);

            if (adj != null) {
                for (String adjWord : adj) {
                    if (previousWord.get(adjWord) == null) {
                        previousWord.put(adjWord, current);
                        q.add(adjWord);
                    }
                }
            }
        }

        previousWord.put(first, null);
        return getChainFromPreviousMap(previousWord, first, second);
    }

    public static List<String> findChain(List<String> words, String first, String second) {

        Map<String, List<String>> adjacentWords = computeAdjacentWords(words);
        return findChain(adjacentWords, first, second);
    }

}
