package leetc.top;

public class P136_SingleNumber {
    public static int singleNumber(int[] nums) {
        int eor = 0;
        for (int num : nums) {
            eor ^= num;
        }
        return eor;
    }
}
