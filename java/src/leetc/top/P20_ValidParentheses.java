package leetc.top;

import java.util.Stack;

public class P20_ValidParentheses {
    public static boolean isValid(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }
        char[] str = s.toCharArray();
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < str.length; i++) {
            char c = str[i];
            if (c == '(' || c == '[' || c == '{') {
                stack.add(c);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }
                char cc = stack.pop();
                if ((c == ')' && cc != '(') || (c == ']' && cc != '[') || (c == '}' && cc != '{')) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }
}
