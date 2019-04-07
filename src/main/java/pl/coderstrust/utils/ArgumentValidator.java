package pl.coderstrust.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.coderstrust.controller.InvoiceController;

public class ArgumentValidator {

    private static Logger log = LoggerFactory.getLogger(InvoiceController.class);

    public static <T> void ensureNotNull(T argument, String paramName) {
        log.debug("Validating passed argument for null");
        if (argument == null) {
            log.error("{} cannot be null", paramName);
            throw new IllegalArgumentException(String.format("%s cannot be null", paramName));
        }
    }
}
