package dp.les1.p2;

import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args){
        String url = "jdbc:postgresql://localhost/ovchip?user=postgres&password=*****";

        try{
            Connection conn = DriverManager.getConnection(url);

            Statement sta = conn.createStatement();
            String query = "SELECT voorletters AS vl, achternaam AS an, tussenvoegsel AS tv, geboortedatum AS gd FROM reiziger";

            ResultSet rs = sta.executeQuery(query);

            String vl;
            String an;
            String tv;
            String gd;
            int aantal = 0;

//            System.out.println("alle reizigers:");
            while (rs.next()){
                vl = rs.getString("vl");
                an = rs.getString("an");
                tv = rs.getString("tv");
                gd = rs.getString("gd");

                aantal += 1;
                if (tv!=null){
                    tv=tv+" ";
                }else{tv="";}

                System.out.println("    #"+aantal+" "+vl+". "+tv+an+" ("+gd+")");
            }
            testReizigerDAO(new ReizigerDAOPsql(conn));
            rs.close();
            sta.close();
            conn.close();



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private static void getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost/ovchip?user=postgres&password=herenweg146";
        Connection conn = DriverManager.getConnection(url);
    }

    private static void closeConnection(){

    }
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
//        System.out.println(sietske);
        rdao.save(sietske);
        reizigers = rdao.findAll();

        System.out.println(reizigers.size() + " reizigers\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
    }
}
