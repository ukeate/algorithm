package chapter12;

import chapter6.BinaryHeap;
import org.omg.CORBA.Any;

import java.nio.BufferUnderflowException;

/**
 * Created by outrun on 6/9/16.
 */
public class SplayTree<AnyType extends Comparable<? super AnyType>> {

    public SplayTree() {
        nullNode = new BinaryNode<AnyType> (null);
        nullNode.left = nullNode.right = nullNode;
        root = nullNode;
    }

    private BinaryNode<AnyType> newNode = null;

    public void insert(AnyType x) {
        if (newNode == null) {
            newNode = new BinaryNode<AnyType>(null);
        }

        newNode.element = x;

        if (root == nullNode) {
            newNode.left = newNode.right = nullNode;
            root = newNode;
        } else {
            root = splay(x, root);

            int compareResult = x.compareTo(root.element);

            if (compareResult < 0) {
                newNode.left = root.left;
                newNode.right = root;
                root.left = nullNode;
                root = newNode;
            } else if (compareResult > 0){
                newNode.right = root.right;
                newNode.left = root;
                root.right = nullNode;
                root = newNode;
            } else {
                return;
            }
        }
        newNode = null;
    }

    public void remove(AnyType x) {
        if (!contains(x)) return;

        BinaryNode<AnyType> newTree;

        if (root.left == nullNode) {
            newTree = root.right;
        } else {
            newTree = root.left;
            newTree = splay(x, newTree);
            newTree.right = root.right;
        }
        root = newTree;
    }

    public AnyType findMin() {
        if (isEmpty()) throw new BufferUnderflowException();

        BinaryNode<AnyType> ptr = root;

        while(ptr.left != nullNode) {
            ptr = ptr.left;
        }

        root = splay(ptr.element, root);
        return ptr.element;
    }

    public AnyType findMax() {
        if(isEmpty()) throw new BufferUnderflowException();

        BinaryNode<AnyType> ptr = root;

        while(ptr.right != nullNode) {
            ptr = ptr.right;
        }

        root = splay(ptr.element, root);
        return ptr.element;
    }

    public boolean contains (AnyType x) {
        if(isEmpty()) return false;

        root = splay(x, root);

        return root.element.compareTo(x) == 0;
    }

    public void makeEmpty() {
        root = nullNode;
    }

    public boolean isEmpty() {
        return root == nullNode;
    }

    private  BinaryNode<AnyType> header = new BinaryNode<AnyType>(null);
    private BinaryNode<AnyType> splay(AnyType x, BinaryNode<AnyType> t) {
        BinaryNode<AnyType> leftTreeMax, rightTreeMin;
        header.left = header.right = nullNode;
        leftTreeMax = rightTreeMin = header;

        nullNode.element = x;

        for (;;) {
            int compareResult = x.compareTo(t.element);
            if (compareResult < 0) {
                if (x.compareTo(t.left.element) < 0) {
                    t = rotateWithLeftChild(t);
                }
                if (t.left == nullNode) {
                    break;
                }
                rightTreeMin.left = t;
                rightTreeMin = t;
                t = t.left;

            } else if (compareResult > 0) {
                if (x.compareTo(t.right.element) > 0) {
                    t = rotateWithRightChild(t);
                }
                if (t.right == nullNode) {
                    break;
                }

                leftTreeMax.right = t;
                leftTreeMax = t;
                t = t.right;
            } else {
                break;
            }
        }

        leftTreeMax.right = t.left;
        rightTreeMin.left = t.right;
        t.left = header.right;
        t.right = header.left;
        return t;
    }

    private static <AnyType> BinaryNode<AnyType> rotateWithLeftChild(BinaryNode<AnyType> k2) {
        BinaryNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        return  k1;
    }

    private static <AnyType> BinaryNode<AnyType> rotateWithRightChild(BinaryNode<AnyType> k1) {
        BinaryNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        return k2;
    }

    private static class BinaryNode<AnyType> {
        BinaryNode(AnyType theElement) {
            this(theElement, null, null);
        }

        BinaryNode(AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt) {
            element = theElement;
            left = lt;
            right = rt;
        }

        AnyType element;
        BinaryNode<AnyType> left;
        BinaryNode<AnyType> right;
    }

    private BinaryNode<AnyType> root;
    private BinaryNode<AnyType> nullNode;
}
