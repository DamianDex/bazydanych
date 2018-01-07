package service;

import reports.CustomerReport;
import reports.SalesReport;

import java.util.List;

public class CustomerReportServiceImpl implements CustomerReportService {

    CustomerReport customerReport;

    public void setCustomerReport(CustomerReport customerReport) {
        this.customerReport = customerReport;
    }

    @Override
    public List<Object[]> generateCustomerReport(String companyName) {
        return customerReport.generateCustomerReport(companyName);
    }
}
