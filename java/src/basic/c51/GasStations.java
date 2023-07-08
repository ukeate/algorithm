package basic.c51;

// 给各站点油和距离数，返回各站点是否良好出发点(0油可跑一圈)
public class GasStations {
    private static int oilDis(int[] dis, int[] oil) {
        int init = -1;
        for (int i = 0; i < dis.length; i++) {
            dis[i] = oil[i] - dis[i];
            if (dis[i] >= 0) {
                init = i;
            }
        }
        return init;
    }

    private static int nextIdx(int idx, int size) {
        return idx == size - 1 ? 0 : (idx + 1);
    }

    private static int lastIdx(int idx, int size) {
        return idx == 0 ? (size - 1) : idx - 1;
    }

    private static void connect(int[] dis, int start, int init, boolean[] res) {
        int need = 0;
        while (start != init) {
            if (dis[start] < need) {
                need -= dis[start];
            } else {
                res[start] = true;
                need = 0;
            }
            start = lastIdx(start, dis.length);
        }
    }

    // dis是油盈余
    private static boolean[] enlargeArea(int[] dis, int init) {
        boolean[] res = new boolean[dis.length];
        int start = init;
        int end = nextIdx(init, dis.length);
        // 接到连通区需要
        int need = 0;
        // 扩充连通区盈余
        int rest = 0;
        do {
            // 非初始状态, start进入连通区，是整个不能连通的情况
            if (start != init && start == lastIdx(end, dis.length)) {
                break;
            }
            if (dis[start] < need) {
                // 无法接连通区
                need -= dis[start];
            } else {
                // 向后扩连通区
                rest += dis[start] - need;
                need = 0;
                while (rest >= 0 && end != start) {
                    rest += dis[end];
                    end = nextIdx(end, dis.length);
                }
                // 已连通成环
                if (rest >= 0) {
                    res[start] = true;
                    // 处理start剩余
                    connect(dis, lastIdx(start, dis.length), init, res);
                    break;
                }
            }
            start = lastIdx(start, dis.length);
        } while (start != init);
        return res;
    }

    public static boolean[] stations(int[] dis, int[] oil) {
        if (dis == null || oil == null || dis.length < 2 || dis.length != oil.length) {
            return null;
        }
        int init = oilDis(dis, oil);
        return init == -1 ? new boolean[dis.length] : enlargeArea(dis, init);
    }

    //

    private static boolean canThrough(int[] arr, int idx) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[idx];
            if (sum < 0) {
                return false;
            }
            idx = nextIdx(idx, arr.length);
        }
        return true;
    }

    private static boolean[] sure(int[] dis, int[] oil) {
        if (dis == null || oil == null || dis.length < 2 || dis.length != oil.length) {
            return null;
        }
        boolean[] res = new boolean[dis.length];
        for (int i = 0; i < dis.length; i++) {
            dis[i] = oil[i] - dis[i];
        }
        for (int i = 0; i < dis.length; i++) {
            res[i] = canThrough(dis, i);
        }
        return res;
    }

    private static int[] randomArr(int size, int max) {
        int[] res = new int[size];
        for (int i = 0; i < res.length; i++) {
            res[i] = (int) (max * Math.random());
        }
        return res;
    }

    private static int[] copy(int[] arr) {
        int[] res = new int[arr.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    private static boolean isEqual(boolean[] arr1, boolean[] arr2) {
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int times = 5000000;
        int max = 20;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int size = (int) (max * Math.random()) + 2;
            int[] dis = randomArr(size, max);
            int[] oil = randomArr(size, max);
            int[] dis1 = copy(dis);
            int[] oil1 = copy(oil);
            int[] dis2 = copy(dis);
            int[] oil2 = copy(oil);
            boolean[] ans1 = stations(dis1, oil1);
            boolean[] ans2 = sure(dis2, oil2);
            if (!isEqual(ans1, ans2)) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
