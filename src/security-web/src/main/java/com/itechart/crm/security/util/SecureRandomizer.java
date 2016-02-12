package com.itechart.crm.security.util;

import java.util.Random;

public class SecureRandomizer implements Randomizer {
    private static final Random GENERATOR = new Random();

    @Override
    public long next() {
        return GENERATOR.nextLong();
    }
}
