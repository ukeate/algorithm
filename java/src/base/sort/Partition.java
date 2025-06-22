package base.sort;

/**
 * 数组分区问题 - 快速排序的核心操作
 * 
 * 问题描述：
 * 给定一个数组和一个目标值，将数组重新排列，使得：
 * - 小于目标值的元素都在左边
 * - 等于目标值的元素都在中间
 * - 大于目标值的元素都在右边
 * 
 * 这是荷兰国旗问题的经典实现，也是快速排序算法的核心操作
 * 
 * 例如：数组[3,1,4,1,5,9,2,6,5]，目标值为5
 * 分区后：[3,1,4,1,2] [5,5] [9,6] （顺序可能不同，但分区正确）
 * 
 * 算法原理：
 * 使用三个指针进行分区：
 * 1. less指针：指向小于区域的右边界
 * 2. more指针：指向大于区域的左边界
 * 3. cur指针：当前处理的元素
 * 
 * 分区策略：
 * - arr[cur] < target：与less+1位置交换，less++，cur++
 * - arr[cur] = target：cur++
 * - arr[cur] > target：与more-1位置交换，more--，cur不动
 * 
 * 时间复杂度：O(N) - 每个元素只访问一次
 * 空间复杂度：O(1) - 原地分区，只使用常数额外空间
 * 
 * 应用场景：
 * - 快速排序的分区操作
 * - 荷兰国旗问题
 * - 三路快排的核心
 * - 数组元素分类问题
 */
public class Partition {

    /**
     * 数组分区主函数 - 荷兰国旗问题的实现
     * 
     * 算法详解：
     * 维护三个区域：
     * [0, less] - 小于target的区域
     * [less+1, more-1] - 等于target的区域
     * [more, arr.length-1] - 大于target的区域
     * 
     * 处理过程：
     * 1. 初始化：less=-1, more=arr.length, cur=0
     * 2. 当cur < more时循环处理：
     *    - 如果arr[cur] < target：交换arr[cur]和arr[less+1]，less++，cur++
     *    - 如果arr[cur] = target：cur++
     *    - 如果arr[cur] > target：交换arr[cur]和arr[more-1]，more--，cur不变
     * 
     * 举例说明（数组[3,1,5,1,5,9,2,6,5]，target=5）：
     * 初始：less=-1, more=9, cur=0
     * 处理3：3<5，交换后[1,3,5,1,5,9,2,6,5]，less=0，cur=1
     * 处理3：3<5，交换后[1,1,5,3,5,9,2,6,5]，less=1，cur=2
     * 处理5：5=5，cur=3
     * 处理3：3<5，交换后[1,1,3,5,5,9,2,6,5]，less=2，cur=4
     * ... 继续处理直到完成
     * 
     * @param arr 待分区的数组
     * @param target 分区的目标值
     * @return 返回等于target区域的边界[left, right]
     */
    public static int[] partition(int[] arr, int target) {
        if (arr == null || arr.length == 0) {
            return new int[]{-1, -1};  // 空数组返回无效边界
        }
        
        int less = -1;           // 小于区域的右边界
        int more = arr.length;   // 大于区域的左边界
        int cur = 0;             // 当前处理位置
        
        // 核心分区循环
        while (cur < more) {
            if (arr[cur] < target) {
                // 当前元素小于目标值，放入小于区域
                swap(arr, ++less, cur++);
            } else if (arr[cur] == target) {
                // 当前元素等于目标值，保持在等于区域
                cur++;
            } else {
                // 当前元素大于目标值，放入大于区域
                // 注意：cur不自增，因为交换过来的元素还未处理
                swap(arr, --more, cur);
            }
        }
        
        // 返回等于target区域的边界
        return new int[]{less + 1, more - 1};
    }

