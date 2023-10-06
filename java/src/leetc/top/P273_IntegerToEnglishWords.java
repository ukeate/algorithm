package leetc.top;

public class P273_IntegerToEnglishWords {
    private static String num1To19(int num) {
        if (num < 1 || num > 19) {
            return "";
        }
        String[] names = {"One ", "Two ", "Three ", "Four ", "Five ", "Six ", "Seven ", "Eight ", "Nine ", "Ten ",
                "Eleven ", "Twelve ", "Thirteen ", "Fourteen ", "Fifteen ", "Sixteen ", "Seventeen ", "Eighteen ", "Nineteen "};
        return names[num - 1];
    }

    private static String num1To99(int num) {
        if (num < 1 || num > 99) {
            return "";
        }
        if (num < 20) {
            return num1To19(num);
        }
        int high = num / 10;
        String[] tyNames = {"Twenty ", "Thirty ", "Forty ", "Fifty ", "Sixty ", "Seventy ", "Eighty ", "Ninety "};
        return tyNames[high - 2] + num1To19(num % 10);
    }

    private static String num1To999(int num) {
        if (num < 1 || num > 999) {
            return "";
        }
        if (num < 100) {
            return num1To99(num);
        }
        int high = num / 100;
        return num1To19(high) + "Hundred " + num1To99(num % 100);
    }

    public static String numberToWords(int num) {
        if (num == 0) {
            return "Zero";
        }
        String res = "";
        if (num < 0) {
            res = "Negative ";
        }
        if (num == Integer.MIN_VALUE) {
            res += "Two Billion ";
            num %= -2000000000;
        }
        num = Math.abs(num);
        int high = 1000000000;
        int highIdx = 0;
        String[] names = {"Billion ", "Million ", "Thousand ", ""};
        while (num != 0) {
            int cur = num / high;
            num %= high;
            if (cur != 0) {
                res += num1To999(cur);
                res += names[highIdx];
            }
            high /= 1000;
            highIdx++;
        }
        return res.trim();
    }

    public static void main(String[] args) {
        int test = Integer.MIN_VALUE;
        System.out.println(numberToWords(test));
        System.out.println(numberToWords(10001));
        System.out.println(numberToWords(-10001));
    }
}
