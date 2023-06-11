package base.direct;

public class IsContinuousSum {
    public static boolean is1(int num) {
        for (int i = 1; i <= num; i++) {
            int sum = i;
            for (int j = i + 1; j <= num; j++) {
                if (sum +j > num) {
                    break;
                }
                if (sum + j == num) {
                    return true;
                }
                sum += j;
            }
        }
        return false;
    }

    public static boolean is2(int num) {
//        return num == (num & -num);
        return (num & (num - 1)) != 0;
    }

    public static void main(String[] args) {
        for (int num = 1; num < 200; num++) {
            System.out.println(num + ":" + is1(num));
        }
        System.out.println("test begin");
        for (int num = 1; num < 5000; num++) {
            if (is1(num) != is2(num)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
