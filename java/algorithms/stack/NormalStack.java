package stack;

/**
 * Created by outrun on 2/22/16.
 */
public class NormalStack<T> {

    private int size;
    private Object[] arr = new Object[4];

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void expandCapacity() {
        Object[] newArr = new Object[size * 2];
        System.arraycopy(arr, 0, newArr, 0, size);
        arr = newArr;
    }

    public void push(T t) {
        arr[size] = t;
        size++;
        if (size == arr.length) {
            expandCapacity();
        }
    }

    public T pop() {
        if (isEmpty()) {
            return null;
        } else {
            T t = peek();
            arr[size - 1] = null;
            size--;
            return t;
        }
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        } else {
            return (T) arr[size - 1];
        }
    }

}
