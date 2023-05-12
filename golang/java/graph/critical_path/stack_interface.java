package graph.critical_path;


public interface stack_interface<T> {

    int get_length();


    boolean isEmpty();


    void push(T data);


    T pop();


    void clear();


    T getTop();


}
