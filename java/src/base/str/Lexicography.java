package base.str;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * 字典序最小的字符串拼接问题
 * 问题描述：给定一个字符串数组，将所有字符串拼接成一个字符串，使得结果的字典序最小
 * 
 * 核心思想：
 * 对于两个字符串a和b，如果a+b的字典序小于b+a，那么a应该排在b前面
 * 这个比较规则具有传递性，可以用来对整个数组进行排序
 * 
 * 算法对比：
 * 方法1：暴力解法 - 生成所有排列，找字典序最小的 O(N! * N * M)
 * 方法2：贪心解法 - 使用自定义比较器排序 O(N * log(N) * M)
 * 
 * 关键洞察：
 * 局部最优解能够导致全局最优解
 * 如果对于任意两个相邻的字符串都满足最优拼接顺序，那么整体就是最优的
 * 
 * 时间复杂度：O(N * log(N) * M) - N为字符串数量，M为平均字符串长度
 * 空间复杂度：O(N * M) - 存储字符串和排序空间
 * 
 * 应用场景：
 * - 文件名排序、版本号排序
 * - 数字拼接成最小值
 * - 字典编译、索引构建
 */
public class Lexicography {
    
    /**
     * 从字符串数组中移除指定位置的元素
     * 辅助方法，用于暴力解法中生成子问题
     * 
     * @param arr 原数组
     * @param idx 要移除的位置
     * @return 移除指定元素后的新数组
     */
    private static String[] removeIndex(String[] arr, int idx) {
        String[] ans = new String[arr.length - 1];
        int ansIdx = 0;
        for (int i = 0; i < arr.length; i++) {
            if (i != idx) {
                ans[ansIdx++] = arr[i];
            }
        }
        return ans;
    }

    /**
     * 递归生成所有可能的字符串拼接结果
     * 暴力解法的核心递归函数
     * 
     * 算法思路：
     * 1. 如果数组为空，返回包含空字符串的集合
     * 2. 尝试每个字符串作为开头
     * 3. 递归处理剩余字符串
     * 4. 将当前字符串与递归结果拼接
     * 
     * @param ss 字符串数组
     * @return 所有可能拼接结果的有序集合
     */
    private static TreeSet<String> process(String[] ss) {
        TreeSet<String> ans = new TreeSet<>();  // 自动按字典序排序
        
        if (ss.length == 0) {
            ans.add("");  // 空数组对应空字符串
            return ans;
        }
        
        // 尝试每个字符串作为第一个
        for (int i = 0; i < ss.length; i++) {
            String first = ss[i];                    // 当前选择的第一个字符串
            String[] nexts = removeIndex(ss, i);     // 剩余的字符串
            TreeSet<String> next = process(nexts);   // 递归处理剩余部分
            
            // 将当前字符串与所有可能的后续拼接
            for (String cur : next) {
                ans.add(first + cur);
            }
        }
        return ans;
    }

    /**
     * 方法1：暴力解法
     * 生成所有可能的字符串排列，返回字典序最小的结果
     * 
     * 算法复杂度：
     * - 时间复杂度：O(N! * N * M) - N!种排列，每种需要O(N*M)时间拼接和比较
     * - 空间复杂度：O(N! * N * M) - 存储所有可能的拼接结果
     * 
     * 适用场景：
     * - 字符串数量很少（N <= 8）的情况
     * - 需要验证贪心算法正确性的场景
     * 
     * @param ss 字符串数组
     * @return 字典序最小的拼接结果
     */
    public static String lowestString1(String[] ss) {
        if (ss == null || ss.length == 0) {
            return "";
        }
        
        TreeSet<String> ans = process(ss);
        return ans.size() == 0 ? "" : ans.first();  // 返回字典序最小的结果
    }

