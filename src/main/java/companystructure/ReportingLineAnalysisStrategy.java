package companystructure;

import java.util.ArrayList;
import java.util.List;

class ReportingLineAnalysisStrategy implements AnalysisStrategy {
    @Override
    public List<AnalysisResult> analyze(EmployeeRepository repository) {
        List<AnalysisResult> results = new ArrayList<>();
        for (Employee emp : repository.getAllEmployees()) {
            if (emp.level >= 4) {
                results.add(new AnalysisResult(emp.name, "reporting_line_long", emp.level - 4));
            }
        }
        return results;
    }
}
