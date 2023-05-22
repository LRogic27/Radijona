package Sort;

import entiteti.Popravak;

import java.util.Comparator;

public class SortPopravci implements Comparator<Popravak> {


    @Override
    public int compare(Popravak o1, Popravak o2) {
        if(o1.equals(o2))
            return 0;
        else return o1.getCijena().compareTo(o2.getCijena());

    }

}
