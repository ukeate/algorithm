#include <bits/stdc++.h>
using namespace std;

int n, sgn, a[20], tot, ans;
int main() {
    scanf("%d", &n);
    if (n > 0) sgn = 1;
    else {
        sgn = -1;
        n = -n;
    }
//    while (n) {
//        a[tot] = n % 10;
//        n /= 10;
//        tot++;
//    }
//    for (int i = 0; i < tot; i++) {
//        ans = ans * 10 + a[i];
//    }
    while (n) {
        ans = ans * 10 + (n % 10);
        n /= 10;
    }
    ans = ans * sgn;
    printf("%d\n", ans);
}
