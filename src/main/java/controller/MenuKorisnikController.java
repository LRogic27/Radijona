package controller;

import Baza.DataBase;
import controller.MainClass;
import entiteti.Narudzba;
import entiteti.VrsteOsoba.Korisnik;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.util.List;
import java.util.Optional;

public class MenuKorisnikController {

    public void homePage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("korisnik/korisnik-start-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        MainClass.getMainStage().setTitle(String.valueOf(MainClass.getTime()));
        MainClass.getMainStage().setScene(scene);
        MainClass.getMainStage().show();
    }

    public void logout() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Confirm logout");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK){
            FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            MainClass.getMainStage().setTitle(String.valueOf(MainClass.getTime()));
            MainClass.getMainStage().setScene(scene);
            MainClass.getMainStage().show();
        }
    }

    public void orderScreen() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("korisnik/korisnik-order.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        MainClass.getMainStage().setTitle(String.valueOf(MainClass.getTime()));
        MainClass.getMainStage().setScene(scene);
        MainClass.getMainStage().show();
    }

    public void filter() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("korisnik/korisnik-filter-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        MainClass.getMainStage().setTitle(String.valueOf(MainClass.getTime()));
        MainClass.getMainStage().setScene(scene);
        MainClass.getMainStage().show();
    }

    public void printOrder() {
        String fileName = "User_ID_" + MainClass.getKorisnik().getId() + "_" +
                MainClass.getKorisnik().getUsername() + "_Order_ID_" + MainClass.getKorisnik().getNarudzba().getId();
        try {
            File newFile = new File("src/main/java/Datoteke/Order/" + fileName + ".txt");
            if (newFile.createNewFile()) {
                try (FileWriter output = new FileWriter(newFile)) {
                    for(Korisnik korisnik: DataBase.dohvatiKorisnike()){
                        if(korisnik.getId() == MainClass.getKorisnik().getId()){
                            MainClass.setKorisnik(korisnik);
                        }
                    }
                    output.write(MainClass.getKorisnik().napisiRacun(MainClass.getKorisnik()));
                } catch (Exception e) {
                    e.getCause();
                }
                saveAdminOrder(MainClass.getKorisnik().getNarudzba());
                DataBase.obrisiNarudzbu(MainClass.getKorisnik().getId());
                DataBase.addNarudzba(MainClass.getKorisnik().getId());
                MainClass.getKorisnik().getNarudzba().setId(MainClass.getLastOrderID()+1);
                MainClass.writeLastOrderID(MainClass.getKorisnik().getNarudzba().getId());
                MainClass.getKorisnik().setNarudzba(DataBase.getNarudzba(MainClass.getKorisnik().getId()));
                homePage();

            } else System.out.println("Postoji vec");
        } catch (IOException e) {
            e.getCause();
        }
    }
    public static void saveAdminOrder(Narudzba narudzba){

        try {
            int index = 1;
            boolean notcreated = true;
            do{
                File newFile = new File("src/main/java/Datoteke/AdminOrders/"+DataBase.getKorisnik(narudzba.getIdKorisnika()).getUsername()+index+".dat");
                DataBase.obrisiNarudzbu(narudzba.getIdKorisnika());
                if (newFile.createNewFile()) {
                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(newFile.getPath()))) {
                        out.writeObject(narudzba);
                    } catch (IOException e) {
                        System.out.println("Pogreska prilikom serijalizacije");
                    }
                    notcreated = false;
                } else {
                    index++;
                }
            }while(notcreated);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
