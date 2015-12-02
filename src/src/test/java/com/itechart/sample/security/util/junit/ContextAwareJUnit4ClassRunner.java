package com.itechart.sample.security.util.junit;

import org.junit.runners.model.InitializationError;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Creates unit tests in spring beans context.
 * That allow wire, intercept and post-process unit tests with spring beans
 *
 * @author andrei.samarou
 */
public class ContextAwareJUnit4ClassRunner extends SpringJUnit4ClassRunner {

    public ContextAwareJUnit4ClassRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected TestContextManager createTestContextManager(Class<?> clazz) {
        return new InnerTestContextManager(clazz);
    }

    @Override
    protected Object createTest() throws Exception {
        TestContext testContext = ((InnerTestContextManager) getTestContextManager()).getAccessibleTestContext();
        AutowireCapableBeanFactory beanFactory = testContext.getApplicationContext().getAutowireCapableBeanFactory();
        Object testInstance = beanFactory.createBean(getTestClass().getJavaClass());
        getTestContextManager().prepareTestInstance(testInstance);
        return testInstance;
    }

    /**
     * Extends TestContextManager that gives an access to protected TestContext
     */
    private static class InnerTestContextManager extends TestContextManager {

        public InnerTestContextManager(Class<?> testClass) {
            super(testClass);
        }

        public TestContext getAccessibleTestContext() {
            return getTestContext();
        }
    }

}
