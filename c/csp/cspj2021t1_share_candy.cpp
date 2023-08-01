/*
 * 7 16 23
 */
#include <bits/stdc++.h>
using namespace std;

int n, L, R;

int main() {
    scanf("%d%d%d", &n, &L, &R);
    if (L / n == R / n) {
        printf("%d\n", R % n);
    } else {
        printf("%d\n", n - 1);
    }
}
