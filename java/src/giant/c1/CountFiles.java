package giant.c1;

import java.io.File;
import java.util.Stack;

/**
 * 文件系统中统计文件数量问题
 * 
 * 问题描述：
 * 给定一个文件路径，统计该路径下（包括子目录）所有文件的数量。
 * 不包括目录本身，只统计文件数量。
 * 
 * 算法思路：
 * 使用栈来模拟DFS遍历，避免递归调用导致的栈溢出问题。
 * 1. 如果输入路径本身是文件，直接返回1
 * 2. 如果是目录，使用栈遍历所有子目录和文件
 * 3. 遇到文件则计数+1，遇到目录则入栈继续遍历
 * 
 * 时间复杂度：O(n)，n为所有文件和目录的总数
 * 空间复杂度：O(d)，d为最大目录深度
 */
public class CountFiles {
    /**
     * 统计指定路径下所有文件的数量（包括子目录中的文件）
     * 
     * @param path 文件或目录路径
     * @return 文件总数，如果路径不存在则返回0
     */
    public static int count(String path) {
        File root = new File(path);
        
        // 检查路径是否存在
        if (!root.isDirectory() && !root.isFile()) {
            return 0; // 路径不存在
        }
        
        // 如果输入路径本身就是一个文件
        if (root.isFile()) {
            return 1;
        }
        
        // 使用栈来模拟DFS遍历，避免递归调用的栈溢出风险
        Stack<File> stack = new Stack<>();
        stack.add(root); // 将根目录入栈
        int files = 0;
        
        while (!stack.isEmpty()) {
            File folder = stack.pop(); // 取出一个目录进行处理
            
            // 遍历当前目录下的所有文件和子目录
            for (File next : folder.listFiles()) {
                if (next.isFile()) {
                    files++; // 发现文件，计数器+1
                }
                if (next.isDirectory()) {
                    stack.push(next); // 发现子目录，入栈等待处理
                }
            }
        }
        return files;
    }

    /**
     * 测试方法
     * 注意：实际使用时需要修改路径为存在的目录
     */
    public static void main(String[] args) {
        String path = "C:\\Users\\PC\\Desktop"; // Windows路径示例
        System.out.println(count(path));
    }
}
