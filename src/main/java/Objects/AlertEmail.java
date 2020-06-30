package Objects;

public final class AlertEmail {
    private final int idEmail;
    private final String email;

    public AlertEmail(int idEmail, String email) {
        this.idEmail = idEmail;
        this.email = email;
    }
    public int getIdEmail() {
        return idEmail;
    }

    public String getEmail() {
        return email;
    }
}
