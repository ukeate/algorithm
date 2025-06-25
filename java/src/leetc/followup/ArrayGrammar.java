package leetc.followup;

/**
 * 数组语法错误检测 (Array Grammar Error Detection)
 * 
 * 问题描述：
 * 给定一系列数组操作语句，检测第一个出现语法错误的语句下标
 * 语句格式：arr[index] = value 或 arr[arr[index]] = arr[arr[value]]
 * 
 * 错误类型：
 * 1. 数组下标越界（index < 0 或 index >= 数组长度）
 * 2. 嵌套数组访问时中间值越界
 * 
 * 解法思路：
 * 模拟执行 + 边界检查：
 * 1. 解析数组声明语句，获取数组名和大小
 * 2. 逐条处理赋值语句：
 *    - 解析左侧表达式，计算最终索引位置
 *    - 解析右侧表达式，计算要赋的值
 *    - 检查所有中间索引是否越界
 * 3. 返回第一个出错语句的下标，如果都正确则返回0
 * 
 * 时间复杂度：O(n×d) - n为语句数，d为最大嵌套深度
 * 空间复杂度：O(array_size) - 数组存储空间
 */
public class ArrayGrammar {
    /**
     * 计算嵌套数组表达式的值
     * 
     * 例如："[[[2]]]" 表示 arr[arr[arr[2]]]，需要进行3层嵌套访问
     * 
     * @param str 数组表达式字符串，如 "[2]" 或 "[[[2]]]"
     * @param arr 数组数据
     * @return 表达式的计算结果，如果访问越界则返回null
     */
    private static Integer value(String str, int[] arr) {
        // 提取最内层的数字
        int val = Integer.valueOf(str.replace("[", "").replace("]", ""));
        // 计算嵌套层数（'['的个数）
        int level = str.lastIndexOf("[") + 1;
        
        // 逐层访问数组，每一层都使用上一层的结果作为索引
        for (int i = 0; i < level; i++) {
            // 检查数组越界
            if (val < 0 || val >= arr.length) {
                return null; // 越界访问，返回null表示错误
            }
            val = arr[val]; // 使用当前值作为索引，获取下一层的值
        }
        return val;
    }

    /**
     * 查找第一个语法错误的语句下标
     * 
     * @param arr 语句数组，第一个元素是数组声明，后续是赋值语句
     * @return 第一个出错语句的下标（1-based），如果都正确返回0
     */
    public static int findError(String[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        // 解析数组声明语句，如 "arr[7]" -> name="arr", size=7
        String name = arr[0].substring(0, arr[0].indexOf("["));
        int size = Integer.valueOf(arr[0].substring(arr[0].indexOf("[") + 1, arr[0].indexOf("]")));
        int[] ar = new int[size]; // 创建模拟数组
        
        // 逐条处理赋值语句
        for (int i = 1; i < arr.length; i++) {
            // 去除空格并按'='分割
            String[] parts = arr[i].replace(" ", "").split("=");
            
            // 解析左侧表达式（赋值目标）
            String left = parts[0].replace(name, ""); // 移除数组名
            left = left.substring(1, left.length() - 1); // 移除最外层的'[]'
            
            // 解析右侧表达式（赋值源）
            String right = parts[1].replace(name, ""); // 移除数组名
            
            // 计算左侧表达式的索引值和右侧表达式的值
            Integer leftIdx = value(left, ar);
            Integer rightVal = value(right, ar);
            
            // 检查左侧索引是否越界（包括中间计算过程的越界）
            if (leftIdx == null || leftIdx < 0 || leftIdx >= size) {
                return i; // 返回出错语句的下标
            }
            
            // 检查右侧表达式计算是否出错
            if (rightVal == null) {
                return i; // 返回出错语句的下标
            }
            
            // 执行赋值操作
            ar[leftIdx] = rightVal;
        }
        
        return 0; // 所有语句都正确
    }

    public static void main(String[] args) {
        String[] contents = {
                "arr[7]",
                "arr[0]=6",
                "arr[1]=3",
                "arr[2]=1",
                "arr[3]=2",
                "arr[4]=4",
                "arr[5]=0",
                "arr[6]=5",
                "arr[arr[1]]=3",
                "arr[arr[arr[arr[5]]]]=arr[arr[arr[3]]]",
                "arr[arr[4]]=arr[arr[arr[0]]]",
                "arr[arr[1]] = 7",
                "arr[0] = arr[arr[arr[1]]]" };
        System.out.println(findError(contents));
    }
}
