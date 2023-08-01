#include <bits/stdc++.h>
using namespace std;

int a, b, c;

int main() {
    scanf("%d%d%d", &a, &b, &c);
//    int d = 0.2 * a + 0.3 * b + 0.5 * c;
    int d = a / 10 * 2 + b / 10 * 3 + c / 10 * 5;
    printf("%d\n", d);
}
