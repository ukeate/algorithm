package base.heap;

import java.util.Arrays;

/**
 * 堆排序算法实现
 * 
 * 堆排序特点：
 * 1. 时间复杂度：O(n log n)，稳定的时间复杂度
 * 2. 空间复杂度：O(1)，原地排序算法
 * 3. 不稳定排序：相等元素的相对位置可能发生改变
 * 4. 选择排序的优化版本
 * 
 * 算法步骤：
 * 1. 构建大根堆：将数组调整为大根堆结构
 * 2. 排序过程：重复以下步骤直到堆为空
 *    - 将堆顶（最大值）与堆尾交换
 *    - 减少堆的大小
 *    - 对新的堆顶进行下沉调整
 * 
 * 优势：能够保证最坏情况下的时间复杂度仍为O(n log n)
 */
public class HeapSort {
    /**
     * 交换数组中两个位置的元素
     * @param arr 数组
     * @param i 位置i
     * @param j 位置j
     */
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 堆插入操作（上浮过程）- 用于逐个构建堆
     * 
     * 将位置i的元素向上调整，直到满足大根堆性质
     * 时间复杂度：O(log n)
     * 
     * @param arr 数组
     * @param i 需要上浮的元素索引
     */
    private static void heapInsert(int[] arr, int i) {
        // 当前节点大于父节点时，需要上浮
        while (arr[i] > arr[(i - 1) / 2]) {
            swap(arr, i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    /**
     * 堆化操作（下沉过程）- 维护堆的性质
     * 
     * 将位置i的元素向下调整，直到满足大根堆性质
     * 时间复杂度：O(log n)
     * 
     * @param arr 数组
     * @param i 需要下沉的元素索引
     * @param size 堆的有效大小
     */
    private static void heapify(int[] arr, int i, int size) {
        int left = i * 2 + 1;   // 左子节点索引
        while (left < size) {
            // 找到子节点中的最大值索引
            int mi = left + 1 < size && arr[left + 1] > arr[left] ? left + 1 : left;
            // 找到父节点和子节点中的最大值索引
            mi = arr[mi] > arr[i] ? mi : i;
            // 如果父节点已经是最大值，则堆性质已满足
            if (mi == i) {
                break;
            }
            // 交换父节点与最大子节点
            swap(arr, mi, i);
            i = mi;
            left = i * 2 + 1;
        }
    }

    /**
     * 堆排序主方法
     * 
     * 两种构建堆的方式：
     * 1. 逐个插入：O(n log n) - 适合动态添加元素
     * 2. 自底向上堆化：O(n) - 适合批量构建堆
     * 
     * 这里使用第二种方式，效率更高
     * 
     * @param arr 待排序数组
     */
    public static void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        
        // 方式1：逐个插入构建堆 - O(N*logN)
        // for (int i = 0; i < arr.length; i++) {
        //     heapInsert(arr, i);
        // }
        
        // 方式2：自底向上构建堆 - O(N)，更高效
        // 从最后一个非叶子节点开始，向前执行堆化操作
        for (int i = arr.length - 1; i >= 0; i--) {
            heapify(arr, i, arr.length);
        }
        
        // 排序过程：重复"取出堆顶，调整堆"的过程
        int heapSize = arr.length;
        swap(arr, 0, --heapSize);   // 将最大值（堆顶）放到数组末尾
        while (heapSize > 0) {
            heapify(arr, 0, heapSize);  // 对新的堆顶进行下沉调整
            swap(arr, 0, --heapSize);   // 将当前最大值放到正确位置
        }
    }

    /**
     * 标准排序方法，用于对比验证
     * @param arr 待排序数组
     */
    private static void sortSure(int[] arr) {
        Arrays.sort(arr);
    }

    /**
     * 生成随机数组用于测试
     * @param maxLen 最大数组长度
     * @param maxVal 最大元素绝对值
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
     * @param arr1 数组1
     * @param arr2 数组2
     * @return 相等返回true，否则返回false
     */
    private static boolean isEqual(int[] arr1, int[] arr2) {
        if (arr1 == null ^ arr2 == null) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
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
     * 打印数组
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
     * 测试方法 - 验证堆排序的正确性
     */
    public static void main(String[] args) {
        int times = 100000;     // 测试次数
        int maxLen = 10;        // 最大数组长度
        int maxVal = 100;       // 最大元素绝对值
        
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr1 = randomArr(maxLen, maxVal);
            int[] arr2 = copy(arr1);
            heapSort(arr1);     // 使用堆排序
            sortSure(arr2);     // 使用标准排序
            
            // 比较两种排序结果是否一致
            if (!isEqual(arr1, arr2)) {
                System.out.println("Wrong");
                print(arr1);
                print(arr2);
                break;
            }
        }
        System.out.println("test end");
    }
}
