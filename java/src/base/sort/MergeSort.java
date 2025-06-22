package base.sort;

/**
 * 归并排序算法 - 经典的分治排序算法
 * 
 * 算法原理：
 * 基于分治思想，将大问题分解为小问题求解：
 * 1. 分治(Divide)：将数组不断二分，直到每个子数组只有一个元素
 * 2. 解决(Conquer)：单个元素天然有序，无需处理
 * 3. 合并(Combine)：将两个有序的子数组合并成一个有序数组
 * 
 * 核心思想：
 * "分而治之" - 大问题化解为小问题，小问题的解合并成大问题的解
 * 
 * 算法特点：
 * - 时间复杂度：O(N*logN) - 所有情况下都是这个复杂度，非常稳定
 * - 空间复杂度：O(N) - 需要额外的辅助数组存储合并结果
 * - 稳定性：稳定排序 - 相等元素的相对顺序不会改变
 * - 分治算法的经典应用
 * 
 * 递归过程分析（以[3,1,4,2]为例）：
 *            [3,1,4,2]
 *           /         \
 *       [3,1]         [4,2]
 *       /   \         /   \
 *     [3]   [1]     [4]   [2]
 *       \   /         \   /
 *       [1,3]         [2,4]
 *           \         /
 *           [1,2,3,4]
 * 
 * 优势：
 * 1. 时间复杂度稳定，不受输入数据影响
 * 2. 适合大数据量排序
 * 3. 可以很好地并行化
 * 4. 外部排序的基础算法
 * 
 * 应用场景：
 * - 大数据排序
 * - 外部排序（数据量超过内存）
 * - 需要稳定排序的场合
 * - 链表排序的首选算法
 */
public class MergeSort {

    /**
     * 合并两个有序数组的核心算法
     * 
     * 算法思路：
     * 1. 创建临时数组存储合并结果
     * 2. 使用双指针分别指向两个有序部分的开始
     * 3. 比较两个指针指向的元素，将较小的放入临时数组
     * 4. 移动对应指针，重复步骤3
     * 5. 将剩余元素依次放入临时数组
     * 6. 将临时数组的内容复制回原数组
     * 
     * 时间复杂度：O(N) - 每个元素只需要访问一次
     * 空间复杂度：O(N) - 需要额外的辅助数组
     * 
     * @param arr 原数组
     * @param l 左边界（包含）
     * @param m 中点位置（包含）
     * @param r 右边界（包含）
     */
    private static void merge(int[] arr, int l, int m, int r) {
        // 创建临时数组存储合并结果
        int[] help = new int[r - l + 1];
        int i = 0;          // 临时数组的索引
        int p1 = l;         // 左半部分的指针
        int p2 = m + 1;     // 右半部分的指针
        
        // 合并两个有序部分
        while (p1 <= m && p2 <= r) {
            // 比较两个指针指向的元素，将较小的放入临时数组
            // 当相等时优先选择左半部分的元素，保证稳定性
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        
        // 处理剩余元素
        // 左半部分还有剩余
        while (p1 <= m) {
            help[i++] = arr[p1++];
        }
        // 右半部分还有剩余
        while (p2 <= r) {
            help[i++] = arr[p2++];
        }
        
        // 将合并结果复制回原数组
        for (i = 0; i < help.length; i++) {
            arr[l + i] = help[i];
        }
    }

    /**
     * 递归版本的归并排序核心递归函数
     * 
     * 递归三要素：
     * 1. 递归边界：l == r时，只有一个元素，天然有序
     * 2. 递归关系：分别排序左右两半，然后合并
     * 3. 递归参数：当前需要排序的区间[l, r]
     * 
     * @param arr 待排序数组
     * @param l 左边界（包含）
     * @param r 右边界（包含）
     */
    private static void process(int[] arr, int l, int r) {
        if (l == r) {
            // 递归边界：只有一个元素，已经有序
            return;
        }
        
        // 计算中点，避免整数溢出
        int m = l + ((r - l) >> 1);
        
        // 递归排序左半部分
        process(arr, l, m);
        // 递归排序右半部分
        process(arr, m + 1, r);
        // 合并两个有序部分
        merge(arr, l, m, r);
    }

    /**
     * 归并排序主函数 - 递归版本
     * 
     * @param arr 待排序数组
     */
    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        process(arr, 0, arr.length - 1);
    }

