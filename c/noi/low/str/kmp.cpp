#include <cstring>

int n = 1000, m = 100;
char s[1002], p[102];
int nxt[101], f[1001];

void kmp() {
    n = strlen(s + 1); m = strlen(p + 1);
    int j = 0;
    nxt[1] = 0;
    for (int i = 2; i <= m; i++) {
        while (j > 0 && p[j + 1] != p[i])
            j = nxt[j];
        if (p[j + 1] == p[i])
            j++;
        nxt[i] = j;
    }
    j = 0;
    for (int i = 1; i <= n; i++) {
        while ((j == m) || (j > 0 && p[j + 1] != s[i]))
            j = nxt[j];
        if (p[j + 1] == s[i])
            j++;
        f[i] = j;
    }
}
