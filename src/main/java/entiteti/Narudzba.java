package entiteti;

import java.time.LocalDate;
import java.util.List;

public class Narudzba extends Entitet {

    private List<Popravak> popravci;

    private LocalDate pocetakPopravka;

    private long idKorisnika;

    public Narudzba(long id, List<Popravak> popravci, LocalDate pocetakPopravka, long idKorisnika) {
        super(id);
        this.popravci = popravci;
        this.pocetakPopravka = pocetakPopravka;
        this.idKorisnika = idKorisnika;
    }

    public List<Popravak> getPopravci() {
        return popravci;
    }

    public void setPopravci(List<Popravak> popravci) {
        this.popravci = popravci;
    }

    public LocalDate getPocetakPopravka() {
        return pocetakPopravka;
    }

    public void setPocetakPopravka(LocalDate pocetakPopravka) {
        this.pocetakPopravka = pocetakPopravka;
    }

    public long getIdKorisnika() {
        return idKorisnika;
    }

    public void setIdKorisnika(long idKorisnika) {
        this.idKorisnika = idKorisnika;
    }

    public static long trajanjePopravka(List<Popravak> popravak){
        long trajanje = 0;
        for(Popravak p: popravak){
            trajanje += p.getBrojDanaTrajanjaPopravka().duration;
        }
        return trajanje;
    }

    @Override
    public String toString() {
        return "Narudzba{" +
                "popravci=" + popravci +
                ", pocetakPopravka=" + pocetakPopravka +
                ", idKorisnika=" + idKorisnika +
                '}';
    }
}
