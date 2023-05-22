package Iznimke;

public class NemoguDodatiPopravak extends Exception {

    public NemoguDodatiPopravak() {
    }

    public NemoguDodatiPopravak(String message) {
        super(message);
    }

    public NemoguDodatiPopravak(String message, Throwable cause) {
        super(message, cause);
    }

    public NemoguDodatiPopravak(Throwable cause) {
        super(cause);
    }
}
