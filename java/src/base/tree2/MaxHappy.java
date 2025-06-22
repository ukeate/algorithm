package base.tree2;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工最大快乐值问题（Employee Maximum Happiness）
 * 
 * 问题描述：
 * 公司要举办一个聚会，每个员工都有一个快乐值。
 * 规则：如果一个员工来参加聚会，那么他的直接下属就不能来参加。
 * 目标：在满足规则的前提下，使得参加聚会的员工快乐值总和最大。
 * 
 * 数据结构：每个员工有快乐值和下属列表，构成一个多叉树结构
 * 
 * 解题思路：
 * 方法1：暴力递归 - 对每个员工考虑来/不来两种情况
 * 方法2：树形DP - 递归收集每个子树在员工来/不来情况下的最大快乐值
 * 
 * 时间复杂度：
 * 方法1：O(2^n) 指数级复杂度
 * 方法2：O(n) 每个员工只访问一次
 */
public class MaxHappy {
    /**
     * 员工类定义
     */
    public static class Employee {
        public int happy;             // 员工快乐值
        public List<Employee> nexts;  // 直接下属列表

        public Employee(int h) {
            happy = h;
            nexts = new ArrayList<>();
        }
    }

    // ==================== 方法1：暴力递归 ====================
    
    /**
     * 暴力递归实现（方法1）
     * @param cur 当前员工
     * @param up 当前员工的上级是否来参加聚会
     * @return 以cur为根的子树的最大快乐值
     */
    private static int process1(Employee cur, boolean up) {
        if (up) {
            // 上级来参加聚会时，当前员工不能来
            int ans = 0;
            for (Employee next : cur.nexts) {
                ans += process1(next, false);  // 当前员工不来，所以下属可来可不来
            }
            return ans;
        } else {
            // 上级不来参加聚会时，当前员工可来可不来
            // 情况1：当前员工来参加
            int p1 = cur.happy;
            for (Employee next : cur.nexts) {
                p1 += process1(next, true);   // 当前员工来了，下属不能来
            }
            
            // 情况2：当前员工不来参加
            int p2 = 0;
            for (Employee next : cur.nexts) {
                p2 += process1(next, false);  // 当前员工不来，下属可来可不来
            }
            
            return Math.max(p1, p2);  // 选择快乐值更大的情况
        }
    }

    /**
     * 求最大快乐值（方法1：暴力递归）
     * @param boss 公司老板（树的根节点）
     * @return 最大快乐值
     */
    public static int maxHappy1(Employee boss) {
        if (boss == null) {
            return 0;
        }
        // 老板没有上级，所以可来可不来
        return process1(boss, false);
    }

    // ==================== 方法2：树形DP ====================

    /**
     * 封装子树信息的类
     */
    private static class Info {
        public int no;   // 当前员工不来参加聚会时，子树的最大快乐值
        public int yes;  // 当前员工来参加聚会时，子树的最大快乐值

        public Info(int n, int y) {
            no = n;
            yes = y;
        }
    }

    /**
     * 树形DP的递归实现（方法2）
     * @param x 当前员工
     * @return 当前子树的信息（来/不来的最大快乐值）
     */
    private static Info process2(Employee x) {
        if (x == null) {
            return new Info(0, 0);  // 空节点：来或不来快乐值都是0
        }
        
        int no = 0;        // x不来时的最大快乐值
        int yes = x.happy; // x来时的最大快乐值（包含x的快乐值）
        
        for (Employee next : x.nexts) {
            Info info = process2(next);
            
            // x不来时，下属可来可不来，选择最大值
            no += Math.max(info.no, info.yes);
            
            // x来时，下属不能来
            yes += info.no;
        }
        
        return new Info(no, yes);
    }

    /**
     * 求最大快乐值（方法2：树形DP）
     * @param head 公司老板（树的根节点）
     * @return 最大快乐值
     */
    public static int maxHappy2(Employee head) {
        Info info = process2(head);
        // 老板可来可不来，选择快乐值更大的情况
        return Math.max(info.no, info.yes);
    }

    // ==================== 测试工具方法 ====================

    /**
     * 随机为员工生成下属
     * @param e 当前员工
     * @param level 当前层级
     * @param maxLevel 最大层级
     * @param maxNext 每个员工最多的下属数
     * @param maxHappy 最大快乐值
     */
    private static void randomNexts(Employee e, int level, int maxLevel, int maxNext, int maxHappy) {
        if (level > maxLevel) {
            return;
        }
        // 随机生成下属数量
        int nextSize = (int) ((maxNext + 1) * Math.random());
        for (int i = 0; i < nextSize; i++) {
            // 为每个下属随机分配快乐值
            Employee next = new Employee((int) ((maxHappy + 1) * Math.random()));
            e.nexts.add(next);
            // 递归为下属生成他们的下属
            randomNexts(next, level + 1, maxLevel, maxNext, maxHappy);
        }
    }

    /**
     * 随机生成公司组织结构
     * @param maxLevel 最大层级
     * @param maxNext 每个员工最多的下属数
     * @param maxHappy 最大快乐值
     * @return 老板节点
     */
    private static Employee randomBoss(int maxLevel, int maxNext, int maxHappy) {
        if (Math.random() < 0.02) {
            return null;  // 2%的概率生成空公司
        }
        // 随机生成老板的快乐值
        Employee boss = new Employee((int) ((maxHappy + 1) * Math.random()));
        randomNexts(boss, 1, maxLevel, maxNext, maxHappy);
        return boss;
    }

    public static void main(String[] args) {
        int times = 100000;   // 测试次数
        int maxLevel = 4;     // 最大层级
        int maxNext = 7;      // 每个员工最多下属数
        int maxHappy = 100;   // 最大快乐值
        System.out.println("test begin");
        
        // 对拍测试：比较两种方法的结果是否一致
        for (int i = 0; i < times; i++) {
            Employee boss = randomBoss(maxLevel, maxNext, maxHappy);
            if (maxHappy1(boss) != maxHappy2(boss)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
