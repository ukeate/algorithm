/**
 * 2 3
 * 1 2
 * 0 3
 * 1 4
 * 0 1
 * 1 5
 * 1 2
 * 1
 */
#include <bits/stdc++.h>
using namespace std;

int n, m, pos, ans;
int stair[11000][110], num[11000][110];

int main() {
    scanf("%d%d", &n, &m);
    for (int i = 1; i <= n; i++) {
        for (int j = 0; j < m; j++) {
           scanf("%d%d", &stair[i][j], &num[i][j]);
        }
    }
    scanf("%d", &pos);
    for (int i = 1; i <= n; i++) {
        int x = num[i][pos];
        ans = (ans + x) % 20123;

        int t = 0;
        for (int j = 0; j < m; j++)
            t += stair[i][j];
        x %= t;
        if (x == 0) x = t;

        if (stair[i][pos]) --x;
        while (x) {
            pos = (pos + 1) % m;
            if (stair[i][pos]) --x;
        }
    }
    printf("%d\n", ans);
}