package org.springmock;

import org.springframework.stereotype.Component;

/**
 * @author Sergey Grigoriev
 */
@Component
public class ServiceImpl implements Service {
    public String hello() {
        return "original";
    }
}
