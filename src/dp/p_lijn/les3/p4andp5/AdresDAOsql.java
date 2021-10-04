package dp.p_lijn.les3.p4andp5;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOsql implements AdresDAO {
    private Connection conn;

    public AdresDAOsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Adres adres) {try{
        String query;
        query = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) " +
                "VALUES (?,?,?,?,?,?)";
        System.out.println("");
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, adres.getId());
        pst.setString(2, adres.getPostcode());
        pst.setString(3, adres.getHuisnummer());
        pst.setString(4, adres.getStraat());
        pst.setString(5, adres.getWoonplaats());
        pst.setInt(6, adres.getReiziger_id());
        ResultSet rs = pst.executeQuery();
        rs.close();
        return true;
    }catch (SQLException e){
        System.out.println(e);
        return false;
    }
    }

    @Override
    public boolean update(Adres adres) {
        try {
            String query;
            query = "UPDATE adres SET postcode=?, huisnummer=?, straat=?, woonplaats=?, reiziger_id=? WHERE adres_id=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, adres.getPostcode());
            pst.setString(2, adres.getHuisnummer());
            pst.setString(3, adres.getStraat());
            pst.setString(4, adres.getWoonplaats());
            pst.setInt(5, adres.getReiziger_id());
            pst.setInt(6, adres.getId());
            pst.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(throwables);

            return false;
        }
    }


    @Override
    public boolean delete(Adres adres) {
        try{
            String query;
            query = "DELETE FROM adres WHERE adres_id=?::integer AND postcode=? AND huisnummer=? AND straat=? AND woonplaats=? AND reiziger_id=?::integer";
            System.out.println("");
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, adres.getId());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReiziger_id());
            ResultSet rs = pst.executeQuery();
            rs.close();
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        String query = "SELECT adres_id AS aid, postcode AS pc, huisnummer AS hn, straat AS st, woonplaats AS wp FROM adres WHERE reiziger_id=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, reiziger.getId());
        ResultSet rs = pst.executeQuery();
        String aid;
        String pc;
        String hn;
        String st;
        String wp;
        if (rs.next()) {
            aid = rs.getString("aid");
            pc = rs.getString("pc");
            hn = rs.getString("hn");
            st = rs.getString("st");
            wp = rs.getString("wp");
            rs.close();
            return new Adres(Integer.parseInt(aid), pc, hn, st, wp, reiziger.getId());
        } else {
            rs.close();
            return null;
        }


    }

    @Override
    public List<Adres> findAll() {
        try {
            Statement sta = conn.createStatement();
            String query = "SELECT adres_id AS aid, postcode AS pc, huisnummer AS hn, straat AS st, woonplaats AS wp, reiziger_id AS rid FROM adres";
            ResultSet rs = sta.executeQuery(query);
            String aid;
            String pc;
            String hn;
            String st;
            String wp;
            String rid;
            List<Adres> adresList = new ArrayList<>();
            while (rs.next()) {
                aid = rs.getString("aid");
                pc = rs.getString("pc");
                hn = rs.getString("hn");
                st = rs.getString("st");
                wp = rs.getString("wp");
                rid = rs.getString("rid");
                Adres a = new Adres(Integer.parseInt(aid), pc, hn, st, wp, Integer.parseInt(rid));
                adresList.add(a);
            }
            rs.close();
            sta.close();
            return adresList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
