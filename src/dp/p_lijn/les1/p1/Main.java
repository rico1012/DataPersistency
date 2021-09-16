package dp.p_lijn.les1.p1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args){
        String url = "jdbc:postgresql://localhost/ovchip?user=postgres&password=";

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

            System.out.println("alle reizigers:");
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
            rs.close();
            sta.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
