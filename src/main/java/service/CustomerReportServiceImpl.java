package service;

import reports.SalesReport;

import java.util.List;

public class CustomerReportServiceImpl implements SalesReportService {

    SalesReport salesReport;

    public void setSalesReport(SalesReport salesReport) {
        this.salesReport = salesReport;
    }

    @Override
    public List<Object[]> generateReport() {
        return salesReport.generateReport();
    }
}
