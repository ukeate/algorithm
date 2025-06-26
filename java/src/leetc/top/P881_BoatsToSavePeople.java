package leetc.top;

import java.util.Arrays;

/**
 * LeetCode 881. 救生艇 (Boats to Save People)
 * 
 * 问题描述：
 * 给定数组 people，其中 people[i] 是第 i 个人的体重，船的数量不限，
 * 每艘船可以承载的最大重量为 limit。
 * 每艘船最多可同时载两个人，但条件是这些人的重量之和最多为 limit。
 * 返回载到每个人所需的最少船数。
 * 
 * 示例：
 * - 输入：people = [1,2], limit = 3
 * - 输出：1
 * - 解释：1 艘船载 (1, 2)
 * 
 * - 输入：people = [3,2,2,1], limit = 3
 * - 输出：3
 * - 解释：3 艘船分别载 (1, 2), (2) 和 (3)
 * 
 * - 输入：people = [3,5,3,4], limit = 5
 * - 输出：4
 * - 解释：4 艘船分别载 (3), (3), (4), (5)
 * 
 * 解法思路：
 * 双指针贪心算法：
 * 1. 排序数组，让轻的人和重的人从两端开始匹配
 * 2. 如果最轻的人和最重的人能同船，则两人一起上船
 * 3. 否则只让最重的人上船（因为他无法和任何人配对）
 * 4. 重复此过程直到所有人都上船
 * 
 * 核心思想：
 * - 贪心策略：总是试图让最重的人和最轻的人配对
 * - 双指针：从数组两端向中间收缩
 * - 最优性：如果最重的人无法和最轻的人配对，则无法和任何人配对
 * 
 * 时间复杂度：O(nlogn) - 排序的时间复杂度
 * 空间复杂度：O(1) - 除排序外只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/boats-to-save-people/
 */
public class P881_BoatsToSavePeople {
    
    /**
     * 复杂解法：分析轻重人群的配对策略
     * 这是一个更复杂但教学性强的解法，展示了详细的分析过程
     * 
     * @param arr 人员体重数组
     * @param limit 船的载重限制
     * @return 最少需要的船数
     */
    public int numRescueBoats(int[] arr, int limit) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        Arrays.sort(arr);
        
        // 检查是否有人超重（无法上船）
        if (arr[n - 1] > limit) {
            return -1;
        }
        
        // 找到轻人群和重人群的分界点
        // 轻人群：体重 <= limit/2，重人群：体重 > limit/2
        int leftR = -1;
        for (int i = n - 1; i >= 0; i--) {
            if (arr[i] <= (limit / 2)) {
                leftR = i;
                break;
            }
        }
        
        // 如果没有轻人，所有人都是重人，每人一艘船
        if (leftR == -1) {
            return n;
        }
        
        int l = leftR, r = leftR + 1; // l指向轻人，r指向重人
        int leftUnused = 0; // 未配对的轻人数量
        
        // 配对过程：每个轻人尝试和重人配对
        while (l >= 0) {
            int rightUsed = 0; // 当前轻人能配对的重人数量
            
            // 当前轻人尽可能多地配对重人
            while (r < n && arr[l] + arr[r] <= limit) {
                r++;
                rightUsed++;
            }
            
            if (rightUsed == 0) {
                // 当前轻人无法配对任何重人
                leftUnused++;
                l--;
            } else {
                // 当前轻人配对了rightUsed个重人
                // 消耗rightUsed个轻人
                l = Math.max(-1, l - rightUsed);
            }
        }
        
        // 计算结果
        int leftAll = leftR + 1;                    // 总轻人数
        int leftUsed = leftAll - leftUnused;        // 已配对的轻人数
        int rightUnused = (n - leftAll) - leftUsed; // 未配对的重人数
        
