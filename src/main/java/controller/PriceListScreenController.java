package controller;

import Baza.DataBase;
import entiteti.DurationOfRepair;
import entiteti.Popravak;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PriceListScreenController {

    @FXML
    private TextField typeTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private ChoiceBox<Integer> durationTextField;
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

    private List<Integer> durationOfRepairList(){
        List<Integer> list = new ArrayList<>();
        for(DurationOfRepair durationOfRepair : DurationOfRepair.values())
            list.add(durationOfRepair.duration);
        return list;
    }
    public void initialize(){

        durationTextField.setItems(FXCollections.observableList(durationOfRepairList()));
        ID.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        type.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getNazivPopravka()));
        price.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getCijena())));
        duration.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getBrojDanaTrajanjaPopravka().duration)));
        popravakTableView.setItems(FXCollections.observableList(DataBase.priceList()));

        ObservableList<Popravak> selectedItems = popravakTableView.getSelectionModel().getSelectedItems();
        selectedItems.addListener(new ListChangeListener<Popravak>() {
                    @Override
                    public void onChanged(
                            Change<? extends Popravak> change) {
                        if(change.getList().size() == 0) return;
                        Popravak p = change.getList().get(0);
                        typeTextField.setText(p.getNazivPopravka());
                        priceTextField.setText(String.valueOf(p.getCijena()));
                        durationTextField.getSelectionModel().select(p.getBrojDanaTrajanjaPopravka().duration);
                    }
                });

    }
    public void changeButton(){
        try{
            long id  = popravakTableView.getSelectionModel().getSelectedItem().getId();
            String type = typeTextField.getText();
            BigDecimal price = BigDecimal.valueOf(Double.valueOf(priceTextField.getText()));
            Integer duration = Integer.valueOf(durationTextField.getValue());
            DataBase.azurirajVrstuPopravka(id,price,type,duration);
            popravakTableView.setItems(FXCollections.observableList(DataBase.priceList()));
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Select item before you press change button");
                    alert.show();
        }

    }
}
