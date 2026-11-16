package io.github.velaliilunalii.orenucopia.block;

import io.github.velaliilunalii.orenucopia.CommonClass;
import io.github.velaliilunalii.orenucopia.block.entity.EchoTreasureBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

import static io.github.velaliilunalii.orenucopia.block.GrowingOutcropBlock.AGE;
import static io.github.velaliilunalii.orenucopia.block.GrowingOutcropBlock.AGING;
import static io.github.velaliilunalii.orenucopia.block.OutcropBlock.ROTATION_FACING;
import static io.github.velaliilunalii.orenucopia.block.OutcropBlock.getRandomOrthogonalDirection;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class EchoTreasureBlock extends BaseEntityBlock {
    public EchoTreasureBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof EchoTreasureBlockEntity echoTreasureBlockEntity) {

            if (!level.isClientSide) {
                GrowingOutcropBlock outcrop = (GrowingOutcropBlock) echoTreasureBlockEntity.getBlock();

                BlockPos newPos;
                for (Direction direction : Direction.values()){
                    newPos = pos.relative(direction);
                    BlockState state = level.getBlockState(newPos);

                    if (state.is(BlockTags.BASE_STONE_OVERWORLD)){
                        level.setBlock(newPos, outcrop.defaultBlockState()
                                .setValue(AGE, 1)
                                .setValue(AGING, false)
                                .setValue(BlockStateProperties.FACING, direction.getOpposite())
                                .setValue(ROTATION_FACING, getRandomOrthogonalDirection(direction.getOpposite()))
                                .setValue(WATERLOGGED, false), 2);
                    }
                }
            }
        }
        super.playerWillDestroy(level, pos, blockState, player);
    }

    public static void setBlock(Level level, BlockPos blockPos, Block block){
        BlockEntity blockEntity = level.getBlockEntity(blockPos);

        if (blockEntity instanceof EchoTreasureBlockEntity echoTreasureBlockEntity) {
            if (!level.isClientSide) {
                echoTreasureBlockEntity.setBlock(block);
            }
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EchoTreasureBlockEntity(blockPos, blockState);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? createTickerHelper(blockEntityType, CommonClass.ECHO_TREASURE_BLOCK_ENTITY, EchoTreasureBlockEntity::tick) : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
