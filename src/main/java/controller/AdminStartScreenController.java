package controller;


import Baza.DataBase;
import Threads.FinishedOrderThread;
import entiteti.GotoveNarudzbe;
import entiteti.Narudzba;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AdminStartScreenController {
    @FXML
    private TableView<GotoveNarudzbe<String>> userTable;
    @FXML
    private TableColumn<GotoveNarudzbe<String>,String> usernameColumn;
    @FXML
    private TableColumn<GotoveNarudzbe<String>,String> orderIDColumn;

    public void initialize(){
        usernameColumn.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getImeKorisnika()));
        orderIDColumn.setCellValueFactory(cellData-> new SimpleStringProperty("Order "+cellData.getValue().getNarudzba().getId()));
        userTable.setItems(FXCollections.observableList(FinishedOrderThread.getOrders()));
    }
}
