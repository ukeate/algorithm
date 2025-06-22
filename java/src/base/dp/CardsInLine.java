package base.dp;

/**
 * 排成一条线的纸牌博弈问题
 * 
 * 问题描述：
 * 给定一个整型数组arr，代表数值不同的纸牌排成一条线。
 * 玩家A和玩家B依次拿走每张纸牌，规定玩家A先拿，玩家B后拿。
 * 但是每个玩家只能拿走最左或最右的纸牌。
 * 玩家A和玩家B都绝顶聪明，请返回最后获胜者的分数。
 * 
 * 解法分析：
 * 1. 定义两个递归函数：
 *    - f(l, r)：作为先手，在arr[l...r]范围上能获得的最大分数
 *    - g(l, r)：作为后手，在arr[l...r]范围上能获得的最大分数
 * 
 * 2. 递归关系：
 *    - f(l, r) = max(arr[l] + g(l+1, r), arr[r] + g(l, r-1))
 *    - g(l, r) = min(f(l+1, r), f(l, r-1))
 * 
 * 3. 最终答案：max(f(0, n-1), g(0, n-1))，即先手和后手的最大分数
 * 
 * 时间复杂度：O(n²)
 * 空间复杂度：O(n²)
 */
public class CardsInLine {

    /**
     * 暴力递归 - 先手函数
     * 作为先手，在arr[l...r]范围上能获得的最大分数
     * 
     * @param arr 纸牌数组
     * @param l 左边界
     * @param r 右边界
     * @return 先手能获得的最大分数
     */
    private static int f1(int[] arr, int l, int r) {
        // base case：只有一张牌时，先手直接拿走
        if (l == r) {
            return arr[l];
        }
        
        // 先手有两种选择：
        // 1. 拿左边的牌，然后在剩余范围作为后手
        int p1 = arr[l] + g1(arr, l + 1, r);
        // 2. 拿右边的牌，然后在剩余范围作为后手
        int p2 = arr[r] + g1(arr, l, r - 1);
        
        // 先手会选择分数更高的策略
        return Math.max(p1, p2);
    }

    /**
     * 暴力递归 - 后手函数
     * 作为后手，在arr[l...r]范围上能获得的最大分数
     * 
     * @param arr 纸牌数组
     * @param l 左边界
     * @param r 右边界
     * @return 后手能获得的最大分数
     */
    private static int g1(int[] arr, int l, int r) {
        // base case：只有一张牌时，后手什么也拿不到
        if (l == r) {
            return 0;
        }
        
        // 后手的分数取决于先手的选择：
        // 如果先手拿了左边的牌，后手在[l+1, r]作为先手
        int p1 = f1(arr, l + 1, r);
        // 如果先手拿了右边的牌，后手在[l, r-1]作为先手
        int p2 = f1(arr, l, r - 1);
        
        // 由于先手会选择对自己有利的策略，后手只能被动接受较小的分数
        return Math.min(p1, p2);
    }

    /**
     * 暴力递归解法入口
     * 
     * @param arr 纸牌数组
     * @return 获胜者的分数
     */
    public static int win1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int first = f1(arr, 0, arr.length - 1);   // 先手分数
        int second = g1(arr, 0, arr.length - 1);  // 后手分数
        
        return Math.max(first, second);
    }

    /**
     * 记忆化搜索 - 先手函数
     * 
     * @param arr 纸牌数组
     * @param l 左边界
     * @param r 右边界
     * @param fmap 先手分数缓存
     * @param gmap 后手分数缓存
     * @return 先手能获得的最大分数
     */
    private static int f2(int[] arr, int l, int r, int[][] fmap, int[][] gmap) {
        // 如果已经计算过，直接返回缓存结果
        if (fmap[l][r] != -1) {
            return fmap[l][r];
        }
        
        int ans = 0;
        if (l == r) {
            ans = arr[l];
        } else {
            int p1 = arr[l] + g2(arr, l + 1, r, fmap, gmap);
            int p2 = arr[r] + g2(arr, l, r - 1, fmap, gmap);
            ans = Math.max(p1, p2);
        }
        
        // 缓存结果
        fmap[l][r] = ans;
        return ans;
    }

    /**
     * 记忆化搜索 - 后手函数
     * 
     * @param arr 纸牌数组
     * @param l 左边界
     * @param r 右边界
     * @param fmap 先手分数缓存
     * @param gmap 后手分数缓存
     * @return 后手能获得的最大分数
     */
    private static int g2(int[] arr, int l, int r, int[][] fmap, int[][] gmap) {
        // 如果已经计算过，直接返回缓存结果
        if (gmap[l][r] != -1) {
            return gmap[l][r];
        }
        
        int ans = 0;
        if (l != r) {  // 只有一张牌时，后手得0分
            int p1 = f2(arr, l + 1, r, fmap, gmap);
            int p2 = f2(arr, l, r - 1, fmap, gmap);
            ans = Math.min(p1, p2);
        }
        
        // 缓存结果
        gmap[l][r] = ans;
        return ans;
    }

    /**
     * 记忆化搜索解法入口
     * 
     * @param arr 纸牌数组
     * @return 获胜者的分数
     */
    public static int win2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        // 初始化缓存数组，-1表示未计算过
        int[][] fmap = new int[n][n];
        int[][] gmap = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                fmap[i][j] = -1;
                gmap[i][j] = -1;
            }
        }
        
        int first = f2(arr, 0, arr.length - 1, fmap, gmap);
        int second = g2(arr, 0, arr.length - 1, fmap, gmap);
        
        return Math.max(first, second);
    }

    /**
     * 动态规划解法
     * 
     * fmap[l][r]表示作为先手在arr[l...r]范围上能获得的最大分数
     * gmap[l][r]表示作为后手在arr[l...r]范围上能获得的最大分数
     * 
     * @param arr 纸牌数组
     * @return 获胜者的分数
     */
    public static int win3(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        int[][] fmap = new int[n][n];
        int[][] gmap = new int[n][n];
        
        // base case：对角线上的值，表示只有一张牌的情况
        for (int i = 0; i < n; i++) {
            fmap[i][i] = arr[i];  // 先手拿走唯一的牌
            // gmap[i][i] = 0; // 后手什么也拿不到（默认值）
        }
        
        // 填充dp表，按照区间长度从小到大填充
        for (int startCol = 1; startCol < n; startCol++) {
            int l = 0;
            int r = startCol;
            
            while (r < n) {
                // 先手策略：选择分数更高的方案
                fmap[l][r] = Math.max(arr[l] + gmap[l + 1][r], arr[r] + gmap[l][r - 1]);
                // 后手策略：先手会选择对自己有利的，后手只能接受较小的分数
                gmap[l][r] = Math.min(fmap[l + 1][r], fmap[l][r - 1]);
                
                l++;
                r++;
            }
        }
        
        return Math.max(fmap[0][n - 1], gmap[0][n - 1]);
    }

    public static void main(String[] args) {
        int[] arr = {5, 7, 4, 5, 8, 1, 6, 0, 3, 4, 6, 1, 7};
        System.out.println(win1(arr));
        System.out.println(win2(arr));
        System.out.println(win3(arr));
    }
}
