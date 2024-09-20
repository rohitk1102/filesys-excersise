package companystructure;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class EmployeeRepository {
    private Map<String, Employee> employees = new HashMap<>();
    private Employee ceo;

    public void addEmployee(Employee emp) {
        employees.put(emp.id, emp);
    }

    public void buildHierarchy() {
        for (Employee emp : employees.values()) {
            if (emp.managerId.isEmpty()) {
                ceo = emp;
            } else {
                Employee manager = employees.get(emp.managerId);
                manager.subordinates.add(emp);
            }
        }
        setLevels(ceo, 0);
    }

    private void setLevels(Employee emp, int level) {
        emp.level = level;
        for (Employee subordinate : emp.subordinates) {
            setLevels(subordinate, level + 1);
        }
    }

    public Employee getCEO() {
        return ceo;
    }

    public Collection<Employee> getAllEmployees() {
        return employees.values();
    }
}
