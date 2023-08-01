package leetc.top;

public class P265_PaintHouseII {
    public static int minCostII(int[][] costs) {
        int n = costs.length;
        if (n == 0) {
            return 0;
        }
        int k = costs[0].length;
        // 前房(最小、次小)代价及颜色
        int preMin1 = 0, preClr1 = -1, preMin2 = 0, preClr2 = -1;
        for (int i = 0; i < n; i++) {
            int curMin1 = Integer.MAX_VALUE, curClr1 = -1, curMin2 = Integer.MAX_VALUE, curClr2 = -1;
            for (int j = 0; j < k; j++) {
                if (j != preClr1) {
                    if (preMin1 + costs[i][j] < curMin1) {
                        curMin2 = curMin1;
                        // curClr1来自之前的j，clr2 != clr1
                        curClr2 = curClr1;
                        curMin1 = preMin1 + costs[i][j];
                        curClr1 = j;
                    } else if (preMin1 + costs[i][j] < curMin2) {
                        // if-else保证了 clr1 != clr2
                        curMin2 = preMin1 + costs[i][j];
                        curClr2 = j;
                    }
                } else if (j != preClr2) {
                    if (preMin2 + costs[i][j] < curMin1) {
                        curMin2 = curMin1;
                        curClr2 = curClr1;
                        curMin1 = preMin2 + costs[i][j];
                        curClr1 = j;
                    } else if (preMin2 + costs[i][j] < curMin2) {
                        curMin2 = preMin2 + costs[i][j];
                        curClr2 = j;
                    }
                }
                // 忽略了preClr1 == preClr2
            }
            preMin1 = curMin1;
            preClr1 = curClr1;
            preMin2 = curMin2;
            preClr2 = curClr2;
        }
        return preMin1;
    }
}
