package companystructure;

class AnalysisResult {
    String employeeName;
    String issueType;
    double value;

    AnalysisResult(String employeeName, String issueType, double value) {
        this.employeeName = employeeName;
        this.issueType = issueType;
        this.value = value;
    }
}