    /**
     * 自定义比较器：定义字符串的拼接顺序
     * 比较规则：如果a+b的字典序小于b+a，则a应该排在b前面
     * 
     * 数学证明（传递性）：
     * 如果a+b < b+a 且 b+c < c+b，那么a+c < c+a
     * 证明：设a、b、c的长度分别为m、n、k
     * 由于a+b < b+a，意味着在某个位置i，a+b[i] < b+a[i]
     * 这种比较关系具有传递性，保证了排序的一致性
     * 
     * 示例：
     * a="b", b="ba" 
     * a+b="bba", b+a="bab"
     * 因为"bba" > "bab"，所以b应该排在a前面
     */
    private static class Comp implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            return (a + b).compareTo(b + a);
        }
    }

    /**
     * 方法2：贪心解法（最优解）
     * 使用自定义比较器对字符串数组排序，然后依次拼接
     * 
     * 算法正确性证明：
     * 1. 定义比较规则：a < b 当且仅当 a+b < b+a
     * 2. 该比较规则满足传递性
     * 3. 局部最优（相邻两个字符串的最优顺序）导致全局最优
     * 
     * 算法复杂度：
     * - 时间复杂度：O(N * log(N) * M) - 排序的时间复杂度
     * - 空间复杂度：O(N * M) - 存储字符串的空间
     * 
     * 优势：
     * - 时间复杂度远优于暴力解法
     * - 算法思路清晰，易于实现
     * - 可扩展到大规模数据
     * 
     * @param ss 字符串数组
     * @return 字典序最小的拼接结果
     */
    public static String lowestString2(String[] ss) {
        if (ss == null || ss.length == 0) {
            return "";
        }
        
        // 使用自定义比较器进行排序
        Arrays.sort(ss, new Comp());
        
        // 按排序后的顺序依次拼接
        String res = "";
        for (int i = 0; i < ss.length; i++) {
            res += ss[i];
        }
        return res;
    }

    /**
     * 生成指定最大长度的随机字符串
     * 用于测试算法正确性
     * 
     * @param maxLen 字符串最大长度
     * @return 随机生成的字符串
     */
    private static String randomString(int maxLen) {
        char[] ans = new char[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < ans.length; i++) {
            int val = (int) (Math.random() * 5);  // 0-4的随机数
            // 随机选择大写或小写字母
            ans[i] = (Math.random() <= 0.5) ? (char) (65 + val) : (char) (97 + val);
        }
        return String.valueOf(ans);
    }

    /**
     * 生成指定最大数量和长度的随机字符串数组
     * 用于大规模测试
     * 
     * @param maxArrLen 数组最大长度
     * @param maxLen 字符串最大长度
     * @return 随机生成的字符串数组
     */
    private static String[] randomStrings(int maxArrLen, int maxLen) {
        String[] ans = new String[(int) ((maxArrLen + 1) * Math.random()) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = randomString(maxLen);
        }
        return ans;
    }

    /**
     * 复制字符串数组
     * 避免测试时修改原数组影响对比
     * 
     * @param arr 原数组
     * @return 复制后的数组
     */
    private static String[] copy(String[] arr) {
        String[] ans = new String[arr.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = String.valueOf(arr[i]);
        }
        return ans;
    }

    /**
     * 大规模测试两种算法的正确性
     * 通过对比暴力解法和贪心解法的结果验证算法正确性
     * 
     * 测试策略：
     * 1. 生成随机的字符串数组
     * 2. 分别用两种方法求解
     * 3. 比较结果是否一致
     * 4. 发现不一致时输出详细信息并停止
     * 
     * 测试限制：
     * - 由于暴力解法的复杂度很高，数组长度限制在6以内
     * - 字符串长度也要相对较短，避免测试时间过长
     */
    public static void main(String[] args) {
        int times = 10000;        // 测试次数
        int maxArrLen = 6;        // 数组最大长度（暴力解法限制）
        int maxLen = 5;           // 字符串最大长度
        
        System.out.println("字典序最小拼接问题测试开始");
        System.out.println("测试参数：次数=" + times + ", 数组最大长度=" + maxArrLen + 
                         ", 字符串最大长度=" + maxLen);
        
        for (int i = 0; i < times; i++) {
            // 生成随机测试数据
            String[] arr1 = randomStrings(maxArrLen, maxLen);
            String[] arr2 = copy(arr1);  // 复制数组避免干扰
            
            // 分别用两种方法求解
            String ans1 = lowestString1(arr1);  // 暴力解法
            String ans2 = lowestString2(arr2);  // 贪心解法
            
            // 验证结果一致性
            if (!ans1.equals(ans2)) {
                System.out.println("发现错误！");
                System.out.print("输入数组: [");
                for (int j = 0; j < arr1.length; j++) {
                    System.out.print("\"" + arr1[j] + "\"");
                    if (j < arr1.length - 1) System.out.print(", ");
                }
                System.out.println("]");
                System.out.println("暴力解法结果: \"" + ans1 + "\"");
                System.out.println("贪心解法结果: \"" + ans2 + "\"");
                return;
            }
            
            // 每1000次测试输出进度
            if ((i + 1) % 1000 == 0) {
                System.out.println("已完成 " + (i + 1) + " 次测试");
            }
        }
        
        System.out.println("测试完成！所有 " + times + " 次测试都通过");
        System.out.println("贪心算法实现正确，推荐使用方法2");
    }
}
