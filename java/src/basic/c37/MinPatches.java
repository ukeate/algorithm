package basic.c37;

import java.util.Arrays;

/**
 * 最少补充数字问题
 * 
 * 问题描述：
 * 给定一个正整数数组arr和一个目标数aim，数组元素可以任意组合（每个元素最多用一次）。
 * 求最少需要补充多少个数字，使得可以组成1到aim范围内的所有整数。
 * 
 * 例如：
 * arr = [1, 3], aim = 6
 * 当前可以组成：1, 3, 4 (1+3)
 * 缺少：2, 5, 6
 * 补充2后：1, 2, 3, 4, 5 (1+2+3-1), 6 (1+2+3)
 * 所以最少补充1个数字
 * 
 * 核心思想：
 * 维护一个范围[1, range]，表示当前可以组成1到range的所有数字。
 * 对于新数字num：
 * 1. 如果num <= range+1，可以扩展范围到[1, range+num]
 * 2. 如果num > range+1，需要先补充数字range+1来填补空隙
 * 
 * 贪心策略：
 * 每次补充数字时，补充range+1，可以使范围扩展到[1, 2*range+1]，
 * 这是单次补充能达到的最大扩展效果。
 * 
 * 时间复杂度：O(N*logN + log(aim))
 * 空间复杂度：O(1)
 */
public class MinPatches {
    
    /**
     * 计算最少需要补充的数字个数
     * 
     * 算法流程：
     * 1. 对数组排序，按从小到大处理
     * 2. 维护当前可组成范围[1, range]
     * 3. 对于每个数字arr[i]：
     *    - 如果arr[i] > range+1，说明存在空隙，需要补充数字
     *    - 每次补充range+1，使范围扩展到[1, 2*range+1]
     *    - 重复直到arr[i] <= range+1
     *    - 然后将arr[i]加入，扩展范围到[1, range+arr[i]]
     * 4. 处理完所有数字后，如果range < aim，继续补充
     * 
     * 关键洞察：
     * - 如果能组成[1, k]，补充数字k+1后能组成[1, 2k+1]
     * - 数字num能扩展范围当且仅当num <= range+1
     * - 贪心选择：每次补充当前最小不能组成的数
     * 
     * @param arr 正整数数组
     * @param aim 目标上限
     * @return 最少需要补充的数字个数
     */
    public static int min(int[] arr, int aim) {
        int patches = 0;    // 补充的数字个数
        long range = 0;     // 当前可以组成的范围上限[1, range]
        
        // 排序，从小到大处理
        Arrays.sort(arr);
        
        for (int i = 0; i < arr.length; i++) {
            // 处理当前数字arr[i]之前的空隙
            // 如果arr[i] > range+1，说明存在空隙[range+1, arr[i]-1]
            while (arr[i] - 1 > range) {
                // 补充数字range+1，使范围从[1,range]扩展到[1,2*range+1]
                range += range + 1;  // 等价于 range = 2*range + 1
                patches++;
                
                // 提前终止：如果已经能组成[1,aim]，返回结果
                if (range >= aim) {
                    return patches;
                }
            }
            
            // 将当前数字arr[i]加入，扩展范围
            // 从[1,range]扩展到[1,range+arr[i]]
            range += arr[i];
            
            // 提前终止检查
            if (range >= aim) {
                return patches;
            }
        }
        
        // 处理完所有数字后，如果还未达到aim，继续补充
        while (aim >= range + 1) {
            // 补充range+1，扩展范围到[1,2*range+1]
            range += range + 1;
            patches++;
        }
        
        return patches;
    }

    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 最少补充数字问题测试 ===");
        
        // 测试用例1：基本情况
        int[] arr1 = {1, 2, 31, 33};
        int aim1 = 2147483647;
        
        System.out.println("测试用例1：");
        System.out.print("数组: [");
        for (int i = 0; i < arr1.length; i++) {
            System.out.print(arr1[i]);
            if (i < arr1.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("目标: " + aim1);
        System.out.println("最少补充数字个数: " + min(arr1, aim1));
        System.out.println();
        
        // 测试用例2：小规模验证
        int[] arr2 = {1, 3};
        int aim2 = 6;
        
        System.out.println("测试用例2：");
        System.out.print("数组: [");
        for (int i = 0; i < arr2.length; i++) {
            System.out.print(arr2[i]);
            if (i < arr2.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("目标: " + aim2);
        System.out.println("最少补充数字个数: " + min(arr2, aim2));
        System.out.println("分析：[1,3]可组成1,3,4，缺少2,5,6，补充2后可组成1到6");
        System.out.println();
        
        // 测试用例3：空数组
        int[] arr3 = {};
        int aim3 = 5;
        
        System.out.println("测试用例3（空数组）：");
        System.out.println("数组: []");
        System.out.println("目标: " + aim3);
        System.out.println("最少补充数字个数: " + min(arr3, aim3));
        System.out.println("分析：需要补充1,2,4来组成1到5");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
