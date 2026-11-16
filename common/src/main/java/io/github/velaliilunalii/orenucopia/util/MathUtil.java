package io.github.velaliilunalii.orenucopia.util;

import net.minecraft.util.RandomSource;

public class MathUtil {
    public static int noLowerBoundsRandomInt(RandomSource randomSource, int bound){
        return bound < 1 ? 0 : randomSource.nextInt(bound);
    }
}
