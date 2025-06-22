package base.link;

import java.util.ArrayList;
import java.util.List;

/**
 * 链表反转算法实现
 * 
 * 包含功能：
 * 1. 单链表反转 - 经典的双指针算法
 * 2. 双向链表反转 - 需要同时处理前驱和后继指针
 * 
 * 反转的核心思想：
 * - 改变节点间的指向关系
 * - 需要保存必要的临时指针避免链表断裂
 * - 空间复杂度O(1)，时间复杂度O(n)
 */
public class Reverse {

    /**
     * 单链表节点定义
     */
    static class Node {
        public Node next;   // 指向下一个节点的指针
        public int val;     // 节点值

        public Node(int val) {
            this.val = val;
        }
    }

    /**
     * 双向链表节点定义
     */
    static class DuoNode {
        public DuoNode last;    // 指向前一个节点的指针
        public DuoNode next;    // 指向下一个节点的指针
        public int val;         // 节点值

        public DuoNode(int val) {
            this.val = val;
        }
    }

    /**
     * 单链表反转 - 双指针法（迭代版本）
     * 
     * 算法步骤：
     * 1. 初始化三个指针：pre（前驱）、head（当前）、next（后继）
     * 2. 保存当前节点的下一个节点
     * 3. 将当前节点指向前驱节点
     * 4. 移动pre和head指针
     * 5. 重复步骤2-4直到遍历完整个链表
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * 
     * @param head 原链表头节点
     * @return 反转后的链表头节点
     */
    public static Node reverse(Node head) {
        Node pre = null;    // 前驱节点
        Node next = null;   // 后继节点
        while (head != null) {
            next = head.next;       // 保存下一个节点
            head.next = pre;        // 当前节点指向前驱
            pre = head;             // 更新前驱节点
            head = next;            // 移动到下一个节点
        }
        return pre;     // pre就是反转后的新头节点
    }

    /**
     * 双向链表反转
     * 
     * 算法步骤：
     * 1. 对于每个节点，交换其last和next指针
     * 2. 原来的next变成新的last
     * 3. 原来的last变成新的next
     * 
     * 注意：双向链表反转需要同时处理两个方向的指针
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * 
     * @param head 原双向链表头节点
     * @return 反转后的双向链表头节点
     */
    public static DuoNode reverseDuo(DuoNode head) {
        DuoNode pre = null;
        DuoNode next = null;
        while (head != null) {
            next = head.next;       // 保存下一个节点
            head.next = pre;        // 下一个指针指向前驱
            head.last = next;       // 前一个指针指向原来的下一个
            pre = head;             // 更新前驱节点
            head = next;            // 移动到下一个节点
        }
        return pre;
    }

    /**
     * 单链表反转的对照实现 - 使用额外空间
     * 
     * 思路：
     * 1. 将所有节点存入数组
     * 2. 重新连接节点，改变指向关系
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @param head 原链表头节点
     * @return 反转后的链表头节点
     */
    private static Node reverseSure(Node head) {
        if (head == null) {
            return null;
        }
        List<Node> list = new ArrayList<>();
        // 将所有节点收集到数组中
        while (head != null) {
            list.add(head);
            head = head.next;
        }
        // 设置新的尾节点
        list.get(0).next = null;
        int n = list.size();
        // 反向连接所有节点
        for (int i = 1; i < n; i++) {
            list.get(i).next = list.get(i - 1);
        }
        return list.get(n - 1);     // 返回新的头节点
    }

    /**
     * 双向链表反转的对照实现 - 使用额外空间
     * 
     * @param head 原双向链表头节点
     * @return 反转后的双向链表头节点
     */
    private static DuoNode reverseDuoList(DuoNode head) {
        if (head == null) {
            return null;
        }
        List<DuoNode> list = new ArrayList<>();
        // 收集所有节点
        while (head != null) {
            list.add(head);
            head = head.next;
        }
        // 设置新的尾节点
        list.get(0).next = null;
        DuoNode pre = list.get(0);
        int n = list.size();
        // 反向连接所有节点
        for (int i = 1; i < n; i++) {
            DuoNode cur = list.get(i);
            cur.last = null;
            cur.next = pre;
            pre.last = cur;
            pre = cur;
        }
        return list.get(n - 1);
    }

