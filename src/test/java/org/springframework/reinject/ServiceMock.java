package org.springframework.reinject;

/**
 * @author Sergey Grigoriev
 */
public class ServiceMock extends ServiceImpl {
    @Override
    public String hello() {
        return "mock1";
    }
}
