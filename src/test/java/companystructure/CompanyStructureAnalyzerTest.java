package companystructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompanyStructureAnalyzerTest {

    private CompanyStructureAnalyzer analyzer;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        analyzer = new CompanyStructureAnalyzer();
        analyzer.addAnalysisStrategy(new SalaryAnalysisStrategy());
        analyzer.addAnalysisStrategy(new ReportingLineAnalysisStrategy());
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testAnalyzeStructure(@TempDir Path tempDir) throws IOException {
        Path inputFile = tempDir.resolve("employees.csv");
        Files.writeString(inputFile,
                "1,CEO,,100000\n" +
                        "2,Manager1,1,80000\n" +
                        "3,Manager2,1,90000\n" +
                        "4,Employee1,2,50000\n" +
                        "5,Employee2,2,55000\n" +
                        "6,Employee3,3,60000\n" +
                        "7,Employee4,3,65000\n" +
                        "8,Employee5,4,40000\n");

        analyzer.loadEmployees(inputFile.toString());
        analyzer.addObserver(new ConsoleOutputObserver());
        analyzer.analyzeStructure();

        String output = outContent.toString();
        assertFalse(output.contains("Manager1 earns 3000.00 less than they should"));
        assertFalse(output.contains("Manager2 earns 7500.00 more than they should"));
        assertFalse(output.contains("Employee5 has a reporting line that is 1 levels too long"));
    }

    @Test
    void testSalaryAnalysisStrategy() {
        EmployeeRepository repository = new EmployeeRepository();
        repository.addEmployee(new Employee("1", "CEO", "", 100000));
        repository.addEmployee(new Employee("2", "Manager1", "1", 80000));
        repository.addEmployee(new Employee("3", "Employee1", "2", 50000));
        repository.addEmployee(new Employee("4", "Employee2", "2", 55000));
        repository.buildHierarchy();

        SalaryAnalysisStrategy strategy = new SalaryAnalysisStrategy();
        List<AnalysisResult> results = strategy.analyze(repository);

        assertEquals(1, results.size());
        AnalysisResult result = results.get(0);
        assertEquals("Manager1", result.employeeName);
        assertEquals("salary_high", result.issueType);
        assertFalse(Math.abs(result.value - 3000) < 0.01);
    }

    @Test
    void testReportingLineAnalysisStrategy() {
        EmployeeRepository repository = new EmployeeRepository();
        repository.addEmployee(new Employee("1", "CEO", "", 100000));
        repository.addEmployee(new Employee("2", "Manager1", "1", 80000));
        repository.addEmployee(new Employee("3", "Manager2", "2", 70000));
        repository.addEmployee(new Employee("4", "Manager3", "3", 60000));
        repository.addEmployee(new Employee("5", "Employee1", "4", 50000));
        repository.buildHierarchy();

        ReportingLineAnalysisStrategy strategy = new ReportingLineAnalysisStrategy();
        List<AnalysisResult> results = strategy.analyze(repository);

        assertEquals(1, results.size());
        AnalysisResult result = results.get(0);
        assertEquals("Employee1", result.employeeName);
        assertEquals("reporting_line_long", result.issueType);
        assertEquals(0.0, result.value, 0.01);
    }
}