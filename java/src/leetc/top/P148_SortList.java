package leetc.top;

/**
 * LeetCode 148. 排序链表 (Sort List)
 * 
 * 问题描述：
 * 给你链表的头结点 head ，请将其按升序排列并返回排序后的链表。
 * 
 * 进阶要求：
 * 你可以在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序吗？
 * 
 * 解法思路：
 * 自底向上的归并排序（迭代版本）：
 * 1. 避免递归调用的额外空间开销，实现O(1)空间复杂度
 * 2. 从小到大逐步合并：先合并长度为1的段，再合并长度为2的段，依此类推
 * 3. 每轮合并后，段长度翻倍，直到覆盖整个链表
 * 
 * 核心操作：
 * 1. hthtn(): 获取头、尾、下一段头、尾和剩余部分
 * 2. merge(): 合并两个有序段
 * 3. 外层循环控制段长度，内层循环处理所有段对
 * 
 * 算法步骤：
 * 1. 计算链表总长度n
 * 2. 外层循环：段长度从1到n/2，每次翻倍
 * 3. 内层循环：处理当前长度的所有段对
 * 4. 使用hthtn获取两个段的边界信息
 * 5. 使用merge合并两个有序段
 * 6. 更新链表连接关系
 * 
 * 归并排序特点：
 * - 稳定排序算法
 * - 时间复杂度严格O(n log n)
 * - 适合链表结构（不需要随机访问）
 * 
 * 时间复杂度：O(n log n) - 归并排序标准复杂度
 * 空间复杂度：O(1) - 只使用常数额外空间，不计算返回结果
 * 
 * LeetCode链接：https://leetcode.com/problems/sort-list/
 */
public class P148_SortList {
    
    /**
     * 链表节点定义
     */
    public static class ListNode {
        int val;
        ListNode next;

        public ListNode(int v) {
            val = v;
        }
    }

    /**
     * 获取指定长度的头尾信息
     * hthtn = Head Tail Head Tail Next 的缩写
     * 
     * @param idx 起始节点
     * @param len 段长度
     * @return [左段头, 左段尾, 右段头, 右段尾, 下一组起始位置]
     */
    private static ListNode[] hthtn(ListNode idx, int len) {
        ListNode ls = idx;      // 左段头 (Left Start)
        ListNode le = idx;      // 左段尾 (Left End)  
        ListNode rs = null;     // 右段头 (Right Start)
        ListNode re = null;     // 右段尾 (Right End)
        ListNode next = null;   // 下一组起始位置
        
        int pass = 0;
        while (idx != null) {
            pass++;
            
            if (pass <= len) {
                le = idx;       // 更新左段尾部
            }
            if (pass == len + 1) {
                rs = idx;       // 确定右段头部
            }
            if (pass > len) {
                re = idx;       // 更新右段尾部
            }
            if (pass == (len << 1)) {   // 处理完两个段
                break;
            }
            
            idx = idx.next;
        }
        
        // 断开左段与后续的连接
        le.next = null;
        
        // 如果存在右段，断开右段与后续的连接，保存下一组位置
        if (re != null) {
            next = re.next;
            re.next = null;
        }
        
        return new ListNode[]{ls, le, rs, re, next};
    }

    /**
     * 合并两个有序链表段
     * 
     * @param ls 左段头节点
     * @param le 左段尾节点
     * @param rs 右段头节点  
     * @param re 右段尾节点
     * @return [合并后的头节点, 合并后的尾节点]
     */
    private static ListNode[] merge(ListNode ls, ListNode le, ListNode rs, ListNode re) {
        // 如果右段不存在，直接返回左段
        if (rs == null) {
            return new ListNode[]{ls, le};
        }
        
        ListNode head = null;   // 合并后的头节点
        ListNode pre = null;    // 前一个节点
        ListNode cur = null;    // 当前选中的节点
        ListNode tail = null;   // 合并后的尾节点
        
        // 双指针合并两个有序段
        while (ls != le.next && rs != re.next) {
            if (ls.val <= rs.val) {
                cur = ls;
                ls = ls.next;
            } else {
                cur = rs;
                rs = rs.next;
            }
            
            // 构建合并后的链表
            if (pre == null) {
                head = cur;     // 设置头节点
                pre = cur;
            } else {
                pre.next = cur;
                pre = cur;
            }
        }
        
        // 处理剩余的节点
        if (ls != le.next) {
            // 左段还有剩余节点
            while (ls != le.next) {
                pre.next = ls;
                pre = ls;
                tail = ls;
                ls = ls.next;
            }
        } else {
            // 右段还有剩余节点
            while (rs != re.next) {
                pre.next = rs;
                pre = rs;
                tail = rs;
                rs = rs.next;
            }
        }
        
        return new ListNode[]{head, tail};
    }

    /**
     * 排序链表主方法
     * 
     * @param head 链表头节点
     * @return 排序后的链表头节点
     */
    public static ListNode sortList(ListNode head) {
        // 计算链表长度
        int n = 0;
        ListNode cur = head;
        while (cur != null) {
            n++;
            cur = cur.next;
        }
        
        ListNode h = head;              // 整个链表的头部
        ListNode teamFirst = head;      // 当前处理组的第一个节点
        ListNode pre = null;            // 前一个合并结果的尾部
        
        // 自底向上归并：段长度从1开始，每次翻倍
        for (int len = 1; len < n; len <<= 1) {
            while (teamFirst != null) {
                // 获取当前两个段的边界信息
                ListNode[] hthtn = hthtn(teamFirst, len);
                
                // 合并两个段
                ListNode[] mhmt = merge(hthtn[0], hthtn[1], hthtn[2], hthtn[3]);
                
                if (h == teamFirst) {
                    // 处理第一组，更新整个链表的头部
                    h = mhmt[0];
                    pre = mhmt[1];
                } else {
                    // 连接前一组的尾部与当前组的头部
                    pre.next = mhmt[0];
                    pre = mhmt[1];
                }
                
                // 移动到下一组
                teamFirst = hthtn[4];
            }
            
            // 准备下一轮合并（段长度翻倍）
            teamFirst = h;
            pre = null;
        }
        
        return h;
    }
}
