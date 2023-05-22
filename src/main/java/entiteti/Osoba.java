package entiteti;

public class Osoba extends Entitet {

    private String username;
    private String password;

    public Osoba(long id, String username, String password) {
        super(id);
        this.username = username;
        this.password = password;
    }
    public Osoba(long id, String username) {
        super(id);
        this.username = username;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "Osoba{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
