//
// Created by outrun on 7/21/16.
//

typedef struct tagSNode {
    int value;
    tagSNode* pNext;

    tagSNode(int v): value(v), pNext(NULL) {}
} SNode;



