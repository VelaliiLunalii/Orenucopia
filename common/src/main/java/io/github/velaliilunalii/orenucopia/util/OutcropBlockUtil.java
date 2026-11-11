package io.github.velaliilunalii.orenucopia.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class OutcropBlockUtil {
    public static boolean isIn(Block[] list, BlockState state){
        if (state == null) return false;
        for (Block block : list){
            if (state.is(block)) return true;
        }
        return false;
    }
}
