package entiteti;

import java.math.BigDecimal;
import java.util.Objects;

public class Popravak extends Entitet {


    private BigDecimal cijena;
    private String nazivPopravka;
    private DurationOfRepair duration;

    public Popravak(long id,PopravakBuilder popravakBuilder) {
        super(id);
        this.cijena = popravakBuilder.cijena;
        this.nazivPopravka = popravakBuilder.nazivPopravka;
        this.duration = popravakBuilder.duration;
    }

    public static class PopravakBuilder{
        private BigDecimal cijena;
        private String nazivPopravka;
        private DurationOfRepair duration;

        public PopravakBuilder setCijenaBuilder(BigDecimal cijena){
            this.cijena = cijena;
            return this;
        }
        public PopravakBuilder setNazivPopravkaBuilder(String nazivPopravka){
            this.nazivPopravka = nazivPopravka;
            return this;
        }
        public PopravakBuilder setDurationBuilder(DurationOfRepair duration){
            this.duration = duration;
            return this;
        }
        public Popravak build(){
            return new Popravak(build().getId(),this);
        }
    }
    public BigDecimal getCijena() {
        return cijena;
    }

    public String getNazivPopravka() {
        return nazivPopravka;
    }

    public DurationOfRepair getBrojDanaTrajanjaPopravka() {
        return duration;
    }

    public void setBrojDanaTrajanjaPopravka(DurationOfRepair n) {
        duration = n;
    }

    public void setCijena(BigDecimal n) {
        cijena = n;
    }

    public void setNaziv(String n) {
        nazivPopravka = n;
    }

    @Override
    public String toString() {
        return "Popravak{" +
                "cijena=" + cijena +
                ", nazivPopravka='" + nazivPopravka + '\'' +
                ", brojDanaTrajanjaPopravka=" + duration +
                '}';
    }
}
