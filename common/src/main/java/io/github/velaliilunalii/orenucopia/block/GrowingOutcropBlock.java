package io.github.velaliilunalii.orenucopia.block;

import io.github.velaliilunalii.orenucopia.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

import static io.github.velaliilunalii.orenucopia.CommonClass.*;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public abstract class GrowingOutcropBlock extends OutcropBlock {
    public static final int MAX_AGE = 2;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);
    public static final BooleanProperty AGING = BooleanProperty.create("aging");

    public GrowingOutcropBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any())
                .setValue(AGE, 1)
                .setValue(AGING, true)
                .setValue(FACING, Direction.SOUTH)
                .setValue(ROTATION_FACING, Direction.EAST)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE, AGING, FACING, ROTATION_FACING, WATERLOGGED});
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos());
        if (blockState.is(this)) {
            return (BlockState)blockState;
        } else {
            return (BlockState)super.getStateForPlacement(blockPlaceContext)
                    .setValue(AGE, 0)
                    .setValue(AGING, true);
        }
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextFloat() < 0.2) {
            if (blockState.is(REDSTONE_OUTCROP)) {
                double d = (double) blockPos.getX() + randomSource.nextDouble();
                double e = (double) blockPos.getY() + randomSource.nextDouble();
                double f = (double) blockPos.getZ() + randomSource.nextDouble();
                level.addParticle(DustParticleOptions.REDSTONE, d, e, f, (double) 0.0F, (double) 0.0F, (double) 0.0F);
            }
            if (blockState.is(GOLD_OUTCROP) || blockState.is(DIAMOND_OUTCROP) || blockState.is(EMERALD_OUTCROP) || blockState.is(NETHER_GOLD_OUTCROP)) {
                double d = (double) blockPos.getX() + randomSource.nextDouble();
                double e = (double) blockPos.getY() + randomSource.nextDouble();
                double f = (double) blockPos.getZ() + randomSource.nextDouble();
                level.addParticle(ParticleTypes.ELECTRIC_SPARK, d, e, f, (double) 0.0F, (double) 0.0F, (double) 0.0F);
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return true;
    }

    public int getMaxAge(){
        return MAX_AGE;
    }

    protected float growthChance(){
        return (float) 1/20;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.randomTick(blockState, serverLevel, blockPos, randomSource);
        int age = blockState.getValue(AGE);
        int max_age = getMaxAge();

        if (randomSource.nextFloat() < growthChance() && blockState.getValue(AGING) && age < max_age){
            String[] outcrop_name = blockState.getBlock().getDescriptionId().split("\\.");
            boolean test = serverLevel.getBlockState(blockPos.relative(blockState.getValue(FACING).getOpposite())).is(ModTags.Blocks.tag(outcrop_name[outcrop_name.length - 1].concat("_bases")));
            if (test || age < max_age - 1){
                serverLevel.setBlock(blockPos, blockState.setValue(AGE, age + 1), 2);
            }
        }
    }

    protected static final VoxelShape SEED_NORTH_SHAPE = Block.box(6.0, 6.0, 15.0, 10.0, 10.0, 16.0);
    protected static final VoxelShape SEED_SOUTH_SHAPE = Block.box(6.0, 6.0, 0.0, 10.0, 10.0, 1.0);
    protected static final VoxelShape SEED_EAST_SHAPE = Block.box(0.0, 6.0, 6.0, 1.0, 10.0, 10.0);
    protected static final VoxelShape SEED_WEST_SHAPE = Block.box(15.0, 6.0, 6.0, 16.0, 10.0, 10.0);
    protected static final VoxelShape SEED_UP_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 1.0, 10.0);
    protected static final VoxelShape SEED_DOWN_SHAPE = Block.box(6.0, 15.0, 6.0, 10.0, 16.0, 10.0);

    protected static final VoxelShape SMALL_NORTH_SHAPE = Block.box(5.0, 5.0, 8.0, 11.0, 11.0, 16.0);
    protected static final VoxelShape SMALL_SOUTH_SHAPE = Block.box(5.0, 5.0, 0.0, 11.0, 11.0, 8.0);
    protected static final VoxelShape SMALL_EAST_SHAPE = Block.box(0.0, 5.0, 5.0, 8.0, 11.0, 11.0);
    protected static final VoxelShape SMALL_WEST_SHAPE = Block.box(8.0, 5.0, 5.0, 16.0, 11.0, 11.0);
    protected static final VoxelShape SMALL_UP_SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 8.0, 11.0);
    protected static final VoxelShape SMALL_DOWN_SHAPE = Block.box(5.0, 8.0, 5.0, 11.0, 16.0, 11.0);

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        Direction $$4 = blockState.getValue(FACING);
        int age = blockState.getValue(AGE);

        if (age == 0){
            return switch ($$4) {
                case NORTH -> SEED_NORTH_SHAPE;
                case SOUTH -> SEED_SOUTH_SHAPE;
                case EAST -> SEED_EAST_SHAPE;
                case WEST -> SEED_WEST_SHAPE;
                case UP -> SEED_UP_SHAPE;
                default -> SEED_DOWN_SHAPE;
            };
        }else if (age == 1){
            return switch ($$4) {
                case NORTH -> SMALL_NORTH_SHAPE;
                case SOUTH -> SMALL_SOUTH_SHAPE;
                case EAST -> SMALL_EAST_SHAPE;
                case WEST -> SMALL_WEST_SHAPE;
                case UP -> SMALL_UP_SHAPE;
                default -> SMALL_DOWN_SHAPE;
            };
        }else {
            return super.getShape(blockState, $$1, $$2, $$3);
        }
    }
}
