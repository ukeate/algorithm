#include <cstring>
#include <iostream>

// r表示rank数组
int n = 10000, m = 10, a[10001], c[11], r[10001];

void countingsort() {
    memset(c, 0, sizeof(c));
    for (int i = 1; i <= n; i++)
        ++c[a[i]];
    for (int i = 1; i <= m; i++)
        for (int j = 1; j <= c[i]; j++)
            printf("%d ", i);
    printf("\n");

    for (int i = 2; i <= m; i++)
        c[i] += c[i - 1];
    for (int i = n; i; --i)
        r[i] = c[a[i]]--;
    for (int i = 1; i <= n; i++)
        printf("%d ", r[i]);
    printf("\n");
};