package base.binary;

/**
 * 在相邻数不重复的数组中找到一个局部最小值
 * 
 * 局部最小定义：
 * - 对于位置0：arr[0] < arr[1]
 * - 对于位置n-1：arr[n-2] > arr[n-1]  
 * - 对于位置i(0<i<n-1)：arr[i-1] > arr[i] < arr[i+1]
 * 
 * 核心思想：利用二分查找的思想，根据局部趋势判断搜索方向
 */
/**
 * 相临数不重复，找一个局部最小
 */
public class FindOneMin {

    /**
     * 查找一个局部最小值的位置
     * 时间复杂度：O(log n)，空间复杂度：O(1)
     * 
     * 算法思路：
     * 1. 先检查边界情况（首尾元素）
     * 2. 对于中间位置，根据与相邻元素的大小关系决定搜索方向
     * 3. 利用"必然存在局部最小"的性质进行二分
     * 
     * @param arr 相邻元素不重复的数组
     * @return 局部最小值的位置索引，如果数组为空返回-1
     */
    public static int findOneMin(int[] arr) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        // 只有一个元素，直接返回
        if (arr.length == 1) {
            return 0;
        }
        // 检查第一个位置是否为局部最小
        if (arr[0] < arr[1]) {
            return 0;
        }
        // 检查最后一个位置是否为局部最小
        if (arr[arr.length - 2] > arr[arr.length - 1]) {
            return arr.length - 1;
        }

        // 在中间区域[1, n-2]中查找局部最小
        int l = 1;
        int r = arr.length - 2;
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (arr[mid] > arr[mid - 1]) {
                // 当前位置比左边大，说明左边可能有局部最小
                r = mid - 1;
            } else if (arr[mid] > arr[mid + 1]) {
                // 当前位置比右边大，说明右边可能有局部最小
                l = mid + 1;
            } else {
                // 当前位置比两边都小，找到局部最小
                return mid;
            }
        }
        // 当l==r时，该位置就是局部最小
        return l;
    }

    /**
     * 验证给定位置是否为局部最小
     * @param arr 数组
     * @param minInd 待验证的位置
     * @return 是否为局部最小
     */
    private static boolean check(int[] arr, int minInd) {
        if (arr.length == 0) {
            return minInd == -1;
        }
        int left = minInd - 1;
        int right = minInd + 1;
        // 检查左邻居（如果存在）是否更大
        boolean leftBigger = left >= 0 ? arr[left] > arr[minInd] : true;
        // 检查右邻居（如果存在）是否更大
        boolean rightBigger = right < arr.length ? arr[right] > arr[minInd] : true;
        return leftBigger && rightBigger;
    }

    /**
     * 打印数组内容
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }
        System.out.println();
    }

    /**
     * 生成相邻元素不重复的随机数组
     * @param maxLen 最大长度
     * @param maxVal 最大值
     * @return 相邻元素不重复的随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int len = (int) (Math.random() * maxLen);
        int[] arr = new int[len];
        if (len > 0) {
            arr[0] = (int) (Math.random() * maxVal);
            for (int i = 1; i < len; i++) {
                // 确保相邻元素不重复
                do {
                    arr[i] = (int) (Math.random() * maxVal);
                } while (arr[i] == arr[i - 1]);
            }
        }
        return arr;
    }

    /**
     * 测试方法，验证局部最小查找算法的正确性
     */
    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int ans = findOneMin(arr);
            if (!check(arr, ans)) {
                System.out.println("Wrong Answer " + ans + ":");
                print(arr);
            }
        }
        System.out.println("test end");
    }

}
