package dp.les1.p3;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface ReizigerDAO {

    boolean save(Reiziger reiziger) throws SQLException;

    boolean update(Reiziger reiziger);

    boolean delete(Reiziger reiziger);

    Reiziger findById(int id) throws SQLException, ParseException;

    List<Reiziger> findByGbdatum(String datum) throws SQLException, ParseException;

    List<Reiziger> findAll();


}
