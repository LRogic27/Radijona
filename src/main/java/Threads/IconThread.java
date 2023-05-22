package Threads;

import controller.MainClass;
import javafx.scene.image.Image;

public class IconThread implements Runnable{

    public static int i = 0;
    @Override
    public void run() {

       i++;
        if(i % 2 == 0){
            MainClass.icon="/bike1.jpg";
        }
        else{
            MainClass.icon = "/bike2.jpg";
        };
        System.out.println(i);
        MainClass.getMainStage().getIcons().remove(0);
        MainClass.getMainStage().getIcons().add(new Image(MainClass.class.getResourceAsStream(MainClass.icon)));
    }
}
