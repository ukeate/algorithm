package basic.c36;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 数组切分成累加和相等的4部分问题
 * 
 * 问题描述：
 * 给定一个数组，判断是否能将其切分成4个连续的部分，使得每个部分的累加和都相等。
 * 切分位置的元素会被去掉，不参与任何部分的累加和计算。
 * 
 * 约束条件：
 * 1. 数组长度至少为7（确保每部分至少有1个元素，3个切分位置）
 * 2. 切分后的4个部分都不能为空
 * 3. 所有4个部分的累加和必须相等
 * 
 * 解题思路：
 * 方法1：预处理 + 双指针 - 时间复杂度O(N)
 * 方法2：前缀和 + 哈希表 - 时间复杂度O(N)
 * 
 * 核心观察：
 * 如果存在有效切分，设每部分和为S，总和为sum，那么：
 * sum = 4S + s1 + s2 + s3（其中s1,s2,s3是三个切分位置的值）
 */
public class Split4Parts {
    
    /**
     * 方法1：预处理 + 双指针解法
     * 
     * 算法思路：
     * 1. 预处理：计算所有可能的中间两个切分位置对应的左右累加和
     * 2. 双指针：枚举第一个和第四个切分位置，在预处理结果中查找匹配
     * 
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
     * 
     * @param arr 输入数组
     * @return 是否能切分成4个累加和相等的部分
     */
    public static boolean can1(int[] arr) {
        // 边界检查：数组长度至少为7
        if (arr == null || arr.length < 7) {
            return false;
        }
        
        // 预处理：用HashSet存储所有可能的中间切分组合
        HashSet<String> set = new HashSet<>();
        int sum = 0;
        
        // 计算总和
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        
        // 预处理阶段：枚举所有可能的中间两个切分位置
        int leftSum = arr[0];  // 第一部分的累加和
        
        // i是第二个切分位置，遍历所有可能位置
        for (int i = 1; i < arr.length - 1; i++) {
            // 计算右侧累加和（第四部分）
            int rightSum = sum - leftSum - arr[i];
            
            // 将左右累加和组合存入set
            // 格式："leftSum_rightSum"
            set.add(leftSum + "_" + rightSum);
            
            leftSum += arr[i];  // 更新左侧累加和
        }
        
        // 双指针阶段：枚举第一个和最后一个切分位置
        int l = 1;                    // 第一个切分位置
        int lsum = arr[0];           // 第一部分累加和
        int r = arr.length - 2;     // 第四个切分位置
        int rsum = arr[arr.length - 1];  // 第四部分累加和
        
        // 双指针查找
        while (l < r - 3) {  // 确保中间至少有2个位置用于第二、三个切分点
            if (lsum == rsum) {
                // 如果第一、四部分累加和相等，检查中间是否有匹配的切分
                // 第二部分和 = lsum（因为要求4部分相等）
                // 第三部分和 = rsum（因为要求4部分相等）
                String lkey = String.valueOf(lsum * 2 + arr[l]);  // 第二个切分位置对应的左侧和
                String rkey = String.valueOf(rsum * 2 + arr[r]);  // 第三个切分位置对应的右侧和
                
                if (set.contains(lkey + "_" + rkey)) {
                    return true;
                }
                
                // 左指针右移
                lsum += arr[l++];
            } else if (lsum < rsum) {
                // 左侧和小，左指针右移
                lsum += arr[l++];
            } else {
                // 右侧和小，右指针左移
                rsum += arr[r--];
            }
        }
        
        return false;
    }

