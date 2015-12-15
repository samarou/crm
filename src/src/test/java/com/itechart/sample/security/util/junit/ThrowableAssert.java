package com.itechart.sample.security.util.junit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

/**
 * Utility class for testing that method throws exceptions
 *
 * @author andrei.samarou
 */
public class ThrowableAssert {

    private final Throwable throwable;

    private ThrowableAssert(Throwable throwable) {
        this.throwable = throwable;
    }

    public static ThrowableAssert assertThrown(Callable callable) {
        return assertThrown(null, callable);
    }

    public static ThrowableAssert assertThrown(Class<? extends Throwable> expectedException, Callable callable) {
        try {
            callable.call();
        } catch (Throwable throwable) {
            ThrowableAssert ta = new ThrowableAssert(throwable);
            if (expectedException != null) {
                ta.isInstanceOf(expectedException);
            }
            return ta;
        }
        if (expectedException != null) {
            throw new AssertionError("Expected exception of type " + expectedException);
        }
        throw new AssertionError("Expected any exception");
    }

    @SuppressWarnings("unchecked")
    public ThrowableAssert isInstanceOf(Class<? extends Throwable> expectedException) {
        assertThat(throwable, isA((Class<Throwable>) expectedException));
        return this;
    }

    public ThrowableAssert hasMessage(String expectedMessage) {
        assertThat(throwable.getMessage(), equalTo(expectedMessage));
        return this;
    }

    @FunctionalInterface
    public interface Callable {
        void call() throws Throwable;
    }
}