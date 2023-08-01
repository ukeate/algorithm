#include <bits/stdc++.h>
using namespace std;

int n, x, ans;

int calc(int d, int x) {
    int cnt = 0;
    while (d != 0) {
        if (d % 10 == x) cnt++;
        d /= 10;
    }
    return cnt;
}

int main() {
    scanf("%d%d", &n, &x);
    for (int i = 1; i <= n; i++) {
        ans += calc(i, x);
    }
    printf("%d\n", ans);
}
