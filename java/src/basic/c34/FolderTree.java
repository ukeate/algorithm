package basic.c34;

import java.util.TreeMap;

/**
 * 目录树结构构建与展示
 * 
 * 问题描述：
 * 给定一组目录路径字符串，构建并输出完整的树形目录结构。
 * 路径使用反斜杠"\"分隔，需要按字典序排列同级目录。
 * 
 * 例如：
 * 输入：["b\\st", "d\\", "a\\d\\e", "a\\b\\c"]
 * 输出：
 *     a
 *         b
 *             c
 *         d
 *             e
 *     b
 *         st
 *     d
 * 
 * 算法思路：
 * 1. 使用树形结构表示目录层次关系
 * 2. TreeMap保证同级目录按字典序排列
 * 3. 递归构建树，递归输出结构
 * 
 * 时间复杂度：O(N*M*logK)，N为路径数量，M为平均路径深度，K为平均同级目录数
 * 空间复杂度：O(N*M)，用于存储树结构
 */
public class FolderTree {
    
    /**
     * 目录树节点类
     * 每个节点代表一个目录，包含路径名和子目录映射
     */
    private static class Node {
        public String path;                    // 当前目录名
        public TreeMap<String, Node> nexts;   // 子目录映射（TreeMap保证有序）
        
        /**
         * 构造函数
         * @param p 目录路径名
         */
        public Node(String p) {
            this.path = p;
            nexts = new TreeMap<>();  // 使用TreeMap自动按字典序排序
        }
    }

    /**
     * 根据路径数组构建目录树
     * 
     * 算法步骤：
     * 1. 创建虚拟根节点
     * 2. 遍历每个路径，按"\"分割成目录名数组
     * 3. 从根节点开始，逐级创建或定位子目录节点
     * 4. TreeMap确保同级目录自动按字典序排列
     * 
     * @param paths 目录路径数组
     * @return 构建好的目录树根节点
     */
    private static Node getTree(String[] paths) {
        Node head = new Node("");  // 创建虚拟根节点
        
        // 处理每个路径
        for (String path : paths) {
            String[] ps = path.split("\\\\");  // 按反斜杠分割路径
            Node cur = head;  // 从根节点开始
            
            // 逐级构建目录结构
            for (int i = 0; i < ps.length; i++) {
                if (!cur.nexts.containsKey(ps[i])) {
                    // 如果子目录不存在，创建新节点
                    cur.nexts.put(ps[i], new Node(ps[i]));
                }
                // 移动到下一级目录
                cur = cur.nexts.get(ps[i]);
            }
        }
        
        return head;
    }

    /**
     * 生成指定数量的空格字符串，用于缩进显示
     * 
     * @param n 缩进级别（层数）
     * @return 包含(n-1)*4个空格的字符串
     */
    private static String space(int n) {
        String rst = "";
        for (int i = 1; i < n; i++) {
            rst += "    ";  // 每级缩进4个空格
        }
        return rst;
    }

    /**
     * 递归打印目录树结构
     * 
     * 算法思路：
     * 1. 如果不是根节点（level > 0），打印当前目录名和缩进
     * 2. 递归打印所有子目录（TreeMap保证按字典序输出）
     * 
     * @param node 当前节点
     * @param level 当前层级（0为根节点，不打印）
     */
    private static void printTree(Node node, int level) {
        if (level > 0) {
            // 打印当前目录（根节点不打印）
            System.out.println(space(level) + node.path);
        }
        
        // 递归打印所有子目录
        // TreeMap的values()按key的字典序返回，确保输出有序
        for (Node next : node.nexts.values()) {
            printTree(next, level + 1);
        }
    }

    /**
     * 主方法：构建并打印目录树
     * 
     * @param paths 目录路径数组
     */
    public static void tree(String[] paths) {
        // 边界条件检查
        if (paths == null || paths.length == 0) {
            return;
        }
        
        // 构建目录树
        Node head = getTree(paths);
        
        // 打印目录树（从level=0开始，但根节点不打印）
        printTree(head, 0);
    }

    /**
     * 测试方法：验证目录树构建和显示功能
     */
    public static void main(String[] args) {
        System.out.println("=== 目录树构建测试 ===");
        
        // 测试用例：多个路径，包含嵌套结构
        String[] arr = { "b\\st", "d\\", "a\\d\\e", "a\\b\\c" };
        
        System.out.println("输入路径：");
        for (String path : arr) {
            System.out.println("  " + path);
        }
        
        System.out.println("\n生成的目录树结构：");
        tree(arr);
        
        System.out.println("\n=== 其他测试用例 ===");
        
        // 测试用例2：更复杂的目录结构
        String[] arr2 = {
            "root\\src\\main\\java\\com\\example",
            "root\\src\\main\\resources",
            "root\\src\\test\\java",
            "root\\target\\classes",
            "root\\pom.xml"
        };
        
        System.out.println("Java项目目录结构：");
        tree(arr2);
        
        // 测试用例3：空输入
        System.out.println("\n空输入测试：");
        tree(new String[]{});
        tree(null);
        
        System.out.println("=== 测试完成 ===");
    }
}
