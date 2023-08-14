package giant.c24;

// [1,num]中，数位没有4的数个数
public class NotContains4 {
    public static boolean isNot4(long num) {
        while (num != 0) {
            if (num % 10 == 4) {
                return false;
            }
            num /= 10;
        }
        return true;
    }

    public static long notContains4Nums1(long num) {
        long count = 0;
        for (long i = 1; i <= num; i++) {
            if (isNot4(i)) {
                count++;
            }
        }
        return count;
    }

    //

    // i-1个数位,包含0有几个数
    public static long[] arr = {0L, 1L, 9L, 81L, 729L, 6561L, 59049L, 531441L, 4782969L, 43046721L, 387420489L,
            3486784401L, 31381059609L, 282429536481L, 2541865828329L, 22876792454961L, 205891132094649L,
            1853020188851841L, 16677181699666569L, 150094635296999121L, 1350851717672992089L};

    private static int len(long num) {
        int len = 0;
        while (num != 0) {
            len++;
            num /= 10;
        }
        return len;
    }

    private static long offset(int len) {
        long offset = 1;
        for (int i = 1; i < len; i++) {
            offset *= 10L;
        }
        return offset;
    }

    // 前边有数字，first包含0
    private static long process(long num, long offset, int len) {
        if (len == 0) {
            return 1;
        }
        long first = num / offset;
        return (first < 4 ? first : (first - 1)) * arr[len]
                + process(num % offset, offset / 10, len - 1);
    }

    // O(log_{10}^{N})
    public static long notContains4Nums2(long num) {
        if (num <= 0) {
            return 0;
        }
        int len = len(num);
        long offset = offset(len);
        long first = num / offset;
        return arr[len] - 1 + (first - (first < 4 ? 1 : 2)) * arr[len]
                + process(num % offset, offset / 10, len - 1);
    }

    //

    // 9进制转10进制, O(log_{10}^{N})
    public static long notContains4Nums3(long num) {
        if (num <= 0) {
            return 0;
        }
        long ans = 0;
        for (long base = 1, cur = 0; num > 0; num /= 10, base *= 9) {
            cur = num % 10;
            ans += (cur < 4 ? cur : cur - 1) * base;
        }
        return ans;
    }

    public static void main(String[] args) {
        long max = 88888888L;
        System.out.println("test begin");
        for (long i = 0; i <= max; i++) {
            if (isNot4(i) && notContains4Nums2(i) != notContains4Nums3(i)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");

        long num = 8173528638135L;
        long start, end;
        System.out.println("性能测试");

//        start = System.currentTimeMillis();
//        long ans1 = notContains4Nums1(num);
//        end = System.currentTimeMillis();
//        System.out.println(ans1 + ", time : " + (end - start) + " ms");

        start = System.currentTimeMillis();
        long ans2 = notContains4Nums2(num);
        end = System.currentTimeMillis();
        System.out.println(ans2 + ", time : " + (end - start) + " ms");

        start = System.currentTimeMillis();
        long ans3 = notContains4Nums3(num);
        end = System.currentTimeMillis();
        System.out.println(ans3 + ", time : " + (end - start) + " ms");
    }
}
