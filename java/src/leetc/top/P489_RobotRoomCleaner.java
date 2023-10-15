package leetc.top;

import java.util.HashSet;

public class P489_RobotRoomCleaner {
    interface Robot {
        public boolean move();

        public void turnLeft();

        public void turnRight();

        public void clean();
    }

    private static final int[][] ds = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    // (x,y)当前位置，d面向方向
    // 不重复走visited, 走完剩下位置，并返回
    private static void clean(Robot robot, int x, int y, int d, HashSet<String> visited) {
        robot.clean();
        visited.add(x + "_" + y);
        for (int i = 0; i < 4; i++) {
            int nd = (i + d) % 4;
            int nx = ds[nd][0] + x;
            int ny = ds[nd][1] + y;
            if (!visited.contains(nx + "_" + ny) && robot.move()) {
                clean(robot, nx, ny, nd, visited);
            }
            robot.turnRight();
        }
        robot.turnRight();
        robot.turnRight();
        robot.move();
        robot.turnRight();
        robot.turnRight();
    }

    public static void cleanRoom(Robot robot) {
        clean(robot, 0, 0, 0, new HashSet<>());
    }
}
