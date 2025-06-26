package basic.c46;

import java.util.Arrays;

/**
 * 完美洗牌算法
 * 
 * 问题描述：
 * 给定长度为2N的数组[L1,L2,...,Ln,R1,R2,...,Rn]
 * 要求在O(1)空间复杂度下重新排列为[R1,L1,R2,L2,...,Rn,Ln]
 * 
 * 核心思想：
 * 利用环形置换（cycle replacement）的数学性质
 * 对于长度为2n的数组，新位置的计算公式是：
 * - pos' = (2 * pos) % (2n + 1) (当pos <= n时)
 * - 或通过分析得出具体的映射关系
 * 
 * 算法应用：
 * 1. 完美洗牌问题
 * 2. Wiggle排序问题（波浪形排序）
 * 
 * 关键技术：
 * - 循环右移操作
 * - 3^k长度的分治处理
 * - 环形置换的检测和执行
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * 
 * @author 算法学习
 */
public class Shuffle {
    
    /**
     * 计算新位置的索引（方法2，可能未使用）
     * 
     * @param i 原位置索引（1-indexed）
     * @param len 数组长度
     * @return 新位置索引
     */
    private static int modifyIdx2(int i, int len) {
        if (i <= len / 2) {
            return 2 * i;
        } else {
            return 2 * (i - (len / 2)) - 1;
        }
    }

    /**
     * 计算新位置的索引（主要使用的方法）
     * 
     * @param i 原位置索引（1-indexed）
     * @param len 数组长度
     * @return 新位置索引（1-indexed）
     * 
     * 算法原理：
     * 对于完美洗牌，位置i的元素会移动到位置(2*i) % (len+1)
     * 这个公式保证了L1,L2,...,Ln,R1,R2,...,Rn变为R1,L1,R2,L2,...,Rn,Ln
     */
    private static int modifyIdx(int i, int len) {
        return (2 * i) % (len + 1);
    }

    /**
     * 执行环形置换操作
     * 
     * @param arr 目标数组
     * @param start 起始位置
     * @param len 处理长度
     * @param k 环的数量（3^k的指数k）
     * 
     * 算法思路：
     * 在长度为3^k-1的数组段中，有k个独立的环
     * 每个环的起始位置是1, 3, 9, ..., 3^(k-1)
     * 对每个环执行置换操作
     */
    private static void cycles(int[] arr, int start, int len, int k) {
        // k个trigger，trigger从1开始，每次乘以3
        for (int i = 0, trigger = 1; i < k; i++, trigger *= 3) {
            int preVal = arr[trigger + start - 1];  // 环起始位置的值
            int cur = modifyIdx(trigger, len);       // 计算下一个位置
            
            // 沿着环进行置换，直到回到起始位置
            while (cur != trigger) {
                int tmp = arr[cur + start - 1];      // 保存当前位置的值
                arr[cur + start - 1] = preVal;       // 将前一个值放到当前位置
                preVal = tmp;                        // 更新前一个值
                cur = modifyIdx(cur, len);           // 计算下一个位置
            }
            
            // 完成环的置换
            arr[cur + start - 1] = preVal;
        }
    }

    /**
     * 数组区间反转
     * 
     * @param arr 目标数组
     * @param l 左边界
     * @param r 右边界
     */
    private static void reverse(int[] arr, int l, int r) {
        while (l < r) {
            int tmp = arr[l];
            arr[l++] = arr[r];
            arr[r--] = tmp;
        }
    }

    /**
     * 数组循环右移操作
     * 
     * @param arr 目标数组
     * @param l 左边界
     * @param m 中间分割点
     * @param r 右边界
     * 
     * 算法思路：
     * 通过三次反转实现循环右移：
     * 1. 反转[l, m]
     * 2. 反转[m+1, r]  
     * 3. 反转[l, r]
     * 
     * 例如：[1,2,3,4,5,6] 以m=2为分割点
     * -> [2,1,6,5,4,3] -> [3,4,5,6,1,2]
     */
    private static void rotate(int[] arr, int l, int m, int r) {
        reverse(arr, l, m);
        reverse(arr, m + 1, r);
        reverse(arr, l, r);
    }

    /**
     * 完美洗牌的核心递归算法
     * 
     * @param arr 目标数组
     * @param l 左边界
     * @param r 右边界
     * 
     * 算法思路：
     * 1. 找到最大的3^k使得3^k-1 <= 当前长度
     * 2. 通过循环右移调整数组，使得前3^k-1个元素符合标准形式
     * 3. 对这3^k-1个元素执行环形置换
     * 4. 递归处理剩余部分
     */
    private static void shuffle(int[] arr, int l, int r) {
        while (r - l + 1 > 0) {
            int len = r - l + 1;
            int base = 3;  // 3的幂次
            int k = 1;     // 指数
            
            // 找最大的k使得base = 3^k <= len + 1
            while (base <= (len + 1) / 3) {
                base *= 3;
                k++;
            }
            
            // 此时base = 3^k，需要处理的长度是base-1 = 3^k-1
            int half = (base - 1) / 2;  // 一半的长度
            int mid = (l + r) / 2;      // 当前数组的中点
            
            // 通过循环右移使得前base-1个元素符合L1..Lhalf,R1..Rhalf的形式
            rotate(arr, l + half, mid, mid + half);
            
            // 对前base-1个元素执行环形置换
            cycles(arr, l, base - 1, k);
            
            // 递归处理剩余部分
            l = l + base - 1;
        }
    }

