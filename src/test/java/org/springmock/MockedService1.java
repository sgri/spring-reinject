package org.springmock;

/**
 * @author Sergey Grigoriev
 */
public class MockedService1 extends ServiceImpl {
    @Override
    public String hello() {
        return "mock1";
    }
}
