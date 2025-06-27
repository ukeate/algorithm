package leetc.top;

/**
 * LeetCode 277. 搜寻名人 (Find the Celebrity)
 * 
 * 问题描述：
 * 假设你是一个专业的狗仔，参加了一个 n 人派对，其中每个人都可能认识或者不认识其他人。
 * 所谓 "名人" 是指：所有其他人都认识他，但是他不认识任何其他人。
 * 现在你想要找出这个名人是谁，或者确定这个名人不存在。你唯一能做的就是问诸如 
 * "A 你好，请问你认识 B 吗？" 这样的问题，以确定 A 是否认识 B。
 * 你需要在最少的问题内找到这个名人（或者确定他不存在）。
 * 
 * 示例：
 * 输入：graph = [[1,1,0],[0,1,0],[1,1,1]]
 * 输出：1
 * 解释：有编号为 0, 1 和 2 的三个人。graph[i][j] = 1 表示人 i 认识人 j，
 * 否则 graph[i][j] = 0 表示人 i 不认识人 j。根据题意，返回名人的编号。
 * 
 * 解法思路：
 * 两阶段算法：
 * 1. 候选人选择：通过一轮比较找到唯一的候选人
 * 2. 候选人验证：验证候选人是否真的是名人
 * 
 * 核心思想：
 * - 如果 A 认识 B，那么 A 不可能是名人
 * - 如果 A 不认识 B，那么 B 不可能是名人
 * - 通过一轮遍历可以排除 n-1 个人，剩下的就是候选人
 * - 再用两轮遍历验证候选人的有效性
 * 
 * 时间复杂度：O(n) - 最多调用 3n 次 knows() 函数
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/find-the-celebrity/
 */
public class P277_FindTheCelebrity {
    public static boolean knows(int x, int i) {
        return true;
    }

    public int findCelebrity(int n) {
        int cand = 0;
        for (int i = 0; i < n; i++) {
            if (knows(cand, i)) {
                cand = i;
            }
        }
        for (int i = 0; i < cand; i++) {
            if (knows(cand, i)) {
                return -1;
            }
        }
        for (int i = 0; i < n; i++) {
            if (!knows(i, cand)) {
                return -1;
            }
        }
        return cand;
    }
}
