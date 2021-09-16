package dp.p_lijn.les1.p2;

import java.sql.SQLException;
import java.util.List;

public interface ReizigerDAO {

    boolean save(Reiziger reiziger) throws SQLException;

    boolean update(Reiziger reiziger);

    boolean delete(Reiziger reiziger);

    Reiziger findById(int id);

    List<Reiziger> findByGbdatum(String datum);

    List<Reiziger> findAll();


}