    /**
     * 交换数组中两个位置的元素
     * 
     * @param arr 数组
     * @param i 第一个位置
     * @param j 第二个位置
     */
    private static void swap(int[] arr, int i, int j) {
        if (i != j) {  // 避免相同位置的无意义交换
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    /**
     * 验证分区结果是否正确
     * 
     * @param arr 分区后的数组
     * @param target 目标值
     * @param bounds 等于区域的边界
     * @return 分区是否正确
     */
    private static boolean verifyPartition(int[] arr, int target, int[] bounds) {
        if (arr == null || bounds == null || bounds.length != 2) {
            return false;
        }
        
        int left = bounds[0];
        int right = bounds[1];
        
        // 检查小于区域
        for (int i = 0; i < left; i++) {
            if (arr[i] >= target) {
                return false;
            }
        }
        
        // 检查等于区域
        for (int i = left; i <= right; i++) {
            if (arr[i] != target) {
                return false;
            }
        }
        
        // 检查大于区域
        for (int i = right + 1; i < arr.length; i++) {
            if (arr[i] <= target) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param maxLen 最大数组长度
     * @param maxVal 数组元素的最大绝对值
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    /**
     * 复制数组
     * 
     * @param arr 原数组
     * @return 复制的数组
     */
    private static int[] copy(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    /**
     * 打印数组
     * 
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        if (arr == null) {
            System.out.println("null");
            return;
        }
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    /**
     * 打印分区结果
     * 
     * @param arr 分区后的数组
     * @param target 目标值
     * @param bounds 等于区域的边界
     */
    private static void printPartitionResult(int[] arr, int target, int[] bounds) {
        System.out.println("分区目标值：" + target);
        System.out.print("分区后数组：");
        print(arr);
        
        if (bounds[0] <= bounds[1]) {
            System.out.println("等于区域：[" + bounds[0] + ", " + bounds[1] + "]");
            
            // 分别打印三个区域
            System.out.print("小于区域：");
            if (bounds[0] > 0) {
                for (int i = 0; i < bounds[0]; i++) {
                    System.out.print(arr[i] + " ");
                }
            } else {
                System.out.print("无");
            }
            System.out.println();
            
            System.out.print("等于区域：");
            for (int i = bounds[0]; i <= bounds[1]; i++) {
                System.out.print(arr[i] + " ");
            }
            System.out.println();
            
            System.out.print("大于区域：");
            if (bounds[1] < arr.length - 1) {
                for (int i = bounds[1] + 1; i < arr.length; i++) {
                    System.out.print(arr[i] + " ");
                }
            } else {
                System.out.print("无");
            }
            System.out.println();
        } else {
            System.out.println("等于区域：无（目标值不存在）");
        }
    }

    /**
     * 测试方法 - 验证分区算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 数组分区问题测试 ===");
        
        // 手工测试用例1
        int[] testArr1 = {3, 1, 4, 1, 5, 9, 2, 6, 5};
        int target1 = 5;
        System.out.println("\n测试用例1：");
        System.out.print("原数组：");
        print(testArr1);
        
        int[] testCopy1 = copy(testArr1);
        int[] bounds1 = partition(testCopy1, target1);
        printPartitionResult(testCopy1, target1, bounds1);
        System.out.println("分区正确性：" + verifyPartition(testCopy1, target1, bounds1));
        
        // 手工测试用例2 - 目标值不存在
        int[] testArr2 = {1, 3, 2, 4, 6};
        int target2 = 5;
        System.out.println("\n测试用例2（目标值不存在）：");
        System.out.print("原数组：");
        print(testArr2);
        
        int[] testCopy2 = copy(testArr2);
        int[] bounds2 = partition(testCopy2, target2);
        printPartitionResult(testCopy2, target2, bounds2);
        System.out.println("分区正确性：" + verifyPartition(testCopy2, target2, bounds2));
        
        // 手工测试用例3 - 所有元素都相等
        int[] testArr3 = {5, 5, 5, 5, 5};
        int target3 = 5;
        System.out.println("\n测试用例3（所有元素都相等）：");
        System.out.print("原数组：");
        print(testArr3);
        
        int[] testCopy3 = copy(testArr3);
        int[] bounds3 = partition(testCopy3, target3);
        printPartitionResult(testCopy3, target3, bounds3);
        System.out.println("分区正确性：" + verifyPartition(testCopy3, target3, bounds3));
        
        // 大规模随机测试
        System.out.println("\n=== 随机测试 ===");
        int times = 100000;
        int maxLen = 100;
        int maxVal = 50;
        System.out.println("测试次数：" + times);
        System.out.println("最大数组长度：" + maxLen);
        System.out.println("最大元素绝对值：" + maxVal);
        
        boolean success = true;
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int target = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
            
            int[] testArr = copy(arr);
            int[] bounds = partition(testArr, target);
            
            if (!verifyPartition(testArr, target, bounds)) {
                System.out.println("第" + (i + 1) + "次测试失败！");
                System.out.print("原数组：");
                print(arr);
                printPartitionResult(testArr, target, bounds);
                success = false;
                break;
            }
        }
        
        if (success) {
            System.out.println("所有测试通过！数组分区算法实现正确。");
        } else {
            System.out.println("测试失败！请检查算法实现。");
        }
        
        System.out.println("=== 测试结束 ===");
    }
}
