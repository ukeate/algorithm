#include <algorithm>
int a[100];

void quicksort(int l, int r) {
    if (l >= r) {
        return;
    }
    int x = a[l];
    int i = l, j = r;
    swap(a[l], a[l + rand() % (r - l + 1)]);
    while (i < j) {
        while (i < j && a[j] > x)
            j--;
        if (i < j)
            a[i++] = a[j];
        while (i < j && a[i] < x)
            i++;
        if (i < j)
            a[j--] = a[i];
    }
    a[i] = x;
    quicksort(l, i - 1);
    quicksort(i + 1, r);
}