    /**
     * 方法2：前缀和 + 哈希表解法
     * 
     * 算法思路：
     * 1. 计算前缀和数组，用HashMap快速查找特定和值的位置
     * 2. 枚举第一个切分位置，计算每部分应有的和，然后查找其他切分位置
     * 
     * 设每部分和为S，第一个切分位置为s1：
     * - 第二个切分位置：前缀和应为2S + arr[s1]
     * - 第三个切分位置：前缀和应为3S + arr[s1] + arr[s2]
     * - 验证：第四部分和是否等于S
     * 
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
     * 
     * @param arr 输入数组
     * @return 是否能切分成4个累加和相等的部分
     */
    public static boolean can2(int[] arr) {
        // 边界检查
        if (arr == null || arr.length < 7) {
            return false;
        }
        
        // 构建前缀和到位置的映射
        HashMap<Integer, Integer> map = new HashMap<>();
        int sum = arr[0];
        
        // 注意：i从1开始，因为位置0不能作为切分位置
        for (int i = 1; i < arr.length; i++) {
            map.put(sum, i);  // 记录前缀和sum对应的分割位置i
            sum += arr[i];
        }
        
        // 枚举第一个切分位置s1
        int lsum = arr[0];  // 第一部分的累加和（即每部分应有的和S）
        
        for (int s1 = 1; s1 < arr.length - 5; s1++) {
            // 查找第二个切分位置s2
            // 第二个切分位置前的前缀和应为：2*lsum + arr[s1]
            int checkSum = lsum * 2 + arr[s1];
            
            if (map.containsKey(checkSum)) {
                int s2 = map.get(checkSum);
                
                // 查找第三个切分位置s3
                // 第三个切分位置前的前缀和应为：3*lsum + arr[s1] + arr[s2]
                checkSum += lsum + arr[s2];
                
                if (map.containsKey(checkSum)) {
                    int s3 = map.get(checkSum);
                    
                    // 验证第四部分的和是否等于lsum
                    // 第四部分 = 总和 - 前三部分 - 三个切分位置的值
                    if (checkSum + arr[s3] + lsum == sum) {
                        return true;
                    }
                }
            }
            
            lsum += arr[s1];  // 更新第一部分的累加和
        }
        
        return false;
    }

    /**
     * 生成随机数组用于测试
     * 
     * @return 随机数组，长度7-16，元素值1-10
     */
    private static int[] randomArr() {
        int[] res = new int[(int) (10 * Math.random()) + 7];
        for (int i = 0; i < res.length; i++) {
            res[i] = (int) (10 * Math.random()) + 1;
        }
        return res;
    }
    
    /**
     * 打印数组
     * 
     * @param arr 要打印的数组
     */
    private static void printArray(int[] arr) {
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
     * 测试方法：验证两种算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 数组切分4等分测试 ===");
        
        // 手动测试用例1：可以切分的情况
        int[] testArr1 = {1, 2, 3, 1, 4, 1, 2, 3, 1};  // 可以切分为 [1,2], [4], [2], [1]
        System.out.println("手动测试用例1:");
        printArray(testArr1);
        System.out.println("方法1结果: " + can1(testArr1));
        System.out.println("方法2结果: " + can2(testArr1));
        System.out.println();
        
        // 手动测试用例2：无法切分的情况
        int[] testArr2 = {1, 1, 1, 1, 1, 1, 1};
        System.out.println("手动测试用例2:");
        printArray(testArr2);
        System.out.println("方法1结果: " + can1(testArr2));
        System.out.println("方法2结果: " + can2(testArr2));
        System.out.println();
        
        // 大规模随机测试
        int times = 3000000;
        System.out.println("开始大规模随机测试...");
        System.out.println("测试次数: " + times);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr();
            boolean result1 = can1(arr);
            boolean result2 = can2(arr);
            
            if (result1 != result2) {  // 使用 != 比较布尔值，等价于 XOR
                System.out.println("发现错误!");
                System.out.print("错误数组: ");
                printArray(arr);
                System.out.println("方法1: " + result1 + " | 方法2: " + result2);
                break;
            }
            
            // 每50万次输出进度
            if ((i + 1) % 500000 == 0) {
                System.out.println("已完成 " + (i + 1) + " 次测试");
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("测试完成！耗时: " + (endTime - startTime) + " ms");
    }
}
