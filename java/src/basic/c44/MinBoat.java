package basic.c44;

/**
 * 救生艇最小数量问题
 * 
 * 问题描述：
 * 给定一个有序的人员体重数组和船的载重限制
 * 每艘船最多只能坐2个人，且总重量不能超过载重限制
 * 求最少需要多少艘船才能载完所有人
 * 
 * 算法思路：
 * 贪心策略 + 双指针技巧
 * 1. 将人员分为两组：轻的(≤limit/2)和重的(>limit/2)
 * 2. 重的人只能单独坐船或与轻的人配对
 * 3. 轻的人可以两两配对或与重的人配对
 * 4. 使用双指针优化配对过程
 * 
 * 关键洞察：
 * - 两个重的人(>limit/2)无法同船
 * - 重的人优先与轻的人配对，剩余的轻的人两两配对
 * - 轻的人中无法配对的最多只剩1个
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * 
 * @author 算法学习
 */
public class MinBoat {
    
    /**
     * 计算最少需要的船只数量
     * 
     * @param arr 有序的人员体重数组
     * @param limit 船的载重限制
     * @return 最少船只数量，如果无法载完返回-1
     * 
     * 算法步骤：
     * 1. 找到轻重分界点（≤limit/2的最右位置）
     * 2. 使用双指针让重的人与轻的人配对
     * 3. 计算剩余轻的人的船只需求
     * 4. 统计总船只数量
     */
    public static int min(int[] arr, int limit) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        
        // 检查最重的人是否超过船的载重
        if (arr[n - 1] > limit) {
            return -1; // 无法载完
        }
        
        // 找到体重≤limit/2的最右位置
        int lessR = -1;
        for (int i = n - 1; i >= 0; i--) {
            if (arr[i] <= (limit / 2)) {
                lessR = i;
                break;
            }
        }
        
        // 特殊情况：所有人都是重的(>limit/2)
        if (lessR == -1) {
            return n; // 每人单独一艘船
        }
        
        // 双指针配对策略
        int l = lessR;           // 轻的人的右边界（向左移动）
        int r = lessR + 1;       // 重的人的左边界（向右移动）
        int noUsed = 0;          // 轻的人中无法与重的人配对的数量
        
        // 让重的人与轻的人配对
        while (l >= 0) {
            int solved = 0;      // 当前轻的人能配对的重的人数量
            
            // 尝试让arr[l]与尽可能多的重的人配对
            while (r < n && arr[l] + arr[r] <= limit) {
                r++;
                solved++;
            }
            
            if (solved == 0) {
                // 当前轻的人无法与任何重的人配对
                noUsed++;
                l--;
            } else {
                // 当前轻的人成功配对，消耗solved个轻的人
                l = Math.max(-1, l - solved);
            }
        }
        
        // 计算船只数量
        int leftAll = lessR + 1;                    // 轻的人总数
        int used = leftAll - noUsed;                // 与重的人配对的轻的人数
        int left = (noUsed + 1) / 2;               // 剩余轻的人需要的船数
        int right = (n - leftAll) - used;          // 单独坐船的重的人数
        
        return used + left + right;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        int[] arr = {1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 5};
        int weight = 6;
        
        System.out.println("人员体重: " + java.util.Arrays.toString(arr));
        System.out.println("载重限制: " + weight);
        System.out.println("最少船只数量: " + min(arr, weight));
        
        // 测试边界情况
        int[] test1 = {1, 2};
        System.out.println("\n测试1 - 轻量组合: " + java.util.Arrays.toString(test1) + 
                          ", 载重: 3, 需要船只: " + min(test1, 3));
        
        int[] test2 = {5, 5, 5, 5};
        System.out.println("测试2 - 全重量: " + java.util.Arrays.toString(test2) + 
                          ", 载重: 6, 需要船只: " + min(test2, 6));
        
        int[] test3 = {3, 2, 2, 1};
        System.out.println("测试3 - 混合重量: " + java.util.Arrays.toString(test3) + 
                          ", 载重: 3, 需要船只: " + min(test3, 3));
    }
}
