package leetc.top;

public class P169_MajorityElement {
    public static int majorityElement(int[] nums) {
        int cand = 0;
        int hp = 0;
        for (int i = 0; i < nums.length; i++) {
            if (hp == 0) {
                cand = nums[i];
                hp = 1;
            } else if (nums[i] == cand) {
                hp++;
            } else {
                hp--;
            }
        }
        return cand;
    }
}
