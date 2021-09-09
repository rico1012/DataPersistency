package dp.les1.p2;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO{
    private Connection conn;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        try{
            String query;
            query = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) " +
                    "VALUES (?,?,?,?,?)";
            System.out.println("");
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, reiziger.getId());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, (Date) reiziger.getGeboortedatum());
            ResultSet rs = pst.executeQuery();
            rs.close();
            return true;
        }catch (SQLException e){
            return false;
        }

    }

    @Override
    public boolean update(Reiziger reiziger) {
        return false;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        return false;
    }

    @Override
    public Reiziger findById(int id) {
        return null;
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        return null;
    }

    @Override
    public List<Reiziger> findAll() {
        try{
            Statement sta = conn.createStatement();
            String query = "SELECT voorletters AS vl, achternaam AS an, tussenvoegsel AS tv, geboortedatum AS gd, reiziger_id AS rid FROM reiziger";

            ResultSet rs = sta.executeQuery(query);

            String rid;
            String vl;
            String an;
            String tv;
            String gd;
            List<Reiziger> reizigerList = new ArrayList<>();
            while (rs.next()){
                rid = rs.getString("rid");
                vl = rs.getString("vl");
                an = rs.getString("an");
                tv = rs.getString("tv");
                gd = rs.getString("gd");
                java.util.Date date1= new SimpleDateFormat("yyyy-MMdd").parse(gd);
                Reiziger r = new Reiziger(Integer.parseInt(rid),vl,tv,an, date1);
                reizigerList.add(r);
            }
            rs.close();
            sta.close();

            return reizigerList;
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
