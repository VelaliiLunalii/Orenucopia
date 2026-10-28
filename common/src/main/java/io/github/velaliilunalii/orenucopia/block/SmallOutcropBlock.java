package io.github.velaliilunalii.orenucopia.block;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SmallOutcropBlock extends OutcropBlock {
    public static final int MAX_AGE = 1;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

    @Override
    public int getMaxAge(){
        return 1;
    }

    @Override
    protected float growthChance(){
        return (float) 1/40;
    }

    public SmallOutcropBlock(Properties properties) {
        super(properties);
    }
}
