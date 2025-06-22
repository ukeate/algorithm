package base.tree;

/**
 * 纸张折叠问题
 * 问题描述：把一张纸条竖着放在桌子上，然后从纸条的下边向上方对折1次，
 * 压出折痕后展开。此时折痕是凹的，即折痕凸起的方向指向纸条的背面。
 * 如果从纸条的下边向上方连续对折2次，压出折痕后展开，此时有三条折痕，
 * 从上到下依次是：凹、凹、凸。给定一个输入参数n，代表纸条从下边向上方
 * 连续对折n次，请从上到下输出所有折痕的方向。
 * 
 * 算法思路：
 * 第i次折叠的折痕规律：
 * 1. 第1次折叠产生1条折痕：凹
 * 2. 第2次折叠产生3条折痕：凹 凹 凸
 * 3. 第3次折叠产生7条折痕：凹 凹 凸 凹 凹 凸 凸
 * 
 * 规律发现：可以用二叉树结构来表示折痕：
 * - 头节点永远是凹折痕
 * - 每个节点的左子树头节点都是凹折痕
 * - 每个节点的右子树头节点都是凸折痕
 * - 中序遍历就是折痕从上到下的顺序
 */
public class PaperFolds {
    /**
     * 递归生成折痕模式
     * @param i 当前层数
     * @param n 总折叠次数
     * @param down 当前节点是否为凹折痕
     */
    private static void process(int i, int n, boolean down) {
        if (i > n) {
            return;
        }
        // 先处理左子树（都是凹折痕）
        process(i + 1, n, true);
        // 打印当前节点
        System.out.println(down ? "凹" : "凸");
        // 再处理右子树（都是凸折痕）
        process(i + 1, n, false);
    }
    
    /**
     * 纸张折叠主方法
     * @param n 折叠次数
     */
    public static void paperFolds(int n) {
        // 从第1层开始，头节点是凹折痕
        process(1, n, true);
    }

    public static void main(String[] args) {
        // 测试：折叠4次的折痕模式
        paperFolds(4);
    }
}
