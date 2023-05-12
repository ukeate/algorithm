package graph.critical_path;

import java.util.Scanner;


public class topology_sort {

    static boolean TopologyicalSort(int A[][], MyArrayStack ma, int[] print) {
        int vexnum = A.length - 1;
        int indegrees[] = new int[vexnum + 1];


        for (int j = 1; j <= vexnum; j++) {
            int temp = 0;
            for (int i = 1; i <= vexnum; i++) {
                if (A[i][j] == 1)
                    temp++;
            }
            indegrees[j] = temp;
        }


        for (int i = 1; i <= vexnum; i++) {
            if (indegrees[i] == 0) {
                ma.push(i);
            }
        }

        if (ma.isEmpty()) {
            return false;
        }

        int count = 0;
        while (!ma.isEmpty()) {
            int temp = (Integer) ma.pop();
            print[++count] = temp;
            for (int j = 1; j <= vexnum; j++) {
                if (A[temp][j] == 1) {
                    indegrees[j]--;
                    if (indegrees[j] == 0) {
                        ma.push(j);
                    }
                }
            }

        }
        if (count < vexnum) {
            return false;
        } else
            return true;
    }

    public static void main(String[] args) {
        System.out.println("Input number of the nodes:");
        Scanner scan = new Scanner(System.in);
        int node_num = scan.nextInt();
        int A[][] = new int[node_num + 1][node_num + 1];
        for (int k = 0; k < node_num + 1; k++) {
            for (int k1 = 0; k1 < node_num + 1; k1++) {
                A[k][k1] = 0;
            }
        }
        System.out.println("Input adjacent nodes, -1 to finish your input:");
        int i = 0;
        int j = 0;
        while ((i = scan.nextInt()) != -1) {
            j = scan.nextInt();
            A[i][j] = 1;
        }
        MyArrayStack<Integer> mas = new MyArrayStack<Integer>();
        int print_stack[] = new int[node_num + 1];


        if (TopologyicalSort(A, mas, print_stack)) {
            System.out.println("the result of the topological sorting is:");
            for (int k = 1; k <= node_num; k++) {
                System.out.print(print_stack[k] + "  ");
            }
            System.out.println();
        } else {
            System.out.println("topological sorting failure, it means that the graph presented by this adjacent matrix has loop!");
            System.exit(0);
        }


    }

}













	
