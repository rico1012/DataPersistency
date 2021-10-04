package dp.p_lijn.les3.p4;

import javax.sound.sampled.Port;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductDAOsql implements ProductDAO{

    private Connection connection;

    public ProductDAOsql(Connection connection) {
        this.connection = connection;
    }

    public boolean productSave(Product product) throws SQLException {
        try{
            String query;
            query = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) " +
                    "VALUES (?,?,?,?)";
            System.out.println("");
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, product.getProduct_nummer());
            pst.setString(2, product.getNaam());
            pst.setString(3, product.getBeschrijving());
            pst.setDouble(4, product.getPrijs());
            ResultSet rs = pst.executeQuery();
            rs.close();
            return true;
        }catch (SQLException e){
            OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOsql(connection);
            for (OVChipkaart ovChipkaart: product.getOvChipkaarten()){
                ovChipkaartDAO.save(ovChipkaart);
                String query = "INSERT INTO ov_chipkaart_product (kaart_nummer,product_nummer, status, last_update) " +
                        "VALUES (?,?,?,?)";
                PreparedStatement pst = connection.prepareStatement(query);
                pst.setInt(1, ovChipkaart.getKaartNummer());
                pst.setInt(2, product.getProduct_nummer());
                pst.setString(3, "placed");
                pst.setDate(4, (Date) Calendar.getInstance().getTime());
                ResultSet rs = pst.executeQuery();
                rs.close();
            }
            System.out.println(e);
            return false;
        }
    }


    public boolean productUpdate(Product product) throws SQLException {
        OVChipkaartDAO ovChipkaartDAO= new OVChipkaartDAOsql(connection);
        try{
            String query;
            query = "UPDATE product SET product_nummer=? , naam=?, beschrijving=?, prijs=? WHERE product_nummer=?";
            System.out.println("");
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, product.getProduct_nummer());
            pst.setString(2, product.getNaam());
            pst.setString(3, product.getBeschrijving());
            pst.setDouble(4, product.getPrijs());
            ResultSet rs = pst.executeQuery();
            rs.close();
            return true;
        }catch (SQLException e){
            List<OVChipkaart> ovChipkaartList = new ArrayList<>(product.getOvChipkaarten());
            String query = "SELECT kaart_nummer AS kn FROM ov_chipkaar_product WHERE product.nummer=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, product.getProduct_nummer());
            ResultSet rs = pst.executeQuery();
            List<OVChipkaart> ovChipkaarts = new ArrayList<>();
            while (rs.next()) {
                String kn = rs.getString("kn");
                for (OVChipkaart ovChipkaart: ovChipkaartDAO.findAll()){
                    if (ovChipkaart.getKaartNummer()==Integer.parseInt(kn)){
                        ovChipkaarts.add(ovChipkaart);
                    }
                }
            }
            List<OVChipkaart> ovChipkaartList2 = new ArrayList<>(ovChipkaarts);
            ovChipkaartList2.remove(ovChipkaartList);
            for (OVChipkaart ovChipkaart : ovChipkaartList2){
                ovChipkaartDAO.delete(ovChipkaart);
            }
            ovChipkaartList.remove(ovChipkaarts);
            for (OVChipkaart ovChipkaart : ovChipkaartList){
                ovChipkaartDAO.save(ovChipkaart);
            }




            rs.close();

            System.out.println(e);
            return false;
        }
    }


    public boolean productDelete(Product product) throws SQLException {
        try{
            String query;
            query = "DELETE FROM product WHERE product_nummer=? AND naam=? AND beschrijving=? AND prijs=?";
            System.out.println("");
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, product.getProduct_nummer());
            pst.setString(2, product.getNaam());
            pst.setString(3, product.getBeschrijving());
            pst.setDouble(4, product.getPrijs());
            ResultSet rs = pst.executeQuery();
            rs.close();
            return true;
        }catch (SQLException e){
            System.out.println(e);
            OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOsql(connection);
            for (OVChipkaart ovChipkaart: product.getOvChipkaarten()){
                ovChipkaartDAO.save(ovChipkaart);
                String  query = "DELETE FROM ov_chipkaart_porduct WHERE kaart_nummer=? AND product_nummer=?";
                PreparedStatement pst = connection.prepareStatement(query);
                pst.setInt(1, ovChipkaart.getKaartNummer());
                pst.setInt(2, product.getProduct_nummer());
                ResultSet rs = pst.executeQuery();
                rs.close();
            }
            return false;
        }
    }
    public List<Product> findByOVchipkaart(OVChipkaart ovChipkaart) throws SQLException {
        String query = "SELECT product.product_nummer AS pn, product.naam AS nm, product.beschrijving AS bs, product.prijs AS ps FROM ov_chipkaart_product JOIN product ON ov_chipkaart.product_nummer = product.product_nummer WHERE ov_chipkaart_product = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, ovChipkaart.getKaartNummer());
        ResultSet rs = pst.executeQuery();
        List<Product> producten = new ArrayList<>();
        if (rs.next()) {
            String product_nummer = rs.getString("pn");
            String naam = rs.getString("nm");
            String beschrijving = rs.getString("bs");
            String prijs = rs.getString("ps");
            producten.add(new Product(Integer.parseInt(product_nummer),naam,beschrijving,Double.parseDouble(prijs)));
        } else {
            rs.close();
        }
        OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOsql(connection);
        for (Product product: producten){
            String query2 = "SELECT kaart_nummer AS kn FROM ov_chipkaar_product WHERE product.nummer=?";
            PreparedStatement pst2 = connection.prepareStatement(query2);
            pst2.setInt(1, product.getProduct_nummer());
            ResultSet rs2 = pst.executeQuery();
            List<OVChipkaart> ovChipkaarts = new ArrayList<>();
            while (rs2.next()) {
                String kn = rs2.getString("kn");
                for (OVChipkaart ovChipkaart2: ovChipkaartDAO.findAll()){
                    if (ovChipkaart2.getKaartNummer()==Integer.parseInt(kn)){
                        ovChipkaarts.add(ovChipkaart2);
                    }
                }
            }
            product.setOvChipkaarten(ovChipkaarts);
            rs2.close();
            pst2.close();
        }

        return producten;
    }
    public List<Product> findAll() throws SQLException {
        Statement sta = connection.createStatement();
        String query = "SELECT product_nummer AS pn, naam AS nm, beschrijving AS bs, prijs AS ps FROM product";
        ResultSet rs = sta.executeQuery(query);
        List<Product> producten = new ArrayList<>();
        while (rs.next()) {
            String product_nummer = rs.getString("pn");
            String naam = rs.getString("nm");
            String beschrijving = rs.getString("bs");
            String prijs = rs.getString("ps");
            producten.add(new Product(Integer.parseInt(product_nummer),naam,beschrijving,Double.parseDouble(prijs)));
        }
        rs.close();
        OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOsql(connection);
        for (Product product: producten){
            String query2 = "SELECT kaart_nummer FROM ov_chipkaart_product WHERE product_nummer=?";
            PreparedStatement pst2 = connection.prepareStatement(query2);
            pst2.setInt(1, product.getProduct_nummer());
            ResultSet rs2 = pst2.executeQuery();
            List<OVChipkaart> ovChipkaarts = new ArrayList<>();
            while (rs2.next()) {
                String kn = rs2.getString("kaart_nummer");
                for (OVChipkaart ovChipkaart2: ovChipkaartDAO.findAll()){
                    if (ovChipkaart2.getKaartNummer()==Integer.parseInt(kn)){
                        ovChipkaarts.add(ovChipkaart2);
                    }
                }
            }
            product.setOvChipkaarten(ovChipkaarts);
            rs2.close();
            pst2.close();
        }
        return producten;
    }
}
