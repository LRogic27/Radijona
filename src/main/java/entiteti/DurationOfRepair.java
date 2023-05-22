package entiteti;

public enum DurationOfRepair {

    DAY(1,"Day"),
    TWO_DAYS(2,"Two days"),
    FIVE_DAYS(5,"Five days"),
    WEEK(7,"Week"),
    TEN_DAYS(10,"Ten days");

    public final Integer duration;
    public final String stringValue;
    private DurationOfRepair(Integer integer,String s){
        this.duration = integer;
        this.stringValue = s;
    }

    public static DurationOfRepair getDurationByIntValue(Integer value){

        if(value == 1)
            return DurationOfRepair.DAY;
        if(value == 2)
            return DurationOfRepair.TWO_DAYS;
        if(value == 5)
            return DurationOfRepair.FIVE_DAYS;
        if(value == 7)
            return DurationOfRepair.WEEK;
        else return DurationOfRepair.TEN_DAYS;
    }
    public static DurationOfRepair getDurationByString(String value){

        if(value.equals("Day"))
            return DurationOfRepair.DAY;
        if(value.equals("Two days"))
            return DurationOfRepair.TWO_DAYS;
        if(value.equals("Five days"))
            return DurationOfRepair.FIVE_DAYS;
        if(value.equals("Week"))
            return DurationOfRepair.WEEK;
        else return DurationOfRepair.TEN_DAYS;
    }
}
