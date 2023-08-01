/*
 * 5 5
 * 2123
 * 1123
 * 23
 * 24
 * 24
 * 2 23
 * 3 123
 * 3 124
 * 2 12
 * 2 12
 */
#include <bits/stdc++.h>
using namespace std;

int n, q;
int a[1100];

int main() {
    scanf("%d%d", &n, &q);
    for (int i = 1; i<= n; i++) {
        scanf("%d", &a[i]);
    }
    for (int i = 1; i <= q; i++) {
        int l, b;
        scanf("%d%d", &l, &b);
        int pw = 1;
        for (int j = 0; j < l; j++)
            pw = pw * 10;
        int ans = -1;
        for (int j = 1; j <= n; j++) {
            if (a[j] % pw == b){
                if (ans == -1 || a[j] < ans) {
                    ans = a[j];
                }
            }
        }
        printf("%d\n", ans);
    }
}
