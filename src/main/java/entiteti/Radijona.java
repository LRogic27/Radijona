package entiteti;

import entiteti.VrsteOsoba.Korisnik;

import java.math.BigDecimal;
import java.util.List;

public sealed interface Radijona permits Korisnik {

        default BigDecimal izracunajCijenuPopravka(Narudzba narudzba){
            BigDecimal bigDecimal = BigDecimal.ZERO;
            for(Popravak popravak:narudzba.getPopravci()){
                bigDecimal = bigDecimal.add(popravak.getCijena());

            }
            return bigDecimal;
        }

        default String napisiRacun(Korisnik korisnik){
            String racun = "";
            racun = "Racun za korisnika (ID:"+korisnik.getId()+" Username: "+korisnik.getUsername()+")\n";
            racun = racun +"Narudzba ID: " + korisnik.getNarudzba().getId()+"\n";
                for(Popravak popravak : korisnik.getNarudzba().getPopravci()){
                    racun = racun + "\t"+ popravak.getNazivPopravka()+"\n";
                }

            racun = racun+"\n.........................................\n\t\t"
                    +izracunajCijenuPopravka(korisnik.getNarudzba())+"$";

            return racun;
        }

}
