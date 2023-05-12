package chapter1;

import org.junit.Test;

/**
 * Created by outrun on 4/7/16.
 */
public class Ex1_2 {

    class IndexPair {
        private int horizontalInd = 0;
        private int verticalInd = 0;

        public IndexPair(int horizontalInd, int verticalInd) {
            this.setHorizontalInd(horizontalInd);
            this.setVerticalInd(verticalInd);
        }

        public char genChar(char[][] charts) {
            return charts[verticalInd][horizontalInd];
        }

        public int getHorizontalInd() {
            return horizontalInd;
        }

        public void setHorizontalInd(int horizontalInd) {
            this.horizontalInd = horizontalInd;
        }

        public int getVerticalInd() {
            return verticalInd;
        }

        public void setVerticalInd(int verticalInd) {
            this.verticalInd = verticalInd;
        }

        @Override
        public String toString() {
            return this.horizontalInd + " | " + this.verticalInd + "\n";
        }
    }

    private char[][] chars = new char[][]{
            {'t', 'h', 'i', 's'},
            {'w', 'a', 't', 's'},
            {'o', 'a', 'h', 'g'},
            {'f', 'g', 'd', 't'},
    };
    private char[][] words = new char[][]{
            {'t', 'h', 'i', 's'},
            {'t', 'w', 'o'},
            {'f', 'a', 't'},
            {'t', 'h', 'a', 't'},
    };
    int charsVerticalLen = 4;
    int charsHorizontalLen = 4;
    int wordsVerticalLen = 4;

    private void printIndexes(IndexPair[] coordinates, int indStart, int indLen, boolean inverse) {

        if (inverse) {
            for (int ind = indStart + indLen - 1; ind >= indStart; ind--) {
                System.out.print(coordinates[ind]);
            }
        } else {
            for (int ind = indStart; ind < indLen; ind++) {
                System.out.print(coordinates[ind]);
            }
        }

        System.out.println("-->");
    }

    private void matchWord(IndexPair[] coordinates, int indStart, int indLen) {

        char[] word;
        for (int wordsInd = 0; wordsInd < wordsVerticalLen; wordsInd++) {
            word = words[wordsInd];
            int len = word.length;
            if (len != indLen) {
                continue;
            }

            IndexPair coordinate;

            boolean matchFlag = true;
            int coInd = indStart;
            for (int wordInd = 0; wordInd < len; wordInd++) {
                coordinate = coordinates[coInd];
                if (word[wordInd] != coordinate.genChar(chars)) {
                    matchFlag = false;
                    break;
                }
                coInd++;
            }

            boolean inverseMatchFlag = true;
            int inverseCoInd = indStart + indLen - 1;
            for (int wordInd = 0; wordInd < len; wordInd++) {
                coordinate = coordinates[inverseCoInd];
                if (word[wordInd] != coordinate.genChar(chars)) {
                    inverseMatchFlag = false;
                    break;
                }
                inverseCoInd--;
            }

            if (matchFlag) {
                printIndexes(coordinates, indStart, indLen, false);
            }
            if (inverseMatchFlag) {
                printIndexes(coordinates, indStart, indLen, true);
            }
        }

    }

    private void scanWord(IndexPair[] context) {
        int len = context.length;
        for (int ind = 0; ind < len; ind++) {
            for (int ind2 = ind; ind2 < len; ind2++) {
                matchWord(context, ind, ind2 - ind + 1);
            }
        }
    }

    @Test
    public void charWordPuzzle() {

        IndexPair[] context1 = new IndexPair[charsHorizontalLen];
        for (int charsVerticalInd = 0; charsVerticalInd < charsVerticalLen; charsVerticalInd++) {
            for (int horizontalInd = 0; horizontalInd < charsHorizontalLen; horizontalInd++) {
                context1[horizontalInd] = new IndexPair(horizontalInd, charsVerticalInd);
            }
            scanWord(context1);
        }

        IndexPair[] context2 = new IndexPair[charsVerticalLen];
        for (int charsHorizontalInd = 0; charsHorizontalInd < charsHorizontalLen; charsHorizontalInd++) {
            for (int verticalInd = 0; verticalInd < charsVerticalLen; verticalInd++) {
                context2[verticalInd] = new IndexPair(charsHorizontalInd, verticalInd);
            }
            scanWord(context2);
        }

        if (charsVerticalLen == charsHorizontalLen) {
            IndexPair[] context3_1 = new IndexPair[charsHorizontalLen];
            IndexPair[] context3_2 = new IndexPair[charsHorizontalLen];
            for (int charsHorizontalInd = 0; charsHorizontalInd < charsHorizontalLen; charsHorizontalInd++) {
                context3_1[charsHorizontalInd] = new IndexPair(charsHorizontalInd, charsHorizontalInd);
                context3_2[charsHorizontalInd] = new IndexPair(charsHorizontalInd, charsHorizontalLen - 1 - charsHorizontalInd);
            }
            scanWord(context3_1);
            scanWord(context3_2);
        }

    }
}
