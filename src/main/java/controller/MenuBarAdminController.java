package controller;

import Baza.DataBase;
import entiteti.GenerickaKlasaSaDvaParametra;
import entiteti.Popravak;
import entiteti.VrsteOsoba.Korisnik;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class MenuBarAdminController {

    static boolean pisanje = false;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH-mm");

    public void logout() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Confirm logout");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            MainClass.getMainStage().setTitle(String.valueOf(MainClass.getTime()));
            MainClass.getMainStage().setScene(scene);
            MainClass.getMainStage().show();
        }
    }
    public void homePage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("admin/admin-start-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        MainClass.getMainStage().setTitle(String.valueOf(MainClass.getTime()));
        MainClass.getMainStage().setScene(scene);
        MainClass.getMainStage().show();
    }
    public void priceListShow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("admin/priceListScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        MainClass.getMainStage().setTitle(String.valueOf(MainClass.getTime()));
        MainClass.getMainStage().setScene(scene);
        MainClass.getMainStage().show();
    }
    public  void  priceListPrint() throws InterruptedException {

        if(!pisanje){
            showOldPriceLists();
        }else{
            File file = new File("src/main/java/Datoteke/PriceList/popravci.dat");
            GenerickaKlasaSaDvaParametra<File,Popravak> generickaKlasaSaJednimParametrom = new GenerickaKlasaSaDvaParametra<>(file);
            List<Popravak> pomLista = generickaKlasaSaJednimParametrom.readObjects(file);
            for(Popravak popravak: pomLista){
                System.out.println(popravak);
            }
            printList();
        }
    }
    public synchronized  void printList(){
        LocalDateTime now = LocalDateTime.now();
        String name = "priceList(" + now.format(formatter) + ")";
        try {
            File newFile = new File("src/main/java/Datoteke/PriceList/popravci.dat");

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(newFile.getPath()))) {
                List<Popravak> popravakList = DataBase.priceList();

                for (Popravak popravak : popravakList) {
                    out.writeObject(popravak);
                }

            } catch (IOException e) {
                System.out.println("Pogreska prilikom serijalizacije");
            }

        }catch (Exception e){
            System.out.println("Greska");
        }
        pisanje = false;
        notifyAll();
    }
    public synchronized void showOldPriceLists() throws InterruptedException {
        if(pisanje){
            wait();

        }else{
            File file = new File("src/main/java/Datoteke/PriceList/popravci.dat");
            GenerickaKlasaSaDvaParametra<File,Popravak> generickaKlasaSaJednimParametrom = new GenerickaKlasaSaDvaParametra<>(file);
            List<Popravak> pomLista = generickaKlasaSaJednimParametrom.readObjects(file);
            for(Popravak popravak: pomLista){
                System.out.println(popravak);
            }
        }
        pisanje = true;
    }
    public void customersShow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("admin/customersScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        MainClass.getMainStage().setTitle(String.valueOf(MainClass.getTime()));
        MainClass.getMainStage().setScene(scene);
        MainClass.getMainStage().show();
    }
    public void customersPrint() {
        LocalDateTime now = LocalDateTime.now();
        String name = "customerList(" + now.format(formatter) + ")";
        try {
            File newFile = new File("src/main/java/Datoteke/Customers/" + name + ".dat");
            if (newFile.createNewFile()) {
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(newFile.getPath()))) {
                    List<Korisnik> korisnikList = DataBase.dohvatiKorisnike();

                    for (Korisnik korisnik : korisnikList) {
                        out.writeObject(korisnik);
                    }
                } catch (IOException e) {
                    System.out.println("Pogreska prilikom serijalizacije");
                }
                showOldCustomerLists();
            } else {
                System.out.println("Postoji vec");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void showOldCustomerLists() {
        File folder = new File("src/main/java/Datoteke/Customers/");
        List<File> listOfFiles = List.of(folder.listFiles());

        for (File file : listOfFiles) {
            GenerickaKlasaSaDvaParametra<File, Korisnik> generickaKlasaSaJednimParametrom = new GenerickaKlasaSaDvaParametra<>(file);
            List<Korisnik> pomLista = generickaKlasaSaJednimParametrom.readObjects(file);
            String s = file.getPath();
            s = s.substring(s.indexOf("(") + 1);
            s = s.substring(0, s.indexOf(")"));
            System.out.println(s);
            for (Korisnik korisnik : pomLista) {
                System.out.println(korisnik);
            }
        }
    }
}
