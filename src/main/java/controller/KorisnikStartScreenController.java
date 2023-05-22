package controller;

import Baza.DataBase;
import Sort.SortPopravciByType;
import entiteti.Narudzba;
import entiteti.Popravak;
import Sort.SortPopravci;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

public class KorisnikStartScreenController {


    @FXML
    private Label welcome;
    @FXML
    private Label orderNumb;
    @FXML
    private TableView<Popravak> popravakTableView;
    @FXML
    private TableColumn<Popravak,String> ID;
    @FXML
    private TableColumn<Popravak,String> type;
    @FXML
    private TableColumn<Popravak,String> price;
    @FXML
    private TableColumn<Popravak,String> duration;

    public void initialize(){
        ID.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        type.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getNazivPopravka()));
        price.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getCijena())));
        duration.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getBrojDanaTrajanjaPopravka().duration)));
        popravakTableView.setItems(FXCollections.observableList(DataBase.getPopravci(MainClass.getKorisnik().getNarudzba().getId())));

        welcome.setText("Welcome "+MainClass.getKorisnik().getUsername());
        long brojDana = ChronoUnit.DAYS.between(MainClass.getKorisnik().getNarudzba().getPocetakPopravka().plusDays
                        (Narudzba.trajanjePopravka(MainClass.getKorisnik().getNarudzba().getPopravci())),LocalDate.now());
        orderNumb.setText("Order number:"+MainClass.getKorisnik().getNarudzba().getId()+" will be finished in "+Math.abs(brojDana)+" days");
    }
    public void sortByPrice(){
        List<Popravak> sorted = DataBase.getPopravci(MainClass.getKorisnik().getNarudzba().getId());
        Collections.sort(sorted,new SortPopravci());
        popravakTableView.setItems(FXCollections.observableList(sorted));
    }
    public void sortByType(){
        List<Popravak> sorted = DataBase.getPopravci(MainClass.getKorisnik().getNarudzba().getId());
        Collections.sort(sorted,new SortPopravciByType());
        popravakTableView.setItems(FXCollections.observableList(sorted));
    }
}
