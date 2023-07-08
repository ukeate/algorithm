package basic.c51;

import java.util.HashMap;

// Least Frequently Used
public class LFU {
    private static class Node {
        public Integer key;
        public Integer val;
        public Integer times;
        public Node up;
        public Node down;
        public Node(int k, int v, int t) {
            key = k;
            val = v;
            times = t;
        }
    }

    public static class NodeList {
        public Node head;
        public Node tail;
        // 前一个桶
        public NodeList last;
        // 后一个桶
        public NodeList next;
        public NodeList(Node node) {
            head = node;
            tail = node;
        }

        public void addHead(Node n) {
            n.down = head;
            head.up = n;
            head = n;
        }

        public void delete(Node n) {
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                if (n == head) {
                    head = n.down;
                    head.up = null;
                } else if (n == tail) {
                    tail = n.up;
                    tail.down = null;
                } else {
                    n.up.down = n.down;
                    n.down.up = n.up;
                }
            }
            n.up = null;
            n.down = null;
        }

        public boolean isEmpty() {
            return head == null;
        }
    }

    public static class LFUCache {
        private int capacity;
        private int size;
        // key对应node
        private HashMap<Integer, Node> records;
        // node所在桶
        private HashMap<Node, NodeList> heads;
        // 最左的桶
        private NodeList headList;
        public LFUCache(int k) {
            capacity = k;
            size = 0;
            records = new HashMap<>();
            heads = new HashMap<>();
            headList = null;
        }

        // 当前桶空了时删除并返回true
        private boolean modifyHeadList(NodeList removeNodeList) {
            if (removeNodeList.isEmpty()) {
                if (headList == removeNodeList) {
                    headList = removeNodeList.next;
                    if (headList != null) {
                        headList.last = null;
                    }
                } else {
                    removeNodeList.last.next = removeNodeList.next;
                    if (removeNodeList.next != null) {
                        removeNodeList.next.last = removeNodeList.last;
                    }
                }
                return true;
            }
            return false;
        }

        // 从oldNodeList删掉，放到次数+1的桶中
        private void move(Node node, NodeList oldNodeList) {
            oldNodeList.delete(node);
            NodeList preList = modifyHeadList(oldNodeList) ? oldNodeList.last : oldNodeList;
            NodeList nextList = oldNodeList.next;
            if (nextList == null) {
                NodeList newList = new NodeList(node);
                if (preList != null) {
                    preList.next = newList;
                }
                newList.last = preList;
                if (headList == null) {
                    headList = newList;
                }
                heads.put(node, newList);
            } else {
                if (nextList.head.times.equals(node.times)) {
                    nextList.addHead(node);
                    heads.put(node, nextList);
                } else {
                    NodeList newList = new NodeList(node);
                    if (preList != null) {
                        preList.next = newList;
                    }
                    newList.last = preList;
                    newList.next = nextList;
                    nextList.last = newList;
                    if (headList == nextList) {
                        headList = newList;
                    }
                    heads.put(node, newList);
                }
            }
        }

        public void put(int key, int val) {
            if (capacity == 0) {
                return;
            }
            if (records.containsKey(key)) {
                Node node = records.get(key);
                node.val = val;
                node.times++;
                NodeList curNodeList = heads.get(node);
                move(node, curNodeList);
            } else {
                if (size == capacity) {
                    Node node = headList.tail;
                    headList.delete(node);
                    modifyHeadList(headList);
                    records.remove(node.key);
                    heads.remove(node);
                    size--;
                }
                Node node = new Node(key, val, 1);
                if (headList == null) {
                    headList = new NodeList(node);
                } else {
                    if (headList.head.times.equals(node.times)) {
                        headList.addHead(node);
                    } else {
                        NodeList newList = new NodeList(node);
                        newList.next = headList;
                        headList.last = newList;
                        headList = newList;
                    }
                }
                records.put(key, node);
                heads.put(node, headList);
                size++;
            }
        }

        public Integer get(int key) {
            if (!records.containsKey(key)) {
                return null;
            }
            Node node = records.get(key);
            node.times++;
            NodeList curNodeList = heads.get(node);
            move(node, curNodeList);
            return node.val;
        }
    }
}
