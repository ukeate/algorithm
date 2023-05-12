package chapter4;

/**
 * Created by outrun on 5/15/16.
 */
public class AvlTree<AnyType> {

    private static class AvlNode<AnyType> {
        AvlNode(AnyType theElement) {
            this(theElement, null, null);
        }

        AvlNode(AnyType theElement, AvlNode<AnyType> lt, AvlNode<AnyType> rt) {
            element = theElement; left = lt; right = rt; height = 0;
        }

        AnyType element;
        AvlNode<AnyType> left;
        AvlNode<AnyType> right;
        int height;
    }

    private int height(AvlNode<AnyType> t) {
        return t == null ? -1 : t.height;
    }

    private int compare(AnyType x, AnyType y) {
        return 0;
    }

    private AvlNode<AnyType> insert(AnyType x, AvlNode<AnyType> t) {
        if (t == null) {
            return new AvlNode<AnyType>(x, null, null);
        }

        int compareResult = compare(x, t.element);

        if (compareResult < 0) {
            t.left = insert(x, t.left);

            if (height(t.left) - height(t.right) == 2) {
                if (compare(x, t.left.element) < 0) {
                    t = rotateWithLeftChild(t);
                } else {
                    t = doubleWithLeftChild(t);
                }
            }
        } else if (compareResult > 0) {
            t.right = insert(x, t.right);

            if (height(t.right) - height(t.left) == 2) {
                if (compare(x, t.right.element) > 0) {
//                    t = rotateWithRightChild(t);
                } else {
//                    t = doubleWithRightChild(t);
                }
            }
        } else {
        }

        t.height = Math.max(height(t.left), height(t.right)) + 1;
        return t;
    }

    private AvlNode<AnyType> rotateWithLeftChild(AvlNode<AnyType> k2) {
        AvlNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;

        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), k2.height) + 1;
        return k1;
    }

    private AvlNode<AnyType> doubleWithLeftChild(AvlNode<AnyType> k3) {
//        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }
}
