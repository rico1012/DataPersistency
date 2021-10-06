package dp.p_lijn.les3.p4andp5;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
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
            OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOsql(conn);
            for (OVChipkaart ovChipkaart: reiziger.getOvChipkaartList()){
                ovChipkaartDAO.save(ovChipkaart);
            }
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException, ParseException {
        try {
            String query;
            query = "UPDATE reiziger SET voorletters=? , tussenvoegsel=? ,achternaam=? ,geboortedatum=? WHERE reiziger_id=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, reiziger.getVoorletters());
            pst.setString(2, reiziger.getTussenvoegsel());
            pst.setString(3, reiziger.getAchternaam());
            pst.setDate(4, (Date) reiziger.getGeboortedatum());
            pst.setInt(5, reiziger.getId());
            pst.executeUpdate();
            OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOsql(conn);
            List<OVChipkaart> ovChipkaartList = new ArrayList<>((ovChipkaartDAO.findByReiziger(reiziger)));
            List<OVChipkaart> ovChipkaartList2 = new ArrayList<>(reiziger.getOvChipkaartList());
            ovChipkaartList.remove(ovChipkaartList2);
            for (OVChipkaart ovChipkaart : ovChipkaartList){
                ovChipkaartDAO.delete(ovChipkaart);
            }
            List<OVChipkaart> ovChipkaartList3 = new ArrayList<>((ovChipkaartDAO.findByReiziger(reiziger)));
            ovChipkaartList2.remove(ovChipkaartList3);
            for (OVChipkaart ovChipkaart : ovChipkaartList2){
                ovChipkaartDAO.save(ovChipkaart);
            }
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();

            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        try{
            OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOsql(conn);
            for (OVChipkaart ovChipkaart: reiziger.getOvChipkaartList()){
                ovChipkaartDAO.delete(ovChipkaart);
            }
            String query;
            query = "DELETE FROM reiziger WHERE reiziger_id=? AND voorletters=? AND tussenvoegsel=? AND achternaam=? AND geboortedatum=? ";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, reiziger.getId());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, (Date) reiziger.getGeboortedatum());
            ResultSet rs = pst.executeQuery();
            rs.close();
            AdresDAO adresDAO = new AdresDAOsql(conn);
            adresDAO.delete(reiziger.getAdres());
            return true;
        }catch (SQLException e){

            return false;
        }
    }

    @Override
    public Reiziger findById(int id) throws SQLException, ParseException {
        String query = "SELECT voorletters AS vl, achternaam AS an, tussenvoegsel AS tv, geboortedatum AS gd, reiziger_id AS rid FROM reiziger WHERE reiziger_id=?";
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery(query);
        pst.setInt(1, id);
        String rid;
        String vl;
        String an;
        String tv;
        String gd;
        while (rs.next()){
            rid = rs.getString("rid");
            vl = rs.getString("vl");
            an = rs.getString("an");
            tv = rs.getString("tv");
            gd = rs.getString("gd");
            java.util.Date date1= new SimpleDateFormat("yyyy-MM-dd").parse(gd);
            Reiziger r = new Reiziger(vl,tv,an, date1);
            r.setId(Integer.parseInt(rid));
            for (OVChipkaart ovChipkaart : (new OVChipkaartDAOsql(conn).findAll())){
                if (ovChipkaart.getReiziger().equals(r)){
                    r.getOvChipkaartList().add(ovChipkaart);
                }
            }
            rs.close();
            pst.close();
            return r;}
        rs.close();
        pst.close();
        return null;

    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) throws SQLException, ParseException {
        String query = "SELECT voorletters AS vl, achternaam AS an, tussenvoegsel AS tv, geboortedatum AS gd, reiziger_id AS rid FROM reiziger WHERE geboortedatum=?";
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery(query);
        pst.setString(1, datum);
        String rid;
        String vl;
        String an;
        String tv;
        String gd;
        List<Reiziger> reizigers = new ArrayList<>();
        while (rs.next()){
            rid = rs.getString("rid");
            vl = rs.getString("vl");
            an = rs.getString("an");
            tv = rs.getString("tv");
            gd = rs.getString("gd");
            java.util.Date date1= new SimpleDateFormat("yyyy-MMdd").parse(gd);
            Reiziger r = new Reiziger(vl,tv,an, date1);
            r.setId(Integer.parseInt(rid));
            reizigers.add(r);
        }
        for (OVChipkaart ovChipkaart : (new OVChipkaartDAOsql(conn).findAll())){
            for (Reiziger reiziger : reizigers){
                if (ovChipkaart.getReiziger().equals(reiziger)){
                    reiziger.getOvChipkaartList().add(ovChipkaart);
                }
            }
        }

        rs.close();
        pst.close();
        return reizigers;
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
                Reiziger r = new Reiziger(vl,tv,an, date1);
                r.setId(Integer.parseInt(rid));
                for (Adres adres : new AdresDAOsql(conn).findAll()){
                    if (adres.getReiziger_id()==Integer.parseInt(rid)){
                        r.setAdres(adres);
                    }
                }
                for (OVChipkaart ovChipkaart : (new OVChipkaartDAOsql(conn).findByReiziger(r))){
                    r.getOvChipkaartList().add(ovChipkaart);
                }
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
