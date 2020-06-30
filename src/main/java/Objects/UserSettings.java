package Objects;

public class UserSettings {
    private double growth;
    private boolean isWhomen;
    private int age;
    private String alertValue;
    private boolean isAlertWarning;
    private String alertEmailTitle;
    private String bodyMassFormule;
    private String ppmMassFormule;
    private String dateOfBirth;

    public String getAlertEmailTitle() {
        return alertEmailTitle;
    }

    public String getBodyMassFormule() {
        return bodyMassFormule;
    }

    public String getPpmMassFormule() {
        return ppmMassFormule;
    }

    public UserSettings(double growth, boolean isWhomen, int age, String alertValue, boolean isAlertWarning, String alertEmailTitle, String bodyMassFormule, String ppmMassFormule, String dateOfBirth) {
        this.growth = growth;
        this.isWhomen = isWhomen;
        this.age = age;
        this.alertValue = alertValue;
        this.isAlertWarning = isAlertWarning;
        this.alertEmailTitle = alertEmailTitle;
        this.bodyMassFormule = bodyMassFormule;
        this.ppmMassFormule = ppmMassFormule;
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAlertValue() {
        return alertValue;
    }

    public double getGrowth() {
        return growth;
    }

    public void setGrowth(double growth) {
        this.growth = growth;
    }

    public boolean isWhomen() {
        return isWhomen;
    }

    public void setWhomen(boolean whomen) {
        isWhomen = whomen;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isAlertWarning() {
        return isAlertWarning;
    }
}
