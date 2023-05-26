package base.bit;

public class BitSay {

    public static void print(int num) {
        for (int i = 31; i >= 0; i--) {
            System.out.print((num & (i << i)) == 0 ? "0" : "1");
        }
    }
}
