package io.github.velaliilunalii.orenucopia.block;

import io.github.velaliilunalii.orenucopia.CommonClass;
import io.github.velaliilunalii.orenucopia.block.entity.EchoTreasureBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

import java.util.Arrays;
import java.util.List;

import static io.github.velaliilunalii.orenucopia.block.GrowingOutcropBlock.AGE;
import static io.github.velaliilunalii.orenucopia.block.GrowingOutcropBlock.AGING;
import static io.github.velaliilunalii.orenucopia.block.OutcropBlock.ROTATION_FACING;
import static io.github.velaliilunalii.orenucopia.block.OutcropBlock.getRandomOrthogonalDirection;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class EchoTreasureBlock extends BaseEntityBlock {
    public EchoTreasureBlock(Properties $$0) {
        super($$0);
    }

    private final List<Direction> horizontalDirections = List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof EchoTreasureBlockEntity echoTreasureBlockEntity) {
            if(!level.isClientSide){
                RandomSource randomSource = player.getRandom();
                GrowingOutcropBlock outcrop = (GrowingOutcropBlock) echoTreasureBlockEntity.getBlock();

                BlockPos newPos;
                for (Direction direction : Direction.values()){
                    newPos = pos.relative(direction);
                    BlockState state = level.getBlockState(newPos.relative(direction));

                    if (randomSource.nextFloat() < 0.75 && state.is(BlockTags.BASE_STONE_OVERWORLD)) {
                        placeDirectionalOutcrop(level, newPos, direction, outcrop);
                    }
                }

                BlockPos below = pos.below();
                for (Direction direction : horizontalDirections){
                    if (randomSource.nextFloat() < 0.25){
                        newPos = below.relative(direction);
                        BlockState state = level.getBlockState(newPos.relative(direction));

                        if (state.is(BlockTags.BASE_STONE_OVERWORLD)) {
                            placeDirectionalOutcrop(level, newPos, direction, outcrop);
                        }
                    }
                }
                BlockPos above = pos.above();
                for (Direction direction : horizontalDirections){
                    if (randomSource.nextFloat() < 0.25){
                        newPos = above.relative(direction);
                        BlockState state = level.getBlockState(newPos.relative(direction));

                        if (state.is(BlockTags.BASE_STONE_OVERWORLD)) {
                            placeDirectionalOutcrop(level, newPos, direction, outcrop);
                        }

                        Direction rotatedDirection = direction;
                        if (randomSource.nextFloat() < 0.5){
                            rotatedDirection = rotatedDirection.getClockWise();
                            newPos = newPos.relative(rotatedDirection);
                            state = level.getBlockState(newPos.relative(direction));

                            if (randomSource.nextFloat() < 0.5 && state.is(BlockTags.BASE_STONE_OVERWORLD)) {
                                placeDirectionalOutcrop(level, newPos, rotatedDirection, outcrop);
                            }

                            rotatedDirection = rotatedDirection.getClockWise();
                            newPos = newPos.relative(rotatedDirection);
                            state = level.getBlockState(newPos.relative(direction));

                            if (randomSource.nextFloat() < 0.5 && state.is(BlockTags.BASE_STONE_OVERWORLD)) {
                                placeDirectionalOutcrop(level, newPos, rotatedDirection, outcrop);
                            }
                        }
                    }
                }
            }else {
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), CommonClass.CHIME, SoundSource.BLOCKS, 1.0F, 1F, false);
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), CommonClass.CRUMBLE, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.2F + 0.8F, false);
            }
        }
        super.playerWillDestroy(level, pos, blockState, player);
    }

    public static void placeDirectionalOutcrop(Level level, BlockPos pos, Direction direction, GrowingOutcropBlock outcrop){
        RandomSource randomSource = level.random;
        level.setBlock(pos, outcrop.defaultBlockState()
                .setValue(AGE, randomSource.nextFloat() < 0.75 ? 1 : Math.min(outcrop.getMaxAge(), 2))
                .setValue(AGING, false)
                .setValue(BlockStateProperties.FACING, direction.getOpposite())
                .setValue(ROTATION_FACING, getRandomOrthogonalDirection(direction.getOpposite()))
                .setValue(WATERLOGGED, false), 2);
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
        return createTickerHelper(blockEntityType, CommonClass.ECHO_TREASURE_BLOCK_ENTITY, EchoTreasureBlockEntity::tick);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
