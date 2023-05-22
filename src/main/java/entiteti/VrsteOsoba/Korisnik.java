package entiteti.VrsteOsoba;

import entiteti.Narudzba;
import entiteti.Osoba;
import entiteti.Radijona;

public non-sealed class Korisnik extends Osoba implements Radijona {



    private Narudzba narudzba;

    public Korisnik(long id,String username,Narudzba narudzba) {
        super(id,username);
        this.narudzba = narudzba;
    }
    public Korisnik(long id,String username,String password) {
        super(id,username,password);
    }
    public Korisnik(long id,String username) {
        super(id,username);
    }
    public Narudzba getNarudzba() {
        return narudzba;
    }

    public void setNarudzba(Narudzba narudzba) {
        this.narudzba = narudzba;
    }

    @Override
    public String toString() {
        return getUsername();
    }


}
