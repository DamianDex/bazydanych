package service;

import reports.SalesReport;

import java.util.List;

public class SalesReportServiceImpl implements SalesReportService {

    private SalesReport salesReport;

    public void setSalesReport(SalesReport salesReport) {
        this.salesReport = salesReport;
    }

    public List<Object[]> generateReport() {
        return salesReport.generateReport();
    }
}
