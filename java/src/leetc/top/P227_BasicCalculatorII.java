package leetc.top;

import java.util.LinkedList;

public class P227_BasicCalculatorII {
    private static void multiDiv(LinkedList<String> list, String str, char op) {
        if (list.isEmpty() || (list.peekLast().equals("+") || list.peekLast().equals("-"))) {
            list.addLast(str);
        } else {
            int num = Integer.parseInt(str);
            String preOp = list.pollLast();
            int preNum = Integer.parseInt(list.pollLast());
            if (preOp.equals("*")) {
                list.addLast(String.valueOf(preNum * num));
            } else {
                list.addLast(String.valueOf(preNum / num));
            }
        }
        list.addLast(String.valueOf(op));
    }

    private static int addSub(LinkedList<String> list) {
        int ans = Integer.parseInt(list.pollFirst());
        while (list.size() != 1) {
            String op = list.pollFirst();
            int cur = Integer.parseInt(list.pollFirst());
            if (op.equals("+")) {
                ans += cur;
            } else {
                ans -= cur;
            }
        }
        return ans;
    }

    public static int calculate(String s) {
        char[] str = s.toCharArray();
        LinkedList<String> list = new LinkedList<>();
        StringBuilder builder = new StringBuilder();
        builder.setLength(0);
        for (int i = 0; i < str.length; i++) {
            if (str[i] != ' ') {
                if (str[i] >= '0' && str[i] <= '9') {
                    builder.append(str[i]);
                } else {
                    multiDiv(list, builder.toString(), str[i]);
                    builder.setLength(0);
                }
            }
        }
        multiDiv(list, builder.toString(), ' ');
        return addSub(list);
    }
}
