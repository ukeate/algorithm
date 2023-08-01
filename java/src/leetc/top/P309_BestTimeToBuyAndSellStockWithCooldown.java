package leetc.top;

public class P309_BestTimeToBuyAndSellStockWithCooldown {
    private static int process1(int[] prices, boolean hasBuy, int idx, int buyPrice) {
        if (idx >= prices.length) {
            return 0;
        }
        if (hasBuy) {
            int noSell = process1(prices, true, idx + 1, buyPrice);
            int sell = prices[idx] - buyPrice + process1(prices, false, idx + 2, 0);
            return Math.max(noSell, sell);
        } else {
            int noBuy = process1(prices, false, idx + 1, 0);
            int buy = process1(prices, true, idx + 1, prices[idx]);
            return Math.max(noBuy, buy);
        }
    }

    public int maxProfit1(int[] prices) {
        return process1(prices, false, 0, 0);
    }

    //

    public static int maxProfit2(int[] prices) {
        if (prices.length < 2) {
            return 0;
        }
        int n = prices.length;
        // 截止i, buy后最佳余额
        // 截止i, sell后最佳余额
        int[] buy = new int[n], sell = new int[n];
        buy[1] = Math.max(-prices[0], -prices[1]);
        sell[1] = Math.max(0, prices[1] - prices[0]);
        for (int i = 2; i < n; i++) {
            // 不卖、卖
            sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i]);
            // 不买、买
            buy[i] = Math.max(buy[i - 1], sell[i - 2] - prices[i]);
        }
        return sell[n - 1];
    }

    //

    public static int maxProfit3(int[] prices) {
        if (prices.length < 2) {
            return 0;
        }
        int buy1 = Math.max(-prices[0], -prices[1]);
        int sell1 = Math.max(0, prices[1] - prices[0]);
        int sell2 = 0;
        for (int i = 2; i < prices.length; i++) {
            int tmp = sell1;
            sell1 = Math.max(sell1, buy1 + prices[i]);
            buy1 = Math.max(buy1, sell2 - prices[i]);
            sell2 = tmp;
        }
        return sell1;
    }
}
