/*
 * 5
 * 100 -1 1 -3 0 10
 */


#include <bits/stdc++.h>
using namespace std;

int n, a[110];
int main() {
    scanf("%d", &n);
    for (int i = n; i >= 0; i--) {
        scanf("%d", &a[i]);
    }
    for (int i = n; i >= 0; i--) {
        if (a[i] == 0) continue;
        if (a[i] > 0) {
            if (i != n) printf("+");
        } else {
            printf("-");
        }
        // 系数
        int y = abs(a[i]);
        if (y != 1) printf("%d", y);
        else {
            if (i == 0) printf("%d", y);
        }
        if (i >= 2) printf("x^%d", i);
        else if (i == 1) printf("x");
    }
    printf("\n");
}