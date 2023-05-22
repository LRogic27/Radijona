package controller;

import Baza.DataBase;
import Datoteke.Login;
import Iznimke.PogreskaLogin;
import entiteti.Osoba;
import entiteti.VrsteOsoba.Admin;
import entiteti.VrsteOsoba.Korisnik;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class LoginScreenController {

    @FXML
    TextField usernameTextField;
    @FXML
    PasswordField passwordTextField;

    @FXML
    private void initialize(){}
    public void loginClick() {
        try {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();

            Optional<Osoba> osoba = Login.loginOsobe(username, password);

            if (!username.contains("#")) {
                if (osoba.isPresent()) {
                    if (osoba.get() instanceof Admin)
                        showAdminStartScreen((Admin) osoba.get());
                    if (osoba.get() instanceof Korisnik) {
                        long id = DataBase.getKorisnikIDByUsername(osoba.get().getUsername());
                        Korisnik korisnik = new Korisnik(id, osoba.get().getUsername(), DataBase.getNarudzba(id));
                        showKorisnikStartScreen(korisnik);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Wrong password or username");
                    alert.show();
                    passwordTextField.clear();
                    usernameTextField.clear();
                }
            } else throw new PogreskaLogin();
        } catch (IOException e) {
            System.out.println(usernameTextField.getText() + passwordTextField.getText());
        } catch (PogreskaLogin e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You can not use character'#'");
            alert.show();
            MainClass.getMainStage().close();
        }
    }
    public void signUp() throws IOException {
        MainClass.getTimeline().pause();
        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("signUpScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        MainClass.getMainStage().setTitle("Hello!");
        MainClass.getMainStage().setScene(scene);
        MainClass.getMainStage().show();
    }
    public void showAdminStartScreen(Admin admin) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("admin/admin-start-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        MainClass.getMainStage().setTitle(String.valueOf(MainClass.getTime()));
        MainClass.getMainStage().setScene(scene);
        MainClass.getMainStage().show();
        MainClass.setAdmin(admin);
    }
    public void showKorisnikStartScreen(Korisnik korisnik) throws IOException {
        MainClass.setKorisnik(korisnik);
        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("korisnik/korisnik-start-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        MainClass.getMainStage().setTitle(String.valueOf(MainClass.getTime()));
        MainClass.getMainStage().setScene(scene);
        MainClass.getMainStage().show();
    }
    public void startButton(){
        MainClass.getTimeline().play();
    }
    public void resetButton(){
        MainClass.setTime(LocalDate.now());
        MainClass.getMainStage().setTitle(String.valueOf(MainClass.getTime()));
    }
    public void stopButton(){
        MainClass.getTimeline().pause();
    }
}