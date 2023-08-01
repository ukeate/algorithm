/**
 * 6
 */
#include <bits/stdc++.h>
using namespace std;

int n;

int main() {
    scanf("%d", &n);
    if (n % 2 == 1) {
        printf("-1\n");
        return 0;
    }
    for (int i = 30; i >= 0; i--) {
        if ((n & (1 << i)) != 0) {
            printf("%d ", 1 << i);
        }
    }
    printf("\n");
}
