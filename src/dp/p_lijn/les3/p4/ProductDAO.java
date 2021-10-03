package dp.p_lijn.les3.p4;

import java.sql.SQLException;

public interface ProductDAO {
    boolean productSave(Product product) throws SQLException;

    boolean productUpdate(Product product) throws SQLException;

    boolean productDelete(Product product) throws SQLException;
}
