package controller;

import Baza.DataBase;
import entiteti.DurationOfRepair;
import entiteti.Popravak;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class KorisnikFilterScreenController {

    @FXML
    TableView<Popravak> tableView;
    @FXML
    TableColumn<Popravak,String> id;
    @FXML
    TableColumn<Popravak,String> price;
    @FXML
    TableColumn<Popravak,String> type;
    @FXML
    TableColumn<Popravak,String> duration;
    @FXML
    TextField idTextField;
    @FXML
    TextField priceTextField;
    @FXML
    TextField typeTextField;
    @FXML
    ChoiceBox<Integer> durationChoiceBox;

    private List<Integer> durationOfRepairList(){
        List<Integer> list = new ArrayList<>();
        for(DurationOfRepair durationOfRepair : DurationOfRepair.values())
            list.add(durationOfRepair.duration);
        return list;
    }
    public void initialize(){
        id.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        type.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getNazivPopravka()));
        price.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getCijena())));
        duration.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getBrojDanaTrajanjaPopravka().duration)));
        tableView.setItems(FXCollections.observableList(MainClass.getKorisnik().getNarudzba().getPopravci()));
        durationChoiceBox.setItems(FXCollections.observableList(durationOfRepairList()));
    }

    public void filter(){
        String idString = idTextField.getText();
        String priceString = priceTextField.getText();
        String typeString = typeTextField.getText();
        Integer durationInteger = durationChoiceBox.getValue();
        List<Popravak> filteredList = new ArrayList<>();
        if(durationChoiceBox.getValue() == null ){
            filteredList = MainClass.getKorisnik().getNarudzba().getPopravci().stream().
                    filter(popravak -> String.valueOf(popravak.getId()).contains(idString)).
                    filter(popravak -> String.valueOf(popravak.getCijena()).contains(priceString)).
                    filter(popravak -> popravak.getNazivPopravka().contains(typeString)).
                    toList();
        }
        else{
            filteredList = MainClass.getKorisnik().getNarudzba().getPopravci().stream().
                    filter(popravak -> String.valueOf(popravak.getId()).contains(idString)).
                    filter(popravak -> String.valueOf(popravak.getCijena()).contains(priceString)).
                    filter(popravak -> popravak.getNazivPopravka().contains(typeString)).
                    filter(popravak -> popravak.getBrojDanaTrajanjaPopravka().duration == durationInteger).
                    toList();
        }

        tableView.setItems(FXCollections.observableList(filteredList));
        durationChoiceBox.setItems(FXCollections.observableList(durationOfRepairList()));
    }
}
