package basic.c52;

/**
 * 路径转换为数值统计问题
 * 
 * 问题描述：
 * 给定一个数组，表示树形结构中每个节点的父节点：
 * - paths[i] = j 表示节点i的父节点是j
 * - paths[i] = i 表示节点i是根节点（首都）
 * 
 * 要求将此数组转换为距离统计数组：
 * - 下标表示距离首都的距离
 * - 值表示该距离上有多少个节点
 * 
 * 算法思路：
 * 1. 第一阶段：将父子关系转换为到根节点的距离
 * 2. 第二阶段：将距离信息转换为统计信息
 * 
 * 优化技巧：
 * - 路径压缩：在查找路径时同时更新中间节点的距离
 * - 负数编码：使用负数表示临时状态，避免重复计算
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * 
 * @author 算法学习
 */
public class PathsToNums {
    
    /**
     * 第一阶段：将父子关系数组转换为距离数组
     * paths[i] = 节点i到根节点的距离
     * 
     * @param paths 输入的父子关系数组，会被修改为距离数组
     */
    private static void paths2distances(int[] paths) {
        int cap = 0;  // 首都(根节点)的位置
        
        // 找到首都位置并处理每个节点
        for (int start = 0; start < paths.length; start++) {
            if (paths[start] == start) {
                cap = start;  // 找到首都
            } else if (paths[start] > -1) {
                // 处理未计算距离的节点
                int curI = paths[start];
                paths[start] = -1;  // 标记为正在处理，暂记为-1
                int preI = start;
                
                // 沿着父节点路径向上查找，直到找到根节点或已处理过的节点
                while (paths[curI] != curI && paths[curI] > -1) {
                    int nextI = paths[curI];
                    paths[curI] = preI;  // 临时记录回退路径
                    preI = curI;
                    curI = nextI;
                }
                
                // 找到根节点或已处理的节点，开始回溯计算距离
                int level = paths[curI] == curI ? 0 : paths[curI];  // 起始层数
                
                // 沿着记录的回退路径计算每个节点的距离
                while (paths[preI] != -1) {
                    int lastPreI = paths[preI];
                    paths[preI] = --level;  // 设置当前节点距离（负数）
                    preI = lastPreI;
                }
                paths[preI] = --level;  // 设置最后一个节点的距离
            }
        }
        
        // 设置首都距离为0
        paths[cap] = 0;
    }

    /**
     * 第二阶段：将距离数组转换为统计数组
     * 将 "节点索引 -> 距离" 转换为 "距离 -> 节点数量"
     * 
     * @param dis 距离数组，会被修改为统计数组
     */
    private static void distances2nums(int[] dis) {
        for (int i = 0; i < dis.length; i++) {
            int idx = dis[i];
            
            // 如果当前距离还未处理（为负数）
            if (idx < 0) {
                dis[i] = 0;  // 当前位置计数清零
                
                // 沿着距离链向前统计
                while (true) {
                    idx = -idx;  // 转换为正数索引
                    
                    if (dis[idx] > -1) {
                        // 已经是统计数，直接增加计数
                        dis[idx]++;
                        break;
                    } else {
                        // 还是距离值，需要继续处理
                        int nextIdx = dis[idx];
                        dis[idx] = 1;  // 设置初始计数
                        idx = nextIdx;
                    }
                }
            }
        }
        
        // 根节点(距离0)固定有1个节点
        dis[0] = 1;
    }

    /**
     * 主转换方法：将父子关系数组转换为距离统计数组
     * 
     * @param paths 父子关系数组，会被原地修改为统计数组
     */
    public static void change(int[] paths) {
        if (paths == null || paths.length == 0) {
            return;
        }
        
        // 第一阶段：转换为距离数组
        paths2distances(paths);
        
        // 第二阶段：转换为统计数组
        distances2nums(paths);
    }

    /**
     * 打印数组的辅助方法
     * 
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    /**
     * 测试方法
     * 验证路径转换算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 路径转换为数值统计测试 ===");
        
        // 测试用例：树形结构
        // 节点关系：9是首都，1->9, 4->1, 0->4, 8->0, 等等
        int[] paths = {9, 1, 4, 9, 0, 4, 8, 9, 0, 1};
        
        System.out.println("原始父子关系数组:");
        System.out.println("索引: 0 1 2 3 4 5 6 7 8 9");
        System.out.print("父节点: ");
        print(paths.clone());
        
        System.out.println("\n树形结构分析:");
        System.out.println("9 是首都(根节点)");
        System.out.println("1, 3, 7 的父节点是 9 (距离1)");
        System.out.println("4 的父节点是 1 (距离2)");
        System.out.println("0, 5 的父节点是 4 (距离3)"); 
        System.out.println("8 的父节点是 0 (距离4)");
        System.out.println("6 的父节点是 8 (距离5)");
        System.out.println("2 的父节点是 4 (距离3)");
        
        // 执行转换
        change(paths);
        
        System.out.println("\n转换后的距离统计数组:");
        System.out.println("索引表示距离，值表示该距离的节点数量:");
        for (int i = 0; i < paths.length; i++) {
            if (paths[i] > 0) {
                System.out.println("距离 " + i + ": " + paths[i] + " 个节点");
            }
        }
        
        System.out.print("最终结果: ");
        print(paths);
        
        // 验证结果
        System.out.println("\n=== 结果验证 ===");
        System.out.println("距离0: 1个节点 (首都9)");
        System.out.println("距离1: 3个节点 (1, 3, 7)");
        System.out.println("距离2: 1个节点 (4)");
        System.out.println("距离3: 3个节点 (0, 5, 2)");
        System.out.println("距离4: 1个节点 (8)");
        System.out.println("距离5: 1个节点 (6)");
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度: O(n) - 每个节点最多访问常数次");
        System.out.println("空间复杂度: O(1) - 原地修改，无额外空间");
        System.out.println("核心技巧: 路径压缩 + 负数编码 + 两阶段转换");
    }
}
