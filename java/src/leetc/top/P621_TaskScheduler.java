package leetc.top;

public class P621_TaskScheduler {
    public static int leastInterval(char[] tasks, int free) {
        int[] count = new int[256];
        int maxCount = 0;
        for (char task : tasks) {
            count[task]++;
            maxCount = Math.max(maxCount, count[task]);
        }
        int maxKinds = 0;
        for (int task = 0; task < 256; task++) {
            if(count[task] == maxCount) {
                maxKinds++;
            }
        }
        int exceptRight = tasks.length - maxKinds;
        int spaces = (free + 1) * (maxCount - 1);
        int restSpaces = Math.max(0, spaces - exceptRight);
        return tasks.length + restSpaces;
    }
}
