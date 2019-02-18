package pl.coderstrust.generators;

import java.util.concurrent.atomic.AtomicLong;

public class CompanyIdGenerator {

    private static AtomicLong atomicLong = new AtomicLong(0);

    public static Long getNextId() {
        return atomicLong.incrementAndGet();
    }
}
