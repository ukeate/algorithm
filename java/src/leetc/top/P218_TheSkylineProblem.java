package leetc.top;

import java.util.*;

public class P218_TheSkylineProblem {

    private static class Node {
        public int x;
        public boolean isAdd;
        public int h;
        public Node(int x, boolean isAdd, int h) {
            this.x = x;
            this.isAdd = isAdd;
            this.h = h;
        }
    }

    private static class Comp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            if (o1.x != o2.x) {
                return o1.x - o2.x;
            }
            if (o1.isAdd != o2.isAdd) {
                return o1.isAdd ? -1 : 1;
            }
            return 0;
        }
    }

    public List<List<Integer>> getSkyline(int[][] matrix) {
        Node[] nodes = new Node[matrix.length * 2];
        for (int i = 0; i < matrix.length; i++) {
            nodes[i * 2] = new Node(matrix[i][0], true, matrix[i][2]);
            nodes[i * 2 + 1] = new Node(matrix[i][1], false, matrix[i][2]);
        }
        Arrays.sort(nodes, new Comp());
        // <高度, 高度次数>
        TreeMap<Integer, Integer> heightTimes = new TreeMap<>();
        // <x, x高度>
        TreeMap<Integer, Integer> xHeight = new TreeMap<>();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].isAdd) {
                if (!heightTimes.containsKey(nodes[i].h)) {
                    heightTimes.put(nodes[i].h, 1);
                } else {
                    heightTimes.put(nodes[i].h, heightTimes.get(nodes[i].h) + 1);
                }
            } else {
                if (heightTimes.get(nodes[i].h) == 1) {
                    heightTimes.remove(nodes[i].h);
                } else {
                    heightTimes.put(nodes[i].h, heightTimes.get(nodes[i].h) - 1);
                }
            }
            if (heightTimes.isEmpty()) {
                xHeight.put(nodes[i].x, 0);
            } else {
                xHeight.put(nodes[i].x, heightTimes.lastKey());
            }
        }
        List<List<Integer>> ans = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : xHeight.entrySet()) {
            int curX = entry.getKey();
            int height = entry.getValue();
            if (ans.isEmpty() || ans.get(ans.size() - 1).get(1) != height) {
                ans.add(new ArrayList<>(Arrays.asList(curX, height)));
            }
        }
        return ans;
    }
}
