package leetc.followup;

// 求第1个出错的表达式下标
public class ArrayGrammar {
    private static Integer value(String str, int[] arr) {
        int val = Integer.valueOf(str.replace("[", "").replace("]", ""));
        int level = str.lastIndexOf("[") + 1;
        for (int i = 0; i < level; i++) {
            if (val < 0 || val >= arr.length) {
                return null;
            }
            val = arr[val];
        }
        return val;
    }

    public static int findError(String[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        String name = arr[0].substring(0, arr[0].indexOf("["));
        int size = Integer.valueOf(arr[0].substring(arr[0].indexOf("[") + 1, arr[0].indexOf("]")));
        int[] ar = new int[size];
        for (int i = 1; i < arr.length; i++) {
            String[] parts = arr[i].replace(" ", "").split("=");
            String left = parts[0].replace(name, "");
            left = left.substring(1, left.length() - 1);
            String right = parts[1].replace(name, "");
            Integer leftIdx = value(left, ar);
            Integer rightVal = value(right, ar);
            if (leftIdx < 0 || leftIdx >= size) {
                return i;
            }
            ar[leftIdx] = rightVal;
        }
        return 0;
    }

    public static void main(String[] args) {
        String[] contents = {
                "arr[7]",
                "arr[0]=6",
                "arr[1]=3",
                "arr[2]=1",
                "arr[3]=2",
                "arr[4]=4",
                "arr[5]=0",
                "arr[6]=5",
                "arr[arr[1]]=3",
                "arr[arr[arr[arr[5]]]]=arr[arr[arr[3]]]",
                "arr[arr[4]]=arr[arr[arr[0]]]",
                "arr[arr[1]] = 7",
                "arr[0] = arr[arr[arr[1]]]" };
        System.out.println(findError(contents));
    }
}
