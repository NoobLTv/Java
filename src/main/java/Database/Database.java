package Database;

//import javafx.collections.ObservableList;

import java.sql.*;
import Domain.Cursist;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class Database {
    private TableView<Cursist> cursistTable = new TableView<>();
    private TableColumn<Cursist, String> emailCol = new TableColumn<>();
    private TableColumn<Cursist, String> nameCol = new TableColumn<>();
    private TableColumn<Cursist, Date> geboCol = new TableColumn<>();
    private TableColumn<Cursist, String> gesCol = new TableColumn<>() ;
    private TableColumn<Cursist, String> woonCol = new TableColumn<>();
    private TableColumn<Cursist, String> adresCol = new TableColumn<>() ;
    private TableColumn<Cursist, String> landCol = new TableColumn<>();



    public Connection getConnection() {
        try {
            Connection connect = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=cursus;integratedSecurity=true;");
            return connect;

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
            return null;
        }
    }


    public ObservableList<Cursist> getCursist() {
        ObservableList<Cursist> cursistObservableList = FXCollections.observableArrayList();
        String query = "SELECT * FROM cursisten";
        Connection con;
        Statement st;
        ResultSet rs;

        try {
            con = getConnection();
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                String email = rs.getString("e_mail");
                String name = rs.getString("naam");
                Date geboortedatum = rs.getDate("geboortedatum");
                String geslacht = rs.getString("geslacht");
                String woonplaats = rs.getString("woonplaats");
                String adres = rs.getString("adres");
                String land = rs.getString("land");
                Cursist cursist = new Cursist(email, name, geboortedatum, geslacht, woonplaats, adres, land);
                cursistObservableList.add(cursist);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        //catch (ClassNotFoundException e) {
        //    return null;
        // }
        return cursistObservableList;
    }
    public void showCursists(){
        ObservableList<Cursist> list = getCursist();
        emailCol.setCellValueFactory(new PropertyValueFactory<Cursist, String>("e_mail"));
        cursistTable.setItems(list);

    }

    public Parent getViewCursist(){
        ObservableList<Cursist> list = getCursist();
        BorderPane bp = new BorderPane();;
        this.emailCol.setCellValueFactory(new PropertyValueFactory<>("e_mail"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory<>("naam"));
        this.gesCol.setCellValueFactory(new PropertyValueFactory<>("geslacht"));
        this.geboCol.setCellValueFactory(new PropertyValueFactory<>("geboortedatum"));
        this.woonCol.setCellValueFactory(new PropertyValueFactory<>("woonplaats"));
        this.adresCol.setCellValueFactory(new PropertyValueFactory<>("adres"));
        this.landCol.setCellValueFactory(new PropertyValueFactory<>("land"));
        this.cursistTable.setItems(list);
        this.cursistTable.getColumns().addAll(this.emailCol, this.nameCol, this.geboCol, this.gesCol, this.woonCol,this.adresCol, this.landCol);
        bp.setCenter(this.cursistTable);

        return bp;
    }

}
