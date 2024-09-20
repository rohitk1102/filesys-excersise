package companystructure;

import java.util.List;

interface AnalysisStrategy {
    List<AnalysisResult> analyze(EmployeeRepository repository);
}
