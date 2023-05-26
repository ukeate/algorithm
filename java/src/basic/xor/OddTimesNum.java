package basic.xor;

public class OddTimesNum {
    // 只有一种数出现奇数次
    public static int oddTimesNum(int[] arr) {
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        return eor;
    }

    // 只有2种数出现奇数次
    public static int[] oddTimesTwoNum(int[] arr) {
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        int rightOne = eor & (-eor);

        int num = 0;
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] & rightOne) != 0) {
                num ^= arr[i];
            }
        }
        return new int[]{num, eor ^ num};
    }

    public static void main(String[] args) {

    }
}
