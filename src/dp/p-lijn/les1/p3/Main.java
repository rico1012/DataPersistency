package dp.les1.p3;


import java.security.PublicKey;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    private static Connection conn;

    public static void main(String[] args) {
        try {
            getConnection();
            testReizigerDAO(new ReizigerDAOPsql(conn));
            testAdresDAO(new AdresDAOsql(conn));
            closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void  getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost/ovchip?user=postgres&password=herenweg146";
        conn = DriverManager.getConnection(url);
        System.out.println("[connection] connected");
    }

    public static void closeConnection() throws SQLException {
        System.out.println();
        System.out.println("[connection] close connection");
        conn.close();
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

    private static void testAdresDAO(AdresDAO adao) throws SQLException {
        System.out.println("\n----------- Test AdresDAO --------------");
        List<Adres> adressen = adao.findAll();
        //adres findall test
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen){
            System.out.println(a);
        }
        System.out.println();

        Adres a1 = new Adres(80, "3648CN", "178H", "jansenstraat", "Maarsen", 77);
        //adres save test
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdressDAO.save() ");
        adao.save(a1);
        adressen=adao.findAll();
        System.out.println("Er zijn nu "+adressen.size() + " adressen\n");
        System.out.println();
        System.out.println("huidige adressen:");
        for (Adres a : adressen){
            System.out.println(a);
        }
        System.out.println();
        //adres update test
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdressDAO.update() ");
        Adres a2= new Adres(80, "3728QN", "283K", "herenweg", "Vinkeveen", 77);
        adao.update(a2);
        adressen = adao.findAll();
        System.out.println();
        for (Adres a : adressen){
            System.out.println(a);
        }
        System.out.println();
        //adres find by reiziger test
        System.out.println("[Test] AdresDAO.findByReiziger()");
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum));
        System.out.println("reziger voor de test: "+ sietske);
        System.out.println("Adres reiziger: "+ adao.findByReiziger(sietske));
        System.out.println();
        //test of adres bij reiziger staat
        System.out.println("[Test] adres is toegevoegd aan reiziger");
        for (Reiziger reiziger : new ReizigerDAOPsql(conn).findAll()){
            System.out.println(reiziger);
        }
        System.out.println();



        //adres delete test
        System.out.println("[Test] AdresDAO.delete() geeft de volgende hoeveelheid");
        adao.delete(a2);
        adressen = adao.findAll();
        System.out.println("Er zijn nu "+adressen.size() + " adressen\n");
        System.out.println("huidige adressen:");
        for (Adres a : adressen){
            System.out.println(a);
        }


    }
}
