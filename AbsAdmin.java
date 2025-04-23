package FlowerShop.View;

import FlowerShop.Model.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface  AbsAdmin {

    List<ModelStaff> getAllStaff();

    boolean addStaff(ModelUser staff);

    boolean updateStaff(ModelUser staff);

    boolean addStaff(ModelStaff staff);

    boolean updateStaff(ModelStaff staff);

    // ==================== HÓA ĐƠN ====================
    List<ModelOrders> getInvoices(String period, Date date);

    // ==================== DOANH THU ====================
    Map<String, Double> getRevenueReport(String period, Date date);

    // ==================== SẢN PHẨM ====================
    List<ModelProduct> getAllProducts() throws SQLException;

    int generateNewProductId() throws SQLException;

    boolean addProduct(ModelProduct product) throws SQLException;

    boolean updateProduct(ModelProduct product) throws SQLException;

    // ==================== THỐNG KÊ ====================
    List<ModelProductSales> getProductStatistics();

    void bubbleSort(List<ModelProductSales> list);

    List<ModelCustomers> getAllCustomers() throws SQLException;
}

