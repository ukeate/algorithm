package leetc.top;

import java.util.ArrayList;
import java.util.List;

public class P119_PascalTriangleII {
    public List<Integer> getRow(int rowIdx) {
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i <= rowIdx; i++) {
            for (int j = i - 1; j > 0; j--) {
                ans.set(j, ans.get(j - 1) + ans.get(j));
            }
            ans.add(1);
        }
        return ans;
    }
}
