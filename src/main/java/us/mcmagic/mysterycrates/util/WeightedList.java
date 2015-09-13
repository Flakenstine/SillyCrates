package us.mcmagic.mysterycrates.util;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class WeightedList<K> extends HashMap<K, Integer> {

    private int total;
    private static final ThreadLocalRandom rand = ThreadLocalRandom.current();

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
        int i = rand.nextInt(total);
        for (Entry<K, Integer> entry:entrySet()) {
            i -= entry.getValue();
            if (i < 0) return entry.getKey();
        }
        return null;
    }
}
