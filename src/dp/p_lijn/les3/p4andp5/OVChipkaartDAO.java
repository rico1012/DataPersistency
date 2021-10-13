package dp.p_lijn.les3.p4andp5;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface OVChipkaartDAO {

    boolean save(OVChipkaart ovChipkaart) throws SQLException, ParseException;

    boolean update(OVChipkaart ovChipkaart) throws SQLException, ParseException;

    boolean delete(OVChipkaart ovChipkaart) throws SQLException;

    List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException, ParseException;

    OVChipkaart findById(int id) throws SQLException, ParseException;

    List<OVChipkaart> findAll();
}
