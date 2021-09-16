package dp.p_lijn.les3.p4;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface OVChipkaartDAO {

    boolean save(OVChipkaart ovChipkaart);

    boolean update(OVChipkaart ovChipkaart);

    boolean delete(OVChipkaart ovChipkaart);

    OVChipkaart findByReiziger(Reiziger reiziger) throws SQLException, ParseException;

    List<OVChipkaart> findAll();
}
