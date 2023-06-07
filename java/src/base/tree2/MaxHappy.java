package base.tree2;

import java.util.ArrayList;
import java.util.List;

public class MaxHappy {
    public static class Employee {
        public int happy;
        public List<Employee> nexts;

        public Employee(int h) {
            happy = h;
            nexts = new ArrayList<>();
        }
    }

    private static int process1(Employee cur, boolean up) {
        if (up) {
            // 上级来时
            int ans = 0;
            for (Employee next : cur.nexts) {
                ans += process1(next, false);
            }
            return ans;
        } else {
            int p1 = cur.happy;
            int p2 = 0;
            for (Employee next : cur.nexts) {
                p1 += process1(next, true);
                p2 += process1(next, false);
            }
            return Math.max(p1, p2);
        }
    }

    public static int maxHappy1(Employee boss) {
        if (boss == null) {
            return 0;
        }
        return process1(boss, false);
    }

    //

    private static class Info {
        public int no;
        public int yes;

        public Info(int n, int y) {
            no = n;
            yes = y;
        }
    }

    private static Info process2(Employee x) {
        if (x == null) {
            return new Info(0, 0);
        }
        int no = 0;
        int yes = x.happy;
        for (Employee next : x.nexts) {
            Info info = process2(next);
            no += Math.max(info.no, info.yes);
            yes += info.no;
        }
        return new Info(no, yes);
    }

    public static int maxHappy2(Employee head) {
        Info info = process2(head);
        return Math.max(info.no, info.yes);
    }

    //

    private static void randomNexts(Employee e, int level, int maxLevel, int maxNext, int maxHappy) {
        if (level > maxLevel) {
            return;
        }
        int nextSize = (int) ((maxNext + 1) * Math.random());
        for (int i = 0; i < nextSize; i++) {
            Employee next = new Employee((int) ((maxHappy + 1) * Math.random()));
            e.nexts.add(next);
            randomNexts(next, level + 1, maxLevel, maxNext, maxHappy);
        }
    }

    private static Employee randomBoss(int maxLevel, int maxNext, int maxHappy) {
        if (Math.random() < 0.02) {
            return null;
        }
        Employee boss = new Employee((int) ((maxHappy + 1) * Math.random()));
        randomNexts(boss, 1, maxLevel, maxNext, maxHappy);
        return boss;
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLevel = 4;
        int maxNext = 7;
        int maxHappy = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            Employee boss = randomBoss(maxLevel, maxNext, maxHappy);
            if (maxHappy1(boss) != maxHappy2(boss)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
