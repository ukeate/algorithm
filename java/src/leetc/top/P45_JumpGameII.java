package leetc.top;

public class P45_JumpGameII {
    public static int jump(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int jump = 0, cur = 0, next = 0;
        for (int i = 0; i < arr.length; i++) {
            if (cur < i) {
                jump++;
                cur = next;
            }
            next = Math.max(next, i + arr[i]);
        }
        return jump;
    }
}
