package io.github.velaliilunalii.orenucopia.item;

import io.github.velaliilunalii.orenucopia.CommonClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

import static io.github.velaliilunalii.orenucopia.block.OutcropBlock.*;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class OutcropSeedItem extends Item {

    public OutcropSeedItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        if (level.getBlockState(pos).isFaceSturdy(level, pos, direction)){
            BlockPos placePos = pos.relative(direction);
            if (level.getBlockState(placePos).canBeReplaced(new BlockPlaceContext(context))) {
                level.setBlock(placePos, CommonClass.SEED_BLOCK_MAP.get(this).defaultBlockState()
                        .setValue(AGE, 0)
                        .setValue(AGING, true)
                        .setValue(BlockStateProperties.FACING, direction)
                        .setValue(ROTATION_FACING, getRandomOrthogonalDirection(direction))
                        .setValue(WATERLOGGED, level.getFluidState(placePos).getType() == Fluids.WATER), 2);
                context.getItemInHand().shrink(1);
            }
        }
        context.getPlayer().swing(context.getHand());
        return super.useOn(context);
    }
}
