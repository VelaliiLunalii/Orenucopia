package io.github.velaliilunalii.orenucopia.block;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class LargeOutcropBlock extends OutcropBlock {
    public static final int MAX_AGE = 2;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

    @Override
    public int getMaxAge(){
        return 2;
    }

    public LargeOutcropBlock(Properties properties) {
        super(properties);
    }
}