    /**
     * 生成随机单链表用于测试
     * @param maxLen 最大长度
     * @param maxVal 最大节点值
     * @return 随机单链表头节点
     */
    private static Node randomList(int maxLen, int maxVal) {
        int size = (int) ((maxLen + 1) * Math.random());
        if (size == 0) {
            return null;
        }
        Node head = new Node((int) ((maxVal + 1) * Math.random()));
        Node pre = head;
        size--;
        while (size != 0) {
            Node cur = new Node((int) ((maxVal + 1) * Math.random()));
            pre.next = cur;
            pre = cur;
            size--;
        }
        return head;
    }

    /**
     * 生成随机双向链表用于测试
     * @param maxLen 最大长度
     * @param maxVal 最大节点值
     * @return 随机双向链表头节点
     */
    private static DuoNode randomDuoList(int maxLen, int maxVal) {
        int size = (int) ((maxLen + 1) * Math.random());
        if (size == 0) {
            return null;
        }
        DuoNode head = new DuoNode((int) ((maxVal + 1) * Math.random()));
        DuoNode pre = head;
        size--;
        while (size != 0) {
            DuoNode cur = new DuoNode((int) ((maxVal + 1) * Math.random()));
            pre.next = cur;
            cur.last = pre;
            pre = cur;
            size--;
        }
        return head;
    }

    /**
     * 获取单链表的节点值序列
     * @param head 链表头节点
     * @return 节点值列表
     */
    private static List<Integer> getOrder(Node head) {
        List<Integer> ans = new ArrayList<>();
        while (head != null) {
            ans.add(head.val);
            head = head.next;
        }
        return ans;
    }

    /**
     * 获取双向链表的节点值序列
     * @param head 链表头节点
     * @return 节点值列表
     */
    private static List<Integer> getDuoOrder(DuoNode head) {
        List<Integer> ans = new ArrayList<>();
        while (head != null) {
            ans.add(head.val);
            head = head.next;
        }
        return ans;
    }

    /**
     * 检查单链表反转是否正确
     * @param ori 原始序列
     * @param head 反转后的链表头节点
     * @return 反转正确返回true，否则返回false
     */
    private static boolean checkReverse(List<Integer> ori, Node head) {
        for (int i = ori.size() - 1; i >= 0; i--) {
            if (!ori.get(i).equals(head.val)) {
                return false;
            }
            head = head.next;
        }
        return true;
    }

    /**
     * 检查双向链表反转是否正确
     * @param ori 原始序列
     * @param head 反转后的链表头节点
     * @return 反转正确返回true，否则返回false
     */
    private static boolean checkDuoReverse(List<Integer> ori, DuoNode head) {
        DuoNode end = null;
        // 正向检查
        for (int i = ori.size() - 1; i >= 0; i--) {
            if (!ori.get(i).equals(head.val)) {
                return false;
            }
            end = head;
            head = head.next;
        }
        // 反向检查
        for (int i = 0; i < ori.size(); i++) {
            if (!ori.get(i).equals(end.val)) {
                return false;
            }
            end = end.last;
        }
        return true;
    }

    /**
     * 测试方法 - 验证链表反转算法的正确性
     */
    public static void main(String[] args) {
        int times = 100000;     // 测试次数
        int len = 50;           // 最大链表长度
        int val = 100;          // 最大节点值
        
        System.out.println("test begin");
        
        // 测试单链表反转
        for (int i = 0; i < times; i++) {
            Node head1 = randomList(len, val);
            List<Integer> order = getOrder(head1);
            head1 = reverse(head1);
            if (!checkReverse(order, head1)) {
                System.out.println("Wrong - 单链表反转错误");
                break;
            }
        }
        
        // 测试双向链表反转
        for (int i = 0; i < times; i++) {
            DuoNode head2 = randomDuoList(len, val);
            List<Integer> order = getDuoOrder(head2);
            head2 = reverseDuo(head2);
            if (!checkDuoReverse(order, head2)) {
                System.out.println("Wrong - 双向链表反转错误");
                break;
            }
        }
        
        System.out.println("test end");
    }
}
