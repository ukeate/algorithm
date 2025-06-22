package base.sort;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * 链表快速排序 - 快速排序在链表上的实现
 * 
 * 问题描述：
 * 对单链表进行快速排序，要求时间复杂度O(N*logN)，空间复杂度O(logN)
 * 
 * 算法特点：
 * 1. 不能像数组那样随机访问元素，需要顺序遍历
 * 2. 不能直接使用双指针从两端向中间移动
 * 3. 需要重新组织链表结构，而不是简单的元素交换
 * 
 * 解决思路：
 * 1. 选择第一个节点作为基准值（pivot）
 * 2. 遍历链表，将节点分为三部分：小于、等于、大于基准值
 * 3. 递归排序小于和大于部分
 * 4. 连接三部分：小于部分 + 等于部分 + 大于部分
 * 
 * 算法优势：
 * - 原地排序，只需要修改指针
 * - 平均时间复杂度O(N*logN)
 * - 适合处理大型链表
 * 
 * 时间复杂度：
 * - 平均情况：O(N*logN)
 * - 最坏情况：O(N²) - 当链表已经有序时
 * 空间复杂度：O(logN) - 递归调用栈的深度
 * 
 * 应用场景：
 * - 链表排序问题
 * - 内存受限的排序场景
 * - 大数据量的外部排序
 */
public class QuickSortLink {
    /**
     * 双向链表节点定义
     */
    public static class Node {
        public int val;      // 节点值
        public Node last;    // 前驱指针
        public Node next;    // 后继指针

        /**
         * 构造函数
         * @param v 节点值
         */
        public Node(int v) {
            this.val = v;
        }
    }

    /**
     * 头尾节点对 - 用于表示链表的头尾
     */
    public static class HeadTail {
        public Node h;  // 头节点
        public Node t;  // 尾节点

        /**
         * 构造函数
         * @param head 头节点
         * @param tail 尾节点
         */
        public HeadTail(Node head, Node tail) {
            this.h = head;
            this.t = tail;
        }
    }

    /**
     * 分区信息类 - 存储分区后的三个部分的信息
     */
    public static class Info {
        public Node lh;  // 小于区域的头节点
        public Node lt;  // 小于区域的尾节点
        public int ls;   // 小于区域的节点数量
        public Node rh;  // 大于区域的头节点
        public Node rt;  // 大于区域的尾节点
        public int rs;   // 大于区域的节点数量
        public Node eh;  // 等于区域的头节点
        public Node et;  // 等于区域的尾节点

        /**
         * 构造函数
         * @param lh 小于区域头节点
         * @param lt 小于区域尾节点
         * @param ls 小于区域节点数量
         * @param rh 大于区域头节点
         * @param rt 大于区域尾节点
         * @param rs 大于区域节点数量
         * @param eh 等于区域头节点
         * @param et 等于区域尾节点
         */
        public Info(Node lh, Node lt, int ls, Node rh, Node rt, int rs, Node eh, Node et) {
            this.lh = lh;
            this.lt = lt;
            this.ls = ls;
            this.rh = rh;
            this.rt = rt;
            this.rs = rs;
            this.eh = eh;
            this.et = et;
        }
    }

    /**
     * 链表分区函数 - 将链表按基准值分为三部分
     * 
     * 算法详解：
     * 1. 遍历链表中除基准节点外的所有节点
     * 2. 根据节点值与基准值的比较结果，将节点分配到对应区域
     * 3. 维护每个区域的头尾指针和节点数量
     * 
     * 分区策略：
     * - 小于基准值：添加到小于区域
     * - 等于基准值：添加到等于区域
     * - 大于基准值：添加到大于区域
     * 
     * @param l 待分区的链表头节点（不包括基准节点）
     * @param p 基准节点
     * @return 分区信息，包含三个区域的头尾指针和节点数量
     */
    public static Info partition(Node l, Node p) {
        Node lh = null, lt = null, rh = null, rt = null, eh = p, et = p;
        int ls = 0, rs = 0;  // 小于和大于区域的节点数量
        Node nxt = null;
        
        // 遍历链表进行分区
        while (l != null) {
            nxt = l.next;  // 保存下一个节点
            l.next = null; // 断开当前节点的连接
            l.last = null;
            
            if (l.val < p.val) {
                // 添加到小于区域
                ls++;
                if (lh == null) {
                    lh = l;
                    lt = l;
                } else {
                    lt.next = l;
                    l.last = lt;
                    lt = l;
                }
            } else if (l.val > p.val) {
                // 添加到大于区域
                rs++;
                if (rh == null) {
                    rh = l;
                    rt = l;
                } else {
                    rt.next = l;
                    l.last = rt;
                    rt = l;
                }
            } else {
                // 添加到等于区域
                et.next = l;
                l.last = et;
                et = l;
            }
            l = nxt;  // 移动到下一个节点
        }
        return new Info(lh, lt, ls, rh, rt, rs, eh, et);
    }

