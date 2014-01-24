package org.springmock;

import org.springframework.stereotype.Component;

/**
 * @author Sergey Grigoriev
 */
@Component("service")
public class ServiceImpl implements Service {
    public String hello() {
        return "original";
    }
}
