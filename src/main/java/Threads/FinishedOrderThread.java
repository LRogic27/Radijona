package Threads;

import controller.MainClass;
import entiteti.GenerickaKlasaSaDvaParametra;
import entiteti.GotoveNarudzbe;
import entiteti.Narudzba;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FilenameFilter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinishedOrderThread implements Runnable{


   private  String finishedOrders ="";

    public  String getFinishedOrders() {
        return finishedOrders;
    }

    public  void setFinishedOrders(String finishedOrders) {
        this.finishedOrders = finishedOrders;
    }

    public FinishedOrderThread() {
    }

    @Override
    public void run() {
                finishThreadRun();
    }

    private void finishThreadRun() {
        MainClass.getMainStage().setTitle(String.valueOf(MainClass.getTime()));
        List<GotoveNarudzbe<String>> list = getOrders();
        String finishedOrdersString = "";
        for(GotoveNarudzbe<String> narudzba:list){
            if(narudzba.getNarudzba().getPocetakPopravka().plusDays(Narudzba.trajanjePopravka(narudzba.getNarudzba().getPopravci())).isEqual(MainClass.getTime()))
            {
                System.out.println(narudzba.getNarudzba().getPocetakPopravka());
                this.setFinishedOrders(String.join("\n","Order ID:"+narudzba.getNarudzba().getId()));
                deleteOrder(narudzba.getImeKorisnika());
            }
        }
        MainClass.setTime(MainClass.getTime().plusDays(1));
    }

    public static List<GotoveNarudzbe<String>> getOrders(){
        List<GotoveNarudzbe<String>> orders = new ArrayList<>();
        File folder = new File("src/main/java/Datoteke/AdminOrders/");
        List<File> listOfFiles = List.of(folder.listFiles());
        for(File file:listOfFiles){
            GenerickaKlasaSaDvaParametra<File,Narudzba> generickaKlasaSaDvaParametra = new GenerickaKlasaSaDvaParametra<>(file);
            Narudzba narudzba = generickaKlasaSaDvaParametra.readObject(file);
            String ime = file.getName().substring(0,file.getName().indexOf("."));
            orders.add(new GotoveNarudzbe<>(ime,narudzba));
        }
        return orders;
    }
    public static void deleteOrder(String name){

        File folder = new File("src/main/java/Datoteke/AdminOrders/");
        List<File> listOfFiles = List.of(folder.listFiles());

        for(File file: listOfFiles){
            String fileName = file.getName().substring(0,file.getName().indexOf("."));
            if(name.equals(fileName))
                file.delete();
        }

    }
}
