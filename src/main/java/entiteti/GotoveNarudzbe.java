package entiteti;

import Threads.FinishedOrderThread;

import java.util.List;

public class GotoveNarudzbe<T> extends GotoveNarudzbeABS{

    private T imeKorisnika;
    private Narudzba narudzba;

    public T getImeKorisnika() {
        return imeKorisnika;
    }

    public void setImeKorisnika(T imeKorisnika) {
        this.imeKorisnika = imeKorisnika;
    }

    public Narudzba getNarudzba() {
        return narudzba;
    }

    public void setNarudzba(Narudzba narudzba) {
        this.narudzba = narudzba;
    }

    public GotoveNarudzbe(T imeKorisnika, Narudzba narudzba) {
        this.imeKorisnika = imeKorisnika;
        this.narudzba = narudzba;
    }

    @Override
    public List<GotoveNarudzbe<String>> dohvatiGotoveNarudzbe() {
        List<GotoveNarudzbe<String>> list = FinishedOrderThread.getOrders();
        return list;
    }
}