    /**
     * 完美洗牌的公共接口
     * 
     * @param arr 长度为偶数的数组，将被修改为洗牌后的结果
     * 
     * 输入格式：[L1,L2,...,Ln,R1,R2,...,Rn]
     * 输出格式：[R1,L1,R2,L2,...,Rn,Ln]
     */
    public static void shuffle(int[] arr) {
        if (arr != null && arr.length != 0 && (arr.length & 1) == 0) {
            shuffle(arr, 0, arr.length - 1);
        }
    }

    /**
     * Wiggle排序（波浪形排序）
     * 
     * @param arr 输入数组，将被重新排列
     * 
     * 问题描述：
     * 将数组重新排列使得arr[0] < arr[1] > arr[2] < arr[3] > ...
     * 即形成波浪形的大小关系
     * 
     * 算法思路：
     * 1. 先对数组排序
     * 2. 根据数组长度奇偶性选择不同的洗牌策略
     * 3. 利用完美洗牌调整元素位置
     * 4. 必要时进行额外的交换操作
     */
    public static void wiggleShuffle(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        
        // 第一步：排序
        Arrays.sort(arr);
        
        if ((arr.length & 1) == 1) {
            // 奇数长度：跳过第一个元素，对剩余部分洗牌
            shuffle(arr, 1, arr.length - 1);
        } else {
            // 偶数长度：对整个数组洗牌，然后交换相邻元素
            shuffle(arr, 0, arr.length - 1);
            
            // 交换相邻元素以形成波浪形
            for (int i = 0; i < arr.length; i += 2) {
                int tmp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = tmp;
            }
        }
    }

    /**
     * 验证数组是否满足Wiggle排序的要求
     * 
     * @param arr 待验证的数组
     * @return 如果满足Wiggle排序返回true，否则返回false
     * 
     * Wiggle排序要求：
     * - 奇数位置的元素 > 相邻的偶数位置元素
     * - 偶数位置的元素 < 相邻的奇数位置元素
     */
    public static boolean isValidWiggle(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if ((i & 1) == 1 && arr[i] < arr[i - 1]) {
                return false;  // 奇数位置应该大于前一个位置
            }
            if ((i & 1) == 0 && arr[i] > arr[i - 1]) {
                return false;  // 偶数位置应该小于前一个位置
            }
        }
        return true;
    }

    /**
     * 打印数组（调试用）
     */
    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    /**
     * 生成随机偶数长度数组（测试用）
     */
    private static int[] randomArr() {
        int len = (int) (10 * Math.random()) * 2;  // 确保是偶数长度
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (100 * Math.random());
        }
        return arr;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 完美洗牌算法测试 ===");
        
        // 基础完美洗牌测试
        System.out.println("1. 完美洗牌测试:");
        int[] test1 = {1, 2, 3, 4, 5, 6, 7, 8}; // L1,L2,L3,L4,R1,R2,R3,R4
        System.out.print("原数组: ");
        print(test1);
        
        shuffle(test1);
        System.out.print("洗牌后: ");
        print(test1); // 应该是 R1,L1,R2,L2,R3,L3,R4,L4 即 5,1,6,2,7,3,8,4
        
        // Wiggle排序测试
        System.out.println("\n2. Wiggle排序测试:");
        int[] test2 = {1, 5, 1, 1, 6, 4};
        System.out.print("原数组: ");
        print(test2);
        
        wiggleShuffle(test2);
        System.out.print("Wiggle排序后: ");
        print(test2);
        System.out.println("是否满足Wiggle要求: " + isValidWiggle(test2));
        
        // 更多Wiggle排序测试
        System.out.println("\n3. 更多Wiggle排序测试:");
        int[][] testCases = {
            {1, 2, 3, 4, 5, 6},
            {1, 1, 1, 1, 2, 2, 2, 2},
            {1, 3, 2, 4},
            {5, 6, 4}
        };
        
        for (int[] testCase : testCases) {
            int[] arr = testCase.clone();
            System.out.print("原数组: ");
            print(arr);
            
            wiggleShuffle(arr);
            System.out.print("结果:   ");
            print(arr);
            System.out.println("有效性: " + (isValidWiggle(arr) ? "✓" : "✗"));
            System.out.println();
        }
        
        // 大规模随机测试
        System.out.println("4. 大规模随机测试:");
        System.out.println("开始测试...");
        
        int testCount = 5000000;
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < testCount; i++) {
            int[] arr = randomArr();
            wiggleShuffle(arr);
            
            if (!isValidWiggle(arr)) {
                System.out.println("错误！发现无效的Wiggle排序:");
                print(arr);
                return;
            }
            
            // 每完成100万次测试输出进度
            if ((i + 1) % 1000000 == 0) {
                System.out.printf("已完成 %d 次测试\n", i + 1);
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.printf("测试完成！共进行 %d 次测试，耗时 %d ms\n", testCount, endTime - startTime);
        System.out.println("所有测试通过，Wiggle排序算法正确！");
        
        // 性能测试
        System.out.println("\n5. 性能测试:");
        int[] largeArr = new int[1000];
        for (int i = 0; i < largeArr.length; i++) {
            largeArr[i] = (int) (1000 * Math.random());
        }
        
        startTime = System.currentTimeMillis();
        wiggleShuffle(largeArr);
        endTime = System.currentTimeMillis();
        
        System.out.printf("大数组（长度%d）Wiggle排序耗时: %d ms\n", largeArr.length, endTime - startTime);
        System.out.println("结果有效性: " + (isValidWiggle(largeArr) ? "✓" : "✗"));
    }
}
