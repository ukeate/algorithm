package leetc.top;

public class P714_BestTimeToBuyAndSellStockWithTransactionFee {
    public static int maxProfit(int[] arr, int fee) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        int bestBuy = -arr[0] - fee;
        int bestSell = 0;
        for (int i = 1; i < n; i++) {
            int curBuy = bestSell - arr[i] - fee;
            int curSell = bestBuy + arr[i];
            bestBuy = Math.max(bestBuy, curBuy);
            bestSell = Math.max(bestSell, curSell);
        }
        return bestSell;
    }
}