    /**
     * 递归快速排序处理函数
     * 
     * 算法步骤：
     * 1. 递归边界：空链表返回null，单节点返回该节点
     * 2. 随机选择基准节点，避免最坏情况
     * 3. 将基准节点从链表中移除
     * 4. 对剩余节点进行分区
     * 5. 递归排序小于和大于区域
     * 6. 连接三个部分：小于 + 等于 + 大于
     * 
     * 优化策略：
     * - 随机选择基准值，降低最坏情况概率
     * - 三路分区，处理重复元素更高效
     * 
     * @param l 链表头节点
     * @param r 链表尾节点
     * @param n 链表节点数量
     * @return 排序后的链表头尾节点对
     */
    public static HeadTail process(Node l, Node r, int n) {
        if (l == null) {
            return null;  // 空链表
        }
        if (l == r) {
            return new HeadTail(l, r);  // 单节点链表
        }
        
        // 随机选择基准节点
        int randIdx = (int) (n * Math.random());
        Node p = l;
        while (randIdx-- != 0) {
            p = p.next;
        }
        
        // 将基准节点从链表中移除
        if (p == l || p == r) {
            if (p == l) {
                l = p.next;
                l.last = null;
            } else {
                p.last.next = null;
            }
        } else {
            p.last.next = p.next;
            p.next.last = p.last;
        }
        p.last = null;
        p.next = null;
        
        // 对剩余节点进行分区
        Info info = partition(l, p);
        
        // 递归排序小于和大于区域
        HeadTail lht = process(info.lh, info.lt, info.ls);
        HeadTail rht = process(info.rh, info.rt, info.rs);
        
        // 连接三个部分
        if (lht != null) {
            lht.t.next = info.eh;
            info.eh.last = lht.t;
        }
        if (rht != null) {
            info.et.next = rht.h;
            rht.h.last = info.et;
        }
        
        // 确定最终的头尾节点
        Node h = lht != null ? lht.h : info.eh;
        Node t = rht != null ? rht.t : info.et;
        return new HeadTail(h, t);
    }

    /**
     * 链表快速排序主函数
     * 
     * 算法流程：
     * 1. 处理边界情况：空链表直接返回
     * 2. 遍历链表计算节点数量，找到尾节点
     * 3. 调用递归处理函数进行排序
     * 4. 返回排序后的链表头节点
     * 
     * @param h 链表头节点
     * @return 排序后的链表头节点
     */
    public static Node quickSort(Node h) {
        if (h == null) {
            return null;  // 空链表直接返回
        }
        
        // 计算链表长度并找到尾节点
        int n = 0;
        Node c = h;
        Node e = null;
        while (c != null) {
            n++;
            e = c;
            c = c.next;
        }
        
        // 调用递归处理函数并返回头节点
        return process(h, e, n).h;
    }

    // ==================== 测试相关代码 ====================

