package reports;

import java.util.List;

public interface CustomerReport {
    public List<Object[]> generateCustomerReport(String companyName);
}
