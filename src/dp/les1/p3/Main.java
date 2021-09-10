package dp.les1.p3;


import java.security.PublicKey;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost/ovchip?user=postgres&password=";

        try {
            Connection conn = DriverManager.getConnection(url);
            dp.les1.p3.ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(conn);
            testReizigerDAO(new ReizigerDAOPsql(conn));
            AdresDAOsql adresDAOsql = new AdresDAOsql(conn);
            for(Reiziger r : reizigerDAOPsql.findAll()){
                Adres a = adresDAOsql.findByReiziger(r);
                if (a!=null){
                    r.setAdres(a);
                    System.out.println(r+a.toString());
                }else {
                    System.out.println(r);
                }
            }
            testAdresDAO(adresDAOsql);
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
//        System.out.println(sietske);
        rdao.save(sietske);
        reizigers = rdao.findAll();

        System.out.println(reizigers.size() + " reizigers\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
    }

    private static void testAdresDAO(AdresDAO adao){
        System.out.println("\n----------- Test AdresDAO --------------");
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen){
            System.out.println(a);
        }
        System.out.println();

        Adres a1 = new Adres(80, "3648CN", "178H", "jansenstraat", "Maarsen", 77);

        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdressDAO.save() ");
        adao.save(a1);
        adressen=adao.findAll();
        System.out.println(adressen.size() + " adressen\n");
        System.out.println();
        System.out.println("[Test] AdresDAO.delete() geeft de volgende hoeveelheid");
        adao.delete(a1);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");
    }
}
