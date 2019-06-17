package chapter12;

import chapter10.Random;

import java.nio.BufferUnderflowException;

/**
 * Created by outrun on 6/12/16.
 */
public class Treap<AnyType extends Comparable<? super AnyType>> {

    public Treap() {
        nullNode = new TreapNode<AnyType>(null);
        nullNode.left = nullNode.right = nullNode;
        nullNode.priority = Integer.MAX_VALUE;
        root = nullNode;
    }

    public void insert(AnyType x) {
        root = insert(x, root);
    }

    public void remove(AnyType x) {
        root = remove(x, root);
    }

    public AnyType findMin() {
        if (isEmpty()) throw new BufferUnderflowException();

        TreapNode<AnyType> ptr = root;
        while (ptr.left != nullNode) {
            ptr = ptr.left;
        }

        return ptr.element;
    }

    public AnyType findMax() {
        if (isEmpty()) throw new BufferUnderflowException();

        TreapNode<AnyType> ptr = root;

        while (ptr.right != nullNode) {
            ptr = ptr.right;
        }

        return ptr.element;
    }

    public boolean contains(AnyType x) {
        TreapNode<AnyType> current = root;
        nullNode.element = x;

        for (;;) {
            int compareResult = x.compareTo(current.element);

            if (compareResult < 0) {
                current = current.left;
            } else if (compareResult > 0) {
                current = current.right;
            } else {
                return current != nullNode;
            }
        }
    }

    public void makeEmpty() {
        root = nullNode;
    }

    public boolean isEmpty() {
        return root == nullNode;
    }

    public void printTree() {
        if (isEmpty()) {
            System.out.println("Empty tree");
        } else {
            printTree(root);
        }
    }

    private void printTree(TreapNode<AnyType> t) {
        if (t != t.left) {
            printTree(t.left);
            System.out.println(t.element.toString());
            printTree(t.right);
        }
    }

    private TreapNode<AnyType> insert(AnyType x, TreapNode<AnyType> t) {
        if (t == nullNode) return new TreapNode<AnyType>(x, nullNode, nullNode);

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0) {
            t.left = insert(x, t.left);
            if (t.left.priority < t.priority) {
                t = rotateWithLeftChild(t);
            }
        } else if (compareResult > 0) {
            t.right = insert(x, t.right);
            if (t.right.priority < t.priority) {
                t = rotateWithRightChild(t);
            }
        }

        return t;
    }

    private TreapNode<AnyType> remove(AnyType x, TreapNode<AnyType> t) {
        if (t != nullNode) {
            int compareResult = x.compareTo(t.element);

            if (compareResult < 0) {
                t.left = remove(x, t.left);
            } else if (compareResult > 0) {
                t.right = remove(x, t.right);
            } else {
                if (t.left.priority < t.right.priority) {
                    t = rotateWithLeftChild(t);
                } else {
                    t = rotateWithRightChild(t);
                }

                if (t != nullNode) {
                    t = remove(x, t);
                } else {
                    t.left = nullNode;
                }
            }
        }

        return t;
    }

    private TreapNode<AnyType> rotateWithLeftChild(TreapNode<AnyType> k2) {
        TreapNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        return k1;
    }

    private TreapNode<AnyType> rotateWithRightChild(TreapNode<AnyType> k1) {
        TreapNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        return k2;
    }

    private static class TreapNode<AnyType> {

        TreapNode(AnyType theElement) {
            this(theElement, null, null);
        }

        TreapNode(AnyType theElement, TreapNode<AnyType> lt, TreapNode<AnyType> rt) {
            element = theElement;
            left = lt;
            right = rt;
            priority = randomObj.randomInt();
        }

        AnyType element;
        TreapNode<AnyType> left;
        TreapNode<AnyType> right;
        int priority;

        private static Random randomObj = new Random();
    }

    private TreapNode<AnyType> root;
    private TreapNode<AnyType> nullNode;
}
