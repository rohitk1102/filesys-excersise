package companystructure;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CompanyStructureAnalyzer {
    private EmployeeRepository employeeRepository;
    private List<AnalysisStrategy> analysisStrategies;
    private List<AnalysisObserver> observers;

    public CompanyStructureAnalyzer() {
        this.employeeRepository = new EmployeeRepository();
        this.analysisStrategies = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public void loadEmployees(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Employee emp = new Employee(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
                employeeRepository.addEmployee(emp);
            }
        }
        employeeRepository.buildHierarchy();
    }

    public void addAnalysisStrategy(AnalysisStrategy strategy) {
        analysisStrategies.add(strategy);
    }

    public void addObserver(AnalysisObserver observer) {
        observers.add(observer);
    }

    public void analyzeStructure() {
        for (AnalysisStrategy strategy : analysisStrategies) {
            List<AnalysisResult> results = strategy.analyze(employeeRepository);
            notifyObservers(results);
        }
    }

    private void notifyObservers(List<AnalysisResult> results) {
        for (AnalysisObserver observer : observers) {
            observer.update(results);
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java CompanyStructureAnalyzer <filename>");
            return;
        }

        CompanyStructureAnalyzer analyzer = new CompanyStructureAnalyzer();
        analyzer.addAnalysisStrategy(new SalaryAnalysisStrategy());
        analyzer.addAnalysisStrategy(new ReportingLineAnalysisStrategy());
        analyzer.addObserver(new ConsoleOutputObserver());

        try {
            analyzer.loadEmployees(args[0]);
            analyzer.analyzeStructure();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}

