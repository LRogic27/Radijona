package Datoteke;

import Baza.DataBase;
import Iznimke.NemoguOcitatiKorisnika;
import entiteti.VrsteOsoba.Admin;
import entiteti.VrsteOsoba.Korisnik;
import entiteti.Osoba;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;

public class Login {

    public static final Logger logger = LoggerFactory.getLogger(DataBase.class);

    public static String hashString(String password) {
        String hash = String.valueOf(password.hashCode());
        return hash;
    }

    public static Optional<Osoba> loginOsobe(String username, String password) {
        Osoba osoba;

        try (Scanner scanner = new Scanner(new File("src/main/java/Datoteke/korisnici"))) {
            while (scanner.hasNextLine()) {
                try {
                    var fields = scanner.nextLine().split(";");
                    if (fields.length != 4) {
                        throw new NemoguOcitatiKorisnika();
                    }
                    Integer ID = Integer.parseInt(fields[0]);
                    var user = fields[1];
                    var pass = fields[2];
                    var bool = fields[3];

                    if (username.equals(user) && String.valueOf(password.hashCode()).equals(pass)) {
                        if (bool.equals("TRUE")) {
                            osoba = new Admin(ID, username, password);
                            return Optional.of(osoba);
                        } else {
                            osoba = new Korisnik(ID, username, password);
                            return Optional.of(osoba);
                        }

                    }
                } catch (NemoguOcitatiKorisnika e) {
                    logger.info("Krivi zapis korisnika");
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

}
