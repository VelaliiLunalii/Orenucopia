package io.github.velaliilunalii.orenucopia.block;

import io.github.velaliilunalii.orenucopia.CommonClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import static io.github.velaliilunalii.orenucopia.util.ItemStackUtil.getTreasurePos;
import static net.minecraft.world.item.Items.ECHO_SHARD;
import static net.minecraft.world.level.block.Blocks.DEEPSLATE;
import static net.minecraft.world.level.block.Blocks.STONE;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class EchoTreasureBlock extends RotatedPillarBlock {

    public EchoTreasureBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, BlockEntity $$4, ItemStack $$5) {
        super.playerDestroy(level, player, blockPos, blockState, $$4, $$5);

//        Block outcrop = CommonClass.randomOutcrop();
//        BlockPos newPos;
//        for (Direction direction : Direction.values()){
//            newPos = blockPos.relative(direction);
//            BlockState state = level.getBlockState(newPos);
//
//            if (state.is(DEEPSLATE) || state.is(STONE)){
//                level.setBlock(newPos, outcrop.defaultBlockState()
//                        .setValue(AGE, 1)
//                        .setValue(AGING, false)
//                        .setValue(BlockStateProperties.FACING, direction.getOpposite())
//                        .setValue(ROTATION_FACING, getRandomOrthogonalDirection(direction.getOpposite()))
//                        .setValue(WATERLOGGED, false), 2);
//            }
//        }
    }
}
