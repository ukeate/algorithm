package leetc.top;

public class P148_SortList {
    public static class ListNode {
        int val;
        ListNode next;

        public ListNode(int v) {
            val = v;
        }
    }

    private static ListNode[] hthtn(ListNode idx, int len) {
        ListNode ls = idx, le = idx, rs = null, re = null, next = null;
        int pass = 0;
        while (idx != null) {
            pass++;
            if (pass <= len) {
                le = idx;
            }
            if (pass == len + 1) {
                rs = idx;
            }
            if (pass > len) {
                re = idx;
            }
            if (pass == (len << 1)) {
                break;
            }
            idx = idx.next;
        }
        le.next = null;
        if (re != null) {
            next = re.next;
            re.next = null;
        }
        return new ListNode[]{ls, le, rs, re, next};
    }

    private static ListNode[] merge(ListNode ls, ListNode le, ListNode rs, ListNode re) {
        if (rs == null) {
            return new ListNode[]{ls, le};
        }
        ListNode head = null, pre = null, cur = null, tail = null;
        while (ls != le.next && rs != re.next) {
            if (ls.val <= rs.val) {
                cur = ls;
                ls = ls.next;
            } else {
                cur = rs;
                rs = rs.next;
            }
            if (pre == null) {
                head = cur;
                pre = cur;
            } else {
                pre.next = cur;
                pre = cur;
            }
        }
        if (ls != le.next) {
            while (ls != le.next) {
                pre.next = ls;
                pre = ls;
                tail = ls;
                ls = ls.next;
            }
        } else {
            while (rs != re.next) {
                pre.next = rs;
                pre = rs;
                tail = rs;
                rs = rs.next;
            }
        }
        return new ListNode[] {head, tail};
    }

    public static ListNode sortList(ListNode head) {
        int n = 0;
        ListNode cur = head;
        while (cur != null) {
            n++;
            cur = cur.next;
        }
        ListNode h = head;
        ListNode teamFirst = head;
        ListNode pre = null;
        for (int len = 1; len < n; len <<= 1) {
            while (teamFirst != null) {
                ListNode[] hthtn = hthtn(teamFirst, len);
                ListNode[] mhmt = merge(hthtn[0], hthtn[1], hthtn[2], hthtn[3]);
                if (h == teamFirst) {
                    h = mhmt[0];
                    pre = mhmt[1];
                } else {
                    pre.next = mhmt[0];
                    pre = mhmt[1];
                }
                teamFirst = hthtn[4];
            }
            teamFirst = h;
            pre = null;
        }
        return h;
    }
}
