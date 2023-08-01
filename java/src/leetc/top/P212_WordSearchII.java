package leetc.top;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class P212_WordSearchII {
    public static class TrieNode {
        public TrieNode[] nexts;
        public int pass;
        public int end;
        public TrieNode() {
            nexts = new TrieNode[26];
            pass = 0;
            end = 0;
        }
    }

    private static void fillWord(TrieNode node, String word) {
        node.pass++;
        char[] chs = word.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            int path = chs[i] - 'a';
            if (node.nexts[path] == null) {
                node.nexts[path] = new TrieNode();
            }
            node = node.nexts[path];
            node.pass++;
        }
        node.end++;
    }

    private static String path(LinkedList<Character> path) {
        char[] str = new char[path.size()];
        int idx = 0;
        for (Character cha : path) {
            str[idx++] = cha;
        }
        return String.valueOf(str);
    }

    private static int process(char[][] board, int row, int col, LinkedList<Character> path, TrieNode cur, List<String> ans) {
        char cha = board[row][col];
        if (cha == 0) {
            return 0;
        }
        int idx = cha - 'a';
        if (cur.nexts[idx] == null || cur.nexts[idx].pass == 0) {
            return 0;
        }
        cur = cur.nexts[idx];
        path.addLast(cha);
        int fix = 0;
        if (cur.end > 0) {
            ans.add(path(path));
            cur.end--;
            fix++;
        }
        board[row][col] = 0;
        if (row > 0) {
            fix += process(board, row - 1, col, path, cur, ans);
        }
        if (row < board.length - 1) {
            fix += process(board, row + 1, col, path, cur, ans);
        }
        if (col > 0) {
            fix += process(board, row, col - 1, path, cur, ans);
        }
        if (col < board[0].length - 1) {
            fix += process(board, row, col + 1, path, cur, ans);
        }
        board[row][col] = cha;
        path.pollLast();
        cur.pass -= fix;
        return fix;
    }

    public List<String> findWords(char[][] board, String[] words) {
        TrieNode head = new TrieNode();
        HashSet<String> set = new HashSet<>();
        for (String word : words) {
            if (!set.contains(word)) {
                fillWord(head, word);
                set.add(word);
            }
        }
        List<String> ans = new ArrayList<>();
        LinkedList<Character> path = new LinkedList<>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                process(board, row, col, path, head, ans);
            }
        }
        return ans;
    }
}
