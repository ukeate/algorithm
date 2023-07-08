package basic.c44;

// Nim博弈，arr下标位置有值个铜板，
// 轮流拿铜板，在一个位置拿任意数量铜板, 不能不拿，谁先把铜板拿完谁赢
public class Nim {
    public static void win(int[] arr) {
        int eor = 0;
        for (int num : arr) {
            eor ^= num;
        }
        if (eor == 0) {
            System.out.println("后手赢");
        } else {
            System.out.println("先手赢");
        }
    }
}
