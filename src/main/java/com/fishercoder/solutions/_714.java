package com.fishercoder.solutions;

/**
 * 714. Best Time to Buy and Sell Stock with Transaction Fee
 *
 * Your are given an array of integers prices, for which the i-th element is the price of a given stock on day i;
 * and a non-negative integer fee representing a transaction fee.
 * You may complete as many transactions as you like,
 * but you need to pay the transaction fee for each transaction.
 * You may not buy more than 1 share of a stock at a time (ie. you must sell the stock share before you buy again.)

 Return the maximum profit you can make.

 Example 1:
 Input: prices = [1, 3, 2, 8, 4, 9], fee = 2
 Output: 8
 Explanation: The maximum profit can be achieved by:
 Buying at prices[0] = 1
 Selling at prices[3] = 8
 Buying at prices[4] = 4
 Selling at prices[5] = 9
 The total profit is ((8 - 1) - 2) + ((9 - 4) - 2) = 8.

 Note:
 0 < prices.length <= 50000.
 0 < prices[i] < 50000.
 0 <= fee < 50000.
 */
public class _714 {
    public static class Solution1 {
        /**
         * O(n) time
         * O(n) space
         * credit: https://discuss.leetcode.com/topic/108009/java-c-clean-code-dp-greedy
         */
        public int maxProfit(int[] prices, int fee) {
            int n = prices.length;
            if (n < 2) {
                return 0;
            }
            int[] hold = new int[n];
            int[] sell = new int[n];
            hold[0] = -prices[0];
            for (int i = 1; i < prices.length; i++) {
                hold[i] = Math.max(hold[i - 1], sell[i - 1] - prices[i]);
                sell[i] = Math.max(sell[i - 1], hold[i - 1] - fee + prices[i]);
            }
            return sell[n - 1];
        }
    }

    public static class Solution2 {
        /**
         * O(n) time
         * O(1) space
         * credit: https://leetcode.com/articles/best-time-to-buy-and-sell-stock-with-transaction-fee/
         * <p>
         * cash: the max profit we could have if we did not have a share of stock in hand
         * hold: the max profit we could have if we hold one share of stack in hand
         * <p>
         * to transition from the i-th day to the i+1 th day, we have two options:
         * 1. sell our stock: cash = Math.max(cash, hold + prices[i] - fee)
         * 2. buy a stock: hold = Math.max(hold, cash - prices[i])
         *
         *
         *
         * Intuition and Algorithm

         At the end of the i-th day, we maintain cash, the maximum profit we could have if we did not have a share of stock,
         and hold, the maximum profit we could have if we owned a share of stock.

         To transition from the i-th day to the i+1-th day, we either sell our stock cash = max(cash, hold + prices[i] - fee)
         or buy a stock hold = max(hold, cash - prices[i]). At the end,
         we want to return cash. We can transform cash first without using temporary variables because selling and buying
         n the same day can't be better than just continuing to hold the stock.
         */
        public int maxProfit(int[] prices, int fee) {
            int cash = 0;
            int hold = -prices[0];
            for (int i = 1; i < prices.length; i++) {
                System.out.println("price["+i+"] : "+prices[i]);
                System.out.println("cash  = Math.max( " +cash+ " , "+  (hold + prices[i] - fee) +" )");
                cash = Math.max(cash, hold + prices[i] - fee);
                System.out.println("cash : "+cash);
                System.out.println("hold = Math.max( "+hold +" , "+ ( cash - prices[i]) +" )");
                hold = Math.max(hold, cash - prices[i]);
                System.out.println("hold : "+hold);
            }
            System.out.println("Result - cash " + cash);
            return cash;
        }
    }


//    Given any day I, its max profit status boils down to one of the two status below:
//
//            (1) buy status:
//    buy[i] represents the max profit at day i in buy status, given that the last action you took is a buy action at day K, where K<=i. And you have the right to sell at day i+1, or do nothing.
//            (2) sell status:
//    sell[i] represents the max profit at day i in sell status, given that the last action you took is a sell action at day K, where K<=i. And you have the right to buy at day i+1, or do nothing.
//
//            Let's walk through from base case.
//
//    Base case:
//    We can start from buy status, which means we buy stock at day 0.
//    buy[0]=-prices[0];
//    Or we can start from sell status, which means we sell stock at day 0.
//    Given that we don't have any stock at hand in day 0, we set sell status to be 0.
//    sell[0]=0;
//
//    Status transformation:
//    At day i, we may buy stock (from previous sell status) or do nothing (from previous buy status):
//    buy[i] = Math.max(buy[i - 1], sell[i - 1] - prices[i]);
//    Or
//    At day i, we may sell stock (from previous buy status) or keep holding (from previous buy status):
//    sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i]);
//
//    Finally:
//    We will return sell[last_day] as our result, which represents the max profit at the last day, given that you took sell action at any day before the last day.
//
//    We can apply transaction fee at either buy status or sell status.
//
//    So here come our two solutions:
//
//    Solution I -- pay the fee when buying the stock:
public static class Solution3 {
    public int maxProfit1(int[] prices, int fee) {
        if (prices.length <= 1) return 0;
        int days = prices.length, buy[] = new int[days], sell[] = new int[days];
        buy[0]=-prices[0]-fee;
        for (int i = 1; i<days; i++) {
            buy[i] = Math.max(buy[i - 1], sell[i - 1] - prices[i] - fee); // keep the same as day i-1, or buy from sell status at day i-1
            sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i]); // keep the same as day i-1, or sell from buy status at day i-1
        }
        return sell[days - 1];
    }
   // Solution II -- pay the fee when selling the stock:

    public int maxProfit2(int[] prices, int fee) {
        if (prices.length <= 1) return 0;
        int days = prices.length, buy[] = new int[days], sell[] = new int[days];
        buy[0]=-prices[0];
        for (int i = 1; i<days; i++) {
            buy[i] = Math.max(buy[i - 1], sell[i - 1] - prices[i]); // keep the same as day i-1, or buy from sell status at day i-1
            sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i] - fee); // keep the same as day i-1, or sell from buy status at day i-1
        }
        return sell[days - 1];
    }
}
}