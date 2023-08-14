package leetc.top;

import java.util.ArrayList;
import java.util.HashMap;

public class P815_BusRoutes {
    public static int numBusesToDestination(int[][] routes, int source, int target) {
        if (source == target) {
            return 0;
        }
        int n = routes.length;
        // <车站, 线路>
        HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < routes[i].length; j++) {
                if (!map.containsKey(routes[i][j])) {
                    map.put(routes[i][j], new ArrayList<>());
                }
                map.get(routes[i][j]).add(i);
            }
        }
        ArrayList<Integer> que = new ArrayList<>();
        boolean[] set = new boolean[n];
        for (int route : map.get(source)) {
            que.add(route);
            set[route] = true;
        }
        int len = 1;
        while (!que.isEmpty()) {
            ArrayList<Integer> nextLevel = new ArrayList<>();
            for (int route : que) {
                int[] bus = routes[route];
                for (int station : bus){
                    if (station == target) {
                        return len;
                    }
                    for (int nextRoute : map.get(station)) {
                        if (!set[nextRoute]) {
                            nextLevel.add(nextRoute);
                            set[nextRoute] = true;
                        }
                    }
                }
            }
            que = nextLevel;
            len++;
        }
        return -1;
    }
}
