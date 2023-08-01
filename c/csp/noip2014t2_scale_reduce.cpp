// 1498 902 10
#include <bits/stdc++.h>

using namespace std;

int A, B, L;

bool coprime(int a, int b) {
    for (int d = 2; d <= a; d++) {
        if (a % d == 0 && b % d == 0) {
            return false;
        }
    }
    return true;
}

int main() {
    scanf("%d%d%d", &A, &B, &L);
    int ansA = -1, ansB = -1;
    for (int a = 1; a <= L; a++) {
        for (int b = 1; b <= L; b++) {
            // 通分
            // 不互质的数不会更新答案
            if (/*coprime(a, b) && */a * B >= A * b) {
                // 去公共减数 A/B，后通分
                double tmp = 1.0 * a / b - 1.0 * A / B;
                if (a * ansB < ansA * b || ansA == -1) {
                    ansA = a;
                    ansB = b;
                }
            }
        }
    }
    printf("%d %d\n", ansA, ansB);
}
