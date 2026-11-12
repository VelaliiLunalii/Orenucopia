package io.github.velaliilunalii.orenucopia.block;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SmallGrowingOutcropBlock extends GrowingOutcropBlock {
    public static final int MAX_AGE = 1;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

    @Override
    public int getMaxAge(){
        return MAX_AGE;
    }

    @Override
    protected float growthChance(){
        return (float) 1/40;
    }

    public SmallGrowingOutcropBlock(Properties properties) {
        super(properties);
    }
}