        // 总船数 = 已配对的轻人数 + 未配对轻人的船数 + 未配对重人数
        return leftUsed + ((leftUnused + 1) >> 1) + rightUnused;
    }

    /**
     * 简洁解法：双指针贪心算法
     * 这是推荐的标准解法，简洁高效
     * 
     * 算法步骤：
     * 1. 排序数组
     * 2. 使用左右双指针分别指向最轻和最重的人
     * 3. 如果两人重量和不超过限制，两人同船，双指针都移动
     * 4. 否则只让重人上船，右指针左移
     * 5. 每次循环消耗一艘船
     * 
     * 贪心正确性证明：
     * - 如果最重的人能和最轻的人同船，这是最优选择
     * - 如果最重的人不能和最轻的人同船，他就不能和任何人同船
     * 
     * @param arr 人员体重数组
     * @param limit 船的载重限制
     * @return 最少需要的船数
     */
    public static int numRescueBoats2(int[] arr, int limit) {
        Arrays.sort(arr);
        int l = 0, r = arr.length - 1; // 双指针：左指向最轻，右指向最重
        int ans = 0; // 船数计数
        
        while (l <= r) {
            // 计算当前考虑的重量和
            int sum = (l == r) ? arr[l] : arr[l] + arr[r];
            
            if (sum > limit) {
                // 两人太重，只让重人（右指针）上船
                r--;
            } else {
                // 两人可以同船，或者只有一人
                l++;
                r--;
            }
            
            // 每次循环消耗一艘船
            ans++;
        }
        
        return ans;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：基本示例
        int[] people1 = {1, 2};
        int limit1 = 3;
        System.out.println("测试用例1:");
        System.out.println("people = " + java.util.Arrays.toString(people1));
        System.out.println("limit = " + limit1);
        System.out.println("输出: " + numRescueBoats2(people1, limit1));
        System.out.println("期望: 1");
        System.out.println("方案: (1,2)一艘船");
        System.out.println();
        
        // 测试用例2：混合重量
        int[] people2 = {3, 2, 2, 1};
        int limit2 = 3;
        System.out.println("测试用例2:");
        System.out.println("people = " + java.util.Arrays.toString(people2));
        System.out.println("limit = " + limit2);
        System.out.println("输出: " + numRescueBoats2(people2, limit2));
        System.out.println("期望: 3");
        System.out.println("方案: (1,2), (2), (3)三艘船");
        System.out.println();
        
        // 测试用例3：都无法配对
        int[] people3 = {3, 5, 3, 4};
        int limit3 = 5;
        System.out.println("测试用例3 (无法配对):");
        System.out.println("people = " + java.util.Arrays.toString(people3));
        System.out.println("limit = " + limit3);
        System.out.println("输出: " + numRescueBoats2(people3, limit3));
        System.out.println("期望: 4");
        System.out.println("方案: (3), (3), (4), (5)四艘船");
        System.out.println();
        
        // 测试用例4：轻重搭配
        int[] people4 = {1, 2, 3, 4, 5};
        int limit4 = 5;
        System.out.println("测试用例4 (轻重搭配):");
        System.out.println("people = " + java.util.Arrays.toString(people4));
        System.out.println("limit = " + limit4);
        System.out.println("输出: " + numRescueBoats2(people4, limit4));
        System.out.println("期望: 3");
        System.out.println("方案: (1,4), (2,3), (5)三艘船");
        System.out.println();
        
        // 测试用例5：极端情况
        int[] people5 = {1, 1, 1, 1};
        int limit5 = 2;
        System.out.println("测试用例5 (都很轻):");
        System.out.println("people = " + java.util.Arrays.toString(people5));
        System.out.println("limit = " + limit5);
        System.out.println("输出: " + numRescueBoats2(people5, limit5));
        System.out.println("期望: 2");
        System.out.println("方案: (1,1), (1,1)两艘船");
        System.out.println();
        
        // 测试用例6：单人
        int[] people6 = {5};
        int limit6 = 5;
        System.out.println("测试用例6 (单人):");
        System.out.println("people = " + java.util.Arrays.toString(people6));
        System.out.println("limit = " + limit6);
        System.out.println("输出: " + numRescueBoats2(people6, limit6));
        System.out.println("期望: 1");
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点:");
        System.out.println("- 核心思想: 双指针贪心，最重配最轻");
        System.out.println("- 时间复杂度: O(nlogn) - 排序主导");
        System.out.println("- 空间复杂度: O(1) - 只用常数额外空间");
        System.out.println("- 贪心策略: 如果最重的人不能和最轻的人配对，就不能和任何人配对");
        System.out.println("- 最优性: 每次都做出局部最优选择，全局最优");
        System.out.println("- 实际应用: 资源分配、负载均衡等场景");
    }
}
