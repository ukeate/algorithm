package base.xor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * K次数问题：在数组中找出现k次的数字
 * 问题描述：数组中只有一种数出现k次，其它数都出现m次（k < m），找出这个出现k次的数
 * 使用位运算解决，不需要额外的哈希表空间
 * 时间复杂度：O(32n) = O(n)，空间复杂度：O(1)
 */
public class KTimesNum {

    /**
     * 找出数组中出现k次的数字（其他数字都出现m次）
     * 核心思想：统计每个二进制位上1的出现次数
     * 如果某个位上1的总数 % m != 0，说明目标数字在这个位上是1
     * 
     * @param arr 输入数组
     * @param k 目标数字出现的次数
     * @param m 其他数字出现的次数
     * @return 出现k次的数字，如果不存在则返回-1
     */
    public static int find(int[] arr, int k, int m) {
        int[] t = new int[32];  // 统计每个二进制位上1的出现次数
        
        // 统计每个二进制位上1的总数
        for (int num : arr) {
            for (int i = 0; i < 32; i++) {
                t[i] += ((num >> i) & 1);  // 检查第i位是否为1
            }
        }
        
        int ans = 0;
        // 重构目标数字
        for (int i = 0; i < 32; i++) {
            int v = t[i] % m;  // 第i位上1的个数对m取余
            if (v != 0) {
                // 如果余数不为0，说明目标数字在第i位上是1
                if (v != k) {
                    // 如果余数不等于k，说明数据不符合题意
                    return -1;
                }
                ans |= (1 << i);  // 将第i位设置为1
            }
        }
        
        // 特殊情况：目标数字是0
        if (ans == 0) {
            int cnt = 0;
            for (int num : arr) {
                if (num == 0) {
                    cnt++;
                }
            }
            if (cnt != k) {
                return -1;  // 0的出现次数不等于k
            }
        }
        return ans;
    }

    /**
     * 暴力解法：使用哈希表统计每个数字的出现次数
     * 用于验证位运算解法的正确性
     * @param arr 输入数组
     * @param k 目标数字出现的次数
     * @param m 其他数字出现的次数（未使用）
     * @return 出现k次的数字，如果不存在则返回-1
     */
    private static int findSure(int[] arr, int k, int m) {
        Map<Integer, Integer> map = new HashMap<>();
        // 统计每个数字的出现次数
        for (int num : arr) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }
        // 找出现k次的数字
        for (int num : map.keySet()) {
            if (map.get(num) == k) {
                return num;
            }
        }
        return -1;
    }

    /**
     * 生成随机数（可能为负数）
     * @param range 数值范围
     * @return 随机数
     */
    private static int randomNumber(int range) {
        return (int) (Math.random() * (range + 1)) - (int) (Math.random() * (range + 1));
    }

    /**
     * 生成测试用的随机数组
     * 数组中包含一个出现k次的数字和若干个出现m次的数字
     * @param maxKinds 最大数字种类数
     * @param range 数值范围
     * @param k 目标数字出现次数
     * @param m 其他数字出现次数
     * @return 随机生成的测试数组
     */
    private static int[] randomArray(int maxKinds, int range, int k, int m) {
        int kTimes = k;
        int kNum = randomNumber(range);  // 出现k次的数字
        int numKinds = (int) (Math.random() * maxKinds) + 2;  // 数字种类数
        int[] arr = new int[kTimes + (numKinds - 1) * m];
        
        int index = 0;
        // 添加出现k次的数字
        for (; index < kTimes; index++) {
            arr[index] = kNum;
        }
        
        numKinds--;
        HashSet<Integer> set = new HashSet<>();
        set.add(kNum);
        
        // 添加其他出现m次的数字
        while (numKinds != 0) {
            int curNum = 0;
            do {
                curNum = randomNumber(range);
            } while (set.contains(curNum));  // 确保数字不重复
            set.add(curNum);
            numKinds--;
            for (int i = 0; i < m; i++) {
                arr[index++] = curNum;
            }
        }
        
        // 随机打乱数组
        for (int i = 0; i < arr.length; i++) {
            int j = (int) (Math.random() * arr.length);
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        return arr;
    }

    /**
     * 打印数组内容（调试用）
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        if (arr == null || arr.length < 1) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    /**
     * 测试方法：对比位运算解法和暴力解法的结果
     */
    public static void main(String[] args) {
        int times = 100000;  // 测试次数
        int kind = 10;       // 最大数字种类数
        int range = 100;     // 数值范围
        int max = 10;        // k和m的最大值
        
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int a = (int) (Math.random() * max) + 1;
            int b = (int) (Math.random() * max) + 1;
            int k = Math.min(a, b);
            int m = Math.max(a, b);
            if (k == m) m++;  // 确保k < m
            
            int[] arr = randomArray(kind, range, k, m);
            int ans1 = find(arr, k, m);        // 位运算解法
            int ans2 = findSure(arr, k, m);    // 暴力解法
            
            if (ans1 != ans2) {
                System.out.println("Wrong");
                System.out.println("k = " + k + ", m = " + m);
                print(arr);
                System.out.println();
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("test end");
    }
}
