package basic.c38;

// [1,n]数字中1出现的次数
public class OneNum {
    private static int count1(int num) {
        int res = 0;
        while (num > 0) {
            if (num % 10 == 1) {
                res++;
            }
            num /= 10;
        }
        return res;
    }

    public static int num1(int num) {
        if (num < 1) {
            return 0;
        }
        int count = 0;
        for (int i = 1; i <= num; i++) {
            count += count1(i);
        }
        return count;
    }

    //

    private static int len(int num) {
        int len = 0;
        while (num > 0) {
            len++;
            num /= 10;
        }
        return len;
    }

    private static int power10(int base) {
        return (int) Math.pow(10, base);
    }

    // 数位dp, 每次一位
    public static int num2(int num) {
        if (num < 1) {
            return 0;
        }
        int len = len(num);
        if (len == 1) {
            return 1;
        }
        int tmp1 = power10(len - 1);
        int first = num / tmp1;
        int firstOneNum = first == 1 ? num % tmp1 + 1 : tmp1;
        int otherOneNum = first * (len - 1) * (tmp1 / 10);
        return firstOneNum + otherOneNum + num2(num % tmp1);
    }

    public static void main(String[] args) {
        int num = 50000000;
        long start = System.currentTimeMillis();
        System.out.println(num1(num));
        long end = System.currentTimeMillis();
        System.out.println("cost time: " + (end - start) + " ms");

        start = System.currentTimeMillis();
        System.out.println(num2(num));
        end = System.currentTimeMillis();
        System.out.println("cost time: " + (end - start) + " ms");
    }
}
