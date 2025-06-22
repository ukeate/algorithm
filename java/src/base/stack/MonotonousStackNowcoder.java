package base.stack;

import java.io.*;

/**
 * 单调栈问题 - 牛客网版本
 * 问题描述：对于数组中的每个元素，找到其左边和右边最近的比它小的元素的位置
 * 
 * 算法原理：
 * 1. 使用单调栈维护一个从栈底到栈顶递增的序列
 * 2. 当遇到比栈顶元素小的元素时，栈顶元素找到了右边最近的小元素
 * 3. 栈顶元素的左边最近小元素就是它在栈中的下一个元素
 * 4. 使用双栈技巧处理相等元素的情况
 * 
 * 时间复杂度：O(N) - 每个元素最多进栈出栈一次
 * 空间复杂度：O(N) - 栈的空间
 * 
 * 示例：
 * 输入数组：[3, 1, 2, 3]
 * 输出结果：
 * 位置0(值3): 左边最近小元素位置=-1, 右边最近小元素位置=1
 * 位置1(值1): 左边最近小元素位置=-1, 右边最近小元素位置=-1
 * 位置2(值2): 左边最近小元素位置=1, 右边最近小元素位置=-1
 * 位置3(值3): 左边最近小元素位置=1, 右边最近小元素位置=-1
 * 
 * @see <a href="https://www.nowcoder.com/practice/2a2c00e7a88a498693568cef63a4b7bb">牛客网原题链接</a>
 */
// https://www.nowcoder.com/practice/2a2c00e7a88a498693568cef63a4b7bb
public class MonotonousStackNowcoder {
    /** 输入数组，存储待处理的元素 */
    public static int[] arr = new int[1000000];
    
    /** 答案数组，ans[i][0]表示位置i左边最近的小元素位置，ans[i][1]表示右边最近的小元素位置 */
    public static int[][] ans = new int[1000000][2];
    
    /** 主栈：维护单调递增序列，存储元素的下标 */
    public static int[] stack1 = new int[1000000];
    
    /** 辅助栈：处理相等元素，存储每个值最右边的位置 */
    public static int[] stack2 = new int[1000000];

    /**
     * 使用单调栈找到每个位置左边和右边最近的较小元素
     * 
     * 算法步骤：
     * 1. 遍历数组，维护一个单调递增栈
     * 2. 当当前元素小于栈顶元素时，栈顶元素找到了右边最近的小元素
     * 3. 栈顶元素的左边最近小元素是栈中它下面的元素
     * 4. 使用辅助栈处理相等元素的情况，避免重复计算
     * 5. 处理栈中剩余元素（它们右边没有更小的元素）
     * 
     * 双栈技巧说明：
     * - stack1：存储所有元素的位置，维护单调性
     * - stack2：存储每个不同值的最右位置，用于快速找到左边最近的小元素
     * 
     * @param n 数组长度
     */
    public static void nearLess(int n) {
        int stackSize1 = 0;  // 主栈大小
        int stackSize2 = 0;  // 辅助栈大小
        
        // 遍历数组中的每个元素
        for (int i = 0; i < n; i++) {
            // 当栈不为空且栈顶元素大于当前元素时，栈顶元素找到了右边最近的小元素
            while (stackSize1 > 0 && arr[stack1[stackSize1 - 1]] > arr[i]) {
                int curIdx = stack1[--stackSize1];  // 弹出栈顶元素
                
                // 找左边最近的小元素：辅助栈中倒数第二个元素
                int left = stackSize2 < 2 ? -1 : stack2[stackSize2 - 2];
                ans[curIdx][0] = left;   // 设置左边最近小元素位置
                ans[curIdx][1] = i;      // 设置右边最近小元素位置（当前元素）
                
                // 如果弹出的元素与新栈顶元素值不同，则需要调整辅助栈
                if (stackSize1 == 0 || arr[stack1[stackSize1 - 1]] != arr[curIdx]) {
                    stackSize2--;
                }
            }
            
            // 处理当前元素入栈
            if (stackSize1 != 0 && arr[stack1[stackSize1 - 1]] == arr[i]) {
                // 如果当前元素与栈顶元素相等，更新辅助栈中该值的最右位置
                stack2[stackSize2 - 1] = i;
            } else {
                // 如果当前元素与栈顶元素不等，在辅助栈中添加新的位置
                stack2[stackSize2++] = i;
            }
            stack1[stackSize1++] = i;  // 当前元素入主栈
        }
        
        // 处理栈中剩余元素（它们右边没有更小的元素）
        while (stackSize1 != 0) {
            int curIdx = stack1[--stackSize1];  // 弹出栈顶元素
            
            // 找左边最近的小元素
            int left = stackSize2 < 2 ? -1 : stack2[stackSize2 - 2];
            ans[curIdx][0] = left;   // 设置左边最近小元素位置
            ans[curIdx][1] = -1;     // 右边没有更小的元素
            
            // 调整辅助栈
            if (stackSize1 == 0 || arr[stack1[stackSize1 - 1]] != arr[curIdx]) {
                stackSize2--;
            }
        }
    }

    /**
     * 主函数：处理输入输出
     * 使用高效的IO方式处理大量数据
     * 
     * 输入格式：
     * 第一行：数组长度n
     * 第二行：n个整数，表示数组元素
     * 
     * 输出格式：
     * n行，每行两个整数，分别表示该位置左边和右边最近的小元素位置
     * 如果不存在则输出-1
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter((System.out)));
        
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;  // 读取数组长度
            
            // 读取数组元素
            for (int i = 0; i < n; i++) {
                in.nextToken();
                arr[i] = (int) in.nval;
            }
            
            // 计算每个位置的答案
            nearLess(n);
            
            // 输出结果
            for (int i = 0; i < n; i++) {
                out.println(ans[i][0] + " " + ans[i][1]);
            }
            out.flush();
        }
    }
}
