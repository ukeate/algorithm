package basic.c36;

// 数组元素是可以跳的最大长度，求跳到最后的最小步数
public class JumpGame {
    public static int jump(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        // 跳过多少步
        int step = 0;
        // 当前步右边界
        int cur = 0;
        // 下一步右边界
        int next = 0;
        for (int i = 0; i < arr.length; i++) {
            if (cur < i) {
                step++;
                cur = next;
            }
            next = Math.max(next, i + arr[i]);
        }
        return step;
    }

    public static void main(String[] args) {
        int[] arr = {3, 2, 3, 1, 1, 4};
        System.out.println(jump(arr));
    }
}