    /**
     * 节点比较器 - 用于标准排序验证
     */
    private static class NodeComp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.val - o2.val;
        }
    }

    /**
     * 标准排序方法 - 用于验证快速排序结果的正确性
     * 
     * 算法步骤：
     * 1. 将链表节点收集到ArrayList中
     * 2. 使用Java标准排序算法排序
     * 3. 重新构建双向链表
     * 
     * @param head 链表头节点
     * @return 排序后的链表头节点
     */
    private static Node sortSure(Node head) {
        if (head == null) {
            return null;
        }
        
        // 收集所有节点到数组中
        ArrayList<Node> arr = new ArrayList<>();
        while (head != null) {
            arr.add(head);
            head = head.next;
        }
        
        // 使用标准排序算法
        arr.sort(new NodeComp());
        
        // 重新构建双向链表
        Node h = arr.get(0);
        h.last = null;
        Node p = h;
        for (int i = 1; i < arr.size(); i++) {
            Node c = arr.get(i);
            p.next = c;
            c.last = p;
            c.next = null;
            p = c;
        }
        return h;
    }

    /**
     * 生成随机双向链表
     * 
     * @param n 链表长度
     * @param maxV 节点值的最大值
     * @return 随机链表的头节点
     */
    private static Node randomLink(int n, int maxV) {
        if (n == 0) {
            return null;
        }
        
        // 创建节点数组
        Node[] arr = new Node[n];
        for (int i = 0; i < n; i++) {
            arr[i] = new Node((int) ((maxV + 1) * Math.random()));
        }
        
        // 构建双向链表
        Node head = arr[0];
        Node pre = head;
        for (int i = 1; i < n; i++) {
            pre.next = arr[i];
            arr[i].last = pre;
            pre = arr[i];
        }
        return head;
    }

    /**
     * 克隆双向链表
     * 
     * @param head 原链表头节点
     * @return 克隆链表的头节点
     */
    private static Node cloneLink(Node head) {
        if (head == null) {
            return null;
        }
        
        // 创建新的头节点
        Node h = new Node(head.val);
        Node p = h;
        head = head.next;
        
        // 逐个克隆节点并建立双向连接
        while (head != null) {
            Node c = new Node(head.val);
            p.next = c;
            c.last = p;
            p = c;
            head = head.next;
        }
        return h;
    }

    /**
     * 将双向链表转换为字符串表示
     * 
     * 格式：正向遍历结果 | 反向遍历结果
     * 用于验证双向链表的正确性
     * 
     * @param head 链表头节点
     * @return 链表的字符串表示
     */
    private static String link2String(Node head) {
        Node cur = head;
        Node end = null;
        StringBuilder builder = new StringBuilder();
        
        // 正向遍历
        while (cur != null) {
            builder.append(cur.val + " ");
            end = cur;
            cur = cur.next;
        }
        builder.append("| ");
        
        // 反向遍历
        while (end != null) {
            builder.append(end.val + " ");
            end = end.last;
        }
        return builder.toString();
    }

    /**
     * 比较两个双向链表是否相等
     * 
     * @param h1 第一个链表头节点
     * @param h2 第二个链表头节点
     * @return 两个链表是否相等
     */
    private static boolean isEqual(Node h1, Node h2) {
        return link2String(h1).equals(link2String(h2));
    }

    /**
     * 测试方法 - 验证双向链表快速排序的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 双向链表快速排序测试 ===");
        
        int times = 10000;
        int maxLen = 500;
        int maxVal = 500;
        System.out.println("测试次数：" + times);
        System.out.println("最大链表长度：" + maxLen);
        System.out.println("最大节点值：" + maxVal);
        System.out.println("开始测试...");
        
        boolean success = true;
        for (int i = 0; i < times; i++) {
            int size = (int) ((maxLen + 1) * Math.random());
            Node head1 = randomLink(size, maxVal);
            Node head2 = cloneLink(head1);
            
            Node sort1 = quickSort(head1);
            Node sort2 = sortSure(head2);
            
            if (!isEqual(sort1, sort2)) {
                System.out.println("第" + (i + 1) + "次测试失败！");
                System.out.println("快速排序结果：" + link2String(sort1));
                System.out.println("标准排序结果：" + link2String(sort2));
                success = false;
                break;
            }
        }
        
        if (success) {
            System.out.println("所有测试通过！双向链表快速排序算法实现正确。");
        } else {
            System.out.println("测试失败！请检查算法实现。");
        }
        
        System.out.println("=== 测试结束 ===");
    }
}
