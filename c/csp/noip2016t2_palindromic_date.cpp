/*
 * 20110101
 * 20111231
 */
#include <bits/stdc++.h>

using namespace std;

int d1, d2, ans;

int getday(int y, int m) {
    if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) {
        return 31;
    }
    if (m == 4 || m == 6 || m == 9 || m == 11) {
        return 30;
    }
    if ((y % 4 == 0 && y % 100 != 0) || y % 400 == 0) {
        return 29;
    } else {
        return 28;
    }
}

int nextday(int x) {
    int y = x / 10000;
    int m = x / 100 % 100;
    int d = x % 100;
    if (d != getday(y, m)) {
        d += 1;
    } else if (m != 12) {
        m += 1; d = 1;
    } else {
        y += 1; m = 1; d = 1;
    }
    return 10000 * y + 100 * m + d;
}

int rev(int x) {
    int ans = 0;
    while (x) {
        ans = ans * 10 + (x % 10);
        x /= 10;
    }
    return ans;
}

//int main() {
//    scanf("%d%d", &d1, &d2);
//    for (int d = d1; d <= d2; d = nextday(d)) {
//        if (d = rev(d)) ans++;
//    }
//    printf("%d\n", ans);
//}

int main() {
    scanf("%d%d", &d1, &d2);
    for (int y = 1000; y <= 9999; y++) {
        int revy = rev(y);
        int m = revy / 100;
        int d = revy % 100;
        if (m >= 1 && m <= 12 && d >= 1 && d <= getday(y, m)) {
            int x = y * 10000 + revy;
            if (x >= d1 && x <= d2)
                ans++;
        }
    }
    printf("%d\n", ans);
}
