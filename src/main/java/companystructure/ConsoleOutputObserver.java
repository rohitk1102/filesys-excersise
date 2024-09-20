package companystructure;

import java.util.List;

class ConsoleOutputObserver implements AnalysisObserver {
    @Override
    public void update(List<AnalysisResult> results) {
        for (AnalysisResult result : results) {
            switch (result.issueType) {
                case "salary_low":
                    System.out.printf("%s earns %.2f less than they should%n", result.employeeName, result.value);
                    break;
                case "salary_high":
                    System.out.printf("%s earns %.2f more than they should%n", result.employeeName, result.value);
                    break;
                case "reporting_line_long":
                    System.out.printf("%s has a reporting line that is %.0f levels too long%n", result.employeeName, result.value);
                    break;
            }
        }
    }
}
