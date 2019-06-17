package stack;

/**
 * Created by outrun on 2/22/16.
 */
class Node<T> {
    T value;
    Node<T> next;

    public Node(T value, Node<T> next) {
        super();
        this.value = value;
        this.next = next;
    }
}

public class NormalQueue<T> {
    private int size;

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        } else {
            return false;
        }
    }

    public int size() {
        return size;
    }

    private Node<T> head;
    private Node<T> last;

    public NormalQueue() {
        super();
        head = new Node<T>(null, null);
        last = head;
    }

    public void offer(T t) {
        Node<T> node = new Node<T>(t, null);
        last.next = node;
        last = node;
        size++;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        } else {
            return head.next.value;
        }
    }
    public T poll() {
        if (isEmpty()) {
            return null;
        } else {
            Node<T> p = head.next;
            head.next = p.next;
            size--;
            if (size == 0) {
                last = head;
            }
            return p.value;
        }
    }
}
