package basic.c37;

/**
 * 最短子数组使数组有序问题
 * 
 * 问题描述：
 * 给定一个整数数组，找到一个最短的连续子数组，将其排序后使得整个数组都变成有序。
 * 返回这个最短子数组的长度。
 * 
 * 例如：
 * 数组 [1, 2, 4, 7, 10, 11, 7, 12, 6, 7, 16, 18, 19]
 * 需要排序的子数组是 [4, 7, 10, 11, 7, 12, 6, 7]（索引3到10）
 * 长度为8
 * 
 * 核心思想：
 * 1. 找到从左侧开始第一个"乱序"的位置（左边界）
 * 2. 找到从右侧开始第一个"乱序"的位置（右边界）
 * 3. "乱序"定义：该位置的元素不在其最终应该在的位置上
 * 
 * 算法策略：
 * - 从右往左找：记录最小值，找到第一个比右侧最小值大的位置（左边界）
 * - 从左往右找：记录最大值，找到第一个比左侧最大值小的位置（右边界）
 * 
 * 时间复杂度：O(N)
 * 空间复杂度：O(1)
 */
public class MinSubForSort {
    
    /**
     * 计算最短子数组长度使整个数组有序
     * 
     * 算法思路：
     * 1. 从右往左扫描，维护右侧的最小值
     *    - 如果当前元素 > 右侧最小值，说明该位置需要调整
     *    - 记录最左侧需要调整的位置（noMinIdx）
     * 
     * 2. 从左往右扫描，维护左侧的最大值
     *    - 如果当前元素 < 左侧最大值，说明该位置需要调整
     *    - 记录最右侧需要调整的位置（noMaxIdx）
     * 
     * 3. 返回需要调整的区间长度
     * 
     * 核心洞察：
     * - 一个元素需要调整，当且仅当它破坏了单调性
     * - 左边界：从右往左第一个比右侧最小值大的位置
     * - 右边界：从左往右最后一个比左侧最大值小的位置
     * 
     * @param arr 输入数组
     * @return 需要排序的最短子数组长度
     */
    public static int min(int[] arr) {
        // 边界条件：null或长度小于2的数组已经有序
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        // 第一步：从右往左找左边界
        int min = arr[arr.length - 1];  // 从右侧开始的最小值
        int noMinIdx = -1;              // 最左侧不满足条件的位置
        
        // 从倒数第二个元素开始向左扫描
        for (int i = arr.length - 2; i >= 0; i--) {
            if (arr[i] > min) {
                // 当前元素比右侧最小值大，说明位置不对，需要调整
                noMinIdx = i;
            } else {
                // 更新右侧最小值
                min = arr[i];
            }
        }
        
        // 如果noMinIdx为-1，说明数组已经是非递减的，无需调整
        if (noMinIdx == -1) {
            return 0;
        }
        
        // 第二步：从左往右找右边界
        int max = arr[0];       // 从左侧开始的最大值
        int noMaxIdx = -1;      // 最右侧不满足条件的位置
        
        // 从第二个元素开始向右扫描
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < max) {
                // 当前元素比左侧最大值小，说明位置不对，需要调整
                noMaxIdx = i;
            } else {
                // 更新左侧最大值
                max = arr[i];
            }
        }
        
        // 返回需要调整的区间长度
        return noMaxIdx - noMinIdx + 1;
    }

    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 最短子数组使数组有序测试 ===");
        
        // 测试用例1：给定示例
        int[] arr1 = {1, 2, 4, 7, 10, 11, 7, 12, 6, 7, 16, 18, 19};
        
        System.out.println("测试用例1：");
        System.out.print("数组: [");
        for (int i = 0; i < arr1.length; i++) {
            System.out.print(arr1[i]);
            if (i < arr1.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("最短子数组长度: " + min(arr1));
        System.out.println("分析：需要排序的部分是索引3到10，长度为8");
        System.out.println();
        
        // 测试用例2：已经有序
        int[] arr2 = {1, 2, 3, 4, 5};
        
        System.out.println("测试用例2（已有序）：");
        System.out.print("数组: [");
        for (int i = 0; i < arr2.length; i++) {
            System.out.print(arr2[i]);
            if (i < arr2.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("最短子数组长度: " + min(arr2));
        System.out.println();
        
        // 测试用例3：完全逆序
        int[] arr3 = {5, 4, 3, 2, 1};
        
        System.out.println("测试用例3（完全逆序）：");
        System.out.print("数组: [");
        for (int i = 0; i < arr3.length; i++) {
            System.out.print(arr3[i]);
            if (i < arr3.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("最短子数组长度: " + min(arr3));
        System.out.println("分析：整个数组都需要排序");
        System.out.println();
        
        // 测试用例4：只有中间部分乱序
        int[] arr4 = {1, 2, 5, 3, 4, 6, 7};
        
        System.out.println("测试用例4（中间乱序）：");
        System.out.print("数组: [");
        for (int i = 0; i < arr4.length; i++) {
            System.out.print(arr4[i]);
            if (i < arr4.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("最短子数组长度: " + min(arr4));
        System.out.println("分析：需要排序索引2到4的部分[5,3,4]");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
