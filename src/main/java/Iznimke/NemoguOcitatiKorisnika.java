package Iznimke;

public class NemoguOcitatiKorisnika extends Exception{
    public NemoguOcitatiKorisnika() {
    }

    public NemoguOcitatiKorisnika(String message) {
        super(message);
    }

    public NemoguOcitatiKorisnika(String message, Throwable cause) {
        super(message, cause);
    }

    public NemoguOcitatiKorisnika(Throwable cause) {
        super(cause);
    }
}
