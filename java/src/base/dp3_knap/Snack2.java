package base.dp3_knap;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

// https://www.nowcoder.com/questionTerminal/d94bb2fa461d42bcb4c0f2b94f5d4281
/**
 * 背包装零食问题的高级优化版本
 * 使用Meet in the Middle（折半搜索）算法优化
 * 当n较大时，常规DP会超时，使用此方法可以将时间复杂度从O(2^n)降低到O(2^(n/2))
 */
public class Snack2 {
    /**
     * 递归生成所有可能的重量组合
     * @param arr 零食重量数组
     * @param idx 当前处理的索引
     * @param w 当前累计重量
     * @param end 结束索引
     * @param bag 背包容量
     * @param map 存储重量->方案数的映射
     * @return 当前分支的方案数
     */
    private static long process(int[] arr, int idx, long w, int end, int bag, TreeMap<Long, Long> map) {
        // 超过背包容量，不可行
        if (w > bag) {
            return 0;
        }
        // 处理完当前范围的所有零食
        if (idx > end) {
            if (w == 0) {
                return 0; // 什么都不装，不计入方案
            }
            // 记录当前重量的方案数
            if (!map.containsKey(w)) {
                map.put(w, 1L);
            } else {
                map.put(w, map.get(w) + 1);
            }
            return 1;
        } else {
            // 不选择当前零食
            long ways = process(arr, idx + 1, w, end, bag, map);
            // 选择当前零食
            ways += process(arr, idx + 1, w + arr[idx], end, bag, map);
            return ways;
        }
    }

    /**
     * Meet in the Middle算法主体
     * 将数组分成两部分，分别计算所有可能的重量组合，然后合并结果
     */
    private static long ways(int[] arr, int bag) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0] <= bag ? 2 : 1; // 可以装或不装
        }
        
        // 将数组分成两部分
        int mid = (arr.length - 1) >> 1;
        
        // 处理左半部分
        TreeMap<Long, Long> lmap = new TreeMap<>();
        long ways = process(arr, 0, 0, mid, bag, lmap);
        
        // 处理右半部分
        TreeMap<Long, Long> rmap = new TreeMap<>();
        ways += process(arr, mid + 1, 0, arr.length - 1, bag, rmap);
        
        // 构建右半部分的前缀和映射（累计方案数）
        TreeMap<Long, Long> rpre = new TreeMap<>();
        long pre = 0;
        for (Map.Entry<Long, Long> entry : rmap.entrySet()) {
            pre += entry.getValue();
            rpre.put(entry.getKey(), pre);
        }
        
        // 合并左右两部分的结果
        for (Map.Entry<Long, Long> entry : lmap.entrySet()) {
            long lweight = entry.getKey(); // 左半部分的重量
            long lways = entry.getValue();  // 左半部分的方案数
            // 找到右半部分中不超过剩余容量的最大重量
            Long floor = rpre.floorKey(bag - lweight);
            if (floor != null) {
                long rways = rpre.get(floor); // 对应的方案数
                ways += lways * rways; // 组合方案数
            }
        }
        return ways + 1; // 加上什么都不装的方案
    }

    /**
     * 主函数 - 处理输入输出
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((System.in)));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            in.nextToken();
            int bag = (int) in.nval;
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                in.nextToken();
                arr[i] = (int) in.nval;
            }
            long ways = ways(arr, bag);
            out.println(ways);
            out.flush();
        }
    }
}
