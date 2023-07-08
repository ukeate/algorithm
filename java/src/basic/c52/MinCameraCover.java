package basic.c52;

// 树节点放相机能照亮上下两层, 求最少相机数
public class MinCameraCover {
    public static class Node {
        public int val;
        public Node left;
        public Node right;
    }

    private static class Info {
        // x没被覆盖时，x为头需要的相机数
        public long uncovered;
        // x被覆盖但没有相机时，x为头需要的相机数
        public long coveredNoCamera;
        // x被覆盖并有相机，x为头需要的相机数
        public long coveredHasCamera;
        public Info(long un, long no, long has) {
            uncovered = un;
            coveredNoCamera = no;
            coveredHasCamera = has;
        }
    }

    private static Info process1(Node x) {
        if (x == null) {
            return new Info(Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
        }
        Info left = process1(x.left);
        Info right = process1(x.right);
        long uncovered = left.coveredNoCamera + right.coveredNoCamera;
        long coveredNoCamera = Math.min(left.coveredHasCamera + right.coveredHasCamera,
                Math.min(left.coveredHasCamera + right.coveredNoCamera,
                        left.coveredNoCamera + right.coveredHasCamera));
        long coveredHasCamera = 1 + Math.min(left.uncovered, Math.min(left.coveredNoCamera, left.coveredHasCamera))
                + Math.min(right.uncovered, Math.min(right.coveredNoCamera, right.coveredHasCamera));
        return new Info(uncovered, coveredNoCamera, coveredHasCamera);
    }

    public static int min(Node root) {
        Info info = process1(root);
        return (int) Math.min(info.uncovered + 1, Math.min(info.coveredNoCamera, info.coveredHasCamera));
    }
}
