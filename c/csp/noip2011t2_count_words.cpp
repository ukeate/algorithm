/*
 * to
 * To be or not to be
 */

#include <bits/stdc++.h>
using namespace std;

char s[20], t[1010000], ch;
int n, m, cnt, pos;

int main() {
    scanf("%s", s);
    n = strlen(s);
    scanf("%c", &ch);
    while (true) {
        scanf("%c", &ch);
        if (ch == '\n') break;
        t[m] = ch;
        m++;
    }
    for (int i = 0; i < n; i++) {
        if (s[i] >= 'A' && s[i] <= 'Z') {
            s[i] = s[i] - 'A' + 'a';
        }
    }
    for (int i = 0; i < m; i++) {
        if (t[i] >= 'A' && t[i] <= 'Z') {
            t[i] = t[i] - 'A' + 'a';
        }
    }
    pos = -1;
    for (int i = 0; i < m; i++) {
        if (i + n - 1 >= m) break;
        bool match = true;
        for (int j = 0; j < n; j++) {
            if (t[i + j] != s[j]) {
                match = false;
                break;
            }
        }
        if (i != 0 && t[i - 1] != ' ') match = false;
        if (i + n < m && t[i + n] != ' ') match = false;
        if (match) {
            cnt += 1;
            if (pos == -1) {
                pos = i;
            }
        }
    }
    if (cnt == 0) {
        printf("-1\n");
    } else {
        printf("%d %d\n", cnt, pos);
    }
}