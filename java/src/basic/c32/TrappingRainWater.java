package basic.c32;

/**
 * 接雨水问题（直方图最大存水量）
 * 
 * 问题描述：给定一个非负整数数组，表示直方图的高度，
 * 计算下雨后能够接到多少雨水。
 * 
 * 解题思想：
 * 对于位置i，其能接到的雨水量 = min(左侧最大值, 右侧最大值) - 当前高度
 * 但需要保证结果非负。
 * 
 * 提供四种不同复杂度的解决方案：
 * 1. 暴力法：每个位置都重新计算左右最大值 - O(N²)
 * 2. 预处理法：预先计算左右最大值数组 - O(N)
 * 3. 空间优化法：只预处理右侧最大值，左侧实时计算 - O(N)
 * 4. 双指针法：从两端向中间移动，最优解 - O(N)
 */
public class TrappingRainWater {
    
    /**
     * 方法1：暴力法
     * 对每个位置都重新计算其左侧和右侧的最大值
     * 时间复杂度：O(N²)
     * 空间复杂度：O(1)
     * 
     * @param arr 直方图高度数组
     * @return 能接到的雨水总量
     */
    public static int water1(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int n = arr.length;
        int water = 0;
        
        // 除了两端，中间的每个位置都可能接雨水
        for (int i = 1; i < n - 1; i++) {
            // 找左侧最大值
            int leftMax = Integer.MIN_VALUE;
            for (int j = 0; j < i; j++) {
                leftMax = Math.max(leftMax, arr[j]);
            }
            
            // 找右侧最大值
            int rightMax = Integer.MIN_VALUE;
            for (int j = i + 1; j < n; j++) {
                rightMax = Math.max(rightMax, arr[j]);
            }
            
            // 计算当前位置能接到的雨水量
            water += Math.max(Math.min(leftMax, rightMax) - arr[i], 0);
        }
        
        return water;
    }

    /**
     * 方法2：预处理法
     * 预先计算每个位置的左侧和右侧最大值，避免重复计算
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
     * 
     * @param arr 直方图高度数组
     * @return 能接到的雨水总量
     */
    public static int water2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int n = arr.length;
        
        // 预处理左侧最大值数组
        int[] leftMaxs = new int[n];
        leftMaxs[0] = arr[0];
        for (int i = 1; i < n; i++) {
            leftMaxs[i] = Math.max(leftMaxs[i - 1], arr[i]);
        }
        
        // 预处理右侧最大值数组
        int[] rightMaxs = new int[n];
        rightMaxs[n - 1] = arr[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMaxs[i] = Math.max(rightMaxs[i + 1], arr[i]);
        }
        
        // 计算每个位置的雨水量
        int water = 0;
        for (int i = 1; i < n - 1; i++) {
            water += Math.max(Math.min(leftMaxs[i - 1], rightMaxs[i + 1]) - arr[i], 0);
        }
        
        return water;
    }

    /**
     * 方法3：空间优化法
     * 只预处理右侧最大值数组，左侧最大值在遍历过程中实时计算
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)（只需要一个辅助数组）
     * 
     * @param arr 直方图高度数组
     * @return 能接到的雨水总量
     */
    public static int water3(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int n = arr.length;
        
        // 只预处理右侧最大值数组
        int[] rightMaxs = new int[n];
        rightMaxs[n - 1] = arr[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMaxs[i] = Math.max(rightMaxs[i + 1], arr[i]);
        }
        
        int water = 0;
        int leftMax = arr[0];  // 左侧最大值实时维护
        
        for (int i = 1; i < n - 1; i++) {
            // 使用实时维护的左侧最大值和预处理的右侧最大值
            water += Math.max(Math.min(leftMax, rightMaxs[i + 1]) - arr[i], 0);
            
            // 更新左侧最大值
            leftMax = Math.max(leftMax, arr[i]);
        }
        
        return water;
    }

    /**
     * 方法4：双指针法（最优解）
     * 从两端向中间移动，每次处理较低一侧的位置
     * 时间复杂度：O(N)
     * 空间复杂度：O(1)
     * 
     * 核心思想：
     * - 如果leftMax <= rightMax，则左指针位置的雨水量只取决于leftMax
     * - 否则右指针位置的雨水量只取决于rightMax
     * - 这是因为较低一侧的最大值已经确定了该位置的雨水上限
     * 
     * @param arr 直方图高度数组
     * @return 能接到的雨水总量
     */
    public static int water4(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int n = arr.length;
        int l = 1;                  // 左指针，从第二个元素开始
        int leftMax = arr[0];       // 左侧目前为止的最大值
        int r = n - 2;              // 右指针，从倒数第二个元素开始
        int rightMax = arr[n - 1];  // 右侧目前为止的最大值
        int water = 0;
        
        while (l <= r) {
            if (leftMax <= rightMax) {
                // 左侧最大值较小，处理左指针位置
                // 此时左指针位置的雨水量只取决于leftMax
                water += Math.max(0, leftMax - arr[l]);
                leftMax = Math.max(leftMax, arr[l++]);
            } else {
                // 右侧最大值较小，处理右指针位置
                // 此时右指针位置的雨水量只取决于rightMax
                water += Math.max(0, rightMax - arr[r]);
                rightMax = Math.max(rightMax, arr[r--]);
            }
        }
        
        return water;
    }

    //

    public static int[] randomArr(int len, int maxVal) {
        int[] ans = new int[(int) (Math.random() * len) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (Math.random() * maxVal) + 1;
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 100;
        int maxVal = 200;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int ans1 = water1(arr);
            int ans2 = water2(arr);
            int ans3 = water3(arr);
            int ans4 = water4(arr);
            if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
