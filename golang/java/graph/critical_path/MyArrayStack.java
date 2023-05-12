package graph.critical_path;


public class MyArrayStack<T> implements stack_interface<T> {
    private int length;
    private Object objs[];

    public MyArrayStack() {
        length = 0;
        objs = new Object[2];
    }

    @Override
    public int get_length() {

        return length;
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public void push(T data) {
        if (length >= objs.length) {
            resize();
        }
        objs[length] = data;
        length++;

    }

    @Override
    public T pop() {
        if (length == 0)
            return null;

        return (T) objs[--length];

    }


    private void resize() {
        Object temp[] = new Object[length * 2];
        for (int i = 0; i < length; i++) {
            temp[i] = objs[i];
            objs[i] = 0;
        }
        objs = temp;

    }

    @Override
    public void clear() {
        objs = new Object[16];
        length = 0;

    }

    @Override
    public T getTop() {
        if (length == 0)
            return null;
        else
            return (T) objs[length - 1];
    }

}
