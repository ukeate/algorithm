package base.recursive;

import java.util.HashSet;
import java.util.Stack;

public class Hanoi {
    private static void f(int n, String from, String to, String other) {
        if (n == 1) {
            System.out.println("Move 1 from " + from + " to " + to);
            return;
        }
        f(n - 1, from, other, to);
        System.out.println("Move " + n + " from " + from + " to " + to);
        f(n - 1, other, to, from);
    }

    public static void hanoi2(int n) {
        if (n < 1) {
            return;
        }
        f(n, "left", "right", "mid");
    }

    //

    private static class Record {
        public int level;
        public String from;
        public String to;
        public String other;

        public Record(int l, String f, String t, String o) {
            level = l;
            from = f;
            to = t;
            other = o;
        }
    }

    public static void hanoi3(int n) {
        if (n < 1) {
            return;
        }
        Stack<Record> stack = new Stack<>();
        HashSet<Record> finishLeft = new HashSet<>();
        stack.add(new Record(n, "left", "right", "mid"));
        while (!stack.isEmpty()) {
            Record cur = stack.pop();
            if (cur.level == 1) {
                System.out.println("Move 1 from " + cur.from + " to " + cur.to);
            } else {
                if (!finishLeft.contains(cur)) {
                    finishLeft.add(cur);
                    stack.push(cur);
                    stack.push(new Record(cur.level - 1, cur.from, cur.other, cur.to));
                } else {
                    System.out.println("Move " + cur.level + " from " + cur.from + " to " + cur.to);
                    stack.push(new Record(cur.level - 1, cur.other, cur.to, cur.from));
                }
            }
        }
    }

    public static void main(String[] args) {
        int n = 5;
        hanoi2(n);
        hanoi3(n);
    }
}
