package companystructure;

import java.util.List;

interface AnalysisObserver {
    void update(List<AnalysisResult> results);
}
