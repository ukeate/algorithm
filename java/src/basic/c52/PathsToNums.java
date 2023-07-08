package basic.c52;

// 输入数组val为key父节点，key=key为首都, 输入到首都距离数组, key为距离值，val为统计数
public class PathsToNums {
    private static void paths2distances(int[] paths) {
        int cap = 0;
        for (int start = 0; start < paths.length; start++) {
            if (paths[start] == start) {
                cap = start;
            } else if (paths[start] > -1) {
                int curI = paths[start];
                // 暂记为-1，并非实际层数
                paths[start] = -1;
                int preI = start;
                // 非首都 且 未处理
                while (paths[curI] != curI && paths[curI] > -1) {
                    int nextI = paths[curI];
                    // 记回退路径
                    paths[curI] = preI;
                    preI = curI;
                    curI = nextI;
                }
                // 首都或已处理，向前算层数
                int level = paths[curI] == curI ? 0 : paths[curI];
                while (paths[preI] != -1) {
                    int lastPreI = paths[preI];
                    paths[preI] = --level;
                    preI = lastPreI;
                }
                paths[preI] = --level;
            }
        }
        paths[cap] = 0;
    }

    private static void distances2nums(int[] dis) {
        for (int i = 0; i < dis.length; i++) {
            int idx = dis[i];
            // 层未处理时
            if (idx < 0) {
                dis[i] = 0;
                while (true) {
                    idx = -idx;
                    if (dis[idx] > -1) {
                        // i层累加
                        dis[idx]++;
                        break;
                    } else {
                        // i层第1次记数
                        int nextIdx = dis[idx];
                        dis[idx] = 1;
                        idx = nextIdx;
                    }
                }
            }
        }
        dis[0] = 1;
    }

    public static void change(int[] paths) {
        if (paths == null || paths.length == 0) {
            return;
        }
        paths2distances(paths);
        distances2nums(paths);
    }

    private static void print(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] paths = {9, 1, 4, 9, 0, 4, 8, 9, 0, 1};
        print(paths);
        change(paths);
        print(paths);
    }
}
