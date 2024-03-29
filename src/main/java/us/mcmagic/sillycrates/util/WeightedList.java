package us.mcmagic.sillycrates.util;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class WeightedList<K> extends HashMap<K, Integer> {

    private int total;
    private static final SecureRandom random = new SecureRandom();

    @Override
    public Integer put(K a, Integer b) {
        Integer i = super.put(a, Math.abs(b));
        total = 0;
        for (Integer in : values()) {
            total += in;
        }
        return i;
    }

    public K get() {
        if (total <= 0) return null;
        int i = random.nextInt(total);
        for (Entry<K, Integer> entry:entrySet()) {
            i -= entry.getValue();
            if (i < 0) return entry.getKey();
        }
        return null;
    }
}