    /**
     * 归并排序的迭代版本 - 自底向上的合并
     * 
     * 算法思路：
     * 1. 从最小的子数组开始（长度为1），逐步扩大合并范围
     * 2. 每次将相邻的两个有序子数组合并
     * 3. 子数组长度每次翻倍：1 -> 2 -> 4 -> 8 -> ...
     * 4. 直到子数组长度超过数组长度
     * 
     * 优势：
     * - 避免递归调用的开销
     * - 更好的内存访问模式
     * - 易于并行化实现
     * 
     * 过程示例（数组长度为8）：
     * 初始: [3,1,4,2,6,5,8,7]
     * size=1: [1,3][2,4][5,6][7,8] (合并相邻的单个元素)
     * size=2: [1,2,3,4][5,6,7,8]   (合并相邻的长度为2的子数组)
     * size=4: [1,2,3,4,5,6,7,8]    (合并相邻的长度为4的子数组)
     * 
     * @param arr 待排序数组
     */
    public static void mergeSort2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        
        int n = arr.length;
        
        // size表示当前要合并的子数组长度
        for (int size = 1; size < n; size <<= 1) {
            int l = 0;  // 当前左块的起始位置
            
            while (l < n) {
                // 防止越界：如果剩余元素不足一个完整的左块，直接跳出
                if (n - l <= size) {
                    break;
                }
                
                // 计算左块的结束位置
                int m = l + size - 1;
                // 计算右块的结束位置，注意不能超过数组边界
                int r = m + Math.min(size, n - 1 - m);
                
                // 合并当前的左块[l,m]和右块[m+1,r]
                merge(arr, l, m, r);
                
                // 移动到下一对相邻块
                l = r + 1;
            }
            
            // 防止size乘2时发生整数溢出
            if (size > (n >> 1)) {
                break;
            }
        }
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
     * 判断两个数组是否相等
     * 
     * @param arr1 数组1
     * @param arr2 数组2
     * @return 相等返回true，否则返回false
     */
    private static boolean isEqual(int[] arr1, int[] arr2) {
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1 == null || arr2 == null) {  // 修复原代码的逻辑错误
            return false;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 打印数组内容
     * 
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    /**
     * 测试方法 - 验证两种归并排序实现的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 归并排序算法测试 ===");
        
        // 手工测试用例
        int[] testArr = {38, 27, 43, 3, 9, 82, 10};
        System.out.println("测试数组：" + java.util.Arrays.toString(testArr));
        
        int[] test1 = copy(testArr);
        int[] test2 = copy(testArr);
        
        mergeSort(test1);
        mergeSort2(test2);
        
        System.out.println("递归版本结果：" + java.util.Arrays.toString(test1));
        System.out.println("迭代版本结果：" + java.util.Arrays.toString(test2));
        System.out.println("两种实现结果一致：" + isEqual(test1, test2));
        
        // 大规模随机测试
        System.out.println("\n=== 随机测试 ===");
        int times = 100000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("测试次数：" + times);
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int[] arr1 = randomArr(maxLen, maxVal);
            int[] arr2 = copy(arr1);
            mergeSort(arr1);      // 递归版本
            mergeSort2(arr2);     // 迭代版本
            if (!isEqual(arr1, arr2)) {
                System.out.println("测试失败！");
                print(arr1);
                print(arr2);
                return;
            }
        }
        long endTime = System.currentTimeMillis();
        
        System.out.println("所有测试通过！");
        System.out.println("耗时：" + (endTime - startTime) + "ms");
        
        // 算法复杂度分析
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度：O(N*logN)");
        System.out.println("- 分治树高度：logN");
        System.out.println("- 每层合并时间：O(N)");
        System.out.println("- 总时间：O(N*logN)");
        
        System.out.println("\n空间复杂度：O(N)");
        System.out.println("- 需要额外的辅助数组");
        
        System.out.println("\n稳定性：稳定排序");
        
        System.out.println("\n适用场景：");
        System.out.println("- 大数据量排序");
        System.out.println("- 需要稳定排序");
        System.out.println("- 外部排序");
        System.out.println("- 链表排序");
        
        System.out.println("\n算法优势：");
        System.out.println("- 时间复杂度稳定，不受数据分布影响");
        System.out.println("- 分治思想，易于理解和实现");
        System.out.println("- 可以很好地并行化");
        System.out.println("- 是其他高级算法的基础");
    }
}
