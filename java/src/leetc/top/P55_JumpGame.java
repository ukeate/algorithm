package leetc.top;

public class P55_JumpGame {
    public boolean canJump(int[] nums) {
        if (nums == null || nums.length < 2) {
            return true;
        }
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (i > max) {
                return false;
            }
            max = Math.max(max, i + nums[i]);
        }
        return true;
    }
}
