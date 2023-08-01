package leetc.top;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class P341_FlattenNestedListIterator {
    public interface NestedInteger {
        public boolean isInteger();
        public Integer getInteger();
        public List<NestedInteger> getList();
    }

    public static class NestedIterator implements Iterator<Integer> {
        private List<NestedInteger> list;
        // valId,下层listId,上层listId
        private Stack<Integer> stack;
        private boolean used;
        public NestedIterator(List<NestedInteger> nestedList) {
            list = nestedList;
            stack = new Stack<>();
            // 假装已get -1 valId
            stack.push(-1);
            used = true;
            hasNext();
        }

        private boolean itemFind(NestedInteger item, int itemId, Stack<Integer> stack) {
            if (item.isInteger()) {
                stack.add(itemId);
                return true;
            } else {
                List<NestedInteger> list = item.getList();
                for (int i = 0; i < list.size(); i++) {
                    if (itemFind(list.get(i), i, stack)) {
                        stack.add(itemId);
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean listFind(List<NestedInteger> nestedList, Stack<Integer> stack) {
            int idx = stack.pop();
            if (!stack.isEmpty() && listFind(nestedList.get(idx).getList(), stack)) {
                stack.push(idx);
                return true;
            }
            // 舍弃valId(在get时已使用), stack为空时处理item
            for (int i = idx + 1; i < nestedList.size(); i++) {
                if (itemFind(nestedList.get(i), i, stack)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean hasNext() {
            if (stack.isEmpty()) {
                return false;
            }
            if (!used) {
                return true;
            }
            if (listFind(list, stack)) {
                used = false;
            }
            return !used;
        }

        private Integer get(List<NestedInteger> nestedList, Stack<Integer> stack) {
            int idx = stack.pop();
            Integer ans = null;
            if (!stack.isEmpty()) {
                ans = get(nestedList.get(idx).getList(), stack);
            } else {
                ans = nestedList.get(idx).getInteger();
            }
            stack.push(idx);
            return ans;
        }

        @Override
        public Integer next() {
            Integer ans = null;
            if (!used) {
                ans = get(list, stack);
                used = true;
                hasNext();
            }
            return ans;
        }
    }

}
