package chapter12;

import jdk.internal.util.xml.impl.Pair;

import java.nio.BufferUnderflowException;

/**
 * Created by outrun on 6/12/16.
 */
public class PairingHeap<AnyType extends Comparable<? super AnyType>> {

    public interface Position<AnyType> {
        AnyType getValue();
    }

    public PairingHeap() {
        root = null;
        theSize = 0;
    }

    public Position<AnyType> insert(AnyType x) {
        PairNode<AnyType> newNode = new PairNode<AnyType>(x);

        if (root == null) {
            root = newNode;
        } else {
            root = compareAndLink(root, newNode);
        }

        theSize++;
        return newNode;
    }

    public AnyType findMin() {
        if (isEmpty()) throw new BufferUnderflowException();

        return root.element;
    }

    public AnyType deleteMin() {
        if (isEmpty()) throw new BufferUnderflowException();

        AnyType x = findMin();
        root.element = null;
        if (root.leftChild == null) {
            root = null;
        } else {
            root = combineSiblings(root.leftChild);
        }

        theSize--;
        return x;
    }

    public void decreaseKey(Position<AnyType> pos, AnyType newVal) {
        if (pos == null) throw new IllegalArgumentException("null Position passed to decreaseKey");

        PairNode<AnyType> p = (PairNode<AnyType>) pos;

        if (p.element == null) throw new IllegalArgumentException("pos already deleted");
        if (p.element.compareTo(newVal) < 0)
            throw new IllegalArgumentException("newVal/oldVal: " + newVal + "/" + p.element);

        p.element = newVal;

        if (p != root) {
            if (p.nextSibling != null) {
                p.nextSibling.prev = p.prev;
            }

            if (p.prev.leftChild == p) {
                p.prev.leftChild = p.nextSibling;
            } else {
                p.prev.nextSibling = p.nextSibling;
            }

            p.nextSibling = null;
            root = compareAndLink(root, p);
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return theSize;
    }

    public void makeEmpty() {
        root = null;
        theSize = 0;
    }

    private PairNode<AnyType> compareAndLink(PairNode<AnyType> first, PairNode<AnyType> second) {
        if (second == null) {
            return first;
        }

        if (second.element.compareTo(first.element) < 0) {
            second.prev = first.prev;
            first.prev = second;

            first.nextSibling = second.leftChild;
            if (first.nextSibling != null) {
                first.nextSibling.prev = first;
            }

            second.leftChild = first;
            return second;
        } else {
            second.prev = first;

            first.nextSibling = second.nextSibling;
            if (first.nextSibling != null) {
                first.nextSibling.prev = first;
            }

            second.nextSibling = first.leftChild;
            if (second.nextSibling != null) {
                second.nextSibling.prev = second;
            }

            first.leftChild = second;
            return first;
        }
    }

    private PairNode<AnyType>[] doubleIfFull(PairNode<AnyType>[] array, int index) {
        if (index == array.length) {
            PairNode<AnyType>[] oldArray = array;

            array = new PairNode[index * 2];
            for (int i = 0; i < index; i++) {
                array[i] = oldArray[i];
            }
        }
        return array;
    }

    private PairNode<AnyType>[] treeArray = new PairNode[5];

    private PairNode<AnyType> combineSiblings(PairNode<AnyType> firstSibling) {
        if (firstSibling.nextSibling == null) {
            return firstSibling;
        }

        int numSiblings = 0;
        for (; firstSibling != null; numSiblings++) {
            treeArray = doubleIfFull(treeArray, numSiblings);
            treeArray[numSiblings] = firstSibling;
            firstSibling.prev.nextSibling = null;
            firstSibling = firstSibling.nextSibling;
        }

        treeArray = doubleIfFull(treeArray, numSiblings);
        treeArray[numSiblings] = null;

        int i = 0;
        for (; i + 1 < numSiblings; i += 2) {
            treeArray[i] = compareAndLink(treeArray[i], treeArray[i + 1]);
        }

        int j = i - 2;
        if (j == numSiblings - 3) {
            treeArray[j] = compareAndLink(treeArray[j], treeArray[j + 2]);
        }

        for (; j >= 2; j -= 2) {
            treeArray[j - 2] = compareAndLink(treeArray[j - 2], treeArray[j]);
        }

        return treeArray[0];
    }

    private static class PairNode<AnyType> implements Position<AnyType> {

        public PairNode(AnyType theElement) {
            element = theElement;
            leftChild = null;
            nextSibling = null;
            prev = null;
        }

        public AnyType getValue() {
            return element;
        }

        public AnyType element;
        public PairNode<AnyType> leftChild;
        public PairNode<AnyType> nextSibling;
        public PairNode<AnyType> prev;
    }

    private PairNode<AnyType> root;
    private int theSize;

}
