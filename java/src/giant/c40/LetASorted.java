package giant.c40;

// 给定两个数组A和B，长度都是N
// A[i]不可以在A中和其他数交换，只可以选择和B[i]交换(0<=i<n)
// 你的目的是让A有序，返回你能不能做到
public class LetASorted {
    private static boolean process1(int[] a, int[] b, int i, int lastA) {
        if (i == a.length) {
            return true;
        }
        // 不交换
        if (a[i] >= lastA && process1(a, b, i + 1, a[i])) {
            return true;
        }
        // 交换
        if (b[i] >= lastA && process1(a, b, i + 1, b[i])) {
            return true;
        }
        return false;
    }

    public static boolean can1(int[] a, int[] b) {
        return process1(a, b, 0, Integer.MIN_VALUE);
    }

    //

    // 当前推进到了i位置，对于A和B都是i位置
    // A[i]前一个数字是否来自A ：
    // 如果来自A，fromA = true；如果来自B，fromA = false；
    // 能否通过题意中的操作，A[i] B[i] 让A整体有序
    // 好处：可变参数成了int + boolean，时间复杂度可以做到O(N)
    private static boolean process2(int[] a, int[] b, int i, boolean fromA) {
        if (i == a.length) {
            return true;
        }
        if (i == 0 || (a[i] >= (fromA ? a[i - 1] : b[i - 1])) && process2(a, b, i + 1, true)) {
            return true;
        }
        if (i == 0 || (b[i] >= (fromA ? a[i - 1] : b[i - 1])) && process2(a, b, i + 1, false)) {
            return true;
        }
        return false;
    }

    public static boolean can2(int[] a, int[] b) {
        return process2(a, b, 0, true);
    }
}
