package Iznimke;

import Baza.DataBase;
import entiteti.Entitet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PogreskaLogin extends Exception {
    public static final Logger logger = LoggerFactory.getLogger(DataBase.class);
    public PogreskaLogin() {
        logger.error("Pogreska prilikom login-a");
    }

    public PogreskaLogin(String message) {
        super(message);
    }

    public PogreskaLogin(String message, Throwable cause) {
        super(message, cause);
    }

    public PogreskaLogin(Throwable cause) {
        super(cause);
    }

    public PogreskaLogin(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
