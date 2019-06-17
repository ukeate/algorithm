package stack;

import org.junit.Test;

/**
 * Created by outrun on 2/22/16.
 */
public class NormalQueueTest {

    @Test
    public void testNormalQueue() {
        NormalQueue<Integer> queue = new NormalQueue<Integer>();
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);

        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
    }
}
