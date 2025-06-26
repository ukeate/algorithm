package basic.c31;

/**
 * 生成达标数组问题
 * 
 * 问题描述：生成一个长度为size的数组，满足对于任意三元组(i,k,j)，其中i<k<j，都有arr[i]+arr[j] != arr[k]*2
 * 即不存在以arr[k]为中位数的等差子序列
 * 
 * 核心思路：
 * 1. 使用递归分治的思想，将问题规模减半
 * 2. 对于一个规模为halfSize的达标数组base，构造规模为size的达标数组
 * 3. 前半部分使用base[i]*2-1（奇数序列）
 * 4. 后半部分使用base[i]*2（偶数序列）
 * 5. 奇偶数不可能构成等差数列，保证了达标性质
 * 
 * 时间复杂度：O(size)（每层递归O(size)，递归深度O(log size)）
 * 空间复杂度：O(log size)（递归调用栈）
 */
public class MakeNo {

    /**
     * 生成长度为size的达标数组
     * @param size 数组长度
     * @return 满足条件的达标数组
     */
    public static int[] make(int size) {
        // 递归基础情况：长度为1的数组直接返回
        if (size == 1) {
            return new int[]{1};
        }
        
        // 计算一半的长度（向上取整）
        int halfSize = (size + 1) / 2;
        
        // 递归获取一半长度的达标数组
        int[] base = make(halfSize);
        
        // 构造结果数组
        int[] ans = new int[size];
        int idx = 0;
        
        // 前半部分：base[i]*2-1，生成奇数序列
        for (; idx < halfSize; idx++) {
            ans[idx] = base[idx] * 2 - 1;
        }
        
        // 后半部分：base[i]*2，生成偶数序列
        for (int i = 0; idx < size; idx++, i++) {
            ans[idx] = base[i] * 2;
        }
        
        return ans;
    }

    /**
     * 验证数组是否满足达标条件
     * 检查是否存在三元组(i,k,j)使得arr[i]+arr[j] = 2*arr[k]
     * @param arr 待检验的数组
     * @return true表示数组达标，false表示不达标
     */
    private static boolean isValid(int[] arr) {
        int n = arr.length;
        // 三层循环检查所有可能的三元组
        for (int i = 0; i < n; i++) {
            for (int k = i + 1; k < n; k++) {
                for (int j = k + 1; j < n; j++) {
                    // 如果存在等差序列，返回false
                    if (arr[i] + arr[j] == 2 * arr[k]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("test begin");
        for (int n = 1; n < 1000; n++) {
            int[] arr = make(n);
            if (!isValid(arr)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
