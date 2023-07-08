package basic.c41;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

// 在矩阵中，word列表中哪些可以走出来
public class WordPath {
    public static class Node {
        public Node[] nexts;
        public int pass;
        public int end;
        public Node() {
            nexts = new Node[26];
            pass = 0;
            end = 0;
        }
    }

    private static void fillWord(Node head, String word) {
        head.pass++;
        char[] s = word.toCharArray();
        int idx = 0;
        Node node = head;
        for (int i = 0; i < s.length; i++) {
            idx = s[i] - 'a';
            if (node.nexts[idx] == null) {
                node.nexts[idx] = new Node();
            }
            node = node.nexts[idx];
            node.pass++;
        }
        node.end++;
    }

    private static String path(LinkedList<Character> path) {
        char[] str = new char[path.size()];
        int idx = 0;
        for (Character ch : path){
            str[idx++] = ch;
        }
        return String.valueOf(str);
    }

    private static int process(char[][] board, int row, int col, LinkedList<Character> path, Node cur, List<String> ans) {
        char ch = board[row][col];
        // 标记为走过的
        if (ch == 0) {
            return 0;
        }
        int idx = ch - 'a';
        if (cur.nexts[idx] == null || cur.nexts[idx].pass == 0) {
            return 0;
        }
        cur = cur.nexts[idx];
        path.addLast(ch);
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
        cur.pass -= fix;
        board[row][col] = ch;
        path.pollLast();
        return fix;
    }

    public static List<String> find(char[][] board, String[] words) {
        Node head = new Node();
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
