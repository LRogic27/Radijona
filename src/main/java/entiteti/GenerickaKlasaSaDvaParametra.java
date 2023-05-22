package entiteti;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class GenerickaKlasaSaDvaParametra<T extends File,V> {

    private T file;

    public GenerickaKlasaSaDvaParametra(T file){
        this.file = file;
    }

    public List<V> readObjects(T file){
        List<V> list = new ArrayList<>();
        boolean cont = true;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.file.getPath()))) {
            while (cont) {
                V object = null;
                try {
                    object = (V) in.readObject();
                } catch (ClassNotFoundException e) {
                    e.getCause();
                }
                if (object != null) {
                    list.add(object);
                } else cont = false;
            }
        } catch (IOException e) {
            e.getCause();
        }
        return list;
    }
    public V readObject(T file){
        V object = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.file.getPath()))) {
            return (V)in.readObject();
        } catch (IOException e) {
            e.getCause();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return object;
    }
}
