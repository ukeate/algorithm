/*
 * 3 3
 * *??
 * ???
 * ?*?
 */
#include <bits/stdc++.h>
using namespace std;

int n, m;
char a[110][110];
int ans[110][110];

int main() {
    scanf("%d%d", &n, &m);
    for (int i = 0; i < n; i++) {
        scanf("%s", a[i]);
    }
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            if (a[i][j] == '*') {
                ans[i][j] = -1;
                continue;
            }
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    int x = i +dx, y = j + dy;
                    if (x >= 0 && x < n &&y >= 0 && y < m) {
                        if (a[x][y] == '*') ans[i][j]++;
                    }
                }
            }
        }
    }
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            if (ans[i][j] == -1) printf("*");
            else printf("%d", ans[i][j]);
        }
        printf("\n");
    }
}