int a[100], c[100];

void mergesort(int l, int r) {
    if (l == r) {
        return;
    }
    int m = (l + r) / 2;
    mergesort(l, m);
    mergesort(m + 1, r);
    int p1 = l, p2 = m + 1, tot = 0;
    while (p1 <= m && p2 <= r) {
        if (a[p1] <= a[p2])
            c[++tot] = a[p1++];
        else
            c[++tot] = a[p2++];
        while (p1 <= m)
            c[++tot] = a[p1++];
        while (p2 <= r)
            c[++tot] = a[p2++];
        for (int i = 1; i <= tot; i++)
            a[i + l - 1] = c[i];
    }
}
