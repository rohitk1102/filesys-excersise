package companystructure;

import java.util.ArrayList;
import java.util.List;

class SalaryAnalysisStrategy implements AnalysisStrategy {
    @Override
    public List<AnalysisResult> analyze(EmployeeRepository repository) {
        List<AnalysisResult> results = new ArrayList<>();
        for (Employee manager : repository.getAllEmployees()) {
            if (!manager.subordinates.isEmpty()) {
                double avgSubordinateSalary = manager.subordinates.stream()
                        .mapToDouble(e -> e.salary)
                        .average()
                        .orElse(0);

                double minSalary = avgSubordinateSalary * 1.2;
                double maxSalary = avgSubordinateSalary * 1.5;

                if (manager.salary < minSalary) {
                    results.add(new AnalysisResult(manager.name, "salary_low", minSalary - manager.salary));
                } else if (manager.salary > maxSalary) {
                    results.add(new AnalysisResult(manager.name, "salary_high", manager.salary - maxSalary));
                }
            }
        }
        return results;
    }
}