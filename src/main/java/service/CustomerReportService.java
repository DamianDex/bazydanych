package service;

import java.util.List;

public interface CustomerReportService {
    public List<Object[]> generateCustomerReport(String companyName);
}
