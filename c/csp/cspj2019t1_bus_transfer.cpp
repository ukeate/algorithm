/*
 * 6
 * 0 10 3
 * 1 5 46
 * 0 12 50
 * 1 3 96
 * 0 5 110
 * 1 6 135
 */

#include <bits/stdc++.h>
using namespace std;

struct ticket {
    int t, price;
    bool used;
}a[110000];
int n, m, ans;

int main() {
    scanf("%d", &n);
    for (int i = 1; i <= n; i++) {
        int g, p, t;
        scanf("%d%d%d", &g, &p, &t);
        if (g == 0) {
            ans += p;
            m += 1;
            a[m].t = t;
            a[m].price = p;
        } else {
            int id = -1;
            for (int j = m; j >= 1; j--) {
                if (a[j].t < t - 45) break;
                if (a[j].price > p && !a[j].used) {
                    id = j;
                }
            }
            if (id != -1) {
                a[id].used = true;
            } else {
                ans += p;
            }
        }
    }
    printf("%d\n", ans);
}