package leetc.top;

public class P123_BestTimeToBuyAndSellStockIII {
    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }
        int ans = 0;
        int buyRest = -prices[0];
        int sellRest = 0;
        int min = prices[0];
        for (int i = 1; i < prices.length; i++) {
            ans = Math.max(ans, buyRest + prices[i]);
            min = Math.min(min, prices[i]);
            sellRest = Math.max(sellRest, prices[i] - min);
            buyRest = Math.max(buyRest, sellRest - prices[i]);
        }
        return ans;
    }
}
