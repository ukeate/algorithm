package basic.c51;

/**
 * 加油站问题 - 环形路径良好出发点判断
 * 
 * 问题描述：
 * 在一条环路上有N个加油站，每个加油站有汽油gas[i]升。
 * 你有一辆油箱无限大的汽车，从第i个加油站开往第i+1个加油站需要消耗汽油cost[i]升。
 * 你从其中一个加油站出发，开始时油箱为空。
 * 返回一个boolean数组，表示每个加油站是否是良好出发点（能够绕环路行驶一周）。
 * 
 * 算法思路：
 * 1. 预处理：计算每个位置的净收益 dis[i] = gas[i] - cost[i]
 * 2. 找到第一个非负净收益位置作为起始点
 * 3. 使用连通区扩展算法：
 *    - 维护一个连通区间，表示可以互相到达的加油站
 *    - 从起始点开始，不断扩展连通区
 *    - 如果能扩展成完整环形，则连通区内所有点都是良好出发点
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * LeetCode相关: https://leetcode.com/problems/gas-station/
 * 
 * @author 算法学习
 */
public class GasStations {
    
    /**
     * 预处理：计算净收益并找到第一个非负位置
     * 
     * @param dis 距离数组（会被修改为净收益数组）
     * @param oil 油量数组
     * @return 第一个净收益非负的位置，如果没有返回-1
     */
    private static int oilDis(int[] dis, int[] oil) {
        int init = -1;
        // 计算每个位置的净收益：油量 - 消耗
        for (int i = 0; i < dis.length; i++) {
            dis[i] = oil[i] - dis[i];
            if (dis[i] >= 0) {
                init = i; // 记录最后一个非负位置
            }
        }
        return init;
    }

    /**
     * 获取下一个位置索引（环形）
     * 
     * @param idx 当前索引
     * @param size 数组大小
     * @return 下一个位置的索引
     */
    private static int nextIdx(int idx, int size) {
        return idx == size - 1 ? 0 : (idx + 1);
    }

    /**
     * 获取上一个位置索引（环形）
     * 
     * @param idx 当前索引
     * @param size 数组大小
     * @return 上一个位置的索引
     */
    private static int lastIdx(int idx, int size) {
        return idx == 0 ? (size - 1) : idx - 1;
    }

    /**
     * 连接算法：从start位置向后连接到init位置
     * 判断start到init之间的位置是否都能成为良好出发点
     * 
     * @param dis 净收益数组
     * @param start 起始位置
     * @param init 目标位置
     * @param res 结果数组
     */
    private static void connect(int[] dis, int start, int init, boolean[] res) {
        int need = 0; // 连接到连通区所需的燃油
        
        // 从start向后遍历到init
        while (start != init) {
            if (dis[start] < need) {
                // 当前位置净收益不足以到达连通区
                need -= dis[start];
            } else {
                // 当前位置可以作为良好出发点
                res[start] = true;
                need = 0;
            }
            start = lastIdx(start, dis.length);
        }
    }

    /**
     * 扩展连通区算法核心逻辑
     * 从初始位置开始，不断扩展可连通的区域
     * 
     * @param dis 净收益数组
     * @param init 初始位置（第一个非负净收益位置）
     * @return 良好出发点的boolean数组
     */
    private static boolean[] enlargeArea(int[] dis, int init) {
        boolean[] res = new boolean[dis.length];
        int start = init;                        // 连通区左边界
        int end = nextIdx(init, dis.length);     // 连通区右边界
        int need = 0;                            // 接到连通区需要的燃油
        int rest = 0;                            // 扩充连通区的盈余燃油
        
        do {
            // 如果start和end相遇但不是初始状态，说明无法形成完整连通区
            if (start != init && start == lastIdx(end, dis.length)) {
                break;
            }
            
            if (dis[start] < need) {
                // 当前start位置无法连接到连通区
                need -= dis[start];
            } else {
                // 当前start位置可以连接，向后扩展连通区
                rest += dis[start] - need;
                need = 0;
                
                // 尽可能向右扩展连通区
                while (rest >= 0 && end != start) {
                    rest += dis[end];
                    end = nextIdx(end, dis.length);
                }
                
                // 如果连通区扩展成完整环形
                if (rest >= 0) {
                    res[start] = true;
                    // 处理start左侧剩余位置
                    connect(dis, lastIdx(start, dis.length), init, res);
                    break;
                }
            }
            start = lastIdx(start, dis.length);
        } while (start != init);
        
        return res;
    }

