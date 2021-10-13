package dp.p_lijn.les3.p4andp5;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductDAOsql implements ProductDAO{

    private Connection connection;

    public ProductDAOsql(Connection connection) {
        this.connection = connection;
    }

    public boolean save(Product product) throws SQLException, ParseException {
        try{
            String query;
            query = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) " +
                    "VALUES (?,?,?,?)";
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
                if (!ovChipkaartDAO.findAll().contains(ovChipkaart)){
                    ovChipkaartDAO.save(ovChipkaart);
                }
                PreparedStatement pst2 = connection.prepareStatement("INSERT INTO ov_chipkaart_product (kaart_nummer,product_nummer, status, last_update) " +
                        "VALUES (?,?,?,?)");
                pst2.setInt(1, ovChipkaart.getKaartNummer());
                pst2.setInt(2, product.getProduct_nummer());
                pst2.setString(3, "actief");
                pst2.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                pst2.executeUpdate();
            }
            return false;
        }
    }


    public boolean update(Product product) throws SQLException {
        OVChipkaartDAO ovChipkaartDAO= new OVChipkaartDAOsql(connection);
        try{
            String query;
            query = "UPDATE product SET naam=?, beschrijving=?, prijs=?::numeric WHERE product_nummer=?";
            System.out.println("");
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(4, product.getProduct_nummer());
            pst.setString(1, product.getNaam());
            pst.setString(2, product.getBeschrijving());
            pst.setDouble(3, product.getPrijs());
            ResultSet rs = pst.executeQuery();
            rs.close();
            return true;
        }catch (SQLException e){
            List<OVChipkaart> ovChipkaartList = new ArrayList<>(product.getOvChipkaarten());
            String query = "SELECT kaart_nummer AS kn FROM ov_chipkaart_product WHERE product_nummer=?";
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
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM ov_chipkaart_product WHERE kaart_nummer=? AND product_nummer=?");
                preparedStatement.setInt(1, ovChipkaart.getKaartNummer());
                preparedStatement.setInt(2, product.getProduct_nummer());
                preparedStatement.executeUpdate();
            }
            ovChipkaartList.remove(ovChipkaarts);
            for (OVChipkaart ovChipkaart : ovChipkaartList){
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ov_chipkaart_product (kaart_nummer,product_nummer, status, last_update) VALUES (?,?,?,?)");
                preparedStatement.setInt(1, ovChipkaart.getKaartNummer());
                preparedStatement.setInt(2, product.getProduct_nummer());
                preparedStatement.setString(3, "actief");
                preparedStatement.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                preparedStatement.executeUpdate();
            }
            rs.close();
            return false;
        }
    }


    public boolean delete(Product product) throws SQLException {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer=?");
            preparedStatement.setInt(1, product.getProduct_nummer());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.close();

            return true;
        }catch (SQLException e){
            String query;
            query = "DELETE FROM product WHERE product_nummer=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, product.getProduct_nummer());
            pst.executeUpdate();
            return false;
        }
    }
    public List<Product> findByOVchipkaart(OVChipkaart ovChipkaart) throws SQLException {
        String query = "SELECT product.product_nummer AS pn, product.naam AS nm, product.beschrijving AS bs, product.prijs AS ps FROM ov_chipkaart_product JOIN product ON ov_chipkaart_product.product_nummer = product.product_nummer WHERE ov_chipkaart_product.kaart_nummer = ?";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, ovChipkaart.getKaartNummer());
        ResultSet rs = pst.executeQuery();
        List<Product> producten = new ArrayList<>();
        while (rs.next()) {
            String product_nummer = rs.getString("pn");
            String naam = rs.getString("nm");
            String beschrijving = rs.getString("bs");
            String prijs = rs.getString("ps");
            producten.add(new Product(Integer.parseInt(product_nummer),naam,beschrijving,Double.parseDouble(prijs)));
        }
        rs.close();

//        OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOsql(connection);
//        for (Product product: producten){
//            String query2 = "SELECT kaart_nummer FROM ov_chipkaart_product WHERE product_nummer=?";
//            PreparedStatement pst2 = connection.prepareStatement(query2);
//            pst2.setInt(1, product.getProduct_nummer());
//            ResultSet rs2 = pst2.executeQuery();
//            List<OVChipkaart> ovChipkaarts = new ArrayList<>();
//            while (rs2.next()) {
//                String kn = rs2.getString("kaart_nummer");
//                for (OVChipkaart ovChipkaart2: ovChipkaartDAO.findAll()){
//                    if (ovChipkaart2.getKaartNummer()==Integer.parseInt(kn)){
//                        ovChipkaarts.add(ovChipkaart2);
//                    }
//                }
//            }
//            product.setOvChipkaarten(ovChipkaarts);
//            rs2.close();
//            pst2.close();
//        }

        return producten;
    }
    public List<Product> findAll() throws SQLException, ParseException {
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
            List<OVChipkaart> ovChipkaartList = new ArrayList<>();
            while (rs2.next()){
                String kn = rs2.getString("kaart_nummer");
                ovChipkaartList.add(ovChipkaartDAO.findById(Integer.parseInt(kn)));
            }
            product.setOvChipkaarten(ovChipkaartList);
            rs2.close();
            pst2.close();
        }
        return producten;
    }
}
