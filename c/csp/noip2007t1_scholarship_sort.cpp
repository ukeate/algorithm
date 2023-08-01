/*
 * 8
 * 80 89 89
 * 88 98 78
 * 90 67 80
 * 87 66 91
 * 78 89 91
 * 88 99 77
 * 67 89 64
 * 78 89 98
 */
#include <bits/stdc++.h>
using namespace std;

int n;
struct student {
    int yw, sx, yy;
    int tot, id;
}stu[310];

bool cmp(student a, student b) {
    if (a.tot != b.tot) {
        return a.tot > b.tot;
    } else if (a.yw != b.yw) {
        return a.yw > b.yw;
    } else {
        return a.id < b.id;
    }
}

int main() {
    scanf("%d", &n);
    for (int i = 1; i <= n; i++) {
        scanf("%d%d%d", &stu[i].yw, &stu[i].sx, &stu[i].yy);
        stu[i].tot = stu[i].yw + stu[i].sx + stu[i].yy;
        stu[i].id = i;
    }
    for (int i = 1; i <= n; i++) {
        for (int j = i + 1; j <= n; j++) {
            if (cmp(stu[j], stu[i]))
                swap(stu[j], stu[i]);
        }
    }
    for (int i = 1; i <= 5; i++) {
        printf("%d %d\n", stu[i].id, stu[i].tot);
    }
}