package dp.p_lijn.les3.p4andp5;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    boolean productSave(Product product) throws SQLException;

    boolean productUpdate(Product product) throws SQLException;

    boolean productDelete(Product product) throws SQLException;

    List<Product> findAll() throws SQLException;
}
