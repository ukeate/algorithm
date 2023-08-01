/**
 * 3 4
 * 3 2 1
 * 2 3
 * 1 3 2
 * 2 2
 * 2 3
 */
#include <bits/stdc++.h>
using namespace std;

struct number {
    int val, pos;
}a[8100];

int n, Q, ans[8100];

bool operator < (number a, number b) {
    return (a.val < b.val) || (a.val == b.val && a.pos < b.pos);
}

int main() {
    scanf("%d%d", &n, &Q);
    for (int i = 1; i <= n; i++) {
        scanf("%d", &a[i].val);
        a[i].pos = i;
    }
    for (int i = 1; i <= n; i++)
        for (int j = i + 1; j <= n; j++)
            if (a[j] < a[i])
                swap(a[j], a[i]);
    for (int i = 1; i <= n; i++)
        ans[a[i].pos] = i;
    for (int i = 0; i < Q; i++) {
        int op;
        scanf("%d", &op);
        if (op == 2) {
            int x;
            scanf("%d", &x);
            printf("%d\n", ans[x]);
        } else {
            int x, v;
            scanf("%d%d", &x, &v);
            int p = ans[x];
            a[p].val = v;
            number tmp = a[p];
            for (int j = p; j < n; j++) {
                a[j] = a[j + 1];
            }
            a[n] = tmp;
            for (int j = n; j >= 2; j--) {
                if (a[j] < a[j - 1])
                    swap(a[j], a[j - 1]);
                else
                    break;
            }
            for (int j = 1; j <= n; j++) {
                ans[a[j].pos] = j;
            }
        }
    }
}
