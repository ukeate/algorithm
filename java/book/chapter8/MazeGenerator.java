package chapter8;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MazeGenerator {
    private int rows;
    private int columns;

    private int[] maze;

    public MazeGenerator(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        maze = new int[rows * columns];

        for (int i = 0; i < rows * columns; i++) {
            maze[i] = -1;
        }
    }

    public void generateMaze() {
        Random random = new Random();

        int maxNum = (columns * 2 - 1) * (rows - 1) + columns - 1;

        int randomNum = 0;
        int rowIndex = 0;
        int columnIndex = 0;
        int restNum = 0;

        int nextNumStep = 1;
        int firstNode = 0;
        int secondNode = 0;
        int firstNodeParent = 0;
        int secondNodeParent = 0;
        Set<Integer> numSet = new HashSet<Integer>();

        do {
            randomNum = random.nextInt(maxNum);
            restNum = randomNum % (columns * 2 - 1);
            rowIndex = randomNum / (columns * 2 - 1);
            if (rowIndex == rows - 1) {
                columnIndex = restNum;
                nextNumStep = 1;
            } else {
                columnIndex = restNum / 2;
                if (restNum % 2 == 0) {
                    nextNumStep = columns;
                } else {
                    nextNumStep = 1;
                }
            }
            firstNode = rowIndex * columns + columnIndex;
            secondNode = firstNode + nextNumStep;
            firstNodeParent = findParentNode(firstNode);
            secondNodeParent = findParentNode(secondNode);

            if (firstNodeParent != secondNodeParent) {
                numSet.add(randomNum);
                unionNode(firstNodeParent, secondNodeParent);
            }
            firstNode = 0;
            secondNode = rows * columns - 1;
            firstNodeParent = findParentNode(firstNode);
            secondNodeParent = findParentNode(secondNode);

        } while (firstNodeParent != secondNodeParent);
        printMaze();
        printGeneratedMaze(numSet);
    }

    private int findParentNode(int index) {
        int parentIndex = maze[index];
        if (parentIndex >= 0) {
            parentIndex = findParentNode(parentIndex);
            maze[index] = parentIndex;
        } else {
            parentIndex = index;
        }

        return parentIndex;
    }

    public void printGeneratedMaze(Set<Integer> numSet) {
        int maxNum = (columns * 2 - 1) * (rows - 1) + columns - 1;
        int columnNum = columns * 2 - 1;
        int restNum = 0;
        boolean first;
        boolean second;

        for (int j = 0; j < columns; j++) {
            if (j == 0) {
                System.out.print(" ");
            } else {
                System.out.print("_ ");
            }
        }

        System.out.println();

        for (int i = 0; i < maxNum; i++) {
            restNum = i % columnNum;
            if (restNum == 0) {
                System.out.print("|");
            }
            if (i / columnNum != rows - 1) {
                first = numSet.contains(i);
                if ((i + 1) % columnNum == 0) {
                    second = false;
                } else {
                    second = numSet.contains(++i);
                }
            } else {
                first = false;
                second = numSet.contains(i);
            }
            System.out.print(getSymbol(first, second));
            if ((i + 1) % columnNum == 0) {
                System.out.println();
            }
        }
        System.out.println(getSymbol(true, false));

    }

    private String getSymbol(boolean first, boolean second) {
        String symbol;
        if (first) {
            symbol = " ";
        } else {
            symbol = "_";
        }
        if (second) {
            symbol += " ";
        } else {
            symbol += "|";
        }
        return symbol;
    }

    private void printMaze() {
        for (int i = 0; i < rows * columns; i++) {
            System.out.print(maze[i] + " ");
        }
        System.out.println();
    }

    private void unionNode(int firstNodeParent, int secondNodeParent) {
        if (maze[firstNodeParent] < maze[secondNodeParent]) {
            maze[firstNodeParent] = maze[secondNodeParent] + maze[firstNodeParent];
            maze[secondNodeParent] = firstNodeParent;
        } else {
            maze[secondNodeParent] = maze[secondNodeParent] + maze[firstNodeParent];
            maze[firstNodeParent] = secondNodeParent;
        }
    }

    public static void main(String[] args) {
        MazeGenerator mg = new MazeGenerator(10, 20);
        mg.generateMaze();
    }
}