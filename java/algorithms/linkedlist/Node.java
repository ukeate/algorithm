package linkedlist;

public class Node<T> {

	public T value;
	public Node<T> next;
	public Node<T> pre;
	public Node() {
		super();
	}
	public Node(T value) {
		super();
		this.value = value;
	}
	public Node(T value, Node<T> next) {
		super();
		this.value = value;
		this.next = next;
	}
	public Node(T value, Node<T> next, Node<T> pre) {
		super();
		this.value = value;
		this.next = next;
		this.pre = pre;
	}
}
