package dp.p_lijn.les3.p4;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface ReizigerDAO {

    boolean save(Reiziger reiziger) throws SQLException;

    boolean update(Reiziger reiziger) throws SQLException, ParseException;

    boolean delete(Reiziger reiziger);

    Reiziger findById(int id) throws SQLException, ParseException;

    List<Reiziger> findByGbdatum(String datum) throws SQLException, ParseException;

    List<Reiziger> findAll();


}
