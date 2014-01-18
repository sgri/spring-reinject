package org.springmock;

/**
 * @author Sergey Grigoriev
 */
public class MockedService extends Service {
    @Override
    public String hello() {
        return "mocked";
    }
}
