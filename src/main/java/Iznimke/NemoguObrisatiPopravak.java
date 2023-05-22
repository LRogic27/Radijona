package Iznimke;

public class NemoguObrisatiPopravak extends Exception{
    public NemoguObrisatiPopravak() {
    }

    public NemoguObrisatiPopravak(String message) {
        super(message);
    }

    public NemoguObrisatiPopravak(String message, Throwable cause) {
        super(message, cause);
    }

    public NemoguObrisatiPopravak(Throwable cause) {
        super(cause);
    }
}
