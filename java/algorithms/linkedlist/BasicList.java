package linkedlist;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Stack;

public class BasicList<T> {

    private Node<T> head = new Node<T>(null, null);

    public Comparator<T> comp;

    @SuppressWarnings("unchecked")
    public int compare(T a, T b) {
        if (comp != null) {
            return comp.compare(a, b);
        } else {
            Comparable<T> c = (Comparable<T>) a;
            return c.compareTo(b);
        }
    }

    public BasicList(T[] arr) {
        Node<T> node, pre = head;
        for (T t : arr) {
            node = new Node<T>(t, null);
            pre.next = node;
            pre = node;
        }
    }

    public void toCircle() {
        Node<T> p = head.next;
        while (p.next != null) {
            p = p.next;
        }
        p.next = head.next;
    }

    public static Node reverseList(Node head) {
        Node pre = head;
        Node p = pre.next;
        Node next;
        while (p != null) {
            next = p.next;
            p.next = pre;

            pre = p;
            p = next;
        }
        head.next = null;
        return pre;
    }

    public void print() {
        Node<T> node = head.next;
        while (node.next != null) {
            System.out.println(node.value);
            node = node.next;
        }
        System.out.println(node.value);
    }

    public static void print(Node head) {
        Node node = head.next;
        while (node.next != null) {
            System.out.println(node.value);
            node = node.next;
        }
        System.out.println(node.value);
    }

    /**
     * 时间复杂度为n, 空间复杂度为n
     */
    public void printInverse() {
        Node<T> node = head.next;
        Stack<T> stack = new Stack<T>();
        while (node.next != null) {
            stack.add(node.value);
            node = node.next;
        }
        stack.add(node.value);
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }

    private void printRecursive(Node<T> node) {
        if (node.next != null) {
            printRecursive(node.next);
        }
        System.out.println(node.value);
    }

    /**
     * 时间复杂度为n, 空间复杂度为n
     */
    public void printInverse2() {
        printRecursive(head.next);
    }

    private Node<T> moveTo(int ind) {
        Node<T> node = head;
        for (int i = 0; i <= ind; i++) {
            if (node.next == null) {
                break;
            } else {
                node = node.next;
            }
        }
        return node;
    }

    public void insert(int ind, T val) {
        Node<T> node = moveTo(ind), node2Ins = new Node<T>(val, null);

        node2Ins.next = node.next;
        node.next = node2Ins;

    }

    public void remove(int ind) {
        Node<T> preNode = moveTo(ind - 1), afterNode = preNode.next.next;
        preNode.next = afterNode;
    }

    public T max() {
        Node<T> node = head.next;
        T max = node.value;
        node = node.next;
        while (node != null) {
            if (compare(node.value, max) > 0) {
                max = node.value;
            }
            node = node.next;
        }
//		if (compare(node.value, max) > 0) {
//			max = node.value;
//		}
        return max;
    }

    public void reverse() {
        Node<T> first = head.next, pre = first, p = pre.next, next;
        while (p != null) {
            next = p.next;
            p.next = pre;

            pre = p;
            p = next;
        }
        first.next = null;
        head.next = pre;
    }

    private Node<T> reverseRecursive(Node<T> p) {
        if (p.next == null) {
            return p;
        } else {
            Node<T> next = p.next;
            Node<T> tail = reverseRecursive(next);
            next.next = p;
            return tail;
        }
    }

    /**
     * 时间复杂度n, 空间复杂度n
     */
    public void reverse2() {
        Node<T> first = head.next;
        Node<T> tail = reverseRecursive(first);
        first.next = null;
        head.next = tail;
    }
    // @Test
    // public void testHere(){
    // BasicList<Integer> list = new BasicList<Integer>(new Integer []{1, 2,
    // 3});
    // list.print();
    // }

    public int lenth() {
        int len = 0;
        Node<T> p = head.next;
        while (p != null) {
            len++;
            p = p.next;
        }
        return len;
    }

    public static int lenth(Node head) {
        int len = 0;
        Node p = head.next;
        while (p != null) {
            len++;
            p = p.next;
        }
        return len;
    }

    /**
     * one pass
     *
     * @param n
     * @return
     */
    public Node<T> findReverse(int n) {
        if (head == null) {
            return head;
        } else {
            int len = lenth();
            Node<T> p = head;
            for (int i = 1; i <= len - n; i++) {
                p = p.next;
            }
            return p;
        }
    }

    public Node<T> findReverse2(int n) {
        if (head == null) {
            return head;
        } else {
            Node<T> p1 = head, p2 = head;
            for (int i = 1; i <= n; i++) {
                p2 = p2.next;
            }
            while (p2 != null) {
                p2 = p2.next;
                p1 = p1.next;
            }
            return p1;
        }

    }

    /**
     * 时间复杂度n, 空间复杂度1
     *
     * @param val
     */
    public void removeVal(T val) {

        Node<T> pre = head;
        Node<T> p = head.next;
        while (p != null) {
            if (p.value == val) {
                pre.next = p.next;
                p = p.next;
            } else {
                pre = p;
                p = p.next;
            }
        }
    }

