//
// Created by outrun on 2/14/16.
//

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void reverse(char* s, int start, int end) {
    char t;
    for(;start < end; start++, end--) {
        t = s[start];
        s[start] = s[end];
        s[end] = t;
    }
}
/**
 * 时间复杂度n, 空间复杂度1
 *
 */
void reverseWords(char* s) {
    if(s == NULL || strlen(s) <= 1) {
        return;
    }
    int n = strlen(s);
    int i = 0;
    int j;
    while(i < n) {
        j = i;
        while(j < n) {
            if(s[j] == ' ') {
                break;
            } else {
                j++;
            }
        }
        reverse(s, i, j - 1);
        while(j < n && s[j] == ' ') {
            j++;
        }
        i = j;
    }
    reverse(s, 0, n - 1);
}
int main(int argc, char** argv) {
    char* s = (char*)malloc(50 * sizeof(char));
    gets(s);
    reverseWords(s);
    printf("%s\n", s);
    return 0;
}