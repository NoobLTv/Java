package GUI;

import Database.Database;
import Domain.Course;
import Domain.Cursist;
import Database.CursistController;


import Domain.Geslacht;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;


import java.sql.*;

public class CursistMenu {
    private Database db = new Database();
    private CursistController cController = new CursistController();
    private TableView<Cursist> cursistTable = new TableView<>();
    private TableColumn<Cursist, String> emailCol = new TableColumn<>("Email");
    private TableColumn<Cursist, String> nameCol = new TableColumn<>("Naam");
    private TableColumn<Cursist, Date> geboCol = new TableColumn<>("GeboorteDatum");
    private TableColumn<Cursist, String> gesCol = new TableColumn<>("Geslacht");
    private TableColumn<Cursist, String> woonCol = new TableColumn<>("Woonplaats");
    private TableColumn<Cursist, String> adresCol = new TableColumn<>("Adres");
    private TableColumn<Cursist, String> landCol = new TableColumn<>("Land");
    private Label emailLabel = new Label("Email: ");
    private Label nameLabel = new Label("Naam: ");
    private Label birthDateLabel = new Label("GeboorteDatum: ");
    private Label sexLabel = new Label("Geslacht: ");
    private Label cityLabel = new Label("woonplaats: ");
    private Label addressLabel = new Label("adres: ");
    private Label countryLabel = new Label("land: ");
    private TextField tfEmail = new TextField();
    private TextField tfName = new TextField();
    private TextField tfBirthDate = new TextField();
    private ComboBox drMenuBox = new ComboBox();
    private TextField tfCity = new TextField();
    private TextField tfAddress = new TextField();
    private TextField tfCountry = new TextField();
    private Button btnInsert = new Button("Toevoegen");
    private Button btnDelete = new Button("Verwijderen");
    private Button btnUpdate = new Button("Update");



    // onderstaande methode zet cursisten data in de cellen van de tabel.
    public void showCursists(){
        ObservableList<Cursist> cursistenLijst = cController.getCursistList();
        this.emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.gesCol.setCellValueFactory(new PropertyValueFactory<>("sex"));
        this.geboCol.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        this.woonCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        this.adresCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        this.landCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        this.cursistTable.setItems(cursistenLijst);
    }

    // methode maakt GUI voor cursisten
    public Scene getView() {

        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(25, 25, 25, 25));

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25,25,25,25));

        HBox hbox = new HBox ();
        hbox.getChildren().setAll(this.btnInsert, this.btnUpdate, this.btnDelete);
        hbox.setSpacing(25);
        hbox.setPadding(new Insets(20, 0, 0 , 0));

        this.btnInsert.setOnAction((event)->this.insertRecord());
        this.btnDelete.setOnAction((event)->this.deleteRecord());
        this.btnUpdate.setOnAction((event)->this.updateRecord());
        this.setCellValueFromTableToTextField();
        this.cursistTable.getColumns().addAll(this.emailCol, this.nameCol, this.geboCol, this.gesCol, this.woonCol, this.adresCol, this.landCol);

        bp.setRight(this.cursistTable);
        bp.setLeft(gridPane);
        this.showCursists();

        for (Geslacht geslacht : Geslacht.values()) {
            drMenuBox.getItems().add(geslacht);
        }

        gridPane.add(emailLabel, 0, 0);
        gridPane.add(tfEmail, 1 ,0);
        gridPane.add(nameLabel, 0,1);
        gridPane.add(tfName, 1, 1);
        gridPane.add(birthDateLabel, 0,2);
        gridPane.add(tfBirthDate, 1, 2);
        gridPane.add(sexLabel, 0, 3);
        gridPane.add(drMenuBox, 1, 3);
        gridPane.add(cityLabel, 0, 4);
        gridPane.add(tfCity, 1, 4);
        gridPane.add(addressLabel, 0, 5);
        gridPane.add(tfAddress, 1, 5);
        gridPane.add(countryLabel, 0, 6);
        gridPane.add(tfCountry, 1, 6);
        gridPane.add(hbox, 0, 7);

        Scene scene = new Scene(bp);
        return scene;
    }



    //De methoden die toevoegButton moet uitvoeren
    private void insertRecord(){
        String query = cController.makeInsertQuery(
                this.tfEmail.getText(),
                this.tfName.getText(),
                this.tfBirthDate.getText(),
                this.drMenuBox.getValue().toString(),
                this.tfCity.getText(),
                this.tfAddress.getText(),
                this.tfCountry.getText());
        cController.executeQuery(query);
        this.showCursists();
    }

    private void deleteRecord(){
        //Controleer of er een record geselecteerd is.
        if (cursistTable.getSelectionModel().getSelectedItem() == null) {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setContentText("Geen record geselecteerd");
            warningAlert.show();}

        // Ophalen en vervolgens verwijderen van record a.d.h.v muisselectie
        else {
            TablePosition pos = cursistTable.getSelectionModel().getSelectedCells().get(0);
            int row = pos.getRow();
            Cursist cursist = cursistTable.getItems().get(row);

            String query = cController.makeDeleteQuery(cursist.getEmail());
            this.cController.executeQuery(query);
            this.showCursists();
        }
    }

    public void updateRecord() {
        if (cursistTable.getSelectionModel().getSelectedItem() == null) {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setContentText("Geen record geselecteerd");
            warningAlert.show();}
        else {
            String query = cController.makeUpdateQuery(
                    this.tfEmail.getText(),
                    this.tfName.getText(),
                    this.tfBirthDate.getText(),
                    this.drMenuBox.getValue().toString(),
                    this.tfCity.getText(),
                    this.tfAddress.getText(),
                    this.tfCountry.getText());
            cController.executeQuery(query);
            this.showCursists();
        }
    }

    //Zet data in de inputfields door op een cel van de tabel te klikken.
    public void setCellValueFromTableToTextField(){
        cursistTable.setOnMouseClicked(e -> {
            Cursist cursist = cursistTable.getItems().get(cursistTable.getSelectionModel().getSelectedIndex());
            this.tfEmail.setText(cursist.getEmail());
            this.tfName.setText(cursist.getName());
            this.tfBirthDate.setText(cursist.getBirthdate());
            this.drMenuBox.setValue(cursist.getSex());
            this.tfCity.setText(cursist.getCity());
            this.tfAddress.setText(cursist.getAddress());
            this.tfCountry.setText(cursist.getCountry());
        });
    }
}

