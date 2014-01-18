package org.springmock;

import org.springframework.stereotype.Component;

/**
 * @author Sergey Grigoriev
 */
@Component
public class Service {
    public String hello() {
        return "original";
    }
}
