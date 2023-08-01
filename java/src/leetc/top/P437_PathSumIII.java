package leetc.top;

import java.util.HashMap;

public class P437_PathSumIII {
    public class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
    }

    private static int process(TreeNode x, int sum, long pre, HashMap<Long, Integer> preSumMap) {
        if (x == null) {
            return 0;
        }
        long all = pre + x.val;
        int ans = 0;
        if (preSumMap.containsKey(all - sum)) {
            ans = preSumMap.get(all - sum);
        }
        if (!preSumMap.containsKey(all)) {
            preSumMap.put(all, 1);
        } else {
            preSumMap.put(all, preSumMap.get(all) + 1);
        }
        ans += process(x.left, sum, all, preSumMap);
        ans += process(x.right, sum, all, preSumMap);
        if (preSumMap.get(all) == 1) {
            preSumMap.remove(all);
        } else {
            preSumMap.put(all, preSumMap.get(all) - 1);
        }
        return ans;
    }

    public static int pathSum(TreeNode root, int sum) {
        HashMap<Long, Integer> preSumMap = new HashMap<>();
        preSumMap.put(0L, 1);
        return process(root, sum, 0, preSumMap);
    }
}
