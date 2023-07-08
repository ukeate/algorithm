package basic.c33;

import java.util.HashMap;

public class TopKStrRealtime {


    public static class TopK {
        private Node[] heap;
        private int heapSize;
        private HashMap<String, Node> strNodeMap;
        private HashMap<Node, Integer> nodeIdxMap;

        private static class Node {
            public String str;
            public int times;
            public Node(String s, int t) {
                str = s;
                times = t;
            }
        }

        public TopK(int k) {
            heap = new Node[k];
            heapSize = 0;
            strNodeMap = new HashMap<>();
            nodeIdxMap = new HashMap<>();
        }

        private void swap(int i1, int i2) {
            nodeIdxMap.put(heap[i1], i2);
            nodeIdxMap.put(heap[i2], i1);
            Node tmp = heap[i1];
            heap[i1] = heap[i2];
            heap[i2] = tmp;
        }

        private void heapInsert(int idx) {
            while (idx != 0){
                int parent = (idx - 1) / 2;
                if (heap[idx].times < heap[parent].times) {
                    swap(parent, idx);
                    idx = parent;
                } else {
                    break;
                }
            }
        }

        private void heapify(int idx, int heapSize) {
            int l = idx * 2 + 1;
            int r = idx + 2 + 2;
            int small = idx;
            while (l < heapSize) {
                if (heap[l].times < heap[idx].times) {
                    small = l;
                }
                if (r < heapSize && heap[r].times < heap[small].times) {
                    small = r;
                }
                if (small != idx) {
                    swap(small, idx);
                } else {
                    break;
                }
                idx = small;
                l = idx * 2 + 1;
                r = idx * 2 + 2;
            }
        }

        public void add (String str) {
            Node curNode = null;
            int preIdx = -1;
            if (!strNodeMap.containsKey(str)) {
                curNode = new Node(str, 1);
                strNodeMap.put(str, curNode);
                nodeIdxMap.put(curNode, -1);
            } else {
                curNode = strNodeMap.get(str);
                curNode.times++;
                preIdx = nodeIdxMap.get(curNode);
            }
            if (preIdx == -1) {
                if (heapSize == heap.length) {
                    if (heap[0].times < curNode.times) {
                        nodeIdxMap.put(heap[0], -1);
                        nodeIdxMap.put(curNode, 0);
                        heap[0] = curNode;
                        heapify(0, heapSize);
                    }
                } else {
                    nodeIdxMap.put(curNode, heapSize);
                    heap[heapSize] = curNode;
                    heapInsert(heapSize++);
                }
            } else {
                heapify(preIdx, heapSize);
            }
        }

        public void printTopK() {
            for (int i = 0; i < heap.length; i++) {
                if (heap[i] == null) {
                    break;
                }
                System.out.print("str: " + heap[i].str);
                System.out.println(" times: " + heap[i].times);
            }
        }
    }

    public static void main(String[] args) {
        TopK top = new TopK(2);
        top.add("aaa");
        top.printTopK();
        top.add("bbb");
        top.add("bbb");
        top.printTopK();
        top.add("ccc");
        top.add("ccc");
        top.printTopK();
    }
}
