package org.springmock;

/**
 * @author Sergey Grigoriev
 */
public class MockedService2 implements Service {
    @Override
    public String hello() {
        return "mock2";
    }

}
