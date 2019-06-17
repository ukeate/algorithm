package stack;

import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by outrun on 2/22/16.
 */
public class Methods {

    /**
     * 判断是否合法弹出序列
     *
     * @param pushSequence
     * @param popSequence
     * @return
     */
    public boolean pushAndPop(Integer[] pushSequence, Integer[] popSequence) {
        Stack<Integer> stack = new Stack<Integer>();
        int i = 0;
        for (Integer k : popSequence) {
            if (!stack.isEmpty() && stack.peek().equals(k)) {
                stack.pop();
            } else {
                while (true) {
                    if (i >= pushSequence.length) {
                        return false;
                    }
                    if (pushSequence[i].equals(k)) {
                        i++;
                        break;
                    } else {
                        stack.push(pushSequence[i]);
                        i++;
                    }
                }
            }
        }
        return true;
    }

    @Test
    public void testPushAndPop() {
        Integer[] push = {1, 2, 3, 4, 5};
        Integer[] pop1 = {4, 5, 3, 2, 1};
        Integer[] pop2 = {4, 3, 5, 1, 2};
        Integer[] pop3 = {3, 5, 4, 2, 1};
        System.out.println(pushAndPop(push, pop1));
        System.out.println(pushAndPop(push, pop2));
        System.out.println(pushAndPop(push, pop3));
    }

    /**
     * 时间复杂度n, 空间复杂度n
     *
     * @param s
     * @return
     */
    public boolean parentheses(String s) {
        if (s.length() % 2 != 0) {
            return false;
        }
        Stack<Character> stack = new Stack<Character>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '(':
                case '{':
                case '[':
                    stack.push(c);
                    break;
                case ')':
                    if (stack.isEmpty() || stack.peek() != '(') {
                        return false;
                    } else {
                        stack.pop();
                    }
                    break;
                case '}':
                    if (stack.isEmpty() || stack.peek() != '{') {
                        return false;
                    } else {
                        stack.pop();
                    }
                    break;
                case ']':
                    if (stack.isEmpty() || stack.peek() != '[') {
                        return false;
                    } else {
                        stack.pop();
                    }
                    break;
                default:
                    break;
            }
        }
        if (stack.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Test
    public void testParentheses() {
        String s1 = "{([]})";
        String s2 = "{()}[]";
        System.out.println(parentheses(s1));
        System.out.println(parentheses(s2));
    }

    public String simplifyPath(String path) {
        LinkedList<String> stack = new LinkedList<String>();
        String[] arr = path.split("/");
        for (String str : arr) {
            if (str.equals("") || str.equals(".")) {
                continue;
            } else {
                if (str.equals("..")) {
                    if (!stack.isEmpty()) {
                        stack.pop();
                    }
                } else {
                    stack.push(str);
                }
            }
        }
        if (stack.isEmpty()) {
            return "/";
        } else {
            Collections.reverse(stack);
            StringBuilder sb = new StringBuilder();
            while (!stack.isEmpty()) {
                sb.append("/").append(stack.pop());
            }
            return sb.toString();
        }
    }

    @Test
    public void testSimplifyPath() {
        System.out.println(simplifyPath("/../../"));
        System.out.println(simplifyPath("/home//foo//"));
        System.out.println(simplifyPath("a/./b/../../../c/d/"));
    }
}
