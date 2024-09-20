package companystructure;

import java.util.ArrayList;
import java.util.List;

class Employee {
    String id;
    String name;
    String managerId;
    double salary;
    List<Employee> subordinates = new ArrayList<>();
    int level = 0;

    Employee(String id, String name, String managerId, double salary) {
        this.id = id;
        this.name = name;
        this.managerId = managerId;
        this.salary = salary;
    }
}
