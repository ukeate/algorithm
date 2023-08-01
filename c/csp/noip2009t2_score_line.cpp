/*
 * 6 3
 * 1000 90
 * 3239 88
 * 2390 95
 * 7231 84
 * 1005 95
 * 1001 88
 */
#include <bits/stdc++.h>
using namespace std;
int n, m;
struct people {
    int score, id;
}p[5010];

int main() {
    scanf("%d%d", &n, &m);
    for (int i = 1; i <= n; i++) {
        scanf("%d%d", &p[i].id, &p[i].score);
    }
    for (int i = 1; i <= n; i++) {
        for (int j = i + 1; j <= n; j++) {
            if (p[j].score > p[i].score ||
            (p[j].score == p[i].score && p[j].id < p[i].id)) {
                swap(p[i], p[j]);
            }
        }
    }
    int bar = p[(int)(m * 1.5)].score;
    int cnt = 0;
    for (int i = 1; i <= n; i++) {
        if (p[i].score >= bar) {
            cnt += 1;
        }
    }
    printf("%d %d\n", bar, cnt);
    for (int i = 1; i <= n; i++) {
        if (p[i].score >= bar) {
            printf("%d %d\n", p[i].id, p[i].score);
        }
    }
}