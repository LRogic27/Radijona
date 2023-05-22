package controller;


import Threads.IconThread;
import Threads.FinishedOrderThread;
import entiteti.VrsteOsoba.Admin;
import entiteti.VrsteOsoba.Korisnik;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class MainClass extends Application {
    private static Stage mainStage;
    private static Korisnik korisnik;
    private static Admin admin;
    private static Timeline timeline;

    public static Timeline getTimeline() {
        return timeline;
    }

    public static void setTimeline(Timeline timeline) {
        MainClass.timeline = timeline;
    }

    private static FinishedOrderThread thread = new FinishedOrderThread();

    public static FinishedOrderThread getThread() {
        return thread;
    }

    public static void setThread(FinishedOrderThread thread) {
        MainClass.thread = thread;
    }

    private static LocalDate time = LocalDate.now();

    public static LocalDate getTime() {
        return time;
    }

    public static void setTime(LocalDate time) {
        MainClass.time = time;
    }

    public static void setMainStage(Stage mainStage) {
        MainClass.mainStage = mainStage;
    }

    public static String icon = "bike1.jpg";


    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle(String.valueOf(MainClass.getTime()));

        stage.getIcons().add(new Image(icon));
        stage.setScene(scene);
        stage.show();
        finishOrders();
        iconChange();
        MainClass.getTimeline().pause();
    }

    public void iconChange() {
        Timeline timeline2 = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.runLater(new IconThread());
            }
        }
        );
        timeline2.getKeyFrames().add(keyFrame);
        timeline2.setCycleCount(Timeline.INDEFINITE);
        timeline2.play();
    }

    public void finishOrders() {
        this.timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            thread.run();
            if (!thread.getFinishedOrders().equals("")) {
                timeline.pause();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, thread.getFinishedOrders());
                alert.setTitle("Completed orders");
                Platform.runLater(() -> {
                    alert.showAndWait();
                    timeline.play();
                });
                thread.setFinishedOrders("");
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static Korisnik getKorisnik() {
        return korisnik;
    }

    public static void setKorisnik(Korisnik korisnik) {
        MainClass.korisnik = korisnik;
    }

    public static Admin getAdmin() {
        return admin;
    }

    public static void setAdmin(Admin admin) {
        MainClass.admin = admin;
    }

    public static void writeLastOrderID(long id) {
        try (FileWriter fileWriter = new FileWriter("src/main/java/Baza/idNarudzba")) {
            fileWriter.write(String.valueOf(id));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getLastOrderID() {
        long id = 0;
        try (Scanner scanner = new Scanner(new File("src/main/java/Baza/idNarudzba"))) {
            return id = scanner.nextLong();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) throws SQLException {
        launch();
    }
}