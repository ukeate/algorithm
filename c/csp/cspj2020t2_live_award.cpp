/*
 * 10 60
 * 200 300 400 500 600 600 0 300 200 100
 */
#include <bits/stdc++.h>

using namespace std;

int n, w, a[110000], cnt[610];

int main() {
    scanf("%d%d", &n, &w);
    for (int i = 1; i <= n; i++) {
        scanf("%d", &a[i]);
    }
    for (int i = 1; i <= n; i++) {
        int k = max(1, i * w / 100);
        cnt[a[i]] += 1;
        int s = 0;
        for (int j = 600; j >= 0; j--) {
            if (k >= s + 1 && k <= s + cnt[j]) {
                printf("%d ", j);
                break;
            }
            s += cnt[j];
        }
    }
    printf("\n");
}
