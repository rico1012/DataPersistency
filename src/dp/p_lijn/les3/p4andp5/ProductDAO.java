package dp.p_lijn.les3.p4andp5;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface ProductDAO {
    boolean save(Product product) throws SQLException, ParseException;

    boolean update(Product product) throws SQLException;

    boolean delete(Product product) throws SQLException;

    List<Product> findByOVchipkaart(OVChipkaart ovChipkaart) throws SQLException;

    List<Product> findAll() throws SQLException, ParseException;
}
