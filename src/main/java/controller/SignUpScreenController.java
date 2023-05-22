package controller;

import Baza.DataBase;
import Iznimke.NemoguOcitatiKorisnika;
import entiteti.VrsteOsoba.Korisnik;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class SignUpScreenController{

    @FXML
    TextField username;
    @FXML
    TextField password;

    public void initialize(){

    }
    public void backButton() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        MainClass.getMainStage().setTitle(String.valueOf(MainClass.getTime()));
        MainClass.getMainStage().setScene(scene);
        MainClass.getMainStage().show();
        MainClass.getTimeline().play();

    }
    public void signUp(){

        String usernameString = username.getText();
        String passwordString = password.getText();
        if(usernameString.contains("#")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Username can not contain '#'");
            alert.show();
            username.clear();
            password.clear();
        }
        else{
            boolean free = true;
            try(Scanner scanner =new Scanner(new File("src/main/java/Datoteke/korisnici"))){
                while(scanner.hasNextLine()){
                    try{
                        String[] fields = scanner.nextLine().split(";");
                        if (fields.length != 4) {
                            throw new NemoguOcitatiKorisnika();
                        }
                        Integer ID = Integer.parseInt(fields[0]);
                        var user = fields[1];
                        var pass = fields[2];
                        var bool = fields[3];

                        if(usernameString.equals(user)){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Username is not available");
                            alert.show();
                            username.clear();
                            password.clear();
                            free = false;
                        }
                    } catch (NemoguOcitatiKorisnika e) {

                    } catch (NumberFormatException e) {

                    }

                }
                if(free){
                    DataBase.addKorisnik(new Korisnik(1,usernameString,passwordString));
                    backButton();

                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }
}