    /**
     * 时间复杂度n, 空间复杂度1
     */
    public void sortedListRemoveDuplicatedVal() {
        Node<T> pre = head;
        Node<T> p = head.next;
        while (p != null) {
            if (pre.value == p.value) {
//                pre.next = p.next;
//                p = p.next;
                while (p != null && pre.value == p.value) {
                    p = p.next;
                }
                pre.next = p;
            } else {
                pre = p;
                p = p.next;
            }
        }
    }

    /**
     * 时间复杂度n, 空间复杂度1
     */
    public void sortedListRemoveDuplicatedVal2() {
        Node<T> pre = head;
        Node<T> p = head.next;
        Node<T> next = null;
        while (p != null && p.next != null) {
            next = p.next;
            if (p.value == next.value) {
                while (next != null && p.value == next.value) {
                    next = next.next;
                }
                p = next;
                pre.next = next;
            } else {
                pre = p;
                p = next;
            }
        }
    }

    /**
     * 时间复杂度n, 空间复杂度1
     *
     * @param m
     * @param n
     */
    public void reverseBetween(int m, int n) {
        if (m == n) {
            return;
        } else {
            Node<T> first = head;
            int k = 1;
            while (k < m) {
                first = first.next;
                k++;
            }
            // first -> m - 1
            // k -> m
            Node<T> pre = first.next;
            Node<T> p = pre.next;
            Node<T> next = null;
            // top -> m
            final Node<T> top = pre;
            // reverse m to n
            while (k < n) {
                next = p.next;

                p.next = pre;
                pre = p;
                p = next;

                k++;
            }
            // k -> n
            // pre -> n
            // p -> n + 1
            top.next = p;
            first.next = pre;
        }
    }

    /**
     * 从倒数第k个节点旋转
     * 时间复杂度n, 空间复杂度1
     *
     * @param k
     */
    public void rotateRight(int k) {
        int len = lenth();
        if (k >= len) {
            k = k % len;
        }
        if (k == 0) {
            return;
        }
        Node<T> first = head.next;
        Node<T> pre = first;
        int index = 1;
        while (index < len - k) {
            pre = pre.next;
            index++;
        }
        // pre -> n - k
        // index -> n - k + 1
        Node<T> newFirst = pre.next;
        Node<T> last = newFirst;
        while (last.next != null) {
            last = last.next;
        }

        pre.next = null;
        last.next = first;
        head.next = newFirst;
    }

    /**
     * 时间复杂度n, 空间复杂度1
     *
     * @return
     */
    public boolean isPalindrome() {
        int len = lenth();
        int half = len / 2;
        Node<T> leftEnd = head.next;
        for (int i = 0; i < half - 1; i++) {
            leftEnd = leftEnd.next;
        }
        Node<T> rightStart = leftEnd.next;
        if (len % 2 != 0) {
            rightStart = rightStart.next;
        }
        rightStart = reverseList(rightStart);

        Node<T> leftStart = head.next;
        for (int i = 1; i <= half; i++) {
            if (leftStart.value != rightStart.value) {
                return false;
            }
            leftStart = leftStart.next;
            rightStart = rightStart.next;
        }
        return true;
    }

    /**
     * 时间复杂度n, 空间复杂度1
     */
    public void swapPairs() {
        Node<T> zero = head;
        Node<T> pre = head.next;
        Node<T> p = pre.next;
        Node<T> next;
        while (pre != null && p != null) {
            next = p.next;

            p.next = pre;
            pre.next = next;
            zero.next = p;

            if (next == null) {
                break;
            } else {
                zero = pre;
                pre = next;
                p = pre.next;
            }
        }
    }

    /**
     * 时间复杂度n, 空间复杂度1
     *
     * @param x
     */
    public void partition(int x) {
        Node<T> leftHead = new Node<T>();
        Node<T> leftTail = leftHead;
        Node<T> rightHead = new Node<T>();
        Node<T> rightTail = rightHead;
        Node<T> p = head.next;
        while (p != null) {
            if ((Integer) p.value < x) {
                leftTail.next = p;
                leftTail = p;
            } else {
                rightTail.next = p;
                rightTail = p;
            }
            p = p.next;
        }
        leftTail.next = rightHead.next;
        head.next = leftHead.next;
    }

    /**
     * 洗牌
     * 空间复杂度1
     */
    public void reorder() {
        int len = lenth();
        int half = len / 2;
        if (len % 2 != 0) {
            half++;
        }
        Node<T> leftEnd = head.next;
        for (int i = 1; i < half; i++) {
            leftEnd = leftEnd.next;
        }
        Node<T> rightStart = leftEnd.next;
        rightStart = reverseList(rightStart);
        leftEnd.next = null;

        Node<T> left = head.next;
        Node<T> right = rightStart;
        boolean flag = true;
        Node<T> next = null;
        while (right != null) {
            if (flag) {
                next = left.next;
                left.next = right;
                left = next;
            } else {
                next = right.next;
                right.next = left;
                right = next;
            }
            flag = !flag;
        }
    }

