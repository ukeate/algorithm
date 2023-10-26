package leetc.top;

import java.util.Arrays;

public class P587_ErectTheFence {

    // 叉乘的实现
    // 假设有a、b、c三个点，并且给出每个点的(x,y)位置
    // 从a到c的向量，在从a到b的向量的哪一侧？
    // 如果a到c的向量，在从a到b的向量右侧，返回正数
    // 如果a到c的向量，在从a到b的向量左侧，返回负数
    // 如果a到c的向量，和从a到b的向量重合，返回0
    private static int cross(int[] a, int[] b, int[] c) {
        return (b[1] - a[1]) * (c[0] - b[0]) - (b[0] - a[0]) * (c[1] - b[1]);
    }

    public int[][] outerTrees(int[][] points) {
        int n = points.length;
        int s = 0;
        int[][] stack = new int[n << 1][];
        Arrays.sort(points, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);
        for 
    }
}
