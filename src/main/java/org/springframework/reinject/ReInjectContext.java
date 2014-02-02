package org.springframework.reinject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergey Grigoriev
 */
@Configuration
public class ReInjectContext {
    @Bean
    public MockInjectionPostProcessor mockInjectionPostProcessor() {
        return new MockInjectionPostProcessor();
    }
}
