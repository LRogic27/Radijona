package Iznimke;

import Baza.DataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class PogreskaPrilikomSpajanjaNaBazu extends Exception {
    public static final Logger logger = LoggerFactory.getLogger(DataBase.class);
    public PogreskaPrilikomSpajanjaNaBazu() {
        logger.error("Pogreska prilikom spajanja na bazu");
    }

    public PogreskaPrilikomSpajanjaNaBazu(String message) {
        super(message);

    }

    public PogreskaPrilikomSpajanjaNaBazu(String message, Throwable cause) {
        super(message, cause);
    }

    public PogreskaPrilikomSpajanjaNaBazu(Throwable cause) {
        super(cause);
    }

    public PogreskaPrilikomSpajanjaNaBazu(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
