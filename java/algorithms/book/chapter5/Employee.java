package chapter5;

/**
 * Created by outrun on 5/16/16.
 */
public class Employee {

    public boolean equals(Object rhs) {
        return rhs instanceof Employee && name.equals(((Employee)rhs).name);
    }

    public int hashCode () {
        return name.hashCode();
    }

    private String name;
    private double salary;
    private int seniority;
}
