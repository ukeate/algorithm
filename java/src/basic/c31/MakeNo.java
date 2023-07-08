package basic.c31;

// 生成达标数组， i<k<j 满足 [i]+[j] != [k]*2
public class MakeNo {

    public static int[] make(int size) {
        if (size == 1) {
            return new int[]{1};
        }
        int halfSize = (size + 1) / 2;
        int[] base = make(halfSize);
        int[] ans = new int[size];
        int idx = 0;
        for (; idx < halfSize; idx++) {
            ans[idx] = base[idx] * 2 - 1;
        }
        for (int i = 0; idx < size; idx++, i++) {
            ans[idx] = base[i] * 2;
        }
        return ans;
    }

    //

    private static boolean isValid(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            for (int k = i + 1; k < n; k++) {
                for (int j = k + 1; j < n; j++) {
                    if (arr[i] + arr[j] == 2 * arr[k]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("test begin");
        for (int n = 1; n < 1000; n++) {
            int[] arr = make(n);
            if (!isValid(arr)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
