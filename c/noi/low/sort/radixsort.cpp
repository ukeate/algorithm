#include <cstring>
// m是数字位数, sa对应到原始位置, v表示某轮排序的第一关键字, r表示rank数组, c是数字计数
int n = 10000, m = 10, a[10001], sa[10001], v[10001], r[10001], c[10];

void countingsort() {
    memset(c, 0, sizeof(c));
    for (int i = 1; i <= n; i++)
        ++c[v[i]];
    for (int i = 1; i <= 9; i++)
        c[i] += c[i - 1];
    for (int i = n; i; --i)
        r[sa[i]] = c[v[sa[i]]]--;
    for (int i = 1; i <= n; i++)
        sa[r[i]] = i;
}

void radixsort() {
    for (int i = 1; i <= n; i++)
        sa[i] = i;
    int x = 1;
    for (int i = 1; i <= m; i++, x *= 10) {
        for (int j = 1; j <= n; j++)
            v[j] = a[j] / x % 10;
        countingsort();
    }
}