package controller;

import Baza.DataBase;
import entiteti.Popravak;
import entiteti.VrsteOsoba.Korisnik;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class CustomersScreenController {
    @FXML
    private ChoiceBox<Korisnik> choiceBox;
    @FXML
    private TableView<Popravak> tableViewPopravci;
    @FXML
    private TableColumn<Popravak,String> ID;
    @FXML
    private TableColumn<Popravak,String> type;
    @FXML
    private TableColumn<Popravak,String> price;
    @FXML
    private TableColumn<Popravak,String> date;
    @FXML
    Button deleteButton;


    public void initialize(){

            choiceBox.setItems(FXCollections.observableList(DataBase.dohvatiKorisnike()));
            if(choiceBox.getItems().size() == 0){
                choiceBox.setItems(FXCollections.observableList(new ArrayList<Korisnik>()));
            }else{
                choiceBox.getSelectionModel().selectFirst();
                setTableViewPopravci(choiceBox.getValue());
            }


              deleteButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
                obrisiKorisnika();
                System.out.println("4"+choiceBox.getItems());
            }
        });
            choiceBox.getSelectionModel().selectedItemProperty().addListener(this::odabranKorisnik);

    }
    private void odabranKorisnik(Observable observable,Korisnik previousKorisnik,Korisnik korisnik){
        setTableViewPopravci(korisnik);
    }
    private void setTableViewPopravci(Korisnik korisnik){
        if(korisnik == null){
            LocalDate pocetakPopravka = null;
            ID.setCellValueFactory(cellData-> new SimpleStringProperty(null));
            type.setCellValueFactory(cellData-> new SimpleStringProperty(null));
            price.setCellValueFactory(cellData-> new SimpleStringProperty(null));
            date.setCellValueFactory(cellData-> new SimpleStringProperty(null));
            tableViewPopravci.setItems(null);
        }else{
            LocalDate pocetakPopravka = korisnik.getNarudzba().getPocetakPopravka();
            ID.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
            type.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getNazivPopravka()));
            price.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getCijena())));
            date.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(pocetakPopravka)));
            tableViewPopravci.setItems(FXCollections.observableList(DataBase.getNarudzba(korisnik.getId()).getPopravci()));
        }

    }
    public void obrisiKorisnika(){

        long id = choiceBox.getValue().getId();
        System.out.println(id);

        if(!(choiceBox.getItems().size() == 0))
        for(Korisnik korisnik : choiceBox.getItems()){
            System.out.println("1"+choiceBox.getItems());
            if(korisnik.getId() == id)
                choiceBox.getItems().remove(korisnik);
            System.out.println("2"+choiceBox.getItems());
            if(choiceBox.getItems().size() == 0){
                choiceBox.setItems(FXCollections.observableList(new ArrayList<Korisnik>()));
                System.out.println("3"+choiceBox.getItems());
                break;
            }
        }

        if(choiceBox.getItems().size()==0){
            choiceBox.setItems(FXCollections.observableList(new ArrayList<Korisnik>()));
            System.out.println("Brisem2 korisnika id:"+id);
            DataBase.obrisiKorisnika(id);
        } else {
            System.out.println("Brisem korisnika id:"+id);
            if(choiceBox.getItems().size() == 0){
                choiceBox.setItems(FXCollections.observableList(new ArrayList<>()));
                choiceBox.getSelectionModel().select(null);
            }else{
                choiceBox.getSelectionModel().selectFirst();
                setTableViewPopravci(choiceBox.getValue());
            }
            DataBase.obrisiKorisnika(id);
        }
    }
    public void obrisiPopravak(){
        try{
            long id = tableViewPopravci.getSelectionModel().getSelectedItem().getId();
            DataBase.obrisiPopravak(id);
            choiceBox.setItems(FXCollections.observableList(DataBase.dohvatiKorisnike()));
            choiceBox.getSelectionModel().selectFirst();
            setTableViewPopravci(choiceBox.getValue());
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Select repair before you press Delete selected repair button");
            alert.show();
        }

    }
}
