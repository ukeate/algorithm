package leetc.top;

public class P968_BinaryTreeCameras {
    public static class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;
    }

    private static class Info {
        public long un;
        public long no;
        public long has;

        public Info(long uncovered, long coveredNoCamera, long coveredHasCamera) {
            un = uncovered;
            no = coveredNoCamera;
            has = coveredHasCamera;
        }
    }

    private static Info process1(TreeNode x) {
        if (x == null) {
            return new Info(Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
        }
        Info li = process1(x.left);
        Info ri = process1(x.right);
        long uncovered = li.no + ri.no;
        long coveredNoCamera = Math.min(li.has + ri.has,
                Math.min(li.has + ri.no, li.no + ri.has));
        long coveredHasCamera = Math.min(li.un, Math.min(li.no, li.has))
                + Math.min(ri.un, Math.min(ri.no, ri.has))
                + 1;
        return new Info(uncovered, coveredNoCamera, coveredHasCamera);
    }

    public int minCameraCover1(TreeNode root) {
        Info data = process1(root);
        return (int) Math.min(data.un + 1, Math.min(data.no, data.has));
    }

    //

    private enum Status {
        un/*uncovered*/, no/*coveredNoCamera*/, has/*coveredHasCamera*/
    }
    private static class Info2 {
        public Status status;
        public int cameras;
        public Info2(Status status, int cameras) {
            this.status = status;
            this.cameras = cameras;
        }
    }

    private static Info2 process2(TreeNode x) {
        if (x == null) {
            return new Info2(Status.no, 0);
        }
        Info2 li = process2(x.left);
        Info2 ri = process2(x.right);
        int cameras = li.cameras + ri.cameras;
        if(li.status == Status.un || ri.status == Status.un) {
            return new Info2(Status.has, cameras + 1);
        }
        if (li.status == Status.has || ri.status == Status.has) {
            return new Info2(Status.no, cameras);
        }
        return new Info2(Status.un, cameras);
    }
    public static int minCameraCover2(TreeNode root) {
        Info2 data = process2(root);
        return data.cameras + (data.status == Status.un ? 1 : 0);
    }
}
