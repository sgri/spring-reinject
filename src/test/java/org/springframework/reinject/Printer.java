package org.springframework.reinject;

import javax.inject.Inject;

/**
 * @author Sergey Grigoriev
 */
public class Printer {
    private @Inject Service service;
    public String print() {
        return service.hello();
    }
}
