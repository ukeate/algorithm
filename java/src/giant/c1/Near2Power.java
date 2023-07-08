package giant.c1;

// 返回大于等于n的最小2的某次方
public class Near2Power {
    public static int find(int n) {
        n--;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : n + 1;
    }

    public static void main(String[] args) {
        System.out.println(find(-120));
        System.out.println(-9 >>> 1);
    }
}
