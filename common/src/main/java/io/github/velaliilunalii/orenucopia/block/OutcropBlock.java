package io.github.velaliilunalii.orenucopia.block;

import io.github.velaliilunalii.orenucopia.CommonClass;
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

public abstract class OutcropBlock extends Block implements SimpleWaterloggedBlock {
    public static final int MAX_AGE = 2;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);
    public static final BooleanProperty AGING = BooleanProperty.create("aging");
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final DirectionProperty ROTATION_FACING = DirectionProperty.create("rotation_facing",
            new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN});

    public OutcropBlock(Properties properties) {
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
            FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
            boolean bl = fluidState.getType() == Fluids.WATER;
            return (BlockState)super.getStateForPlacement(blockPlaceContext)
                    .setValue(AGE, 0)
                    .setValue(AGING, true)
                    .setValue(WATERLOGGED, bl)
                    .setValue(FACING, blockPlaceContext.getClickedFace())
                    .setValue(ROTATION_FACING, getRandomOrthogonalDirection(blockPlaceContext.getClickedFace()));
        }
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return (Boolean)blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    public boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return !blockState.getCollisionShape(blockGetter, blockPos).getFaceShape(direction).isEmpty() || blockState.isFaceSturdy(blockGetter, blockPos, direction);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        Direction direction = blockState.getValue(FACING);
        BlockPos blockPos2 = blockPos.relative(direction.getOpposite());
        return this.mayPlaceOn(levelReader.getBlockState(blockPos2), levelReader, blockPos2, direction);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return (BlockState)blockState.setValue(FACING, rotation.rotate((Direction)blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation((Direction)blockState.getValue(FACING)));
    }

    public static Direction[] xOrthogonal = {Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH};
    public static Direction[] yOrthogonal = {Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH};
    public static Direction[] zOrthogonal = {Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST};

    public static Direction getRandomOrthogonalDirection(Direction direction){
        int random = new Random().nextInt(4);
        if (direction.getAxis().equals(Direction.Axis.X)) return xOrthogonal[random];
        if (direction.getAxis().equals(Direction.Axis.Y)) return yOrthogonal[random];
        return zOrthogonal[random];
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
            if (blockState.is(GOLD_OUTCROP) || blockState.is(DIAMOND_OUTCROP) || blockState.is(EMERALD_OUTCROP)) {
                double d = (double) blockPos.getX() + randomSource.nextDouble();
                double e = (double) blockPos.getY() + randomSource.nextDouble();
                double f = (double) blockPos.getZ() + randomSource.nextDouble();
                level.addParticle(ParticleTypes.ELECTRIC_SPARK, d, e, f, (double) 0.0F, (double) 0.0F, (double) 0.0F);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
        BlockPos pos = blockPos.relative(blockState.getValue(FACING).getOpposite());
        if (level.getBlockState(pos).isAir()) level.destroyBlock(blockPos, true);
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return true;
    }

    public int getMaxAge(){
        return 0;
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
            boolean test = CommonClass.isIn(getBaseForOutcrops(blockState), serverLevel.getBlockState(blockPos.relative(blockState.getValue(FACING).getOpposite())));
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

    protected static final VoxelShape NORTH_SHAPE = Block.box(4.0, 4.0, 4.0, 12.0, 12.0, 16.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(4.0, 4.0, 0.0, 12.0, 12.0, 12.0);
    protected static final VoxelShape EAST_SHAPE = Block.box(0.0, 4.0, 4.0, 12.0, 12.0, 12.0);
    protected static final VoxelShape WEST_SHAPE = Block.box(4.0, 4.0, 4.0, 16.0, 12.0, 12.0);
    protected static final VoxelShape UP_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 12.0, 12.0);
    protected static final VoxelShape DOWN_SHAPE = Block.box(4.0, 4.0, 4.0, 12.0, 16.0, 12.0);

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
            return switch ($$4) {
                case NORTH -> NORTH_SHAPE;
                case SOUTH -> SOUTH_SHAPE;
                case EAST -> EAST_SHAPE;
                case WEST -> WEST_SHAPE;
                case UP -> UP_SHAPE;
                default -> DOWN_SHAPE;
            };
        }
    }
}
