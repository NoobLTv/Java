package Database;

import Domain.Cursist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CursistController {
private Database db = new Database();



// Onderstaande methode geeft een lijst met cursist objecten.
    public ObservableList<Cursist> getCursistList() {
        ObservableList<Cursist> CursistLijst = FXCollections.observableArrayList();
        String query = "SELECT * FROM cursisten";

        try {
            Connection con = db.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Cursist cursist = new Cursist(
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getInt("birthDay"),
                        rs.getInt("birthMonth"),
                        rs.getInt("birthYear"),
                        rs.getString("sex"),
                        rs.getString("city"),
                        rs.getString("postalCode"),
                        rs.getString("street"),
                        rs.getInt("houseNr"),
                        rs.getString("country"));
                CursistLijst.add(cursist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return CursistLijst;
    }

    //Deze methoden voert een query uit.
    public void executeQuery(String query){
        Connection conn = db.getConnection();
        try{
            Statement st = conn.createStatement();
            st.executeUpdate(query);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Deze methode stelt een query samen om een cursist record toe te voegen.
    public String makeInsertQuery (String email, String name, int bd, int bm, int by, String sex, String city,
                                   String postalCode, String street, int houseNr, String country){

        String query = "INSERT INTO cursisten VALUES('" + email + "','"
                + name + "'," + bd + ","
               + bm + "," + by + ",'"
               + sex + "','" + city +  "','"
                + postalCode + "','" + street + "',"
                + houseNr + ",'" + country + "')";
     return query;
    }

    //Deze methode stelt een query samen om een cursist record te verwijderen
    public String makeDeleteQuery (String email){
        String query = "DELETE FROM cursisten WHERE email =" + "'" + email + "'";
        return query;
    }

    // deze methode stelt een update query samen om een cursist te updaten
    public String makeUpdateQuery(String email, String name, int bd, int bm, int by, String sex, String city,
                                  String postalCode, String street, int houseNr, String country) {
        String query = "UPDATE cursisten SET name ='" + name +
                "', birthDay = " + bd +
                ", birthMonth = " + bm +
                ", birthYear = " + by +
                ", sex = '" + sex +
                "', city = '" + city +
                "', postalCode = '" + postalCode +
                "', street = '" + street +
                "', houseNr = '" + houseNr +
                "', country = '" + country +
                "' WHERE email = '" + email +"'";
        return query;
    }
}
