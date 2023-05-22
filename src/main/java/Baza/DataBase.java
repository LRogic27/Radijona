package Baza;

import Datoteke.Login;
import Iznimke.NemoguObrisatiPopravak;
import Iznimke.NemoguOcitatiKorisnika;
import Iznimke.PogreskaPrilikomSpajanjaNaBazu;
import entiteti.DurationOfRepair;
import entiteti.Narudzba;
import entiteti.Popravak;
import entiteti.VrsteOsoba.Admin;
import entiteti.VrsteOsoba.Korisnik;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class DataBase {

    public static final Logger logger = LoggerFactory.getLogger(DataBase.class);

    private static Connection connectDataBase() throws PogreskaPrilikomSpajanjaNaBazu {
        Connection connection = null;
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("src/main/java/Baza/dataBase.properties"));
            String bazaPodatakaUrl = properties.getProperty("bazaPodatakaUrl");
            String userName = properties.getProperty("userName");
            String password = properties.getProperty("password");
            connection = DriverManager.getConnection(bazaPodatakaUrl, userName, password);
        } catch (SQLException e) {
            if (connection == null)
                throw new PogreskaPrilikomSpajanjaNaBazu();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static List<Korisnik> dohvatiKorisnike() {
        List<Korisnik> korisnici = new ArrayList<>();
        try {
            Connection connection = connectDataBase();
            if (connection != null) {
                logger.info("Uspjesno spojen na bazu. Dohvaća korisnike");
            } else throw new PogreskaPrilikomSpajanjaNaBazu(" dohvatiKorisnike()");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM KORISNIK");

            while (resultSet.next()) {
                long id = resultSet.getLong("ID");
                String username = resultSet.getString("USERNAME");
                Narudzba narudzba = DataBase.getNarudzba(id);
                Korisnik korisnik = new Korisnik(id, username, narudzba);
                korisnici.add(korisnik);
            }

            connection.close();
            logger.info("Konekcija sa bazom zatvorena");

        } catch (SQLException e) {
            e.getCause();
        } catch (PogreskaPrilikomSpajanjaNaBazu e) {
            throw new RuntimeException(e);
        }
        return korisnici;
    }

    public static void addKorisnik(Korisnik korisnik) {
        try {
            Connection connection = connectDataBase();
            if (connection != null) {
                logger.info("Uspjesno povezivanje na bazu u metodi addKorisnik");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO KORISNIK(USERNAME) VALUES(?)");
            preparedStatement.setString(1, korisnik.getUsername());
            preparedStatement.executeUpdate();

            try (FileWriter output = new FileWriter("src/main/java/Datoteke/korisnici", true)) {
                BufferedWriter bufferedWriter = new BufferedWriter(output);
                bufferedWriter.write(DataBase.getKorisnikIDByUsername(korisnik.getUsername()) + ";" + korisnik.getUsername() + ";" + Login.hashString(korisnik.getPassword()) + ";FALSE\n");
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (PogreskaPrilikomSpajanjaNaBazu e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getKorisnikIDByUsername(String username) {
        long id = 0;
        try {
            Connection connection = connectDataBase();
            if (connection != null) {
                logger.info("Uspjesno spojen na bazu. Dohvaća korisnike");
            } else throw new PogreskaPrilikomSpajanjaNaBazu(" dohvatiKorisnike()");

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM KORISNIK WHERE USERNAME = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getLong("ID");
                String name = username;
                return id;
            }

            connection.close();
            logger.info("Konekcija sa bazom zatvorena");

        } catch (SQLException e) {
            e.getCause();
        } catch (PogreskaPrilikomSpajanjaNaBazu e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    public static void addNarudzba(long idKorisnik) {
        try {
            Connection connection = connectDataBase();
            if (connection != null)
                logger.info("Povezan sa bazom u metoid addNarudzba");

            PreparedStatement statement = connection.prepareStatement("INSERT INTO NARUDZBA(KORISNIK_ID,POCETAK_POPRAVKA) VALUES(?,?)");
            statement.setLong(1, idKorisnik);
            statement.setDate(2, Date.valueOf(LocalDate.now()));
            statement.executeUpdate();

            connection.close();

        } catch (PogreskaPrilikomSpajanjaNaBazu e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Narudzba getNarudzba(long idKorisnik) {
        Narudzba narudzba = null;
        try {
            Connection connection = connectDataBase();
            if (connection != null) {
                logger.info("Konekcija sa bazom otvorena u metodi getNarudzba");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM NARUDZBA WHERE KORISNIK_ID = ?");
            preparedStatement.setLong(1, idKorisnik);
            ResultSet resultSet = preparedStatement.executeQuery();

            //ovaj if provjerava postoji li zapis u bazi koji zadovoljava uvjet
            if (!resultSet.isBeforeFirst()) {
                System.out.println("tu sam");
                DataBase.addNarudzba(idKorisnik);
                return DataBase.getNarudzba(idKorisnik);
            }

            while (resultSet.next()) {
                long id = resultSet.getLong("ID");
                LocalDate pocetak = resultSet.getDate("POCETAK_POPRAVKA").toLocalDate();
                return narudzba = new Narudzba(id, DataBase.getPopravci(id), pocetak, idKorisnik);
            }

            connection.close();
            logger.info("Konekcija sa bazom zatvorena");

        } catch (SQLException | PogreskaPrilikomSpajanjaNaBazu e) {
            throw new RuntimeException(e);
        }
        return narudzba;
    }

    public static List<Popravak> getPopravci(long idNarudzba) {
        List<Popravak> listaPopravaka = new ArrayList<>();
        try {
            Connection connection = connectDataBase();
            if (connection != null) {
                logger.info("Konekcija sa bazom otvorena u metodi getPopravci()");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM POPRAVAK WHERE NARUDZBA_ID = ?");
            preparedStatement.setLong(1, idNarudzba);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("ID");
                String naziv = resultSet.getString("NAZIV");
                BigDecimal cijena = BigDecimal.valueOf(resultSet.getDouble("CIJENA"));
                Integer trajanje = resultSet.getInt("TRAJANJE_POPRAVKA");
                listaPopravaka.add(new Popravak(id, new Popravak.PopravakBuilder().setCijenaBuilder(cijena).
                        setDurationBuilder(DurationOfRepair.getDurationByIntValue(trajanje)).setNazivPopravkaBuilder(naziv)));
            }

            connection.close();
            logger.info("Konekcija sa bazom zatvorena");

        } catch (SQLException | PogreskaPrilikomSpajanjaNaBazu e) {
            throw new RuntimeException(e);
        }
        return listaPopravaka;
    }

    public static Optional<Popravak> getVrstaPopravakByID(long id) {
        Popravak popravak;
        try {
            Connection connection = connectDataBase();
            if (connection != null) {
                logger.info("Konekcija sa bazom otvorena u metodi getVrstaPopravkaByID");
            }

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM VRSTA_POPRAVKA WHERE ID = ?");
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long idPopravka = resultSet.getLong("ID");
                String naziv = resultSet.getString("NAZIV");
                BigDecimal cijena = BigDecimal.valueOf(resultSet.getDouble("CIJENA"));
                Integer trajanjePopravka = resultSet.getInt("TRAJANJE_POPRAVKA");
                popravak = new Popravak(idPopravka, new Popravak.PopravakBuilder().setCijenaBuilder(cijena).
                        setDurationBuilder(DurationOfRepair.getDurationByIntValue(trajanjePopravka)).setNazivPopravkaBuilder(naziv));
                return Optional.ofNullable(popravak);
            }

            connection.close();
            logger.info("Konekcija sa bazom zatvorena");

        } catch (SQLException | PogreskaPrilikomSpajanjaNaBazu e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public static void azurirajVrstuPopravka(long id, BigDecimal cijena, String nazivPopravka, Integer trajanjePopravka) {
        try {
            Connection connection = connectDataBase();

            if (connection != null) {
                logger.info("Konekcija sa bazom otvorena u metodi azurirajVrstuPopravka");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE VRSTA_POPRAVKA SET NAZIV = ?, CIJENA = ?, TRAJANJE_POPRAVKA = ? WHERE ID = ?");
            preparedStatement.setString(1, nazivPopravka);
            preparedStatement.setDouble(2, Double.valueOf(cijena.doubleValue()));
            preparedStatement.setInt(3, trajanjePopravka);
            preparedStatement.setLong(4, id);

            preparedStatement.executeUpdate();

            connection.close();
            logger.info("Konekcija sa bazom zatvorena");

        } catch (SQLException | PogreskaPrilikomSpajanjaNaBazu e) {
            e.getCause();
        }
    }

    public static void dodajPopravak(long idNarudzba, long idPopravka) {
        try {
            Connection connection = connectDataBase();
            if (connection != null) {
                logger.info("Konekcija sa bazom otvorena u metodi dodajPopravak");
            }

            Optional<Popravak> popravak = DataBase.getVrstaPopravakByID(idPopravka);
            if (popravak.isPresent()) {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO POPRAVAK(NARUDZBA_ID,NAZIV,CIJENA,TRAJANJE_POPRAVKA) VALUES(?,?,?,?)");
                preparedStatement.setLong(1, idNarudzba);
                preparedStatement.setString(2, popravak.get().getNazivPopravka());
                preparedStatement.setDouble(3, popravak.get().getCijena().doubleValue());
                preparedStatement.setInt(4, popravak.get().getBrojDanaTrajanjaPopravka().duration);
                preparedStatement.executeUpdate();
            }

            connection.close();
            logger.info("Konekcija sa bazom zatvorena");

        } catch (SQLException | PogreskaPrilikomSpajanjaNaBazu e) {
            System.out.println("Greska kod gumba dodaj " + idNarudzba);
        }
    }

    public static void obrisiKorisnika(long idKorisnika) {
        try {
            Connection connection = connectDataBase();
            if (connection != null) {
                logger.info("Konekcija sa bazom otvorena u metodi obrisiKorisnika");
            }
            DataBase.obrisiKorisnikaTxt(idKorisnika);
            DataBase.obrisiNarudzbu(idKorisnika);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE KORISNIK WHERE ID = ?");
            preparedStatement.setLong(1, idKorisnika);
            preparedStatement.executeUpdate();

            connection.close();
            logger.info("Konekcija sa bazom zatvorena");

        } catch (SQLException | PogreskaPrilikomSpajanjaNaBazu | IOException e) {
            throw new RuntimeException(e);
        }
    }

    //ova metoda treba obrisat korisnika iz txt datoteke ne radi
    public static void obrisiKorisnikaTxt(long idKorisnika) throws IOException {
        String korisnik = DataBase.getKorisnik(idKorisnika).getUsername();
        System.out.println("korisnik:"+korisnik+":kraj");
        File file1 = new File("src/main/java/Datoteke/korisnici");
        File file2 = new File("src/main/java/Datoteke/korisnici.new");

        file2.createNewFile();
        try {
            Scanner korisnikFile = new Scanner(file1);
            FileWriter fileWriter = new FileWriter(file2);

            while (korisnikFile.hasNextLine()) {
                String[] line = korisnikFile.nextLine().split(";");
                System.out.println(line[1]);

                if (!line[1].equals(korisnik)) {

                    fileWriter.write(line[0] + ";" + line[1] + ";" + line[2] + ";" + line[3]+"\n");
                }
            }
            korisnikFile.close();
            fileWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        file1.delete();
        file2.renameTo(file1);
        //Files.move(file2.toPath(),file1.toPath());

    }

    public static Korisnik getKorisnik(long idKorisnika){
        try {
            Connection connection = connectDataBase();
            if (connection != null) {
                logger.info("Konekcija sa bazom otvorena u metodi getKorisnik()");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM KORISNIK WHERE ID = ?");
            preparedStatement.setLong(1, idKorisnika);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String username = resultSet.getString("USERNAME");
                Narudzba narudzba = DataBase.getNarudzba(idKorisnika);
                return new Korisnik(idKorisnika,username,narudzba);
            }

            connection.close();
            logger.info("Konekcija sa bazom zatvorena");

        } catch (SQLException | PogreskaPrilikomSpajanjaNaBazu e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public static void obrisiNarudzbu(long idKorisnika) {
        long narudzbaID = 0;
        try {
            Connection connection = connectDataBase();
            if (connection != null) {
                logger.info("Konekcija sa bazom otvorena u metodi obrisiKorisnika");
            }
            PreparedStatement p = connection.prepareStatement("SELECT * FROM NARUDZBA WHERE KORISNIK_ID = ?");
            p.setLong(1, idKorisnika);
            ResultSet resultSet = p.executeQuery();
            ;
            while (resultSet.next()) {
                narudzbaID = resultSet.getLong("ID");
            }
            PreparedStatement prepared = connection.prepareStatement("DELETE POPRAVAK WHERE NARUDZBA_ID = ?");
            prepared.setLong(1, narudzbaID);
            prepared.executeUpdate();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE NARUDZBA WHERE KORISNIK_ID = ?");
            preparedStatement.setLong(1, idKorisnika);
            preparedStatement.executeUpdate();

            connection.close();
            logger.info("Konekcija sa bazom zatvorena");
        } catch (SQLException | PogreskaPrilikomSpajanjaNaBazu e) {
            throw new RuntimeException(e);
        }
    }

    public static void obrisiNarudzbuByID(long idNarudzba) {
        long narudzbaID = 0;
        try {
            Connection connection = connectDataBase();
            if (connection != null) {
                logger.info("Konekcija sa bazom otvorena u metodi obrisiKorisnika");
            }
            PreparedStatement p = connection.prepareStatement("SELECT * FROM NARUDZBA WHERE ID = ?");
            p.setLong(1, idNarudzba);
            ResultSet resultSet = p.executeQuery();

            while (resultSet.next()) {
                narudzbaID = resultSet.getLong("ID");
            }

            PreparedStatement prepared = connection.prepareStatement("DELETE POPRAVAK WHERE NARUDZBA_ID = ?");
            prepared.setLong(1, narudzbaID);
            prepared.executeUpdate();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE NARUDZBA WHERE ID = ?");
            preparedStatement.setLong(1, idNarudzba);
            preparedStatement.executeUpdate();

            connection.close();
            logger.info("Konekcija sa bazom zatvorena");

        } catch (SQLException | PogreskaPrilikomSpajanjaNaBazu e) {
            throw new RuntimeException(e);
        }
    }

    public static void obrisiPopravak(long idPopravka) {
        try {
            Connection connection = connectDataBase();
            if (connection != null) {
                logger.info("Konekcija sa bazom otvorena u metodi obrisiPopravak");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("DELETE POPRAVAK WHERE ID = ?");
            preparedStatement.setLong(1, idPopravka);
            preparedStatement.executeUpdate();

            connection.close();
            logger.info("Konekcija sa bazom zatvorena");

        } catch (SQLException | PogreskaPrilikomSpajanjaNaBazu e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Popravak> priceList() {
        List<Popravak> lista = new ArrayList<>();
        try {
            Connection connection = connectDataBase();
            if (connection != null) {
                logger.info("Konekcija sa bazom otvorena u metodi getVrstaPopravkaByID");
            }

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM VRSTA_POPRAVKA");

            while (resultSet.next()) {
                long idPopravka = resultSet.getLong("ID");
                String naziv = resultSet.getString("NAZIV");
                BigDecimal cijena = BigDecimal.valueOf(resultSet.getDouble("CIJENA"));
                Integer trajanjePopravka = resultSet.getInt("TRAJANJE_POPRAVKA");
                Popravak pom = new Popravak(idPopravka, new Popravak.PopravakBuilder().setCijenaBuilder(cijena).
                        setDurationBuilder(DurationOfRepair.getDurationByIntValue(trajanjePopravka)).setNazivPopravkaBuilder(naziv));
                lista.add(pom);
            }

            connection.close();
            logger.info("Konekcija sa bazom zatvorena");

        } catch (SQLException | PogreskaPrilikomSpajanjaNaBazu e) {
            e.getCause();
        }
        return lista;
    }
}
