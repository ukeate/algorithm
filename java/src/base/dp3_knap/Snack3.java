package base.dp3_knap;

import java.io.*;
import java.util.Arrays;

// https://www.nowcoder.com/questionTerminal/d94bb2fa461d42bcb4c0f2b94f5d4281
/**
 * 背包装零食问题的极致优化版本
 * 使用更高效的Meet in the Middle算法，避免使用TreeMap以提高性能
 * 通过数组排序和二分查找来实现快速合并
 */
public class Snack3 {
    public static long[] arr = new long[31];          // 零食重量数组
    public static int size = 0;                      // 数组大小
    public static long[] leftSum = new long[1 << 16]; // 左半部分所有可能的重量和
    public static int leftSize = 0;                   // 左半部分方案数
    public static long[] rightSum = new long[1 << 16]; // 右半部分所有可能的重量和
    public static int rightSize = 0;                   // 右半部分方案数

    /**
     * 二分查找：找到右半部分中不超过num的方案数
     * @param num 目标值
     * @return 不超过num的方案数量
     */
    private static int find(long num) {
        int ans = -1;
        int l = 0;
        int r = rightSize - 1;
        int m = 0;
        // 二分查找最大的不超过num的位置
        while (l <= r) {
            m = (l + r) / 2;
            if (rightSum[m] <= num) {
                ans = m;
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return ans + 1; // 返回方案数量
    }

    /**
     * DFS生成左半部分的所有可能重量组合
     * @param cur 当前处理的索引
     * @param end 结束索引
     * @param sum 当前累计重量
     */
    private static void dfsLeft(int cur, int end, long sum) {
        if (cur == end) {
            leftSum[leftSize++] = sum;
        } else {
            dfsLeft(cur + 1, end, sum);           // 不选择当前零食
            dfsLeft(cur + 1, end, sum + arr[cur]); // 选择当前零食
        }
    }

    /**
     * DFS生成右半部分的所有可能重量组合
     * @param cur 当前处理的索引
     * @param end 结束索引
     * @param sum 当前累计重量
     */
    private static void dfsRight(int cur, int end, long sum) {
        if (cur == end) {
            rightSum[rightSize++] = sum;
        } else {
            dfsRight(cur + 1, end, sum);           // 不选择当前零食
            dfsRight(cur + 1, end, sum + arr[cur]); // 选择当前零食
        }
    }

    /**
     * 计算装零食的方案数
     * @param w 背包容量
     * @return 总方案数
     */
    private static long ways(long w) {
        if (size == 0) {
            return 0;
        }
        if (size == 1) {
            return arr[0] <= w ? 2 : 1; // 单个零食：装或不装
        }
        
        // 将数组分成两部分
        int mid = size >> 1;
        
        // 生成左半部分的所有可能组合
        leftSize = 0;
        dfsLeft(0, mid + 1, 0L);
        
        // 生成右半部分的所有可能组合
        rightSize = 0;
        dfsRight(mid + 1, size, 0L);
        
        // 对两部分的结果进行排序，便于二分查找
        Arrays.sort(leftSum, 0, leftSize);
        Arrays.sort(rightSum, 0, rightSize);
        
        long ans = 0;
        long count = 1;
        
        // 处理左半部分的每个重量值
        for (int i = 1; i < leftSize; i++) {
            if (leftSum[i] != leftSum[i - 1]) {
                // 当前重量值发生变化，计算之前重量值的贡献
                ans += count * (long) find(w - leftSum[i - 1]);
                count = 1;
            } else {
                // 相同重量值，计数器增加
                count++;
            }
        }
        // 处理最后一个重量值
        ans += count * (long) find(w - leftSum[leftSize - 1]);
        return ans;
    }

    /**
     * 主函数 - 处理输入输出
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            size = (int) in.nval;
            in.nextToken();
            int bag = (int) in.nval;
            for (int i = 0; i < size; i++) {
                in.nextToken();
                arr[i] = (int) in.nval;
            }
            long ways = ways(bag);
            out.println(ways);
            out.flush();
        }
    }
}
