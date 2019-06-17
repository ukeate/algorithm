package stack;

import org.junit.Test;

/**
 * Created by outrun on 2/22/16.
 */
public class NormalStackTest {

    @Test
    public void testSqStack() {

        NormalStack<Integer> stack = new NormalStack<Integer>();
        Integer[] arr = {1, 2, 3};
        for (Integer k : arr) {
            stack.push(k);
        }
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }
}
