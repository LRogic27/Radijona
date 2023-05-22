package controller;

import Baza.DataBase;
import Iznimke.NemoguDodatiPopravak;
import Iznimke.NemoguObrisatiPopravak;
import entiteti.Narudzba;
import entiteti.Popravak;
import entiteti.VrsteOsoba.Korisnik;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.security.spec.ECField;

public class KorisnikOrderController {

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
    @FXML
    private TableView<Popravak> korisnikPopravci;
    @FXML
    private TableColumn<Popravak,String> typeKorisnik;
    @FXML
    private TableColumn<Popravak,String> priceKorisnik;
    @FXML
    private TableColumn<Popravak,String> durationKorisnik;


    public void initialize(){

        ID.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        type.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getNazivPopravka()));
        price.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getCijena())));
        duration.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getBrojDanaTrajanjaPopravka().duration)));
        popravakTableView.setItems(FXCollections.observableList(DataBase.priceList()));

        typeKorisnik.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getNazivPopravka()));
        priceKorisnik.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getCijena())));
        durationKorisnik.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getBrojDanaTrajanjaPopravka().duration)));
        korisnikPopravci.setItems(FXCollections.observableList(DataBase.getPopravci(MainClass.getKorisnik().getNarudzba().getId())));
    }

    public void addButton(){
        try{
            if(popravakTableView.getSelectionModel().getSelectedItem() !=null){
                Popravak popravak = popravakTableView.getSelectionModel().getSelectedItem();
                DataBase.dodajPopravak(MainClass.getKorisnik().getNarudzba().getId(),popravak.getId());
                korisnikPopravci.setItems(FXCollections.observableList(DataBase.getPopravci(MainClass.getKorisnik().getNarudzba().getId())));
                MainClass.getKorisnik().setNarudzba(DataBase.getNarudzba(MainClass.getKorisnik().getId()));
            }else throw new NemoguDodatiPopravak();

        }catch (NemoguDodatiPopravak e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Select repair before you press add button");
            alert.show();
        }

    }
    public void deleteButton(){
        try{
            if(korisnikPopravci.getSelectionModel().getSelectedItem() != null){
                Popravak popravak = korisnikPopravci.getSelectionModel().getSelectedItem();
                DataBase.obrisiPopravak(popravak.getId());
                korisnikPopravci.setItems(FXCollections.observableList(DataBase.getPopravci(MainClass.getKorisnik().getNarudzba().getId())));
                MainClass.getKorisnik().setNarudzba(DataBase.getNarudzba(MainClass.getKorisnik().getId()));
            }else throw new NemoguObrisatiPopravak();
        }catch (NemoguObrisatiPopravak e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Select repair before you press delete button");
            alert.show();
        }

    }
}
