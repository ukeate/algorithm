package base.dp3_knap;

import java.io.*;
import java.util.Arrays;

// https://www.nowcoder.com/questionTerminal/d94bb2fa461d42bcb4c0f2b94f5d4281
public class Snack3 {
    public static long[] arr = new long[31];
    public static int size = 0;
    public static long[] leftSum = new long[1 << 16];
    public static int leftSize = 0;
    public static long[] rightSum = new long[1 << 16];
    public static int rightSize = 0;

    private static int find(long num) {
        int ans = -1;
        int l = 0;
        int r = rightSize - 1;
        int m = 0;
        while (l <= r) {
            m = (l + r) / 2;
            if (rightSum[m] <= num) {
                ans = m;
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return ans + 1;
    }

    private static void dfsLeft(int cur, int end, long sum) {
        if (cur == end) {
            leftSum[leftSize++] = sum;
        } else {
            dfsLeft(cur + 1, end, sum);
            dfsLeft(cur + 1, end, sum + arr[cur]);
        }
    }

    private static void dfsRight(int cur, int end, long sum) {
        if (cur == end) {
            rightSum[rightSize++] = sum;
        } else {
            dfsRight(cur + 1, end, sum);
            dfsRight(cur + 1, end, sum + arr[cur]);
        }
    }

    private static long ways(long w) {
        if (size == 0) {
            return 0;
        }
        if (size == 1) {
            return arr[0] <= w ? 2 : 1;
        }
        int mid = size >> 1;
        leftSize = 0;
        dfsLeft(0, mid + 1, 0L);
        rightSize = 0;
        dfsRight(mid + 1, size, 0L);
        Arrays.sort(leftSum, 0, leftSize);
        Arrays.sort(rightSum, 0, rightSize);
        long ans = 0;
        long count = 1;
        for (int i = 1; i < leftSize; i++) {
            if (leftSum[i] != leftSum[i - 1]) {
                ans += count * (long) find(w - leftSum[i - 1]);
                count = 1;
            } else {
                count++;
            }
        }
        ans += count * (long) find(w - leftSum[leftSize - 1]);
        return ans;
    }

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
