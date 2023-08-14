package giant.c19;

// https://leetcode.cn/problems/1nzheng-shu-zhong-1chu-xian-de-ci-shu-lcof/
// 1~n数字中，字面1出现的次数
public class OneNumber {
    private static int get1Nums(int num) {
        int res = 0;
        while (num > 0) {
            if (num % 10 == 1) {
                res++;
            }
            num /= 10;
        }
        return res;
    }

    public static int sure(int num) {
        if (num < 1) {
            return 0;
        }
        int count = 0;
        for (int i = 1; i <= num; i++) {
            count += get1Nums(i);
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

    public static int countDigitOne(int num) {
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
        return firstOneNum + otherOneNum +countDigitOne(num % tmp1);
    }
}