    /**
     * 主方法：判断每个加油站是否是良好出发点
     * 
     * @param dis 每段路程的油耗数组
     * @param oil 每个加油站的油量数组
     * @return boolean数组，表示每个位置是否是良好出发点
     */
    public static boolean[] stations(int[] dis, int[] oil) {
        // 参数校验
        if (dis == null || oil == null || dis.length < 2 || dis.length != oil.length) {
            return null;
        }
        
        // 预处理：计算净收益并找到初始位置
        int init = oilDis(dis, oil);
        
        // 如果没有任何位置的净收益非负，则没有良好出发点
        return init == -1 ? new boolean[dis.length] : enlargeArea(dis, init);
    }

    // ==================== 验证算法（暴力解法） ====================

    /**
     * 检查从指定位置出发是否能跑完一圈
     * 
     * @param arr 净收益数组
     * @param idx 起始位置
     * @return 是否能跑完一圈
     */
    private static boolean canThrough(int[] arr, int idx) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[idx];
            if (sum < 0) {
                return false;
            }
            idx = nextIdx(idx, arr.length);
        }
        return true;
    }

    /**
     * 暴力解法：检查每个位置
     * 
     * @param dis 距离数组
     * @param oil 油量数组
     * @return 每个位置是否是良好出发点
     */
    private static boolean[] sure(int[] dis, int[] oil) {
        if (dis == null || oil == null || dis.length < 2 || dis.length != oil.length) {
            return null;
        }
        
        boolean[] res = new boolean[dis.length];
        
        // 计算净收益
        for (int i = 0; i < dis.length; i++) {
            dis[i] = oil[i] - dis[i];
        }
        
        // 检查每个位置
        for (int i = 0; i < dis.length; i++) {
            res[i] = canThrough(dis, i);
        }
        return res;
    }

    // ==================== 测试工具方法 ====================

    /**
     * 生成随机数组
     */
    private static int[] randomArr(int size, int max) {
        int[] res = new int[size];
        for (int i = 0; i < res.length; i++) {
            res[i] = (int) (max * Math.random());
        }
        return res;
    }

    /**
     * 数组复制
     */
    private static int[] copy(int[] arr) {
        int[] res = new int[arr.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    /**
     * 比较两个boolean数组是否相等
     */
    private static boolean isEqual(boolean[] arr1, boolean[] arr2) {
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 测试方法：对比优化算法和暴力算法的结果
     */
    public static void main(String[] args) {
        int times = 5000000;
        int max = 20;
        
        System.out.println("=== 加油站问题测试开始 ===");
        
        for (int i = 0; i < times; i++) {
            int size = (int) (max * Math.random()) + 2;
            int[] dis = randomArr(size, max);
            int[] oil = randomArr(size, max);
            
            // 准备测试数据
            int[] dis1 = copy(dis);
            int[] oil1 = copy(oil);
            int[] dis2 = copy(dis);
            int[] oil2 = copy(oil);
            
            // 对比两种算法结果
            boolean[] ans1 = stations(dis1, oil1);      // 优化算法
            boolean[] ans2 = sure(dis2, oil2);          // 暴力算法
            
            if (!isEqual(ans1, ans2)) {
                System.out.println("算法结果不一致，测试失败！");
                System.out.print("距离: ");
                for (int d : dis) System.out.print(d + " ");
                System.out.print("\n油量: ");
                for (int o : oil) System.out.print(o + " ");
                System.out.println();
                break;
            }
        }
        
        System.out.println("=== 测试完成，共进行 " + times + " 次对比 ===");
        
        // 示例测试
        System.out.println("\n=== 示例测试 ===");
        int[] exampleGas = {1, 2, 3, 4, 5};
        int[] exampleCost = {3, 4, 5, 1, 2};
        int[] dis1 = copy(exampleCost);
        int[] oil1 = copy(exampleGas);
        
        boolean[] result = stations(dis1, oil1);
        System.out.println("油量: [1, 2, 3, 4, 5]");
        System.out.println("消耗: [3, 4, 5, 1, 2]");
        System.out.print("良好出发点: ");
        for (int i = 0; i < result.length; i++) {
            if (result[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }
}
