package Objects;

public class Measure {
    private int Id;
    private int iDUser;
    private double value;
    private String date;


    public Measure(int id, int iDUser, double value, String date) {
        this.Id=id;
        this.iDUser = iDUser;
        this.value = value;
        this.date = date;

    }

    public int getId() {
        return Id;
    }

    public int getiDUser() {
        return iDUser;
    }

    public double getValue() {
        return value;
    }

    public String getDate() {
        return date;
    }
}