    /**
     * 约瑟夫环
     * 时间复杂度n * step, 空间复杂度n
     */
    public void josephusCircle(int start, int step) {
        int len = lenth();
        toCircle();

        Node<T> startNode = head.next;
        for (int i = 1; i < start; i++) {
            startNode = startNode.next;
        }

        Node<T> pre = startNode;
        for (int i = 1; i <= len; i++) {
            for (int t = 1; t < step - 1; t++) {
                pre = pre.next;
            }
            System.out.println(pre.next.value);
            pre.next = pre.next.next;
            pre = pre.next;
        }
        pre.next = null;
    }

    public static Node[] array2Intersection(int[] arr1, int[] arr2, int interIndex) {
        Node head1 = new Node<Integer>(0);
        Node head2 = new Node<Integer>(0);
        Node p1 = head1;
        Node p2 = head2;
        int m = arr1.length;
        Node intersection = null;
        for (int i = 0; i < m; i++) {
            p1.next = new Node<Integer>(arr1[i]);
            p1 = p1.next;
            if (i == interIndex - 1) {
                intersection = p1;
            }
        }
        int n = arr2.length;
        for (int i = 0; i < n; i++) {
            p2.next = new Node<Integer>(arr2[i]);
            p2 = p2.next;
        }
        p2.next = intersection;
        Node[] arrs = {head1, head2};
        return arrs;
    }


    /**
     * 时间复杂度m*n, 空间复杂度1
     *
     * @param head1
     * @param head2
     * @return
     */
    public static Node getIntersectionNodeBruteForce(Node head1, Node head2) {
        for (Node p = head1; p != null; p = p.next) {
            for (Node q = head2; q != null; q = q.next) {
                if (p == q) {
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * 时间复杂度 m+n 到 m*sqrt(n)之间
     * 空间复杂度n
     * 哈希表，空间换时间
     *
     * @param head1
     * @param head2
     * @return
     */
    public static Node getIntersectionNodeHash(Node head1, Node head2) {
        HashSet<Node> hashSet = new HashSet<Node>();
        for (Node p = head2; p != null; p = p.next) {
            hashSet.add(p);
        }
        for (Node p = head1; p != null; p = p.next) {
            if (hashSet.contains(p)) {// contains 时间复杂度不只1,所以有sqrt(n)
                return p;
            }
        }
        return null;
    }

    /**
     * 时间复杂度max(m,n), 空间复杂度1
     *
     * @param head1
     * @param head2
     * @return
     */
    public static Node getIntersection(Node head1, Node head2) {
        int m = lenth(head1);
        int n = lenth(head2);
        int k;
        Node p = head1;
        Node q = head2;
        if (m > n) {
            k = m - n;
            for (int i = 1; i <= k; i++) {
                p = p.next;
            }
        } else if (m < n) {
            k = n - m;
            for (int i = 1; i <= k; i++) {
                q = q.next;
            }
        }
        while (p != null && q != null) {
            if (p == q) {
                return p;
            } else {
                p = p.next;
                q = q.next;
            }
        }
        return null;
    }

    public static Node arr2cycle(int[] arr, int index) {
        Node head = new Node<Integer>();
        Node p = head;
        int n = arr.length;
        Node startNode = null;
        for (int i = 0; i < n; i++) {
            p.next = new Node<Integer>(arr[i]);
            p = p.next;
            if (i == index - 1) {
                startNode = p;
            }
        }
        p.next = startNode;
        return head;
    }

    /**
     * 时间复杂度n, 空间复杂度1
     *
     * @param head
     * @return
     */
    public static boolean hasCycle(Node head) {
        Node slow = head;
        Node fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                return true;
            }
        }
        return false;

    }

    /**
     * 相遇点到交点距离与头结点到交点的距离相等
     * 时间复杂度n, 空间复杂度1
     *
     * @param head
     * @return
     */
    public static Node detectCycleNode(Node head) {
        Node slow = head;
        Node fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                break;
            }
        }
        if (fast == null || fast.next == null) {
            return null;
        } else {
            slow = head;
            while (fast != slow) {
                fast = fast.next;
                slow = slow.next;
            }
            return fast;
        }

    }

    public Node<T> findDuplicateValueHash() {
        HashSet<T> hashSet = new HashSet<T>();
        Node<T> p = head.next;
        while (p != null) {

            if (!hashSet.contains(p.value)) {
                hashSet.add(p.value);
            } else {
                return p;
            }
            p = p.next;
        }
        return null;
    }

    /**
     *
     * 数组环, 从数组最后元素开始, 不断找到数组值对应index的，重复index时, 该index的数组值也为重复值
     * 时间复杂度n, 空间复杂度1
     * @return
     */
    public static int findDuplicateValueCircle(Integer[] nums) {
        int n = nums.length;
        // 转换成求"链表环交点问题"
        int fast = n - 1;
        int slow = n - 1;
        while (true) {
            slow = nums[slow] - 1;
            fast = nums[fast] - 1;
            fast = nums[fast] - 1;
            if (fast == slow) {
                break;
            }
        }
        slow = n - 1;
        while (slow != fast) {
            slow = nums[slow] - 1;
            fast = nums[fast] - 1;
        }
        return slow;
    }
}
