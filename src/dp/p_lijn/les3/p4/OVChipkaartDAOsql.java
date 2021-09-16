package dp.p_lijn.les3.p4;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OVChipkaartDAOsql implements OVChipkaartDAO{
    private Connection connection;

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        try{
            String query;
            query = "INSERT INTO ov_chipkaart (kaart_nummer::integer , geldig_toch::date , klasse::integer , saldo::numeric , reiziger_id::integer ) " +
                    "VALUES (?,?,?,?,?)";
            System.out.println("");
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, ovChipkaart.getKaartNummer());
            pst.setDate(2, (java.sql.Date) ovChipkaart.getGeldigTot());
            pst.setInt(3, ovChipkaart.getKlasse());
            pst.setInt(4, ovChipkaart.getSaldo());
            pst.setInt(5, ovChipkaart.getReiziger().getId());
            ResultSet rs = pst.executeQuery();
            rs.close();
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        try {
            String query;
            query = "UPDATE ov_chipkaart SET reiziger_id=? , geldig_tot=?, klasse=?, saldo=? WHERE kaart_nummer=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(5, ovChipkaart.getKaartNummer());
            pst.setDate(2, (java.sql.Date) ovChipkaart.getGeldigTot());
            pst.setInt(3, ovChipkaart.getKlasse());
            pst.setInt(4, ovChipkaart.getSaldo());
            pst.setInt(1, ovChipkaart.getReiziger().getId());
            pst.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        try{
            String query;
            query = "DELETE FROM ov_chipkaart WHERE kaart_nummer=? AND geldig_tot=? AND klasse=? AND saldo=? AND reiziger_id=?) ";
            System.out.println("");
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, ovChipkaart.getKaartNummer());
            pst.setDate(2, (java.sql.Date) ovChipkaart.getGeldigTot());
            pst.setInt(3, ovChipkaart.getKlasse());
            pst.setInt(4, ovChipkaart.getSaldo());
            pst.setInt(5, ovChipkaart.getReiziger().getId());
            ResultSet rs = pst.executeQuery();
            rs.close();
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    @Override
    public OVChipkaart findByReiziger(Reiziger reiziger) throws SQLException, ParseException {
        String query = "SELECT kaart_nummer AS kaartNummer, geldig_tot AS geldigTot, klasse AS klasse, saldo AS saldo FROM ov_chipkaart WHERE reiziger_id=?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, reiziger.getId());
        ResultSet rs = pst.executeQuery();
        String kaartNummer;
        String geldigTot;
        String klasse;
        String saldo;
        if (rs.next()) {
            kaartNummer = rs.getString("kaartNummer");
            geldigTot = rs.getString("geldigTot");
            klasse = rs.getString("klasse");
            saldo = rs.getString("saldo");
            rs.close();
            Date date = new SimpleDateFormat("MM-dd-yyyy").parse(geldigTot);
            return new OVChipkaart(Integer.parseInt(kaartNummer), date,Integer.parseInt(klasse), Integer.parseInt(saldo), reiziger);
        } else {
            rs.close();
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findAll() {
        try{
            Statement sta = connection.createStatement();
            String query = "SELECT kaart_nummer AS kaartNummer, geldig_tot AS geldigTot, klasse AS klasse, saldo AS saldo, reiziger_id AS reizigerId FROM ov_chipkaart";

            ResultSet rs = sta.executeQuery(query);

            String kaartNummer;
            String geldigTot;
            String klasse;
            String saldo;
            String reizigerId;
            List<OVChipkaart> ovChipkaartList= new ArrayList<>();
            if (rs.next()) {
                kaartNummer = rs.getString("kaartNummer");
                geldigTot = rs.getString("geldigTot");
                klasse = rs.getString("klasse");
                saldo = rs.getString("saldo");
                reizigerId = rs.getString("reizigerId");
                rs.close();
                Date date = new SimpleDateFormat("MM-dd-yyyy").parse(geldigTot);
                Reiziger reiziger = null;
                for (Reiziger reiziger1 : (new ReizigerDAOPsql(connection)).findAll()){
                    if (reiziger1.getId()==Integer.parseInt(reizigerId)){
                        reiziger=reiziger1;
                    }
                }
                OVChipkaart ovChipkaart = new OVChipkaart(Integer.parseInt(kaartNummer), date,Integer.parseInt(klasse), Integer.parseInt(saldo), reiziger);
                ovChipkaartList.add(ovChipkaart);
            }
            rs.close();
            sta.close();

            return ovChipkaartList;
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
